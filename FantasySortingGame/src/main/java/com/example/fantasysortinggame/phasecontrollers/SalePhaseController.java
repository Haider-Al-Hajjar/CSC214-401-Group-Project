package com.example.fantasysortinggame.phasecontrollers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class SalePhaseController {

    private Database database;
    private Stage stage;
    @FXML
    private Label dayLabel;
    @FXML
    public Button allButton;
    @FXML
    private VBox itemContainer;
    @FXML
    private Button proceedButton;
    @FXML
    private Label totalGoldLabel;
    @FXML
    private Button junkButton;
    @FXML
    private Button treasureButton;


    private ArrayList<Item> items;
    private String currentFilter = "All";

    @FXML
    public void initialize() {
        allButton.setOnAction(e -> {
            currentFilter = "All";
            displayItems();
        });
        junkButton.setOnAction(e -> {
            currentFilter = "Junk";
            displayItems();
        });

        treasureButton.setOnAction(e -> {
            currentFilter = "Treasure";
            displayItems();
        });
        proceedButton.setOnAction(e -> finishPhase());
    }

    public void setDependencies(Database database, Stage stage) {
        this.database = database;
        this.stage = stage;
        loadItems();
        updateTopBar();
    }

    private void finishPhase() {
        if (stage != null) stage.close();
        database.saveToFile();
        GamePhaseManager.runBuyPhase();
    }

    private void loadItems() {
        items = database.getUsedItems();
        if (items == null) items = new ArrayList<>();
        displayItems();
    }

    private void displayItems() {
        itemContainer.getChildren().clear();
        for (Item item : items) {
            if (item.isSold()) continue; // skip sold items
            // Filtering
            if (!"All".equalsIgnoreCase(currentFilter)) {
                if (!item.getItemSort().equalsIgnoreCase(currentFilter)) continue;
            }

            HBox row = new HBox(10);

            ImageView imageView = new ImageView();
            if (item.getImageLink() != null) {
                try {
                    imageView.setImage(new Image(item.getImageLink()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);

            VBox infoBox = new VBox(5);
            Label titleLabel = new Label(item.getTitle());
            Label descLabel = new Label(item.getDescription());
            Label valueLabel = new Label("Value: " + estimateItemValue(item) + " gold");

            double baseValue = estimateItemValue(item);
            double bonus = calculateCorrectSortBonus(item);
            double totalValue = baseValue + bonus;

            Button sellButton = new Button(
                    "Sell for " + totalValue + " gold" +
                            (bonus > 0 ? " (Correct Sort Bonus: +" + bonus + ")" : "")
            );
            sellButton.setOnAction(e -> sellItem(item));

            infoBox.getChildren().addAll(titleLabel, descLabel, valueLabel, sellButton);
            row.getChildren().addAll(imageView, infoBox);
            itemContainer.getChildren().add(row);
        }
    }

    private void sellItem(Item item) {
        item.setSold(true);
        if (database.upgradeIsBought("Haggler's Hat")) {
            database.addGold(((estimateItemValue(item)+calculateCorrectSortBonus(item)) * 1.15));
        } else {
            database.addGold(estimateItemValue(item));
        }
        displayItems();
        updateTopBar();
    }
    private double estimateItemValue(Item item) {
        return 15.0; // base value
    }

    private double calculateCorrectSortBonus(Item item) {
        if (item.getItemSort().equalsIgnoreCase(item.getItemTypeValue())) {
            return estimateItemValue(item) * 0.5; // 50% bonus
        }
        return 0;
    }


    private void updateTopBar() {
        if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
        if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
    }

    public static void showSalePhase(Database db, Stage parentStage) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    SalePhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/SalePhase.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.setTitle("Sale Phase");

            SalePhaseController controller = loader.getController();
            controller.setDependencies(db, stage);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
