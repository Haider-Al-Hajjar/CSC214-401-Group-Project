package com.example.fantasysortinggame.mainmenu;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Singleton controller for playing sound effects in the game.
 * <p>
 * Currently handles button click sounds.
 */
public class SoundEffectController {

    public static SoundEffectController instance;
    private Clip buttonClickClip;

    /**
     * Initializes the SoundEffectController and loads the button click sound.
     */
    public SoundEffectController() {
        try {
            URL soundURL = getClass().getResource("/com/example/fantasysortinggame/soundfiles/Button Click Sound.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            buttonClickClip = AudioSystem.getClip();
            buttonClickClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of the SoundEffectController.
     *
     * @return Singleton instance.
     */
    public static SoundEffectController getInstance() {
        if (instance == null) {
            instance = new SoundEffectController();
        }
        return instance;
    }

    /**
     * Plays the button click sound.
     * <p>
     * Stops and rewinds if currently playing.
     */
    public void playButtonClick() {
        if (buttonClickClip == null) return;
        if (buttonClickClip.isRunning()) buttonClickClip.stop();
        buttonClickClip.setFramePosition(0);
        buttonClickClip.start();
    }
}
