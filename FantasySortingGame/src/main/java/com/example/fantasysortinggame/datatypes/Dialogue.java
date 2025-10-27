package com.example.fantasysortinggame.datatypes;

import java.util.ArrayList;

public class Dialogue {
    String dialogue;
    boolean hasHappened;
    ArrayList<Npc> charactersAppearing;
    boolean isDialogueTriggered(){
        // should check the game state to see if it matches the dialogue.
        // should probably also have a class for dialogue triggers just like event triggers.
        return false;
    }
}
