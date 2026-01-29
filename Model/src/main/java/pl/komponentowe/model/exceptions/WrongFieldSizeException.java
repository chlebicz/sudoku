package pl.komponentowe.model.exceptions;

/**
 * Exception thrown when the field size is incorrect.
 */
public class WrongFieldSizeException extends SudokuException {

    /**
     * Constructor.
     * @param message Error message key
     */
    public WrongFieldSizeException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public WrongFieldSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
