package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EndlessModeTest {

    private EndlessMode endlessMode;
    private Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
        endlessMode = new EndlessMode();
        db.setMistakes(0);
    }

    @Test
    void testShouldDayStartDependsOnLoss() {
        assertTrue(endlessMode.shouldDayStart(db));
        db.setMistakes(3);
        assertFalse(endlessMode.shouldDayStart(db));
    }
    // need to fix item creation
//    @Test
//    void testOnCorrectSortDoesNothing() {
//        Item item = new Item("TestItem", 10);
//        endlessMode.onCorrectSort(db, item); // does nothing
//        assertEquals(0, db.getGold());
//    }

    @Test
    void testOnMistakeIncrementsMistakes() {
        assertEquals(0, db.getMistakes());
        endlessMode.onMistake(db);
        assertEquals(1, db.getMistakes());
        endlessMode.onMistake(db);
        assertEquals(2, db.getMistakes());
    }

    @Test
    void testHasLostBehavior() {
        assertFalse(endlessMode.hasLost(db));
        db.setMistakes(3);
        assertTrue(endlessMode.hasLost(db));
    }

    @Test
    void testGetMistakeMessage() {
        db.setMistakes(1);
        String msg = endlessMode.getMistakeMessage(db);
        assertTrue(msg.contains("2 mistakes remaining") || msg.contains("2 mistake remaining")); // dynamic text
    }

    @Test
    void testCheckEnding() {
        db.setDay(5);
        db.setMistakes(3); // triggers loss
        Optional<EndingResult> result = endlessMode.checkEnding(db);
        assertTrue(result.isPresent());
        assertEquals("endless_loss", result.get().type());
    }
}


