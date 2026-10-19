package com.tmn.polygontriangulation;

import com.tmn.graphic.NMain;
import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class Main {

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

            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int c = e.getKeyCode();
                    if (c == KeyEvent.VK_CONTROL) {
                        setZoomFactor(1.15);
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
                        setZoomFactor(1.05);
                    }
                }

            });
        }

        protected void drag(MouseEvent e) {
            super.drag(e);
            double dx = ((e.getX() - anchorX));
            double dy = ((e.getY() - anchorY));
            if (p != null) {
                p.setLocation((int) (cp.getX() + dx / getScale()), (int) (cp.getY() + dy / getScale()));
                originX = oldOriginX;
                originY = oldOriginY;
            }
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.setColor(Color.white);
            Line2D l = new Line2D.Double();
            for (int i = 0; i < ps.length; i++) {
                Point2D p = ps[i];
                Point2D p1 = ps[(i + 1) % ps.length];
                l.setLine(p, p);
                g2d.draw(l);
                g2d.drawString(i + " " + p.getX() + " " + p.getY(), (int) p.getX(), (int) p.getY());
            }

            l.setLine(mouseWorldX, mouseWorldY, mouseWorldX, mouseWorldY);
            g2d.draw(l);

//            drawCircle(g2d, circumcircle);
            long start = System.nanoTime();
            Rectangle2D rect = PolygonUtils.getMinRect(ps);
            g2d.draw(rect);
            Path2D path = PolygonUtils.getCoveredTrianglePath(rect);
            g2d.draw(path);
            long end = System.nanoTime();

            for (DelaunayTriangulation.Triangle t : delaunayTriangles) {
                if (t.contains(mouseWorldX, mouseWorldY)) {
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
                if (PolygonUtils.isPointInTriangle(new Point2D.Double(mouseWorldX, mouseWorldY), a, b, c)) {
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
        NMain main = new NMain(new Panel(800, 600));
        main.run();
    }
}
