package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

public class StoryEvent {
    private String title;
    private String description;
    private ArrayList<String> options;
    private String correctOption;
    private long start;
    private long end;
    private long maxTime;
    private boolean solvedInTime;
    private boolean eventHasHappened;
    private boolean eventSolvedCorrectly;
    private ArrayList<Item> itemsAffected;
    private ArrayList<StoryEventTrigger> storyEventTriggers;
    boolean isEventValid(ArrayList<StoryEventTrigger> storyEventTriggers) {
        // loop through arraylist and check each trigger.
        return false;
    }

    public StoryEvent(String title, String description, ArrayList<String> options, String correctOption, long start, long end, long maxTime, boolean solvedInTime, boolean eventHasHappened, boolean eventSolvedCorrectly, ArrayList<Item> itemsAffected, ArrayList<StoryEventTrigger> storyEventTriggers) {
        this.title = title;
        this.description = description;
        this.options = options;
        this.correctOption = correctOption;
        this.start = start;
        this.end = end;
        this.maxTime = maxTime;
        this.solvedInTime = solvedInTime;
        this.eventHasHappened = eventHasHappened;
        this.eventSolvedCorrectly = eventSolvedCorrectly;
        this.itemsAffected = itemsAffected;
        this.storyEventTriggers = storyEventTriggers;
    }
}
