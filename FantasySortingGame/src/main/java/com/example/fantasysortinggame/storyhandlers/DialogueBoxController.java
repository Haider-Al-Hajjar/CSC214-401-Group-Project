package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.DialogueEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Controller for displaying dialogue to the player.
 * <p>
 * Each dialogue sequence opens in its own Stage. Dialogue entries are
 * queued and displayed in order. The stage closes automatically when
 * the dialogue finishes.
 * </p>
 */
public class DialogueBoxController {

    @FXML private TextField nameField;
    @FXML private ImageView imageView;
    @FXML private TextField dialogueField;
    @FXML private Button continueButton;

    /** Root pane of the FXML (used to hide/close UI) */
    @FXML private AnchorPane rootPane;

    /** Stage showing this dialogue */
    private Stage stage;

    /** Dialogue being displayed */
    private Dialogue currentDialogue;

    /** Queue of dialogue entries to display */
    private final Queue<DialogueEntry> dialogueQueue;

    /** Database reference (optional, in case needed for NPCs etc.) */
    private final Database database;

    /** Constructor */
    public DialogueBoxController(Database database) {
        this.database = database;
        this.dialogueQueue = new LinkedList<>();
    }

    /** Assigns the Stage to this controller */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Launches a new dialogue window with a stage.
     *
     * @param database Database reference for this dialogue
     * @param dialogue Dialogue sequence to display
     */
    public static void showDialogueWindow(Database database, Dialogue dialogue) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogueBoxController.class.getResource("/path/to/DialogueBox.fxml")
            );
            loader.setControllerFactory(param -> new DialogueBoxController(database));
            Parent root = loader.load();

            DialogueBoxController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Dialogue");
            stage.setScene(new Scene(root));
            stage.show();

            controller.setStage(stage);
            controller.runDialogue(dialogue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the dialogue sequence.
     *
     * @param dialogue The Dialogue to display
     */
    private void runDialogue(Dialogue dialogue) {
        this.currentDialogue = dialogue;
        dialogueQueue.clear();
        dialogueQueue.addAll(dialogue.getDialogueEntries());

        // Wire the Continue button to advance dialogue
        continueButton.setOnAction(e -> displayNextDialogueEntry());

        displayNextDialogueEntry();
    }

    /**
     * Displays the next dialogue entry in the queue.
     * Closes the stage if no entries remain.
     */
    private void displayNextDialogueEntry() {
        if (dialogueQueue.isEmpty()) {
            closeDialogue();
            return;
        }

        DialogueEntry entry = dialogueQueue.poll();
        nameField.setText(entry.getSpeaker().getName());

        // Load profile image
        Image pfp = new Image(String.valueOf(entry.getSpeaker().getProfilePicturePath()));
        imageView.setImage(pfp);

        dialogueField.setText(entry.getText());
    }

    /**
     * Closes the dialogue window.
     * Marks the dialogue as completed.
     */
    private void closeDialogue() {
        if (currentDialogue != null) {
            currentDialogue.setHappened(true);
        }

        if (stage != null) {
            stage.close();
        } else {
            rootPane.setVisible(false); // fallback
        }

        System.out.println("Dialogue closed.");
    }
}
