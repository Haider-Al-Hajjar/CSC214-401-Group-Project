package com.example.fantasysortinggame.salephase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SalePhaseController {

    private final Database database;
    private ArrayList<Item> unsoldItems;
    private Stage stage;

    @FXML
    private VBox itemContainer;
    @FXML
    private Button proceedButton;

    public SalePhaseController(Database database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        proceedButton.setVisible(false);
        proceedButton.setOnAction(e -> finishPhase());
    }

    public static void showSalePhase(Database db, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(SalePhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/SalePhase.fxml"));
            loader.setControllerFactory(param -> new SalePhaseController(db));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sale Phase");
            stage.show();

            SalePhaseController controller = loader.getController();
            controller.setStage(stage);
            controller.loadItems();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadItems() {
        unsoldItems = database.getUsedItems()
                .stream()
                .filter(item -> !item.isSold())
                .collect(Collectors.toCollection(ArrayList::new));
        displaySaleMenu();
    }

    private void displaySaleMenu() {
        itemContainer.getChildren().clear();

        boolean allSold = unsoldItems.isEmpty();

        for (Item item : unsoldItems) {
            HBox row = new HBox(10);

            // Item Image
            ImageView imageView = new ImageView();
            if (item.getImageLink() != null) {
                try {
                    imageView.setImage(new Image(item.getImageLink().toURI().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            // Item Info
            VBox infoBox = new VBox(5);
            Label nameLabel = new Label(item.getTitle());
            Label descLabel = new Label(item.getDescription());
            Label typeLabel = new Label("Type: " + item.getItemSort());
            Label valueLabel = new Label("Value: " + estimateItemValue(item) + " gold");

            infoBox.getChildren().addAll(nameLabel, descLabel, typeLabel, valueLabel);

            // Sell Button
            Button sellButton = new Button("Sell for " + estimateItemValue(item) + " gold");
            sellButton.setOnAction(e -> onSellItem(item));

            row.getChildren().addAll(imageView, infoBox, sellButton);
            itemContainer.getChildren().add(row);
        }

        proceedButton.setVisible(allSold); // enable when all items sold
    }

    public void onSellItem(Item item) {
        item.setSold(true);
        database.addGold(estimateItemValue(item));
        unsoldItems.remove(item);
        displaySaleMenu();
    }

    private double estimateItemValue(Item item) {
        // Bonus for correctly sorted items
        double baseValue = 30.0;
        if (!item.getItemSort().equals("Unsorted")) baseValue *= 1.5;
        return baseValue;
    }

    private void finishPhase() {
        if (stage != null) stage.close();
        GamePhaseManager.runBuyPhase();
    }
}
