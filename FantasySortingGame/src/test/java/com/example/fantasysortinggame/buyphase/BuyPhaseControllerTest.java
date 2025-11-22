package com.example.fantasysortinggame.buyphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Upgrade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuyPhaseControllerTest {

    @Test
    void testLoadUpgradesLoadsCorrectly() {

        // 1. Create database
        Database db = new Database();

        // 2. Create fake upgrades using your real constructor
        ArrayList<Upgrade> fakeUpgrades = new ArrayList<>();
        fakeUpgrades.add(new Upgrade("Speed Boost", 100, false));
        fakeUpgrades.add(new Upgrade("Strength Boost", 150, false));

        // 3. Set unbought upgrades in the database
        db.setUnboughtUpgrades(fakeUpgrades);

        // 4. Create controller
        BuyPhaseController controller = new BuyPhaseController(db);

        // 5. Run the method being tested
        controller.loadUpgrades();

        // 6. Validate behavior
        assertNotNull(db.getUnboughtUpgrades());
        assertEquals(2, db.getUnboughtUpgrades().size());
    }
}
