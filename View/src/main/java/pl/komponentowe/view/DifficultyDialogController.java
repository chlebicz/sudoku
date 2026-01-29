package pl.komponentowe.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.Locale;

/**
 * Controller for the difficulty selection dialog.
 */
public class DifficultyDialogController {
    /**
     * Handles Easy button click.
     */
    @FXML
    public void onEasy() {
        BoardViewController c = SceneManager.INSTANCE.setView(View.BOARD_VIEW);
        c.setDifficultyLevel(DifficultyLevel.EASY);
    }

    /**
     * Handles Medium button click.
     */
    @FXML
    public void onMedium() {
        BoardViewController c = SceneManager.INSTANCE.setView(View.BOARD_VIEW);
        c.setDifficultyLevel(DifficultyLevel.MEDIUM);
    }

    /**
     * Handles Hard button click.
     */
    @FXML
    public void onHard() {
        BoardViewController c = SceneManager.INSTANCE.setView(View.BOARD_VIEW);
        c.setDifficultyLevel(DifficultyLevel.HARD);
    }

    /**
     * Handles About button click.
     * @param actionEvent Event
     */
    @FXML
    public void handleAboutClick(ActionEvent actionEvent) {
        SceneManager.INSTANCE.setView(View.AUTHORS_VIEW);
    }

    /**
     * Switches language to Polish.
     * @param actionEvent Event
     */
    public void onPlClick(ActionEvent actionEvent) {
        SceneManager.INSTANCE.setLocale(new Locale("pl"));
    }

    /**
     * Switches language to English.
     * @param actionEvent Event
     */
    public void onEnClick(ActionEvent actionEvent) {
        SceneManager.INSTANCE.setLocale(new Locale("en"));
    }

    /**
     * Switches language to Chinese (Taiwan).
     * @param actionEvent Event
     */
    public void onZhClick(ActionEvent actionEvent) {
        SceneManager.INSTANCE.setLocale(new Locale("zh", "TW"));
    }
}
