package pl.komponentowe.model;

/**
 * Repository for storing and retrieving a Sudoku board instance.
 */
public class SudokuBoardRepository {
    private SudokuBoard board;

    /**
     * Constructor.
     * @param board The Sudoku board to be stored
     */
    public SudokuBoardRepository(SudokuBoard board) {
        this.board = board;
    }

    /**
     * Creates a copy of the stored Sudoku board.
     * @return A clone of the stored SudokuBoard
     */
    SudokuBoard createInstanceOfSudokuBoard() {
        return board.clone();
    }
}
