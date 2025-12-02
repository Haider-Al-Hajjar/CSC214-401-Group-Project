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
    public VBox shopContainer;
    @FXML
    public Button proceedButton;
    @FXML
    public Label dayLabel;

    /**
     * Controller for the Buy Phase of the game.
     * <p>
     * Handles displaying available upgrades, processing purchases, and advancing to the next phase.
     */
    public BuyPhaseController(Database database) {
        this.database = database;
    }

    /**
     * Updates the top bar labels showing the current day and player's gold.
     */
    private void updateTopBar() {
        if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
        if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
    }


    /**
     * Shows the Buy Phase window as a modal and sets up the callback to the next phase.
     *
     * @param db              Reference to the game database
     * @param parentStage     The main application stage
     * @param onPhaseComplete Runnable to execute after the buy phase completes
     */
    public static void showBuyPhase(Database db, Stage parentStage, Runnable onPhaseComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(BuyPhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/BuyPhase.fxml"));
            loader.setControllerFactory(param -> new BuyPhaseController(db));
            parentStage.setScene(new Scene(loader.load()));
            parentStage.setTitle("Buy Phase: " + db.getGameMode());
            parentStage.show();

            BuyPhaseController controller = loader.getController();
            controller.setOnPhaseComplete(onPhaseComplete, parentStage);
            controller.loadUpgrades();
            controller.updateTopBar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the callback to run after the buy phase ends and links the proceed button to it.
     *
     * @param onComplete Callback to run after phase completion
     * @param stage      Stage representing this Buy Phase
     */
    public void setOnPhaseComplete(Runnable onComplete, Stage stage) {
        this.stage = stage; // assign the stage
        this.onPhaseComplete = () -> {
            if (this.stage != null) this.stage.close();
            if (onComplete != null) onComplete.run();
            database.setDay(database.getDay() + 1); // wrap day around to 1 if it exceeds the bounds of the max day.
            database.saveToFile();
            GameEngine.runSortPhase(); // next phase after buying
        };

        proceedButton.setOnAction(e -> {
            GameEngine.getSoundController().playButtonClick();
            if (onPhaseComplete != null) onPhaseComplete.run();
        });
    }


    /**
     * Loads all upgrades from the database that have not been purchased yet.
     */
    public void loadUpgrades() {
        unboughtUpgrades = database.getAllUpgrades().stream().filter(u -> !u.isBought()).collect(Collectors.toCollection(ArrayList::new));
        displayBuyMenu();
    }

    /**
     * Handles a player clicking to buy a specific upgrade.
     *
     * @param upgrade The upgrade being purchased
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
     * Displays all currently available (unbought) upgrades in the UI.
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
     * Displays a single upgrade in the shop UI with its title, ability, and Buy button.
     *
     * @param upgrade The upgrade to display
     */
    public void displayUpgrade(Upgrade upgrade) {
        VBox box = new VBox();
        box.setSpacing(4);
        box.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");

        Label title = new Label(upgrade.getName() + " (Cost: " + upgrade.getCost() + ")");
        Label ability = new Label("Ability: " + upgrade.getAbility());

        Button buyButton = new Button("Buy");
        buyButton.setOnAction(e -> {
            GameEngine.getSoundController().playButtonClick();
            onBuyUpgradeClickHandler(upgrade);
        });
        if (database.getGold() < upgrade.getCost()) {
            buyButton.setDisable(true);
        }
        box.getChildren().addAll(title, ability, buyButton);
        shopContainer.getChildren().add(box);
    }

}
