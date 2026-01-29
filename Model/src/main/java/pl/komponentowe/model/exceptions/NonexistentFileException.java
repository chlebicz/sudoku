package pl.komponentowe.model.exceptions;

/**
 * Exception thrown when a file does not exist.
 */
public class NonexistentFileException extends DaoException {

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public NonexistentFileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * @param message Error message key
     */
    public NonexistentFileException(String message) {
        super(message);
    }
}
