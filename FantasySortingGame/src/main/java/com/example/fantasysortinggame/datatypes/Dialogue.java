package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a full dialogue sequence, composed of multiple DialogueEntries.
 * Extends StoryEvent to allow triggering based on game conditions.
 */
public class Dialogue extends StoryEvent {

    /** The list of lines in this dialogue sequence */
    private final List<DialogueEntry> entries;

    /**
     * Constructs a Dialogue object.
     *
     * @param entries           List of dialogue lines
     * @param happened          Whether this dialogue has already occurred
     * @param storyEventTriggers Conditions for triggering this dialogue
     */
    public Dialogue(List<DialogueEntry> entries, boolean happened, ArrayList<StoryEventTrigger> storyEventTriggers) {
        super(happened, storyEventTriggers);
        this.entries = entries;
    }

    /** @return The list of dialogue entries */
    public List<DialogueEntry> getDialogueEntries() {
        return entries;
    }

}
