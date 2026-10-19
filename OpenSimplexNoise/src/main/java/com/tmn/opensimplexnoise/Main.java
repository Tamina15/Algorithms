package com.tmn.opensimplexnoise;

import com.tmn.graphic.NMain;
import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    static class Panel extends NPanel {

        public Panel(int width, int height) {
            super(width, height);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int h4 = height / 4;
            int[][] ps = {
                {0, width / 2, 0, h4, 0},
                {width / 2, width, 0, h4, 1},
                {0, width / 2, h4, h4 * 2, 2},
                {width / 2, width, h4, h4 * 2, 3},
                {0, width / 2, h4 * 2, h4 * 3, 4},
                {width / 2, width, h4 * 2, h4 * 3, 5},
                {0, width / 2, h4 * 3, h4 * 4, 6},
                {width / 2, width, h4 * 3, h4 * 4, 7}
            };
            parts = ps;
            t = new double[parts.length];
        }

        BufferedImage image;
        double increment = 0.01f;
        double zoff = 0.5f;
        double zincrement = 0.02f;

        @Override
        public void draw(Graphics2D g2d) {
            g2d.drawImage(image, null, 0, 0);
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, 50, t.length * 20 + 20);
            g2d.setColor(Color.black);
            g2d.drawString(executor.getActiveCount() + "", 20, 20);
            for (int i = 0; i < t.length; i++) {
                g2d.drawString(t[i] + "", 10, 20 * i + 40);
            }
        }

        public void genNoise(int x, int y, double z, int w0, int w1, int h0, int h1) {
            double xoff = x + w0 * increment;
            for (int i = w0; i < w1; i++) {
                xoff += increment;
                double yoff = y + h0 * increment;
                for (int j = h0; j < h1; j++) {
                    yoff += increment;
                    double value = OpenSimplex2S.noise3_ImproveXY(1, xoff, yoff, z);
                    int rgb = 0x010101 * (int) ((value + 1) * 127.5);
//                int rgb = value > 0 ? -1 : -16777216;
                    image.setRGB(i, j, rgb);
                }
            }
        }

        public void timedGenNoise(int i, int x, int y, double z, int w0, int w1, int h0, int h1) {
            long start = System.nanoTime();
            genNoise(x, y, z, w0, w1, h0, h1);
            long end = System.nanoTime();
            double time = (end - start) * 1.0 / 1000000;
            t[i] = time;
        }

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        int[][] parts;
        double[] t;

        public void update() {
            if (executor.getActiveCount() > 10) {
                return;
            }
            for (int[] part : parts) {
                executor.submit(() -> timedGenNoise(part[4], (int) originX, (int) originY, zoff, part[0], part[1], part[2], part[3]));
            }
            zoff += zincrement;
            if (zoff > 1000) {
                zincrement = -zincrement;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NMain main = new NMain(new Panel(800, 600));
        main.run();
    }
}
