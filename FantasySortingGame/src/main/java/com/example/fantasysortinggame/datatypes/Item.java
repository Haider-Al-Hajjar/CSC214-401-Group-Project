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

    /**
     * QuickTimeEvents linked to this item
     */
    private ArrayList<QuickTimeEvent> events;

    /**
     * Dialogues linked to this item
     */
    private ArrayList<Dialogue> dialogues;

    /**
     * Default constructor
     */
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

    /**
     * Full constructor (imageLink is now STRING)
     */
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

    /**
     * Copy all fields from another item
     */
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

    /**
     * Returns the category or sorting classification of this item.
     *
     * @return the item sort/category
     */
    public String getItemSort() {
        return itemSort;
    }

    /**
     * Sets the category or sorting classification of this item.
     *
     * @param itemSort the item category
     */
    public void setItemSort(String itemSort) {
        this.itemSort = itemSort;
    }

    /**
     * Returns the image path associated with this item.
     *
     * @return the image link path
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * Sets the image path associated with this item.
     *
     * @param imageLink the path or filename to use
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * Returns the item description text.
     *
     * @return description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the item's description text.
     *
     * @param description the description to apply
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the display title of the item.
     *
     * @return item title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the display title of the item.
     *
     * @param title the title to apply
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns this item's type object.
     *
     * @return the ItemType
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Returns the string representation of this item's type.
     *
     * @return the item type value as a string
     */
    public String getItemTypeValue() {
        return itemType.getItemType();
    }

    /**
     * Sets the type classification of this item.
     *
     * @param itemType the ItemType to assign
     */
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * Indicates whether this item has been sold.
     *
     * @return true if sold, false otherwise
     */
    public boolean isSold() {
        return isSold;
    }

    /**
     * Marks this item as sold or unsold.
     *
     * @param sold true to mark as sold, false otherwise
     */
    public void setSold(boolean sold) {
        this.isSold = sold;
    }

    /**
     * Returns the list of QuickTimeEvents associated with this item.
     *
     * @return list of item events
     */
    public ArrayList<QuickTimeEvent> getStoryEvents() {
        return events;
    }

    /**
     * Sets the list of QuickTimeEvents associated with this item.
     *
     * @param events list of events to assign
     */
    public void setEvents(ArrayList<QuickTimeEvent> events) {
        this.events = events;
    }

    /**
     * Returns the list of dialogue sequences linked to this item.
     *
     * @return list of dialogues
     */
    public ArrayList<Dialogue> getDialogues() {
        return dialogues;
    }

    /**
     * Sets the list of dialogue sequences associated with this item.
     *
     * @param dialogues the list of dialogues to assign
     */
    public void setDialogues(ArrayList<Dialogue> dialogues) {
        this.dialogues = dialogues;
    }

    /**
     * Returns the base gold value of this item.
     *
     * @return item value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the base gold value of this item.
     *
     * @param value the new value to assign
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Alternative accessor for retrieving this item's events.
     *
     * @return list of QuickTimeEvents
     */
    public ArrayList<QuickTimeEvent> getQuickTimeEvents() {
        return events;
    }

    /**
     * Alternative accessor for retrieving this item's dialogues.
     *
     * @return list of dialogues
     */
    public ArrayList<Dialogue> getDialogueList() {
        return dialogues;
    }

    /**
     * Returns this item's display name (equivalent to title).
     *
     * @return item name
     */
    public String getName() {
        return title;
    }
}
