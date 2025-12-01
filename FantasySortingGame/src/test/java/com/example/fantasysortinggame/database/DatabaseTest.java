package com.example.fantasysortinggame.database;
import com.example.fantasysortinggame.datatypes.Upgrade;
import com.example.fantasysortinggame.gamemodes.GameModeNames;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private static final String TEST_FILE = "testSave.json";
    private static final File SAVE_DIR = new File("savedfiles");

    @BeforeEach
    void cleanupFile() {
        File f = new File(SAVE_DIR, TEST_FILE);
        if (f.exists()) f.delete();
    }

    @Test
    void testStoryModeCreatesNewSave() {
        Database db = new Database();
        db.loadFromFile(TEST_FILE, GameModeNames.Story);

        assertEquals(0, db.getDay());
        assertNotNull(db.getAllItems());
        assertNotNull(db.getAllUpgrades());
        assertNotNull(db.getAllNpcs());
        assertNotNull(db.getAllDialogues());
        assertNotNull(db.getUsedItems());
    }

    @Test
    void testSaveFileIsCreated() {
        Database db = new Database();
        db.loadFromFile(TEST_FILE, GameModeNames.Story);

        File f = new File(SAVE_DIR, TEST_FILE);
        assertTrue(f.exists());
    }

    @Test
    void testSaveThenLoadRestoresValues() {
        Database db1 = new Database();
        db1.loadFromFile(TEST_FILE, GameModeNames.Story);
        db1.setDay(3);
        db1.setSeed(12345);
        db1.setGold(50.0);
        db1.saveToFile();

        Database db2 = new Database();
        db2.loadFromFile(TEST_FILE, GameModeNames.Story);

        assertEquals(3, db2.getDay());
        assertEquals(12345, db2.getSeed());
        assertEquals(50.0, db2.getGold());
    }

    @Test
    void testLoadNonExistingFileDoesNotCrash() {
        Database db = new Database();
        assertDoesNotThrow(() -> db.loadFromFile("nopeFile.json", GameModeNames.Story));
    }

    @Test
    void testItemsByDayAreSubsetOfAllItems() {
        Database db = new Database();
        db.loadFromFile(TEST_FILE, GameModeNames.Story);
        db.setDay(1);

        List<?> items = db.getItems();
        List<?> dayItems = db.getAllItems().get(db.getDayInBound() - 1);

        assertTrue(dayItems.containsAll(items), "Returned items should be subset of allItems for the day");
    }

    @Test
    void testUpgradeBoughtCheck() {
        Database db = new Database();
        db.loadFromFile(TEST_FILE, GameModeNames.Story);

        // Add a dummy upgrade
        var upgrade = new Upgrade("TestUpgrade", 10, false, "Does nothing");
        db.getAllUpgrades().add(upgrade);

        assertFalse(db.upgradeIsBought("TestUpgrade"));
        upgrade.setBought(true);
        assertTrue(db.upgradeIsBought("TestUpgrade"));
    }

    @AfterEach
    void cleanupAfter() {
        File f = new File(SAVE_DIR, TEST_FILE);
        if (f.exists()) f.delete();
    }
}