package com.tmn.graphic;

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
    private Point origin = new Point(0, 0);
    private Point mousePt = new Point(0, 0);
    private double zoomFactor = 1;
    private int scale = 1;

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
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = ((e.getX() - mousePt.x) * scale);
                int dy = ((e.getY() - mousePt.y) * scale);
                mousePt.setLocation(e.getX(), e.getY());
                origin.setLocation(origin.getX() + dx, origin.getY() + dy);
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

    private void init() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(origin.x, origin.y);

        // draw here
        
        ///////////////
        g2d.dispose();
    }

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
    }

}
