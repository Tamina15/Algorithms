/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tmn.algorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author HP
 * @author Tamina
 */
public class FFT {

    /**
     * Calculate the discrete magnitude representation of a sinusoid wave
     *
     * @see https://courses.washington.edu/matlab1/Lesson_11.html
     *
     * @param resolution the number of data points
     * @param totalTime how many period of this wave will be sample
     * @param frequency the frequency of the wave
     * @param amplitude the strength of the sinusoid wave
     * @param phase the phase of the sinusoid wave
     * @param random choose whether to add randomness to the wave
     *
     * @return the Complex array containing the points
     */
    public static Complex[] makeCosinewave(int resolution, int totalTime, int frequency, double amplitude, int phase, boolean random) {
        // timesteps
        double dt = totalTime / (resolution * 1.0);
        Complex[] complex = new Complex[resolution];

        for (int i = 0; i < resolution; i++) {
            double t = i * dt;
            double point = amplitude * Math.cos(2 * Math.PI * t * frequency - Math.PI * phase / 180);
            if (random) {
                point += (Math.random() + Math.random() * -1) * 0.5;
            }
            complex[i] = new Complex(point, 0);
        }
        return complex;
    }

    /**
     * Calculate the discrete magnitude representation of a sinusoid wave
     *
     * @see https://courses.washington.edu/matlab1/Lesson_11.html
     *
     * @param resolution the number of data points
     * @param totalTime how many period of this wave will be sample
     * @param frequency the frequency of the wave
     * @param amplitude the strength of the sinusoid wave
     * @param phase the phase of the sinusoid wave
     * @param random choose whether to add randomness to the wave
     *
     * @return the Complex array containing the points
     */
    public static Complex[] makeSinewave(int resolution, int totalTime, int frequency, double amplitude, int phase, boolean random) {
        // timesteps
        double dt = totalTime / (resolution * 1.0);
        Complex[] complex = new Complex[resolution];

        for (int i = 0; i < resolution; i++) {
            double t = i * dt;
            double point = amplitude * Math.sin(2 * Math.PI * t * frequency - Math.PI * phase / 180);
            if (random) {
                point += (Math.random() + Math.random() * -1) * 0.5;
            }
            complex[i] = new Complex(point, 0);
        }
        return complex;
    }

    static int N = 2048;
    static double DT = 1 / (N * 1.0);
    static double[] T = new double[N];

    static ArrayList<Complex[]> list_sinewave = new ArrayList();

    static Complex[] sum_fourier;
    static Complex[] cos;
    static Complex[] sin;
    static Complex[] sum = new Complex[N];

    static Complex[] sum_sinewave = new Complex[N];

    public static void main(String[] args) {

        for (int i = 0; i < N; i++) {
            T[i] = i * DT;
        }
        double s = 0;
        int sign = -1;
        for (int i = 0; i < N; i++) {
            if (i % 20 == 0) {
                sign *= -1;
            }
            s = s + 0.1 * sign;
            Complex a = new Complex(s, 0);
            sum_sinewave[i] = a;
        }
        System.out.println(Arrays.toString(sum_sinewave));
//        Complex[] y = makeCosinewave(N, 2, 10, 1, 0, false);
//        list_sinewave.add(y);
//        Complex[] y1 = makeSinewave(N, 2, 10, 1, 0, false);
//        list_sinewave.add(y1);
//
//        cos = makeCosinewave(N, 2, 10, 1, 0, false);
//        sin = makeSinewave(N, 2, 10, 1, 0, false);
//
//        for (int i = 0; i < N; i++) {
//            double sum_real = cos[i].re() + sin[i].re();
//            Complex a = new Complex(sum_real, 0);
//            sum[i] = a;
//        }
//        for (int i = 0; i < N; i++) {
//            double sum_real = 0;
//            for (Complex[] c : list_sinewave) {
//                sum_real += c[i].re();
//            }
//            Complex a = new Complex(sum_real, 0);
//            sum_sinewave[i] = a;
//        }
        sum_fourier = Complex.fft(sum_sinewave);
//        sum_fourier = Complex.fft(sum);
//        sum_fourier = Complex.sine_fft(sum);
        Complex.show(sum_fourier, "y = fft(x)");
        Frame f = new Frame();
    }

