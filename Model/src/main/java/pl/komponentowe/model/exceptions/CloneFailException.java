package pl.komponentowe.model.exceptions;

/**
 * Exception thrown when cloning operations fail.
 */
public class CloneFailException extends SudokuException {

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public CloneFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
