package com.tmn.flockingsimulation;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Frame f = new Frame();
        Thread drawThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 30;
            long lastTime = System.nanoTime();
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    f.repaint();
                    delta--;
                }
            }
        });
        Thread updateThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 30;
            long lastTime = System.nanoTime();
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    long s = System.nanoTime();
                    f.update();
                    long e = System.nanoTime();
                    f.mainPanel.delta = (e - s) * 1.0 / 1000000;
                    delta--;
                }
            }
        });
        drawThread.join();
        updateThread.join();
        System.out.println("Main");
    }

}
