package pl.komponentowe.model;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.exceptions.SudokuException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseInitTest {
    private Logger logger = LoggerFactory.getLogger(DatabaseInitTest.class);

    @Test
    void shouldVerifyTablesExistInDatabase() throws Exception {
        try {
            DatabaseInit.createTables();
        } catch (SudokuException e) {
            Assumptions.abort(
                "Database connectivity is not set up correctly, skipping database-related tests"
            );
        }

        String url = DatabaseInit.getPostgresUrl();
        String user = DatabaseInit.getPostgresUsername();
        String pass = DatabaseInit.getPostgresPassword();

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet rs = metaData.getTables(null, null, "boards", null)) {
                assertTrue(rs.next());
            }

            try (ResultSet rs = metaData.getTables(null, null, "board_fields", null)) {
                assertTrue(rs.next());
            }
        }
    }
}