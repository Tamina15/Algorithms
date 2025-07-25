package com.tmn.quadtree;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

class Frame extends JFrame {

    public Frame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setEnabled(true);
    }
}

public class Main {

    public static void main(String[] args) {
        Panel p = new Panel(800, 600);
        Frame f = new Frame();
        f.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                p.requestFocusInWindow();
            }
        });
        f.add(p);
        f.pack();
        f.setLocationRelativeTo(null);
//        f.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the JFrame

        final int FPS = 60;
        final int UPS = 60;
        Thread drawThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / FPS;
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
            double interval = 1000000000 / UPS;
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
        try {
            drawThread.join();
            updateThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
