package pl.komponentowe.model.exceptions;

/**
 * Exception thrown when a DAO operation fails.
 */
public class DaoException extends SudokuException {

    /**
     * Constructor.
     * @param message Error message key
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
