package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

public class StoryEvent {
    private boolean happened;
    private ArrayList<StoryEventTrigger> storyEventTriggers;
    public boolean isTriggered() {
        // run through the story event triggers to see if it's been triggered.
        return false;
    }
    public boolean isTriggered(String newSort) {
        // run through the story event triggers to see if it's been triggered.
        // also check if newSort is a trigger and if so what does it change.
        return false;
    }

    public StoryEvent(boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.happened = happened;
        this.storyEventTriggers = storyEventTriggers;
    }

    public boolean hasHappened() {
        return happened;
    }

    public void setHappened(boolean happened) {
        this.happened = happened;
    }

    public ArrayList<StoryEventTrigger> getStoryEventTriggers() {
        return storyEventTriggers;
    }

    public void setStoryEventTriggers(ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.storyEventTriggers = storyEventTriggers;
    }
}
