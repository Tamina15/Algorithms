/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tmn.algorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author HP
 * @author Tamina
 */
public class XiaolinWuLineAlgorithm {

    static class Panel extends JPanel {

        public int width, height;
        double originX, originY, oldOriginX, oldOriginY, anchorX, anchorY, mouseWorldX, mouseWorldY, currentMouseX, currentMouseY;
        private double scale = 1;
        double zoomFactor = 1.05, inversedZoomFactor = 1 / zoomFactor, minScale = 0.15, maxScale = 15.3674;

        public Panel(int width, int height) {
            super();
            this.width = width;
            this.height = height;
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
            setFocusable(true);
            addMouseListeners();
        }

        private void addMouseListeners() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        setAnchorPoint(e);
                        if (getPointOnClick(pointA) || getPointOnClick(pointB)) {
                            System.out.println(p);
                            return;
                        }
                        p = null;
                        cp = null;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    p = null;
                    cp = null;
                }

            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    currentMouse(e);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        drag(e);
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    currentMouse(e);
                    move(e);
                }
            });

            addMouseWheelListener((MouseWheelEvent e) -> zoom(e));
        }

        private void currentMouse(MouseEvent e) {
            currentMouseX = e.getX();
            currentMouseY = e.getY();
        }

        protected void setAnchorPoint(MouseEvent e) {
            oldOriginX = originX;
            oldOriginY = originY;

            anchorX = e.getX();
            anchorY = e.getY();
        }

        protected void move(MouseEvent e) {
            mouseWorldX = (currentMouseX - originX) / scale;
            mouseWorldY = (currentMouseY - originY) / scale;
        }

        protected void drag(MouseEvent e) {
            double dx = ((e.getX() - anchorX));
            double dy = ((e.getY() - anchorY));
            if (p != null) {
                p.setLocation((cp.getX() + dx / scale), (cp.getY() + dy / scale));
                originX = oldOriginX;
                originY = oldOriginY;
            } else {
                originX = oldOriginX + dx;
                originY = oldOriginY + dy;
            }
        }

        protected void zoom(MouseWheelEvent e) {
            double oldScale = scale;

            if (e.getWheelRotation() < 0) {
                scale *= zoomFactor;
            } else if (e.getWheelRotation() > 0) {
                scale *= inversedZoomFactor;
            }

            if (Math.abs(scale - 1) < 0.04) {
                scale = 1;
            } else {
                scale = Math.clamp(scale, minScale, maxScale);
            }

            if (scale == oldScale) {
                return;
            }

            double x = e.getX();
            double y = e.getY();

            // World position currently under the mouse
            double worldX = (x - originX) / oldScale;
            double worldY = (y - originY) / oldScale;

            // Move origin so the same world point stays under the mouse
            originX = x - worldX * scale;
            originY = y - worldY * scale;
        }

        boolean getPointOnClick(Point2D point) {
            if (point.distance(mouseWorldX, mouseWorldY) < 10) {
                p = point;
                cp = new Point2D.Double(point.getX(), point.getY());
                return true;
            }
            return false;
        }

        int count = 0;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform at = g2d.getTransform();
            g2d.translate(originX, originY);
            g2d.scale(scale, scale);

            drawLine(pointA, pointB, g2d);

            // Restore the original AffineTransform
            g2d.setTransform(at);
        }
        Point2D p, cp;
        Point2D pointA = new Point2D.Double(150, 150);
        Point2D pointB = new Point2D.Double(300, 300);

        public void drawLine(Point2D a, Point2D b, Graphics2D g2d) {
            double x0 = a.getX(), y0 = a.getY(), x1 = b.getX(), y1 = b.getY();

            boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
            if (steep) {
                //swap(x0, y0)
                double t0 = x0;
                x0 = y0;
                y0 = t0;
                //swap(x1, y1)
                double t1 = x1;
                x1 = y1;
                y1 = t1;
            }
            if (x0 > x1) {
//                swap(x0, x1)
                double t0 = x0;
                x0 = x1;
                x1 = t0;
//                swap(y0, y1)
                double t1 = y0;
                y0 = y1;
                y1 = t1;
            }
            double dx = x1 - x0;
            double dy = y1 - y0;
            double gradient;

            if (dx == 0) {
                gradient = 1;
            } else {
                gradient = dy / dx;
            }
            double xend = round(x0);
            double yend = y0 + gradient * (xend - x0);
            double xgap = rfpart(x0 + 0.5);
            double xpxl1 = xend;
            double ypxl1 = ipart(yend);

            if (steep) {
                plot(g2d, ypxl1, xpxl1, rfpart(yend) * xgap);
                plot(g2d, ypxl1 + 1, xpxl1, fpart(yend) * xgap);
            } else {
                plot(g2d, xpxl1, ypxl1, rfpart(yend) * xgap);
                plot(g2d, xpxl1, ypxl1 + 1, fpart(yend) * xgap);
            }
            double intery = yend + gradient;
            xend = round(x1);
            yend = y1 + gradient * (xend - x1);
            xgap = fpart(x1 + 0.5);
            double xpxl2 = xend; //this will be used in the main loop
            double ypxl2 = ipart(yend);
            if (steep) {
                plot(g2d, ypxl2, xpxl2, rfpart(yend) * xgap);
                plot(g2d, ypxl2 + 1, xpxl2, fpart(yend) * xgap);
            } else {
                plot(g2d, xpxl2, ypxl2, rfpart(yend) * xgap);
                plot(g2d, xpxl2, ypxl2 + 1, fpart(yend) * xgap);
            }
            if (steep) {
                for (double x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
                    plot(g2d, ipart(intery), x, rfpart(intery));
                    plot(g2d, ipart(intery) + 1, x, fpart(intery));
                    intery += gradient;
                }
            } else {
                for (double x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
                    plot(g2d, x, ipart(intery), rfpart(intery));
                    plot(g2d, x, ipart(intery) + 1, fpart(intery));
                    intery += gradient;
                }
            }
            count = 1;
        }

        public void plot(Graphics2D g2d, double x, double y, double alpha) {
            int c = (int) (alpha * 255);
            g2d.setColor(new Color(c, c, c));
//            g2d.setColor(Color.white);
            g2d.drawLine((int) x, (int) y, (int) x, (int) y);
            if (count == 0) {
                System.out.println(x + " " + y + " " + alpha);
            }
        }

        public double ipart(double x) {
            return Math.floor(x);
        }

        public double round(double x) {
            return ipart(x + 0.5d);
        }

        public double fpart(double x) {
            return x - ipart(x);
        }

        public double rfpart(double x) {
            return 1 - fpart(x);
        }

        public void update() {
        }

    }

    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setEnabled(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running.set(false);
                frame.dispose();
            }
        });
        Panel panel = new Panel(800, 600);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);

        Thread drawThread = Thread.startVirtualThread(() -> {
            final long frameTime = 1_000_000_000L / 60;
            long next = System.nanoTime();
            while (running.get()) {
                SwingUtilities.invokeLater(frame::repaint);
                next += frameTime;
                long sleep = next - System.nanoTime();
                if (sleep > 0) {
                    LockSupport.parkNanos(sleep);
                }
            }
        });

        Thread updateThread = Thread.startVirtualThread(() -> {
            final long interval = 1_000_000_000L / 60;
            long previous = System.nanoTime();
            long accumulator = 0;

            long next = System.nanoTime();

            while (running.get()) {
                long now = System.nanoTime();
                accumulator += now - previous;
                previous = now;
                while (accumulator >= interval) {
                    panel.update();
                }

                next += interval;
                while (true) {
                    long remaining = next - System.nanoTime();

                    if (remaining <= 0) {
                        break;
                    }

                    if (remaining > 1_000_000) {
                        LockSupport.parkNanos(remaining - 500_000);
                    } else {
                        Thread.onSpinWait();
                    }
                }
            }
        });
        try {
            drawThread.join();
            updateThread.join();
        } catch (InterruptedException ex) {
        }
        System.out.println("Main");
    }
}
