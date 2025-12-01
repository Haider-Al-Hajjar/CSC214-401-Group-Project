package com.example.fantasysortinggame.gamephasemanager;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SortPhaseControllerTest {

    private static Database db;
    private SortPhaseController controller;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown); // initialize JavaFX
        latch.await();
        db = new Database();
    }
    // make items create correctly
//    @BeforeEach
//    void setup() {
//        controller = new SortPhaseController();
//
//        // Populate database with mock items
//        ArrayList<Item> items = new ArrayList<>();
//        Item i1 = new Item();
//        i1.setName("item1");
//        i1.setItemSort("Unsorted");
//        i1.setDescription("desc1");
//        i1.setItemTypeValue("type1");
//
//        Item i2 = new Item();
//        i2.setName("item2");
//        i2.setItemSort("Junk");
//        i2.setDescription("desc2");
//        i2.setItemTypeValue("type2");
//
//        items.add(i1);
//        items.add(i2);
//
//        db.setItems(items);
//        db.setDay(1);
//
//        controller.setDependencies(db, null, () -> {});
//    }

    @Test
    void testRootFilterNodeForDay1() {
        SortPhaseController.FilterNode root = controller.getItemSortCategoriesByDay(1);
        assertEquals(4, root.children.size()); // All, Unsorted, Junk, Treasure
    }

    @Test
    void testItemSortMatches() {
        SortPhaseController.FilterNode root = controller.getItemSortCategoriesByDay(1);
        SortPhaseController.FilterNode junkNode = root.children.stream()
                .filter(n -> n.name.equals("Junk")).findFirst().orElse(null);
        assertNotNull(junkNode);
        assertTrue(junkNode.matches("Junk"));
        assertFalse(junkNode.matches("Treasure"));
    }

    @Test
    void testHandleSortSelectionUpdatesItem() {
        Item item = db.getItems().get(0); // Unsorted
        Button dummyButton = new Button();

        controller.handleSortSelection(item, dummyButton, "Junk");

        assertEquals("Junk", item.getItemSort());
        assertEquals("Junk", dummyButton.getText());
    }
}
