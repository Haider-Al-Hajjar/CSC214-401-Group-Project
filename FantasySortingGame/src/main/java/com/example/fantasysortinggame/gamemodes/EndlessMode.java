package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class EndlessMode implements GameMode {
    private final int MAX_MISTAKES = 3;

    /**
     * Determines whether the next day should start in Endless mode.
     * In Endless mode, the day only starts if the player has not lost.
     *
     * @param db reference to the game database
     * @return true if the day should start, false if the player has lost
     */
    @Override
    public boolean shouldDayStart(Database db) {
        return (!hasLost(db));
    }

    /**
     * Handles logic for when an item is sorted correctly.
     * Endless mode does not modify this behavior.
     *
     * @param db   reference to the game database
     * @param item the item that was sorted correctly
     */
    @Override
    public void onCorrectSort(Database db, Item item) {
        // Endless mode does not alter this logic
    }

    /**
     * Handles logic for when the player makes a mistake.
     * Increases the mistake counter in the database.
     *
     * @param db reference to the game database
     */
    @Override
    public void onMistake(Database db) {
        db.setMistakes(db.getMistakes() + 1);
    }

    /**
     * Determines if QuickTime events are allowed in Endless mode.
     *
     * @return true (QuickTime events are always allowed)
     */
    @Override
    public boolean allowQuickTimeEvents() {
        return true;
    }

    /**
     * Determines whether the game can end in Endless mode.
     *
     * @return true (game can end if conditions are met)
     */
    @Override
    public boolean canGameEnd() {
        return true;
    }

    /**
     * Logic executed at the end of a day in Endless mode.
     * Endless mode does not modify this behavior.
     *
     * @param db reference to the game database
     */
    @Override
    public void onDayEnd(Database db) {
        // Endless mode does not alter this logic
    }

    /**
     * Checks if the player has lost in Endless mode.
     * The player loses if mistakes reach the maximum allowed.
     *
     * @param db reference to the game database
     * @return true if the player has lost
     */
    @Override
    public boolean hasLost(Database db) {
        return db.getMistakes() >= MAX_MISTAKES;
    }

    /**
     * Returns a message indicating remaining mistakes allowed.
     *
     * @param db reference to the game database
     * @return message showing remaining mistakes
     */
    @Override
    public String getMistakeMessage(Database db) {
        String returnString = "You made a mistake!\n You have " + (MAX_MISTAKES - db.getMistakes()) + " ";
        returnString += MAX_MISTAKES - db.getMistakes() == 1 ? "mistake " : "mistakes ";
        returnString += "remaining.";
        return returnString;
    }

    /**
     * Checks for ending conditions in Endless mode.
     * Returns a loss result if the player has exceeded maximum mistakes.
     *
     * @param db reference to the game database
     * @return Optional containing ending result if lost, empty otherwise
     */
    @Override
    public Optional<EndingResult> checkEnding(Database db) {
        if (hasLost(db)) {
            return Optional.of(
                    new EndingResult("endless_loss", "Survived " + db.getDay() + " days.")
            );
        }
        return Optional.empty();

    }
}

