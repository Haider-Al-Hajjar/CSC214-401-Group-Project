package com.example.fantasysortinggame.mainmenu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundEffectControllerTest {

    @Test
    void testSingletonInstance() {
        SoundEffectController a = SoundEffectController.getInstance();
        SoundEffectController b = SoundEffectController.getInstance();
        assertSame(a, b, "getInstance() should return the same singleton instance");
    }

    @Test
    void testPlayButtonClickDoesNotThrow() {
        SoundEffectController controller = SoundEffectController.getInstance();
        assertDoesNotThrow(controller::playButtonClick);
    }
}