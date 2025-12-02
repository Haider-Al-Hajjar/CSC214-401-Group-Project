package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamemodes.*;
import com.example.fantasysortinggame.mainmenu.EndingCardController;
import com.example.fantasysortinggame.mainmenu.MistakePopupController;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Core game engine managing the progression of phases and delegating
 * game mode-specific logic. Handles starting days, sorting, sales,
 * purchases, and game endings.
 */
public class GameEngine {

    private static Database database;
    private static Stage primaryStage;
    private static GameMode gameMode;
    private static SoundEffectController soundEffectController;

    /**
     * Initializes the GameEngine with the main database, primary stage,
     * and sound effect controller. Also sets the game mode.
     *
     * @param db                         The game database
     * @param stage                      The primary application stage
     * @param soundEffectControllerParam Sound controller for button and event sounds
     */
    public static void initialize(Database db, Stage stage, SoundEffectController soundEffectControllerParam) {
        database = db;
        primaryStage = stage;
        soundEffectController = soundEffectControllerParam;
        setGameMode(db.getGameMode());
    }

    /**
     * Sets the current game mode to the specified type.
     *
     * @param type The type of game mode to use
     */
    public static void setGameMode(GameModeNames type) {
        gameMode = GameModeFactory.create(type);
    }

    /**
     * Returns the sound effect controller used by the game.
     *
     * @return SoundEffectController instance
     */
    public static SoundEffectController getSoundController() {
        return soundEffectController;
    }

    /**
     * Returns the current active GameMode.
     *
     * @return The active GameMode
     */
    public static GameMode getGameMode() {
        return gameMode;
    }


    /**
     * Starts the day cycle: checks if the day should begin according to the
     * game mode and starts the sorting phase.
     */
    public static void startDayCycle() {
        if (gameMode instanceof TimedMode ttMode) {
            ttMode.startTimer();
        }

        if (!gameMode.shouldDayStart(database)) {
            handleEnding();
            return;
        }

        runSortPhase();
    }

    /**
     * Handles the game ending by showing the ending screen and stopping
     * timers if applicable.
     */
    public static void handleEnding() {
        gameMode.checkEnding(database).ifPresent(ending -> {
            // Show ending screen / dialogue
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(GameEngine.class.getResource("/com/example/fantasysortinggame/fxmlfiles/EndingCard.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            EndingCardController controller = loader.getController();
            controller.setEnding(ending.type(), ending.description());

            stage.setScene(new Scene(root));
            stage.setTitle("Game Over");
            stage.show();

            // Optionally stop timers for TimedMode
            if (gameMode instanceof TimedMode ttMode) ttMode.stopTimer();
        });
    }

    /**
     * Runs the Sort Phase, with a callback to handle either ending or
     * transition to the Sale Phase.
     */
    public static void runSortPhase() {
        SortPhaseController.showSortPhase(database, primaryStage, () -> {
            Optional<EndingResult> ending = gameMode.checkEnding(database);
            ending.ifPresentOrElse(
                    e -> handleEnding(),
                    GameEngine::runSalePhase
            );
        });
    }

    /**
     * Runs the Sale Phase, with a callback to handle either ending or
     * transition to the Buy Phase.
     */
    public static void runSalePhase() {
        SalePhaseController.showSalePhase(database, primaryStage, () -> {
            Optional<EndingResult> ending = gameMode.checkEnding(database);
            ending.ifPresentOrElse(
                    e -> handleEnding(),
                    GameEngine::runBuyPhase
            );

        });
    }

    /**
     * Runs the Buy Phase, with a callback to handle either ending or
     * transition back to the Sort Phase.
     */
    public static void runBuyPhase() {
        BuyPhaseController.showBuyPhase(database, primaryStage, () -> {
            Optional<EndingResult> ending = gameMode.checkEnding(database);
            ending.ifPresentOrElse(
                    e -> handleEnding(),
                    GameEngine::runSortPhase
            );

        });
    }

    /**
     * Handles a correctly sorted item:
     * - Adds the item's value to player's gold
     * - Delegates mode-specific logic to the current GameMode
     *
     * @param item The item that was sorted correctly
     */
    public static void onCorrectSort(Item item) {
        // generic hooks
        database.setGold(database.getGold() + item.getValue());
        gameMode.onCorrectSort(database, item);  // mode-specific logic
        // maybe log stats, trigger global events, etc.
    }

    /**
     * Handles an incorrect sort:
     * - Delegates mistake logic to the current GameMode
     * - Shows mistake popup if a message is provided
     * - Ends the game if the player has lost according to the game mode
     */
    public static void onIncorrectSort() {
        gameMode.onMistake(database);

        if (!(gameMode.getMistakeMessage(database) == null)) {
            MistakePopupController.showPopup(
                    gameMode.getMistakeMessage(database)
            );
        }

        if (gameMode.hasLost(database)) {
            handleEnding();
        }
    }


}