package pl.komponentowe.model;

import org.junit.jupiter.api.Test;

/**
 * Test class for SudokuBoard.
 */
class SudokuBoardTest {
    /**
     * Verifies the correctness of SudokuBoard.set(x, y, value).
     */
    @Test
    void testSetXY() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(5, 4, 7);
        assert(board.get(5, 4) == 7);
    }

    /**
     * Verifies the correctness of SudokuBoard.set(index, value).
     */
    @Test
    void testSetIndex() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(18, 9);
        assert(board.get(18) == 9);
    }

    @Test
    void testGetRow() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(2, 0, 5);
        SudokuFieldContainer container = board.getRow(0);
        assert(container.getField(2).getFieldValue() == 5);
    }

    @Test
    void testGetColumn() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(3, 5, 7);
        SudokuFieldContainer container = board.getColumn(3);
        assert(container.getField(5).getFieldValue() == 7);
    }

    @Test
    void testGetBox() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(3, 4, 5);
        SudokuFieldContainer container = board.getBox(3, 4);
        assert(container.getField(3).getFieldValue() == 5);
    }

    @Test
    void testToString() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.set(0, 0, 5);
        board.set(1, 0, 6);
        String result = board.toString();
        assert(result.startsWith("SudokuBoard{board=[SudokuField{value=5}, SudokuField{value=6}"));
    }

    @Test
    void boardObjectIsEqualToItself() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);
        SudokuBoard board2 = board1;
        assert(board1.equals(board2));
    }

    @Test
    void boardIsNotEqualToNull() {
        SudokuBoard board1 =  new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard board2 = null;
        assert(!board1.equals(board2));
    }

    @Test
    void boardIsNotEqualToDifferentClassObj() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        Object obj = new Object();
        assert(!board.equals(obj));
    }

    @Test
    void boardsWithEqualFieldsAreEqual() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);

        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.set(0, 0, 5);
        board2.set(1, 0, 6);

        assert(board1.equals(board2));
    }

    @Test
    void boardsWithDifferentFieldsAreNotEqual() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);

        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.set(0, 0, 5);
        board2.set(1, 0, 7);

        assert(!board1.equals(board2));
    }

    @Test
    void equalBoardsHaveEqualHashCodes() {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 5);
        board1.set(1, 0, 6);

        SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.set(0, 0, 5);
        board2.set(1, 0, 6);

        assert(board1.equals(board2));
        assert(board1.hashCode() == board2.hashCode());
    }

    @Test
    void cloneSudokuBoard() throws CloneNotSupportedException {
        SudokuBoard board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.set(0, 0, 4);
        SudokuBoard board2 = board1.clone();
        assert(board1.equals(board2));
        board2.set(0, 0, 5);
        assert(board1.get(0, 0) != board2.get(0, 0));
    }
}