package pl.komponentowe.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.*;
import pl.komponentowe.model.exceptions.DaoIoException;
import pl.komponentowe.model.exceptions.NonexistentFileException;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the Sudoku board view.
 */
public class BoardViewController {
    @FXML
    public GridPane boardGrid;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public Button darkModeButton;

    @FXML
    public Label timerLabel;

    @FXML
    public Button resetButton;

    private SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
    private SudokuBoard cleanBoard;

    private final Logger logger = LoggerFactory.getLogger(BoardViewController.class);

    /**
     * Sets the difficulty level and initializes the board.
     * @param difficultyLevel Difficulty level
     */
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        board.solveGame();
        difficultyLevel.removeFields(board);
        cleanBoard = board.clone();
        reset();
    }

    private void initializeBoardGrid() {
        final int sudokuBoardSize = 9;

        for (int row = 0; row < sudokuBoardSize; ++row) {
            for (int col = 0; col < sudokuBoardSize; ++col) {
                initializeBoardField(col, row);
            }
        }
    }

    private final SudokuFieldBinder binder = new SudokuFieldBinder();

    private void initializeBoardField(int col, int row) {
        TextField textField = new TextField();
        textField.getStyleClass().add("field");

        SudokuField boardField = board.getField(col, row);
        setValue(textField, boardField);

        // disable editing for initially set fields
        boolean wasFieldValueGenerated = cleanBoard.getField(col, row).getFieldValue() == boardField.getFieldValue();
        if (boardField.getFieldValue() != 0 && wasFieldValueGenerated) {
            textField.setEditable(false);
            textField.getStyleClass().add("generated");
        }

        // field border sizes
        int top = 1;
        int right = 1;
        int bottom = 1;
        int left = 1;

        // thicker lines on the board separating 3x3 squares
        if (col == 2 || col == 5) {
            right = 3;
        }
        if (row == 2 || row == 5) {
            bottom = 3;
        }

        String style = String.format(
                "-fx-border-width: %d %d %d %d;", top, right, bottom, left
        );

        textField.setStyle(style);
        textField.setAlignment(Pos.CENTER);
        textField.setPrefWidth(39.0);
        textField.setPrefHeight(45.0);

        binder.bindField(boardField, textField);

        StackPane cell = new StackPane();
        cell.getChildren().add(textField);

        boardGrid.add(cell, col, row);
    }

    private void setValue(TextField text, SudokuField field) {
        int fieldValue = field.getFieldValue();
        text.setText(fieldValue == 0 ? "" : String.valueOf(fieldValue));
    }

    /**
     * Handles the "Read from File" button click event.
     */
    @FXML
    public void handleReadFromFileClicked() {
        List<String> choices;
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getDao()) {
            choices = dao.names();
        } catch (Exception e) {
            logger.error("Error with dao", e);
            return;
        }

        choices = choices.stream().filter(c -> !c.endsWith("_clean")).toList();

        if (choices.isEmpty()) {
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);

        ResourceBundle resources = SceneManager.INSTANCE.getResourceBundle();
        dialog.setTitle(resources.getString("readFromFile"));
        dialog.setHeaderText(resources.getString("readFromFile"));

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(fileName -> {
            try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getDao()) {
                board = dao.read(fileName);
            } catch (Exception e) {
                logger.error("Error occurred when trying to read Sudoku board");
                logger.error(e.getMessage());
            }

            try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getDao()) {
                String nameOfFile = fileName + "_clean";
                cleanBoard = dao.read(nameOfFile);
            } catch (NonexistentFileException e) {
                // case when we load board name_clean, then name_clean_clean does not exist
                cleanBoard = board.clone();
            } catch (Exception e) {
                cleanBoard = board.clone();
                logger.error("Error occurred when trying to read clean Sudoku board");
                logger.error(e.getMessage());
            }

            reset();
        });
    }

    /**
     * Handles the "Write to File" button click event.
     */
    @FXML
    public void handleWriteToFile() {
        TextInputDialog dialog = new TextInputDialog("sudoku_save");

        ResourceBundle resources = SceneManager.INSTANCE.getResourceBundle();
        dialog.setTitle(resources.getString("saveToFile"));
        dialog.setHeaderText(resources.getString("saveToFile"));
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(fileName -> {
            if (fileName.trim().isEmpty()) {
                return;
            }

            try (Dao<SudokuBoard> decoratedDao = SudokuBoardDaoFactory.getCleanSavingDao(cleanBoard)) {
                decoratedDao.write(fileName, board);
            } catch (DaoIoException e) {
                logger.error(e.getLocalizedMessage());
            } catch (Exception e) {
                logger.error("unknown error while writing sudokuboard");
            }
        });
    }

    private final String moonPath = "M12 3c.132 0 .263 0 .393 0a7.5 7.5 0 0 0 7.92 12.446a9 9 0 1 1 -8.313 -12.446z";
    private final String sunPath  = "M6.76 4.84l-1.8-1.79-1.41 1.41 1.79 1.79 1.42-1.41zM4 "
            + "10.5H1v2h3v-2zm9-9.95h-2V3.5h2V.55zm7.45 3.91l-1.41-1.41-1.79 1.79 1.41 1.41 "
            + "1.79-1.79zm-3.21 13.76l1.79 1.79 1.41-1.41-1.79-1.79-1.41 1.41zM20 10.5v2h3v-2h-3zm-8-5c-3.31 "
            + "0-6 2.69-6 6s2.69 6 6 6 6-2.69 6-6-2.69-6-6-6zm-1 16.95h2V19.5h-2v2.95zm-7.45-3.91l1.41 "
            + "1.41 1.79-1.8-1.41-1.41-1.79 1.8z";

    @FXML
    public void toggleDarkMode(ActionEvent event) {
        String css = getClass().getResource("dark-theme.css").toExternalForm();

        var stylesheets = anchorPane.getStylesheets();
        var svg = ((SVGPath) darkModeButton.getGraphic());

        if (!stylesheets.contains(css)) {
            stylesheets.add(css);
            svg.setContent(sunPath);
        } else {
            stylesheets.remove(css);
            svg.setContent(moonPath);
        }
    }

    public void handleReset(ActionEvent actionEvent) {
        board = cleanBoard.clone();
        reset();
    }

    private void reset() {
        boardGrid.getChildren().clear();
        initializeBoardGrid();
        setupTimer();
    }

    private int secondsElapsed = 0;
    private Timeline timeline;

    private void setupTimer() {
        secondsElapsed = 0;
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            updateTimerLabel();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimerLabel() {
        int mins = secondsElapsed / 60;
        int secs = secondsElapsed % 60;
        timerLabel.setText(String.format("%02d:%02d", mins, secs));
    }
}