package com.example.fantasysortinggame.mainmenu;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndingCardControllerTest {

    private EndingCardController controller;

    @BeforeEach
    void setup() {
        controller = new EndingCardController();

        // Manually create the UI components since we are not loading FXML
        controller.endingTitle = new Label();
        controller.endingDescription = new Label();
        controller.closeButton = new Button();
    }

    @Test
    void testSetEndingUpdatesLabels() {
        controller.setEnding("Victory", "You completed the game!");
        assertEquals("Victory", controller.endingTitle.getText());
        assertEquals("You completed the game!", controller.endingDescription.getText());
    }

    @Test
    void testInitializeSetsCloseButtonAction() {
        // We can't actually close a Stage in a headless test, but we can check the action is not null
        controller.initialize();
        assertNotNull(controller.closeButton.getOnAction(), "Close button should have an action set");
    }
}