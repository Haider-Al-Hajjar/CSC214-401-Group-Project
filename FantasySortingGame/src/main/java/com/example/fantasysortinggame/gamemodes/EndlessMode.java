package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class EndlessMode implements GameMode {
    private final int MAX_MISTAKES = 3;
    private int mistakes = 0;

    @Override
    public boolean shouldDayStart(Database db) {
        return (!hasLost(db));
    }

    @Override
    public void onCorrectSort(Database db, Item item) {
        // Endless mode does not alter this logic
    }

    @Override
    public void onMistake(Database db) {
        mistakes++;
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
        // Endless mode does not alter this logic
    }

    @Override
    public boolean hasLost(Database db) {
        return mistakes >= MAX_MISTAKES;
    }

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

