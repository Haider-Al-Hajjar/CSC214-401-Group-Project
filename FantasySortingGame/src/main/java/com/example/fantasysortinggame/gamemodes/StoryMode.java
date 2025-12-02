package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class StoryMode implements GameMode {
    /**
     * Determines whether a new day should start in Story Mode.
     *
     * @param db reference to the game database
     * @return true if the current day is less than or equal to the maximum day
     */
    @Override
    public boolean shouldDayStart(Database db) {
        return (db.getDay() <= db.getMaxDay());
    }

    /**
     * Logic executed when an item is sorted correctly.
     *
     * @param db   reference to the game database
     * @param item the item that was correctly sorted
     */
    @Override
    public void onCorrectSort(Database db, Item item) {
        // Story mode does not alter this logic
    }

    /**
     * Logic executed when a sorting mistake occurs.
     *
     * @param db reference to the game database
     */
    @Override
    public void onMistake(Database db) {
        // Story mode does not alter this logic
    }

    /**
     * Determines if QuickTime events are allowed in Story Mode.
     *
     * @return true if QuickTime events are enabled
     */
    @Override
    public boolean allowQuickTimeEvents() {
        return true;
    }

    /**
     * Determines whether the game can end in Story Mode.
     *
     * @return true if the game can end
     */
    @Override
    public boolean canGameEnd() {
        return true;
    }

    /**
     * Logic executed at the end of a day in Story Mode.
     *
     * @param db reference to the game database
     */
    @Override
    public void onDayEnd(Database db) {
        // Story mode does not alter this logic
    }

    /**
     * Checks whether the player has lost in Story Mode.
     *
     * @param db reference to the game database
     * @return false (player cannot lose in this mode)
     */
    @Override
    public boolean hasLost(Database db) {
        return false;
    }

    /**
     * Returns a message describing the player's mistake in Story Mode.
     *
     * @param db reference to the game database
     * @return null (Story Mode does not use mistake messages)
     */
    @Override
    public String getMistakeMessage(Database db) {
        return null;
    }

    /**
     * Checks if Story Mode has reached an ending condition.
     *
     * @param db reference to the game database
     * @return Optional containing an EndingResult if the mode has ended, otherwise empty
     */
    @Override
    public Optional<EndingResult> checkEnding(Database db) {
        if (db.getDay() < db.getMaxDay()) {
            return Optional.empty();
        }
        double percentSold = calcPercentSold(db);
        String type;

        if (percentSold > .70) type = "story_heartless";
        else if (percentSold < .40) type = "story_hoarder";
        else type = "story_healthy";

        return Optional.of(new EndingResult(type, "Sold " + (int) (percentSold * 100) + "% of items."));
    }

    /**
     * Calculates the percentage of items that were sold.
     *
     * @param db reference to the game database
     * @return fraction of sold items (0.0–1.0)
     */
    private double calcPercentSold(Database db) {
        var usedItems = db.getUsedItems();
        if (usedItems.isEmpty()) return 0;

        long sold = usedItems.stream().filter(Item::isSold).count();
        return (double) sold / usedItems.size();
    }

}
