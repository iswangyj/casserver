package com.ume.framework.plugin.sessionman;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author SxL
 * Created on 9/25/2018 1:37 PM.
 */
public class ShowMemoryContent extends TimerTask {
    private static Timer timer = new Timer();

    public ShowMemoryContent() {
    }

    public static void runTimer4ShowMemory() {
        long delay = 60000L;
        long period = 60000L;
        timer.schedule(new ShowMemoryContent(), delay, period);
    }

    @Override
    public void run() {
        SessionManager.getInstance().showMemoryContent();
    }
}
