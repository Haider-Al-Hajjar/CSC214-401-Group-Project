package com.example.fantasysortinggame.datatypes;

import java.io.File;
import java.util.ArrayList;

public class Item {
    private String itemSort;
    private File imageLink;
    private String description;
    private String title;
    private ItemType itemType;
    private boolean isSold;
    private ArrayList<QuickTimeEvent> events;
    private ArrayList<Dialogue> dialogues;
    Item() {
        // itemSort is going to be a String that has many substrings inside of it each for each subcategory.
        // ex: "Magic Item, Magic Weapon, Magic Sword"
        this.itemSort = "";
        this.imageLink = null;
        this.description = "";
        this.title = "";
        this.itemType = null;
        this.isSold = false;
        this.events = new ArrayList<QuickTimeEvent>();
        this.dialogues = new ArrayList<Dialogue>();

    }
    public Item(String itemSort, File imageLink, String description, String title, ItemType itemType, boolean isSold, ArrayList<QuickTimeEvent> events, ArrayList<Dialogue> dialogues) {
        this.itemSort = itemSort;
        this.imageLink = imageLink;
        this.description = description;
        this.title = title;
        this.itemType = itemType;
        this.isSold = isSold;
        this.events = events;
        this.dialogues = dialogues;
    }

    public String getItemSort() {
        return itemSort;
    }

    public void setItemSort(String itemSort) {
        this.itemSort = itemSort;
    }

    public File getImageLink() {
        return imageLink;
    }

    public void setImageLink(File imageLink) {
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
        isSold = sold;
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
}
