package com.example.fantasysortinggame.sortphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.storyhandlers.DialogueBoxController;
import com.example.fantasysortinggame.storyhandlers.QuickTimeEventController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SortPhaseControllerTest {

    @Test
    void testSortingAnItemReducesUnsortedCount() {

        QuickTimeEventController qte = null;
        DialogueBoxController dialogue = null;
        Database db = new Database();
        ArrayList<Item> items = new ArrayList<>();
        Item i1 = new Item();
        i1.setItemSort("unsorted");
        Item i2 = new Item();
        i2.setItemSort("unsorted");
        items.add(i1);
        items.add(i2);
        db.setAllItems(items);
        SortPhaseController controller = new SortPhaseController(db, qte, dialogue);
        controller.loadItems("unsorted", "default", 0, 0);
        controller.onChangeItemSortClickHandler(i1, "metal");
        int remainingUnsorted = controller.filterDailyItems().size();
        assertEquals(1, remainingUnsorted);
    }
}
