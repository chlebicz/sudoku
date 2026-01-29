package pl.komponentowe.model;

import java.util.List;

/**
 * Class representing a row in the Sudoku board.
 */
public class SudokuRow extends SudokuFieldContainer {
    /**
     * Constructor.
     * @param board The Sudoku board
     * @param y Row index
     */
    public SudokuRow(SudokuBoard board, int y) {
        super(createFields(board, y));
    }

    private static List<SudokuField> createFields(SudokuBoard board, int y) {
        SudokuField[] fields = new SudokuField[9];
        for (int x = 0; x < 9; x++) {
            fields[x] = board.getField(x, y);
        }
        return List.of(fields);
    }
}
