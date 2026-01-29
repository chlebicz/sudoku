package pl.komponentowe.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import pl.komponentowe.model.exceptions.CloneFailException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Class representing a Sudoku board.
 */
public class SudokuBoard implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 0L;

    private SudokuField[] board = new SudokuField[81];

    private SudokuSolver sudokuSolver;

    private SudokuRow[] rows = new SudokuRow[9];
    private SudokuColumn[] columns = new SudokuColumn[9];
    private SudokuBox[] boxes = new SudokuBox[9];

    @Override
    public SudokuBoard clone() throws CloneFailException {
        try {
            SudokuBoard sudokuBoardCopy = (SudokuBoard) super.clone();

            sudokuBoardCopy.board = new SudokuField[81];
            for (int i = 0; i < this.board.length; i++) {
                sudokuBoardCopy.board[i] = this.board[i].clone();
            }

            sudokuBoardCopy.rows = new SudokuRow[9];
            for (int y = 0; y < sudokuBoardCopy.rows.length; y++) {
                sudokuBoardCopy.rows[y] = (SudokuRow) this.rows[y].clone();
            }

            sudokuBoardCopy.columns = new SudokuColumn[9];
            for (int x = 0; x < sudokuBoardCopy.columns.length; x++) {
                sudokuBoardCopy.columns[x] = (SudokuColumn) this.columns[x].clone();
            }

            sudokuBoardCopy.boxes = new SudokuBox[9];
            for (int i = 0; i < sudokuBoardCopy.boxes.length; i++) {
                sudokuBoardCopy.boxes[i] = (SudokuBox) this.boxes[i].clone();
            }

            return sudokuBoardCopy;
        } catch (CloneNotSupportedException e) {
            throw new CloneFailException("sudokuBoardCloneFail", e);
        }
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        helper.add("board", board);
        return helper.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuBoard otherBoard = (SudokuBoard) o;
        return Arrays.equals(otherBoard.board,  board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }

    /**
     * Constructor.
     * @param sudokuSolver SudokuSolver for the board
     */
    public SudokuBoard(SudokuSolver sudokuSolver) {
        this.sudokuSolver = sudokuSolver;
        for (int i = 0; i < board.length; i++) {
            board[i] = new SudokuField();
        }

        for (int y = 0; y < rows.length; y++) {
            rows[y] = new SudokuRow(this, y);
        }

        for (int x = 0; x < columns.length; x++) {
            columns[x] = new SudokuColumn(this, x);
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                boxes[y * 3 + x] = new SudokuBox(this, x * 3, y * 3);
            }
        }
    }

    /**
     * Calculates the row of the field with the given index.
     * @param index Field index
     * @return Row number
     */
    public int getIndexRow(int index) {
        return index / 9;
    }

    /**
     * Calculates the column of the field with the given index.
     * @param index Field index
     * @return Column number
     */
    public int getIndexColumn(int index) {
        return index % 9;
    }

    /**
     * Generates a random board meeting Sudoku rules.
     */
    public void solveGame() {
        sudokuSolver.solve(this);
    }

    /**
     * Returns the value on the board at the given coordinates.
     * @param x Column number
     * @param y Row number
     * @return The number located at the specified row and column
     */
    public int get(int x, int y) {
        return getField(x, y).getFieldValue();
    }

    /**
     * Returns the value on the board at the given index.
     * @param index Field index
     * @return The number located at the specified index
     */
    public int get(int index) {
        return board[index].getFieldValue();
    }

    /**
     * Returns the SudokuField at the given coordinates.
     * @param x Column number
     * @param y Row number
     * @return The SudokuField object
     */
    public SudokuField getField(int x, int y) {
        return board[9 * y + x];
    }

    /**
     * Sets the value at the given coordinates on the board.
     * @param x Column number
     * @param y Row number
     * @param value The number to insert
     */
    public void set(int x, int y, int value) {
        board[9 * y + x].setFieldValue(value);
    }

    /**
     * Sets the value at the given index on the board.
     * @param index Field index
     * @param value The number to insert
     */
    public void set(int index, int value) {
        board[index].setFieldValue(value);
    }

    /**
     * Clears the entire board (sets all fields to zero).
     */
    public void clear() {
        for (int i = 0; i < board.length; i++) {
            board[i] = new SudokuField();
        }
    }

    /**
     * Returns the row at the given index.
     * @param y Row index
     * @return The SudokuFieldContainer representing the row
     */
    public SudokuFieldContainer getRow(int y) {
        return rows[y];
    }

    /**
     * Returns the column at the given index.
     * @param x Column index
     * @return The SudokuFieldContainer representing the column
     */
    public SudokuFieldContainer getColumn(int x) {
        return columns[x];
    }

    /**
     * Returns the box (3x3 square) covering the given coordinates.
     * @param x Column coordinate
     * @param y Row coordinate
     * @return The SudokuFieldContainer representing the box
     */
    public SudokuFieldContainer getBox(int x, int y) {
        return boxes[y / 3 * 3 + x / 3];
    }
}
