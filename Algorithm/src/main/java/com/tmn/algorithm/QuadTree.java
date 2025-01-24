/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Tran Minh Nhat
 */
public class QuadTree {

    ArrayList<Point.Double> points;
    Rectangle.Double boundary;
    double midX, midY;
    double halfWidth, halfHeight;
    int maxCapacity;
    boolean isDivided;
    QuadTree nw;
    QuadTree ne;
    QuadTree se;
    QuadTree sw;

    public QuadTree(Rectangle.Double boundary, int maxCapacity) {
        this.boundary = boundary;
        midX = this.boundary.x + this.boundary.width / 2;
        midY = this.boundary.y + this.boundary.height / 2;
        halfWidth = this.boundary.width / 2;
        halfHeight = this.boundary.height / 2;
        this.maxCapacity = maxCapacity;
        points = new ArrayList();
        isDivided = false;
    }

    public boolean insert(Point.Double p) {
        if (!boundary.contains(p)) {
            return false;
        }
        if (points.size() < maxCapacity) {
            points.add(p);
            return true;
        }
        if (!isDivided) {
            subdivide();
            isDivided = true;
        }
        if (p.x > midX && p.y <= midY) {
            return ne.insert(p);
        }
        if (p.x >= midX && p.y > midY) {
            return se.insert(p);
        }
        if (p.x <= midX && p.y < midY) {
            return nw.insert(p);
        }
        if (p.x < midX && p.y >= midY) {
            return sw.insert(p);
        }
        return false;
    }

    public void subdivide() {
        Rectangle.Double NEboundary = new Rectangle.Double(midX, boundary.y, halfWidth, halfHeight);
        Rectangle.Double NWboundary = new Rectangle.Double(boundary.x, boundary.y, halfWidth, halfHeight);
        Rectangle.Double SEboundary = new Rectangle.Double(midX, midY, halfWidth, halfHeight);
        Rectangle.Double SWboundary = new Rectangle.Double(boundary.x, midY, halfWidth, halfHeight);
        ne = new QuadTree(NEboundary, maxCapacity);
        nw = new QuadTree(NWboundary, maxCapacity);
        se = new QuadTree(SEboundary, maxCapacity);
        sw = new QuadTree(SWboundary, maxCapacity);
    }

    public ArrayList<Point.Double> query(Rectangle.Double range, ArrayList<Point.Double> found) {
        if (!boundary.intersects(range)) {
            return found;
        }
        for (Point.Double p : points) {
            if (range.contains(p)) {
                found.add(p);
            }
        }
        if (isDivided) {
            ne.query(range, found);
            nw.query(range, found);
            se.query(range, found);
            sw.query(range, found);
        }

        return found;
    }

    public void draw(Graphics2D g2d) {
        g2d.draw(boundary);
        for (Point.Double p : points) {
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine((int) p.x, (int) p.y, (int) p.x, (int) p.y);
        }
        g2d.setStroke(new BasicStroke(1));
        if (this.isDivided) {
            ne.draw(g2d);
            nw.draw(g2d);
            se.draw(g2d);
            sw.draw(g2d);
        }
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
    }

    static class Panel extends JPanel {

        int panelWidth = 1900;
        int panelHeight = 800;
        Random random = new Random();
        int n = 1000;
        Rectangle.Double boundary = new Rectangle.Double(0, 0, panelWidth, panelHeight);
        QuadTree qtree = new QuadTree(boundary, 4);
        Rectangle.Double range = new Rectangle.Double(250, 300, 200, 200);
        ArrayList<Point.Double> points = new ArrayList();

        public Panel() {
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setDoubleBuffered(true);
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addMouseMotionListener(new MouseMoved());
            for (int i = 0; i < n; i++) {
                Point.Double p = new Point.Double(random.nextInt(panelWidth), random.nextInt(panelHeight));
                points.add(p);
                boolean t = qtree.insert(p);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.white);
            g2d.setFont(new Font("Times New Roman", Font.BOLD, 20));
            g2d.drawString(Frame.FPScount, 30, 30);
            g2d.drawString(Frame.delta + "", 60, 30);

            long s = System.nanoTime();
            long e = System.nanoTime();
            qtree.draw(g2d);
            g2d.setColor(Color.green);
            g2d.draw(range);
            ArrayList<Point.Double> found = qtree.query(range, new ArrayList<>());
//            ArrayList<Point.Double> found = found(range);
            g2d.setStroke(new BasicStroke(4));
            for (Point.Double f : found) {
                g2d.drawLine((int) f.x, (int) f.y, (int) f.x, (int) f.y);
            }
            g2d.drawString("time: " + (e - s)*1.0/1_000_000 + "ms", 10, 10);
            g2d.dispose();
            g.dispose();
        }

        public ArrayList<Point2D.Double> found(Rectangle.Double range) {
            ArrayList<Point.Double> found = new ArrayList();
            for (Point.Double p : points) {
                if (range.contains(p)) {
                    found.add(p);
                }
            }
            return found;
        }

        public BufferedImage saveImage() {
            BufferedImage image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            paint(g2);
            return image;
        }

        public void update() {
            QuadTree temp_qtree = new QuadTree(boundary, 4);
            ArrayList<Point.Double> temp_points = new ArrayList();
            for (int i = 0; i < n; i++) {
                Point.Double p = new Point.Double(random.nextInt(panelWidth), random.nextInt(panelHeight));
                temp_points.add(p);
                boolean t = temp_qtree.insert(p);
            }
            points = temp_points;
            qtree = temp_qtree;
        }

        class MouseMoved extends MouseAdapter {

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getX() < panelWidth && e.getY() < panelHeight && e.getX() > 0 && e.getX() > 0) {
//                    qtree.insert(new Point.Double(e.getX(), e.getY()));
                    range.x = e.getX();
                    range.y = e.getY();
                }
            }
        }
    }

    static class Frame extends JFrame implements Runnable {

        Thread thread;
        public final int FPS = 60;
        public static String FPScount = "NaN";
        public static double delta = 0;
        public static boolean updating;
        public static BufferedImage image;

        ;
        Panel panel;

        Frame() {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new Panel();
            image = new BufferedImage(panel.panelWidth, panel.panelHeight, BufferedImage.TYPE_INT_RGB);
            this.add(panel);
            this.pack();
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            this.setBackground(Color.black);
            this.setVisible(true);
            newThread();
        }

        private void newThread() {
            thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            double drawInterval = 1000000000 / FPS;
            long lastTime = System.nanoTime();
            long timer = 0;
            int count = 0;
            while (thread != null) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                timer += (currentTime - lastTime);
                lastTime = currentTime;
                if (delta >= 1) {
                    panel.repaint();
                    delta--;
                    count++;
                }
                if (timer >= 1000000000) {
                    FPScount = "" + count;
//                    panel.update();
                    count = 0;
                    timer = 0;
                    updating = false;
                }
            }
        }
    }
}
