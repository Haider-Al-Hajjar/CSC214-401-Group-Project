package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

public class SortPhaseController { // may need to be static, as I think there should only be one instance of this
    private final Database database;

    int unsortedItemCount = 0;
    String filter = "";
    String view = "default";
    int day = 1;
    int seed = 0;

    public SortPhaseController(Database database) {
        this.database = database;

    }

    void loadItems(String filter, String view, int day, int seed) {

    }

    // get daily items from the database
    // set unsorted item count to the length of items gotten
    // display items based on view and filter
    //


    void displaySortMenu(String filter, String view, int day, int seed) {
        /*
            ArrayList<Item> itemsToDisplay = dailyItems.getItems(filter)
            for (Item item : itemsToDisplay)
                displayItem(item, view)
         */
    }

    void displayItem(Item item, String view) {
        /*
            update the sort menu / stage with the given item's information according to the view.
         */
    }

    void onChangeSortClick(Item item, String newSort) {
        /*
            if (item.getStoryEvent().isEventValid)
                    eventHandler.runStoryEvent(item.getStoryEvent(), item, newSort)
                    return
                    if (item.getDialogue().isDialogueTriggerd == true)
                    dialogueHandler.runDialogueHandler(item.getDialogue())
                    return
                    if (item.currentSort.equals("unsorted")
            unsortedItemCount- -;
            if (newSort.equals("unsorted")
            unsortedItemCount++;
            item.changeSort(newSort)
                    if (unsortedItemCount == 0)
                    soundEffectHandler.playSound(endOfDaySound);
            displayItems(filter, view, day, seed)
        */
    }
}
