package com.example.fantasysortinggame.datatypes;

public class ItemType {
    private String itemType;

    public String getItemType() {
        // fetch day from Database
        // if day is at a certain threshold, return a specific part of the string.
        return itemType;
    }
    public ItemType() {
        this.itemType = "";
    }
    public ItemType(String itemType) {
        this.itemType = itemType;
    }
}
