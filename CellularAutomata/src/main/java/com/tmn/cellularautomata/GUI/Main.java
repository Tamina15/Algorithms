package com.tmn.cellularautomata.GUI;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

public class Main {

    private static Frame f;
    private static Panel p;

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            f = new Frame();
            p = new Panel(800, 500);
            f.add(p);
            f.pack();
            f.setLocationRelativeTo(null);
        });

        Thread.startVirtualThread(loop(f, 60, () -> {
            SwingUtilities.invokeLater(() -> {
                f.repaint();
            });
        }, (Integer cps) -> {
            p.fps = cps;
        }));

        Thread.startVirtualThread(loop(f, 120, () -> {
            p.update();
        }, (Integer cps) -> {
            p.ups = cps;
        }
        ));
    }

    public static Runnable loop(Frame f, int fps, Runnable task, Consumer<Integer> perSecondTask) {
        return () -> {
            double delta = 0;
            double interval = 1000000000 / fps;
            long lastTime = System.nanoTime();
            long timer = 0;
            int count = 0, cps = 0;
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;
                if (delta >= 1) {
                    task.run();
                    count++;
                    delta--;
                }
                if (timer >= 1000000000) {
                    cps = count;
                    perSecondTask.accept(cps);
                    count = 0;
                    timer = 0;
                }
            }
        };
    }
}
