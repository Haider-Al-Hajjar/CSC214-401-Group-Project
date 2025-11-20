package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.DialogueEntry;
import com.example.fantasysortinggame.datatypes.Npc;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Handles displaying dialogue to the player.
 * Uses a queue to easily manage multi-line dialogues in order.
 */
public class DialogueBoxController {
    private final Database database;
    private Queue<DialogueEntry> dialogueQueue;
    private Dialogue currentDialogue;

    public DialogueBoxController(Database database) {
        this.database = database;
        this.dialogueQueue = new LinkedList<>();
    }

    /**
     * Runs a dialogue sequence.
     * @param dialogue The dialogue to display
     */
    public void runDialogue(Dialogue dialogue) {
        this.currentDialogue = dialogue;
        this.dialogueQueue.clear();
        this.dialogueQueue.addAll(dialogue.getDialogueEntries());
        displayNextDialogueEntry();
    }

    /**
     * Displays the next dialogue entry in the queue.
     * If no entries remain, the dialogue is closed.
     */
    public void displayNextDialogueEntry() {
        if (dialogueQueue.isEmpty()) {
            closeDialogue();
            return;
        }

        DialogueEntry entry = dialogueQueue.poll();

        // Placeholder for UI logic:
        // setNpcUI(entry.getSpeaker()); // Show portrait + name
        // setDialogueBoxText(entry.getText());
        System.out.println(entry.getSpeaker() + ": " + entry.getText());
    }

    /**
     * Closes the dialogue UI.
     */
    public void closeDialogue() {
        if (currentDialogue != null) {
            currentDialogue.setHappened(true);
        }
        // Placeholder for UI cleanup
        // EventUI.closeDialogueBox();
        System.out.println("Dialogue closed.");
    }
}
