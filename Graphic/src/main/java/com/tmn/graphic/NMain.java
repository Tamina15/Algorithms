package com.tmn.graphic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class NMain implements Runnable {

    private final JFrame frame;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public NMain(JFrame frame) {
        this.frame = frame;
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running.set(false);
                frame.dispose();
            }
        });
    }

    public NMain(NPanel p) {
        this(new NFrame());
        frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                p.requestFocusInWindow();
            }
        });
        frame.add(p);
    }

    @Override
    public void run() {
        frame.pack();
        frame.setLocationRelativeTo(null);

        System.out.println("Hold left mouse button and drag to move around");
        System.out.println("Scroll mouse wheel to zoom. Hold CTRL to increase zoom speed");

        Thread drawThread = Thread.startVirtualThread(() -> {
            final long frameTime = 1_000_000_000L / 60;
            long next = System.nanoTime();
            while (running.get()) {
                SwingUtilities.invokeLater(frame::repaint);
                next += frameTime;
                long sleep = next - System.nanoTime();
                if (sleep > 0) {
                    LockSupport.parkNanos(sleep);
                }
            }
        });

        Thread updateThread = Thread.startVirtualThread(() -> {
            if (!(frame instanceof NFrame nframe)) {
                return;
            }

            final long interval = 1_000_000_000L / 60;
            long previous = System.nanoTime();
            long accumulator = 0;

            long next = System.nanoTime();

            while (running.get()) {
                long now = System.nanoTime();
                accumulator += now - previous;
                previous = now;
                while (accumulator >= interval) {
                    nframe.update();
                }

                next += interval;
                while (true) {
                    long remaining = next - System.nanoTime();

                    if (remaining <= 0) {
                        break;
                    }

                    if (remaining > 1_000_000) {
                        LockSupport.parkNanos(remaining - 500_000);
                    } else {
                        Thread.onSpinWait();
                    }
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
