package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class ScoredMode implements GameMode {
    private int score = 0;
    private final int CORRECT_SORT_SCORE_BONUS = 10;
    private final int MISTAKE_SCORE_PENALTY = -20;

    @Override
    public boolean shouldDayStart(Database db) {
        // hasWon checks for MAX_DAY (also hasLost is always false)
        return (!hasLost(db) && !hasWon(db));
    }


    @Override
    public void onCorrectSort(Database db, Item item) {
        score += CORRECT_SORT_SCORE_BONUS;
    }

    @Override
    public void onMistake(Database db) {
        score += MISTAKE_SCORE_PENALTY;
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
        // Score attack mode does not alter this logic
    }

    @Override
    public boolean hasLost(Database db) {
        // You cannot lose in Score attack mode.
        return false;
    }

    @Override
    public String getMistakeMessage(Database db) {
        return "You made a mistake!\nYour score is decreased by " + (MISTAKE_SCORE_PENALTY*-1) + " points.\nYou have made " + db.getMistakes() + " mistakes. Your current score is " + score + " points.";
    }

    private boolean hasWon(Database db) {
        return (db.getDay() >= db.getMaxDay());
    }

    @Override
    public Optional<EndingResult> checkEnding(Database db) {
        if (hasWon(db)) {
            return Optional.of(
                    new EndingResult("score_attack_win","Completed " + db.getDay() + " days. Score: " + score + ". Mistakes " + db.getMistakes() +".")
            );
        }
        return Optional.empty();
    }
}
