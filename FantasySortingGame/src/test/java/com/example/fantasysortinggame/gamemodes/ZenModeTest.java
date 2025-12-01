package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZenModeTest {

    private ZenMode zenMode;
    private Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
        zenMode = new ZenMode();
    }

    @Test
    void testShouldDayStartAlwaysTrue() {
        assertTrue(zenMode.shouldDayStart(db));
    }

    @Test
    void testCannotLose() {
        assertFalse(zenMode.hasLost(db));
        assertFalse(zenMode.canGameEnd());
    }

    // need to fix item creation
//    @Test
//    void testOnCorrectSortDoesNothing() {
//        Item item = new Item("TestItem", 10);
//        zenMode.onCorrectSort(db, item); // should not throw
//        assertEquals(0, db.getGold()); // ZenMode does not alter gold directly
//    }

    @Test
    void testOnMistakeDoesNothing() {
        zenMode.onMistake(db); // should not throw
        assertEquals(0, db.getMistakes());
    }

    @Test
    void testCheckEndingEmpty() {
        assertTrue(zenMode.checkEnding(db).isEmpty());
    }

    @Test
    void testGetMistakeMessageNull() {
        assertNull(zenMode.getMistakeMessage(db));
    }
}