    static class Panel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

        int f = 0;

        int panelWidth = 1200;
        int panelHeight = 800;

        // Zomming
        Double zoomFactor = 0.5;
        boolean zoomer;
        AffineTransform at;
        /////////////////////

        Point.Double[] sine_pts = new Point.Double[N];
        Point.Double[] fourier_pts = new Point.Double[N];
        Point.Double[] sin_points = new Point.Double[N];
        Point.Double[] cos_points = new Point.Double[N];
        Point.Double[] sum_points = new Point.Double[N];
//        Point.Double[] sine_fourier_pts = new Point.Double[N];

        public Panel() {
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setDoubleBuffered(true);
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addMouseWheelListener(this);
            this.addMouseListener(this);
            this.addMouseMotionListener(this);

            for (int i = 0; i < N; i++) {
                Point.Double sine_pt = new Point.Double(T[i] * xscale, sum_sinewave[i].re() * yscale);
                sine_pts[i] = sine_pt;

                Point.Double fourier_pt = new Point.Double(T[i] * xscale * 20, sum_fourier[i].re() * 0.5);
                fourier_pts[i] = fourier_pt;
            }
//            for (int i = 0; i < N; i++) {
//                Point.Double sine_pt = new Point.Double(T[i] * xscale, sin[i].re() * yscale);
//                sin_points[i] = sine_pt;
//
//                Point.Double cosine_pt = new Point.Double(T[i] * xscale, cos[i].re() * yscale);
//                cos_points[i] = cosine_pt;
//                Point.Double sum_pt = new Point.Double(T[i] * xscale, sum[i].re() * yscale);
//                sum_points[i] = sum_pt;
//            }
        }
        // Fit width
        int xscale = 1200;
        int yscale = 30;

        int n = 0;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.white);
            g2d.translate(oX, oY);
            g2d.setFont(new Font("Times New Roman", Font.BOLD, 10));
            FontMetrics metrics = getFontMetrics(g.getFont());
            // Zooming
            if (zoomer == true) {
                at = new AffineTransform();
                at.scale(zoomFactor, zoomFactor);
                g2d.transform(at);
            }

            long s = System.currentTimeMillis();
            CubicCurve2D cc = new CubicCurve2D.Double();
//
//            g2d.translate(0, panelHeight / 3);
            for (int i = 0; i < N - 1; i++) {
                g2d.drawLine((int) fourier_pts[i].x, (int) -fourier_pts[i].y, (int) fourier_pts[i + 1].x, (int) -fourier_pts[i + 1].y);
                if ((int) sum_fourier[i].re() < -1 || (int) sum_fourier[i].re() > 1) {
                    int stringWidth = metrics.stringWidth(i + "");

                    g2d.drawString(i + "", (int) fourier_pts[i].x - stringWidth / 2, 10);
                }
            }
            g2d.translate(-panelWidth, panelHeight / 3);

            for (int i = 0; i < N - 1; i++) {
                g2d.drawLine((int) sine_pts[i].x, (int) -sine_pts[i].y, (int) sine_pts[i + 1].x, (int) -sine_pts[i + 1].y);
//                g2d.drawLine((int) sine_pts[i].x, (int) -sine_pts[i].y, (int) sine_pts[i].x, (int) -sine_pts[i].y);
            }
            g2d.drawLine(0, 0, panelWidth, 0);
            g2d.translate(0, panelHeight / 3);
            for (int i = 0; i < N - 3; i = i + 3) {
                g2d.setColor(Color.white);
                cc.setCurve(sine_pts[i].x, -sine_pts[i].y, sine_pts[i + 1].x, -sine_pts[i + 1].y, sine_pts[i + 2].x, -sine_pts[i + 2].y, sine_pts[i + 3].x, -sine_pts[i + 3].y);
                g2d.draw(cc);
            }
