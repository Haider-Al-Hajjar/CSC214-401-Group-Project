package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Optional;

public class TimedMode implements GameMode {
    private static final int CORRECT_SORT_TIME_BONUS = 5;
    private final int MAX_TIME = 200;
    private int remainingSeconds = MAX_TIME;
    private Timeline timer;
    private int MISTAKE_TIME_PENALTY = -5;

    /**
     * Returns the number of remaining seconds in Timed Mode.
     *
     * @return remaining time in seconds
     */
    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    /**
     * Sets the number of remaining seconds.
     *
     * @param remainingSeconds new remaining time in seconds
     */
    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    /**
     * Starts the countdown timer for Timed Mode.
     */
    public void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Timer tick logic executed every second.
     * Decrements remainingSeconds and stops timer if it reaches zero.
     */
    private void tick() {
        remainingSeconds--;
        System.out.println("Time left: " + remainingSeconds + "s");
        if (remainingSeconds <= 0) {
            timer.stop();
            // Optional: notify PhaseManager that game is over
        }
    }

    /**
     * Stops the countdown timer if it is running.
     */
    public void stopTimer() {
        if (timer != null) timer.stop();
    }

    /**
     * Determines whether a new day should start in Timed Mode.
     *
     * @param db reference to the game database
     * @return true if the player has not won or lost
     */
    @Override
    public boolean shouldDayStart(Database db) {
        return (!hasLost(db) && !hasWon(db));
    }

    /**
     * Logic executed when an item is sorted correctly.
     * Adds bonus seconds to the timer.
     *
     * @param db   reference to the game database
     * @param item the item that was correctly sorted
     */
    @Override
    public void onCorrectSort(Database db, Item item) {
        remainingSeconds += CORRECT_SORT_TIME_BONUS; // Correctly sorting an item adds time.
    }

    /**
     * Logic executed when a sorting mistake occurs.
     * Subtracts time from the timer.
     *
     * @param db reference to the game database
     */
    @Override
    public void onMistake(Database db) {
        remainingSeconds += MISTAKE_TIME_PENALTY; // Mistakes subtract time
    }

    /**
     * Determines if QuickTime events are allowed in Timed Mode.
     *
     * @return true
     */
    @Override
    public boolean allowQuickTimeEvents() {
        return true;
    }

    /**
     * Determines whether the game can end in Timed Mode.
     *
     * @return true
     */
    @Override
    public boolean canGameEnd() {
        return true;
    }

    /**
     * Logic executed at the end of a day in Timed Mode.
     *
     * @param db reference to the game database
     */
    @Override
    public void onDayEnd(Database db) {
        // Time trial mode does not alter this logic
    }

    /**
     * Checks whether the player has lost in Timed Mode.
     *
     * @param db reference to the game database
     * @return true if remaining time is zero or less
     */
    @Override
    public boolean hasLost(Database db) {
        return remainingSeconds <= 0;
    }

    /**
     * Returns a message describing the player's mistake in Timed Mode.
     *
     * @param db reference to the game database
     * @return formatted mistake message with time penalty
     */
    @Override
    public String getMistakeMessage(Database db) {
        return "You made a mistake!\n Your time penalized by " + (MISTAKE_TIME_PENALTY * -1) + " seconds!";
    }

    /**
     * Checks whether the player has won in Timed Mode.
     *
     * @param db reference to the game database
     * @return true if remaining time is positive and maximum day reached
     */
    private boolean hasWon(Database db) {
        return (remainingSeconds > 0 && db.getDay() >= db.getMaxDay());
    }

    /**
     * Checks if Timed Mode has reached an ending condition.
     *
     * @param db reference to the game database
     * @return Optional containing an EndingResult if the mode has ended, otherwise empty
     */
    @Override
    public Optional<EndingResult> checkEnding(Database db) {
        if (hasLost(db)) {
            return Optional.of(new EndingResult(
                    "timetrial_loss",
                    "Ran out of time. Days: " + db.getDay()
            ));
        }
        if (hasWon(db)) {
            return Optional.of(new EndingResult(
                    "timetrial_win",
                    "Completed in " + (MAX_TIME - remainingSeconds) + " seconds."
            ));
        }
        return Optional.empty();
    }
}
