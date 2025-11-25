package com.example.fantasysortinggame.salephase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SalePhaseController {

    private final Database database;
    private Runnable onPhaseComplete;
    private ArrayList<Item> unsoldItems;

    @FXML
    private VBox itemContainer;
    @FXML private Button proceedButton;

    public SalePhaseController(Database database) {
        this.database = database;
    }

    public static void showSalePhase(Database db, Stage parentStage, Runnable onPhaseComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(SalePhaseController.class.getResource("/path/to/SalePhase.fxml"));
            loader.setControllerFactory(param -> new SalePhaseController(db));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sale Phase");
            stage.show();

            SalePhaseController controller = loader.getController();
            controller.setOnPhaseComplete(onPhaseComplete, stage);
            controller.loadItems();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPhaseComplete(Runnable callback, Stage stage) {
        this.onPhaseComplete = () -> {
            stage.close();
            callback.run();
        };
        proceedButton.setOnAction(e -> {
            if (onPhaseComplete != null) onPhaseComplete.run();
        });
    }

    public void loadItems() {
        unsoldItems = database.getUsedItems().stream().filter(item -> !item.isSold()).collect(Collectors.toCollection(ArrayList::new));
        displaySaleMenu();
    }

    private void displaySaleMenu() {
        itemContainer.getChildren().clear();
        for (Item item : unsoldItems) {
            // TODO: create sell buttons and UI nodes
        }
    }

    public void onSellItem(Item item) {
        item.setSold(true);
        database.addGold(estimateItemValue(item));
        unsoldItems.remove(item);
        displaySaleMenu();
    }

    private double estimateItemValue(Item item) {
        return 30.0; // placeholder
    }
}
