package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class StoryMode implements GameMode {

    @Override
    public boolean shouldDayStart(Database db) {
        return (db.getDay() <= db.getMaxDay());
    }

    @Override
    public void onCorrectSort(Database db, Item item) {
        // Story mode does not alter this logic
    }

    @Override
    public void onMistake(Database db) {
        // Story mode does not alter this logic
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
        // Story mode does not alter this logic
    }

    @Override
    public boolean hasLost(Database db) {
        return false;
    }

    @Override
    public String getMistakeMessage(Database db) {
        return null;
    }


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

        return Optional.of(new EndingResult(type, "Sold " + (int)(percentSold * 100) + "% of items."));
    }
    private double calcPercentSold(Database db) {
        var usedItems = db.getUsedItems();
        if (usedItems.isEmpty()) return 0;

        long sold = usedItems.stream().filter(Item::isSold).count();
        return (double) sold / usedItems.size();
    }

}