//            for (int i = 0; i < N - 3; i = i + 3) {
//                cc.setCurve(sin_points[i].x, sin_points[i].y, sin_points[i + 1].x, sin_points[i + 1].y, sin_points[i + 2].x, sin_points[i + 2].y, sin_points[i + 3].x, sin_points[i + 3].y);
//                g2d.draw(cc);
//            }
//            g2d.drawLine(0, 0, panelWidth * xscale, 0);
//            g2d.translate(0, panelHeight / 3);
//            for (int i = 0; i < N - 3; i = i + 3) {
//                cc.setCurve(cos_points[i].x, cos_points[i].y, cos_points[i + 1].x, cos_points[i + 1].y, cos_points[i + 2].x, cos_points[i + 2].y, cos_points[i + 3].x, cos_points[i + 3].y);
//                g2d.draw(cc);
//            }
//            g2d.drawLine(0, 0, panelWidth * xscale, 0);
//            g2d.translate(0, panelHeight / 3);
//            for (int i = 0; i < N - 3; i = i + 3) {
//                cc.setCurve(sum_points[i].x, sum_points[i].y, sum_points[i + 1].x, sum_points[i + 1].y, sum_points[i + 2].x, sum_points[i + 2].y, sum_points[i + 3].x, sum_points[i + 3].y);
//                g2d.draw(cc);
//            }

            long e = System.currentTimeMillis();
            g2d.drawLine(0, 0, panelWidth * xscale, 0);
            g2d.drawString("time: " + (e - s) + "ms", 10, 10);
            g2d.dispose();
            g.dispose();
            n = (n + 1) % 100;

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
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Zoom in
            if (e.getWheelRotation() < 0) {
                this.setZoomFactor(1.1 * this.getZoomFactor());

            }
            //Zoom out
            if (e.getWheelRotation() > 0) {
//                if (this.getZoomFactor() / 1.1 > 1) {
//                    this.setZoomFactor(this.getZoomFactor() / 1.1);
//                } else {
//                    this.setZoomFactor(1);
//                }
                this.setZoomFactor(this.getZoomFactor() / 1.1);
            }
        }

        int startX = 0;
        int startY = 0;
        int offsetX = 0;
        int offsetY = 0;
        int oX;
        int oY;
        boolean press = false;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            press = false;
