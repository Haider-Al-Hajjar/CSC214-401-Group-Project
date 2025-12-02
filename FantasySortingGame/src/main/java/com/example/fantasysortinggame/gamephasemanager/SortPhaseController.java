package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.*;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.*;

public class SortPhaseController {

    @FXML
    private Label dayLabel;
    @FXML
    private Label totalGoldLabel;
    @FXML
    private HBox filterButtonContainer;
    @FXML
    private HBox viewButtonContainer;
    @FXML
    private VBox itemContainer;
    @FXML
    private Button proceedButton;
    @FXML
    private AnchorPane dialogueContainer;

    private Database database;
    private Stage stage;
    private String currentView = "Complete"; // default
    private ArrayList<Item> items;
    private String currentFilter = "Unsorted"; // default shows unsorted
    private FilterNode rootFilterNode;
    private Runnable onPhaseComplete;


    /**
     * Initializes the controller after FXML loading.
     * Sets up the proceed button visibility and action.
     */
    @FXML
    public void initialize() {
        proceedButton.setVisible(false);
        proceedButton.setOnAction(e -> {
            GameEngine.getSoundController().playButtonClick();
            finishPhase();
        });
    }

    /**
     * Sets dependencies for the Sort Phase, including database, stage, and
     * callback for phase completion. Initializes filters, views, top bar,
     * and loads items.
     *
     * @param database        Database instance for game state access.
     * @param stage           Primary stage to show the phase.
     * @param onPhaseComplete Runnable to execute when phase finishes.
     */
    public void setDependencies(Database database, Stage stage, Runnable onPhaseComplete) {
        System.out.println(getClass().getClassLoader().getResource("com/example/fantasysortinggame/images/itemimages/img.png"));
        this.database = database;
        this.stage = stage;
        this.onPhaseComplete = onPhaseComplete;

        rootFilterNode = getItemSortCategoriesByDay(database.getDayInBound());
        createFilterButtons(rootFilterNode);
        createViewButtons(Arrays.asList("Complete", "Image", "Lore"));

        updateTopBar();
        loadItems();
    }

