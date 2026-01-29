package pl.komponentowe.model.exceptions;

/**
 * Exception thrown for unknown Sudoku related errors.
 */
public class UnknownSudokuException extends SudokuException {

    /**
     * Constructor.
     * @param message Error message key
     */
    public UnknownSudokuException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public UnknownSudokuException(String message, Throwable cause) {
        super(message, cause);
    }
}
