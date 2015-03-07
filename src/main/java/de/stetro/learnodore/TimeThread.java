package de.stetro.learnodore;

import java.util.logging.Level;
import java.util.logging.Logger;

class TimeThread extends Thread {

    private final Application application;
    private boolean isRunning = true;

    TimeThread(Application application) {
        this.application = application;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeThread.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                application.onSecond();
            }
        }
    }

    public void stopSeconds() {
        isRunning = false;
    }
}
