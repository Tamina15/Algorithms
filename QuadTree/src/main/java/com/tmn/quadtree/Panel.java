package com.tmn.quadtree;

import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends NPanel {

    private final int viewPortBuffer = 150;
    private Rectangle.Double originalViewPort, viewPort;

    Random random = new Random();
    int n = 150000;
    Rectangle.Double boundary;
    QuadTree qtree;

    ArrayList<MovingPoint> points;
    ArrayList<MovingPoint> collideds;
    double collisionLength;
    double updateTime;

    public Panel(int width, int height) {
        super(width, height);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    setZoomFactor(1.15);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    setZoomFactor(1.05);
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

                    originX += half_dx;
                    originY += half_dy;

                    centerViewPort();
                }
            }
        });

        newViewPort();

        boundary = new Rectangle.Double(0, 0, width * 30, height * 30);
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
        double scale = getScale();
        viewPort.width = originalViewPort.width / scale;
        viewPort.height = originalViewPort.height / scale;
        viewPort.x = (originalViewPort.x - originX) / scale;
        viewPort.y = (originalViewPort.y - originY) / scale;
    }

    @Override
    protected void drag(MouseEvent e) {
        super.drag(e);
        double scale = getScale();
        viewPort.x = (originalViewPort.x - originX) / scale;
        viewPort.y = (originalViewPort.y - originY) / scale;
    }

    @Override
    protected void zoom(MouseWheelEvent e) {
        super.zoom(e);
        centerViewPort();
    }

    private long s, e;

    @Override
    public void draw(Graphics2D g2d) {
        s = System.nanoTime();

        g2d.setColor(Color.white);
        qtree.draw(g2d, viewPort);
//        drawTree(g2d, qtree);
        g2d.setColor(Color.green);
        g2d.draw(viewPort);

        e = System.nanoTime();

    }

    @Override
    public void drawOriginal(Graphics2D g2d) {
        g2d.setColor(Color.white);
        double drawTime = (e - s) * 1.0 / 1_000_000;
        g2d.drawString("draw time: " + drawTime + "ms", 0, 30);
        g2d.drawString("update time: " + updateTime + "ms", 0, 60);
        g2d.drawString("zoom: " + getScale(), 0, 90);
        g2d.drawString("FPS: " + 1000.0 / drawTime, 0, 120);
        g2d.drawString("UPS: " + ups, 0, 150);
        g2d.dispose();
    }
    double ups;

    public void update(double fps, double ups) {
        this.ups = ups;
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
            if (viewPort.contains(ps)) {
                newQuadTree.insert(ps);
            }
        }
        qtree = newQuadTree;
    }

    private void collideDectection() {
        ArrayList<MovingPoint> newCollideds = new ArrayList<>(n / 2);
        for (MovingPoint p : points) {
//            if (viewPort.contains(p)) {
            ArrayList<MovingPoint> founds = new ArrayList<>();
            qtree.query(p.range, founds);
            for (int i = 0; i < founds.size(); i++) {
                MovingPoint f = founds.get(i);
                if (p != f && p.collide(f)) {
                    newCollideds.add(f);
                }
//                }
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
