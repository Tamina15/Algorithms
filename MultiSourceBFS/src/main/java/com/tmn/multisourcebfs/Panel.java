package com.tmn.multisourcebfs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Panel extends JPanel {

    private final int width, height;
    private final Point origin = new Point(0, 0);
    private final Point oldOrigin = new Point(0, 0);
    private final Point mousePt = new Point(0, 0);
    private double zoomFactor = 1;
    private int scale = 1;
    double delta;

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
                    setAnchorPoint(e);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move(e);
            }
        });

        this.addMouseWheelListener((MouseWheelEvent e) -> {
            // Zoom in
            if (e.getWheelRotation() < 0) {
                zoomFactor *= 1.1;
            }
            // Zoom out
            if (e.getWheelRotation() > 0) {
                zoomFactor /= 1.1;
            }
        });
        init();
    }

    public Panel(Dimension d) {
        this(d.width, d.height);
    }

    public Panel() {
        this(Toolkit.getDefaultToolkit().getScreenSize());
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOrigin.setLocation(origin);
        mousePt.setLocation(e.getPoint());
    }

    private void move(MouseEvent e) {
        double dx = ((e.getX() - mousePt.x) / zoomFactor);
        double dy = ((e.getY() - mousePt.y) / zoomFactor);
        origin.setLocation(oldOrigin.getX() + dx, oldOrigin.getY() + dy);
    }

    private void init() {
        bfs = new MultiSourceBFS(width, height, 0, 0, width, height);
        bfs.randomWater(10);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(origin.x, origin.y);

        // draw here
        bfs.draw(g2d);
        ///////////////

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 50, 30);
        g2d.setColor(Color.black);
        g2d.drawString(delta + "", 10, 10);
        g2d.dispose();
    }

    MultiSourceBFS bfs;
    int waitBefore = 100, waitAfter = -1, delay;

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
            waitAfter = 120;
            return;
        }
        long s = System.nanoTime();
        bfs.run(5);
        long e = System.nanoTime();
        delta = (e - s) * 1.0 / 1000000;
    }

}
