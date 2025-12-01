package com.example.fantasysortinggame.mainmenu;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MistakePopupControllerTest {

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {}); // Initialize JavaFX
    }

    @Test
    void testSetMessageUpdatesLabel() throws Exception {
        MistakePopupController controller = new MistakePopupController();
        Label label = new Label();

        // Inject label
        var field = MistakePopupController.class.getDeclaredField("messageLabel");
        field.setAccessible(true);
        field.set(controller, label);

        controller.setMessage("Test Message");
        assertEquals("Test Message", label.getText());
    }

    @Test
    void testShowPopupDoesNotThrow() {
        assertDoesNotThrow(() -> MistakePopupController.showPopup("Popup Test"));
    }
}