package com.example.fantasysortinggame.datatypes;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents an in-game item, which may have associated events, dialogues, and upgrades.
 */
public class Item {

    private String itemSort; // Categories like "Magic Item, Magic Weapon"
    private File imageLink; // Path to item image
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
    Item() {
        this.itemSort = "";
        this.imageLink = null;
        this.description = "";
        this.title = "";
        this.itemType = null;
        this.value = 0;
        this.isSold = false;
        this.events = new ArrayList<>();
        this.dialogues = new ArrayList<>();
    }

    /** Full constructor */
    public Item(String itemSort, File imageLink, String description, String title, ItemType itemType, boolean isSold, ArrayList<QuickTimeEvent> events, ArrayList<Dialogue> dialogues, double value) {
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

    /**
     * Copies all properties from another item.
     * Useful for QTE success/failure results.
     *
     * @param other Item to copy from
     */
    public void copyFrom(Item other) {
        setItemSort(other.getItemSort());
        setImageLink(other.getImageLink());
        setDescription(other.getDescription());
        setTitle(other.getTitle());
        setItemType(other.getItemType());
        setSold(other.isSold());
        setDialogues(other.getDialogues());
        setEvents(other.getStoryEvents());
        setValue(other.getValue());
    }

    // --- Getters and setters ---
    public String getItemSort() { return itemSort; }
    public void setItemSort(String itemSort) { this.itemSort = itemSort; }
    public File getImageLink() { return imageLink; }
    public void setImageLink(File imageLink) { this.imageLink = imageLink; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ItemType getItemType() { return itemType; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }
    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }
    public ArrayList<QuickTimeEvent> getStoryEvents() { return events; }
    public void setEvents(ArrayList<QuickTimeEvent> events) { this.events = events; }
    public ArrayList<Dialogue> getDialogues() { return dialogues; }
    public void setDialogues(ArrayList<Dialogue> dialogues) { this.dialogues = dialogues; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}
