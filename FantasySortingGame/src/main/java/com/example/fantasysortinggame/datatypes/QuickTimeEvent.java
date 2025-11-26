package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

/**
 * Represents a time-sensitive choice event where the player must select the correct option.
 * Extends StoryEvent for triggering conditions.
 */
public class QuickTimeEvent extends StoryEvent {

    private String title;
    private String description;
    private ArrayList<String> options;
    private String correctOption;
    private long start;
    private long end;
    private long maxTime;
    private boolean solvedInTime;
    private boolean eventSolvedCorrectly;
    private ArrayList<Item> itemsAffected;
    private Item successResult;
    private Item failureResult;

    /**
     * Full constructor
     */
    public QuickTimeEvent(boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers, String title, String description, ArrayList<String> options, String correctOption, long start, long end, long maxTime, boolean solvedInTime, boolean eventSolvedCorrectly, ArrayList<Item> itemsAffected, Item successResult, Item failureResult) {
        super(happened, storyEventTriggers);
        this.title = title;
        this.description = description;
        this.options = options;
        this.correctOption = correctOption;
        this.start = start;
        this.end = end;
        this.maxTime = maxTime;
        this.solvedInTime = solvedInTime;
        this.eventSolvedCorrectly = eventSolvedCorrectly;
        this.itemsAffected = itemsAffected;
        this.successResult = successResult;
        this.failureResult = failureResult;
    }

    public String getTrigger() {
        if (getStoryEventTriggers() != null && getStoryEventTriggers().size() > 0) {
            StoryEventTrigger trigger = getStoryEventTriggers().get(0);
            if (trigger != null && trigger.getRequiredSort() != null) {
                return trigger.getRequiredSort();
            }
        }
        return "";
    }

    // --- Getters and setters ---
    public Item getSuccessResult() {
        return successResult;
    }

    public void setSuccessResult(Item successResult) {
        this.successResult = successResult;
    }

    public Item getFailureResult() {
        return failureResult;
    }

    public void setFailureResult(Item failureResult) {
        this.failureResult = failureResult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isSolvedInTime() {
        return solvedInTime;
    }

    public void setSolvedInTime(boolean solvedInTime) {
        this.solvedInTime = solvedInTime;
    }

    public boolean isEventSolvedCorrectly() {
        return eventSolvedCorrectly;
    }

    public void setEventSolvedCorrectly(boolean eventSolvedCorrectly) {
        this.eventSolvedCorrectly = eventSolvedCorrectly;
    }

    public ArrayList<Item> getItemsAffected() {
        return itemsAffected;
    }

    public void setItemsAffected(ArrayList<Item> itemsAffected) {
        this.itemsAffected = itemsAffected;
    }

}
