package pl.komponentowe.model;

import java.io.Serializable;

/**
 * Interface representing a class that solves Sudoku.
 */
public interface SudokuSolver extends Serializable {
    /**
     * Fills the given Sudoku board according to the game rules.
     * @param board Board to be filled.
     */
    void solve(SudokuBoard board);
}
