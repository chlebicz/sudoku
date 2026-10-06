package pl.komponentowe.model;

import pl.komponentowe.model.exceptions.DaoException;
import pl.komponentowe.model.exceptions.UnknownSudokuException;

/**
 * Repository for storing and retrieving a Sudoku board instance using Dao.
 */
public class SudokuBoardRepository implements Repository<SudokuBoard> {
    private final Dao<SudokuBoard> dao;

    /**
     * Constructor.
     * @param dao The DAO adapter used for persistence
     */
    public SudokuBoardRepository(Dao<SudokuBoard> dao) {
        this.dao = dao;
    }

    @Override
    public SudokuBoard create(SudokuBoard board) {
        throw new UnsupportedOperationException("create is not supported without name, use dao.write");
    }

    /**
     * Saves the board under the specified name.
     * @param name The name of the board
     * @param board The board to save
     */
    public void save(String name, SudokuBoard board) {
        dao.write(name, board);
    }

    @Override
    public SudokuBoard read(String id) {
        return dao.read(id);
    }

    @Override
    public SudokuBoard update(SudokuBoard board) {
        throw new UnsupportedOperationException("update is not supported directly, use save with name");
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("delete is not supported by Dao currently");
    }
}
