package pl.komponentowe.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.exceptions.SudokuException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton managing scenes and stage of the application.
 */
public enum SceneManager {
    /**
     * Singleton instance.
     */
    INSTANCE;

    private Stage stage;

    /**
     * Sets the primary stage.
     * @param stage Primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private View currentView;

    private ResourceBundle resourceBundle;

    /**
     * Returns the current resource bundle.
     * @return ResourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Sets the application locale and reloads the current view.
     * @param locale New locale
     */
    public void setLocale(Locale locale) {
        Locale.setDefault(locale);

        if (currentView != null) {
            initialize();
            setView(currentView);
        }
    }

    /**
     * Initializes the resource bundle and sets the stage title.
     */
    public void initialize() {
        resourceBundle = ResourceBundle.getBundle(
            "pl.komponentowe.view.Strings",
            Locale.getDefault()
        );
        SudokuException.setErrorMessages(resourceBundle);

        if (stage == null) {
            logger.error("stage needs to be set before calling initialize");
            return;
        }
        stage.setTitle(resourceBundle.getString("sudoku"));
    }

    private final Logger logger = LoggerFactory.getLogger(SceneManager.class);

    /**
     * Sets the current view.
     * @param view View to be set
     * @param <T> Type of the controller
     * @return The controller of the view
     */
    public <T> T setView(View view) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getPath()));

        if (resourceBundle != null) {
            loader.setResources(resourceBundle);
        }

        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            logger.error("failed to load fxml resource");
            return null;
        }

        if (stage == null) {
            logger.error("stage needs to be set before calling setView");
            return null;
        }
        stage.setScene(new Scene(root));

        currentView = view;

        return loader.getController();
    }
}
