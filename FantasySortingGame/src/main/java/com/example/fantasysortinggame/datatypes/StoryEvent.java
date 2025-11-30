package com.example.fantasysortinggame.datatypes;

import com.example.fantasysortinggame.database.Database;

import java.util.ArrayList;

/**
 * Base class for story-driven events.
 * Supports triggers and tracking whether it has occurred.
 */
public class StoryEvent {

    protected boolean happened;
    protected  ArrayList<StoryEventTrigger> storyEventTriggers= new ArrayList<>();;

    public StoryEvent(boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.happened = happened;
        this.storyEventTriggers = storyEventTriggers;
    }


    public boolean hasHappened() { return happened; }
    public void setHappened(boolean happened) { this.happened = happened; }
    public ArrayList<StoryEventTrigger> getStoryEventTriggers() { return storyEventTriggers; }
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
    public void setStoryEventTriggers(ArrayList<StoryEventTrigger> storyEventTriggers) { this.storyEventTriggers = storyEventTriggers != null ? storyEventTriggers : new ArrayList<>(); }

    public boolean isTriggered() { return false; }
    public boolean isTriggered(String newSort) { return false; }
}
