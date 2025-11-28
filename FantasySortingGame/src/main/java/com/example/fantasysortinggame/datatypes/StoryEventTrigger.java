package com.example.fantasysortinggame.datatypes;

import com.example.fantasysortinggame.database.Database;

/**
 * Defines conditions for triggering a StoryEvent.
 */
public class StoryEventTrigger {

    private int requiredDay;
    private String requiredSort;
    private boolean requiredItemIsSoldValue;

    public int getRequiredDay() { return requiredDay; }
    public void setRequiredDay(int requiredDay) { this.requiredDay = requiredDay; }
    public String getRequiredSort() { return requiredSort; }
    public void setRequiredSort(String requiredSort) { this.requiredSort = requiredSort; }
    public boolean isRequiredItemIsSoldValue() { return requiredItemIsSoldValue; }
    public void setRequiredItemIsSoldValue(boolean requiredItemIsSoldValue) { this.requiredItemIsSoldValue = requiredItemIsSoldValue; }
    public boolean isTriggered(Database db) {

        // DAY TRIGGER
        if (requiredDay > 0 && db.getDay() < requiredDay) {
            return false;
        }

        // SORT TRIGGER (requires your SortPhase to pass the matched sort string)
        if (requiredSort != null && !requiredSort.isEmpty()) {
            // Sort triggers must be activated from SortPhase manually
            return false;
        }

        // ITEM SOLD TRIGGER (true means: trigger only if ANY item is sold)
        if (requiredItemIsSoldValue) {
            if (db.getUsedItems() == null) return false;
            boolean anySold = db.getUsedItems().stream().anyMatch(item -> item.isSold());
            if (!anySold) return false;
        }

        return true;
    }
}
