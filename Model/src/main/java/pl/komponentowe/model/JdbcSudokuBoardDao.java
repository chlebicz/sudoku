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
        String boardInsertSql = "INSERT INTO boards (board_name) VALUES (?)";
        String fieldsInsertSql = "INSERT INTO board_fields (board_id, index, value) VALUES (?, ?, ?)";

        try {
            // Transaction start
            connection.setAutoCommit(false);

            int boardId = 0;
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
                    int value = board.get(index);
                    fieldsStatement.setInt(3, value);

                    fieldsStatement.addBatch();
                }

                fieldsStatement.executeBatch();
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
