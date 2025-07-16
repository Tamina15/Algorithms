package com.tmn.edgedetection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Panel extends JPanel {

    private int width, height;
    private final Point origin = new Point(0, 0);
    private final Point oldOrigin = new Point(0, 0);
    private final Point mousePt = new Point(0, 0);
    private double zoomFactor = 1, zoomMutiplier = 1.05;
    private int scale = 1;
    double delta = 0;

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
                if (SwingUtilities.isRightMouseButton(e)) {
                    getImage();
                    initImages();
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    move(e);
                }
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
                    zoomMutiplier = 1.15;
                }
                if (c == KeyEvent.VK_Q) {
                    option.setHighFraction(option.getHighFraction() + 0.001);
                    doUpdate = true;
                }
                if (c == KeyEvent.VK_A) {
                    option.setHighFraction(option.getHighFraction() - 0.001);
                    doUpdate = true;
                }
                if (c == KeyEvent.VK_W) {
                    option.setLowFraction(option.getLowFraction() + 0.001);
                    doUpdate = true;
                }
                if (c == KeyEvent.VK_S) {
                    option.setLowFraction(option.getLowFraction() - 0.001);
                    doUpdate = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_CONTROL) {
                    zoomMutiplier = 1.05;
                }
                if (c == KeyEvent.VK_Q || c == KeyEvent.VK_A || c == KeyEvent.VK_W || c == KeyEvent.VK_S) {
                    doUpdate = true;
                }
            }
        });

        option = new Option(0.85, 0.15);
        getImage();
        initImages();
    }

    private void setAnchorPoint(MouseEvent e) {
        oldOrigin.setLocation(origin);
        mousePt.setLocation(e.getPoint());
    }

    private void move(MouseEvent e) {
        double dx = ((e.getX() - mousePt.x) / zoomFactor);
        double dy = ((e.getY() - mousePt.y) / zoomFactor);
        origin.setLocation(oldOrigin.getX() + dx, oldOrigin.getY() + dy);
    }

    private void zoom(MouseWheelEvent e) {
        // Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= zoomMutiplier;
        }
        // Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= zoomMutiplier;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(origin.x, origin.y);

        draw(g2d);
        g2d.dispose();
    }

    private int drawImage(Graphics2D g2d, Image image, int x, int y, String label) {
        g2d.drawString(label, x - 10, y);
        g2d.drawImage(image, x, y, null);
        return x + image.getWidth(null);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.setFont(new Font(null, 1, 40));
        int x = 0;
        x = drawImage(g2d, image, x, 0, "Original");
        x = drawImage(g2d, grayScaleImage, x, 0, "Gray scale");
        x = drawImage(g2d, sobelX, x, 0, "Sobel X");
        x = drawImage(g2d, sobelY, x, 0, "Sovel Y");
        x = drawImage(g2d, sobel, x, 0, "Sobel Filter");
        x = drawImage(g2d, gradient, x, 0, "Gradient Image");
        x = drawImage(g2d, nonMaxImage, x, 0, "Non-max Suppression");
        x = drawImage(g2d, doubleThreshold, x, 0, "Double Threshold");
        x = drawImage(g2d, hysteresis, x, 0, "Hysteresis");
        x = drawImage(g2d, image, x, 0, "Original");
        x = 0;
        int y = image.getHeight() + 50;
        x = drawImage(g2d, image, x, y, "Original");
        x = drawImage(g2d, grayScaleImage, x, y, "Gray scale");
        x = drawImage(g2d, cannySobelX, x, y, "Sobel X");
        x = drawImage(g2d, cannySobelY, x, y, "Sobel Y");
        x = drawImage(g2d, cannySobel, x, y, "Sobel Filter");
        drawImage(g2d, cannyAngle2, x, 2 * y, "Gradient Image 2");
        x = drawImage(g2d, cannyAngle, x, y, "Gradient Image");
        x = drawImage(g2d, cannyNonMax, x, y, "Non-max Suppression");
        g2d.drawString(pTime + "", x, y * 2);
        g2d.drawString(option.getHighFraction() + "", x, 2 * y + 100);
        g2d.drawString(option.getLowFraction() + "", x, 2 * y + 200);
        x = drawImage(g2d, cannyDouble, x, y, "Double Threshold");
        x = drawImage(g2d, cannyHysteresis, x, y, "Hysteresis");
        x = drawImage(g2d, image, x, y, "Original");
    }

    boolean doUpdate = false;

    public void update() {
        if (doUpdate) {
            filter();
            doUpdate = false;
        }
    }

    Option option;
    BufferedImage image, grayScaleImage, sobelX, sobelY, sobel, gradient, nonMaxImage, doubleThreshold, hysteresis;
    BufferedImage cannySobelX, cannySobelY, cannySobel, cannyAngle, cannyAngle2, cannyNonMax, cannyDouble, cannyHysteresis;
    Threshold threshold;
    double[][] gradients;
    CannyEdgeDetection canny, angleCanny;

    private void getImage() {
        try {
            URL url = URI.create("https://picsum.photos/1500").toURL();
//            File file = new File("Bikesgray.jpg");
            File file = new File("2-1500x1500.jpg");
//            File file = new File("15-1500x1500.jpg");
//            File file = new File("1_original.jpg");
//            File file = new File("Untitled.png");
            image = ImageIO.read(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initImages() {
        if (image == null) {
            return;
        }
        grayScaleImage = getGrayScaleImage(image);
        grayScaleImage = applyGaussianBlur(grayScaleImage);
        sobelX = applySobelFilterX(grayScaleImage);
        sobelY = applySobelFilterY(grayScaleImage);
        sobel = applySobelFilter(sobelX, sobelY);
        gradients = computeGradientAngle(sobelX, sobelY);
        gradient = getGradientImage(gradients);
        nonMaxImage = nonMaximumSuppression(sobel, gradients);
        threshold = threshold(nonMaxImage, 50, 150);
        doubleThreshold = threshold.image();
        hysteresis = hysteresis(threshold);

        canny = new CannyEdgeDetection(image, option);
        canny.filter();

        cannySobelX = canny.getSobelXImage();
        cannySobelY = canny.getSobelYImage();
        cannySobel = canny.getSobelImage();
        cannyAngle = canny.getAngleImage();
        cannyNonMax = canny.getNonMaximumSuppressionImage();

        angleCanny = new CannyEdgeDetection(cannyAngle, option);
        angleCanny.filter();
        cannyAngle2 = angleCanny.getSobelImage();

        filter();
    }
    double pTime = 0;

    private void filter() {

        pTime = canny.filter();
        cannyDouble = canny.getDoubleThesholdingImage();
        cannyHysteresis = canny.getHysteresisImage();
    }

    private BufferedImage getGrayScaleImage(Image image) {
        BufferedImage gsI = new BufferedImage(image.getWidth(null), image.getHeight(null), TYPE_BYTE_GRAY);
        GrayFilter filter = new GrayFilter();
        ImageProducer prod = new FilteredImageSource(image.getSource(), filter);
        Image tempI = Toolkit.getDefaultToolkit().createImage(prod);
        Graphics2D bGr = gsI.createGraphics();
        bGr.drawImage(tempI, 0, 0, null);
        bGr.dispose();
        return gsI;
    }

    private BufferedImage applyGaussianBlur(BufferedImage image) {
        float[] kernel = {
            1 / 16f, 2 / 16f, 1 / 16f,
            2 / 16f, 4 / 16f, 2 / 16f,
            1 / 16f, 2 / 16f, 1 / 16f
        };
        Kernel gaussianKernel = new Kernel(3, 3, kernel);
        ConvolveOp op = new ConvolveOp(gaussianKernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    private BufferedImage applySobelFilterY(BufferedImage image) {
        float[] kernelY = {
            -1, 0, 1,
            -2, 0, 2,
            -1, 0, 1
        };
        Kernel kernel = new Kernel(3, 3, kernelY);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    private BufferedImage applySobelFilterX(BufferedImage image) {
        float[] kernelX = {
            -1, -2, -1,
            0, 0, 0,
            1, 2, 1
        };
        Kernel kernel = new Kernel(3, 3, kernelX);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    private BufferedImage applySobelFilter(BufferedImage gX, BufferedImage gY) {
        BufferedImage sobelImage = new BufferedImage(gX.getWidth(), gX.getHeight(), TYPE_BYTE_GRAY);
        int w = sobelImage.getWidth();
        int h = sobelImage.getHeight();
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int pixelX = gX.getRGB(x, y) & 0xFF; // get first byte
                int pixelY = gY.getRGB(x, y) & 0xFF;
                double m = Math.sqrt(pixelX * pixelX + pixelY * pixelY);
                int magnitude = (int) Math.min(255, m);
                int gray = (magnitude << 16) | (magnitude << 8) | magnitude; // Grayscale color, r = g = b
                sobelImage.setRGB(x, y, gray);
            }
        }

        return sobelImage;
    }

    private double[][] computeGradientAngle(BufferedImage gX, BufferedImage gY) {
        int w = gX.getWidth();
        int h = gX.getHeight();

        double[][] gradients = new double[w][h];
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int pixelX = gX.getRGB(x, y) & 0xFF; // get first byte
                int pixelY = gY.getRGB(x, y) & 0xFF;
                double angle = Math.atan2(pixelY, pixelX) * 180.0 / Math.PI;
                gradients[x][y] = angle;
            }
        }
        return gradients;
    }

    DecimalFormat formatter = new DecimalFormat("#00.0 ");

    private BufferedImage getGradientImage(double[][] gradients) {
        int w = gradients.length;
        int h = gradients[0].length;
        BufferedImage g = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int a = (int) (gradients[x][y]);
                if (a < 0) {
                    a += 180;
                }
                a += 75;
                int p = (a << 16) | (a << 8) | a;
                g.setRGB(x, y, p);
            }
        }
        return g;
    }

    private BufferedImage nonMaximumSuppression(BufferedImage sobelImage, double[][] gradients) {
        int w = sobelImage.getWidth();
        int h = sobelImage.getHeight();
        BufferedImage nonMaxImage = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        nonMaxImage.createGraphics().drawImage(sobelImage, 0, 0, w, h, null);
        for (int x = 2; x < w - 2; x++) {
            for (int y = 2; y < h - 2; y++) {
                try {
                    int q = 225, r = 255;
                    double a = gradients[x][y];
                    if ((0 <= a && a < 22.5) || (157.5 <= a && a <= 180)) {
                        q = nonMaxImage.getRGB(x, y + 1) & 0xFF;
                        r = nonMaxImage.getRGB(x, y - 1) & 0xFF;
                    } else if (22.5 <= a && a < 67.5) {
                        q = nonMaxImage.getRGB(x + 1, y - 1) & 0xFF;
                        r = nonMaxImage.getRGB(x - 1, y + 1) & 0xFF;
                    } else if (67.5 <= a && a < 112.5) {
                        q = nonMaxImage.getRGB(x + 1, y) & 0xFF;
                        r = nonMaxImage.getRGB(x - 1, y) & 0xFF;
                    } else if (112.5 <= a && a < 157.5) {
                        q = nonMaxImage.getRGB(x - 1, y - 1) & 0xFF;
                        r = nonMaxImage.getRGB(x + 1, y + 1) & 0xFF;
                    }
                    int pixel = nonMaxImage.getRGB(x, y);
                    int pi = pixel & 0xFF;
                    if (pi >= q && pi >= r) {
                        nonMaxImage.setRGB(x, y, pixel);
                    } else {
                        nonMaxImage.setRGB(x, y, 0);
                    }
                } catch (Exception e) {
                    nonMaxImage.setRGB(x - 1, y - 1, 0);
                }
            }
        }
        return nonMaxImage;
    }

    private Threshold threshold(BufferedImage nonMaxImage, double lowThresholdRatio, double highThresholdRatio) {
        int w = nonMaxImage.getWidth();
        int h = nonMaxImage.getHeight();
        int max = 0;
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int p = nonMaxImage.getRGB(x, y) & 0xFF;
                if (p > max) {
                    max = p;
                }
            }
        }
        int highThreshold = (int) (highThresholdRatio);
        int lowThreshold = (int) (lowThresholdRatio);
        BufferedImage dThreshold = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        int weakPixel = 150 << 16 | 150 << 8 | 150;
        int strongPixel = 255 << 16 | 255 << 8 | 255;
        int ca = 0, cb = 0;
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int p = nonMaxImage.getRGB(x, y) & 0xFF;
                if (p >= highThreshold) {
                    ca++;
                    dThreshold.setRGB(x, y, strongPixel);
                }
                if (p <= highThreshold && p >= lowThreshold) {
                    cb++;
                    dThreshold.setRGB(x, y, weakPixel);
                }
            }
        }
        Threshold threshold = new Threshold(dThreshold, weakPixel, strongPixel);
        return threshold;
    }

    private BufferedImage hysteresis(Threshold threshold) {
        BufferedImage image = threshold.image();
        int weakPixel = threshold.weak();
        int strongPixel = threshold.strong();
        int w = image.getWidth();
        int h = image.getHeight();
        int[][] pixels = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                pixels[x][y] = image.getRGB(x, y);
            }
        }
        BufferedImage hysteresis = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        int[] ps = new int[w * h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                try {
                    if (pixels[x][y] == weakPixel) {
                        if (pixels[x - 1][y - 1] == strongPixel
                                || pixels[x - 1][y] == strongPixel
                                || pixels[x - 1][y + 1] == strongPixel
                                || pixels[x][y - 1] == strongPixel
                                || pixels[x][y + 1] == strongPixel
                                || pixels[x + 1][y - 1] == strongPixel
                                || pixels[x + 1][y] == strongPixel
                                || pixels[x + 1][y + 1] == strongPixel) {
                            pixels[x][y] = strongPixel;
                        } else {
                            pixels[x][y] = strongPixel;
                        }
                    }
                } catch (Exception e) {
                }
                ps[y * w + x] = pixels[x][y];
            }
        }
        hysteresis.getRaster().setPixels(0, 0, w, h, ps);
        return hysteresis;
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

record Threshold(BufferedImage image, int weak, int strong) {

}
