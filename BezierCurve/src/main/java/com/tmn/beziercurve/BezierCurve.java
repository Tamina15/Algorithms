package com.tmn.beziercurve;

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
import java.util.Random;
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
                        if (curveIndex == -1) {
                            p = null;
                            cp = null;
                        }
                    }
                    if (SwingUtilities.isRightMouseButton(e)) {
                        path.toggleLoop();
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
                    if (c == KeyEvent.VK_SHIFT) {
                        path.toggleWindingRule();
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
                path.newPath();
                cubic.setCurve(ps, 0);
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
            ps = new MyPoint[20];
            Random r = new Random();
            for (int i = 0; i < ps.length; i++) {
                ps[i] = new MyPoint(r.nextInt(width), r.nextInt(height));
            }
            cubic.setCurve(ps, 0);
            path.setPoints(ps);
            path.closeLoop();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform saveAT = g2d.getTransform();
            g2d.translate(origin.x, origin.y);
            g2d.scale(scale, scale);

            g2d.setColor(Color.white);

            g2d.fill(path);

            g2d.setColor(Color.red);
            for (int i = 0; i < ps.length; i++) {
                g2d.draw(ps[i]);
                g2d.drawString(i + "", (int) ps[i].x, (int) ps[i].y);
            }

            g2d.setStroke(new BasicStroke(1, 1, 1));
            g2d.setTransform(saveAT);

            g2d.setColor(Color.white);

            g2d.drawString(path.isOpenLoop() ? "Open" : "Close", 0, 10);

            for (int i = 0; i < ps.length; i++) {
                g2d.drawString(ps[i].toString(), 0, i * 20 + 20);
            }
        }

        MyPoint p, cp = new MyPoint();
        int curveIndex = -1;
        MyPoint[] ps = new MyPoint[]{
            new MyPoint(100, 50),
            new MyPoint(150, 200),
            new MyPoint(390, 150),
            new MyPoint(300, 450),
            new MyPoint(410, 450),
            new MyPoint(500, 150),
            new MyPoint(600, 100)
        };

        BezierSpline path = new BezierSpline(ps);
        CubicCurve2D cubic = new CubicCurve2D.Double();
        double flatten = 0.015;
        int count = 0;

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
