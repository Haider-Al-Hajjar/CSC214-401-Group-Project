package com.example.fantasysortinggame.phasecontrollers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalePhaseController {

    @FXML
    private Label dayLabel;
    @FXML
    private Label totalGoldLabel;
    @FXML
    private HBox filterButtonContainer;
    @FXML
    private VBox itemContainer;
    @FXML
    private Button proceedButton;

    private Database database;
    private Stage stage;
    private ArrayList<Item> items;
    private String currentFilter = "All";
    private FilterNode rootFilterNode;

    @FXML
    public void initialize() {
        proceedButton.setOnAction(e -> finishPhase());
    }

    public void setDependencies(Database database, Stage stage) {
        this.database = database;
        this.stage = stage;
        updateTopBar();

        Dialogue triggered = database.getTriggeredDialogue();
        if (triggered != null) {
            DialogueBoxController.showDialogueWindow(database, triggered);
        }


    items =database.getUsedItems();
        if(items ==null)items =new ArrayList<>();

    rootFilterNode =

    getItemSortCategoriesByDay(database.getDay());

    createFilterButtons(rootFilterNode);

    // Ensure proceed button works
        proceedButton.setOnAction(e ->

    finishPhase());

    updateTopBar();

    displayItems();
}

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

private void displayItems() {
    itemContainer.getChildren().clear();

    for (Item item : items) {
        if (item.isSold()) continue;

        FilterNode filterNode = findFilterNode(rootFilterNode, currentFilter);
        if (filterNode != null && !filterNode.matches(item.getItemSort())) continue;

        HBox row = new HBox(10);
        VBox infoBox = new VBox(5);

        Label titleLabel = new Label(item.getTitle());
        Label descLabel = new Label(item.getDescription());

        double baseValue = estimateItemValue(item);
        double bonus = calculateCorrectSortBonus(item);
        double totalValue = baseValue + bonus;

        Label valueLabel = new Label("Value: " + totalValue + " gold" + (bonus > 0 ? " (Correct Sort Bonus: +" + bonus + ")" : ""));

        Button sellButton = new Button("Sell for " + totalValue + " gold");
        sellButton.setOnAction(e -> sellItem(item));

        infoBox.getChildren().addAll(titleLabel, descLabel, valueLabel, sellButton);
        row.getChildren().add(infoBox);
        itemContainer.getChildren().add(row);
    }
}

private void sellItem(Item item) {
    item.setSold(true);
    double gain = estimateItemValue(item) + calculateCorrectSortBonus(item);
    if (database.upgradeIsBought("Haggler's Hat")) gain *= 1.15;
    database.addGold(gain);

    displayItems();
    updateTopBar();
}

private double estimateItemValue(Item item) {
    return 1.0;
}

private double calculateCorrectSortBonus(Item item) {
    if (item.getItemSort().equalsIgnoreCase(item.getItemTypeValue())) return estimateItemValue(item) * 0.5;
    return 0;
}

private void updateTopBar() {
    if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
    if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
}

private void finishPhase() {
    if (stage != null) stage.close();
    database.saveToFile();
    GamePhaseManager.runBuyPhase();
}

private FilterNode findFilterNode(FilterNode node, String filterName) {
    if (node.name.equalsIgnoreCase(filterName)) return node;
    for (FilterNode child : node.children) {
        FilterNode result = findFilterNode(child, filterName);
        if (result != null) return result;
    }
    return null;
}

private FilterNode getItemSortCategoriesByDay(int day) {
    FilterNode root = new FilterNode("root");
    if (day <= 2) {
        root.children.add(new FilterNode("All"));
        root.children.add(new FilterNode("Junk"));
        root.children.add(new FilterNode("Treasure"));
    } else if (day <= 4) {
        root.children.add(new FilterNode("All"));
        root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("Usable Junk"), new FilterNode("Broken Junk"), new FilterNode("Curious Junk"))));
        root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("Magical Treasure"), new FilterNode("Historical Treasure"), new FilterNode("Luxurious Treasure"))));
    } else {
        root.children.add(new FilterNode("All"));
        root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("Usable Junk", Arrays.asList(new FilterNode("Consumable"), new FilterNode("Tools"), new FilterNode("Everyday"))), new FilterNode("Broken Junk", Arrays.asList(new FilterNode("Depleted"), new FilterNode("Rusted / Cracked"))), new FilterNode("Curious Junk", Arrays.asList(new FilterNode("Oddities"), new FilterNode("Crafting Materials"), new FilterNode("Collectibles"))))));
        root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("Magical Treasure", Arrays.asList(new FilterNode("Artifacts"), new FilterNode("Cursed"), new FilterNode("Minor"))), new FilterNode("Historical Treasure", Arrays.asList(new FilterNode("Relics"), new FilterNode("Keepsakes"), new FilterNode("Maps"))), new FilterNode("Luxurious Treasure", Arrays.asList(new FilterNode("Jewelry"), new FilterNode("Fungible"), new FilterNode("Ornament"))))));
    }
    return root;
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

    public boolean matches(String itemSort) {
        if (itemSort == null) return false;

        // Exact match
        if (itemSort.equalsIgnoreCase(this.name)) return true;

        String lowerName = name == null ? "" : name.toLowerCase();

        // Root-level "All" -> match everything
        if (lowerName.equals("all")) return true;

        // "All X" -> match anything in X's canonical subtree
        if (lowerName.startsWith("all ")) {
            String target = name.substring(4).trim();
            if (target.isEmpty()) return false;

            FilterNode canonical = SalePhaseController.this.findFilterNode(SalePhaseController.this.rootFilterNode, target);
            if (canonical == null) return false;

            return subtreeMatch(canonical, itemSort);
        }

        // Normal recursive check through children
        for (FilterNode child : children) {
            if (child.matches(itemSort)) return true;
        }

        return false;
    }

    private boolean subtreeMatch(FilterNode node, String itemSort) {
        if (node == null || itemSort == null) return false;

        // Skip nodes whose name starts with "All"
        if (!node.name.toLowerCase().startsWith("all ") && itemSort.equalsIgnoreCase(node.name)) return true;

        for (FilterNode child : node.children) {
            if (subtreeMatch(child, itemSort)) return true;
        }
        return false;
    }

}

public static void showSalePhase(Database db, Stage parentStage) {
    try {
        FXMLLoader loader = new FXMLLoader(SalePhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/SalePhase.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Sale Phase");

        SalePhaseController controller = loader.getController();
        controller.setDependencies(db, stage);

        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
