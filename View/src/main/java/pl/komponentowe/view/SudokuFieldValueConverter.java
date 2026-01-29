package pl.komponentowe.view;

import javafx.util.StringConverter;
import pl.komponentowe.model.exceptions.SudokuNullPointerException;

/**
 * Converter for transforming Sudoku field numeric values to strings and vice-versa.
 */
public class SudokuFieldValueConverter extends StringConverter<Number> {
    /**
     * Converts a Number to a String.
     * @param value The number value
     * @return String representation, empty if 0
     * @throws SudokuNullPointerException if value is null
     */
    @Override
    public String toString(Number value) {
        if (value == null) {
            throw new SudokuNullPointerException("unexpectedNullValue");
        }

        if (value.intValue() == 0) {
            return "";
        }

        return value.toString();
    }

    /**
     * Converts a String to a Number.
     * @param value The string value
     * @return Number representation, 0 if empty
     * @throws NumberFormatException if string is not a valid integer
     * @throws SudokuNullPointerException if value is null
     */
    @Override
    public Number fromString(String value) throws NumberFormatException {
        if (value == null) {
            throw new SudokuNullPointerException("unexpectedNullValue");
        }

        if (value.trim().isEmpty()) {
            return 0;
        }

        return Integer.parseInt(value);
    }
}
