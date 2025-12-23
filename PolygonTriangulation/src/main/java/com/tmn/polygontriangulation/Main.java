package com.tmn.polygontriangulation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    static class Panel extends JPanel {

        private final JPanel panel = this;
        private int width, height;
        private final Point.Double origin = new Point.Double();
        private final Point.Double oldOrigin = new Point.Double();
        private final Point.Double mousePt = new Point.Double(), mouseWorld = new Point.Double();
        private double scale = 1, zoomFactor = 1.05, inversedZoomFactor = 1 / zoomFactor, minScale = 0.15, maxScale = 15.3674;

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
                        for (Point2D t : ps) {
                            if (t.distance(mouseWorld) < 10) {
                                p = t;
                                cp = new Point2D.Double(t.getX(), t.getY());
                                break;
                            }
                        }
                        if (p == null) {
                            cp = null;
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    p = null;
                    cp = null;
                }

            });

            this.addMouseMotionListener(new MouseMotionAdapter() {
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

                    if (c == KeyEvent.VK_ESCAPE) {
                        resetTransform();
                    }

                    if (c == KeyEvent.VK_SPACE) {
                        PolygonUtils.doTriangulation = !PolygonUtils.doTriangulation;
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
            init();
        }

        private void resetTransform() {
            scale = 1;
            oldOrigin.setLocation(0, 0);
            origin.setLocation(0, 0);
            mousePt.setLocation(0, 0);
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
            if (p != null) {
//                p.setLocation((cp.getX() + dx / scale), (cp.getY() + dy / scale));
                p.setLocation((int) (cp.getX() + dx / scale), (int) (cp.getY() + dy / scale));
            } else {
                origin.setLocation(oldOrigin.getX() + dx, oldOrigin.getY() + dy);
            }
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
        }

        private void init() {
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform saveAT = g2d.getTransform();
            g2d.translate(origin.x, origin.y);
            g2d.scale(scale, scale);
            g2d.setColor(Color.white);

            Line2D l = new Line2D.Double();

            for (int i = 0; i < ps.length; i++) {
                Point2D p = ps[i];
                Point2D p1 = ps[(i + 1) % ps.length];
                l.setLine(p, p);
                g2d.draw(l);
                g2d.drawString(i + " " + p.getX() + " " + p.getY(), (int) p.getX(), (int) p.getY());
            }

            l.setLine(mouseWorld, mouseWorld);
            g2d.draw(l);

//            drawCircle(g2d, circumcircle);
            long start = System.nanoTime();
            Rectangle2D rect = PolygonUtils.getMinRect(ps);
            g2d.draw(rect);
            Path2D path = PolygonUtils.getCoveredTrianglePath(rect);
            g2d.draw(path);
            long end = System.nanoTime();

            for (DelaunayTriangulation.Triangle t : delaunayTriangles) {
                if (t.contains(mouseWorld)) {
                    g2d.setColor(Color.green);
                    t.fill(g2d);
                    double[] d = PolygonUtils.circumcircle(t.a, t.b, t.c);
                    double cx = d[0];
                    double cy = d[1];
                    double cr = d[2];
                    Ellipse2D ellipse2D = new Ellipse2D.Double(cx - cr, cy - cr, 2 * cr, 2 * cr);
                    g2d.draw(ellipse2D);
                } else {
                    g2d.setColor(Color.red);
                    t.draw(g2d);
                }
            }
            start = System.nanoTime();
            cont = delaunay.triangulate0(ps);
            end = System.nanoTime();
            g2d.translate(1, 1);
            double[] d = new double[3];
            for (int i = 0; i < cont.size();) {
                Point2D a = cont.get(i++);
                Point2D b = cont.get(i++);
                Point2D c = cont.get(i++);
                path.reset();
                path.moveTo(a.getX(), a.getY());
                path.lineTo(b.getX(), b.getY());
                path.lineTo(c.getX(), c.getY());
                path.closePath();
                if (PolygonUtils.isPointInTriangle(mouseWorld, a, b, c)) {
                    PolygonUtils.circumcircle(a, b, c, d);
                    double cx = d[0];
                    double cy = d[1];
                    double cr = d[2];
                    Ellipse2D ellipse2D = new Ellipse2D.Double(cx - cr, cy - cr, 2 * cr, 2 * cr);
                    g2d.draw(ellipse2D);
                    g2d.setColor(Color.green);
                    g2d.fill(path);
                } else {
                    g2d.setColor(Color.white);
                    g2d.draw(path);
                }
            }
            g2d.setTransform(saveAT);

            g2d.setStroke(new BasicStroke(5));

            l.setLine(origin, origin);
            g2d.draw(l);

            g2d.drawString(triangulationTime + "", 0, 50);
            g2d.drawString((end - start) * 1.0 / 1000000 + "", 0, 90);
        }

        Point2D p, cp;
//        Point2D[] ps = new Point2D[]{
//            new Point2D.Double(10, 10),
//            new Point2D.Double(100, 0),
//            new Point2D.Double(30, 70),
//            new Point2D.Double(-15, 50),
//            new Point2D.Double(80, 40),
//            new Point2D.Double(100, 100),
//            new Point2D.Double(792, 922), //
//        };
        Point2D[] ps = PolygonUtils.SimplePolygon.generateRandom(1500, 0, 2000);
        double triangulationTime = 0;
        double[] circumcircle = new double[3];

        BruteforceDelaunayTriangulation bdt = new BruteforceDelaunayTriangulation();
        BathlamosDelaunayTriangulation delaunay = new BathlamosDelaunayTriangulation();
        List<DelaunayTriangulation.Triangle> delaunayTriangles = new ArrayList<>();

        List<Point2D> cont = new ArrayList<>();

        public void update() {
            long start = System.nanoTime();
//            delaunayTriangles = bdt.triangulate(ps);
//            delaunay.triangulate(ps);
            long end = System.nanoTime();
            triangulationTime = (end - start) * 1.0 / 1000000;

        }

        public void drawCircle(Graphics2D g2d, double[] d) {
            double cx = circumcircle[0];
            double cy = circumcircle[1];
            double cr = circumcircle[2];
            g2d.setColor(Color.green);
            Line2D l = new Line2D.Double(cx, cy, cx, cy);
            g2d.draw(l);
            Ellipse2D ellipse2D = new Ellipse2D.Double(cx - cr, cy - cr, 2 * cr, 2 * cr);
            g2d.draw(ellipse2D);
        }

        public void setWidth(int width) {
            this.width = width;
            this.setPreferredSize(new Dimension(width, height));
        }

        public void setHeight(int height) {
            this.height = height;
            this.setPreferredSize(new Dimension(width, height));
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Panel p = new Panel(800, 500);
        Frame f = new Frame();
        f.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                p.requestFocusInWindow();
            }
        });
        f.add(p);
        f.pack();
        f.setLocationRelativeTo(null);
        Thread drawThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 60;
            long lastTime = System.nanoTime();
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    f.repaint();
                    delta--;
                }
            }
        });
        Thread updateThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 60;
            long lastTime = System.nanoTime();
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    p.update();
                    delta--;
                }
            }
        });
        System.out.println("Hold left mouse button and drag to move around");
        System.out.println("Scroll mouse wheel to zoom. Hold CTRL to increase zoom speed");
        drawThread.join();
        updateThread.join();
    }

    static class Frame extends JFrame {

        public Frame() {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setVisible(true);
            this.setEnabled(true);
        }
    }
}
