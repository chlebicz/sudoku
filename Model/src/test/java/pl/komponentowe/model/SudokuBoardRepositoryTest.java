package pl.komponentowe.model;

import org.junit.jupiter.api.Test;

public class SudokuBoardRepositoryTest {
    @Test
    void createInstanceReturnsClone() throws CloneNotSupportedException {
        SudokuBoard templateBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        templateBoard.set(0, 0, 2);
        SudokuBoardRepository repository = new SudokuBoardRepository(templateBoard);
        SudokuBoard cloneBoard = repository.createInstanceOfSudokuBoard();
        assert(cloneBoard.equals(templateBoard));
        cloneBoard.set(0, 0, 7);
        assert(!cloneBoard.equals(templateBoard));
    }
}
