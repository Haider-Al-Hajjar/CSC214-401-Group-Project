package com.example.fantasysortinggame.datatypes;

public class StoryEventTrigger {
    private int requiredDay;
    private String requiredSort;
    private boolean requiredItemIsSoldValue;

    public int getRequiredDay() {
        return requiredDay;
    }

    public String getRequiredSort() {
        return requiredSort;
    }

    public boolean isRequiredItemIsSoldValue() {
        return requiredItemIsSoldValue;
    }

    public void setRequiredDay(int requiredDay) {
        this.requiredDay = requiredDay;
    }

    public void setRequiredSort(String requiredSort) {
        this.requiredSort = requiredSort;
    }

    public void setRequiredItemIsSoldValue(boolean requiredItemIsSoldValue) {
        this.requiredItemIsSoldValue = requiredItemIsSoldValue;
    }
}
