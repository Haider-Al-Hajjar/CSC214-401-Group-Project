package com.example.fantasysortinggame.gamephasemanager;
import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SalePhaseControllerTest {

    private Database db;
    private SalePhaseController controller;
    // Fix item creation
//    @BeforeEach
//    void setup() {
//        db = new Database();
//        controller = new SalePhaseController();
//
//        // Populate database with test items
//        ArrayList<Item> items = new ArrayList<>();
//        Item i1 = new Item("item1", "Unsorted", "desc", "type1", "/img.png");
//        Item i2 = new Item("item2", "Junk", "desc", "type2", "/img.png");
//        items.add(i1);
//        items.add(i2);
//
//        db.setUsedItems(items);
//        db.setDay(1);
//
//        controller.setDependencies(db, null, () -> {}); // null Stage, no UI
//    }

    @Test
    void testFilterNodeStructure() {
        SalePhaseController.FilterNode root = controller.getItemSortCategoriesByDay(1);
        assertEquals(3, root.children.size()); // All, Junk, Treasure
    }

    @Test
    void testFilterMatches() {
        SalePhaseController.FilterNode root = controller.getItemSortCategoriesByDay(1);
        SalePhaseController.FilterNode junkNode = root.children.stream()
                .filter(n -> n.name.equals("Junk")).findFirst().orElse(null);
        assertNotNull(junkNode);
        assertTrue(junkNode.matches("Junk"));
        assertFalse(junkNode.matches("Treasure"));
    }

    @Test
    void testSellItemLogic() {
        Item item = db.getUsedItems().get(0);
        assertFalse(item.isSold());

        // Directly call the internal method via reflection if needed or make it package-private
        controller.sellItem(item);

        assertTrue(item.isSold());
        assertEquals(db.getGold(), Math.max(item.getValue(), controller.MIN_ITEM_VALUE));
    }
    // Fix item creation
//    @Test
//    void testCorrectSortBonus() {
//        Item item = new Item("i", "typeA", "desc", "typeA", "/img.png"); // type matches sort
//        db.setUsedItems(new ArrayList<>(Arrays.asList(item)));
//
//        double bonus = controller.calculateCorrectSortBonus(item);
//        assertEquals(Math.max(item.getValue(), controller.MIN_ITEM_VALUE) * 0.5, bonus);
//    }
}