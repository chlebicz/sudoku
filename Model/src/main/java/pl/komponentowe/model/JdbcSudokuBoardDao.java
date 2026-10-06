package pl.komponentowe.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.exceptions.DaoException;
import pl.komponentowe.model.exceptions.NonexistentFileException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for storing Sudoku boards in a JDBC-compliant database.
 */
public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {
    private final Connection connection;

    /**
     * Constructor that initializes the database connection using environment variables.
     * @throws DaoException if environment variables are missing or connection fails
     */
    public JdbcSudokuBoardDao() {
        String url = DatabaseInit.getPostgresUrl();
        String user = DatabaseInit.getPostgresUsername();
        String password = DatabaseInit.getPostgresPassword();

        if (url == null || user == null || password == null) {
            throw new DaoException("noDbEnv");
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DaoException("dbConnError", e);
        }
    }

    @Override
    public SudokuBoard read(String name) {
        String sql = "SELECT f.index, f.value "
            + "FROM boards b "
            + "JOIN board_fields f ON b.id = f.board_id "
            + "WHERE b.board_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet rs = statement.executeQuery()) {
                SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());

                int counter = 0;
                while (rs.next()) {
                    int index = rs.getInt("index");
                    int value = rs.getInt("value");
                    board.set(index, value);
                    counter++;
                }

                if (counter == 0) {
                    throw new NonexistentFileException("noSuchFile");
                }

                if (counter != 81) {
                    throw new DaoException("errorReadingBoard");
                }

                return board;
            }
        } catch (SQLException e) {
            throw new DaoException("unknownDbError", e);
        }
    }

    private static Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDao.class);

    @Override
    public void write(String name, SudokuBoard board) {
        String checkExistingSql = "SELECT id, version FROM boards WHERE board_name = ?";
        String updateBoardSql = "UPDATE boards SET version = version + 1 WHERE id = ? AND version = ?";
        String updateFieldsSql = "UPDATE board_fields SET value = ? WHERE board_id = ? AND index = ?";
        String boardInsertSql = "INSERT INTO boards (board_name, version) VALUES (?, 0)";
        String fieldsInsertSql = "INSERT INTO board_fields (board_id, index, value) VALUES (?, ?, ?)";

        try {
            // Transaction start and Isolation level
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);

            int boardId = -1;
            int currentVersion = -1;

            try (PreparedStatement checkStatement = connection.prepareStatement(checkExistingSql)) {
                checkStatement.setString(1, name);
                try (ResultSet rs = checkStatement.executeQuery()) {
                    if (rs.next()) {
                        boardId = rs.getInt("id");
                        currentVersion = rs.getInt("version");
                    }
                }
            }

            if (boardId != -1) {
                // UPDATE EXISTING WITH OPTIMISTIC LOCKING
                try (PreparedStatement updateBoardStatement = connection.prepareStatement(updateBoardSql)) {
                    updateBoardStatement.setInt(1, boardId);
                    updateBoardStatement.setInt(2, currentVersion);
                    int updatedRows = updateBoardStatement.executeUpdate();

                    if (updatedRows == 0) {
                        throw new DaoException("optimisticLockException");
                    }
                }

                try (PreparedStatement updateFieldsStatement = connection.prepareStatement(updateFieldsSql)) {
                    for (int index = 0; index < 81; ++index) {
                        updateFieldsStatement.setInt(1, board.get(index));
                        updateFieldsStatement.setInt(2, boardId);
                        updateFieldsStatement.setInt(3, index);
                        updateFieldsStatement.addBatch();
                    }
                    updateFieldsStatement.executeBatch();
                }
            } else {
                // INSERT NEW
                try (PreparedStatement boardStatement = connection.prepareStatement(
                    boardInsertSql, Statement.RETURN_GENERATED_KEYS
                )) {
                    boardStatement.setString(1, name);
                    boardStatement.executeUpdate();

                    try (ResultSet rs = boardStatement.getGeneratedKeys()) {
                        if (rs.next()) {
                            boardId = rs.getInt(1);
                        } else {
                            throw new DaoException("unknownDbError");
                        }
                    }
                }

                try (PreparedStatement fieldsStatement = connection.prepareStatement(fieldsInsertSql)) {
                    fieldsStatement.setInt(1, boardId);

                    for (int index = 0; index < 81; ++index) {
                        fieldsStatement.setInt(2, index);
                        fieldsStatement.setInt(3, board.get(index));
                        fieldsStatement.addBatch();
                    }
                    fieldsStatement.executeBatch();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DaoException("unknownDbException", ex);
            }

            throw new DaoException("unknownDbError", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("fatal error when trying to setAutoCommit");
            }
        }
    }

    @Override
    public List<String> names() {
        String sql = "SELECT board_name FROM boards";
        List<String> result = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    String name = rs.getString("board_name");
                    result.add(name);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("unknownDbError", e);
        }

        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
