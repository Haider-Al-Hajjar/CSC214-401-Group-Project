package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

    /**
     * The currently running QuickTimeEvent
     */
    private QuickTimeEvent currentEvent;

    /**
     * The item affected by the current QuickTimeEvent
     */
    private Item currentItem;

    /**
     * JavaFX timer used to track the QTE duration
     */
    private Timeline timer;

    /**
     * Starts a QuickTimeEvent for a given item.
     * <p>
     * This method sets the start time, initializes the solved flags,
     * displays the event in the UI (placeholder comment), and starts
     * the countdown timer based on the event's maximum allowed time.
     * </p>
     *
     * @param event The QuickTimeEvent to run
     * @param item  The item that will be affected by the event
     */
    public void runStoryEvent(QuickTimeEvent event, Item item) {
        this.currentEvent = event;
        this.currentItem = item;

        // Record start time
        currentEvent.setStart(System.currentTimeMillis());
        currentEvent.setSolvedInTime(false);
        currentEvent.setEventSolvedCorrectly(false);

        // Display the event in the UI
        // EventUI.displayStoryEvent(currentEvent);

        // Start the countdown timer
        startTimer(currentEvent.getMaxTime());
    }

    /**
     * Starts a countdown timer for the QuickTimeEvent.
     * <p>
     * When the timer expires, {@link #onTimeExpired()} is called.
     * </p>
     *
     * @param maxTime Duration of the QTE in milliseconds
     */
    private void startTimer(long maxTime) {
        timer = new Timeline(new KeyFrame(Duration.millis(maxTime), e -> onTimeExpired()));
        timer.setCycleCount(1);
        timer.play();
    }

    /**
     * Handles the player's option selection for the QTE.
     * <p>
     * This method stops the timer, records the end time, checks if the
     * chosen option is correct, marks whether the event was solved in time,
     * applies effects to the item, and closes the event UI.
     * </p>
     *
     * @param option The option chosen by the player
     */
    public void onOptionClick(String option) {
        if (timer != null) timer.stop();

        // Record end time
        currentEvent.setEnd(System.currentTimeMillis());

        // Check correctness
        boolean correct = option.equals(currentEvent.getCorrectOption());
        currentEvent.setEventSolvedCorrectly(correct);

        // Determine if solved in time
        long elapsed = currentEvent.getEnd() - currentEvent.getStart();
        currentEvent.setSolvedInTime(elapsed <= currentEvent.getMaxTime());

        // Apply any item transformations
        applyItemEffects(currentEvent, currentItem);

        // Close UI (placeholder for JavaFX implementation)
        // EventUI.endEventDisplay();
    }

    /**
     * Called automatically when the QTE timer expires.
     * <p>
     * Marks the event as failed, both in correctness and timing, and closes the UI.
     * </p>
     */
    private void onTimeExpired() {
        currentEvent.setEnd(System.currentTimeMillis());
        currentEvent.setSolvedInTime(false);
        currentEvent.setEventSolvedCorrectly(false);

        // Close UI (placeholder for JavaFX implementation)
        // EventUI.endEventDisplay();
    }

    /**
     * Applies the effects of the QTE to the affected item.
     * <p>
     * Depending on whether the event was solved correctly, the item is
     * updated using the success or failure result item.
     * </p>
     *
     * @param event The QuickTimeEvent containing the result items
     * @param item  The item to apply the transformation to
     */
    private void applyItemEffects(QuickTimeEvent event, Item item) {
        if (item == null) return;

        Item result = event.isEventSolvedCorrectly() ? event.getSuccessResult() : event.getFailureResult();

        // Update the item's properties
        item.copyFrom(result);
    }
}
