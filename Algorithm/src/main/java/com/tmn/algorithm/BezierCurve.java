package com.tmn.algorithm;

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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class BezierCurve {

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
                        for (MyPoint t : ps) {
                            if (t.distance(mouseWorld) < 10) {
                                p = t;
                                cp = new MyPoint(t);
                                curveIndex = 1;
                                break;
                            }
                        }
//                        if (curveIndex == -1) {
//                            for (MyPoint t : ps2) {
//                                if (t.distance(mouseWorld) < 10) {
//                                    p = t;
//                                    cp = new MyPoint(t);
//                                    curveIndex = 2;
//                                    break;
//                                }
//                            }
//                        }
                        if (curveIndex == -1) {
                            p = null;
                            cp = null;
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    curveIndex = -1;
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
                    if (c == KeyEvent.VK_Q) {
                        flatten += 0.01;
                    }
                    if (c == KeyEvent.VK_A) {
                        flatten -= 0.01;
                        if (flatten <= 0.001) {
                            flatten = 0.01;
                        }
                    }
                    if (c == KeyEvent.VK_ESCAPE) {
                        resetTransform();
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
                p.setLocation((cp.x + dx / scale), (cp.y + dy / scale));
                curve.setCurve(ps, 0);
//                curve2.setCurve(ps2, 0);
                curve2.setCurve(ps, 3);
                getCubicPath(curve2, list);
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
            list = new ArrayList<>(20);
            getCubicPath(curve2, list);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform saveAT = g2d.getTransform();

            g2d.translate(origin.x, origin.y);
            g2d.scale(scale, scale);

            g2d.setColor(Color.white);
//            g2d.setStroke(new BasicStroke(1, 1, 1));

            g2d.draw(curve);
            g2d.draw(curve2);

            g2d.setColor(Color.red);

            drawQuad(g2d, curve);
//            drawCubic(g2d, curve2);
//            g2d.setStroke(new BasicStroke(5, 1, 1));
            Line2D l = new Line2D.Double();

//            g2d.setColor(Color.GREEN);
//            l.setLine(curve.getP1(), curve.getP1());
//            g2d.draw(l);
//
//            g2d.setColor(Color.blue);
//            l.setLine(curve.getP2(), curve.getP2());
//            g2d.draw(l);
//
//            g2d.setColor(Color.MAGENTA);
//            l.setLine(curve.getCtrlP1(), curve.getCtrlP1());
//            g2d.draw(l);
//
//            g2d.setColor(Color.ORANGE);
//            l.setLine(curve.getCtrlP2(), curve.getCtrlP2());
//            g2d.draw(l);
//            g2d.setColor(Color.GREEN);
//            l.setLine(curve2.getP1(), curve2.getP1());
//            g2d.draw(l);
//
//            g2d.setColor(Color.blue);
//            l.setLine(curve2.getP2(), curve2.getP2());
//            g2d.draw(l);
//            g2d.setColor(Color.MAGENTA);
//            l.setLine(curve2.getCtrlP1(), curve2.getCtrlP1());
//            g2d.draw(l);
//
//            g2d.setColor(Color.ORANGE);
//            l.setLine(curve2.getCtrlP2(), curve2.getCtrlP2());
//            g2d.draw(l);
            g2d.setStroke(new BasicStroke(1, 1, 1));
            g2d.setTransform(saveAT);

            g2d.setColor(Color.white);

            g2d.drawString(flatten + " " + count, 0, 20);
            for (int i = 0; i < ps.length; i++) {
                g2d.drawString(ps[i] + "", 0, i * 20 + 40);
            }

        }

        MyPoint p, cp = new MyPoint();
        int curveIndex = -1;
//        MyPoint[] ps = new MyPoint[]{new MyPoint(100, 50), new MyPoint(300, 200), new MyPoint(400, 150)};
//        MyPoint[] ps = new MyPoint[]{new MyPoint(100, 50), new MyPoint(300, 200), new MyPoint(400, 150), new MyPoint(400, 450)};
//        CubicCurve2D curve = new CubicCurve2D.Double(ps[0].x, ps[0].y, ps[1].x, ps[1].y, ps[2].x, ps[2].y, ps[3].x, ps[3].y);
//        MyPoint[] ps2 = new MyPoint[]{new MyPoint(100, 100), new MyPoint(200, 200), new MyPoint(400, 200), new MyPoint(300, 100)};
//        CubicCurve2D curve2 = new CubicCurve2D.Double(ps2[0].x, ps2[0].y, ps2[1].x, ps2[1].y, ps2[2].x, ps2[2].y, ps2[3].x, ps2[3].y);

        MyPoint[] ps = new MyPoint[]{
            new MyPoint(100, 50),
            new MyPoint(150, 200),
            new MyPoint(390, 450),
            new MyPoint(400, 450),
            new MyPoint(410, 450),
            new MyPoint(500, 150),
            new MyPoint(600, 100)
        };

        QuadCurve2D curve = new QuadCurve2D.Double(ps[0].x, ps[0].y, ps[1].x, ps[1].y, ps[2].x, ps[2].y);
//        CubicCurve2D curve = new CubicCurve2D.Double(ps[0].x, ps[0].y, ps[1].x, ps[1].y, ps[2].x, ps[2].y, ps[3].x, ps[3].y);
        CubicCurve2D curve2 = new CubicCurve2D.Double(ps[3].x, ps[3].y, ps[4].x, ps[4].y, ps[5].x, ps[5].y, ps[6].x, ps[6].y);

        double flatten = 0.015;
        int count = 0;
        ArrayList<MyPoint> list;

        private void drawQuad(Graphics2D g2d, QuadCurve2D curve) {
            count = 0;
            PathIterator iterator = curve.getPathIterator(null, flatten);
            double x0 = curve.getX1(), y0 = curve.getY1();
            double[] d = new double[6];
            while (!iterator.isDone()) {
                count++;
                int type = iterator.currentSegment(d);
                switch (type) {
                    case PathIterator.SEG_LINETO -> {
                        double x1 = d[0];
                        double y1 = d[1];
                        g2d.draw(new Line2D.Double(x0, y0, x1, y1));
                        x0 = x1;
                        y0 = y1;
                    }
                    default -> {
                    }
                }
                iterator.next();
            }
        }

        private void drawCubic(Graphics2D g2d, CubicCurve2D curve) {
            PathIterator iterator = curve.getPathIterator(null, flatten);
            double x0 = curve.getX1(), y0 = curve.getY1();
            double[] d = new double[6];
            while (!iterator.isDone()) {
                int type = iterator.currentSegment(d);
                switch (type) {
                    case PathIterator.SEG_LINETO -> {
                        double x1 = d[0];
                        double y1 = d[1];
                        g2d.draw(new Line2D.Double(x0, y0, x1, y1));
                        x0 = x1;
                        y0 = y1;
                    }
                    default -> {
                    }
                }
                iterator.next();
            }
        }

        private void getCubicPath(CubicCurve2D curve, ArrayList<MyPoint> list) {
            list.clear();
            PathIterator iterator = curve.getPathIterator(null, flatten);
            double x0 = curve.getX1(), y0 = curve.getY1();
            list.add(new MyPoint(x0, y0));
            double[] d = new double[6];
            while (!iterator.isDone()) {
                int type = iterator.currentSegment(d);
                switch (type) {
                    case PathIterator.SEG_LINETO -> {
                        double x1 = d[0];
                        double y1 = d[1];
                        list.add(new MyPoint(x1, y1));
                    }
                    default -> {
                    }
                }
                iterator.next();
            }
        }

        public void update() {

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
