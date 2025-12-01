package com.example.fantasysortinggame.storyhandlers;
import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Dialogue;
import com.example.fantasysortinggame.datatypes.DialogueEntry;
import com.example.fantasysortinggame.datatypes.Npc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class DialogueBoxControllerTest {

    private Database db;
    private DialogueBoxController controller;

    @BeforeEach
    void setUp() {
        db = new Database();
        controller = new DialogueBoxController(db);

        // Simulate FXML injection for testing
        controller.nameLabel = new javafx.scene.control.Label();
        controller.dialogueLabel = new javafx.scene.control.Label();
        controller.imageView = new javafx.scene.image.ImageView();
        controller.continueButton = new javafx.scene.control.Button();
        controller.rootPane = new javafx.scene.layout.AnchorPane();
    }
    // Fix NPC creation
//    @Test
//    void testDialogueProgressionAndCallback() {
//        Npc speaker1 = new Npc("Alice", "pfp1.png");
//        Npc speaker2 = new Npc("Bob", "pfp2.png");
//
//        DialogueEntry entry1 = new DialogueEntry(speaker1, "Hello there!");
//        DialogueEntry entry2 = new DialogueEntry(speaker2, "Hi!");
//
//        Dialogue dialogue = new Dialogue(List.of(entry1, entry2));
//
//        boolean[] callbackCalled = {false};
//        controller.setOnDialogueEnd(() -> callbackCalled[0] = true);
//
//        controller.runDialogue(dialogue);
//
//        // First entry displayed
//        assertEquals("Alice", controller.nameLabel.getText());
//        assertEquals("Hello there!", controller.dialogueLabel.getText());
//
//        // Simulate pressing continue button
//        controller.continueButton.fire();
//        assertEquals("Bob", controller.nameLabel.getText());
//        assertEquals("Hi!", controller.dialogueLabel.getText());
//
//        // Simulate pressing continue to end dialogue
//        controller.continueButton.fire();
//        assertFalse(controller.rootPane.isVisible() == false); // hidden
//        assertTrue(dialogue.hasHappened());
//        assertTrue(callbackCalled[0]);
//    }
}
