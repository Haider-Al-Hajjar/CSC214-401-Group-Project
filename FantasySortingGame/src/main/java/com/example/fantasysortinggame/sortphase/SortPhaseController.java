package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.datatypes.Item;

public interface SortPhaseController { // may need to be static, as I think there should only be one instance of this
    int unsortedItemCount = 0;
    String filter = "";
    String view = "default";
    int day = 1;
    int seed = 0;
    void loadItems(String filter, String view, int day, int seed);
    // get daily items from the database
    // set unsorted item count to the length of items gotten
    // display items based on view and filter
    //
    void displayItems(String filter, String view, int day, int seed);
    /*
    ArrayList<Item> itemsToDisplay = dailyItems.getItems(filter)
	SortPhaseUI.sortMenuDisplay()
            for (Item item: itemsToDisplay)
            SortPhaseUI.ItemUIDisplay(item, view)
     */
    void onChangeSortClick(Item item, String newSort);
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
