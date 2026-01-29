package pl.komponentowe.model.exceptions;

import java.util.ResourceBundle;

/**
 * Base exception class for the Sudoku application.
 * Supports localized error messages.
 */
public class SudokuException extends RuntimeException {
    private static ResourceBundle errorMessages;

    /**
     * Sets the resource bundle for error messages.
     * @param errorMessages ResourceBundle containing error messages
     */
    public static void setErrorMessages(ResourceBundle errorMessages) {
        SudokuException.errorMessages = errorMessages;
    }

    /**
     * Constructor.
     * @param message Error message key
     */
    public SudokuException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message Error message key
     * @param cause Cause of the exception
     */
    public SudokuException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getLocalizedMessage() {
        String message = getMessage();

        if (errorMessages != null && errorMessages.containsKey(message)) {
            return errorMessages.getString(message);
        }

        return message;
    }
}
