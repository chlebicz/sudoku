package pl.komponentowe.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.DatabaseInit;
import pl.komponentowe.model.exceptions.SudokuException;

import java.util.Locale;

/**
 * Main application class.
 */
public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Main method.
     * @param args Command line arguments
     */
    static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     * @param stage Primary stage
     */
    @Override
    public void start(Stage stage) {
        try {
            DatabaseInit.createTables();
        } catch (SudokuException e) {
            logger.info(
                "Database connectivity is not set up correctly, defaulting to file storage."
            );
        }

        SceneManager.INSTANCE.setStage(stage);
        SceneManager.INSTANCE.initialize();
        SceneManager.INSTANCE.setView(View.DIFFICULTY_DIALOG);
        stage.show();

        logger.info("Application started");
    }
}
