package pl.komponentowe.model.exceptions;

/**
 * Exception thrown for other IO errors.
 */
public class OtherIoException extends SudokuException {

    /**
     * Constructor.
     * @param message Error message key
     */
    public OtherIoException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public OtherIoException(String message, Throwable cause) {
        super(message, cause);
    }
}
