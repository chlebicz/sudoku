package pl.komponentowe.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.komponentowe.model.SudokuField;

/**
 * Class responsible for binding SudokuFields to UI controls (TextFields).
 */
public class SudokuFieldBinder {
    private final SudokuFieldValueConverter fieldValueConverter = new SudokuFieldValueConverter();

    private TextFormatter.Change filterFieldInput(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.matches("([1-9])?")) {
            return change;
        }
        return null;
    }

    Logger logger = LoggerFactory.getLogger(SudokuFieldBinder.class);

    /**
     * Binds a SudokuField to a JavaFX TextField.
     * Sets up bidirectional binding and input filtering.
     * @param field The SudokuField model object
     * @param textField The JavaFX TextField control
     */
    public void bindField(SudokuField field, TextField textField) {
        JavaBeanIntegerProperty fieldProperty;
        try {
            fieldProperty = JavaBeanIntegerPropertyBuilder.create()
                .bean(field)
                .name("fieldValue")
                .build();
        } catch (NoSuchMethodException e) {
            logger.error("Unknown error occurred when trying to bind field");
            return;
        }

        TextFormatter<Integer> textFormatter = new TextFormatter<>(
            this::filterFieldInput
        );
        textField.setTextFormatter(textFormatter);

        Bindings.bindBidirectional(textField.textProperty(), fieldProperty, fieldValueConverter);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            Number newNumber = fieldValueConverter.fromString(newValue);
            int newInt = newNumber.intValue();

            // check if SudokuFieldContainer reverted the field value change
            int correctValue = fieldProperty.get();
            if (correctValue != newInt) {
                textField.setText(fieldValueConverter.toString(correctValue));
            }
        });
    }
}

