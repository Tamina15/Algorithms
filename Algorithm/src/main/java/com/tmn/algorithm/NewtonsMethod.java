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
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class NewtonsMethod {

    static class Panel extends JPanel {

        private final JPanel panel = this;
        private int width, height;
        private final Point.Double origin = new Point.Double(), oldOrigin = new Point.Double(origin.x, origin.y);
        private final Point.Double mousePt = new Point.Double(), mouseWorld = new Point.Double(mousePt.x, mousePt.y);
        private double scale = 1, zoomFactor = 1.05, inversedZoomFactor = 1 / zoomFactor, minScale = 0.53033, maxScale = 15.3674;
        public double updateTime, drawTime;

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
                    }
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
            move(e);
        }

        private void move(MouseEvent e) {
            mousePt.setLocation(e.getPoint());
            mouseWorld.setLocation((mousePt.x - origin.x) / scale, (mousePt.y - origin.y) / scale);

        }

        private void drag(MouseEvent e) {
            double dx = ((e.getX() - mousePt.x));
            double dy = ((e.getY() - mousePt.y));
            origin.setLocation(oldOrigin.getX() + dx, oldOrigin.getY() + dy);
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
            origin.setLocation(width / 2, height / 2);
            YAxisLine = new Line2D.Double(0, -height, 0, height * 2);
            double x1 = -width * 2, x2 = width * 2;
            XAxisLine = new Line2D.Double(getScreenPoint(x1, fx(XAxisCoefficients, x1), null), getScreenPoint(x2, fx(XAxisCoefficients, x2), null));

            double x = -4;
            for (int i = 0; i < n; i++) {
                double y = fx(co, x);
                screenPoints[i] = getScreenPoint(x, y, null);
                tangents[i] = new MyPoint();
                x += 0.02;
            }
            for (int i = 0; i < iteration; i++) {
                newtonTangentLines[i] = new Line2D.Double();
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform saveAT = g2d.getTransform();

            g2d.translate(origin.x, origin.y);
            g2d.scale(scale, scale);

            BasicStroke st1 = new BasicStroke(1);
            BasicStroke st3 = new BasicStroke(3);
            /// Draw here
            g2d.setColor(Color.white);
            g2d.draw(XAxisLine);
            g2d.draw(YAxisLine);
            for (int i = 0; i < n; i++) {
                if (i == pIndex) {
                    g2d.setStroke(st3);
                    g2d.setColor(Color.green);
                } else {
                    g2d.setStroke(st1);
                    g2d.setColor(Color.white);
                }
                g2d.draw(screenPoints[i]);
            }

            g2d.setColor(Color.white);
            g2d.setStroke(st1);
            g2d.draw(tangentLine);

            if (tangentIntersection != null) {
                g2d.setColor(Color.blue);
                g2d.setStroke(st3);
                g2d.draw(getScreenPoint(tangentIntersection, null));
                g2d.setStroke(st1);
                g2d.setColor(Color.white);
            }

            g2d.setColor(Color.red);
            for (int i = 0; i < iteration; i++) {
                g2d.draw(newtonTangentLines[i]);
            }
            if (root != null) {
                g2d.setStroke(st3);
                g2d.setColor(Color.yellow);
                g2d.draw(getScreenPoint(root, 0, null));
                g2d.setStroke(st1);
            }

            ///
            g2d.setTransform(saveAT);
            g2d.setColor(Color.white);

        }

        int n = 1500;
//        double[] co = new double[]{1, 1, -2.8, -3.4, 2.8, 4, 1};
        double[] co = new double[]{0, -2.8, -3.4, 2.8, 4, 1};
//        double[] co = new double[]{1, -2, -3, 4};
//        double[] co = new double[]{2, 1, 3};
        MyPoint[] screenPoints = new MyPoint[n];
        int pIndex = -1;

        double[] tangentCoefficients = new double[2];
        double[] XAxisCoefficients = new double[]{0, 0};
        Line2D YAxisLine, XAxisLine;
        MyPoint[] tangents = new MyPoint[n];
        Line2D.Double tangentLine = new Line2D.Double();
        MyPoint tangentIntersection = new MyPoint();

        int iteration = 15;
        double[] nr = new double[iteration];
        Line2D.Double[] newtonTangentLines = new Line2D.Double[iteration];
        Double root;

        public void update() {
            int minIndex = 0;
            MyPoint min = screenPoints[minIndex];
            double minDisanceSq = mouseWorld.distanceSq(min);
            for (int i = 0; i < screenPoints.length; i++) {
                MyPoint p = screenPoints[i];
                double d = mouseWorld.distanceSq(p);
                if (d < minDisanceSq) {
                    minDisanceSq = d;
                    minIndex = i;
                }
            }
            pIndex = minIndex;
            MyPoint p = screenPoints[minIndex];
            MyPoint a = getXYPoint(p, null);
            tangents(co, a.x, tangents);
            tangentCoefficients(co, a.x, tangentCoefficients);
            double x1 = -width * 2, x2 = width * 2;
            tangentLine.setLine(getScreenPoint(x1, fx(tangentCoefficients, x1), null), getScreenPoint(x2, fx(tangentCoefficients, x2), null));
            calculateIntersectionPoint(XAxisCoefficients, tangentCoefficients, tangentIntersection);

            root = NewtonRaphson(co, a.x);
            NewtonRaphson(co, a.x, iteration, nr);
            double[] newtonTangentCoefficients = new double[2];
            for (int i = 0; i < iteration; i++) {
                double x = nr[i];
                tangentCoefficients(co, x, newtonTangentCoefficients);
                double nx1 = -width * 2, nx2 = width * 2;
                MyPoint p1 = getScreenPoint(nx1, fx(newtonTangentCoefficients, nx1), null);
                MyPoint p2 = getScreenPoint(nx2, fx(newtonTangentCoefficients, nx2), null);
                Line2D.Double newtonTangentLine = newtonTangentLines[i];
                newtonTangentLine.setLine(p1, p2);
            }
        }

        /**
         * Evaluate a function represented by {@code coefficients} array at value {@code x}.
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         *     ax^3 + bx^2 + cx + d = 0
         * </pre>
         *
         * @param coefficients the array of coefficients
         * @param x
         * @return Evaluation of the function f(x)
         */
        private double fx(double[] coefficients, double x) {
            double y = 0;
            int l = coefficients.length;
            for (int i = 0; i < l; i++) {
                double z = coefficients[i];
                y += Math.pow(x, i) * z;
            }
            return y;
        }

        /**
         * Evaluate the derivative (or <i>slope</i>) of a function
         * represented by {@code coefficients} array at value {@code x}.
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         * =>  f(x) = ax^3 + bx^2 + cx + d
         * =>  f'(x) = 3ax^2 + 2bx + c
         * </pre>
         *
         * @param coefficients the array of coefficients
         * @param x
         * @return Evaluation of the function f'(x)
         */
        private double dfx(double[] coefficients, double x) {
            double y = 0;
            int l = coefficients.length;
            // Skip first coefficient coeffectine
            for (int i = 1; i < l; i++) {
                double z = coefficients[i];
                y += Math.pow(x, i - 1) * z * i;
            }
            return y;
        }

        /**
         * Calculate the tangent line coordinates of a function
         * represented by {@code coefficients} array at value {@code x}
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         * =>  f(x) = ax^3 + bx^2 + cx + d
         * =>  f'(x) = 3ax^2 + 2bx + c
         * =>  t(a) = f'(x) * (a - x) + f(x)
         * </pre>
         *
         * @param coefficients the array of coefficients
         * @param aX
         * @param points       coordinate arrays of the tangent line
         */
        private void tangents(double[] coefficients, double x, MyPoint[] points) {
            double y = fx(coefficients, x);
            double slope = dfx(coefficients, x);
            int l = points.length;
            int lh = l / 2;
            for (int a = -lh; a < lh; a++) {
                double fa = slope * (a - x) + y;
                MyPoint p = points[a + lh];
                getScreenPoint(a, fa, p);
            }
        }

        /**
         * Calculate the tangent line coordinate of a function
         * represented by {@code coefficients} array at position {@code x} and tangent value {@code a}.
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         * =>  f(x) = ax^3 + bx^2 + cx + d
         * =>  f'(x) = 3ax^2 + 2bx + c
         * =>  t(a) = f'(x) * (a - x) + f(x)
         * </pre>
         *
         * @param coefficients the array of coefficients
         * @param aX
         * @param points       coordinate arrays of the tangent line
         */
        private MyPoint tangentPoint(double[] coefficients, double x, double a, MyPoint out) {
            double y = fx(coefficients, x);
            double slope = dfx(coefficients, x);
            double fa = slope * (a - x) + y;
            if (out == null) {
                out = new MyPoint(a, fa);
            } else {
                out.setLocation(a, fa);
            }
            return out;
        }

        /**
         * Calculate the tangent line coefficients of a function
         * represented by {@code coefficients} array at value {@code x}.
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         *     f(x)  = ax^3 + bx^2 + cx + d
         *     f'(x) = 3ax^2 + 2bx + c
         *     t(a)  = f'(x) * (a - x) + f(x)
         * =>  t(a)  = f'(x) * a + ((-f'(x) * x) + f(x))
         * let t(a) = y, f'(x) = m, and ((-f'(x) * x) + f(x)) = b.
         * =>  y     =   m   * a + b
         * Coefficients array is [b, m] or [((-f'(x) * x) + f(x)), f'(x)]
         * </pre>
         *
         * @param coefficients the array of coefficients
         * @param x
         * @param out          the tangent coefficients array
         */
        public double[] tangentCoefficients(double[] coefficients, double x, double[] out) {
            double y = fx(coefficients, x); // f(x)
            double slope = dfx(coefficients, x); // f'(x)

            if (out == null) {
                out = new double[]{(-slope * x) + y, slope};
            } else {
                out[1] = slope;
                out[0] = (-slope * x) + y;
            }
            return out;
        }

        /**
         * Calculate the root of an function represented by {@code coefficients} array
         * using Newton-Raphson method
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         *     f(x)  = ax^3 + bx^2 + cx + d
         *     f'(x) = 3ax^2 + 2bx + c
         * </pre>
         *
         * @param coefficients  the array of coefficients
         * @param inititalValue the initial guest value
         * @param iteration     the number of iteration to run
         * @param imd           the intermediate values
         */
        public Double NewtonRaphson(double[] coefficients, double inititalValue, int iteration, double[] imd) {
            if (iteration <= 0) {
                throw new IllegalArgumentException("Iteration must not smaller or equal to zero");
            }
            if (imd == null) {
                imd = new double[iteration];
            } else {
                if (imd.length != iteration) {
                    throw new IllegalArgumentException("Intermediate array must have the same length as iteration count");
                }
            }

            double fx = fx(coefficients, inititalValue);
            double dfx = dfx(coefficients, inititalValue);
            if (dfx == 0) {
                return null;
            }
            Double result = inititalValue - (fx / dfx);
            imd[0] = result;
            for (int i = 1; i < iteration; i++) {
                fx = fx(coefficients, result);
                dfx = dfx(coefficients, result);
                if (dfx == 0) {
                    return result;
                }
                result = result - (fx / dfx);
                imd[i] = result;
            }
            return result;
        }

        /**
         * Calculate the root of an function represented by {@code coefficients} array
         * using Newton-Raphson method
         * <p/>
         * Example of coefficients array:
         * <pre>
         *     coefficients = [D, C, B, A];
         *     f(x)  = ax^3 + bx^2 + cx + d
         *     f'(x) = 3ax^2 + 2bx + c
         * </pre>
         *
         * @param coefficients  the array of coefficients
         * @param inititalValue the initial guest value
         * @param iteration     the number of iteration to run
         * @param imd           the intermediate values
         */
        public Double NewtonRaphson(double[] coefficients, double inititalValue) {
            double fx = fx(coefficients, inititalValue);
            double dfx = dfx(coefficients, inititalValue);
            if (dfx == 0) {
                return null;
            }
            Double result = inititalValue - (fx / dfx);
            for (int i = 1; i < 20; i++) {
                fx = fx(coefficients, result);
                dfx = dfx(coefficients, result);
                if (dfx == 0) {
                    break;
                }
                result = result - (fx / dfx);
            }
            fx = fx(coefficients, result);
            if (Math.abs(fx) < 0.001) {
                return result;
            } else {
                return null;
            }
        }

        private MyPoint getScreenPoint(MyPoint p, MyPoint out) {
            return getScreenPoint(p.getX(), p.getY(), out);
        }

        // Update getXYPoint along with this function
        private MyPoint getScreenPoint(double x, double y, MyPoint out) {
            double x1 = x * 100; // space out x coordinate
            double y1 = -y * 100; // inverse the y coordinate
            if (out == null) {
                out = new MyPoint(x1, y1);
            } else {
                out.setLocation(x1, y1);
            }
            return out;
        }

        private MyPoint getXYPoint(MyPoint screen, MyPoint out) {
            double x1 = screen.x / 100;
            double y1 = -screen.y / 100;
            if (out == null) {
                out = new MyPoint(x1, y1);
            } else {
                out.setLocation(x1, y1);
            }
            return out;
        }

        public MyPoint calculateIntersectionPoint(double[] co1, double[] co2, MyPoint out) {
            double m1 = co1[1];
            double m2 = co2[1];

            if (m1 == m2) {
                out = null;
                return out;
            }

            double b1 = co1[0];
            double b2 = co2[0];

            double x = (b2 - b1) / (m1 - m2);
            double y = m1 * x + b1;
            if (out == null) {
                out = new MyPoint(x, y);
            } else {
                out.setLocation(x, y);
            }
            return out;
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
            long start, end;
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    start = System.nanoTime();
                    f.repaint();
                    end = System.nanoTime();
                    p.drawTime = (end - start) * 1.0 / 1000000;
                    delta--;
                }
            }
        });
        Thread updateThread = Thread.startVirtualThread(() -> {
            double delta = 0;
            double interval = 1000000000 / 60;
            long lastTime = System.nanoTime();
            long start, end;
            while (f.isEnabled()) {
                long currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / interval;
                lastTime = currentTime;
                if (delta >= 1) {
                    start = System.nanoTime();
                    p.update();
                    end = System.nanoTime();
                    p.updateTime = (end - start) * 1.0 / 1000000;
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
