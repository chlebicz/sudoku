package pl.komponentowe.model.exceptions;

/**
 * Exception thrown when an IO error occurs in DAO operations.
 */
public class DaoIoException extends DaoException {

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public DaoIoException(String message, Throwable cause) {
        super(message, cause);
    }
}
