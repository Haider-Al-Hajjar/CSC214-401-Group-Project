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


    public String getTrigger() {
        if (getStoryEventTriggers() != null && !getStoryEventTriggers().isEmpty()) {
            StoryEventTrigger trigger = getStoryEventTriggers().get(0);
            if (trigger != null && trigger.getRequiredSort() != null) {
                return trigger.getRequiredSort();
            }
        }
        return "";
    }

    /**
     * Returns the event's title.
     *
     * @return title text
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the event's title.
     *
     * @param title the title to assign
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the event's description text.
     *
     * @return description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the event's description text.
     *
     * @param description the description to assign
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the list of options the player may choose from.
     *
     * @return list of option strings
     */

    public ArrayList<String> getOptions() {
        return options;
    }

    /**
     * Replaces the list of selectable options.
     *
     * @param options new option list
     */
    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    /**
     * Returns the option that correctly solves the event.
     *
     * @return correct option
     */

    public String getCorrectOption() {
        return correctOption;
    }

    /**
     * Sets the correct option for the event.
     *
     * @param correctOption the correct option string
     */

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    /**
     * Returns the event's start time (epoch ms).
     *
     * @return start timestamp
     */
    public long getStart() {
        return start;
    }

    /**
     * Sets the event's start timestamp.
     *
     * @param start epoch ms
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * Returns the event's end time (epoch ms).
     *
     * @return end timestamp
     */
    public long getEnd() {
        return end;
    }

    /**
     * Sets the event's end timestamp.
     *
     * @param end epoch ms
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * Returns the maximum time allowed for the player to respond.
     *
     * @return max time in ms
     */
    public long getMaxTime() {
        return maxTime;
    }

    /**
     * Sets the maximum time the player has to respond.
     *
     * @param maxTime time in ms
     */
    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * Indicates whether the player selected an option before time ran out.
     *
     * @return true if solved within time, false otherwise
     */
    public boolean isSolvedInTime() {
        return solvedInTime;
    }

    /**
     * Marks whether the event was solved before time expired.
     *
     * @param solvedInTime true if solved in time
     */
    public void setSolvedInTime(boolean solvedInTime) {
        this.solvedInTime = solvedInTime;
    }

    /**
     * Indicates whether the player selected the correct option.
     *
     * @return true if solved correctly
     */
    public boolean eventIsSolvedCorrectly() {
        return eventSolvedCorrectly;
    }

    /**
     * Sets whether the player selected the correct option.
     *
     * @param eventSolvedCorrectly true if correct
     */
    public void setEventSolvedCorrectly(boolean eventSolvedCorrectly) {
        this.eventSolvedCorrectly = eventSolvedCorrectly;
    }

    /**
     * Returns a list of item identifiers affected by this event.
     *
     * @return list of affected items
     */
    public ArrayList<String> getItemsAffected() {
        return itemsAffected;
    }

    /**
     * Sets the list of items affected by this event.
     *
     * @param itemsAffected item identifiers
     */
    public void setItemsAffected(ArrayList<String> itemsAffected) {
        this.itemsAffected = itemsAffected;
    }

    // --- Success properties ---

    /**
     * Returns the name of the success outcome item.
     *
     * @return success item name
     */
    public String getSuccessName() {
        return successName;
    }

    /**
     * Returns the sort/category assigned to the success outcome item.
     *
     * @return success sort
     */
    public String getSuccessSort() {
        return successSort;
    }

    /**
     * Returns the type of item created on success.
     *
     * @return success ItemType
     */
    public ItemType getSuccessType() {
        return successType;
    }

    /**
     * Returns the value assigned to the success item.
     *
     * @return success value
     */
    public int getSuccessValue() {
        return successValue;
    }

    /**
     * Returns the description of the success outcome.
     *
     * @return success description
     */
    public String getSuccessDescription() {
        return successDescription;
    }

    /**
     * Returns the name of the failure outcome item.
     *
     * @return failure item name
     */
    public String getFailureName() {
        return failureName;
    }

    /**
     * Returns the sort/category assigned to the failure outcome item.
     *
     * @return failure sort
     */    public String getFailureSort() {
        return failureSort;
    }

    /**
     * Returns the type of item created on failure.
     *
     * @return failure ItemType
     */    public ItemType getFailureType() {
        return failureType;
    }

    /**
     * Returns the value assigned to the failure item.
     *
     * @return failure value
     */public int getFailureValue() {
        return failureValue;
    }

    /**
     * Returns the description of the failure outcome.
     *
     * @return failure description
     */
    public String getFailureDescription() {
        return failureDescription;
    }
}
