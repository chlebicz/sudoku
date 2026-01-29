package pl.komponentowe.model;

import com.google.common.base.MoreObjects;
import pl.komponentowe.model.exceptions.CloneFailException;
import pl.komponentowe.model.exceptions.OtherIoException;
import pl.komponentowe.model.exceptions.SudokuNullPointerException;
import pl.komponentowe.model.exceptions.UnknownSudokuException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * Class representing a single field on the Sudoku board.
 */
public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    @Serial
    private static final long serialVersionUID = 0L;

    private int value;
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Serial
    private void readObject(ObjectInputStream ois) throws UnknownSudokuException, OtherIoException {
        try {
            ois.defaultReadObject();
            support = new PropertyChangeSupport(this);
        } catch (ClassNotFoundException e) {
            throw new UnknownSudokuException("classNotFound", e);
        } catch (IOException e) {
            throw new OtherIoException("IOException", e);
        }
    }

    @Override
    public SudokuField clone() {
        try {
            SudokuField field = (SudokuField) super.clone();
            field.support = new PropertyChangeSupport(this);
            return field;
        } catch (CloneNotSupportedException e) {
            throw new CloneFailException("sudokuFieldCloneFail", e);
        }

    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        helper.add("value", value);
        return helper.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuField otherField = (SudokuField) o;
        return value == otherField.getFieldValue();
    }

    @Override
    public int hashCode() {
        return value;
    }

    /**
     * No-argument constructor initializing the value to 0.
     */
    public SudokuField() {
        value = 0;
    }

    /**
     * Constructor initializing the field with a specific value.
     * @param fieldValue Initial value of the field
     */
    public SudokuField(int fieldValue) {
        this.value = fieldValue;
    }

    /**
     * Returns the current value of the field.
     * @return Field value
     */
    public int getFieldValue() {
        return value;
    }

    /**
     * Sets a new value for the field.
     * Notifies listeners about the change.
     * @param value New value (0-9)
     * @throws UnknownSudokuException if value is not in range 0-9
     */
    public void setFieldValue(int value) {
        if (value < 0 || value > 9) {
            throw new UnknownSudokuException("wrongFieldValue");
        }
        int previous = this.value;
        this.value = value;
        support.firePropertyChange("fieldValue", previous, this.value);
    }

    /**
     * Adds a property change listener for the field value.
     * @param listener The listener to add
     */
    public void addFieldValueListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     * @param listener The listener to remove
     */
    public void removeFieldValueListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

    @Override
    public int compareTo(SudokuField o) {
        if (o == null) {
            throw new SudokuNullPointerException("nullPointer");
        }
        return Integer.compare(this.value, o.value);
    }
}