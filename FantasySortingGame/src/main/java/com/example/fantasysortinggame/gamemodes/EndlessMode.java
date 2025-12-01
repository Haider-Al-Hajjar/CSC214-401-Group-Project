package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class EndlessMode implements GameMode {
    private final int MAX_MISTAKES = 3;

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
        db.setMistakes(db.getMistakes()+1);
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
        return db.getMistakes() >= MAX_MISTAKES;
    }

    @Override
    public String getMistakeMessage(Database db) {
        String returnString = "You made a mistake!\n You have " + (MAX_MISTAKES - db.getMistakes()) + " ";
        returnString += MAX_MISTAKES - db.getMistakes() == 1 ? "mistake " : "mistakes ";
        returnString += "remaining.";
        return returnString;
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

