package com.example.fantasysortinggame.gamephasemanager;
import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Upgrade;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuyPhaseControllerTest {

    private static Database db;

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {}); // Initialize JavaFX once
    }

    @BeforeEach
    void setup() {
        db = new Database();
        db.setAllUpgrades(new ArrayList<>());
        db.setGold(100);
    }

    @Test
    void testLoadUpgradesDisplaysOnlyUnbought() {
        Upgrade u1 = new Upgrade("Upgrade1", 50, false, "Ability1");
        Upgrade u2 = new Upgrade("Upgrade2", 20, true, "Ability2");
        db.getAllUpgrades().add(u1);
        db.getAllUpgrades().add(u2);

        BuyPhaseController controller = new BuyPhaseController(db);
        controller.shopContainer = new VBox();

        controller.loadUpgrades();

        assertEquals(1, controller.shopContainer.getChildren().size());
    }

    @Test
    void testBuyingUpgradeReducesGoldAndMarksBought() {
        Upgrade u = new Upgrade("Upgrade1", 50, false, "Ability1");
        db.getAllUpgrades().add(u);

        BuyPhaseController controller = new BuyPhaseController(db);
        controller.shopContainer = new VBox();
        controller.totalGoldLabel = new Label();
        controller.loadUpgrades();

        controller.onBuyUpgradeClickHandler(u);

        assertEquals(50, db.getGold());
        assertTrue(u.isBought());
    }

    @Test
    void testCannotBuyIfNotEnoughGold() {
        Upgrade u = new Upgrade("Expensive", 200, false, "Ability");
        db.getAllUpgrades().add(u);

        BuyPhaseController controller = new BuyPhaseController(db);
        controller.shopContainer = new VBox();
        controller.totalGoldLabel = new Label();
        controller.loadUpgrades();

        // Button would be disabled in UI; simulate logic check
        assertTrue(db.getGold() < u.getCost());
        controller.onBuyUpgradeClickHandler(u);

        assertFalse(u.isBought());
        assertEquals(100, db.getGold());
    }

    @Test
    void testProceedButtonTriggersOnPhaseComplete() {
        BuyPhaseController controller = new BuyPhaseController(db);
        controller.proceedButton = new Button();
        controller.shopContainer = new VBox();

        final boolean[] called = {false};
        controller.setOnPhaseComplete(() -> called[0] = true, new Stage());

        // simulate button click
        Platform.runLater(() -> controller.proceedButton.fire());

        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        assertTrue(called[0]);
    }
}