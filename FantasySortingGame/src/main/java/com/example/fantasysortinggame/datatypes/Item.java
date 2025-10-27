package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

public class Item {
    private String itemSort;
    private String imageLink;
    private String description;
    private String title;
    private ItemType itemType;
    private boolean isSold;
    private ArrayList<Event> events;
    Item() {
        this.itemSort = "";
        this.imageLink = "";
        this.description = "";
        this.title = "";
        this.itemType = null;
        this.isSold = false;
        this.events = new ArrayList<>();

    }
    public Item(String itemSort, String imageLink, String description, String title, ItemType itemType, boolean isSold, ArrayList<Event> events) {
        this.itemSort = itemSort;
        this.imageLink = imageLink;
        this.description = description;
        this.title = title;
        this.itemType = itemType;
        this.isSold = isSold;
        this.events = new ArrayList<>(events);
    }
}
