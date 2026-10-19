package com.tmn.opensimplexnoise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class OpenSimplexViewer extends JFrame {

    public OpenSimplexViewer() {

        GridPanel panel = new GridPanel();

        JSlider slider = new JSlider(-500, 500, -211);

        JLabel status = new JLabel();

        slider.addChangeListener(e -> {
            panel.stretch = slider.getValue() / 1000.0;
            panel.repaint();
        });

        Timer timer = new Timer(50, e
                -> status.setText(String.format(
                        "Stretch = %.9f (OpenSimplex = -0.211324865)",
                        panel.stretch)));

        timer.start();

        setLayout(new BorderLayout());

        add(slider, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        setTitle("OpenSimplex Skew Viewer");
        setSize(1400, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(()
                -> new OpenSimplexViewer().setVisible(true));
    }

    static class GridPanel extends JPanel
            implements MouseMotionListener,
            MouseListener,
            MouseWheelListener {

        double stretch = -0.211324865;

        double zoom = 100;

        double panX = 700;
        double panY = 500;

        double mouseWorldX;
        double mouseWorldY;

        boolean panning;

        int lastMouseX;
        int lastMouseY;
        int count = 0;

        GridPanel() {

            addMouseMotionListener(this);
            addMouseListener(this);
            addMouseWheelListener(this);

            setFocusable(true);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {

                    if (e.getKeyCode() == KeyEvent.VK_R) {

                        zoom = 100;
                        panX = getWidth() / 2.0;
                        panY = getHeight() / 2.0;

                        repaint();
                    }
                }
            });
        }

        Point2D.Double skew(double x, double y) {

            double k = (x + y) * stretch;

            return new Point2D.Double(
                    x + k,
                    y + k
            );
        }

        Point2D.Double unskew(double xs, double ys) {
            double s = stretch;
            double det = (1.0 + s) * (1.0 + s) - s * s;
            double x = ((1.0 + s) * xs - s * ys) / det;
            double y = (-s * xs + (1.0 + s) * ys) / det;
            return new Point2D.Double(x, y);
        }

        int sx(double x) {
            return (int) Math.round(panX + x * zoom);
        }

        int sy(double y) {
            return (int) Math.round(panY - y * zoom);
        }

        double wx(int screenX) {
            return (screenX - panX) / zoom;
        }

        double wy(int screenY) {
            return (panY - screenY) / zoom;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawGrid(g2);
            drawBasisVectors(g2);
            drawMouseInfo(g2);
        }

        private void drawGrid(Graphics2D g2) {
            double minX = wx(0);
            double maxX = wx(getWidth());

            double minY = wy(getHeight());
            double maxY = wy(0);

            int startX = (int) Math.floor(minX) - 5;
            int endX = (int) Math.ceil(maxX) + 5;

            int startY = (int) Math.floor(minY) - 5;
            int endY = (int) Math.ceil(maxY) + 5;

            g2.setColor(Color.GRAY);

            for (int y = startY; y <= endY; y++) {
                for (int x = startX; x < endX; x++) {
                    Point2D.Double a = skew(x, y);
                    Point2D.Double b = skew(x + 1, y);
                    draw(g2, a, b);
                }
            }

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y < endY; y++) {
                    Point2D.Double a = skew(x, y);
                    Point2D.Double b = skew(x, y + 1);
                    draw(g2, a, b);
                }
            }

            drawLatticePoints(g2, startX, endX, startY, endY);
            highlightSimplex(g2);
            Point2D.Double mouse
                    = skew(mouseWorldX, mouseWorldY);

            drawCrosshair(g2, mouse);
            Point2D.Double world
                    = new Point2D.Double(
                            mouseWorldX,
                            mouseWorldY);

            Point2D.Double skewed
                    = skew(mouseWorldX,
                            mouseWorldY);

            g2.setColor(Color.RED);
            drawCrosshair(g2, skewed);

            g2.setColor(Color.BLUE);

            int wx = sx(world.x);
            int wy = sy(world.y);

            g2.drawOval(wx - 4, wy - 4, 8, 8);
            g2.drawString("Count = " + count++,20, 50);
        }

        private void drawLatticePoints(Graphics2D g2, int startX, int endX, int startY, int endY) {
            g2.setColor(Color.BLACK);
            for (int y = startY; y <= endY; y++) {
                for (int x = startX; x <= endX; x++) {
                    Point2D.Double p = skew(x, y);
                    int px = sx(p.x);
                    int py = sy(p.y);
                    g2.fillOval(px - 2, py - 2, 4, 4);
                    if (zoom > 70) {
                        g2.drawString(x + "," + y, px + 4, py - 4);
                    }
                }
            }
        }

        private void highlightSimplex(Graphics2D g2) {

            double k
                    = (mouseWorldX + mouseWorldY) * stretch;

            double xs = mouseWorldX + k;
            double ys = mouseWorldY + k;

            int xsb = (int) Math.floor(xs);
            int ysb = (int) Math.floor(ys);

            double xins = xs - xsb;
            double yins = ys - ysb;

            Point2D.Double c0 = skew(xsb, ysb);
            Point2D.Double c1;
            Point2D.Double c2;

            if (xins + yins <= 1) {

                c1 = skew(xsb + 1, ysb);
                c2 = skew(xsb, ysb + 1);

            } else {

                c0 = skew(xsb + 1, ysb + 1);
                c1 = skew(xsb + 1, ysb);
                c2 = skew(xsb, ysb + 1);
            }

            Polygon poly = new Polygon();

            poly.addPoint(sx(c0.x), sy(c0.y));
            poly.addPoint(sx(c1.x), sy(c1.y));
            poly.addPoint(sx(c2.x), sy(c2.y));

            g2.setColor(new Color(255, 200, 0, 120));
            g2.fillPolygon(poly);

            g2.setColor(Color.ORANGE);
            g2.drawPolygon(poly);
        }

        private void drawBasisVectors(Graphics2D g2) {

            Point2D.Double origin = skew(0, 0);
            Point2D.Double e1 = skew(1, 0);
            Point2D.Double e2 = skew(0, 1);

            g2.setStroke(new BasicStroke(3));

            g2.setColor(Color.RED);
            draw(g2, origin, e1);

            g2.setColor(Color.BLUE);
            draw(g2, origin, e2);

            g2.setStroke(new BasicStroke(1));

            g2.setColor(Color.RED);
            g2.drawString("e1=(1,0)",
                    sx(e1.x), sy(e1.y));

            g2.setColor(Color.BLUE);
            g2.drawString("e2=(0,1)",
                    sx(e2.x), sy(e2.y));
        }

        private void drawMouseInfo(Graphics2D g2) {

            double k
                    = (mouseWorldX + mouseWorldY) * stretch;

            double xs = mouseWorldX + k;
            double ys = mouseWorldY + k;

            int xsb = (int) Math.floor(xs);
            int ysb = (int) Math.floor(ys);

            double xins = xs - xsb;
            double yins = ys - ysb;

            int y = 20;

            g2.setColor(Color.BLACK);

            g2.drawString(
                    String.format(
                            "World = (%.5f, %.5f)",
                            mouseWorldX,
                            mouseWorldY),
                    20, y);

            y += 20;

            g2.drawString(
                    String.format(
                            "Skewed = (%.5f, %.5f)",
                            xs,
                            ys),
                    20, y);

            y += 20;

            g2.drawString(
                    String.format(
                            "Cell = (%d,%d)",
                            xsb,
                            ysb),
                    20, y);

            y += 20;

            g2.drawString(
                    String.format(
                            "xins=%.5f yins=%.5f sum=%.5f",
                            xins,
                            yins,
                            xins + yins),
                    20, y);

            y += 20;

            g2.drawString(
                    (xins + yins <= 1)
                            ? "LOWER SIMPLEX"
                            : "UPPER SIMPLEX",
                    20, y);

            y += 20;

            g2.drawString(
                    String.format(
                            "Zoom=%.2f",
                            zoom),
                    20, y);

            y += 20;

            g2.drawString(
                    "Mouse Wheel=Zoom  Right Drag=Pan  R=Reset",
                    20, y);
        }

        private void drawCrosshair(
                Graphics2D g2,
                Point2D.Double p) {

            int x = sx(p.x);
            int y = sy(p.y);

            g2.setColor(Color.RED);

            g2.drawLine(x - 8, y, x + 8, y);
            g2.drawLine(x, y - 8, x, y + 8);
        }

        private void draw(
                Graphics2D g2,
                Point2D.Double a,
                Point2D.Double b) {

            g2.draw(new Line2D.Double(
                    sx(a.x),
                    sy(a.y),
                    sx(b.x),
                    sy(b.y)));
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            requestFocusInWindow();

            double worldX = wx(e.getX());
            double worldY = wy(e.getY());

            double factor
                    = e.getPreciseWheelRotation() < 0
                    ? 1.1
                    : 0.9;

            zoom *= factor;

            panX = e.getX() - worldX * zoom;
            panY = e.getY() + worldY * zoom;

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            if (panning) {

                int dx = e.getX() - lastMouseX;
                int dy = e.getY() - lastMouseY;

                panX += dx;
                panY += dy;

                lastMouseX = e.getX();
                lastMouseY = e.getY();

            } else {

                double skewX = wx(e.getX());
                double skewY = wy(e.getY());

                Point2D.Double world
                        = unskew(skewX, skewY);

                mouseWorldX = world.x;
                mouseWorldY = world.y;
            }

            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            double skewX = wx(e.getX());
            double skewY = wy(e.getY());

            Point2D.Double world
                    = unskew(skewX, skewY);

            mouseWorldX = world.x;
            mouseWorldY = world.y;

            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {

            requestFocusInWindow();

            if (SwingUtilities.isRightMouseButton(e)) {

                panning = true;
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            panning = false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
