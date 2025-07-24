package com.tmn.graphic;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NMain implements Runnable {

    NFrame frame;

    public NMain(NFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        Thread drawThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 60;
            long lastTime = System.nanoTime();
            while (frame.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    frame.repaint();
                    delta--;
                }
            }
        });
        Thread updateThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 60;
            long lastTime = System.nanoTime();
            while (frame.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    frame.update();
                    delta--;
                }
            }
        });
        try {
            drawThread.join();
            updateThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(NMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Main");
    }
}
