package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.gamemodes.GameMode;
import com.example.fantasysortinggame.gamemodes.GameModeNames;
import com.example.fantasysortinggame.gamephasemanager.GameEngine;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class StartMenuController {

    @FXML
    public ToggleButton gameModeButton;
    @FXML
    public TextField gameNameField;
    @FXML
    private ToggleButton startNewGameButton;
    private final Database database;

    public StartMenuController(Database db) {
        this.database = db;
    }

    @FXML
    public void initialize() {

        // --- popup menu with game modes ---
        ContextMenu modeMenu = new ContextMenu();

        ArrayList<MenuItem> gameModes = new ArrayList<>(List.of(new MenuItem[]{new MenuItem("Story Mode"), new MenuItem("Endless Mode"), new MenuItem("Zen Mode"), new MenuItem("Timed Mode"), new MenuItem("Scored Mode")}));
        modeMenu.getItems().addAll(gameModes);

        // Show popup on click
        gameModeButton.setOnAction(e -> {
            modeMenu.show(startNewGameButton,
                    javafx.geometry.Side.BOTTOM, 0, 0);
        });

        // Handle selection
        for (MenuItem gameMode : modeMenu.getItems()) {
            gameMode.setOnAction(e -> database.setGameMode(GameModeNames.valueOf(gameMode.getText().split(" ")[0])));
        }

        startNewGameButton.setOnAction(e-> {
            startGame();
        });
    }

    private String convertToEnum(String menuText) {
        return switch (menuText) {
            case "Story Mode" -> "STORY_MODE";
            case "Endless Mode" -> "ENDLESS_MODE";
            case "Zen Mode" -> "ZEN_MODE";
            case "Time Trial Mode" -> "TIME_TRIAL_MODE";
            case "Score Attack Mode" -> "SCORE_ATTACK_MODE";
            default -> throw new IllegalArgumentException("Unknown game mode: " + menuText);
        };
    }

    private void startGame() {
        database.loadFromFile(gameNameField.getText(), database.getGameMode() == null ? GameModeNames.valueOf("Story") : database.getGameMode());

        GameEngine.initialize(database, new Stage());
        GameEngine.setGameMode(database.getGameMode()); // add this in your GameEngine
        GameEngine.startDayCycle();

        // Close start menu window
        ((Stage) startNewGameButton.getScene().getWindow()).close();
    }
}
