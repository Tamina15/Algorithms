package com.tmn.beziercurve;

import com.tmn.graphic.NMain;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.SwingUtilities;
import com.tmn.graphic.NPanel;

public class BezierCurve {

    static class Panel extends NPanel {

        public Panel(int width, int height) {
            super(width, height);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        for (Point2D t : ps) {
                            if (t.distance(mouseWorldX, mouseWorldY) < 10) {
                                p = t;
                                cp = new Point2D.Double(t.getX(), t.getY());
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

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int c = e.getKeyCode();
                    switch (c) {
                        case KeyEvent.VK_CONTROL -> {
                            setZoomFactor(1.15);
                        }
                        case KeyEvent.VK_Q -> {
                            flatten += 0.01;
                        }
                        case KeyEvent.VK_A -> {
                            flatten -= 0.01;
                            if (flatten <= 0.001) {
                                flatten = 0.01;
                            }
                        }
                        case KeyEvent.VK_ESCAPE -> {
                            resetTransform();
                        }
                        case KeyEvent.VK_SHIFT -> {
                            path.toggleWindingRule();
                        }
                        case KeyEvent.VK_Z -> {
                            fill = !fill;
                        }
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

            init();
        }

        @Override
        protected void drag(MouseEvent e) {
            super.drag(e);
            double dx = ((e.getX() - anchorX));
            double dy = ((e.getY() - anchorY));
            if (p != null) {
                p.setLocation((cp.getX() + dx / getScale()), (cp.getY() + dy / getScale()));
                path.newPath();
                cubic.setCurve(ps, 0);
                originX = oldOriginX;
                originY = oldOriginY;
            }
        }

        private void init() {
            ps = new Point2D[10];
            for (int i = 0; i < ps.length; i++) {
                ps[i] = new Point2D.Double(r.nextInt(width), r.nextInt(height));
            }
            cubic.setCurve(ps, 0);
            path = new BezierSpline(ps);
            angle = new double[ps.length];
            turn = new double[ps.length];
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.setColor(Color.white);
            if (fill) {
                g2d.fill(path);
            } else {
                g2d.draw(path);
            }
            g2d.setColor(Color.red);
            for (int i = 0; i < ps.length; i++) {
                drawPoint(g2d, ps[i]);
                g2d.drawString(i + "", (int) ps[i].getX(), (int) ps[i].getY());
            }
        }

        @Override
        public void drawOriginal(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(1, 1, 1));
            g2d.setColor(Color.white);
            g2d.drawString(path.isOpenLoop() ? "Open" : "Close", 0, 10);
            for (int i = 0; i < ps.length; i++) {
                g2d.drawString(ps[i].toString(), 0, i * 20 + 20);
            }
        }

        private final Line2D cacheLine = new Line2D.Double();

        private void drawPoint(Graphics2D g, Point2D p) {
            cacheLine.setLine(p, p);
            g.draw(cacheLine);
        }

        Point2D p, cp = new Point2D.Double();
        int curveIndex = -1;
        Point2D[] ps;
//        = new Point2D[]{
//            new Point2D.Double(100, 50),
//            new Point2D.Double(150, 200),
//            new Point2D.Double(390, 150),
//            new Point2D.Double(300, 450),
//            new Point2D.Double(410, 450),
//            new Point2D.Double(500, 150),
//            new Point2D.Double(600, 100)
//        };

        BezierSpline path;
        CubicCurve2D cubic = new CubicCurve2D.Double();
        double flatten = 0.015;
        Random r = new Random();
        double dt = 0.016;
        double[] angle, turn;
        boolean fill;

        @Override
        public void update() {
            for (int i = 0; i < ps.length; i++) {
                turn[i] += (Math.random() - 0.5) * 0.01;
                turn[i] = Math.clamp(turn[i], -0.05, 0.05);

                angle[i] += turn[i];
                var point = ps[i];
                ps[i].setLocation(point.getX() + Math.cos(angle[i]) * 2, point.getY() + Math.sin(angle[i]) * 2);
            }
            path.newPath();
        }

    }

    public static void main(String[] args) {
        NMain main = new NMain(new Panel(800, 600));
        main.run();
    }
}
