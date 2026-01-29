package pl.komponentowe.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.exceptions.DaoIoException;
import pl.komponentowe.model.exceptions.NonexistentFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * DAO implementation for storing Sudoku boards in files.
 */
public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    /**
     * Directory where the files are stored.
     */
    public String storageDir;

    /**
     * Constructor.
     * @param storageDir Directory path for storage
     */
    public FileSudokuBoardDao(String storageDir) {
         this.storageDir = storageDir;
    }

    @Override
    public List<String> names() {
        Path storagePath = Paths.get(storageDir);
        if (!Files.exists(storagePath)) {
            try {
                // Ensure directory exists even if we just return empty list
                Files.createDirectories(storagePath);
                return Collections.emptyList();
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }

        try (Stream<Path> stream = Files.list(storagePath)) {
            return stream
                .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    Logger logger = LoggerFactory.getLogger(FileSudokuBoardDao.class);

    @Override
    public SudokuBoard read(String name) throws NonexistentFileException {
        Path filePath = Paths.get(storageDir, name);

        try (InputStream fis = Files.newInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            return (SudokuBoard) obj;
        } catch (NoSuchFileException e) {
            throw new NonexistentFileException("noSuchFile", e);
        } catch (Exception e) {
            logger.error("Unknown error occurred when reading Sudoku board");
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void write(String name, SudokuBoard obj) throws DaoIoException {
        Path storagePath = Paths.get(storageDir);

        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
            } catch (IOException e) {
                throw new DaoIoException("writeFailed", e);
            }
        }

        Path filePath = storagePath.resolve(name);
        try (OutputStream fos = Files.newOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new DaoIoException("writeFailed", e);
        }
    }

    @Override
    public void close() {

    }
}