//            System.out.println("release");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
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
        public static BufferedImage image;

        ;
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
//                    panel.update();
                    count = 0;
                    timer = 0;
                    updating = false;
                }
            }
        }
    }

    static public class Complex {

        private final double re; // the real part
        private final double im; // the imaginary part

        // create a new object with the given real and imaginary parts
        public Complex(double real, double imag) {
            re = real;
            im = imag;
        }

        // return a string representation of the invoking Complex object
        public String toString() {
            if (im == 0) {
                return re + " + 0i";
            }
            if (re == 0) {
                return im + "i";
            }
            if (im < 0) {
                return re + " - " + (-im) + "i";
            }
            return re + " + " + im + "i";
        }

        // return abs/modulus/magnitude and angle/phase/argument
        public double abs() {
            return Math.hypot(re, im);
        } // Math.sqrt(re*re + im*im)

        public double phase() {
            return Math.atan2(im, re);
        } // between -pi and pi

        // return a new Complex object whose value is (this + b)
        public Complex plus(Complex b) {
            Complex a = this; // invoking object
            double real = a.re + b.re;
            double imag = a.im + b.im;
            return new Complex(real, imag);
        }

        // return a new Complex object whose value is (this - b)
        public Complex minus(Complex b) {
            Complex a = this;
            double real = a.re - b.re;
            double imag = a.im - b.im;
            return new Complex(real, imag);
        }

        // return a new Complex object whose value is (this * b)
        public Complex times(Complex b) {
            Complex a = this;
            double real = a.re * b.re - a.im * b.im;
            double imag = a.re * b.im + a.im * b.re;
            return new Complex(real, imag);
        }

        // scalar multiplication
        // return a new object whose value is (this * alpha)
        public Complex times(double alpha) {
            return new Complex(alpha * re, alpha * im);
        }

        // return a new Complex object whose value is the conjugate of this
        public Complex conjugate() {
            return new Complex(re, -im);
        }

        // return a new Complex object whose value is the reciprocal of this
        public Complex reciprocal() {
            double scale = re * re + im * im;
            return new Complex(re / scale, -im / scale);
        }

        // return the real or imaginary part
        public double re() {
            return re;
        }

        public double im() {
            return im;
        }

        // return a / b
        public Complex divides(Complex b) {
            Complex a = this;
            return a.times(b.reciprocal());
        }

        // return a new Complex object whose value is the complex exponential of
        // this
        public Complex exp() {
            return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re)
                    * Math.sin(im));
        }

        // return a new Complex object whose value is the complex sine of this
        public Complex sin() {
            return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re)
                    * Math.sinh(im));
        }

        // return a new Complex object whose value is the complex cosine of this
        public Complex cos() {
            return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re)
                    * Math.sinh(im));
        }

        // return a new Complex object whose value is the complex tangent of
        // this
        public Complex tan() {
            return sin().divides(cos());
        }

        // a static version of plus
        public static Complex plus(Complex a, Complex b) {
            double real = a.re + b.re;
            double imag = a.im + b.im;
            Complex sum = new Complex(real, imag);
            return sum;
        }

        // compute the FFT of x[], assuming its length is a power of 2
        public static Complex[] fft(Complex[] x) {
            int N = x.length;

            // base case
            if (N == 1) {
                return new Complex[]{x[0]};
            }

            // radix 2 Cooley-Tukey FFT
            if (N % 2 != 0) {
                throw new RuntimeException("N is not a power of 2");
            }

            // fft of even terms
            Complex[] even = new Complex[N / 2];
            for (int k = 0; k < N / 2; k++) {
                even[k] = x[2 * k];
            }
            Complex[] q = fft(even);

            // fft of odd terms
            Complex[] odd = even; // reuse the array
            for (int k = 0; k < N / 2; k++) {
                odd[k] = x[2 * k + 1];
            }
            Complex[] r = fft(odd);

            // combine
            Complex[] y = new Complex[N];
            for (int k = 0; k < N / 2; k++) {
                double kth = -2 * k * Math.PI / N;
                Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
                y[k] = q[k].plus(wk.times(r[k]));
                y[k + N / 2] = q[k].minus(wk.times(r[k]));
            }
            return y;
        }

        public static Complex[] sine_fft(Complex[] x) {
            int N = x.length;

            // base case
            if (N == 1) {
                return new Complex[]{x[0]};
            }

            // radix 2 Cooley-Tukey FFT
            if (N % 2 != 0) {
                throw new RuntimeException("N is not a power of 2");
            }

            // fft of even terms
            Complex[] even = new Complex[N / 2];
            for (int k = 0; k < N / 2; k++) {
                even[k] = x[2 * k];
            }
            Complex[] q = fft(even);

            // fft of odd terms
            Complex[] odd = even; // reuse the array
            for (int k = 0; k < N / 2; k++) {
                odd[k] = x[2 * k + 1];
            }
            Complex[] r = fft(odd);

            // combine
            Complex[] y = new Complex[N];
            for (int k = 0; k < N / 2; k++) {
                double kth = -2 * k * Math.PI / N;
                Complex wk = new Complex(Math.sin(kth), Math.cos(kth));
                y[k] = q[k].plus(wk.times(r[k]));
                y[k + N / 2] = q[k].minus(wk.times(r[k]));
            }
            return y;
        }

        // compute the inverse FFT of x[], assuming its length is a power of 2
        public static Complex[] ifft(Complex[] x) {
            int N = x.length;
            Complex[] y = new Complex[N];

            // take conjugate
            for (int i = 0; i < N; i++) {
                y[i] = x[i].conjugate();
            }

            // compute forward FFT
            y = fft(y);

            // take conjugate again
            for (int i = 0; i < N; i++) {
                y[i] = y[i].conjugate();
            }

            // divide by N
            for (int i = 0; i < N; i++) {
                y[i] = y[i].times(1.0 / N);
            }

            return y;

        }

        // display an array of Complex numbers to standard output
        public static void show(Complex[] x, String title) {
            System.out.println(title);
            for (int i = 0; i < x.length; i++) {
                System.out.println(i + " " + x[i]);
            }
            System.out.println();
        }
    }
}
