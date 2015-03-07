package de.stetro.learnodore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

public class Application extends MouseAdapter implements KeyListener {

    private ApplicationState state = ApplicationState.STOP;
    private final MainGui gui;
    private static final int DEFAULT_WORK_TIME = 25;
    private static final int DEFAULT_PAUSE_TIME = 10;
    private TimeThread thread;
    private int time = 0;
    private int count = 0;

    public static void main(String[] args) {
        Application application = new Application();
        application.start();
    }

    public Application() {
        gui = new MainGui();
        gui.setTitle("Learnodore Pomodoretechnik");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (state) {
            case STOP:
                state = ApplicationState.PAUSE;
                switchState();
                break;
            case PAUSE:
                state = ApplicationState.STOP;
                time = 0;
                break;
            case WORK:
                state = ApplicationState.STOP;
                time = 0;
                break;
        }
        gui.updateGui(state, time, count);
    }

    private void start() {
        gui.setVisible(true);
        gui.registerClickListener(this);
        gui.addKeyListener(this);
        time = DEFAULT_WORK_TIME * 60;
        gui.updateGui(state, time, count);
        this.thread = new TimeThread(this);
        (thread).start();
    }

    void onSecond() {
        switch (state) {
            case PAUSE:
                if (time > 0) {
                    time--;
                    gui.updateGui(state, time, count);
                } else {
                    switchState();
                    playSound("work.wav");
                    count++;
                }
                break;
            case WORK:
                if (time > 0) {
                    time--;
                    gui.updateGui(state, time, count);
                } else {
                    switchState();
                    playSound("pause.wav");
                }
                break;
        }
    }

    private void switchState() {
        switch (state) {
            case PAUSE:
                state = ApplicationState.WORK;
                Integer workTime = gui.getWorkTime();
                if (workTime == null) {
                    time = DEFAULT_WORK_TIME * 60;
                } else {
                    time = workTime * 60;
                }
                break;
            case WORK:
                state = ApplicationState.PAUSE;
                Integer pauseTime = gui.getPauseTime();
                if (pauseTime == null) {
                    time = DEFAULT_PAUSE_TIME * 60;
                } else {
                    time = pauseTime * 60;
                }
                break;
        }
    }

    public void playSound(String name) {
        try {
            URL url = this.getClass().getClassLoader().getResource(name);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();
        } catch (UnsupportedAudioFileException | NullPointerException | IOException | LineUnavailableException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            mouseClicked(null);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
