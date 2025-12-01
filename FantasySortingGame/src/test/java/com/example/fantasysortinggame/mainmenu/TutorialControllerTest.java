package com.example.fantasysortinggame.mainmenu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TutorialControllerTest {

    private TutorialController controller;

    @BeforeEach
    void setup() {
        controller = new TutorialController();

        // create dummy UI components
        controller.tutorialImageView = new ImageView();
        controller.tutorialTextBox = new Label();
        controller.nextButton = new Button();

        controller.initialize(); // load images and texts
    }

    @Test
    void testTutorialImagesLoaded() {
        assertNotNull(controller.tutorialImages);
        assertFalse(controller.tutorialImages.isEmpty(), "Tutorial images should be loaded");
        for (Image img : controller.tutorialImages) {
            assertNotNull(img, "Image should not be null");
        }
    }

    @Test
    void testTutorialTextsLoaded() {
        assertNotNull(controller.tutorialTexts);
        assertFalse(controller.tutorialTexts.isEmpty(), "Tutorial texts should be loaded");
    }

    @Test
    void testImageLooping() {
        int size = controller.tutorialImages.size();
        controller.currentIndex = size - 1; // last image
        controller.onNextButtonHandler();    // advance

        assertEquals(0, controller.currentIndex % size, "After last image, index should wrap to 0");
        assertEquals(controller.tutorialImages.get(0), controller.tutorialImageView.getImage());
        assertEquals(controller.tutorialTexts.get(0), controller.tutorialTextBox.getText());
    }

    @Test
    void testCurrentIndexIncrements() {
        int initial = controller.currentIndex;
        controller.onNextButtonHandler();
        assertEquals(initial + 1, controller.currentIndex);
    }

    @Test
    void testMultipleLoops() {
        int loops = controller.tutorialImages.size() * 3;
        for (int i = 0; i < loops; i++) {
            controller.onNextButtonHandler();
        }
        assertTrue(controller.currentIndex >= loops, "Current index should track multiple loops correctly");
    }
}