    /**
     * Returns a FilterNode tree representing item sort categories based on
     * the current day in the game.
     *
     * @param day Current in-bound day of the game.
     * @return Root FilterNode with all category children.
     */
    FilterNode getItemSortCategoriesByDay(int day) {
        FilterNode root = new FilterNode("root"); // dummy root
        if (day <= 2) { // before day 3 (index 2), categoris are undivided
            root.children.add(new FilterNode("All"));
            root.children.add(new FilterNode("Unsorted"));
            root.children.add(new FilterNode("Junk"));
            root.children.add(new FilterNode("Treasure"));
        } else if (day <= 4) { // between day 3 (index 2) and 5 (index 4), categories receive first subdivision
            root.children.add(new FilterNode("All"));
            root.children.add(new FilterNode("Unsorted"));
            root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("All Junk"), new FilterNode("Usable Junk"), new FilterNode("Broken Junk"), new FilterNode("Curious Junk"))));
            root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("All Treasure"), new FilterNode("Magical Treasure"), new FilterNode("Historical Treasure"), new FilterNode("Luxurious Treasure"))));
        } else { // on day 5 and beyond, categories receive final subdivision.
            root.children.add(new FilterNode("All"));
            root.children.add(new FilterNode("Unsorted"));
            root.children.add(new FilterNode("Junk", Arrays.asList(new FilterNode("All Junk"), new FilterNode("Usable Junk", Arrays.asList(new FilterNode("All Usable Junk"), new FilterNode("Consumable"), new FilterNode("Tools"), new FilterNode("Everyday"))), new FilterNode("Broken Junk", Arrays.asList(new FilterNode("All Broken Junk"), new FilterNode("Depleted"), new FilterNode("Weathered"))), new FilterNode("Curious Junk", Arrays.asList(new FilterNode("All Curious Junk"), new FilterNode("Oddities"), new FilterNode("Crafting Materials"), new FilterNode("Collectibles"))))));
            root.children.add(new FilterNode("Treasure", Arrays.asList(new FilterNode("All Treasure"), new FilterNode("Magical Treasure", Arrays.asList(new FilterNode("All Magical Treasure"), new FilterNode("Artifacts"), new FilterNode("Cursed"), new FilterNode("Minor"))), new FilterNode("Historical Treasure", Arrays.asList(new FilterNode("All Historical Treasure"), new FilterNode("Relics"), new FilterNode("Keepsakes"), new FilterNode("Maps"))), new FilterNode("Luxurious Treasure", Arrays.asList(new FilterNode("All Luxurious Treasure"), new FilterNode("Jewelry"), new FilterNode("Fungible"), new FilterNode("Ornament"))))));
        }
        return root;
    }

    /**
     * Creates filter buttons from the given FilterNode tree and adds them to the filterButtonContainer.
     * <p>
     * <p>
     * <p>
     * <p>
     * Leaf nodes are represented as ToggleButtons.
     * <p>
     * <p>
     * <p>
     * <p>
     * Nodes with children are represented as MenuButtons with recursive submenus.
     *
     * @param root Root FilterNode of the filter tree.
     */
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

    /**
     * Recursively builds submenu items for a MenuButton.
     *
     * @param parent Parent MenuButton to attach submenu items to.
     * @param node   FilterNode to build from.
     */
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

    /**
     * Recursively builds submenu items for a Menu.
     *
     * @param menu Parent Menu to attach items to.
     * @param node FilterNode to build from.
     */
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

    /**
     * Creates toggle buttons for each view type (Complete, Image, Lore)
     * and sets their actions.
     *
     * @param views List of view names.
     */
    private void createViewButtons(List<String> views) {
        viewButtonContainer.getChildren().clear();

        for (String viewName : views) {
            ToggleButton btn = new ToggleButton(viewName);
            btn.setOnAction(e -> {
                GameEngine.getSoundController().playButtonClick();

                currentView = btn.getText();
                displayItems();
            });
            viewButtonContainer.getChildren().add(btn);
        }
    }

    /**
     * Displays items in the itemContainer based on the current filter and view mode.
     * Skips sold items.
     * Determines if all items are sorted to toggle the proceed button.
     * Supports three views: "Complete", "Lore", and "Image".
     */
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
                String link = item.getImageLink();
                ImageView imageView = new ImageView();

                if (link != null) {
                    try {
                        InputStream is = getClass().getResourceAsStream(item.getImageLink());
                        if (is != null) {
                            imageView.setImage(new Image(is));
                        } else {
                            System.out.println("❌ Resource not found: " + link);
                        }
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
                sortButton.setOnAction(ev -> {
                    GameEngine.getSoundController().playButtonClick();
                    showSortMenu(sortButton, item);
                });

                infoBox.getChildren().addAll(titleLabel, descText, sortButton);
                row.getChildren().addAll(imageView, infoBox);
                itemContainer.getChildren().add(row);
            } else if (currentView.equalsIgnoreCase("Lore")) {
                // Info box
                VBox infoBox = new VBox(5);
                Label titleLabel = new Label(item.getTitle());
                Text descText = new Text(item.getDescription());

                Button sortButton = new Button(item.getItemSort());
                sortButton.setOnAction(ev -> {
                    GameEngine.getSoundController().playButtonClick();
                    showSortMenu(sortButton, item);
                });

                infoBox.getChildren().addAll(titleLabel, descText, sortButton);
                row.getChildren().addAll(infoBox);
                itemContainer.getChildren().add(row);
            } else if (currentView.equalsIgnoreCase("Image")) {
                ImageView imageView = new ImageView();
                if (item.getImageLink() != null) {
                    try {
                        InputStream is = getClass().getResourceAsStream(item.getImageLink());
                        imageView.setImage(new Image(is));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                Label titleLabel = new Label(item.getTitle());
                Button sortButton = new Button(item.getItemSort());
                VBox infoBox = new VBox(5);
                sortButton.setOnAction(ev -> {
                    GameEngine.getSoundController().playButtonClick();
                    showSortMenu(sortButton, item);
                });

                infoBox.getChildren().addAll(titleLabel, imageView, sortButton);
                row.getChildren().addAll(infoBox);
                itemContainer.getChildren().add(row);
            }
        }
        proceedButton.setVisible(allSorted);
    }

    /**
     * Updates top bar labels for day and total gold.
     */
    private void updateTopBar() {
        if (dayLabel != null) dayLabel.setText("Day: " + database.getDay());
        if (totalGoldLabel != null) totalGoldLabel.setText("Gold: " + database.getGold());
    }

    /**
     * Loads items for the day, merging with previously used items,
     * <p>
     * and triggers day-start events.
     */
    private void loadItems() {
        if (database == null) return;

        ArrayList<Item> dayItems = database.getItems();
        if (database.upgradeIsBought("Little Helper")) {
            dayItems.getFirst().setItemSort(dayItems.getFirst().getItemTypeValue());
        }
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
        triggerDayStartEvents();
    }

    /**
     * Triggers all relevant day-start story events for items and dialogues.
     */
    private void triggerDayStartEvents() {
        for (Item item : items) {
            if (item.getQuickTimeEvents() == null) continue;

            for (StoryEvent event : item.getQuickTimeEvents()) {
                if (event.hasHappened()) continue;

                for (StoryEventTrigger trigger : event.getStoryEventTriggers()) {
                    // Pass null for newSort because nothing is being sorted
                    if (trigger.isTriggered(database, null, false)) {
                        // Only trigger if this is a day-start relevant event
                        if (trigger.getRequiredSort() == null && !trigger.happensOnSale()) {
                            if (event instanceof Dialogue dialogue) {
                                showDialogue(dialogue);
                            } else if (event instanceof QuickTimeEvent qte) {
                                QuickTimeEventController.showQuickTimeEventWindow(qte, items, stage);
                            }
                            event.setHappened(true);
                            break;
                        }
                    }
                }
            }
        }
        // Trigger global dialogues (like Butler)
        for (Dialogue dialogue : database.getAllDialogues()) {
            if (dialogue.hasHappened()) continue;

            if (dialogue.getStoryEventTriggers() != null) {
                for (StoryEventTrigger trigger : dialogue.getStoryEventTriggers()) {
                    if (trigger.isTriggered(database, null, false)) {
                        // Day-start dialogue check
                        if ((trigger.getRequiredSort() == null || trigger.getRequiredSort().isEmpty()) && !trigger.happensOnSale()) {
                            showDialogue(dialogue);
                            dialogue.setHappened(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Displays a context menu for sorting an item according to the day and
     * <p>
     * current category structure.
     *
     * @param button Button that triggers the menu.
     * @param item   Item to sort.
     */
    private void showSortMenu(Button button, Item item) {
        ContextMenu menu = new ContextMenu();
        int day = database.getDayInBound();

        // Don't include "All" as an option for sorting the item itself
        if (day <= 2) {
            menu.getItems().addAll(createMenuItem("Unsorted", button, item), createMenuItem("Junk", button, item), createMenuItem("Treasure", button, item));
        } else if (day <= 4) {
            menu.getItems().addAll(createMenuItem("Unsorted", button, item), createSubMenu("Junk", new String[]{"Usable Junk", "Broken Junk", "Curious Junk"}, button, item), createSubMenu("Treasure", new String[]{"Magical Treasure", "Historical Treasure", "Luxurious Treasure"}, button, item));
        } else {
            menu.getItems().addAll(createMenuItem("Unsorted", button, item), createSubSubMenu("Junk", new String[][]{{"Usable Junk", "Broken Junk", "Curious Junk"}, {"Consumable", "Tools", "Everyday"}, {"Depleted", "Weathered"}, {"Oddities", "Crafting Materials", "Collectibles"}}, button, item), createSubSubMenu("Treasure", new String[][]{{"Magical Treasure", "Historical Treasure", "Luxurious Treasure"}, {"Artifacts", "Cursed / Dangerous", "Minor / Utility Magic"}, {"Relics", "Keepsakes", "Documents / Maps"}, {"Jewelry", "Hoardable", "Decorative / Ornamental"}}, button, item));
        }

        menu.show(button, Side.BOTTOM, 0, 0);
        for (StoryEvent event : item.getQuickTimeEvents()) {
            if (!event.hasHappened() && event.shouldTrigger(database)) {
                if (event instanceof Dialogue dialogueEvent) {
                    showDialogue(dialogueEvent);
                } else if (event instanceof QuickTimeEvent qteEvent) {
                    QuickTimeEventController.showQuickTimeEventWindow(qteEvent, items, stage);
                }
                // Mark as happened (optional if not handled inside showDialogueWindow/runQTE)
                event.setHappened(true);
            }
        }
    }

    /**
     * Shows a dialogue by disabling other UI elements, running the dialogue, and re-enabling the UI once the dialogue ends.
     *
     * @param dialogue Dialogue object to display.
     */
    public void showDialogue(Dialogue dialogue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fantasysortinggame/fxmlfiles/dialogueBox.fxml"));
            loader.setControllerFactory(param -> new DialogueBoxController(database));
            AnchorPane dialogueUI = loader.load();
            DialogueBoxController controller = loader.getController();

            dialogueContainer.getChildren().setAll(dialogueUI);

            // Disable rest of UI
            itemContainer.setDisable(true);
            filterButtonContainer.setDisable(true);
            viewButtonContainer.setDisable(true);
            proceedButton.setDisable(true);

            controller.runDialogue(dialogue);

            controller.setOnDialogueEnd(() -> {
                itemContainer.setDisable(false);
                filterButtonContainer.setDisable(false);
                viewButtonContainer.setDisable(false);
                proceedButton.setDisable(false);
                dialogueContainer.getChildren().clear();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a MenuItem for a given sort option and attaches the click handler.
     *
     * @param name   Name of the sort option.
     * @param button Button associated with the item being sorted.
     * @param item   Item to sort.
     * @return MenuItem configured to sort the given item.
     */
    private MenuItem createMenuItem(String name, Button button, Item item) {
        MenuItem mi = new MenuItem(name);
        mi.setOnAction(e -> {
            GameEngine.getSoundController().playButtonClick();
            handleSortSelection(item, button, name);
        });
        return mi;
    }

    /**
     * Creates a Menu containing multiple sub-items for sorting.
     *
     * @param name     Name of the parent menu.
     * @param subItems Array of sub-item names.
     * @param button   Button associated with the item being sorted.
     * @param item     Item to sort.
     * @return Menu populated with sub-items.
     */
    private Menu createSubMenu(String name, String[] subItems, Button button, Item item) {
        Menu menu = new Menu(name);
        for (String sub : subItems) {
            menu.getItems().add(createMenuItem(sub, button, item));
        }
        return menu;
    }

    /**
     * Creates a hierarchical sub-sub menu for sorting items.
     *
     * @param name        Name of the parent menu.
     * @param subSubItems 2D array representing submenus and their child items.
     * @param button      Button associated with the item being sorted.
     * @param item        Item to sort.
     * @return MenuItem representing the hierarchical menu.
     */
    private MenuItem createSubSubMenu(String name, String[][] subSubItems, Button button, Item item) {
        Menu menu = new Menu(name);
        for (int i = 0; i < subSubItems[0].length; i++) {
            menu.getItems().add(createSubMenu(subSubItems[0][i], subSubItems[i + 1], button, item));
        }
        return menu;
    }

    /**
     * Handles sort selection for an item, triggers events if needed,
     * <p>
     * updates UI, and processes correct/incorrect sort logic.
     *
     * @param item    Item being sorted.
     * @param button  Button used to select the sort.
     * @param newSort The new sort category selected.
     */
    void handleSortSelection(Item item, Button button, String newSort) {

        if (item.getQuickTimeEvents() != null) {
            for (StoryEvent event : item.getQuickTimeEvents()) {
                if (!event.hasHappened()) {
                    for (StoryEventTrigger trigger : event.getStoryEventTriggers()) {
                        // Pass the newSort to check sort-dependent triggers
                        if (trigger.isTriggered(database, newSort, false)) {
                            if (event instanceof Dialogue dialogue) {
                                showDialogue(dialogue);
                            } else if (event instanceof QuickTimeEvent qte) {
                                QuickTimeEventController.showQuickTimeEventWindow(qte, items, stage);
                            }
                            event.setHappened(true); // mark event done
                            displayItems();
                            return; // event triggered, no need to check other triggers.
                            // This also interrupts the normal itemSort process, and so it should return.
                        }
                    }
                }
            }
        }
        item.setItemSort(newSort);
        if (newSort.equalsIgnoreCase(item.getItemTypeValue()) || newSort.equalsIgnoreCase("Unsorted")) {
            GameEngine.onCorrectSort(item);
        } else {
            GameEngine.onIncorrectSort();
        }
        button.setText(newSort);
        displayItems();

    }

    /**
     * Finishes the
     * phase:
     * closes stage, saves
     * database,
     * and runs
     * <p>
     * onPhaseComplete callback.
     */
    private void finishPhase() {
        if (stage != null) {
            stage.close();
        }
        database.saveToFile();
        if (onPhaseComplete != null) {
            onPhaseComplete.run();
        }
    }

    /**
     * Finds a FilterNode by name in a tree recursively.
     *
     * @param node       Root node to search from.
     * @param filterName Name of the filter to find.
     * @return FilterNode if found, else null.
     */
    private FilterNode findFilterNode(FilterNode node, String filterName) {
        if (node.name.equalsIgnoreCase(filterName)) return node;
        for (FilterNode child : node.children) {
            FilterNode result = findFilterNode(child, filterName);
            if (result != null) return result;
        }
        return null;
    }

    /**
     * Shows the Sort Phase by loading the FXML and initializing the controller.
     *
     * @param db              Database instance for the game.
     * @param parentStage     Stage to display the phase.
     * @param onPhaseComplete Runnable to execute when the phase ends.
     */
    public static void showSortPhase(Database db, Stage parentStage, Runnable onPhaseComplete) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(SortPhaseController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/sortPhase.fxml"));
            parentStage.setScene(new javafx.scene.Scene(loader.load()));
            parentStage.setTitle("Sort Phase: " + db.getGameMode());

            SortPhaseController controller = loader.getController();
            controller.setDependencies(db, parentStage, onPhaseComplete);
            parentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inner class representing a node in the filter tree for item sorts.
     */
    class FilterNode {
        String name;
        List<FilterNode> children;

        /**
         * Creates a leaf FilterNode with no children.
         *
         * @param name Name of the filter node.
         */
        public FilterNode(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        /**
         * Creates a FilterNode with specified children.
         *
         * @param name     Name of the filter node.
         * @param children List of child FilterNodes.
         */
        public FilterNode(String name, List<FilterNode> children) {
            this.name = name;
            this.children = children == null ? new ArrayList<>() : children;
        }

        /**
         * Checks if the node is a leaf (no children).
         *
         * @return true if the node has no children, false otherwise.
         */
        public boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * Determines if an item sort string matches this filter node.
         * <p>
         * <p>
         * <p>
         * <p>
         * Matching rules:
         * <p>
         * <p>
         * <p>
         * <p>
         * Exact match (case-insensitive)
         * <p>
         * <p>
         * <p>
         * <p>
         * Root-level "All" matches everything
         * <p>
         * <p>
         * <p>
         * <p>
         * "All X" matches anything in X's canonical subtree
         * <p>
         * <p>
         * <p>
         * <p>
         * Otherwise checks descendants recursively
         *
         * @param itemSort Item sort string to test.
         * @return true if the itemSort matches this node.
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

        /**
         * Recursively checks a subtree for a match with the given item sort.
         *
         * @param node     Node to search from.
         * @param itemSort Item sort string to match.
         * @return true if a match is found in the subtree.
         */
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
