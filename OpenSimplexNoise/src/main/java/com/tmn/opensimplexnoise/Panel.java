package com.tmn.opensimplexnoise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Panel extends JPanel {

    private final int width, height;
    private Point origin = new Point(0, 0);
    private Point mousePt = new Point(0, 0);
    private double zoomFactor = 1;
    private int scale = 1;
    double delta = 0;

    public Panel(int width, int height) {
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mousePt.setLocation(e.getPoint());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); // Generated from
                // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = (e.getX() - mousePt.x) * scale;
                int dy = (e.getY() - mousePt.y) * scale;
                mousePt.setLocation(e.getX(), e.getY());
                origin.setLocation(origin.getX() + dx, origin.getY() + dy);
            }
        });
        this.addMouseWheelListener((MouseWheelEvent e) -> {
            // Zoom in
            if (e.getWheelRotation() < 0) {
                zoomFactor *= 1.1;
                increment += 0.001;
            }
            // Zoom out
            if (e.getWheelRotation() > 0) {
                zoomFactor /= 1.1;
                increment -= 0.001;
            }
            if (increment < 0) {
                increment = 0;
            }
        });
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
//        g2d.scale(zoomFactor, zoomFactor);
//        g2d.translate(origin.x, origin.y);

        g2d.drawImage(image, null, 0, 0);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 50, t.length * 20 + 20);
        g2d.setColor(Color.black);
        g2d.drawString(executor.getActiveCount() + "", 20, 20);
        for (int i = 0; i < t.length; i++) {
            g2d.drawString(t[i] + "", 10, 20 * i + 40);
        }

        g2d.dispose();
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
            executor.submit(() -> timedGenNoise(part[4], origin.x, origin.y, zoff, part[0], part[1], part[2], part[3]));
        }
        zoff += zincrement;
        if(zoff > 1000){
            zincrement = -zincrement;
        }
    }
}
