package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public interface GameMode {
    Optional<EndingResult> checkEnding(Database db);

    // Called when a new day begins
    boolean shouldDayStart(Database db);

    // Called when an item is correctly sorted
    void onCorrectSort(Database db, Item item);

    // Called when an item is incorrectly sorted
    void onMistake(Database db);

    // Called when a QuickTimeEvent is completed or skipped
    boolean allowQuickTimeEvents();

    // Whether the game can end
    boolean canGameEnd();

    // Called at end of day; used by TimeTrial and ScoreAttack
    void onDayEnd(Database db);

    // Whether the player has lost
    boolean hasLost(Database db);
}

