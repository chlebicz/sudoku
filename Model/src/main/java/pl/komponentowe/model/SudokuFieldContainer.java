package pl.komponentowe.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import pl.komponentowe.model.exceptions.CloneFailException;
import pl.komponentowe.model.exceptions.OtherIoException;
import pl.komponentowe.model.exceptions.UnknownSudokuException;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class for containers of Sudoku fields (Row, Column, Box).
 */
public class SudokuFieldContainer implements Serializable, Cloneable {
    @Override
    public SudokuFieldContainer clone() throws CloneFailException {
        try {
            SudokuFieldContainer containerCopy = (SudokuFieldContainer) super.clone();

            List<SudokuField> fieldsCopy = new ArrayList<>();
            for (SudokuField field : this.fields) {
                fieldsCopy.add(field.clone());
            }
            containerCopy.fields = fieldsCopy;

            containerCopy.addListeners();

            return containerCopy;
        } catch (CloneNotSupportedException e) {
            throw new CloneFailException("sudokuFieldContainerCloneFail", e);
        }
    }

    @Serial
    private static final long serialVersionUID = 0L;

    private List<SudokuField> fields;

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        helper.add("fields", fields);
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

        SudokuFieldContainer otherContainer = (SudokuFieldContainer) o;
        return this.fields.equals(otherContainer.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }

    /**
     * Returns the field at the specified index within the container.
     * @param index Index of the field
     * @return SudokuField object
     */
    public SudokuField getField(int index) {
        return fields.get(index);
    }

    /**
     * Constructor.
     * @param fields List of fields belonging to this container
     * @throws UnknownSudokuException if the number of fields is not 9
     */
    public SudokuFieldContainer(List<SudokuField> fields) throws UnknownSudokuException {
        if (fields.size() != 9) {
            throw new UnknownSudokuException("wrongNumberOfFields");
        }

        this.fields = fields;
        addListeners();
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws UnknownSudokuException, OtherIoException {
        try {
            ois.defaultReadObject();
            addListeners();
        } catch (ClassNotFoundException e) {
            throw new UnknownSudokuException("classNotFound", e);
        } catch (IOException e) {
            throw new OtherIoException("IOException", e);
        }
    }

    private void addListeners() {
        for (SudokuField field : fields) {
            field.addFieldValueListener((PropertyChangeEvent e) -> {
                if ((int) e.getNewValue() == 0) {
                    return;
                }

                if (!verify()) {
                    field.setFieldValue((Integer) e.getOldValue());
                }
            });
        }
    }

    /**
     * Verifies if the container (row, column, or box) contains valid unique numbers.
     * Zeros are ignored as they represent empty fields.
     * @return true if the container is valid (no duplicates), false otherwise
     */
    public boolean verify() {
        Set<Integer> uniqueNumbers = new HashSet<>();

        for (SudokuField sudokuField : fields) {
            if (sudokuField.getFieldValue() == 0) {
                continue;
            }
            if (uniqueNumbers.contains(sudokuField.getFieldValue())) {
                return false;
            }
            uniqueNumbers.add(sudokuField.getFieldValue());
        }

        return true;
    }
}
