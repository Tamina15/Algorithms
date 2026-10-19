package com.tmn.multisourcebfs;

import com.tmn.graphic.NMain;
import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Graphics2D;

public class Main {

    static class Panel extends NPanel {

        double delta;

        public Panel(int width, int height) {
            super(width, height);
            bfs = new MultiSourceBFS(width, height, 0, 0, width, height);
            bfs.randomWater(10);
        }

        @Override
        public void draw(Graphics2D g2d) {
            bfs.draw(g2d);
        }

        @Override
        public void drawOriginal(Graphics2D g2d) {
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, 50, 30);
            g2d.setColor(Color.black);
            g2d.drawString(delta + "", 10, 10);
        }

        MultiSourceBFS bfs;
        int waitBefore = 150, waitAfter = -1, delay;

        public void update() {
            if (waitBefore > 0) {
                waitBefore--;
                return;
            }
            if (waitAfter > 0) {
                waitAfter--;
                return;
            }
            if (delay > 0) {
                delay--;
                return;
            } else {
                delay = 0;
            }
            if (bfs.isDone()) {
                bfs.reset(15);
                waitAfter = 0;
                return;
            }
            long s = System.nanoTime();
            bfs.run(5);
            long e = System.nanoTime();
            delta = (e - s) * 1.0 / 1000000;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        NMain main = new NMain(new Panel(800, 600));
        main.run();
    }
}
