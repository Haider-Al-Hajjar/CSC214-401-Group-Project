package com.example.fantasysortinggame.mainmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FantasySortingGameController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}