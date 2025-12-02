package com.example.fantasysortinggame.datatypes;

import com.example.fantasysortinggame.database.Database;

import java.util.ArrayList;

/**
 * Base class for story-driven events.
 * Supports triggers and tracking whether it has occurred.
 */
public class StoryEvent {

    protected boolean happened;
    protected ArrayList<StoryEventTrigger> storyEventTriggers = new ArrayList<>();
    ;

    public StoryEvent(boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.happened = happened;
        this.storyEventTriggers = storyEventTriggers;
    }

    /**
     * Returns whether the event has already occurred.
     *
     * @return true if the event has happened
     */

    public boolean hasHappened() {
        return happened;
    }

    /**
     * Marks whether the event has occurred.
     *
     * @param happened true if the event has occurred
     */
    public void setHappened(boolean happened) {
        this.happened = happened;
    }

    /**
     * Returns the list of triggers that determine whether this event may occur.
     *
     * @return list of StoryEventTrigger objects
     */
    public ArrayList<StoryEventTrigger> getStoryEventTriggers() {
        return storyEventTriggers;
    }

    /**
     * Determines whether the event should trigger based on its trigger conditions.
     * If no triggers exist, the event triggers automatically.
     *
     * @param db reference to the game database for evaluating conditions
     * @return true if all trigger conditions are satisfied
     */
    public boolean shouldTrigger(Database db) {
        if (storyEventTriggers == null || storyEventTriggers.isEmpty())
            return true; // no conditions means auto-trigger

        for (StoryEventTrigger trigger : storyEventTriggers) {
            if (!trigger.isTriggered(db)) {
                return false;
            }
        }

        return true; // all triggers passed
    }

    /**
     * Sets the list of trigger conditions for this event.
     * If null is provided, the list becomes empty.
     *
     * @param storyEventTriggers new trigger list
     */
    public void setStoryEventTriggers(ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.storyEventTriggers = storyEventTriggers != null ? storyEventTriggers : new ArrayList<>();
    }

    /**
     * Checks if this event is triggered.
     * Default implementation returns false and is meant to be overridden.
     *
     * @return false by default
     */
    public boolean isTriggered() {
        return false;
    }

    /**
     * Checks if this event is triggered based on a provided item sort.
     * Default implementation returns false and is meant to be overridden.
     *
     * @param newSort item sort to evaluate
     * @return false by default
     */
    public boolean isTriggered(String newSort) {
        return false;
    }
}
