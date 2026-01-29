package pl.komponentowe.model;

import java.util.List;

/**
 * Class representing a 3x3 box in the Sudoku board.
 */
public class SudokuBox extends SudokuFieldContainer {
    /**
     * Constructor.
     * @param board The Sudoku board
     * @param x Column coordinate of the box start
     * @param y Row coordinate of the box start
     */
    public SudokuBox(SudokuBoard board, int x, int y) {
        super(createFields(board, x, y));
    }

    private static List<SudokuField> createFields(SudokuBoard board, int x, int y) {
        SudokuField[] fields = new SudokuField[9];
        int fieldCnt = 0;
        for (int i = x / 3 * 3; i < x / 3 * 3 + 3; i++) {
            for (int j = y / 3 * 3; j < y / 3 * 3 + 3; j++) {
                fields[fieldCnt] = board.getField(j, i);
                fieldCnt++;
            }
        }
        return List.of(fields);
    }
}
