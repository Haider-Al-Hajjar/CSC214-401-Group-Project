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
/**
 * Controller responsible for managing and displaying in-game dialogue windows.
 * <p>
 * This controller handles:
 * <ul>
 *     <li>Loading speaker portraits and names,</li>
 *     <li>Displaying dialogue text sequentially,</li>
 *     <li>Managing the continue button,</li>
 *     <li>Tracking dialogue progression, and</li>
 *     <li>Notifying listeners when the dialogue finishes.</li>
 * </ul>
 * It is instantiated by {@link #loadDialogue(Database, Dialogue)} with
 * dependency injection for database access.
 * </p>
 */
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
    /**
     * Creates a new {@code DialogueBoxController} and injects the game's
     * {@link Database} instance for use in character, story, and asset
     * lookups during dialogue processing.
     *
     * @param database the game's central {@link Database} used to resolve
     *                 speaker data, profile images, and dialogue metadata
     */
    public DialogueBoxController(Database database) {
        this.database = database;
    }
    /**
     * Registers a callback to be executed when the dialogue sequence finishes.
     * <p>
     * This callback is triggered after the final dialogue entry is displayed
     * and the dialogue UI is closed. It is optional and may be used to advance
     * story progress, trigger a quest event, or resume gameplay.
     * </p>
     *
     * @param callback a {@link Runnable} to execute upon dialogue completion;
     *                 may be {@code null} if no action is needed
     */
    public void setOnDialogueEnd(Runnable callback) {
        this.onDialogueEnd = callback;
    }
    /**
     * Begins a dialogue sequence by loading all dialogue entries into an
     * internal queue and displaying the first entry.
     * <p>
     * This method prepares the controller to run the dialogue by:
     * </p>
     * <ul>
     *     <li>storing the active {@link Dialogue} reference,</li>
     *     <li>resetting the internal entry queue,</li>
     *     <li>assigning a click handler to the continue button, and</li>
     *     <li>displaying the first dialogue entry immediately.</li>
     * </ul>
     *
     * @param dialogue the {@link Dialogue} containing the ordered list of
     *                 {@link DialogueEntry} objects to display to the player
     *
     * @see #displayNextDialogueEntry()
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
    /**
     * Displays the next entry in the dialogue sequence.
     * <p>
     * This method pulls the next {@link DialogueEntry} from the internal queue,
     * updates the speaker name, profile image, and dialogue text, and refreshes
     * the dialogue UI. If the queue is empty, the dialogue ends automatically.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>If no entries remain, closes the dialogue.</li>
     *     <li>Updates the name label with the speaker’s name.</li>
     *     <li>Attempts to load and display the speaker’s profile image.</li>
     *     <li>Displays the dialogue text in the dialogue label.</li>
     * </ul>
     *
     * @see DialogueEntry
     * @see #closeDialogue()
     */
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
    /**
     * Ends the current dialogue sequence, hides the dialogue UI, and runs any
     * registered completion callback.
     * <p>
     * This method is called automatically when all dialogue entries have been
     * displayed or may be triggered manually if needed. It also marks the
     * dialogue as "happened" for story progression.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Marks the dialogue as completed in the {@link Dialogue} object.</li>
     *     <li>Hides the dialogue UI by setting the root pane invisible.</li>
     *     <li>Executes the {@code onDialogueEnd} callback if one is set.</li>
     * </ul>
     *
     * @see Dialogue#setHappened(boolean)
     * @see #setOnDialogueEnd(Runnable)
     */
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
     * Loads and initializes a dialogue UI element from the FXML file.
     * <p>
     * This method creates a new {@link DialogueBoxController} (injecting the
     * provided {@link Database}), loads the {@code dialogueBox.fxml} file,
     * starts the dialogue sequence using the given {@link Dialogue} object,
     * and returns the fully constructed {@link AnchorPane} UI node.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Loads the DialogueBox UI from the FXML file.</li>
     *     <li>Injects the game's {@link Database} into the controller.</li>
     *     <li>Runs the provided {@link Dialogue} through the controller.</li>
     *     <li>Returns the fully initialized UI component for display.</li>
     *     <li>On failure, prints the stack trace and returns an empty {@link AnchorPane}.</li>
     * </ul>
     *
     * @param db        the game {@link Database} instance used for character, item,
     *                  and story lookups in dialogue processing
     * @param dialogue  the {@link Dialogue} object containing the scripted lines,
     *                  choices, or branching logic to be displayed
     *
     * @return the root {@link AnchorPane} containing the dialogue UI; an empty pane
     *         is returned if an error occurs during loading
     *
     * @see DialogueBoxController
     * @see FXMLLoader
     * @see Dialogue
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
