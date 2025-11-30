package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Upgrade;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BuyPhaseController {

    private final Database database;
    @FXML
    public Label totalGoldLabel;
    private ArrayList<Upgrade> unboughtUpgrades;
    private Runnable onPhaseComplete;

    private Stage stage;
    @FXML
    private VBox shopContainer;
    @FXML
    private Button proceedButton;
    @FXML
    private Label dayLabel;

    public BuyPhaseController(Database database) {
        this.database = database;
    }

    private void updateTopBar() {
        if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
        if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
    }


    /**
     * Static helper to show the Buy Phase window and chain to next phase.
     */
    public static void showBuyPhase(Database db, Stage parentStage, Runnable onPhaseComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(BuyPhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/BuyPhase.fxml"));
            loader.setControllerFactory(param -> new BuyPhaseController(db));
            parentStage.setScene(new Scene(loader.load()));
            parentStage.setTitle("Buy Phase");
            parentStage.show();

            BuyPhaseController controller = loader.getController();
            controller.setOnPhaseComplete(onPhaseComplete, parentStage);
            controller.loadUpgrades();
            controller.updateTopBar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPhaseComplete(Runnable onComplete, Stage stage) {
        this.stage = stage; // assign the stage
        this.onPhaseComplete = () -> {
            if (this.stage != null) this.stage.close();
            if (onComplete != null) onComplete.run();
            database.setDay(database.getDay() + 1);
            database.saveToFile();
            GameEngine.runSortPhase(); // next phase after buying
        };

        proceedButton.setOnAction(e -> {
            if (onPhaseComplete != null) onPhaseComplete.run();
        });
    }


    /**
     * Load all unbought upgrades from the database.
     */
    public void loadUpgrades() {
        unboughtUpgrades = database.getAllUpgrades().stream().filter(u -> !u.isBought()).collect(Collectors.toCollection(ArrayList::new));
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
        updateTopBar();
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
        VBox box = new VBox();
        box.setSpacing(4);
        box.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");

        Label title = new Label(upgrade.getName() + " (Cost: " + upgrade.getCost() + ")");
        Label ability = new Label("Ability: " + upgrade.getAbility());

        Button buyButton = new Button("Buy");
        buyButton.setOnAction(e -> onBuyUpgradeClickHandler(upgrade));
        if (database.getGold() < upgrade.getCost()) {
            buyButton.setDisable(true);
        }
        box.getChildren().addAll(title, ability, buyButton);
        shopContainer.getChildren().add(box);
    }

}
