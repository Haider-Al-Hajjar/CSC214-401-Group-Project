package com.example.fantasysortinggame.gamemodes;
import com.example.fantasysortinggame.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TimedModeTest {

    private TimedMode timedMode;
    private Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
        db.setDay(1);
        timedMode = new TimedMode();
        timedMode.setRemainingSeconds(50); // start with 50 seconds
    }

    // need to fix item creation
//    @Test
//    void testCorrectSortAddsTime() {
//        int before = timedMode.getRemainingSeconds();
//        timedMode.onCorrectSort(db, new Item("Test", 10));
//        assertEquals(before + 5, timedMode.getRemainingSeconds()); // +CORRECT_SORT_TIME_BONUS
//    }

    @Test
    void testMistakeSubtractsTime() {
        int before = timedMode.getRemainingSeconds();
        timedMode.onMistake(db);
        assertEquals(before - 5, timedMode.getRemainingSeconds()); // -MISTAKE_TIME_PENALTY
    }

    @Test
    void testHasLost() {
        timedMode.setRemainingSeconds(0);
        assertTrue(timedMode.hasLost(db));
        timedMode.setRemainingSeconds(1);
        assertFalse(timedMode.hasLost(db));
    }

    @Test
    void testShouldDayStartDependsOnWinOrLoss() {
        timedMode.setRemainingSeconds(0);
        assertFalse(timedMode.shouldDayStart(db));
        timedMode.setRemainingSeconds(10);
        db.setDay(db.getMaxDay());
        assertFalse(timedMode.shouldDayStart(db));
    }

    @Test
    void testCheckEndingLoss() {
        timedMode.setRemainingSeconds(0);
        Optional<EndingResult> ending = timedMode.checkEnding(db);
        assertTrue(ending.isPresent());
        assertEquals("timetrial_loss", ending.get().type());
    }

    @Test
    void testCheckEndingWin() {
        timedMode.setRemainingSeconds(10);
        db.setDay(db.getMaxDay());
        Optional<EndingResult> ending = timedMode.checkEnding(db);
        assertTrue(ending.isPresent());
        assertEquals("timetrial_win", ending.get().type());
    }
}