package pl.komponentowe.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class implementing the Sudoku solving algorithm based on backtracking.
 */
public class BacktrackingSudokuSolver implements SudokuSolver {
    @Serial
    private static final long serialVersionUID = 0L;

    @Override
    public void solve(SudokuBoard board) {
        recursivelyFill(0, board);
    }

    private boolean isInRow(int index, int number, SudokuBoard board) {
        int row = board.getIndexRow(index);

        for (int i = 0; i < 9; ++i) {
            if (board.get(i, row) == number) {
                return true;
            }
        }

        return false;
    }

    private boolean isInColumn(int index, int number, SudokuBoard board) {
        int column = board.getIndexColumn(index);

        for (int i = 0; i < 9; ++i) {
            if (board.get(column, i) == number) {
                return true;
            }
        }

        return false;
    }

    private boolean isInBox(int index, int number, SudokuBoard board) {
        // coordinates of the field with the given index
        int fieldColumn = board.getIndexColumn(index);
        int fieldRow = board.getIndexRow(index);

        // horizontal and vertical index of the box the field belongs to
        int boxHorizontalIndex = fieldColumn / 3;
        int boxVerticalIndex = fieldRow / 3;

        // coordinates of the first field belonging to the determined box
        int firstColumn = boxHorizontalIndex * 3;
        int firstRow = boxVerticalIndex * 3;
        // and the last field in that box
        int lastColumn = firstColumn + 3;
        int lastRow = firstRow + 3;

        for (int row = firstRow; row < lastRow; ++row) {
            for (int column = firstColumn; column < lastColumn; ++column) {
                if (board.get(column, row) == number) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canPlace(int index, int number, SudokuBoard board) {
        return !isInRow(index, number, board) && !isInColumn(index, number, board)
            && !isInBox(index, number, board);
    }

    private boolean recursivelyFill(int startIndex,  SudokuBoard board) {
        if (startIndex >= 81) {
            return true;
        }

        // skip if the field is not empty
        if (board.get(startIndex) != 0) {
            return recursivelyFill(startIndex + 1, board);
        }

        List<Integer> seedRow;
        seedRow = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)
        );
        Collections.shuffle(seedRow);

        for (int option : seedRow) {
            if (!canPlace(startIndex, option, board)) {
                continue;
            }

            board.set(startIndex, option);
            if (recursivelyFill(startIndex + 1, board)) {
                return true;
            }
        }

        board.set(startIndex, 0);
        return false;
    }
}
