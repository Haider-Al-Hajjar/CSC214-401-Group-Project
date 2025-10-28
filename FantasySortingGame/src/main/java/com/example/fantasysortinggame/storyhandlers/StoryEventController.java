package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.StoryEvent;

public interface StoryEventController {
    void runStoryEvent(StoryEvent storyEvent, Item item);
    /*
        EventUI.displayStoryEvent(storyEvent)
        startTimer(storyEvent.startingTime)
	    if event not solved in time
            storyEvent.eventSolvedInTime = false
     */
    void onOptionClick(String option);
    /*
        eventSolvedCorrectly = storyEvent.correctOption().equals(option))
        endTimer()
    */
}
