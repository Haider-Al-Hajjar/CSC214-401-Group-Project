package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

/**
 * Represents an in-game item, which may have associated events, dialogues, and upgrades.
 */
public class Item {

    private String itemSort;            // Category of item
    private String imageLink;           // Path to item image (STRING ONLY)
    private String description;
    private String title;
    private ItemType itemType;
    private boolean isSold;
    private double value;

    /** QuickTimeEvents linked to this item */
    private ArrayList<QuickTimeEvent> events;

    /** Dialogues linked to this item */
    private ArrayList<Dialogue> dialogues;

    /** Default constructor */
    public Item() {
        this.itemSort = "";
        this.imageLink = "";
        this.description = "";
        this.title = "";
        this.itemType = null;
        this.value = 0;
        this.isSold = false;
        this.events = new ArrayList<QuickTimeEvent>();
        this.dialogues = new ArrayList<Dialogue>();
    }

    /** Full constructor (imageLink is now STRING) */
    public Item(String itemSort,
                String imageLink,
                String description,
                String title,
                ItemType itemType,
                boolean isSold,
                ArrayList<QuickTimeEvent> events,
                ArrayList<Dialogue> dialogues,
                double value) {

        this.itemSort = itemSort;
        this.imageLink = imageLink;
        this.description = description;
        this.title = title;
        this.itemType = itemType;
        this.isSold = isSold;
        this.events = events;
        this.dialogues = dialogues;
        this.value = value;
    }

    /** Copy all fields from another item */
    public void copyFrom(Item other) {
        this.itemSort = other.getItemSort();
        this.imageLink = other.getImageLink();
        this.description = other.getDescription();
        this.title = other.getTitle();
        this.itemType = other.getItemType();
        this.isSold = other.isSold();
        this.dialogues = other.getDialogues();
        this.events = other.getStoryEvents();
        this.value = other.getValue();
    }

    // --- Getters and setters ---
    public String getItemSort() {
        return itemSort;
    }

    public void setItemSort(String itemSort) {
        this.itemSort = itemSort;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        this.isSold = sold;
    }

    public ArrayList<QuickTimeEvent> getStoryEvents() {
        return events;
    }

    public void setEvents(ArrayList<QuickTimeEvent> events) {
        this.events = events;
    }

    public ArrayList<Dialogue> getDialogues() {
        return dialogues;
    }

    public void setDialogues(ArrayList<Dialogue> dialogues) {
        this.dialogues = dialogues;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // Convenience helpers
    public String getCurrentSort() {
        return itemSort;
    }

    public ArrayList<QuickTimeEvent> getEvents() {
        return events;
    }

    public ArrayList<Dialogue> getDialogueList() {
        return dialogues;
    }

    public String getImagePath() {
        return imageLink;
    }

    public String getName() {
        return title;
    }
}
