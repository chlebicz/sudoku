package pl.komponentowe.model;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.exceptions.DaoException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JdbcSudokuBoardDaoTest {
    @AutoClose
    private static Dao<SudokuBoard> dao;

    @AutoClose
    private static Connection connection;

    private static Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDaoTest.class);

    @BeforeAll
    public static void initialize() throws SQLException {
        try {
            dao = SudokuBoardDaoFactory.getJdbcDao();
        } catch (DaoException e) {
            Assumptions.abort(
                "Database connectivity is not set up correctly, skipping database-related tests"
            );
        }

        String url = DatabaseInit.getPostgresUrl();
        String user = DatabaseInit.getPostgresUsername();
        String password = DatabaseInit.getPostgresPassword();
        connection = DriverManager.getConnection(url, user, password);
    }

    @BeforeEach
    public void clean() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM boards WHERE board_name = 'SudokuBoard2'";
            statement.executeUpdate(sql);
        }
    }

    @Test
    public void testReadWriteSudokuBoard() {
        SudokuBoard testBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        testBoard.solveGame();
        dao.write("SudokuBoard2", testBoard);
        SudokuBoard fetchedBoard = dao.read("SudokuBoard2");
        assert(fetchedBoard != testBoard);
        assert(testBoard.equals(fetchedBoard));
    }

    @Test
    public void testSameSudokuBoardName() {
        SudokuBoard testBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        dao.write("SudokuBoard2", testBoard);

        SudokuBoard testBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());
        assertThrows(
            DaoException.class,
            () -> dao.write("SudokuBoard2", testBoard2)
        );
    }

    @Test
    public void testNamesSudokuBoard() {
        assert(!dao.names().contains("SudokuBoard2"));

        SudokuBoard testBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        dao.write("SudokuBoard2", testBoard);
        assert(dao.names().contains("SudokuBoard2"));
    }

    @Test
    public void writeTransactionTest() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "ALTER TABLE board_fields RENAME TO board_fields_test";
            statement.executeUpdate(sql);
        }

        SudokuBoard testBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        assertThrows(
            DaoException.class,
            () -> dao.write("SudokuBoard2", testBoard)
        );

        assert(!dao.names().contains("SudokuBoard2"));

        try (Statement statement = connection.createStatement()) {
            String sql = "ALTER TABLE board_fields_test RENAME TO board_fields";
            statement.executeUpdate(sql);
        }
    }

    @Test
    public void closeTest() throws Exception {
        Dao<SudokuBoard> testDao = SudokuBoardDaoFactory.getJdbcDao();

        Field connectionField = JdbcSudokuBoardDao.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        Connection conn = (Connection) connectionField.get(testDao);

        assert(!conn.isClosed());
        testDao.close();
        assert(conn.isClosed());
    }
}
