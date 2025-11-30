package com.example.fantasysortinggame.mainmenu;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundEffectController {

    private static SoundEffectController instance;
    private Clip buttonClickClip;

    private SoundEffectController() {
        try {
            URL soundURL = getClass().getResource("/com/example/fantasysortinggame/soundfiles/button_click.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            buttonClickClip = AudioSystem.getClip();
            buttonClickClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static SoundEffectController getInstance() {
        if (instance == null) {
            instance = new SoundEffectController();
        }
        return instance;
    }

    public void playButtonClick() {
        if (buttonClickClip == null) return;
        if (buttonClickClip.isRunning()) buttonClickClip.stop();
        buttonClickClip.setFramePosition(0);
        buttonClickClip.start();
    }
}
