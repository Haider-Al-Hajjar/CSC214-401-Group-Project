package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SortPhaseController {

    private Database database;
    private Stage stage;

    private ArrayList<Item> unsortedItems;

    @FXML
    private VBox itemContainer; // Container for item UI
    @FXML
    private Button proceedButton;

    /** Empty constructor needed by FXMLLoader */
    public SortPhaseController() {}

    /** Called automatically by FXMLLoader */
    @FXML
    public void initialize() {
        // Hook up the proceed button
        proceedButton.setOnAction(e -> finishPhase());
    }

    /** Set dependencies after FXML load */
    public void setDependencies(Database database, Stage stage) {
        this.database = database;
        this.stage = stage;

        // Load items now that dependencies are available
        loadItems();
    }

    private void finishPhase() {
        if (stage != null) stage.close();
        GamePhaseManager.runSalePhase();
    }

    private void loadItems() {
        if (database == null) return;

        unsortedItems = database.getUsedItems();
        if (unsortedItems == null) unsortedItems = new ArrayList<>();
        displayItems();
    }

    private void displayItems() {
        itemContainer.getChildren().clear();
        for (Item item : unsortedItems) {
            VBox itemBox = new VBox(5);

            Label titleLabel = new Label(item.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label descLabel = new Label(item.getDescription());
            Label typeLabel = new Label("Type: " + (item.getItemType() != null ? item.getItemType().getItemType() : "Unknown"));
            Label valueLabel = new Label("Value: " + item.getValue());

            itemBox.getChildren().addAll(titleLabel, descLabel, typeLabel, valueLabel);
            itemBox.setStyle("-fx-border-color: gray; -fx-padding: 5; -fx-background-color: #f0f0f0;");

            itemContainer.getChildren().add(itemBox);
        }
    }

    /** Static helper to show this phase */
    public static void showSortPhase(Database db, Stage primaryStage) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    SortPhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/sortPhase.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loader.load()));
            stage.setTitle("Sort Phase");

            SortPhaseController controller = loader.getController();
            controller.setDependencies(db, stage);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
