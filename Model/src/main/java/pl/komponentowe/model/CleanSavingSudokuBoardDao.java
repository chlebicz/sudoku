package pl.komponentowe.model;

import pl.komponentowe.model.exceptions.UnknownSudokuException;

import java.util.List;

/**
 * A decorator for Dao that saves the clean state of the board along with the board itself.
 */
public class CleanSavingSudokuBoardDao implements Dao<SudokuBoard> {

    private final Dao<SudokuBoard> dao;
    private final SudokuBoard cleanBoard;

    /**
     * Constructor.
     * @param dao The underlying DAO to be decorated
     * @param cleanBoard The clean version of the SudokuBoard
     */
    public CleanSavingSudokuBoardDao(Dao<SudokuBoard> dao, SudokuBoard cleanBoard) {
        this.dao = dao;
        this.cleanBoard = cleanBoard;
    }

    @Override
    public SudokuBoard read(String name) {
        return dao.read(name);
    }

    /**
     * Writes the board and its clean version (suffixed with "_clean").
     * @param name Name under which the object will be saved
     * @param obj The object to save
     */
    @Override
    public void write(String name, SudokuBoard obj) {
        dao.write(name + "_clean", cleanBoard);
        dao.write(name, obj);
    }

    @Override
    public List<String> names() {
        return dao.names();
    }

    @Override
    public void close() throws UnknownSudokuException {
        try {
            dao.close();
        } catch (Exception e) {
            throw new UnknownSudokuException("closeError", e);
        }
    }
}
