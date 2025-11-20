package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing the "Sort Phase" of the game.
 */
public class SortPhaseController extends SoundEffectController {

    private final Database database;
    private final QuickTimeEventController quickTimeEventController;
    private final DialogueBoxController dialogueBoxController;

    private List<Item> dailyItems;
    private String filter;
    private String view;
    private boolean dayOver;

    public SortPhaseController(Database database, QuickTimeEventController quickTimeEventController, DialogueBoxController dialogueBoxController) {
        this.database = database;
        this.quickTimeEventController = quickTimeEventController;
        this.dialogueBoxController = dialogueBoxController;
        this.filter = "unsorted";
        this.view = "default";
        this.dayOver = false;
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
        System.out.println("Menu.");
        for (Item item : filterDailyItems()) {
            displayItem(item, view);
        }
    }

    /**
     * Displays a single item (placeholder for UI logic).
     */
    void displayItem(Item item, String view) {
        if ("default".equals(view)) {
            System.out.println("Item in default view: " + item);
        } else {
            System.out.println("Item in specific view: " + item + ". View: " + view);
        }
    }

    /**
     * Handles when the player changes the sort of an item.
     */
    void onChangeItemSortClickHandler(Item item, String newSort) {
        playSound(buttonClickSound);

        // Try to trigger any events or dialogues first
        if (tryTriggerEvents(item, newSort)) {
            return;
        }

        // Update the item's sort
        item.setItemSort(newSort);

        // Check if the day is over
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
     *
     * @return true if an event or dialogue was triggered, false otherwise
     */
    private boolean tryTriggerEvents(Item item, String newSort) {
        // Check QuickTimeEvents
        for (QuickTimeEvent event : item.getStoryEvents()) {
            if (event.isTriggered(newSort) && !event.hasHappened()) {
                quickTimeEventController.runStoryEvent(event, item);
                return true;
            }
        }

        // Check Dialogues
        for (Dialogue dialogue : item.getDialogues()) {
            if (dialogue.isTriggered(newSort) && !dialogue.hasHappened()) {
                dialogueBoxController.runDialogue(dialogue);
                return true;
            }
        }

        return false;
    }
}
