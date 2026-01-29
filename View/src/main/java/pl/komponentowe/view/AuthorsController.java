package pl.komponentowe.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller for the Authors view.
 * Handles the display of author names and animation.
 */
public class AuthorsController {
    @FXML
    public VBox vbox;

    /**
     * Initializes the controller class.
     * Loads authors from resources and starts animation.
     */
    @FXML
    public void initialize() {
        var authorsBundle = (ListResourceBundle) ResourceBundle.getBundle(
            "pl.komponentowe.view.Authors"
        );
        var authors = (String[]) authorsBundle.getObject("authors");

        for (String author : authors) {
            Text line = new Text(author);
            vbox.getChildren().add(line);
        }

        startAnimation();
    }

    private void startAnimation() {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(vbox);
        translate.setDuration(Duration.seconds(20));
        translate.setFromY(100);
        translate.setToY(-400);

        FadeTransition fade = new FadeTransition();
        fade.setNode(vbox);
        fade.setDuration(Duration.seconds(5));
        fade.setFromValue(0);
        fade.setToValue(1);

        ParallelTransition parallelTransition = new ParallelTransition(translate, fade);
        parallelTransition.setCycleCount(1);
        parallelTransition.play();
        parallelTransition.setOnFinished(event -> {
            SceneManager.INSTANCE.setView(View.DIFFICULTY_DIALOG);
        });
    }
}
