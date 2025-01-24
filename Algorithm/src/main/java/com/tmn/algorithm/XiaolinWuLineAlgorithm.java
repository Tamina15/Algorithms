/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tmn.algorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author HP
 * @author Tamina
 */
public class XiaolinWuLineAlgorithm {

    public static void main(String[] args) {
        Frame frame = new Frame();
    }

    static class Panel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

        int panelWidth = 800;
        int panelHeight = 800;
        // Zomming
        Double zoomFactor = 1.0d;
        boolean zoomer;
        AffineTransform at;

        public Panel() {
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setDoubleBuffered(true);
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addMouseListener(this);
            this.addMouseWheelListener(this);
            this.addMouseMotionListener(this);
        }
        int count = 0;

        public void plot(Graphics2D g2d, double x, double y, double alpha) {
            g2d.setColor(new Color(255, 255, 255, (int) alpha));
            g2d.drawLine((int) x, (int) y, (int) x, (int) y);
            if (count == 0) {
                System.out.println((int) x + " " + (int) y + " " + (int) alpha);
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

        public void drawLine(double x0, double y0, double x1, double y1, Graphics2D g2d) {
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

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            // Zooming
            if (zoomer == true) {
                at = new AffineTransform();
                at.scale(zoomFactor, zoomFactor);
                //zoomer = false;
                g2d.transform(at);
            }
            ////

            // Dragging
            g2d.translate(oX, oY);
            ///////////

//            drawLine(0, 0, panelWidth / 2, panelHeight, g2d);
//            drawLine(0, 0, 100, 100, g2d);
//            drawLine(0, 0, 200, 200, g2d);
//            drawLine(0, 0, 300, 300, g2d);
//            drawLine(0, 0, 400, 400, g2d);
            drawLine(0, 0, 500, 500, g2d);
            g2d.dispose();
            g.dispose();
        }

        private void update() {
        }

        public Double getZoomFactor() {
            return zoomFactor;
        }

        public void setZoomFactor(double factor) {
            if (factor < this.zoomFactor) {
                this.zoomFactor = this.zoomFactor / 1.1;
            } else {
                this.zoomFactor = factor;
            }
            this.zoomer = true;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }
        int startX = 0;
        int startY = 0;
        int offsetX = 0;
        int offsetY = 0;
        int oX;
        int oY;
        boolean press = false;

        @Override
        public void mouseReleased(MouseEvent e) {
            press = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Zoom in
            if (e.getWheelRotation() < 0) {
                this.setZoomFactor(1.1 * this.getZoomFactor());

            }
            //Zoom out
            if (e.getWheelRotation() > 0) {
                if (this.getZoomFactor() / 1.1 > 1) {
                    this.setZoomFactor(this.getZoomFactor() / 1.1);
                } else {
                    this.setZoomFactor(1);
                }
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!press) {
                offsetX = oX;
                offsetY = oY;
                startX = e.getXOnScreen();
                startY = e.getYOnScreen();
                press = true;
            }
            oX = e.getXOnScreen() - startX + offsetX;
            oY = e.getYOnScreen() - startY + offsetY;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

    }

    static class Frame extends JFrame implements Runnable {

        Thread thread;
        public final int FPS = 60;
        public static String FPScount = "NaN";
        public static double delta = 0;
        public static boolean updating;

        Panel panel;

        Frame() {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new Panel();
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
                    panel.update();
                    count = 0;
                    timer = 0;
                    updating = false;
                }
            }
        }
    }
}
