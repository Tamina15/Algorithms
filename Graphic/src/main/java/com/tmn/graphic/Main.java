package com.tmn.graphic;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Frame f = new Frame();
        Panel p = new Panel(800, 800);
        f.add(p);
        f.pack();
        f.setLocationRelativeTo(null);

        Thread drawThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 60;
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
            double interval = 1000000000 / 60;
            long lastTime = System.nanoTime();
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    p.update();
                    delta--;
                }
            }
        });
        drawThread.join();
        updateThread.join();
        System.out.println("Main");
    }
}
