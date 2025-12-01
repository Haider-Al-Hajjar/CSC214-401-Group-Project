package com.example.fantasysortinggame.gamemodes;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StoryModeTest {

    private Database db;
    private StoryMode storyMode;

    @BeforeEach
    void setUp() {
        db = new Database();
        db.setDay(1);
        db.setAllItems(new ArrayList<>()); // empty items structure
        db.setUsedItems(new ArrayList<>());
        storyMode = new StoryMode();
    }

    @Test
    void shouldDayStart_WithinMaxDay_ReturnsTrue() {
        db.setDay(3);
        assertTrue(storyMode.shouldDayStart(db));
    }

    @Test
    void shouldDayStart_ExceedsMaxDay_ReturnsFalse() {
        db.setDay(db.getMaxDay() + 1);
        assertFalse(storyMode.shouldDayStart(db));
    }

    @Test
    void hasLost_AlwaysFalse() {
        assertFalse(storyMode.hasLost(db));
    }

    @Test
    void getMistakeMessage_AlwaysNull() {
        assertNull(storyMode.getMistakeMessage(db));
    }

    @Test
    void checkEnding_BeforeMaxDay_ReturnsEmpty() {
        db.setDay(3);
        Optional<EndingResult> result = storyMode.checkEnding(db);
        assertTrue(result.isEmpty());
    }

    @Test
    void checkEnding_AfterMaxDay_CalculatesCorrectType() {
        db.setDay(db.getMaxDay() + 1);

        // 0% sold → hoarder
        db.setUsedItems(new ArrayList<>());
        Optional<EndingResult> result = storyMode.checkEnding(db);
        assertTrue(result.isPresent());
        assertEquals("story_hoarder", result.get().type());

        // 100% sold → heartless
        Item item1 = new Item();
        item1.setSold(true);
        db.setUsedItems(new ArrayList<>() {{ add(item1); }});
        result = storyMode.checkEnding(db);
        assertTrue(result.isPresent());
        assertEquals("story_heartless", result.get().type());
    }
}