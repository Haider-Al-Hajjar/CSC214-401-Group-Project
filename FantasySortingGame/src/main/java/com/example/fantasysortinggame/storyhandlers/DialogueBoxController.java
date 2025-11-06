package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;

public class DialogueBoxController {
    private final Database database;
    private Dialogue currentDialogue = null;
    private String newNpcInDialogueCharSequence = "newNPC:";
    public DialogueBoxController(Database database) {
        this.database = database;
    }

    public void runDialogue(Dialogue dialogue) {
        currentDialogue = dialogue;
        displayDialogue(currentDialogue, "");
    }

    public void displayDialogue(Dialogue dialogue, String theStorySoFar) {
        /*
            NOTE TO SELF: CUT THE DIALOGUE INTO CHUNKS ELSEWHERE AND THEN JUST DISPLAY A CHUNK!!!!!
         */


        if (theStorySoFar.equals(dialogue.getDialogue())) {
            currentDialogue.setHappened(true);
            closeDialogue();
        }
        else {
            int startIndex = dialogue.getDialogue().indexOf(theStorySoFar) + theStorySoFar.length();
            String displayString = dialogue.getDialogue().substring(startIndex, (startIndex+20));
            // later, the displaystring will display an appropriate number of characters.
            if (displayString.contains(newNpcInDialogueCharSequence)) {

            }
        }
        /*
            if thestory so far != dialogue.getDialogue
                update the screen with dialogue after theStorySoFar
                    if the string is longer than the window, then stop at a specific point.
            else
                currentDialogue.setHappened(true);
                closeDialogue
         */

    }

    public void closeDialogue() {
        // This will eventually just delete the stage showing the dialogue.
    }

    public void onNextButtonClickHandler() {
        /*
            get the text from the display and store in
            String theStorySoFar
            displayDialogue(dialogue, theStorySoFar);
         */
    }
}
