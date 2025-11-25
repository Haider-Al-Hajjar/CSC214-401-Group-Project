package com.example.fantasysortinggame.datatypes;

/**
 * Represents a category/type of an item.
 */
public class ItemType {

    private String itemType;

    public ItemType() { this.itemType = ""; }

    public ItemType(String itemType) { this.itemType = itemType; }

    /** @return The string representing the item type */
    public String getItemType() { return itemType; }
}
