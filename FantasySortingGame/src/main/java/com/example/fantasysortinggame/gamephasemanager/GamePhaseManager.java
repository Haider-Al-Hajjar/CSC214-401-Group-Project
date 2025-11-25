package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;

import javafx.stage.Stage;
import com.example.fantasysortinggame.sortphase.SortPhaseController;
import com.example.fantasysortinggame.buyphase.BuyPhaseController;
import com.example.fantasysortinggame.salephase.SalePhaseController;
public class GamePhaseManager {

    private final Database database;
    private final Stage primaryStage;
    private SortPhaseController SortPhaseController;
    private BuyPhaseController BuyPhaseController;
    private SalePhaseController SalePhaseController;

    public GamePhaseManager(Database database, Stage primaryStage) {
        this.database = database;
        this.primaryStage = primaryStage;
    }

    /** Entry point: start the day cycle */
    public void startDayCycle() {
        runSortPhase();
    }

    /** Sort Phase → calls UI, waits for completion, then Sale Phase */
    private void runSortPhase() {
        SortPhaseController.showSortPhase(database, primaryStage, this::runSalePhase);
    }

    /** Sale Phase → waits for player to finish → Buy Phase */
    private void runSalePhase() {
        SalePhaseController.showSalePhase(database, primaryStage, this::runBuyPhase);
    }

    /** Buy Phase → end of day or loop for next day */
    private void runBuyPhase() {
        BuyPhaseController.showBuyPhase(database, primaryStage, () -> {
            System.out.println("End of day completed.");
            // Optionally: start next day
            // startDayCycle();
        });
    }
}
