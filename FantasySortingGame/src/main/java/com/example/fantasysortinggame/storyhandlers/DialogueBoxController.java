package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.DialogueEntry;
import com.example.fantasysortinggame.gamephasemanager.GameEngine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class DialogueBoxController {

    @FXML
    Label nameLabel;
    @FXML
    ImageView imageView;
    @FXML
    Label dialogueLabel;
    @FXML
    Button continueButton;
    @FXML
    AnchorPane rootPane;

    private Dialogue currentDialogue;
    private final Queue<DialogueEntry> dialogueQueue = new LinkedList<>();
    private final Database database;

    // Callback for when the dialogue ends
    private Runnable onDialogueEnd;

    public DialogueBoxController(Database database) {
        this.database = database;
    }

    /**
     * Set a callback to run when the dialogue ends
     */
    public void setOnDialogueEnd(Runnable callback) {
        this.onDialogueEnd = callback;
    }

    /**
     * Start displaying the dialogue
     */
    public void runDialogue(Dialogue dialogue ) {
        this.currentDialogue = dialogue;
        dialogueQueue.clear();
        dialogueQueue.addAll(dialogue.getDialogueEntries());

        // Continue button advances dialogue
        continueButton.setOnAction(e -> {
            GameEngine.getSoundController().playButtonClick();
            displayNextDialogueEntry();
        });
        displayNextDialogueEntry();
    }

    private void displayNextDialogueEntry() {
        if (dialogueQueue.isEmpty()) {
            closeDialogue();
            return;
        }

        DialogueEntry entry = dialogueQueue.poll();
        nameLabel.setText(entry.getSpeaker().getName());

        try {
            Image pfp = new Image(
                    getClass().getResourceAsStream("/" + entry.getSpeaker().getProfilePicturePath())
            );
            imageView.setImage(pfp);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImage(null); // fallback
        }

        dialogueLabel.setText(entry.getText());
    }

    private void closeDialogue() {
        if (currentDialogue != null) currentDialogue.setHappened(true);

        // Hide UI
        rootPane.setVisible(false);

        // Run callback if set
        if (onDialogueEnd != null) {
            onDialogueEnd.run();
        }
    }

    /**
     * Load the FXML for inline embedding
     */
    public static AnchorPane loadDialogue(Database db, Dialogue dialogue) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogueBoxController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/dialogueBox.fxml")
            );
            loader.setControllerFactory(param -> new DialogueBoxController(db));

            AnchorPane dialogueUI = loader.load();
            DialogueBoxController controller = loader.getController();
            controller.runDialogue(dialogue);

            return dialogueUI;

        } catch (IOException e) {
            e.printStackTrace();
            return new AnchorPane();
        }
    }
}
