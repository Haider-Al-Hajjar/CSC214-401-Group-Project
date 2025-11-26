package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.phasecontrollers.SortPhaseController;
import com.example.fantasysortinggame.phasecontrollers.BuyPhaseController;
import com.example.fantasysortinggame.phasecontrollers.SalePhaseController;
import javafx.stage.Stage;

public class GamePhaseManager {

    private static Database database;
    private static Stage primaryStage;

    // Initialize the utility class once
    public static void initialize(Database db, Stage stage) {
        database = db;
        primaryStage = stage;
    }

    /** Entry point: start the day cycle */
    public static void startDayCycle() {
        runSortPhase();
    }

    /** Sort Phase → calls UI, waits for completion, then Sale Phase */
    public static void runSortPhase() {
        SortPhaseController.showSortPhase(database, primaryStage);
    }

    /** Sale Phase → waits for player to finish → Buy Phase */
    public static void runSalePhase() {
        SalePhaseController.showSalePhase(database, primaryStage);
    }

    /** Buy Phase → end of day or loop for next day */
    public static void runBuyPhase() {
        BuyPhaseController.showBuyPhase(database, primaryStage, () -> {
            System.out.println("End of day completed.");
            // Optionally: start next day
            // startDayCycle();
        });
    }
}