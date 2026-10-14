package com.tmn.graphic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class NMain implements Runnable {

    private final NFrame frame;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public NMain(NFrame frame) {
        this.frame = frame;
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running.set(false);
                frame.dispose();
            }
        });
    }

    @Override
    public void run() {
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
            final long interval = 1_000_000_000L / 60;
            long previous = System.nanoTime();
            long accumulator = 0;
            while (running.get()) {
                long now = System.nanoTime();
                accumulator += now - previous;
                previous = now;
                while (accumulator >= interval) {
                    frame.update();
                    accumulator -= interval;
                }
                long sleep = interval - accumulator;
                if (sleep > 0) {
                    LockSupport.parkNanos(sleep);
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
