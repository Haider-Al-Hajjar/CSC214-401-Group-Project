package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

/**
 * Base class for story-driven events.
 * Supports triggers and tracking whether it has occurred.
 */
public class StoryEvent {

    protected boolean happened;
    protected  ArrayList<StoryEventTrigger> storyEventTriggers;

    public StoryEvent(boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.happened = happened;
        this.storyEventTriggers = storyEventTriggers;
    }

    public boolean hasHappened() { return happened; }
    public void setHappened(boolean happened) { this.happened = happened; }
    public ArrayList<StoryEventTrigger> getStoryEventTriggers() { return storyEventTriggers; }
    public void setStoryEventTriggers(ArrayList<StoryEventTrigger> storyEventTriggers) { this.storyEventTriggers = storyEventTriggers; }

    public boolean isTriggered() { return false; }
    public boolean isTriggered(String newSort) { return false; }
}
