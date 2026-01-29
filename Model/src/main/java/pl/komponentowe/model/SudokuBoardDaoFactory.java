package pl.komponentowe.model;

import pl.komponentowe.model.exceptions.DaoException;

/**
 * Factory class for creating DAO instances for SudokuBoard.
 */
public class SudokuBoardDaoFactory {
    /**
     * Creates a file-based DAO wrapped with clean state saving functionality.
     * @param storageDir The directory where files are stored
     * @param cleanBoard The initial clean board state
     * @return Dao instance
     */
    public static Dao<SudokuBoard> getCleanSavingFileDao(String storageDir, SudokuBoard cleanBoard) {
        return new CleanSavingSudokuBoardDao(getFileDao(storageDir), cleanBoard);
    }

    /**
     * Creates a JDBC-based DAO wrapped with clean state saving functionality.
     * @param cleanBoard The initial clean board state
     * @return Dao instance
     */
    public static Dao<SudokuBoard> getCleanSavingJdbcDao(SudokuBoard cleanBoard) {
        return new CleanSavingSudokuBoardDao(getJdbcDao(), cleanBoard);
    }

    /**
     * Creates a file-based DAO.
     * @param storageDir The directory where files are stored
     * @return Dao instance
     */
    public static Dao<SudokuBoard> getFileDao(String storageDir) {
        return new FileSudokuBoardDao(storageDir);
    }

    /**
     * Creates a JDBC-based DAO.
     * @return Dao instance
     */
    public static Dao<SudokuBoard> getJdbcDao() {
        return new JdbcSudokuBoardDao();
    }

    /**
     * Creates a JDBC-based DAO if DB connectivity is set up, and a default
     * file-based DAO otherwise.
     * @return Dao instance
     */
    public static Dao<SudokuBoard> getDao() {
        try {
            return getJdbcDao();
        } catch (DaoException e) {
            return getFileDao("board-store");
        }
    }

    /**
     * Creates a default DAO wrapped with clean state saving functionality.
     * @param cleanBoard The initial clean board state
     * @return Dao instance
     */
    public static Dao<SudokuBoard> getCleanSavingDao(SudokuBoard cleanBoard) {
        return new CleanSavingSudokuBoardDao(getDao(), cleanBoard);
    }
}
