package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
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
    ToggleButton startNewGameButton;
    private final Database database;
    SoundEffectController soundEffectController;

    /**
     * Constructs the StartMenuController with the provided database.
     *
     * @param db Database instance.
     */
    public StartMenuController(Database db) {
        this.database = db;
        this.soundEffectController = new SoundEffectController();
    }

    /**
     * Initializes UI elements and sets up event handlers.
     */
    @FXML
    public void initialize() {

        // --- popup menu with game modes ---
        ContextMenu modeMenu = new ContextMenu();

        ArrayList<MenuItem> gameModes = new ArrayList<>(List.of(new MenuItem[]{new MenuItem("Story Mode"), new MenuItem("Endless Mode"), new MenuItem("Zen Mode"), new MenuItem("Timed Mode"), new MenuItem("Scored Mode")}));
        modeMenu.getItems().addAll(gameModes);

        // Show popup on click
        gameModeButton.setOnAction(e -> {
            soundEffectController.playButtonClick();
            modeMenu.show(startNewGameButton,
                    javafx.geometry.Side.BOTTOM, 0, 0);
        });

        // Handle selection
        for (MenuItem gameMode : modeMenu.getItems()) {
            gameMode.setOnAction(e -> {
                soundEffectController.playButtonClick();
                database.setGameMode(GameModeNames.valueOf(gameMode.getText().split(" ")[0]));
            });
        }

        startNewGameButton.setOnAction(e -> {
            soundEffectController.playButtonClick();
            startGame();
        });
    }

    /**
     * Converts a menu display string to a GameMode enum.
     *
     * @param menuText Menu display string.
     * @return Corresponding GameMode enum name.
     */
    private String convertToEnum(String menuText) {
        return switch (menuText) {
            case "Story Mode" -> "STORY_MODE";
            case "Endless Mode" -> "ENDLESS_MODE";
            case "Zen Mode" -> "ZEN_MODE";
            case "Timed Mode" -> "TIME_TRIAL_MODE";
            case "Scored Mode" -> "SCORE_ATTACK_MODE";
            default -> throw new IllegalArgumentException("Unknown game mode: " + menuText);
        };
    }

    /**
     * Starts the game using the current database state.
     * <p>
     * Loads existing game or starts tutorial if new.
     */
    private void startGame() {
        boolean fileExists = database.loadFromFile(gameNameField.getText(), database.getGameMode() == null ? GameModeNames.valueOf("Story") : database.getGameMode());
        GameEngine.initialize(database, new Stage(), soundEffectController);
        GameEngine.setGameMode(database.getGameMode()); // add this in your GameEngine
        if (fileExists) {
            GameEngine.startDayCycle();
        } else {
            TutorialController.showTutorial(database);
        }
        // Close start menu window
        ((Stage) startNewGameButton.getScene().getWindow()).close();
    }
}
