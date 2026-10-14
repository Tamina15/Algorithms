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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class NPanel extends JPanel implements NUpdatable {

    public int width, height;
    private final Point2D origin = new Point.Double();
    private final Point2D oldOrigin = new Point.Double();
    private final Point2D mousePanel = new Point.Double(), mouseWorld = new Point.Double();
    private double scale = 1;
    private final double zoomFactor = 1.05, inversedZoomFactor = 1 / zoomFactor, minScale = 0.15, maxScale = 15.3674;

    public NPanel(Dimension d) {
        this(d.width, d.height);
    }

    public NPanel() {
        this(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public NPanel(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListeners();
    }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setAnchorPoint(e);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    drag(e);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                move(e);
            }
        });

        addMouseWheelListener((MouseWheelEvent e) -> zoom(e));
    }

    public void resetTransform() {
        resetScale();
        resetTranslate();
    }

    public void resetScale() {
        scale = 1;
    }

    public void resetTranslate() {
        oldOrigin.setLocation(0, 0);
        origin.setLocation(0, 0);
        mousePanel.setLocation(0, 0);
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOrigin.setLocation(origin);
        mousePanel.setLocation(e.getPoint());
    }

    private void move(MouseEvent e) {
        mousePanel.setLocation(e.getPoint());
        mouseWorld.setLocation((mousePanel.getX() - origin.getX()) / scale,
                (mousePanel.getY() - origin.getY()) / scale);
    }

    private void drag(MouseEvent e) {
        double dx = ((e.getX() - mousePanel.getX()));
        double dy = ((e.getY() - mousePanel.getY()));
        origin.setLocation(oldOrigin.getX() + dx, oldOrigin.getY() + dy);
    }

    private void zoom(MouseWheelEvent e) {
        double z = 1, c = scale;

        // Zoom in
        if (e.getWheelRotation() < 0) {
            z = zoomFactor;
        }

        // Zoom out
        if (e.getWheelRotation() > 0) {
            z = inversedZoomFactor;
        }
        scale = scale * z;

        if (Math.abs(scale - 1) < 0.04) {
            scale = 1;
        } else {
            scale = Math.clamp(scale, minScale, maxScale);
        }

        if (scale == c) {
            return;
        }

        Point a = e.getPoint();
        double x = a.x - (a.x - origin.getX()) * z;
        double y = a.y - (a.y - origin.getY()) * z;
        origin.setLocation(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Original rectangle position and size
        int rectX = 50, rectY = 50, rectWidth = 100, rectHeight = 50;

        // Save the original transform
        AffineTransform originalTransform = g2d.getTransform();

        g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
        g2d.setColor(Color.white);

        // Apply scaling
        g2d.translate(origin.getX(), origin.getY());
        g2d.scale(scale, scale);

        // Draw the rectangle
        g2d.fillRect(rectX, rectY, rectWidth, rectHeight);
        draw(g2d);
        // Restore the original transform
        g2d.setTransform(originalTransform);
    }

    public void draw(Graphics2D g2d) {

    }

    @Override
    public void update() {
    }
}
