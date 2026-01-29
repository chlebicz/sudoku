package pl.komponentowe.model;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SudokuBoardDaoFactoryTest {

    @BeforeEach
    public void clean() {
        try {
            FileUtils.deleteDirectory(new File("board-store"));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSudokuBoardDaoFactory() throws Exception {
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("board-store")) {
            assert(dao instanceof Dao);
            SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
            dao.write("SudokuBoard1", board);
            File f = new File("board-store");
            assert(f.exists());
        }
    }
}
