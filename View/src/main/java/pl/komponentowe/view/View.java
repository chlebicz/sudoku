package pl.komponentowe.view;

/**
 * Enum representing different views (FXML files) in the application.
 */
public enum View {
    /**
     * Dialog for selecting difficulty level.
     */
    DIFFICULTY_DIALOG("/pl/komponentowe/view/DifficultyDialog.fxml"),
    /**
     * Main Sudoku board view.
     */
    BOARD_VIEW("/pl/komponentowe/view/BoardView.fxml"),
    /**
     * View displaying authors.
     */
    AUTHORS_VIEW("/pl/komponentowe/view/AuthorsView.fxml");

    private final String path;

    View(String path) {
        this.path = path;
    }

    /**
     * Returns the path to the FXML file.
     * @return FXML resource path
     */
    public String getPath() {
        return path;
    }
}
