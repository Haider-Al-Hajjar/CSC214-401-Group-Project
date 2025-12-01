package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.gamemodes.GameModeNames;
import com.example.fantasysortinggame.gamephasemanager.GameEngine;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartMenuControllerTest {

    private Database db;
    private StartMenuController controller;

    @BeforeEach
    void setUp() {
        db = new Database();
        controller = new StartMenuController(db);

        // Simulate FXML injection
        controller.gameModeButton = new ToggleButton();
        controller.startNewGameButton = new ToggleButton();
        controller.gameNameField = new TextField("TestGame");

        // Initialize controller logic
        controller.initialize();
    }

    @Test
    void testGameModeSelectionUpdatesDatabase() {
        // Simulate clicking "Story Mode"
        MenuItem storyModeItem = controller.gameModeButton.getContextMenu().getItems().get(0);
        storyModeItem.fire();

        assertEquals(GameModeNames.Story, db.getGameMode());
    }

    @Test
    void testStartGameInitializesGameEngine() {
        controller.gameNameField.setText("MyTestGame");

        // override GameEngine to intercept calls
        GameEngine.initialize(db, null, controller.soundEffectController);

        controller.startNewGameButton.fire();

        // GameEngine should now have the database set
        assertNotNull(GameEngine.getGameMode());
        assertEquals(db.getGameMode(), GameEngine.getGameMode());
    }
}