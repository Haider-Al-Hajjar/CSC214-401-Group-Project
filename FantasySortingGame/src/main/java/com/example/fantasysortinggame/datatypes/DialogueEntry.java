package com.example.fantasysortinggame.datatypes;

import com.example.fantasysortinggame.datatypes.Npc;

/**
 * Represents a single line of dialogue spoken by an NPC.
 */
public class DialogueEntry {

    /** The NPC speaking this line */
    private final Npc speaker;

    /** The text of the dialogue */
    private final String text;

    /**
     * Constructs a DialogueEntry.
     *
     * @param speaker The NPC speaking
     * @param text    The text spoken
     */
    public DialogueEntry(Npc speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    /** @return The speaker */
    public Npc getSpeaker() {
        return speaker;
    }

    /** @return The text of the dialogue line */
    public String getText() {
        return text;
    }
}
