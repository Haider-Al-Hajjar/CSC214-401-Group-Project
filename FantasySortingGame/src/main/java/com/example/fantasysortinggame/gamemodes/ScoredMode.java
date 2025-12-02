package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class ScoredMode implements GameMode {
    private int score = 0;
    private final int CORRECT_SORT_SCORE_BONUS = 10;
    private final int MISTAKE_SCORE_PENALTY = -20;

    /**
     * Determines whether a new day should start in Score Attack mode.
     *
     * @param db reference to the game database
     * @return true if the day should start, false otherwise
     */
    @Override
    public boolean shouldDayStart(Database db) {
        // hasWon checks for MAX_DAY (also hasLost is always false)
        return (!hasLost(db) && !hasWon(db));
    }

    /**
     * Updates the score when an item is sorted correctly.
     *
     * @param db   reference to the game database
     * @param item the item that was correctly sorted
     */
    @Override
    public void onCorrectSort(Database db, Item item) {
        score += CORRECT_SORT_SCORE_BONUS;
    }

    /**
     * Updates the score and mistake count when a sorting mistake occurs.
     *
     * @param db reference to the game database
     */
    @Override
    public void onMistake(Database db) {
        score += MISTAKE_SCORE_PENALTY;
        db.setMistakes(db.getMistakes() + 1);
    }

    /**
     * Determines if QuickTime events are allowed in Score Attack mode.
     *
     * @return true if QuickTime events are enabled
     */
    @Override
    public boolean allowQuickTimeEvents() {
        return true;
    }

    /**
     * Determines whether the game can end in Score Attack mode.
     *
     * @return true if the game can end
     */
    @Override
    public boolean canGameEnd() {
        return true;
    }

    /**
     * Logic executed at the end of a day in Score Attack mode.
     *
     * @param db reference to the game database
     */
    @Override
    public void onDayEnd(Database db) {
        // Score attack mode does not alter this logic
    }

    /**
     * Checks whether the player has lost in Score Attack mode.
     *
     * @param db reference to the game database
     * @return false (player cannot lose in this mode)
     */
    @Override
    public boolean hasLost(Database db) {
        // You cannot lose in Score attack mode.
        return false;
    }

    /**
     * Returns a message describing the player's score and mistakes after an error.
     *
     * @param db reference to the game database
     * @return mistake message string
     */
    @Override
    public String getMistakeMessage(Database db) {
        return "You made a mistake!\nYour score is decreased by " + (MISTAKE_SCORE_PENALTY * -1) + " points.\nYou have made " + db.getMistakes() + " mistakes. Your current score is " + score + " points.";
    }

    /**
     * Checks whether the player has won Score Attack mode.
     *
     * @param db reference to the game database
     * @return true if the player has reached the maximum number of days
     */
    private boolean hasWon(Database db) {
        return (db.getDay() >= db.getMaxDay());
    }

    /**
     * Checks if Score Attack mode has reached an ending condition.
     *
     * @param db reference to the game database
     * @return Optional containing an EndingResult if the player has won, otherwise empty
     */
    @Override
    public Optional<EndingResult> checkEnding(Database db) {
        if (hasWon(db)) {
            return Optional.of(
                    new EndingResult("score_attack_win", "Completed " + db.getDay() + " days. Score: " + score + ". Mistakes " + db.getMistakes() + ".")
            );
        }
        return Optional.empty();
    }
}
