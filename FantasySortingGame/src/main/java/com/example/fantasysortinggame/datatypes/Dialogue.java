package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

public class Dialogue extends StoryEvent {
    private String dialogue;
    private ArrayList<Npc> charactersAppearing;

    public Dialogue(ArrayList<StoryEventTrigger> storyEventTriggers, String dialogue, ArrayList<Npc> charactersAppearing) {
        super(false, storyEventTriggers);
        this.dialogue = dialogue;
        this.charactersAppearing = charactersAppearing;
    }

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public ArrayList<Npc> getCharactersAppearing() {
        return charactersAppearing;
    }

    public void setCharactersAppearing(ArrayList<Npc> charactersAppearing) {
        this.charactersAppearing = charactersAppearing;
    }
}
