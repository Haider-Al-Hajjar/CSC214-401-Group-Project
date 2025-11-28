package com.example.fantasysortinggame.phasecontrollers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortPhaseController {

    public ToggleButton completeViewButton;
    public ToggleButton loreViewButton;
    public ToggleButton imageViewButton;
    @FXML
    private Label dayLabel;
    @FXML
    Label totalGoldLabel;
    @FXML
    public ToggleButton allFilterButton;
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

    private Database database;
    private Stage stage;
    private String currentView = "Complete"; // default
    private ArrayList<Item> items;
    private String currentFilter = "Unsorted"; // default shows unsorted
    private static final Map<String, Object> CATEGORY_TREE = Map.of(
            "Junk", Map.of(
                    "Usable Junk", List.of("Consumable", "Tools", "Everyday"),
                    "Broken Junk", List.of("Depleted", "Rusted / Cracked", "Miscellaneous"),
                    "Curious Junk", List.of("Oddities", "Crafting Materials", "Collectibles")
            ),
            "Treasure", Map.of(
                    "Artifacts", List.of("Cursed / Dangerous", "Minor / Utility Magic"),
                    "Historical Treasure", List.of("Relics", "Keepsakes", "Documents / Maps"),
                    "Luxurious Treasure", List.of("Jewelry", "Treasure Hoard", "Decorative / Ornamental")
            )
    );

    @FXML
    public void initialize() {
        proceedButton.setVisible(false);

        // Setup filter buttons
        allFilterButton.setOnAction(e -> {
            currentFilter = "All";
            displayItems();
        });

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
        completeViewButton.setOnAction(e -> {
            currentView = "Complete";
            displayItems();
        });

        imageViewButton.setOnAction(e -> {
            currentView = "Image";
            displayItems();
        });

        loreViewButton.setOnAction(e -> {
            currentView = "Lore";
            displayItems();
        });

        proceedButton.setOnAction(e -> finishPhase());
    }

    private void updateTopBar() {
        if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
        if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
    }

    public void setDependencies(Database database, Stage stage) {
        this.database = database;
        this.stage = stage;
        loadItems();
        updateTopBar();

        Dialogue triggered =
                database.getTriggeredDialogue();
        if (triggered != null) {
            DialogueBoxController.showDialogueWindow(database, triggered);
        }

    }

    private void loadItems() {
        if (database == null) return;
        items = database.getItems();
        if (items == null) items = new ArrayList<>();
        if (database.upgradeIsBought("Little Helper")) {
            items.getFirst().setItemSort(items.getFirst().getItemTypeValue());
        }
        displayItems();
    }

    private MenuItem buildMenuTree(String label, Object node, Button button, Item item) {
        if (node instanceof List<?> list) {
            // Leaf list → create a submenu with direct menu items
            Menu menu = new Menu(label);
            for (Object o : list) {
                menu.getItems().add(createMenuItem(o.toString(), button, item));
            }
            return menu;

        } else if (node instanceof Map<?, ?> map) {
            // Nested structure → recursive menus
            Menu menu = new Menu(label);
            for (var entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object child = entry.getValue();
                menu.getItems().add(buildMenuTree(key, child, button, item));
            }
            return menu;
        }


        // Unexpected type
        return createMenuItem(label, button, item);
    }

    private void displayItems() {
        itemContainer.getChildren().clear();

        boolean allSorted = true;

        for (Item item : items) {
            if (item.isSold()) {
                continue;
            }
            if ("unsorted".equalsIgnoreCase(item.getItemSort())) allSorted = false;

            // Apply strict filter
            if (!"all".equalsIgnoreCase(currentFilter) && !item.getItemSort().equalsIgnoreCase(currentFilter)) continue;

            HBox row = new HBox(10);

            // Image
            ImageView imageView = new ImageView();
            if (item.getImageLink() != null) {
                try {
                    imageView.setImage(new javafx.scene.image.Image(item.getImageLink()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);

            // Info box
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

        proceedButton.setVisible(allSorted);
    }

    private void showSortMenu(Button button, Item item) {
        ContextMenu menu = new ContextMenu();
        int day = database.getDay();

        if (day <= 2) {
            menu.getItems().addAll(
                    createMenuItem("Unsorted", button, item),
                    createMenuItem("Junk", button, item),
                    createMenuItem("Treasure", button, item)
            );
        } else if (day <= 4) {
            menu.getItems().addAll(
                    createSubMenu("Junk", new String[]{"Usable Junk", "Broken Junk", "Curious Junk"}, button, item),
                    createSubMenu("Treasure", new String[]{"Magical Treasure", "Historical Treasure", "Luxurious Treasure"}, button, item),
                    createMenuItem("Unsorted", button, item)
            );
        } else { // Day 5+
            for (var entry : CATEGORY_TREE.entrySet()) {
                menu.getItems().add(
                        buildMenuTree(entry.getKey(), entry.getValue(), button, item)
                );
            }

            menu.getItems().add(createMenuItem("Unsorted", button, item));
        }

        menu.show(button, Side.BOTTOM, 0, 0);
    }

    private MenuItem createMenuItem(String name, Button button, Item item) {
        MenuItem mi = new MenuItem(name);
        mi.setOnAction(e -> {
            item.setItemSort(name);
            button.setText(name);
            displayItems();
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
                displayItems();
            });
            menu.getItems().add(mi);
        }
        return menu;
    }

    private void finishPhase() {
        if (stage != null) stage.close();
        database.getUsedItems().addAll(items);
        database.saveToFile();
        GamePhaseManager.runSalePhase(); // moves to the next phase
    }

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
