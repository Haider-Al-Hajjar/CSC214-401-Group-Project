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
    private ArrayList<StoryEvent> events;
    Item() {
        this.itemSort = "";
        this.imageLink = null;
        this.description = "";
        this.title = "";
        this.itemType = null;
        this.isSold = false;
        this.events = new ArrayList<>();

    }
    public Item(String itemSort, File imageLink, String description, String title, ItemType itemType, boolean isSold, ArrayList<StoryEvent> events) {
        this.itemSort = itemSort;
        this.imageLink = imageLink;
        this.description = description;
        this.title = title;
        this.itemType = itemType;
        this.isSold = isSold;
        this.events = new ArrayList<>(events);
    }
}
