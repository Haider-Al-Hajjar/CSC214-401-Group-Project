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

public class GameEngine {

    private static Database database;
    private static Stage primaryStage;
    private static GameMode gameMode;
    private static SoundEffectController soundEffectController;

    // Initialize the utility class once
    public static void initialize(Database db, Stage stage, SoundEffectController soundEffectControllerParam) {
        database = db;
        primaryStage = stage;
        soundEffectController = soundEffectControllerParam;
        setGameMode(db.getGameMode());
    }

    public static void setGameMode(GameModeNames type) {
        gameMode = GameModeFactory.create(type);
    }

    public static SoundEffectController getSoundController() {
        return soundEffectController;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }


    /** Entry point: start the day cycle */
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
    public static void runSortPhase() {
        SortPhaseController.showSortPhase(database,primaryStage,  () -> {
            Optional<EndingResult> ending = gameMode.checkEnding(database);
            ending.ifPresentOrElse(
                    e -> handleEnding(),
                    GameEngine::runSalePhase
            );
        });
    }

    public static void runSalePhase() {
        SalePhaseController.showSalePhase(database, primaryStage, () -> {
            Optional<EndingResult> ending = gameMode.checkEnding(database);
            ending.ifPresentOrElse(
                    e -> handleEnding(),
                    GameEngine::runBuyPhase
            );

        });
    }

    public static void runBuyPhase() {
        BuyPhaseController.showBuyPhase(database, primaryStage, () -> {
            Optional<EndingResult> ending = gameMode.checkEnding(database);
            ending.ifPresentOrElse(
                    e -> handleEnding(),
                    GameEngine::runSortPhase
            );

        });
    }

    public static void onCorrectSort(Item item) {
        // generic hooks
        database.setGold(database.getGold()+ item.getValue());
        gameMode.onCorrectSort(database, item);  // mode-specific logic
        // maybe log stats, trigger global events, etc.
    }

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