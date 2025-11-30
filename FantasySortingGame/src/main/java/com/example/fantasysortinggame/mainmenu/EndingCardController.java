package com.example.fantasysortinggame.mainmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EndingCardController {
    @FXML
    public Label endingTitle;
    @FXML
    public Label endingDescription;
    @FXML
    public Button closeButton;

    public void setEnding(String title, String description) {
        endingTitle.setText(title);
        endingDescription.setText(description);
    }

    @FXML
    public void initialize() {
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());
    }
}
