package com.example.fantasysortinggame.datatypes;

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
}
