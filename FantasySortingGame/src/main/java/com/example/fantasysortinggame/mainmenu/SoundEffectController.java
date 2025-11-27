package com.example.fantasysortinggame.mainmenu;

import java.io.File;

public class SoundEffectController {
    /*
        Once we finish the rest of the classes, we should have onButtonClickedHandlers call the sound effect controller to play a sound.
     */
    protected File buttonClickSound = new File("com/example/fantasysortinggame/soundfiles/PLACEHOLDERButtonClickSoundFile");

    public static SoundEffectController getInstance() {
        return null;
    }

    protected void playSound(File soundFile) {

    }

    public void playPhaseEnd() {

    }

    public void playSellSuccess() {
    }

    public void playError() {
    }

    public void playButtonClick() {
    }

    public void playPhaseStart() {
    }
}
