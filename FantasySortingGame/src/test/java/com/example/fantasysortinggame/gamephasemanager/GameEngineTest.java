package com.example.fantasysortinggame.gamephasemanager;
import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.gamemodes.GameModeNames;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private Database db;
    private SoundEffectController soundController;

    @BeforeEach
    void setup() {
        db = new Database();
        db.setGold(0);
        db.setDay(1);
        db.setItems(new ArrayList<>()); // no items
        soundController = new SoundEffectController();
        GameEngine.initialize(db, null, soundController);
        GameEngine.setGameMode(GameModeNames.Story);
    }
    // Fix item creation
//    @Test
//    void testOnCorrectSortIncreasesGold() {
//        Item item = new Item("item1", "Unsorted", "desc", "Junk", "/img.png");
//        double initialGold = db.getGold();
//        GameEngine.onCorrectSort(item);
//        assertTrue(db.getGold() > initialGold, "Gold should increase after correct sort");
//    }

    @Test
    void testOnIncorrectSortDoesNotCrash() {
        db.setGold(100);
        // just run it; no exceptions and gold remains >= 0
        GameEngine.onIncorrectSort();
        assertTrue(db.getGold() >= 0);
    }

    @Test
    void testGameModeIsSet() {
        assertNotNull(GameEngine.getGameMode(), "Game mode should be initialized");
    }
}