package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuController {

    public ToggleButton loadSavedGameButton;
    public TextField gameNameField;
    @FXML
    private ToggleButton startNewGameButton;
    private final Database database;

    public StartMenuController(Database db) {
        this.database = db;
    }

    @FXML
    public void initialize() {
        startNewGameButton.setOnAction(e -> {
            database.loadFromFile(gameNameField.getText(), null);
            ((Stage) startNewGameButton.getScene().getWindow()).close(); // Close start menu
            GamePhaseManager.startDayCycle();
        });
    }

    public static void showStartMenu(Database db, Runnable onStartGame) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartMenuController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/startMenu.fxml"));
        loader.setControllerFactory(param -> new StartMenuController(db));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Start Menu");
        stage.show();
    }
}
