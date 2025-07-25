package com.tmn.quadtree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Panel extends JPanel {

    int width;
    int height;

    private final int viewPortBuffer = 150;
    private Rectangle.Double originalViewPort, viewPort;

    private final Point origin = new Point(0, 0);
    private final Point oldOrigin = new Point(0, 0);
    private final Point mousePt = new Point(0, 0);

    private double zoomFactor = 1, zoomDelta = 0.02, zoomMultiplier = 1.05;

    Random random = new Random();
    int n = 5000;
    Rectangle.Double boundary;
    QuadTree qtree;

    ArrayList<MovingPoint> points;
    ArrayList<MovingPoint> collideds, founds;
    double collisionLength;
    double updateTime;

    public Panel(Dimension d) {
        this(d.width, d.height);
    }

    public Panel() {
        this(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public Panel(int width, int height) {
        this.width = width;
        this.height = height;

        this.setPreferredSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
        this.setBackground(Color.black);
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
            public void mouseMoved(MouseEvent e) {

            }

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

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    zoomDelta = 0.15;
                    zoomMultiplier = 1.15;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    zoomDelta = 0.05;
                    zoomMultiplier = 1.05;
                }
            }
        });

        Panel panel = this;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                double half_dx = (panel.getWidth() - panel.width) / 2;
                double half_dy = (panel.getHeight() - panel.height) / 2;

                panel.width = panel.getWidth();
                panel.height = panel.getHeight();

                originalViewPort.x += half_dx;
                originalViewPort.y += half_dy;

                origin.x += half_dx;
                origin.y += half_dy;

                centerViewPort();
            }
        });

        newViewPort();

        boundary = new Rectangle.Double(0, 0, 15 * width, 10 * height);
        qtree = new QuadTree(boundary, 10);
        points = new ArrayList<>(n);
        collisionLength = 50;

        for (int i = 0; i < n; i++) {
            Point p = new Point(random.nextDouble(boundary.width), random.nextDouble(boundary.height));
            Point v = new Point(random.nextDouble(-5, 5), random.nextDouble(-5, 5));
            MovingPoint mp = new MovingPoint(p, v, boundary, collisionLength);
            points.add(mp);
            qtree.insert(mp);
        }

        founds = new ArrayList<>(n);
        collideds = new ArrayList<>(n);
    }

    private void newViewPort() {
        int halfBuffer = viewPortBuffer / 2;
        int w = width + viewPortBuffer;
        int h = height + viewPortBuffer;
        int oX = (width - w) / 2 - halfBuffer;
        int oY = (height - h) / 2 - halfBuffer;
        this.viewPort = new Rectangle2D.Double(oX, oY, w + viewPortBuffer, h + viewPortBuffer);
        this.originalViewPort = new Rectangle2D.Double(oX, oY, w + viewPortBuffer, h + viewPortBuffer);
    }

    private void centerViewPort() {
        viewPort.width = originalViewPort.width / zoomFactor;
        viewPort.height = originalViewPort.height / zoomFactor;
        viewPort.x = (originalViewPort.x - origin.x) / zoomFactor;
        viewPort.y = (originalViewPort.y - origin.y) / zoomFactor;
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOrigin.setLocation(origin);
        mousePt.setLocation(e.getPoint());
    }

    private void move(MouseEvent e) {
        double dx = ((e.getX() - mousePt.x));
        double dy = ((e.getY() - mousePt.y));
        origin.setLocation(oldOrigin.x + dx, oldOrigin.y + dy);
        viewPort.x = (originalViewPort.x - origin.x) / zoomFactor;
        viewPort.y = (originalViewPort.y - origin.y) / zoomFactor;
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
        centerViewPort();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = g2d.getTransform();

        g2d.translate(origin.x, origin.y);
        g2d.scale(zoomFactor, zoomFactor);

        long s = System.nanoTime();

//        qtree.draw(g2d, viewPort);
        drawTree(g2d, qtree);

        long e = System.nanoTime();

        g2d.setTransform(at);

        g2d.setColor(Color.white);
        g2d.drawString("draw time: " + (e - s) * 1.0 / 1_000_000 + "ms", 0, 30);
        g2d.drawString("update time: " + updateTime + "ms", 0, 60);
        g2d.drawString("zoom: " + zoomFactor, 0, 90);
        g2d.dispose();
    }

    public void update() {
        long start = System.nanoTime();

        updatePosition();
        updateTree();
        collideDectection();

        long end = System.nanoTime();
        updateTime = (end - start) * 1.0 / 1_000_000;
    }

    private void updatePosition() {
        for (MovingPoint ps : points) {
            ps.update();
        }
    }

    private void updateTree() {
        QuadTree newQuadTree = new QuadTree(boundary, 5);
        for (MovingPoint ps : points) {
            if (viewPort.contains(ps.position)) {
                newQuadTree.insert(ps);
            }
        }
        qtree = newQuadTree;
    }

    private void collideDectection() {
        ArrayList<MovingPoint> newCollideds = new ArrayList<>(n / 2);
        for (MovingPoint p : points) {
            if (viewPort.contains(p.position)) {
                founds.clear();
                qtree.query(p.range, founds);
                for (int i = 0; i < founds.size(); i++) {
                    MovingPoint f = founds.get(i);
                    if (p != f && p.collide(f)) {
                        newCollideds.add(f);
                    }
                }
            }
        }
        collideds = newCollideds;
    }

    private void drawTree(Graphics2D g2d, QuadTree tree) {
        if (!tree.boundary.intersects(viewPort)) {
            return;
        }
        g2d.setColor(Color.white);
        g2d.draw(tree.boundary);
        ArrayList<MovingPoint> cs = new ArrayList<>();
        for (int i = 0; i < tree.pointsIndex; i++) {
            MovingPoint mp = tree.points[i];
            if (collideds.contains(mp)) {
                cs.add(mp);
                continue;
            }
            Point.Double p = mp.position;
            g2d.drawLine((int) p.x, (int) p.y, (int) p.x, (int) p.y);
            g2d.draw(mp.collisionBox);
        }
        g2d.setColor(Color.green);
        for (MovingPoint mp : cs) {
            Point.Double p = mp.position;
            g2d.drawLine((int) p.x, (int) p.y, (int) p.x, (int) p.y);
            g2d.draw(mp.collisionBox);
        }

        if (tree.isDivided) {
            drawTree(g2d, tree.ne);
            drawTree(g2d, tree.nw);
            drawTree(g2d, tree.se);
            drawTree(g2d, tree.sw);
        }
    }

}
