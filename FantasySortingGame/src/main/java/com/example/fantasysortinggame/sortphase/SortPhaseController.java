package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SortPhaseController {

    private Database database;
    private Stage stage;

    @FXML
    private ToggleButton unsortedFilterButton;
    @FXML
    private ToggleButton junkFilterButton;
    @FXML
    private ToggleButton treasureFilterButton;

    @FXML
    private VBox itemContainer;
    @FXML
    private Button proceedButton;

    private ArrayList<Item> unsortedItems;
    private String currentFilter = "All"; // default shows all

    /**
     * Empty constructor needed by FXMLLoader
     */
    public SortPhaseController() {
    }

    /**
     * Called automatically by FXMLLoader
     */
    @FXML
    public void initialize() {
        proceedButton.setVisible(false); // hidden initially

        // Set up filter buttons
        unsortedFilterButton.setOnAction(e -> {
            currentFilter = "Unsorted";
            displayItems();
        });
        junkFilterButton.setOnAction(e -> {
            currentFilter = "Junk";
            displayItems();
        });
        treasureFilterButton.setOnAction(e -> {
            currentFilter = "Treasure";
            displayItems();
        });

        // Hook up the end phase button
        proceedButton.setOnAction(e -> finishPhase());
    }

    /**
     * Set dependencies after FXML load
     */
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

        boolean allSorted = true;

        for (Item item : unsortedItems) {
            if (item.getItemSort().equals("Unsorted")) allSorted = false;

            // Apply filter
            if (!currentFilter.equals("All") && !item.getItemSort().equals(currentFilter)) continue;

            HBox row = new HBox(10);

            ImageView imageView = new ImageView();
            if (item.getImageLink() != null) {
                try {
                    imageView.setImage(new javafx.scene.image.Image(item.getImageLink().toURI().toString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);

            VBox infoBox = new VBox(5);
            TextField titleField = new TextField(item.getTitle());
            titleField.setEditable(false);
            TextField descField = new TextField(item.getDescription());
            descField.setEditable(false);

            Button sortButton = new Button(item.getItemSort());
            sortButton.setOnAction(ev -> showSortMenu(sortButton, item));

            infoBox.getChildren().addAll(titleField, descField, sortButton);
            row.getChildren().addAll(imageView, infoBox);

            itemContainer.getChildren().add(row);
        }

        // Show proceed button only if all items are sorted
        proceedButton.setVisible(allSorted);
    }

    private void showSortMenu(Button button, Item item) {
        ContextMenu menu = new ContextMenu();

        int currentDay = database.getDay();

        if (currentDay <= 2) {
            menu.getItems().addAll(
                    createMenuItem("Junk", button, item),
                    createMenuItem("Treasure", button, item),
                    createMenuItem("Unsorted", button, item)
            );
        } else if (currentDay <= 4) {
            menu.getItems().addAll(
                    createSubMenu("Junk", new String[]{"Usable Junk", "Broken Junk", "Curious Junk"}, button, item),
                    createSubMenu("Treasure", new String[]{"Magical Treasure", "Historical Treasure", "Luxurious Treasure"}, button, item),
                    createMenuItem("Unsorted", button, item)
            );
        } else {
            menu.getItems().addAll(
                    createSubMenu("Junk", new String[]{
                            "Usable Junk", "Consumable", "Tools", "Everyday",
                            "Broken Junk", "Depleted", "Rusted / Cracked",
                            "Curious Junk", "Oddities", "Crafting Materials", "Collectibles"
                    }, button, item),
                    createSubMenu("Treasure", new String[]{
                            "Artifacts: Cursed / Dangerous", "Artifacts: Minor / Utility Magic",
                            "Historical Treasure: Relics", "Historical Treasure: Keepsakes", "Historical Treasure: Documents / Maps",
                            "Luxurious Treasure: Jewelry", "Luxurious Treasure: Treasure Hoard", "Luxurious Treasure: Decorative / Ornamental"
                    }, button, item),
                    createMenuItem("Unsorted", button, item)
            );
        }

        menu.show(button, Side.BOTTOM, 0, 0);
    }

    private MenuItem createMenuItem(String name, Button button, Item item) {
        MenuItem mi = new MenuItem(name);
        mi.setOnAction(e -> {
            item.setItemSort(name);
            button.setText(name);
            displayItems(); // refresh list to update End Phase button visibility
        });
        return mi;
    }

    private Menu createSubMenu(String name, String[] subItems, Button button, Item item) {
        Menu menu = new Menu(name);
        for (String sub : subItems) {
            MenuItem mi = new MenuItem(sub);
            mi.setOnAction(e -> {
                item.setItemSort(sub);
                button.setText(sub);
                displayItems(); // refresh list to update End Phase button visibility
            });
            menu.getItems().add(mi);
        }
        return menu;
    }

    /**
     * Static helper to show this phase
     */
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
