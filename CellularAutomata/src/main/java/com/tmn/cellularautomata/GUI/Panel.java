package com.tmn.cellularautomata.GUI;

import com.tmn.cellularautomata.CellularAutomata;
import com.tmn.cellularautomata.WrappedCellularAutomata;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Panel extends JPanel {

    private final int width, height;
    private final Point origin = new Point(0, 0);
    private final Point oldOrigin = new Point(0, 0);
    private final Point mousePt = new Point(0, 0);
    private double zoomFactor = 1, zoomDelta = 0.02, zoomMultiplier = 1.05;
    private int scale = 1;
    int fps, ups;

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
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    move(e);
                }
            }
        });

        this.addMouseWheelListener((MouseWheelEvent e) -> {
            zoom(e);
        });

        init();

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 30));
        textField.setBackground(Color.white);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int r = Integer.parseInt(textField.getText());
                    ca.restartToRule(r);
                    wca.restartToRule(r);
                    rca.restartToRule(r);
                    rwca.restartToRule(r);
                } catch (NumberFormatException err) {
                }
            }
        });
        this.add(textField);
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
        double dx = ((e.getX() - mousePt.x));
        double dy = ((e.getY() - mousePt.y));
        origin.setLocation(oldOrigin.x + dx, oldOrigin.y + dy);
    }

    private void zoom(MouseWheelEvent e) {
        // Zoom in
        boolean zoomIn = true;
        if (e.getWheelRotation() < 0) {
//            zoomFactor += zoomDelta;
            zoomFactor *= zoomMultiplier;
        }
        // Zoom out
        if (e.getWheelRotation() > 0) {
            zoomIn = false;
//            zoomFactor -= zoomDelta;
            zoomFactor /= zoomMultiplier;
        }
        zoomFactor = Math.clamp(zoomFactor, 0.01, 10);
        if (Math.abs(zoomFactor - 1.0) < 0.01) {
            zoomFactor = 1;
        }
    }

    private void init() {
        int length = Long.SIZE * 3 ;
        int[] initital = new int[length / 3];
        Random r = new Random();
        for (int i = 0; i < initital.length; i++) {
            initital[i] = r.nextInt(length);
        }
//        ca = new CellularAutomata(100, width, height);
//        ca = new CellularAutomata(length, width, height);
        long seed = r.nextLong();
        ca = new CellularAutomata(length, seed);
        wca = new WrappedCellularAutomata(length, seed);
        rca = new CellularAutomata(length, 0L);
        rwca = new WrappedCellularAutomata(length, 0L);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = g2d.getTransform();
        g2d.translate(origin.x, origin.y);
        g2d.scale(zoomFactor, zoomFactor);

        // draw here
        ca.draw(g2d);
        wca.draw(g2d, ca.getWidth() + 15, 0);
        rca.draw(g2d, 0, ca.getHeight() + 15);
        rwca.draw(g2d, ca.getWidth() + 15, ca.getHeight() + 15);
        ///////////////

        g2d.setColor(Color.red);
        g2d.setTransform(at);
        g2d.drawString("FPS: " + fps, 0, 10);
        g2d.drawString("UPS: " + ups, 0, 20);
    }

    CellularAutomata ca, rca;
    WrappedCellularAutomata wca, rwca;

    int waitBefore = 0, waitAfter = -1, delay;

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

        if (!ca.done) {
            ca.update();
        }

        if (!wca.done) {
            wca.update();
        }

        if (!rca.done) {
            rca.update();
        }

        if (!rwca.done) {
            rwca.update();
        }
    }
}
