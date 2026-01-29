package pl.komponentowe.model;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import pl.komponentowe.model.exceptions.NonexistentFileException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSudokuBoardDaoTest {

    private static SudokuSolver solver = new BacktrackingSudokuSolver();
    private static SudokuBoard testboard = new SudokuBoard(solver);
    @AutoClose
    private static Dao<SudokuBoard> dao;
    private static final String storageDir = "board-store";

    @BeforeEach
    public void clean() {
        try {
            FileUtils.deleteDirectory(new File(storageDir));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeAll
    public static void setObjects() {
        solver = new BacktrackingSudokuSolver();
        testboard = new SudokuBoard(solver);
        testboard.solveGame();
        dao = SudokuBoardDaoFactory.getFileDao(storageDir);
    }

    @Test
    public void testWriteSudokuBoard() {
        Path storagePath = Paths.get(storageDir, "SudokuBoard1");
        File f = new File(storagePath.toString());
        dao.write("SudokuBoard1", testboard);
        assert(f.exists());
    }

    @Test
    public void testReadSudokuBoard() {
        assertThrows(
                NonexistentFileException.class,
                () -> dao.read("SudokuBoard2")
        );
        dao.write("SudokuBoard2", testboard);
        SudokuBoard testboard2 = dao.read("SudokuBoard2");
        assert(testboard.equals(testboard2));
    }

    @Test
    public void testNamesSudokuBoard() {
        assert(dao.names().isEmpty());
        dao.write("SudokuBoard3", testboard);
        assert(dao.names().contains("SudokuBoard3"));
    }
}
