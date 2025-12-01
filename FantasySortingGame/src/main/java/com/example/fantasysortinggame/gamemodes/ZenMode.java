package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.Optional;

public class ZenMode implements GameMode {

    @Override
    public boolean shouldDayStart(Database db) {
        return true;
    }

    @Override
    public void onCorrectSort(Database db, Item item) {
        // Zen mode does not alter this logic
    }

    @Override
    public void onMistake(Database db) {
        // Zen mode does not alter this logic
    }

    @Override
    public boolean allowQuickTimeEvents() {
        return false;
    }

    @Override
    public boolean canGameEnd() {
        return false; // You cannot lose in Zen mode
    }

    @Override
    public void onDayEnd(Database db) {
        // Zen mode does not alter this logic
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
        return Optional.empty();
    }
}
