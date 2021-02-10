package com.intelligents.haunting;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class MusicPlayer {

    private Clip clip;
    private FloatControl controller;

    /**
     * This class takes a .wav file and allows music to be applied to your application. Ensure that when using this
     *
     * @param filepath the path from content root is used.
     *                 <p>
     *                 For short clips i.e. books falling, walking, laughter: make sure to use the appropriate method.
     *                 <p>
     *                 The controller field allows for volume adjustment and is best utilized when attached to a GUI slider.
     */


    MusicPlayer(String filepath) {

        {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filepath));
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                controller = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    void startMusic() {
        clip.start();
    }

    void pauseMusic() {
        clip.stop();
    }

    void quitMusic() {
        clip.close();
    }

    void playSoundEffect() {
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    void stopSoundEffect() {
        clip.setMicrosecondPosition(0);
        clip.stop();
    }

    float getVolume() {
        return controller.getValue();
    }

    void setVolume(float volume) {
        controller.setValue(volume);
    }
}
