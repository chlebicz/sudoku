package pl.komponentowe.model;

import org.junit.jupiter.api.Test;
import pl.komponentowe.model.exceptions.UnknownSudokuException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldContainerTest {
    @Test
    void verifyReturnsFalseWhenNumbersRepeat() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField(1);
        }

        SudokuFieldContainer container = new SudokuFieldContainer(List.of(fields));
        assertFalse(container.verify());
    }

    @Test
    void verifyReturnsTrueWhenNumbersDontRepeat() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField(i + 1);
        }

        SudokuFieldContainer container = new SudokuFieldContainer(List.of(fields));
        assertTrue(container.verify());
    }

    @Test
    void verifyReturnsTrueWhenOnlyZeroesRepeat() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 8; i++) {
            fields[i] = new SudokuField(0);
        }
        fields[8] = new SudokuField(8);

        SudokuFieldContainer container = new SudokuFieldContainer(List.of(fields));
        assertTrue(container.verify());
    }

    @Test
    void autoVerifyUndoesIncorrectFieldValue() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(0, 0, 1);
        board.set(0, 1, 1);
        assert(board.get(0, 1) == 0);
    }

    @Test
    void illegalSizeExceptionTest() {
        SudokuField[] fields = new SudokuField[8];
        for (int i = 0; i < 8; i++) {
            fields[i] = new SudokuField(0);
        }

        assertThrows(
            UnknownSudokuException.class,
            () -> new SudokuFieldContainer(List.of(fields))
        );
    }

    @Test
    void toStringTest() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(0, 0, 1);
        board.set(1, 0, 2);

        SudokuFieldContainer container = board.getRow(0);
        assert(container.toString().startsWith("SudokuRow{fields=[SudokuField{value=1}, SudokuField{value=2},"));
    }

    @Test
    void fieldContainerIsEqualToItself() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuFieldContainer container1 = board.getRow(0);
        SudokuFieldContainer container2 = board.getRow(0);
        assert(container1.equals(container2));
    }

    @Test
    void fieldContainerIsNotEqualToOtherClassObj() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuFieldContainer container1 = board.getRow(0);
        Object obj = new Object();
        assert(!container1.equals(obj));
    }

    @Test
    void fieldContainerIsNotEqualToNull() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuFieldContainer container1 = board1.getRow(0);
        SudokuFieldContainer container2 = null;
        assert(!container1.equals(container2));
    }

    @Test
    void fieldContainersWithEqualFieldsAreEqual() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);

        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.set(0, 0, 5);
        board2.set(1, 0, 6);

        SudokuFieldContainer container1 = board1.getRow(0);
        SudokuFieldContainer container2 = board2.getRow(0);
        assert(container1.equals(container2));
        assert(container2.equals(container1));
    }

    @Test
    void fieldContainersWithDifferentFieldsAreNotEqual() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);

        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.set(0, 0, 5);
        board2.set(1, 0, 9);

        SudokuFieldContainer container1 = board1.getRow(0);
        SudokuFieldContainer container2 = board2.getRow(0);
        assert(!container1.equals(container2));
        assert(!container2.equals(container1));
    }

    @Test
    void equalFieldContainersHaveEqualHashCodes() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);

        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.set(0, 0, 5);
        board2.set(1, 0, 6);

        SudokuFieldContainer container1 = board1.getRow(0);
        SudokuFieldContainer container2 = board2.getRow(0);

        assert(container1.equals(container2));
        assert(container1.hashCode() == container2.hashCode());
    }

    @Test
    void differentContainerTypesAreNotEqual() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuFieldContainer container1 = board.getRow(0);
        SudokuFieldContainer container2 = board.getColumn(0);
        SudokuFieldContainer container3 = board.getBox(0, 0);
        assert(!container1.equals(container2));
        assert(!container1.equals(container3));
        assert(!container2.equals(container3));
    }

    @Test
    void cloneFieldContainer() throws CloneNotSupportedException {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        SudokuFieldContainer container1 = board1.getRow(0);
        SudokuFieldContainer container2 = container1.clone();
        assert(container1.equals(container2));
        SudokuField field = container2.getField(0);
        field.setFieldValue(3);
        assert(container1.getField(0).getFieldValue() !=
                container2.getField(0).getFieldValue());
        SudokuField field2 = container2.getField(1);
        field2.setFieldValue(3);
        assert (field.getFieldValue() != field2.getFieldValue());
    }
}
