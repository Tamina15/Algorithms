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
    protected double originX, originY;
    protected double oldOriginX, oldOriginY;
    protected double anchorX, anchorY;
    protected double mouseWorldX, mouseWorldY;
    protected double currentMouseX, currentMouseY;
    private double scale = 1;
    protected double zoomFactor = 1.05, inversedZoomFactor = 1 / zoomFactor, minScale = 0.15, maxScale = 15.3674;

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
                currentMouse(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    drag(e);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                currentMouse(e);
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

        anchorX = 0;
        anchorY = 0;
    }

    private void currentMouse(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
    }

    protected void setAnchorPoint(MouseEvent e) {
        oldOriginX = originX;
        oldOriginY = originY;

        anchorX = e.getX();
        anchorY = e.getY();
    }

    protected void move(MouseEvent e) {
        mouseWorldX = (currentMouseX - originX) / scale;
        mouseWorldY = (currentMouseY - originY) / scale;
    }

    protected void drag(MouseEvent e) {
        double dx = ((e.getX() - anchorX));
        double dy = ((e.getY() - anchorY));

        originX = oldOriginX + dx;
        originY = oldOriginY + dy;
    }

    protected void zoom(MouseWheelEvent e) {
        double oldScale = scale;

        if (e.getWheelRotation() < 0) {
            scale *= zoomFactor;
        } else if (e.getWheelRotation() > 0) {
            scale *= inversedZoomFactor;
        }

        if (Math.abs(scale - 1) < 0.04) {
            scale = 1;
        } else {
            scale = Math.clamp(scale, minScale, maxScale);
        }

        if (scale == oldScale) {
            return;
        }

        double x = e.getX();
        double y = e.getY();

        // World position currently under the mouse
        double worldX = (x - originX) / oldScale;
        double worldY = (y - originY) / oldScale;

        // Move origin so the same world point stays under the mouse
        originX = x - worldX * scale;
        originY = y - worldY * scale;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Save the original AffineTransform
        AffineTransform at = g2d.getTransform();

        // Apply scaling
        g2d.translate(originX, originY);
        g2d.scale(scale, scale);

        draw(g2d);

        // Restore the original AffineTransform
        g2d.setTransform(at);

        drawOriginal(g2d);

        // Restore the original AffineTransform
        g2d.setTransform(at);
    }

    public void draw(Graphics2D g2d) {
    }

    public void drawOriginal(Graphics2D g2d) {
    }

    @Override
    public void update() {
    }

    public double getScale() {
        return scale;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        inversedZoomFactor = 1 / this.zoomFactor;
    }
}
