package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for handling Quick Time Events (QTEs) in the game.
 * <p>
 * A Quick Time Event is a timed choice-based event where the player
 * must select the correct option within a limited time. Depending on
 * success or failure, the associated item may be transformed.
 * </p>
 */
public class QuickTimeEventController {

    @FXML private TextField timeLeftField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private Button option1Field;
    @FXML private Button option2Field;

    /** The stage showing this QTE */
    private Stage stage;

    /** The currently running QuickTimeEvent */
    private QuickTimeEvent currentEvent;

    /** The item affected by the current QuickTimeEvent */
    private Item currentItem;

    /** JavaFX timer used to track the QTE duration */
    private Timeline timer;

    /**
     * Sets the stage for this controller.
     * Needed to properly close the window when done.
     *
     * @param stage The stage to assign
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Closes the QTE window and stops the timer.
     */
    private void closeStage() {
        if (timer != null) timer.stop();
        if (stage != null) stage.close();
    }

    /**
     * Launches a QTE window with the given event and item.
     * <p>
     * Loads the FXML, sets up a new stage, and runs the event.
     * </p>
     *
     * @param event The QuickTimeEvent to run
     * @param item  The item affected by the event
     */
    public static void showQuickTimeEventWindow(QuickTimeEvent event, Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    QuickTimeEventController.class.getResource("/path/to/QuickTimeEvent.fxml")
            );
            Parent root = loader.load();

            QuickTimeEventController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Quick Time Event");
            stage.setScene(new Scene(root));
            stage.show();

            controller.setStage(stage);
            controller.runStoryEvent(event, item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a QuickTimeEvent.
     * <p>
     * Initializes timer, sets UI fields, and records start time.
     * </p>
     *
     * @param event The QuickTimeEvent to run
     * @param item  The item affected by the event
     */
    public void runStoryEvent(QuickTimeEvent event, Item item) {
        this.currentEvent = event;
        this.currentItem = item;

        currentEvent.setStart(System.currentTimeMillis());
        currentEvent.setSolvedInTime(false);
        currentEvent.setEventSolvedCorrectly(false);

        // Update UI fields
        titleField.setText(currentEvent.getTitle());
        descriptionField.setText(currentEvent.getDescription());
        option1Field.setText(currentEvent.getOptions().get(0));
        option2Field.setText(currentEvent.getOptions().get(1));

        // Wire buttons
        option1Field.setOnAction(e -> onOptionClick(option1Field.getText()));
        option2Field.setOnAction(e -> onOptionClick(option2Field.getText()));

        // Start countdown timer
        startTimer(currentEvent.getMaxTime());
    }

    /**
     * Starts a countdown timer for the QTE.
     *
     * @param maxTimeMillis Duration of the QTE in milliseconds
     */
    private void startTimer(long maxTimeMillis) {
        long[] timeLeft = {maxTimeMillis / 1000}; // in seconds

        updateTimeDisplay(timeLeft[0]);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft[0]--;
            updateTimeDisplay(timeLeft[0]);

            if (timeLeft[0] <= 0) {
                timer.stop();
                onTimeExpired();
            }
        }));

        timer.setCycleCount((int) timeLeft[0]);
        timer.play();
    }

    /**
     * Updates the UI element displaying time left.
     *
     * @param seconds Time remaining in seconds
     */
    private void updateTimeDisplay(long seconds) {
        timeLeftField.setText(formatTime(seconds));
    }

    /**
     * Converts seconds to a human-readable "MM:SS" format.
     *
     * @param seconds Seconds left
     * @return Formatted string
     */
    private String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    /**
     * Called when the player selects an option.
     *
     * @param option The option chosen
     */
    private void onOptionClick(String option) {
        if (timer != null) timer.stop();

        currentEvent.setEnd(System.currentTimeMillis());
        boolean correct = option.equals(currentEvent.getCorrectOption());
        currentEvent.setEventSolvedCorrectly(correct);
        currentEvent.setSolvedInTime(
                (currentEvent.getEnd() - currentEvent.getStart()) <= currentEvent.getMaxTime()
        );

        applyItemEffects(currentEvent, currentItem);

        closeStage();
    }

    /**
     * Called when the timer expires.
     * Marks the event as failed and closes the window.
     */
    private void onTimeExpired() {
        currentEvent.setEnd(System.currentTimeMillis());
        currentEvent.setEventSolvedCorrectly(false);
        currentEvent.setSolvedInTime(false);

        closeStage();
    }

    /**
     * Applies the effects of the QTE to the item.
     *
     * @param event The QuickTimeEvent
     * @param item  The affected item
     */
    private void applyItemEffects(QuickTimeEvent event, Item item) {
        if (item == null) return;
        Item result = event.isEventSolvedCorrectly() ? event.getSuccessResult() : event.getFailureResult();
        item.copyFrom(result);
    }
}
