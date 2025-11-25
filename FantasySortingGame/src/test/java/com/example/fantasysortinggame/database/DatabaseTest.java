//package com.example.fantasysortinggame.database;
//
//import org.junit.jupiter.api.*;
//import java.io.File;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DatabaseTest {
//
//    private static final String TEST_FILE = "testSave.json";
//
//    @BeforeEach
//    void cleanupFile() {
//        File f = new File(TEST_FILE);
//        if (f.exists()) f.delete();
//    }
//
//    @Test
//    void testTutorialModeCreatesNewSave() {
//        Database db = new Database();
//        db.loadFromFile(TEST_FILE, null);
//
//        assertEquals(0, db.getDay());
//        assertNotNull(db.getAllItems());
//        assertNotNull(db.getAllUpgrades());
//        assertNotNull(db.getAllNpcs());
//        assertNotNull(db.getAllDialogues());
//        assertNotNull(db.getUsedItems());
//    }
//
//    @Test
//    void testSaveFileIsCreated() {
//        Database db = new Database();
//        db.loadFromFile(TEST_FILE, null);
//
//        File f = new File(TEST_FILE);
//        assertTrue(f.exists());
//    }
//
//    @Test
//    void testSaveThenLoadRestoresValues() {
//        Database db1 = new Database();
//        db1.loadFromFile(TEST_FILE, null);
//        db1.setDay(5);
//        db1.setSeed(12345);
//        db1.saveToFile();
//
//        Database db2 = new Database();
//        db2.loadFromFile(TEST_FILE, null);
//
//        assertEquals(5, db2.getDay());
//        assertEquals(12345, db2.getSeed());
//    }
//
//    @Test
//    void testCollectionsNeverNullAfterLoad() {
//        Database db = new Database();
//        db.loadFromFile(TEST_FILE, null);
//
//        assertNotNull(db.getAllItems());
//        assertNotNull(db.getUsedItems());
//        assertNotNull(db.getAllUpgrades());
//        assertNotNull(db.getUnboughtUpgrades());
//        assertNotNull(db.getBoughtUpgrades());
//        assertNotNull(db.getAllEvents());
//        assertNotNull(db.getAllNpcs());
//        assertNotNull(db.getAllDialogues());
//    }
//
//    @Test
//    void testLoadNonExistingFileDoesNotCrash() {
//        Database db = new Database();
//        assertDoesNotThrow(() -> db.loadFromFile("nopeFile.json", null));
//    }
//
//    @Test
//    void testItemsByDayReturnsAllItems() {
//        Database db = new Database();
//        db.loadFromFile(TEST_FILE, null);
//
//        assertEquals(db.getAllItems(), db.getItemsByDayAndSeed(1, 999));
//    }
//}
