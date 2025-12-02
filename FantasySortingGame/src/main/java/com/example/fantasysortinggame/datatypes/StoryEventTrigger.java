package com.example.fantasysortinggame.datatypes;

import com.example.fantasysortinggame.database.Database;

/**
 * Defines conditions for triggering a StoryEvent.
 * <p>
 * Fields that are "empty" (0, null, false) are ignored.
 * A trigger only fires if **all active conditions are satisfied**.
 */
public class StoryEventTrigger {

    private int requiredDay;                // 0 = ignore, >0 = only trigger on this day or later
    private String requiredSort;            // null or "" = ignore, non-empty = must match sort string
    private boolean happensOnSale;          // false = ignore, true = must have any sold item

    /**
     * Returns the minimum in-game day required for this trigger.
     * A value of 0 indicates the day condition is ignored.
     *
     * @return required day, or 0 if unused
     */
    public int getRequiredDay() {
        return requiredDay;
    }

    /**
     * Sets the minimum in-game day required for this trigger.
     * A value of 0 disables day checking.
     *
     * @param requiredDay the minimum day required
     */
    public void setRequiredDay(int requiredDay) {
        this.requiredDay = requiredDay;
    }

    /**
     * Returns the required item sort string for this trigger.
     * An empty string indicates this condition is ignored.
     *
     * @return required sort string
     */
    public String getRequiredSort() {
        return requiredSort;
    }

    /**
     * Sets the required item sort for this trigger.
     * Use null or an empty string to disable sort checking.
     *
     * @param requiredSort sort string to match
     */
    public void setRequiredSort(String requiredSort) {
        this.requiredSort = requiredSort;
    }

    /**
     * Indicates whether this trigger activates only during a sale action.
     * false means the sale condition is ignored.
     *
     * @return true if the trigger requires a sale event
     */
    public boolean happensOnSale() {
        return happensOnSale;
    }

    /**
     * Sets whether this trigger should activate only during a sale action.
     *
     * @param happensOnSale true if trigger requires a sale
     */
    public void setHappensOnSale(boolean happensOnSale) {
        this.happensOnSale = happensOnSale;
    }

    /**
     * Evaluates all active trigger conditions.
     *
     * <ul>
     *   <li>Day condition passes if requiredDay is 0 or the current day is >= requiredDay.</li>
     *   <li>Sort condition passes if requiredSort is empty or matches currentSort (case-insensitive).</li>
     *   <li>Sale condition passes if happensOnSale is false or isBeingSold is true.</li>
     * </ul>
     *
     * @param db          reference to game database
     * @param currentSort sort involved in the action, or null if not applicable
     * @param isBeingSold true if the current action is selling an item
     * @return true if all enabled conditions are satisfied
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
     * Simplified trigger check when neither sort nor sale conditions apply.
     *
     * @param db reference to game database
     * @return true if trigger conditions are satisfied
     */
    public boolean isTriggered(Database db) {
        return isTriggered(db, null, false);
    }
}
