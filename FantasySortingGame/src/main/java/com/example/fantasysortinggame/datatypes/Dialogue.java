package com.example.fantasysortinggame.datatypes;
import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.StoryEventTrigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a full dialogue sequence, composed of multiple DialogueEntries.
 * Extends StoryEvent to allow triggering based on game conditions.
 */
public class Dialogue extends StoryEvent {

    /**
     * The list of lines in this dialogue sequence
     */
    private final List<DialogueEntry> entries;

    /**
     * Constructs a Dialogue object.
     *
     * @param entries            List of dialogue lines
     * @param happened           Whether this dialogue has already occurred
     * @param storyEventTriggers Conditions for triggering this dialogue
     */
    public Dialogue(List<DialogueEntry> entries, boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers) {
        super(happened, storyEventTriggers);
        this.entries = entries;
    }

    /**
     * @return The list of dialogue entries
     */
    public List<DialogueEntry> getDialogueEntries() {
        return entries;
    }

    public String getSpeaker() {
        if (entries != null && entries.size() > 0 && entries.get(0).getSpeaker() != null) {
            return entries.get(0).getSpeaker().getName();
        }
        return "Unknown";
    }

    public String getText() {
        if (entries != null && entries.size() > 0) {
            return entries.get(0).getText();
        }
        return "";
    }

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
}
