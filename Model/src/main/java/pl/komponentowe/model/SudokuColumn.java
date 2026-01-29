package pl.komponentowe.model;

import java.util.List;

/**
 * Class representing a column in the Sudoku board.
 */
public class SudokuColumn extends SudokuFieldContainer {
    /**
     * Constructor.
     * @param board The Sudoku board
     * @param x Column index
     */
    public SudokuColumn(SudokuBoard board, int x) {
        super(createFields(board, x));
    }

    private static List<SudokuField> createFields(SudokuBoard board, int x) {
        SudokuField[] fields = new SudokuField[9];
        for (int y = 0; y < 9; y++) {
            fields[y] = board.getField(x, y);
        }
        return List.of(fields);
    }
}