package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;

public class DialogueBoxController {
    private final Database database;
    private Dialogue dialogue = null;

    public DialogueBoxController(Database database) {
        this.database = database;
    }

    void runDialogue(Dialogue dialogue) {
        /*
                this.dialogue = dialogue;
                displayDialogue(dialogue, "")
                dialogue.setHasHappened(true)
        */
    }
    void displayDialogue(Dialogue dialogue, String theStorySoFar) {
        /*
            if thestory so far != dialogue.getDialogue
                update the screen with dialogue after theStorySoFar
                    if the string is longer than the window, then stop at a specific point.
            else
                closeDialogue
         */
    }

    void onNextButtonClickHandler(){
        /*
            get the text from the display and store in
            String theStorySoFar
            displayDialogue(dialogue, theStorySoFar);
         */
    }
}
