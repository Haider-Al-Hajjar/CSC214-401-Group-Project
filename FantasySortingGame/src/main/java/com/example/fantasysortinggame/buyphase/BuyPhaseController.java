package com.example.fantasysortinggame.buyphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Upgrade;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;

public class BuyPhaseController {

    private final Database database;
    private ArrayList<Upgrade> unboughtUpgrades;
    private Runnable onPhaseComplete;

    @FXML private VBox shopContainer;
    @FXML private Button proceedButton;

    public BuyPhaseController(Database database) {
        this.database = database;
    }

    /**
     * Static helper to show the Buy Phase window and chain to next phase.
     */
    public static void showBuyPhase(Database db, Stage parentStage, Runnable onPhaseComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(BuyPhaseController.class.getResource("/path/to/BuyPhase.fxml"));
            loader.setControllerFactory(param -> new BuyPhaseController(db));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Buy Phase");
            stage.show();

            BuyPhaseController controller = loader.getController();
            controller.setOnPhaseComplete(onPhaseComplete, stage);
            controller.loadUpgrades();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the callback to be run when player clicks Proceed.
     */
    public void setOnPhaseComplete(Runnable callback, Stage stage) {
        this.onPhaseComplete = () -> {
            stage.close();
            callback.run();
        };

        proceedButton.setOnAction(e -> {
            if (onPhaseComplete != null) onPhaseComplete.run();
        });
    }

    /**
     * Load all unbought upgrades from the database.
     */
    public void loadUpgrades() {
        unboughtUpgrades = database.getUnboughtUpgrades();
        displayBuyMenu();
    }

    /**
     * Handle a player clicking to buy an upgrade.
     */
    public void onBuyUpgradeClickHandler(Upgrade upgrade) {
        if (database.getGold() >= upgrade.getCost()) {
            database.setGold(database.getGold() - upgrade.getCost());
            upgrade.setBought(true);
        }
        displayBuyMenu();
    }

    /**
     * Display the upgrades that are still available to buy.
     */
    public void displayBuyMenu() {
        shopContainer.getChildren().clear();

        for (Upgrade upgrade : unboughtUpgrades) {
            if (!upgrade.isBought()) {
                displayUpgrade(upgrade);
            }
        }
    }

    /**
     * Populate the display for a single upgrade.
     */
    public void displayUpgrade(Upgrade upgrade) {
        // TODO: load individual upgrade FXML node or build a UI element
        // Example: button to buy this upgrade
        Button buyButton = new Button(upgrade.getName() + " - " + upgrade.getCost() + "G");
        buyButton.setOnAction(e -> onBuyUpgradeClickHandler(upgrade));
        shopContainer.getChildren().add(buyButton);
    }
}
