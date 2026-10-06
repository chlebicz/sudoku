package pl.komponentowe.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SudokuBoardRepositoryTest {

    // Simple Mock Dao for testing Repository
    private static class MockDao implements Dao<SudokuBoard> {
        private SudokuBoard savedBoard;
        private String savedName;

        @Override
        public SudokuBoard read(String name) {
            if (name.equals(savedName)) {
                return savedBoard;
            }
            return null;
        }

        @Override
        public void write(String name, SudokuBoard obj) {
            this.savedName = name;
            this.savedBoard = obj;
        }

        @Override
        public List<String> names() {
            return List.of(savedName);
        }

        @Override
        public void close() throws Exception {
            // Nothing to close
        }
    }

    @Test
    void saveAndReadBoard() throws Exception {
        SudokuBoard templateBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        templateBoard.set(0, 0, 2);

        MockDao dao = new MockDao();
        SudokuBoardRepository repository = new SudokuBoardRepository(dao);

        repository.save("testBoard", templateBoard);
        SudokuBoard readBoard = repository.read("testBoard");

        assertEquals(templateBoard, readBoard);
    }

    @Test
    void unsupportedOperationsThrowException() {
        MockDao dao = new MockDao();
        SudokuBoardRepository repository = new SudokuBoardRepository(dao);
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());

        assertThrows(UnsupportedOperationException.class, () -> repository.create(board));
        assertThrows(UnsupportedOperationException.class, () -> repository.update(board));
        assertThrows(UnsupportedOperationException.class, () -> repository.delete("testBoard"));
    }
}
