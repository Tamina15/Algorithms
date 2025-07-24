package com.tmn.graphic;

import com.tmn.graphic.NUpdatable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class NPanel extends JPanel implements NUpdatable {

    private final int width, height;
    private final Point origin = new Point(0, 0);
    private final Point oldOrigin = new Point(0, 0);
    private final Point mousePt = new Point(0, 0);
    private double zoomFactor = 1, zoomMutiplier = 0.05;
    private int scale = 1;

    public NPanel(int width, int height) {
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
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                move(e);
            }
        });

        this.addMouseWheelListener((MouseWheelEvent e) -> {
            zoom(e);
        });

        init();
    }

    public NPanel(Dimension d) {
        this(d.width, d.height);
    }

    public NPanel() {
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

    private void zoom(MouseWheelEvent e) {
        // Zoom in
        boolean isZoomIn = true;
        if (e.getWheelRotation() < 0) {
            zoomFactor += zoomMutiplier;
        }
        // Zoom out
        if (e.getWheelRotation() > 0) {
            isZoomIn = false;
            zoomFactor -= zoomMutiplier;
        }
    }

    private void init() {
    }

    @Override
    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//        AffineTransform at = g2d.getTransform();
//        g2d.translate(origin.x, origin.y);
//        g2d.scale(zoomFactor, zoomFactor);
//        draw(g2d, at);
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Original rectangle position and size
        int rectX = 50, rectY = 50, rectWidth = 100, rectHeight = 50;

        // Save the original transform
        AffineTransform originalTransform = g2d.getTransform();

        g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
        // Apply scaling
                g2d.translate(origin.x, origin.y);
        g2d.scale(zoomFactor, zoomFactor);

        // Adjust rectangle position to keep it relative after scaling
        int adjustedX = (int) (rectX / zoomFactor);
        int adjustedY = (int) (rectY / zoomFactor);

        // Draw the rectangle
        g2d.drawRect(adjustedX, adjustedY, rectWidth, rectHeight);

        // Restore the original transform
        g2d.setTransform(originalTransform);
    }

    public void draw(Graphics2D g2d, AffineTransform at) {

    }

    @Override
    public void update() {
    }

}
