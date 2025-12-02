package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.*;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalePhaseController {

    @FXML
    Label dayLabel;
    @FXML
    Label totalGoldLabel;
    @FXML
    HBox filterButtonContainer;
    @FXML
    VBox itemContainer;
    @FXML
    Button proceedButton;
    @FXML
    private AnchorPane dialogueContainer;

    private Database database;
    private Stage stage;
    private ArrayList<Item> items;
    String currentFilter = "All";
    FilterNode rootFilterNode;
    private Runnable onPhaseComplete;
    final double MIN_ITEM_VALUE = 10;
    public static void showSalePhase(Database db, Stage parentStage, Runnable onComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(SalePhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/SalePhase.fxml"));
            parentStage.setScene(new Scene(loader.load()));
            parentStage.setTitle("Sale Phase: " + db.getGameMode());

            SalePhaseController controller = loader.getController();
            controller.setDependencies(db, parentStage, onComplete);

            parentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDependencies(Database database, Stage stage, Runnable onComplete) {
        this.database = database;
        this.stage = stage;
        this.onPhaseComplete = onComplete;

        updateTopBar();
        Dialogue onStartSalePhaseDialogue = database.getTriggeredDialogue();
        if (onStartSalePhaseDialogue != null) {
            showDialogue(onStartSalePhaseDialogue);
        }

        items = database.getUsedItems();
        if (items == null) {
            items = new ArrayList<>();
        }

        rootFilterNode = getItemSortCategoriesByDay(database.getDayInBound());
        createFilterButtons(rootFilterNode);
        // Ensure proceed button works
        proceedButton.setOnAction(e -> {
            GameEngine.getSoundController().playButtonClick();
            finishPhase();
        });
        updateTopBar();
        displayItems();
    }

    FilterNode getItemSortCategoriesByDay(int day) {
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
            root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("Usable Junk", Arrays.asList(new FilterNode("Consumable"), new FilterNode("Tools"), new FilterNode("Everyday"))), new FilterNode("Broken Junk", Arrays.asList(new FilterNode("Depleted"), new FilterNode("Weathered"))), new FilterNode("Curious Junk", Arrays.asList(new FilterNode("Oddities"), new FilterNode("Crafting Materials"), new FilterNode("Collectibles"))))));
            root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("Magical Treasure", Arrays.asList(new FilterNode("Artifacts"), new FilterNode("Cursed"), new FilterNode("Minor"))), new FilterNode("Historical Treasure", Arrays.asList(new FilterNode("Relics"), new FilterNode("Keepsakes"), new FilterNode("Maps"))), new FilterNode("Luxurious Treasure", Arrays.asList(new FilterNode("Jewelry"), new FilterNode("Fungible"), new FilterNode("Ornament"))))));
        }
        return root;
    }


    private void createFilterButtons(FilterNode root) {
        filterButtonContainer.getChildren().clear();
        for (FilterNode child : root.children) {
            if (child.isLeaf()) {
                ToggleButton btn = new ToggleButton(child.name);
                btn.setOnAction(e -> {
                    GameEngine.getSoundController().playButtonClick();
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
                    GameEngine.getSoundController().playButtonClick();
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
                    GameEngine.getSoundController().playButtonClick();
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

    void updateTopBar() {
        if (dayLabel != null) {
            dayLabel.setText("Day: " + database.getDay());
        }
        if (totalGoldLabel != null) {
            totalGoldLabel.setText("Gold: " + database.getGold());
        }
    }

    void displayItems() {
        itemContainer.getChildren().clear();

        for (Item item : items) {
            if (item.isSold()) {
                continue;
            }

            FilterNode filterNode = findFilterNode(rootFilterNode, currentFilter);
            if (filterNode != null && !filterNode.matches(item.getItemSort())) {
                continue;
            }

            HBox row = new HBox(10);
            VBox infoBox = new VBox(5);
            Label titleLabel = new Label(item.getTitle());
            Label descLabel = new Label(item.getDescription());

            double baseValue = estimateItemValue(item);
            double bonus = calculateCorrectSortBonus(item);
            double totalValue = baseValue + bonus;

            Label valueLabel = new Label("Value: " + totalValue + " gold" + (bonus > 0 ? " (Correct Sort Bonus: +" + bonus + ")" : ""));
            Button sellButton = new Button("Sell for " + totalValue + " gold");
            sellButton.setOnAction(e -> {
                GameEngine.getSoundController().playButtonClick();
                sellItem(item);
            });
            infoBox.getChildren().addAll(titleLabel, descLabel, valueLabel, sellButton);
            row.getChildren().add(infoBox);
            itemContainer.getChildren().add(row);
        }
    }

    private FilterNode findFilterNode(FilterNode node, String filterName) {
        if (node.name.equalsIgnoreCase(filterName)) {
            return node;
        }
        for (FilterNode child : node.children) {
            FilterNode result = findFilterNode(child, filterName);
            if (result != null) return result;
        }
        return null;
    }

    void sellItem(Item item) {
        if (item.getEvents() != null && !item.getEvents().isEmpty()) {
            for (StoryEvent event : item.getEvents()) {
                if (event.hasHappened()) continue;

                for (StoryEventTrigger trigger : event.getStoryEventTriggers()) {
                    // Pass null for sort because this is a sale context
                    // Only trigger if the event cares about sold items
                    if (trigger.isTriggered(database, null, true) && trigger.happensOnSale()) {
                        event.setHappened(true);

                        if (event instanceof QuickTimeEvent qte) {
                            QuickTimeEventController.showQuickTimeEventWindow(qte, items, stage);
                        } else if (event instanceof Dialogue dialogue) {
                            showDialogue(dialogue);
                        }
                        displayItems();
                        return; // Only trigger one event per sale, and do not complete sale if it happens.
                    }
                }
            }
        }
        item.setSold(true);
        double gain = estimateItemValue(item) + calculateCorrectSortBonus(item);
        if (database.upgradeIsBought("Haggler's Hat")) gain *= 1.15;
        database.addGold(gain);

        displayItems();
        updateTopBar();
    }

    private double estimateItemValue(Item item) {
        return (Math.max(item.getValue(), MIN_ITEM_VALUE));
    }

    double calculateCorrectSortBonus(Item item) {
        if (item.getItemSort().equalsIgnoreCase(item.getItemTypeValue())) {
            return estimateItemValue(item) * 0.5;
        }
        return 0;
    }

    public void showDialogue(Dialogue dialogue) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/fantasysortinggame/fxmlfiles/dialogueBox.fxml")
            );
            loader.setControllerFactory(param -> new DialogueBoxController(database));
            AnchorPane dialogueUI = loader.load();
            DialogueBoxController controller = loader.getController();

            dialogueContainer.getChildren().setAll(dialogueUI);

            // Disable rest of UI while dialogue runs
            itemContainer.setDisable(true);
            filterButtonContainer.setDisable(true);
            proceedButton.setDisable(true);

            controller.runDialogue(dialogue);

            controller.setOnDialogueEnd(() -> {
                itemContainer.setDisable(false);
                filterButtonContainer.setDisable(false);
                proceedButton.setDisable(false);
                dialogueContainer.getChildren().clear();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void finishPhase() {
        if (stage != null) {
            stage.close();
        }
        database.saveToFile();
        if (onPhaseComplete != null) {
            onPhaseComplete.run();
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
}
