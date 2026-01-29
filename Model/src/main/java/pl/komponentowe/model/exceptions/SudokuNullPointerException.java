package pl.komponentowe.model.exceptions;

/**
 * Exception serving as a wrapper for NullPointerException in the Sudoku context.
 */
public class SudokuNullPointerException extends SudokuException {

    /**
     * Constructor.
     * @param message Error message key
     */
    public SudokuNullPointerException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public SudokuNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }
}
