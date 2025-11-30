package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Optional;

public class TimeTrialMode implements GameMode {
    private final int MAX_TIME = 600;
    private int remainingSeconds = MAX_TIME;
    private Timeline timer;

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void tick() {
        remainingSeconds--;
        System.out.println("Time left: " + remainingSeconds + "s");
        if (remainingSeconds <= 0) {
            timer.stop();
            // Optional: notify PhaseManager that game is over
        }
    }

    public void stopTimer() {
        if (timer != null) timer.stop();
    }

    @Override
    public boolean shouldDayStart(Database db) {
        return (!hasLost(db) && !hasWon(db));
    }

    @Override
    public void onCorrectSort(Database db, Item item) {
        remainingSeconds += 5; // Correctly sorting an item adds time.
    }

    @Override
    public void onMistake(Database db) {
        remainingSeconds -= 5; // Mistakes subtract time
    }

    @Override
    public boolean allowQuickTimeEvents() {
        return true;
    }

    @Override
    public boolean canGameEnd() {
        return true;
    }

    @Override
    public void onDayEnd(Database db) {
        // Time trial mode does not alter this logic
    }

    @Override
    public boolean hasLost(Database db) {
        return remainingSeconds <= 0;
    }

    private boolean hasWon(Database db) {
        return (remainingSeconds > 0 && db.getDay() > db.getMaxDay());
    }

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
