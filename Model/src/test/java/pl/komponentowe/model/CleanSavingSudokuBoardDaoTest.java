package pl.komponentowe.model;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CleanSavingSudokuBoardDaoTest {

    private static SudokuSolver solver = new BacktrackingSudokuSolver();
    private static SudokuBoard testboard = new SudokuBoard(solver);
    private static SudokuBoard cleanboard = new SudokuBoard(solver);
    private static Dao<SudokuBoard> dao;
    @AutoClose
    private static Dao<SudokuBoard> decoratedDao;
    private static final String storageDir = "board-store";

    @BeforeEach
    public void clean() {
        try {
            FileUtils.deleteDirectory(new File(storageDir));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeAll
    public static void setObjects() {
        solver = new BacktrackingSudokuSolver();
        testboard = new SudokuBoard(solver);
        cleanboard = new SudokuBoard(solver);
        testboard.solveGame();
        cleanboard.solveGame();
        dao = SudokuBoardDaoFactory.getFileDao(storageDir);
        decoratedDao = new CleanSavingSudokuBoardDao(dao, cleanboard);
    }

    @Test
    public void testWriteSudokuBoard() {
        Path storagePath = Paths.get(storageDir, "SudokuBoard1");
        Path cleanPath = Paths.get(storageDir, "SudokuBoard1_clean");
        File f = new File(storagePath.toString());
        File f2 = new File(cleanPath.toString());
        decoratedDao.write("SudokuBoard1", testboard);
        assert(f.exists());
        assert(f2.exists());
    }

    @Test
    public void testReadSudokuBoard() {
        decoratedDao.write("SudokuBoard2", testboard);
        SudokuBoard testboard2 = decoratedDao.read("SudokuBoard2");
        assert(testboard.equals(testboard2));
    }

    @Test
    public void testNamesSudokuBoard() {
        assert(decoratedDao.names().isEmpty());
        decoratedDao.write("SudokuBoard3", testboard);
        assert(decoratedDao.names().contains("SudokuBoard3"));
    }
}
