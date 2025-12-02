package com.example.fantasysortinggame.datatypes;

import com.example.fantasysortinggame.database.Database;

/**
 * Defines conditions for triggering a StoryEvent.
 * <p>
 * Fields that are "empty" (0, null, false) are ignored.
 * A trigger only fires if **all active conditions are satisfied**.
 */
public class StoryEventTrigger {

    // --- Conditions ---
    private int requiredDay;                // 0 = ignore, >0 = only trigger on this day or later
    private String requiredSort;            // null or "" = ignore, non-empty = must match sort string
    private boolean happensOnSale;          // false = ignore, true = must have any sold item

    // --- Getters / Setters ---
    public int getRequiredDay() {
        return requiredDay;
    }

    public void setRequiredDay(int requiredDay) {
        this.requiredDay = requiredDay;
    }

    public String getRequiredSort() {
        return requiredSort;
    }

    public void setRequiredSort(String requiredSort) {
        this.requiredSort = requiredSort;
    }

    public boolean happensOnSale() {
        return happensOnSale;
    }

    public void setHappensOnSale(boolean happensOnSale) {
        this.happensOnSale = happensOnSale;
    }

    /**
     * Generic trigger check.
     *
     * @param db          Database reference.
     * @param currentSort Optional: current sort string if called during sorting. Can be null for day-start or sale triggers.
     * @return true if all active conditions are satisfied.
     */
    public boolean isTriggered(Database db, String currentSort, boolean isBeingSold) {
        boolean requiredDayFlag = requiredDay <= db.getDay();// if we've reached an acceptable day.
        boolean requiredSortFlag = true;
        boolean happensOnSaleFlag = true;
        if (!(requiredSort.isEmpty())) { // if this is an event that triggers on sort.
            if (!requiredSort.equalsIgnoreCase(currentSort)) {
                // but it isn't being sorted or isn't being sorted in a way that we care about
                // (equalsignorecase() handles null)
                requiredSortFlag = false; // flag failed
            }
        }
        if (happensOnSale) {
            if (!isBeingSold) {
                // if the event only happens on sale, check
                happensOnSaleFlag = false;
            }

        }

        // All active conditions passed
        return requiredDayFlag && requiredSortFlag && happensOnSaleFlag;
    }

    /**
     * Convenience overload for calls where sort and sale are not relevant.
     */
    public boolean isTriggered(Database db) {
        return isTriggered(db, null, false);
    }
}
