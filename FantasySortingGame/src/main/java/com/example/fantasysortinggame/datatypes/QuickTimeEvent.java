package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

/**
 * Represents a time-sensitive choice event where the player must select the correct option.
 * Extends StoryEvent for triggering conditions.
 */
public class QuickTimeEvent extends StoryEvent {

    public QuickTimeEvent(boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers, String title, String description, ArrayList<String> options, String correctOption, long start, long end, long maxTime, boolean solvedInTime, boolean eventSolvedCorrectly, ArrayList<String> itemsAffected, String successName, String successDescription, String successSort, ItemType successType, int successValue, String failureName, String failureDescription, String failureSort, ItemType failureType, int failureValue) {
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
        this.successName = successName;
        this.successDescription = successDescription;
        this.successSort = successSort;
        this.successType = successType;
        this.successValue = successValue;
        this.failureName = failureName;
        this.failureDescription = failureDescription;
        this.failureSort = failureSort;
        this.failureType = failureType;
        this.failureValue = failureValue;
    }

    private String title;
    private String description;
    private ArrayList<String> options;
    private String correctOption;
    private long start;
    private long end;
    private long maxTime;
    private boolean solvedInTime;
    private boolean eventSolvedCorrectly;
    private ArrayList<String> itemsAffected;

    // Success/failure properties
    private String successName;
    private String successDescription;
    private String successSort;
    private ItemType successType;
    private int successValue;

    private String failureName;
    private String failureDescription;
    private String failureSort;
    private ItemType failureType;
    private int failureValue;


    // --- Trigger ---
    public String getTrigger() {
        if (getStoryEventTriggers() != null && !getStoryEventTriggers().isEmpty()) {
            StoryEventTrigger trigger = getStoryEventTriggers().get(0);
            if (trigger != null && trigger.getRequiredSort() != null) {
                return trigger.getRequiredSort();
            }
        }
        return "";
    }

    // --- Getters & setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ArrayList<String> getOptions() { return options; }
    public void setOptions(ArrayList<String> options) { this.options = options; }

    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }

    public long getStart() { return start; }
    public void setStart(long start) { this.start = start; }

    public long getEnd() { return end; }
    public void setEnd(long end) { this.end = end; }

    public long getMaxTime() { return maxTime; }
    public void setMaxTime(long maxTime) { this.maxTime = maxTime; }

    public boolean isSolvedInTime() { return solvedInTime; }
    public void setSolvedInTime(boolean solvedInTime) { this.solvedInTime = solvedInTime; }

    public boolean eventIsSolvedCorrectly() { return eventSolvedCorrectly; }
    public void setEventSolvedCorrectly(boolean eventSolvedCorrectly) { this.eventSolvedCorrectly = eventSolvedCorrectly; }

    public ArrayList<String> getItemsAffected() { return itemsAffected; }
    public void setItemsAffected(ArrayList<String> itemsAffected) { this.itemsAffected = itemsAffected; }

    // --- Success properties ---
    public String getSuccessName() { return successName; }
    public String getSuccessSort() { return successSort; }
    public ItemType getSuccessType() { return successType; }
    public int getSuccessValue() { return successValue; }
    public String getSuccessDescription() { return successDescription;}
    // --- Failure properties ---
    public String getFailureName() { return failureName; }
    public String getFailureSort() { return failureSort; }
    public ItemType getFailureType() { return failureType; }

    public int getFailureValue() { return failureValue; }
    public String getFailureDescription() { return failureDescription;}
}
