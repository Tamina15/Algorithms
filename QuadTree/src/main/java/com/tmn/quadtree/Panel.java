package com.tmn.quadtree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
import java.awt.geom.Point2D;
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

    private final Point.Double origin = new Point.Double();
    private final Point.Double oldOrigin = new Point.Double();
    private final Point.Double mousePt = new Point.Double(), mouseWorld = new Point.Double();
    private double scale = 1, zoomFactor = 1.05, inversedZoomFactor = 1 / zoomFactor, minScale = 0.15, maxScale = 15.3674;

    Random random = new Random();
    int n = 15000;
    Rectangle.Double boundary;
    QuadTree qtree;

    ArrayList<MovingPoint> points;
    ArrayList<MovingPoint> collideds;
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
                move(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    drag(e);
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
                    zoomFactor = 1.15;
                    inversedZoomFactor = 1 / 1.15;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    zoomFactor = 1.05;
                    inversedZoomFactor = 1 / 1.05;
                }
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                Component c = componentEvent.getComponent();
                if (c instanceof Panel panel) {
                    double dx = (panel.getWidth() - panel.width);
                    double dy = (panel.getHeight() - panel.height);
                    // need this to work properly
                    double half_dx = dx / 2;
                    double half_dy = dy / 2;

                    panel.width = panel.getWidth();
                    panel.height = panel.getHeight();

                    originalViewPort.width += dx;
                    originalViewPort.height += dy;

                    origin.x += half_dx;
                    origin.y += half_dy;

                    centerViewPort();
                }
            }
        });

        newViewPort();

        boundary = new Rectangle.Double(0, 0, 20 * width, 15 * height);
        qtree = new QuadTree(boundary, 10);
        points = new ArrayList<>(n);
        collisionLength = 50;

        for (int i = 0; i < n; i++) {
            Point2D p = new Point2D.Double(random.nextDouble(boundary.width), random.nextDouble(boundary.height));
            Point2D v = new Point2D.Double(random.nextDouble(-5, 5), random.nextDouble(-5, 5));
            MovingPoint mp = new MovingPoint(p, v, boundary, collisionLength);
            points.add(mp);
            qtree.insert(mp);
        }

        collideds = new ArrayList<>(n);
    }

    private void newViewPort() {
        int halfBuffer = viewPortBuffer / 2;
        int w = width / 3 + viewPortBuffer;
        int h = height / 3 + viewPortBuffer;
        int oX = (width - w) / 2 - halfBuffer;
        int oY = (height - h) / 2 - halfBuffer;
        this.viewPort = new Rectangle2D.Double(oX, oY, w + viewPortBuffer, h + viewPortBuffer);
        this.originalViewPort = new Rectangle2D.Double(oX, oY, w + viewPortBuffer, h + viewPortBuffer);
    }

    private void centerViewPort() {
        viewPort.width = originalViewPort.width / scale;
        viewPort.height = originalViewPort.height / scale;
        viewPort.x = (originalViewPort.x - origin.x) / scale;
        viewPort.y = (originalViewPort.y - origin.y) / scale;
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOrigin.setLocation(origin);
        mousePt.setLocation(e.getPoint());
    }

    private void move(MouseEvent e) {
        mousePt.setLocation(e.getPoint());
        mouseWorld.setLocation((mousePt.x - origin.x) / scale, (mousePt.y - origin.y) / scale);
    }

    private void drag(MouseEvent e) {
        double dx = ((e.getX() - mousePt.x));
        double dy = ((e.getY() - mousePt.y));
        origin.setLocation(oldOrigin.x + dx, oldOrigin.y + dy);
        viewPort.x = (originalViewPort.x - origin.x) / scale;
        viewPort.y = (originalViewPort.y - origin.y) / scale;
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
        origin.x = a.x - (a.x - origin.x) * z;
        origin.y = a.y - (a.y - origin.y) * z;

        centerViewPort();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = g2d.getTransform();

        g2d.translate(origin.x, origin.y);
        g2d.scale(scale, scale);

        long s = System.nanoTime();

        g2d.setColor(Color.white);
        qtree.draw(g2d, viewPort);
//        drawTree(g2d, qtree);
        g2d.setColor(Color.green);
        g2d.draw(viewPort);

        long e = System.nanoTime();

        g2d.setTransform(at);

        g2d.setColor(Color.white);
        g2d.drawString("draw time: " + (e - s) * 1.0 / 1_000_000 + "ms", 0, 30);
        g2d.drawString("update time: " + updateTime + "ms", 0, 60);
        g2d.drawString("zoom: " + scale, 0, 90);
        g2d.dispose();
    }

    public void update() {
        long start = System.nanoTime();

        updatePosition();
        updateTree();
//        collideDectection();

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
            if (viewPort.contains(ps)) {
                newQuadTree.insert(ps);
            }
        }
        qtree = newQuadTree;
    }

    private void collideDectection() {
        ArrayList<MovingPoint> newCollideds = new ArrayList<>(n / 2);
        for (MovingPoint p : points) {
            if (viewPort.contains(p)) {
                ArrayList<MovingPoint> founds = new ArrayList<>();
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
        if (!tree.getBoundary().intersects(viewPort)) {
            return;
        }
        g2d.setColor(Color.white);
        g2d.draw(tree.getBoundary());
        for (int i = 0; i < tree.getPointsIndex(); i++) {
            MovingPoint mp = (MovingPoint) tree.get(i);
            if (collideds.contains(mp)) {
                g2d.setColor(Color.white);
            } else {
                g2d.setColor(Color.green);
            }
            g2d.drawLine((int) mp.x, (int) mp.y, (int) mp.x, (int) mp.y);
            g2d.draw(mp.collisionBox);
        }
        if (tree.isDivided()) {
            drawTree(g2d, tree.ne);
            drawTree(g2d, tree.nw);
            drawTree(g2d, tree.se);
            drawTree(g2d, tree.sw);
        }
    }

}
