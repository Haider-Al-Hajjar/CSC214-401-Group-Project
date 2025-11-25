package com.example.fantasysortinggame.salephase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SalePhaseController {

    private final Database database;
    private final SoundEffectController sfx;
    private final QuickTimeEventController qteCtrl;
    private final DialogueBoxController dialogueCtrl;

    private ArrayList<Item> unsoldItems;
    private String currentFilter = "";

    // FXML elements
    @FXML private ImageView itemImageView;
    @FXML private TextField itemNameField;
    @FXML private TextField itemDescriptionField;
    @FXML private TextField itemTypeField;
    @FXML private Button unsortedButton;
    @FXML private Button junkButton;
    @FXML private Button treasureButton;
    @FXML private Button sellButton;

    private Item selectedItem;

    public SalePhaseController(Database database,
                               QuickTimeEventController qteCtrl,
                               DialogueBoxController dialogueCtrl) {
        this.database = database;
        this.sfx = SoundEffectController.getInstance();
        this.qteCtrl = qteCtrl;
        this.dialogueCtrl = dialogueCtrl;
    }

    @FXML
    public void initialize() {
        // Attach filter buttons
        unsortedButton.setOnAction(e -> onChangeFilterButtonClickedHandler("unsorted"));
        junkButton.setOnAction(e -> onChangeFilterButtonClickedHandler("junk"));
        treasureButton.setOnAction(e -> onChangeFilterButtonClickedHandler("treasure"));

        // Sell button
        sellButton.setOnAction(e -> onSellButtonClickedHandler(selectedItem));
    }

    public void loadItems() {
        ArrayList<Item> todayItems = database.getUsedItems();
        unsoldItems = todayItems.stream().filter(item -> !item.isSold())
                .collect(Collectors.toCollection(ArrayList::new));
        currentFilter = "";
        displaySaleMenu();
        sfx.playPhaseStart();
    }

    public void onChangeFilterButtonClickedHandler(String filter) {
        this.currentFilter = filter != null ? filter : "";
        displaySaleMenu();
        sfx.playButtonClick();
    }

    public void onSellButtonClickedHandler(Item itemClicked) {
        if (itemClicked == null || itemClicked.isSold()) {
            sfx.playError();
            return;
        }

        boolean eventTriggered = false;

        for (QuickTimeEvent event : itemClicked.getStoryEvents()) {
            if (!event.hasHappened() && event.isTriggered("sold")) {
                qteCtrl.runStoryEvent(event, itemClicked);
                eventTriggered = true;
                break;
            }
        }

        if (!eventTriggered) {
            for (Dialogue dialogue : itemClicked.getDialogues()) {
                if (!dialogue.hasHappened() && dialogue.isTriggered("sold")) {
                    dialogueCtrl.runDialogue(dialogue);
                    eventTriggered = true;
                    break;
                }
            }
        }

        if (!eventTriggered) {
            sellItem(itemClicked);
        }
    }

    private void sellItem(Item item) {
        double value = estimateItemValue(item);
        database.addGold(value);
        item.setSold(true);
        unsoldItems.remove(item);
        sfx.playSellSuccess();
        displaySaleMenu();
    }

    private double estimateItemValue(Item item) {
        String sort = item.getItemSort().toLowerCase();
        if (sort.contains("legendary") || sort.contains("artifact")) return 100.0;
        if (sort.contains("rare") || sort.contains("magic")) return 50.0;
        if (sort.contains("common")) return 20.0;
        return 30.0;
    }

    private void displaySaleMenu() {
        ArrayList<Item> toShow = unsoldItems.stream()
                .filter(item -> currentFilter.isEmpty() || item.getItemSort().contains(currentFilter))
                .collect(Collectors.toCollection(ArrayList::new));

        if (toShow.isEmpty()) {
            itemNameField.setText("No items to sell");
            itemDescriptionField.clear();
            itemTypeField.clear();
            itemImageView.setImage(null);
            selectedItem = null;
        } else {
            // Select first item by default
            selectedItem = toShow.get(0);
            itemNameField.setText(selectedItem.getTitle());
            itemDescriptionField.setText(selectedItem.getDescription());
            itemTypeField.setText(selectedItem.getItemSort());

            if (selectedItem.getImagePath() != null) {
                Image img = new Image(selectedItem.getImagePath());
                itemImageView.setImage(img);
            } else {
                itemImageView.setImage(null);
            }
        }
    }

    public void onEndPhaseClick() {
        sfx.playPhaseEnd();
        System.out.println("Sale Phase Complete! Moving to Buy Phase...");
    }

    // Getters for UI state
    public ArrayList<Item> getUnsoldItems() { return unsoldItems; }
    public String getCurrentFilter() { return currentFilter; }
    public double getPlayerGold() { return database.getGold(); }
}
