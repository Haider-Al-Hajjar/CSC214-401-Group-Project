package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SortPhaseController {

    private final Database database;
    private Runnable onPhaseComplete;

    private ArrayList<Item> unsortedItems;

    @FXML
    private VBox itemContainer; // Container for item UI
    @FXML
    private Button proceedButton;

    public SortPhaseController(Database database) {
        this.database = database;
    }

    public static void showSortPhase(Database db, Stage parentStage, Runnable onPhaseComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(SortPhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/sortPhase.fxml"));
            loader.setControllerFactory(param -> new SortPhaseController(db));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sort Phase");
            stage.show();

            SortPhaseController controller = loader.getController();
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
        unsortedItems = database.getUsedItems(); // or filter by today
        if (unsortedItems == null) {
            unsortedItems = new ArrayList<>(); // prevent NPE
        }

        displayItems();
    }

    private void displayItems() {
        itemContainer.getChildren().clear();
        for (Item item : unsortedItems) {
            // TODO: add UI node for each item
        }
    }
}
