package pl.komponentowe.model;
import org.junit.jupiter.api.Test;
import pl.komponentowe.model.exceptions.SudokuNullPointerException;
import pl.komponentowe.model.exceptions.UnknownSudokuException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {
    @Test
    void testSetFieldValue() {
        SudokuField sudokuField = new SudokuField();
        assert(sudokuField.getFieldValue() == 0);
        sudokuField.setFieldValue(1);
        assert(sudokuField.getFieldValue() == 1);
        sudokuField.setFieldValue(2);
        assert(sudokuField.getFieldValue() == 2);
    }

    @Test
    void fieldValueListenerTest() {
        SudokuField sudokuField = new SudokuField();

        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        PropertyChangeListener listener = (PropertyChangeEvent e) -> {
            listenerCalled.set(true);
        };
        sudokuField.addFieldValueListener(listener);

        sudokuField.setFieldValue(1);
        assertTrue(listenerCalled.get());

        listenerCalled.set(false);
        sudokuField.removeFieldValueListener(listener);
        sudokuField.setFieldValue(2);
        assertFalse(listenerCalled.get());
    }

    @Test
    void fieldValueSetExceptionTest() {
        SudokuField sudokuField = new SudokuField();

        assertDoesNotThrow(() -> {
            sudokuField.setFieldValue(1);
        });
        assert(sudokuField.getFieldValue() == 1);

        assertThrows(UnknownSudokuException.class, () -> sudokuField.setFieldValue(12));
        assert(sudokuField.getFieldValue() == 1);

        assertThrows(UnknownSudokuException.class, () -> sudokuField.setFieldValue(-1));
        assert(sudokuField.getFieldValue() == 1);
    }

    @Test
    void toStringTest() {
        SudokuField sudokuField = new SudokuField(5);
        assert(sudokuField.toString().equals("SudokuField{value=5}"));
    }

    @Test
    void fieldObjectIsEqualToItself() {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = sudokuField1;
        assert(sudokuField1.equals(sudokuField2));
    }

    @Test
    void fieldObjectIsNotEqualToOtherClassObj() {
        SudokuField sudokuField = new SudokuField(5);
        Object obj = new Object();
        assert(!sudokuField.equals(obj));
    }

    @Test
    void fieldObjectIsNotEqualToNull() {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = null;
        assert(!sudokuField1.equals(sudokuField2));
    }

    @Test
    void fieldsWithEqualValueAreEqual() {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = new SudokuField(5);
        assert(sudokuField1.equals(sudokuField2));
        assert(sudokuField2.equals(sudokuField1));
    }

    @Test
    void fieldsWithDifferentValuesAreNotEqual() {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = new SudokuField(6);
        assert(!sudokuField1.equals(sudokuField2));
        assert(!sudokuField2.equals(sudokuField1));
    }

    @Test
    void equalFieldsHaveEqualHashCodes() {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = new SudokuField(5);
        assert(sudokuField1.equals(sudokuField2));
        assert(sudokuField1.hashCode() == sudokuField2.hashCode());
    }

    @Test
    void cloneSudokuField() throws CloneNotSupportedException {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = sudokuField1.clone();
        assert(sudokuField1.equals(sudokuField2));
        sudokuField1.setFieldValue(1);
        assert(sudokuField1.getFieldValue() != sudokuField2.getFieldValue());
    }

    @Test
    void compareSudokuFields() {
        SudokuField sudokuField1 = new SudokuField(5);
        SudokuField sudokuField2 = new SudokuField(3);
        SudokuField sudokuField3 = new SudokuField(5);
        assert(sudokuField1.compareTo(sudokuField2) == 1);
        assert(sudokuField1.compareTo(sudokuField3) == 0);
        assert(sudokuField2.compareTo(sudokuField1) == -1);
        assertThrows(SudokuNullPointerException.class, () -> sudokuField1.compareTo(null));
    }
}
