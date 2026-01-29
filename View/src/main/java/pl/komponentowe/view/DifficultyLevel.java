package pl.komponentowe.view;

import pl.komponentowe.model.SudokuBoard;

import java.util.Random;

/**
 * Enum representing the difficulty levels of the game.
 */
public enum DifficultyLevel {
    /**
     * Easy level.
     */
    EASY(10),
    /**
     * Medium level.
     */
    MEDIUM(20),
    /**
     * Hard level.
     */
    HARD(30);

    private final int fieldsToRemove;

    DifficultyLevel(int fieldsToRemove) {
        this.fieldsToRemove = fieldsToRemove;
    }

    /**
     * Removes a certain number of fields from the board based on difficulty.
     * @param board The Sudoku board to modify
     */
    public void removeFields(SudokuBoard board) {
        final int sudokuBoardSize = 81;

        int[] indexesToRemove = new Random()
            .ints(0, sudokuBoardSize)
            .distinct()
            .limit(fieldsToRemove)
            .toArray();

        for (int index : indexesToRemove) {
            board.set(index, 0);
        }
    }
}
