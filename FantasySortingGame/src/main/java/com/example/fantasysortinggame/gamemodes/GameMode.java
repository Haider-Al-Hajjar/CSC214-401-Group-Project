package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public interface GameMode {
    /**
     * Checks if the game has reached an ending condition.
     *
     * @param db reference to the game database
     * @return Optional containing an EndingResult if the game has ended, otherwise empty
     */
    Optional<EndingResult> checkEnding(Database db);

    /**
     * Determines whether a new day should start in this game mode.
     *
     * @param db reference to the game database
     * @return true if the day should start, false otherwise
     */
    boolean shouldDayStart(Database db);

    /**
     * Handles logic when a player correctly sorts an item.
     *
     * @param db   reference to the game database
     * @param item the item that was sorted correctly
     */
    void onCorrectSort(Database db, Item item);

    /**
     * Handles logic when a player makes a sorting mistake.
     *
     * @param db reference to the game database
     */
    void onMistake(Database db);

    /**
     * Determines if QuickTime events are allowed in this game mode.
     *
     * @return true if QuickTime events are enabled
     */
    boolean allowQuickTimeEvents();

    /**
     * Determines whether the game can end in this mode.
     *
     * @return true if the game can end
     */
    boolean canGameEnd();

    /**
     * Logic executed at the end of a day in this game mode.
     *
     * @param db reference to the game database
     */
    void onDayEnd(Database db);

    /**
     * Checks whether the player has lost in this game mode.
     *
     * @param db reference to the game database
     * @return true if the player has lost
     */
    boolean hasLost(Database db);

    /**
     * Returns a message describing the player's remaining mistakes or penalty status.
     *
     * @param db reference to the game database
     * @return mistake message string
     */
    String getMistakeMessage(Database db);
}

