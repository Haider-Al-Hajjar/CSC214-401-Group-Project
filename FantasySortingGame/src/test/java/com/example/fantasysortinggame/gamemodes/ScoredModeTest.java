package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ScoredModeTest {

    private ScoredMode scoredMode;
    private Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
        scoredMode = new ScoredMode();
        db.setDay(1);
        db.setMistakes(0);
    }

    // need to fix item creation
//    @Test
//    void testScoreIncrementOnCorrectSort() {
//        scoredMode.onCorrectSort(db, new Item("Test", 10));
//        Optional<EndingResult> ending = scoredMode.checkEnding(db);
//        assertTrue(ending.isEmpty());
//    }

    @Test
    void testScorePenaltyOnMistake() {
        scoredMode.onMistake(db);
        String msg = scoredMode.getMistakeMessage(db);
        assertTrue(msg.contains("20 points"));
        assertEquals(1, db.getMistakes());
    }

    @Test
    void testHasLostAlwaysFalse() {
        assertFalse(scoredMode.hasLost(db));
        db.setMistakes(10);
        assertFalse(scoredMode.hasLost(db));
    }

    @Test
    void testDayStartDependsOnWin() {
        assertTrue(scoredMode.shouldDayStart(db));
        db.setDay(db.getMaxDay());
        assertFalse(scoredMode.shouldDayStart(db)); // reached max day → considered "won"
    }

    @Test
    void testCheckEndingWhenWon() {
        db.setDay(db.getMaxDay());
        Optional<EndingResult> ending = scoredMode.checkEnding(db);
        assertTrue(ending.isPresent());
        assertEquals("score_attack_win", ending.get().type());
    }
}
