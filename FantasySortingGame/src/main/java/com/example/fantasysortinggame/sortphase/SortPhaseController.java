package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing the "Sort Phase" of the game.
 * <p>
 * This version opens the sort phase in its own Stage and populates
 * the UI according to the FXML provided.
 */
public class SortPhaseController extends SoundEffectController {

    private final Database database;
    private final QuickTimeEventController quickTimeEventController;
    private final DialogueBoxController dialogueBoxController;

    private Stage stage;
    private List<Item> dailyItems;
    private String filter;
    private String view;
    private boolean dayOver;

    // UI elements
    @FXML private BorderPane rootPane;
    @FXML private ImageView itemImageView;
    @FXML private TextField itemNameField;
    @FXML private TextField itemDescriptionField;
    @FXML private TextField itemTypeField;
    @FXML private ToggleButton unsortedToggle;
    @FXML private ToggleButton junkToggle;
    @FXML private ToggleButton treasureToggle;
    @FXML private ToggleButton endPhaseToggle;

    /**
     * Constructor.
     */
    public SortPhaseController(Database database, QuickTimeEventController quickTimeEventController, DialogueBoxController dialogueBoxController) {
        this.database = database;
        this.quickTimeEventController = quickTimeEventController;
        this.dialogueBoxController = dialogueBoxController;
        this.filter = "unsorted";
        this.view = "default";
        this.dayOver = false;
    }

    /**
     * Opens the Sort Phase UI in a new Stage.
     */
    public void showSortPhaseWindow(int day, int seed) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/SortPhase.fxml"));
            loader.setController(this); // Use this instance
            Parent root = loader.load();

            stage = new Stage();
            stage.setTitle("Sort Phase");
            stage.setScene(new Scene(root));
            stage.show();

            // Load items after stage is ready
            loadItems(filter, view, day, seed);

            // Attach button actions
            attachButtonHandlers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Attaches actions to UI buttons.
     */
    private void attachButtonHandlers() {
        unsortedToggle.setOnAction(e -> filter = "unsorted");
        junkToggle.setOnAction(e -> filter = "junk");
        treasureToggle.setOnAction(e -> filter = "treasure");
        endPhaseToggle.setOnAction(e -> stage.close()); // End phase closes the window
    }

    /**
     * Loads items for the day, applies filter and view, and displays them.
     */
    void loadItems(String filter, String view, int day, int seed) {
        this.dailyItems = database.getItemsByDayAndSeed(day, seed);
        this.filter = filter;
        this.view = view;
        displaySortMenu(filter, view, day);
        dayOver = false;
    }

    /**
     * Filters the daily items according to the current filter.
     */
    ArrayList<Item> filterDailyItems() {
        ArrayList<Item> filteredItems = new ArrayList<>();
        for (Item item : dailyItems) {
            if (item.getItemSort().contains(filter)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    /**
     * Displays the sort menu (placeholder for UI logic).
     */
    void displaySortMenu(String filter, String view, int day) {
        System.out.println("Displaying menu for filter: " + filter);
        for (Item item : filterDailyItems()) {
            displayItem(item, view);
        }
    }

    /**
     * Displays a single item in the UI (placeholder: can update ImageView and TextFields).
     */
    void displayItem(Item item, String view) {
        if ("default".equals(view)) {
            System.out.println("Item in default view: " + item);
        } else {
            System.out.println("Item in specific view: " + item + ". View: " + view);
        }

        // Example: populate UI fields with first filtered item
        itemNameField.setText(item.getTitle());
        itemDescriptionField.setText(item.getDescription());
        itemTypeField.setText(item.getItemSort());
        // TODO: load image into itemImageView
    }

    /**
     * Handles when the player changes the sort of an item.
     */
    void onChangeItemSortClickHandler(Item item, String newSort) {
        playSound(buttonClickSound);

        if (tryTriggerEvents(item, newSort)) return;

        item.setItemSort(newSort);

        if (getUnsortedItemCount() == 0) {
            dayOver = true;
            File endOfDaySound = new File("com/example/fantasysortinggame/sound files/PLACEHOLDERendOfDayFile.txt");
            playSound(endOfDaySound);
        }

        displaySortMenu(filter, view, database.getDay());
    }

    /**
     * Computes the number of items that are still unsorted.
     */
    private long getUnsortedItemCount() {
        return dailyItems.stream().filter(i -> "unsorted".equals(i.getItemSort())).count();
    }

    /**
     * Tries to trigger a QTE or dialogue associated with an item.
     */
    private boolean tryTriggerEvents(Item item, String newSort) {
        for (QuickTimeEvent event : item.getStoryEvents()) {
            if (event.isTriggered(newSort) && !event.hasHappened()) {
                quickTimeEventController.showQuickTimeEventWindow(event, item);
                return true;
            }
        }

        for (Dialogue dialogue : item.getDialogues()) {
            if (dialogue.isTriggered(newSort) && !dialogue.hasHappened()) {
                dialogueBoxController.showDialogueWindow(database, dialogue);
                return true;
            }
        }
        return false;
    }
}
