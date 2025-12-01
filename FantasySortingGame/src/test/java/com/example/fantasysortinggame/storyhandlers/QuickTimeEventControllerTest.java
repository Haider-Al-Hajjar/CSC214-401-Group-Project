package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuickTimeEventControllerTest {

    private QuickTimeEventController controller;
    private List<Item> items;
    // Fix Item Creation
//    @BeforeEach
//    void setUp() {
//        controller = new QuickTimeEventController();
//
//        // Mock FXML-injected fields
//        controller.titleField = new TextField();
//        controller.descriptionField = new TextField();
//        controller.timeLeftField = new TextField();
//        controller.optionsContainer = new VBox();
//
//        items = new ArrayList<>();
//        Item item = new Item("OldItem", "desc", "Junk", "Type", 10);
//        items.add(item);
//    }
    // Fix QTE creation
//    @Test
//    void testQuickTimeEventCorrectOption() {
//        QuickTimeEvent qte = new QuickTimeEvent(
//                "QTE Title",
//                "QTE Description",
//                List.of("OptionA", "OptionB"),
//                "OptionA",
//                List.of("OldItem")
//        );
//        qte.setSuccessName("SuccessItem");
//        qte.setFailureName("FailureItem");
//
//        controller.startEvent(qte, items);
//
//        // simulate clicking correct option
//        Button btn = (Button) controller.optionsContainer.getChildren().get(0);
//        btn.fire();
//
//        assertTrue(qte.eventIsSolvedCorrectly());
//        assertTrue(qte.isSolvedInTime());
//        assertEquals("SuccessItem", items.get(0).getTitle());
//    }
    // Fix QTE creation
//    @Test
//    void testQuickTimeEventWrongOption() {
//        QuickTimeEvent qte = new QuickTimeEvent(
//                "QTE Title",
//                "QTE Description",
//                List.of("OptionA", "OptionB"),
//                "OptionA",
//                List.of("OldItem")
//        );
//        qte.setSuccessName("SuccessItem");
//        qte.setFailureName("FailureItem");
//
//        controller.startEvent(qte, items);
//
//        // simulate clicking wrong option
//        Button btn = (Button) controller.optionsContainer.getChildren().get(1);
//        btn.fire();
//
//        assertFalse(qte.eventIsSolvedCorrectly());
//        assertTrue(qte.isSolvedInTime());
//        assertEquals("FailureItem", items.get(0).getTitle());
//    }
}