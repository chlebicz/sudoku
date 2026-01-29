package pl.komponentowe.model;

import io.github.cdimascio.dotenv.Dotenv;
import pl.komponentowe.model.exceptions.SudokuNullPointerException;
import pl.komponentowe.model.exceptions.UnknownSudokuException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class responsible for initializing the database connection and schema.
 */
public class DatabaseInit {
    private static Dotenv dotenv;

    private static void configureDotenv() {
        if (dotenv != null) {
            return;
        }

        String envDirectory = "./";

        if (!Files.exists(Paths.get(".env")) && Files.exists(Paths.get("../.env"))) {
            envDirectory = "../";
        }

        dotenv = Dotenv.configure().directory(envDirectory).ignoreIfMissing().load();
    }

    /**
     * Retrieves the PostgreSQL URL from the environment variables.
     * @return Postgres URL string
     */
    public static String getPostgresUrl() {
        configureDotenv();
        return dotenv.get("POSTGRES_URL");
    }

    /**
     * Retrieves the PostgreSQL username from the environment variables.
     * @return Postgres username string
     */
    public static String getPostgresUsername() {
        configureDotenv();
        return dotenv.get("POSTGRES_USER");
    }

    /**
     * Retrieves the PostgreSQL password from the environment variables.
     * @return Postgres password string
     */
    public static String getPostgresPassword() {
        configureDotenv();
        return dotenv.get("POSTGRES_PASSWORD");
    }

    /**
     * Creates the necessary tables in the database if they do not exist.
     * Establishes a connection and executes SQL DDL statements.
     * @throws SudokuNullPointerException if database credentials are not set
     * @throws UnknownSudokuException if a database error occurs
     */
    public static void createTables() {
        String dbUrl = getPostgresUrl();
        String dbUser = getPostgresUsername();
        String dbPassword = getPostgresPassword();

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new SudokuNullPointerException("nullPointer");
        }

        // Table for storing Sudoku boards metadata
        String sqlBoards = """
            CREATE TABLE IF NOT EXISTS boards (
                id SERIAL PRIMARY KEY,
                board_name VARCHAR(100) NOT NULL UNIQUE,
                clean_id INTEGER REFERENCES boards(id) ON DELETE SET NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        // Table for storing individual fields (cells) of the boards
        String sqlCells = """
            CREATE TABLE IF NOT EXISTS board_fields (
                id SERIAL PRIMARY KEY,
                board_id INTEGER NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
                index INTEGER NOT NULL CHECK (index BETWEEN 0 AND 80),
                value INTEGER,
                CONSTRAINT unique_board_index UNIQUE (board_id, index)
            );
        """;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            try {
                conn.setAutoCommit(false);

                stmt.execute(sqlBoards);
                stmt.execute(sqlCells);

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new UnknownSudokuException("unknownDbError");
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new UnknownSudokuException("unknownDbError");
        }
    }
}
