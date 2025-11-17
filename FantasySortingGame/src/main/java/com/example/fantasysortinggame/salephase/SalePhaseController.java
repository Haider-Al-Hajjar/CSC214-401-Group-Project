package com.example.fantasysortinggame.salephase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;

import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 SALE PHASE CONTROLLER
This class handles everything in the "Sell Items" phase.

 GOAL:
  Show all items the player sorted today (that are NOT sold)
  Let player filter by category
  Let player click "Sell" on an item
  Add gold
  Mark item as sold
  Remove from display
  Trigger story event or dialogue if needed
  When player clicks "Proceed" → go to Buy Phase

 */
public class SalePhaseController {

    private final Database database;                    // Access to all game data
    private final SoundEffectController sfx;            // For sound effects
    private final QuickTimeEventController qteCtrl;     // For QTEs
    private final DialogueBoxController dialogueCtrl;   // For dialogues

    private ArrayList<Item> unsoldItems;                // Items available to sell
    private String currentFilter = "";                  // e.g., "Magic", "Weapon", ""

    /*
     Constructor: Initialize with required dependencies
     */
    public SalePhaseController(Database database,
                               QuickTimeEventController qteCtrl,
                               DialogueBoxController dialogueCtrl) {
        this.database = database;
        this.sfx = SoundEffectController.getInstance(); // assuming singleton
        this.qteCtrl = qteCtrl;
        this.dialogueCtrl = dialogueCtrl;
    }

    // 1. LOAD ITEMS WHEN SALE PHASE STARTS


    /*
     Called when entering Sale Phase.
     Loads all items from today that are NOT sold.
     */
    public void loadItems() {
        // Get all items used today
        ArrayList<Item> todayItems = database.getUsedItems();

        // Filter: keep only unsold ones
        unsoldItems = todayItems.stream()
                .filter(item -> !item.isSold())
                .collect(Collectors.toCollection(ArrayList::new));

        currentFilter = ""; // Reset filter
        displaySaleMenu();  // Show UI
        sfx.playPhaseStart(); // Optional: play sound
    }


    // 2. FILTERING


    /*
     Called when player clicks a filter button (e.g., "Magic", "All")
     @param filter The category to show, or "" for all
     */
    public void onChangeFilterButtonClickedHandler(String filter) {
        this.currentFilter = filter != null ? filter : "";
        displaySaleMenu(); // Refresh UI with new filter
        sfx.playButtonClick();
    }


    // 3. SELLING AN ITEM


    /*
     Called when player clicks "Sell" on an item
     @param itemClicked The item the player wants to sell
     */
    public void onSellButtonClickedHandler(Item itemClicked) {
        if (itemClicked == null || itemClicked.isSold()) {
            sfx.playError();
            return;
        }

        // === 1. Check for Story Events (QuickTimeEvent or Dialogue) ===
        boolean eventTriggered = false;

        // Check QuickTimeEvents
        for (QuickTimeEvent event : itemClicked.getStoryEvents()) {
            if (!event.hasHappened() && event.isTriggered("sold")) {
                qteCtrl.runStoryEvent(event, itemClicked);
                eventTriggered = true;
                break;
            }
        }

        // Check Dialogues
        if (!eventTriggered) {
            for (Dialogue dialogue : itemClicked.getDialogues()) {
                if (!dialogue.hasHappened() && dialogue.isTriggered("sold")) {
                    dialogueCtrl.runDialogue(dialogue);
                    eventTriggered = true;
                    break;
                }
            }
        }

        // === 2. If no event, or after event, sell the item ===
        if (!eventTriggered) {
            sellItem(itemClicked);
        }
        // If event was triggered, wait — UI will call sellItem() again after
    }

    /*
     Actually sell the item: add gold, mark sold, update UI
     */
    private void sellItem(Item item) {
        // Estimate value (you can improve this later)
        double value = estimateItemValue(item);
        database.addGold(value);

        item.setSold(true); // Mark as sold
        unsoldItems.remove(item); // Remove from display list

        sfx.playSellSuccess();
        displaySaleMenu(); // Refresh UI
    }

    /*
     Simple value estimation based on item type
     You can make this smarter later!
     */
    private double estimateItemValue(Item item) {
        String sort = item.getItemSort().toLowerCase();
        if (sort.contains("legendary") || sort.contains("artifact")) return 100.0;
        if (sort.contains("rare") || sort.contains("magic")) return 50.0;
        if (sort.contains("common")) return 20.0;
        return 30.0; // default
    }


    // 4. UI DISPLAY


    /*
     Refresh the sale screen with current items and filter
     */
    private void displaySaleMenu() {
        // This will be called by UI (Kayla's FXML)
        // For now, just print to console for testing
        System.out.println("\n=== SALE PHASE ===");
        System.out.println("Gold: " + database.getGold());
        System.out.println("Filter: " + (currentFilter.isEmpty() ? "All" : currentFilter));

        ArrayList<Item> toShow = unsoldItems.stream()
                .filter(item -> currentFilter.isEmpty() || item.getItemSort().contains(currentFilter))
                .collect(Collectors.toCollection(ArrayList::new));

        if (toShow.isEmpty()) {
            System.out.println("No items to sell. Click Proceed.");
        } else {
            for (Item item : toShow) {
                double value = estimateItemValue(item);
                System.out.println("- " + item.getTitle() +
                        " [" + item.getItemSort() + "] → " + value + " gold");
            }
        }

        // In real UI: SalePhaseUI.render(toShow, this::onSellButtonClickedHandler, currentFilter);
    }

    // 5. END OF PHASE


    /*
      Called when player clicks "Proceed" button
      Tells the main game loop to go to Buy Phase
     */

    public void onEndPhaseClick() {
        sfx.playPhaseEnd();
        // This should trigger BuyPhase
        System.out.println("Sale Phase Complete! Moving to Buy Phase...");
    }

    // GETTERS (for UI to read state)

    public ArrayList<Item> getUnsoldItems() {
        return unsoldItems;
    }

    public String getCurrentFilter() {
        return currentFilter;
    }

    public double getPlayerGold() {
        return database.getGold();
    }
}