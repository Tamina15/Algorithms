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
    private Point origin = new Point(0, 0);
    private Point mousePt = new Point(0, 0);
    private double zoomFactor = 1;
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
                    mousePt.setLocation(e.getPoint());
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int dx = (int) ((e.getX() - mousePt.x) * scale);
                    int dy = (int) ((e.getY() - mousePt.y) * scale);
                    mousePt.setLocation(e.getX(), e.getY());
                    origin.setLocation(origin.getX() + dx, origin.getY() + dy);
                }
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

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 30));
        textField.setBackground(Color.white);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int p = Integer.parseInt(textField.getText());
                    ca.restartToRule(p);
                    wca.restartToRule(p);
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

    private void init() {
        int length = 191;
        int[] initital = new int[length / 3];
        Random r = new Random();
        for (int i = 0; i < initital.length; i++) {
            initital[i] = r.nextInt(length);
        }
//        ca = new CellularAutomata(100, width, height);
//        ca = new CellularAutomata(length, width, height);
        ca = new CellularAutomata(length);
        wca = new WrappedCellularAutomata(length * 2 + 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = g2d.getTransform();
        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(origin.x, origin.y);

        // draw here
        ca.draw(g2d);
//        wca.draw(g2d);
        wca.draw(g2d, ca.getWidth() + 15, 0);
        ///////////////

        g2d.setColor(Color.red);
        g2d.drawString("FPS: " + fps, 0, -10);
        g2d.drawString("UPS: " + ups, 0, -20);

        g2d.setTransform(at);
    }

    CellularAutomata ca;
    WrappedCellularAutomata wca;

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
    }
}
