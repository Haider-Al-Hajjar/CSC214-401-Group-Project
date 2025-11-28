package com.example.fantasysortinggame.phasecontrollers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class SortPhaseController {

    public ToggleButton completeViewButton;
    public ToggleButton loreViewButton;
    public ToggleButton imageViewButton;
    @FXML
    private Label dayLabel;
    @FXML
    Label totalGoldLabel;
    @FXML
    private HBox filterButtonContainer;

    @FXML
    private HBox viewButtonContainer;

    @FXML
    private VBox itemContainer;
    @FXML
    private Button proceedButton;

    private Database database;
    private Stage stage;
    private String currentView = "Complete"; // default
    private ArrayList<Item> items;
    private String currentFilter = "Unsorted"; // default shows unsorted
    private FilterNode rootFilterNode;

    private void createFilterButtons(FilterNode root) {
        filterButtonContainer.getChildren().clear();
        for (FilterNode child : root.children) {
            if (child.isLeaf()) {
                ToggleButton btn = new ToggleButton(child.name);
                btn.setOnAction(e -> {
                    currentFilter = child.name;
                    displayItems();
                });
                filterButtonContainer.getChildren().add(btn);
            } else {
                MenuButton menuBtn = new MenuButton(child.name);
                buildSubMenu(menuBtn, child);
                filterButtonContainer.getChildren().add(menuBtn);
            }
        }
    }

    private void buildSubMenu(MenuButton parent, FilterNode node) {
        for (FilterNode child : node.children) {
            if (child.isLeaf()) {
                MenuItem mi = new MenuItem(child.name);
                mi.setOnAction(e -> {
                    currentFilter = child.name;
                    displayItems();
                });
                parent.getItems().add(mi);
            } else {
                Menu subMenu = new Menu(child.name);
                buildSubMenuItems(subMenu, child);
                parent.getItems().add(subMenu);
            }
        }
    }

    private void buildSubMenuItems(Menu menu, FilterNode node) {
        for (FilterNode child : node.children) {
            if (child.isLeaf()) {
                MenuItem mi = new MenuItem(child.name);
                mi.setOnAction(e -> {
                    currentFilter = child.name;
                    displayItems();
                });
                menu.getItems().add(mi);
            } else {
                Menu subMenu = new Menu(child.name);
                buildSubMenuItems(subMenu, child);
                menu.getItems().add(subMenu);
            }
        }
    }

    @FXML
    public void initialize() {
        proceedButton.setVisible(false);
        proceedButton.setOnAction(e -> finishPhase());
    }

    private void createViewButtons(List<String> views) {
        viewButtonContainer.getChildren().clear();

        for (String viewName : views) {
            ToggleButton btn = new ToggleButton(viewName);
            btn.setOnAction(e -> {
                currentView = btn.getText();
                displayItems();
            });
            viewButtonContainer.getChildren().add(btn);
        }
    }

    private void updateTopBar() {
        if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
        if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
    }

    private FilterNode getItemSortCategoriesByDay(int day) {
        FilterNode root = new FilterNode("root"); // dummy root
        if (day <= 2) {
            root.children.add(new FilterNode("All"));
            root.children.add(new FilterNode("Unsorted"));
            root.children.add(new FilterNode("Junk"));
            root.children.add(new FilterNode("Treasure"));
        } else if (day <= 4) {
            root.children.add(new FilterNode("All"));
            root.children.add(new FilterNode("Unsorted"));
            root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("All Junk"), new FilterNode("Usable Junk"), new FilterNode("Broken Junk"), new FilterNode("Curious Junk"))));
            root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("All Treasure"), new FilterNode("Magical Treasure"), new FilterNode("Historical Treasure"), new FilterNode("Luxurious Treasure"))));
        } else {
            root.children.add(new FilterNode("All"));
            root.children.add(new FilterNode("Unsorted"));
            root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("All Junk"), new FilterNode("Usable Junk", Arrays.asList(new FilterNode("All Usable Junk"), new FilterNode("Consumable"), new FilterNode("Tools"), new FilterNode("Everyday"))), new FilterNode("Broken Junk", Arrays.asList(new FilterNode("All Broken Junk"), new FilterNode("Depleted"), new FilterNode("Rusted / Cracked"))), new FilterNode("Curious Junk", Arrays.asList(new FilterNode("All Curious Junk"), new FilterNode("Oddities"), new FilterNode("Crafting Materials"), new FilterNode("Collectibles"))))));
            root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("All Treasure"), new FilterNode("Magical Treasure", Arrays.asList(new FilterNode("All Magical Treasure"), new FilterNode("Artifacts"), new FilterNode("Cursed"), new FilterNode("Minor"))), new FilterNode("Historical Treasure", Arrays.asList(new FilterNode("All Historical Treasure"), new FilterNode("Relics"), new FilterNode("Keepsakes"), new FilterNode("Maps"))), new FilterNode("Luxurious Treasure", Arrays.asList(new FilterNode("All Luxurious Treasure"), new FilterNode("Jewelry"), new FilterNode("Fungible"), new FilterNode("Ornament"))))));
        }
        return root;
    }

    public void setDependencies(Database database, Stage stage) {
        this.database = database;
        this.stage = stage;

        rootFilterNode = getItemSortCategoriesByDay(database.getDay());
        createFilterButtons(rootFilterNode);

        createViewButtons(Arrays.asList("Complete", "Image", "Lore"));
        updateTopBar();
        loadItems();
    }

    private void loadItems() {
        if (database == null) return;

        ArrayList<Item> dayItems = database.getItems();
        if (dayItems == null) dayItems = new ArrayList<>();

        // Start with previously used items
        items = new ArrayList<>(database.getUsedItems());

        // Add new items if they aren’t already in 'items'
        for (Item newItem : dayItems) {
            boolean exists = false;
            for (Item oldItem : items) {
                if (oldItem.getName().equals(newItem.getName())) { // or some unique ID
                    exists = true;
                    break;
                }
            }
            if (!exists) items.add(newItem);
        }

        displayItems();
    }


    private void displayItems() {
        itemContainer.getChildren().clear();
        boolean allSorted = true;

        for (Item item : items) {
            if (item.isSold()) continue;
            if ("unsorted".equalsIgnoreCase(item.getItemSort())) allSorted = false;

            if (!"all".equalsIgnoreCase(currentFilter)) {
                FilterNode filterNode = findFilterNode(rootFilterNode, currentFilter);
                if (filterNode == null || !filterNode.matches(item.getItemSort())) continue;
            }


            HBox row = new HBox(10);

            if (currentView.equalsIgnoreCase("Complete")) {            // Image
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
                Label titleLabel = new Label(item.getTitle());
                Text descText = new Text(item.getDescription());

                Button sortButton = new Button(item.getItemSort());
                sortButton.setOnAction(ev -> showSortMenu(sortButton, item));

                infoBox.getChildren().addAll(titleLabel, descText, sortButton);
                row.getChildren().addAll(imageView, infoBox);
                itemContainer.getChildren().add(row);
            } else if (currentView.equalsIgnoreCase("Lore")) {
                // Info box
                VBox infoBox = new VBox(5);
                Label titleLabel = new Label(item.getTitle());
                Text descText = new Text(item.getDescription());

                Button sortButton = new Button(item.getItemSort());
                sortButton.setOnAction(ev -> showSortMenu(sortButton, item));

                infoBox.getChildren().addAll(titleLabel, descText, sortButton);
                row.getChildren().addAll(infoBox);
                itemContainer.getChildren().add(row);
            } else if (currentView.equalsIgnoreCase("Image")) {
                ImageView imageView = new ImageView();
                if (item.getImageLink() != null) {
                    try {
                        imageView.setImage(new javafx.scene.image.Image(item.getImageLink()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                Label titleLabel = new Label(item.getTitle());
                Button sortButton = new Button(item.getItemSort());
                VBox infoBox = new VBox(5);
                sortButton.setOnAction(ev -> showSortMenu(sortButton, item));

                infoBox.getChildren().addAll(titleLabel, imageView, sortButton);
                row.getChildren().addAll(infoBox);
                itemContainer.getChildren().add(row);
            }
        }

        proceedButton.setVisible(allSorted);
    }

    private void showSortMenu(Button button, Item item) {
        ContextMenu menu = new ContextMenu();
        int day = database.getDay();

        // Don't include "All" as an option for sorting the item itself
        if (day <= 2) {
            menu.getItems().addAll(createMenuItem("Unsorted", button, item), createMenuItem("Junk", button, item), createMenuItem("Treasure", button, item));
        } else if (day <= 4) {
            menu.getItems().addAll(createMenuItem("Unsorted", button, item), createSubMenu("Junk", new String[]{"Usable Junk", "Broken Junk", "Curious Junk"}, button, item), createSubMenu("Treasure", new String[]{"Magical Treasure", "Historical Treasure", "Luxurious Treasure"}, button, item));
        } else {
            menu.getItems().addAll(createMenuItem("Unsorted", button, item), createSubSubMenu("Junk", new String[][]{{"Usable Junk", "Broken Junk", "Curious Junk"}, {"Consumable", "Tools", "Everyday"}, {"Depleted", "Rusted / Cracked"}, {"Oddities", "Crafting Materials", "Collectibles"}}, button, item), createSubSubMenu("Treasure", new String[][]{{"Magical Treasure", "Historical Treasure", "Luxurious Treasure"}, {"Artifacts", "Cursed / Dangerous", "Minor / Utility Magic"}, {"Relics", "Keepsakes", "Documents / Maps"}, {"Jewelry", "Hoardable", "Decorative / Ornamental"}}, button, item));
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

    private MenuItem createSubSubMenu(String name, String[][] subSubItems, Button button, Item item) {
        Menu menu = new Menu(name);
        for (int i = 1; i <= subSubItems[0].length; i++) {
            menu.getItems().add(createSubMenu(subSubItems[0][i - 1], subSubItems[i], button, item));
        }
        return menu;
    }


    private void finishPhase() {
        if (stage != null) stage.close();
        database.saveToFile();          // usedItems already contains all items
        GamePhaseManager.runSalePhase();
    }

    private FilterNode findFilterNode(FilterNode node, String filterName) {
        if (node.name.equalsIgnoreCase(filterName)) return node;
        for (FilterNode child : node.children) {
            FilterNode result = findFilterNode(child, filterName);
            if (result != null) return result;
        }
        return null;
    }

    public static void showSortPhase(Database db, Stage primaryStage) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(SortPhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/sortPhase.fxml"));
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

    class FilterNode {
        String name;
        List<FilterNode> children;

        public FilterNode(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public FilterNode(String name, List<FilterNode> children) {
            this.name = name;
            this.children = children == null ? new ArrayList<>() : children;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * Main matching entry.
         * - exact name match (case-insensitive)
         * - "All" at root -> match everything
         * - "All X" -> find node named X in the master tree and match anything in X's subtree
         * - otherwise: check descendants normally
         */
        public boolean matches(String itemSort) {
            if (itemSort == null) return false;

            // Exact match
            if (itemSort.equalsIgnoreCase(this.name)) return true;

            String lowerName = name == null ? "" : name.toLowerCase();

            // Root-level "All" (treat as global wildcard)
            if (lowerName.equals("all")) return true;

            // "All X" case: locate the node named X in the main tree and test that subtree
            if (lowerName.startsWith("all ")) {
                String target = name.substring(4).trim(); // "All Junk" -> "Junk"
                if (target.isEmpty()) return false;

                // Find the canonical node for target under the root of the whole tree
                FilterNode canonical = SortPhaseController.this.findFilterNode(SortPhaseController.this.rootFilterNode, target);
                if (canonical == null) return false;

                return subtreeMatch(canonical, itemSort);
            }

            // Normal recursive check through descendants
            for (FilterNode child : children) {
                if (child.matches(itemSort)) return true;
            }

            return false;
        }

        // Recursively search a node's subtree for a match (case-insensitive)
        private boolean subtreeMatch(FilterNode node, String itemSort) {
            if (node == null || itemSort == null) return false;
            if (itemSort.equalsIgnoreCase(node.name)) return true;
            for (FilterNode child : node.children) {
                if (subtreeMatch(child, itemSort)) return true;
            }
            return false;
        }
    }

}
