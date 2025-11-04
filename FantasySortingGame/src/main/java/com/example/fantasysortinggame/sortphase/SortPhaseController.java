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

public class SortPhaseController extends SoundEffectController{ // may need to be static, as I think there should only be one instance of this
    private final Database database;
    private final QuickTimeEventController quickTimeEventController;
    private final DialogueBoxController dialogueBoxController;
    private ArrayList<Item> dailyItems;
    private int unsortedItemCount;
    private String filter;
    private String view;
    private boolean dayOver;

    public SortPhaseController(Database database, QuickTimeEventController quickTimeEventController, DialogueBoxController dialogueBoxController) {
        this.database = database;
        this.quickTimeEventController = quickTimeEventController;
        this.dialogueBoxController = dialogueBoxController;
        this.unsortedItemCount = 0;
        this.filter = "unsorted";
        this.view = "default";
        this.dayOver = false;
    }

    void loadItems(String filter, String view, int day, int seed) {
        dailyItems = database.getItemsByDayAndSeed(day, seed);
        unsortedItemCount = dailyItems.size();
        displaySortMenu(filter, view, day);
        dayOver = false;
    }

    ArrayList<Item> filterDailyItems() {
        ArrayList<Item> filteredItems = new ArrayList<Item>();
        for (Item item : dailyItems) {
            if (item.getItemSort().contains(filter)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    void displaySortMenu(String filter, String view, int day) {
        // Once the FXML is working, this will just call the fxml to display a topbar appropriately.
        System.out.println("Menu.");
        for (Item item : filterDailyItems())
            displayItem(item, view);
    }

    void displayItem(Item item, String view) {
        // Once the FXML is working, this will just call the fxml to display an item appropriately.
        if (view.equals("default")) {
            System.out.println("Item in default view: " + item);
        } else { // Once views are defined, there will be an if for each view.
            System.out.println("Item in specific view: " + item + ". View: " + view);
        }
    }

    void onChangeItemSortClickHandler(Item item, String newSort) {
        playSound(buttonClickSound);
        for (QuickTimeEvent event : item.getStoryEvents()) {
            if (event.isTriggered(newSort) && !event.hasHappened()) {
                quickTimeEventController.runStoryEvent(event, item);
                return;
            }
        }
        for (Dialogue dialogue : item.getDialogues()) {
            if (dialogue.isTriggered(newSort) && !dialogue.hasHappened()) {
                dialogueBoxController.runDialogue(dialogue);
                return;
            }
        }
        if (item.getItemSort().equals("unsorted")) {
            unsortedItemCount--;
        }
        if (newSort.equals("unsorted") ) {
            unsortedItemCount++;
        }
        item.setItemSort(newSort);
        if (unsortedItemCount == 0) {
            dayOver = true;
            File endOfDaySound = new File("com/example/fantasysortinggame/sound files/PLACEHOLDERendOfDayFile.txt");
            playSound(endOfDaySound);
        }
        displaySortMenu(filter, view, database.getDay());
    }
}
