package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class ZenMode implements GameMode {
    /**
     * Determines whether a new day should start in Zen Mode.
     *
     * @param db reference to the game database
     * @return always true
     */
    @Override
    public boolean shouldDayStart(Database db) {
        return true;
    }

    /**
     * Logic executed when an item is sorted correctly in Zen Mode.
     *
     * @param db   reference to the game database
     * @param item the item that was correctly sorted
     */
    @Override
    public void onCorrectSort(Database db, Item item) {
        // Zen mode does not alter this logic
    }

    /**
     * Determines if QuickTime events are allowed in Zen Mode.
     *
     * @return false
     */
    @Override
    public void onMistake(Database db) {
        // Zen mode does not alter this logic
    }

    /**
     * Determines whether the game can end in Zen Mode.
     *
     * @return false
     */
    @Override
    public boolean allowQuickTimeEvents() {
        return false;
    }

    /**
     * Determines whether the game can end in Zen Mode.
     *
     * @return false
     */
    @Override
    public boolean canGameEnd() {
        return false; // You cannot lose in Zen mode
    }

    /**
     * Logic executed at the end of a day in Zen Mode.
     *
     * @param db reference to the game database
     */
    @Override
    public void onDayEnd(Database db) {
        // Zen mode does not alter this logic
    }

    /**
     * Checks whether the player has lost in Zen Mode.
     *
     * @param db reference to the game database
     * @return always false
     */
    @Override
    public boolean hasLost(Database db) {
        return false;
    }

    /**
     * Returns a message describing a mistake in Zen Mode.
     *
     * @param db reference to the game database
     * @return always null
     */
    @Override
    public String getMistakeMessage(Database db) {
        return null;
    }

    /**
     * Checks if Zen Mode has reached an ending condition.
     *
     * @param db reference to the game database
     * @return always empty
     */
    @Override
    public Optional<EndingResult> checkEnding(Database db) {
        return Optional.empty();
    }
}
