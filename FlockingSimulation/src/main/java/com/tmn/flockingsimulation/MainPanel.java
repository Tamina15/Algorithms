package com.tmn.flockingsimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainPanel extends JPanel {

    private final int width, height;
    private Point origin = new Point(0, 0);
    private Point mousePt = new Point(0, 0);
    double delta;

    public MainPanel(int width, int height) {
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
                int dx = (e.getX() - mousePt.x) * Option.scale;
                int dy = (e.getY() - mousePt.y) * Option.scale;
                mousePt.setLocation(e.getX(), e.getY());
                origin.setLocation(origin.getX() + dx, origin.getY() + dy);
            }
        });
        init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(Option.zoomFactor, Option.zoomFactor);
        g2d.translate(origin.x, origin.y);
//        g2d.setStroke(new BasicStroke(3));
        draw(g2d);
        g2d.drawRect((int) bound.x, (int) bound.y, (int) bound.getMaxX(), (int) bound.getMaxY());
        g2d.scale(1 / Option.zoomFactor, 1 / Option.zoomFactor);
        g2d.drawString(delta + "", 20, 40);
        g2d.dispose();
    }

    private void init() {
        bound = new Rectangle(0, 0, width * 4, height * 4);
        boids = new Boid[400];
        for (int i = 0; i < boids.length; i++) {
            boids[i] = new Boid(i, bound);
        }
    }

    Boid[] boids;
    Rectangle bound;

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for (Boid boid : boids) {
            boid.draw(g2d, boids);
        }

    }

    public void update() {
        for (Boid b : boids) {
            b.update(boids);
        }
    }
}
