package com.tmn.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class NPanel extends JPanel implements NUpdatable {

    public int width, height;
    private double originX, originY, oldOriginX, oldOriginY, mousePanelX, mousePanelY, mouseWorldX, mouseWorldY;
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
        oldOriginX = 0;
        oldOriginY = 0;

        originX = 0;
        originY = 0;

        mousePanelX = 0;
        mousePanelY = 0;
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOriginX = originX;
        oldOriginY = originY;

        mousePanelX = e.getX();
        mousePanelY = e.getY();
    }

    private void move(MouseEvent e) {
        mousePanelX = e.getX();
        mousePanelY = e.getY();

        mouseWorldX = (mousePanelX - originX) / scale;
        mouseWorldY = (mousePanelY - originY) / scale;
    }

    private void drag(MouseEvent e) {
        double dx = ((e.getX() - mousePanelX));
        double dy = ((e.getY() - mousePanelY));

        originX = oldOriginX + dx;
        originY = oldOriginY + dy;
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

        double x = e.getX();
        double y = e.getY();
        originX = x - (x - originX) * z;
        originY = y - (y - originY) * z;
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
        g2d.translate(originX, originY);
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
