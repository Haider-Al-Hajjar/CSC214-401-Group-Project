package com.example.fantasysortinggame.mainmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the ending card UI.
 * <p>
 * Handles displaying the ending title and description, and closing the window.
 */
public class EndingCardController {
    @FXML
    public Label endingTitle;
    @FXML
    public Label endingDescription;
    @FXML
    public Button closeButton;

    /**
     * Sets the ending title and description.
     *
     * @param title       Title text.
     * @param description Description text.
     */
    public void setEnding(String title, String description) {
        endingTitle.setText(title);
        endingDescription.setText(description);
    }

    /**
     * Initializes the controller and attaches the close button action.
     */
    @FXML
    public void initialize() {
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());
    }
}
