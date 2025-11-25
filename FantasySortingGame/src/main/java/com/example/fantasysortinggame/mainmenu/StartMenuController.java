package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuController {

    public ToggleButton loadSavedGameButton;
    public TextField gameNameField;
    @FXML private ToggleButton startNewGameButton;
    private final Database database;
    private final Runnable onStartGame;

    public StartMenuController(Database db, Runnable onStartGame) {
        this.database = db;
        this.onStartGame = onStartGame;
    }

    @FXML
    public void initialize() {
        startNewGameButton.setOnAction(e -> {
            onStartGame.run(); // Start the game phases
            ((Stage) startNewGameButton.getScene().getWindow()).close(); // Close start menu
        });
    }

    public static void showStartMenu(Database db, Runnable onStartGame) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartMenuController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/startMenu.fxml"));
        loader.setControllerFactory(param -> new StartMenuController(db, onStartGame));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Start Menu");
        stage.show();
    }
}
