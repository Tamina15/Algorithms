package com.tmn.edgedetection;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CannyEdgeDetection {

    private static final Map<Integer, int[]> colorCache = new HashMap<>();
    private final BufferedImage original;
    private BufferedImage grayScaleImage, filteredImage;
    private final int width, height;

    private final Option option;

    private final int[][] pixels, gradientsX, gradientsY, doubleThresholding, hysteresis;
    private final double[][] gaussians, gradients, angles, nonMaxSuppression;

    // https://github.com/rstreet85/JCanny
    private int mean; // https://en.wikipedia.org/wiki/Arithmetic_mean#:~:text=mean%20or%20average,numbers%20in%20the%20collection
    // The standard deviation of a random variable is the square root of its variance.
    private int standardDeviation;// https://en.wikipedia.org/wiki/Standard_deviation

    private final int highPixel = 255, lowPixel = 150;

    private static final double[] MASK_GAUSSIAN = {
        1 / 16f, 2 / 16f, 1 / 16f,
        2 / 16f, 4 / 16f, 2 / 16f,
        1 / 16f, 2 / 16f, 1 / 16f
    };
    private static final int[][] MASK_Y = new int[][]{
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };
    private static final int[][] MASK_X = new int[][]{
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };

    static {
        for (int i = 0; i <= 255; i++) {
            colorCache.put(i, new int[]{i, i, i});
        }
    }

    public CannyEdgeDetection(BufferedImage original, Option option) {
        this.original = original;
        this.option = option;

        this.width = original.getWidth();
        this.height = original.getHeight();

        this.pixels = new int[width][height];
        this.gaussians = new double[width][height];
        this.gradientsX = new int[width][height];
        this.gradientsY = new int[width][height];
        this.gradients = new double[width][height];
        this.angles = new double[width][height];
        this.nonMaxSuppression = new double[width][height];
        this.doubleThresholding = new int[width][height];
        this.hysteresis = new int[width][height];

        queue = new int[width * height];

        setPixels();
        filter0();
    }

    private void setPixels() {
        grayScaleImage = getGrayScaleImage(original);
        grayScaleImage = applyGaussianBlur(grayScaleImage);
        int w = grayScaleImage.getWidth();
        int h = grayScaleImage.getHeight();
        Raster raster = grayScaleImage.getRaster();
        int[] data = raster.getPixels(0, 0, w, h, (int[]) null);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                pixels[x][y] = data[y * width + x];
            }
        }
    }

    private void filter0() {
        sobelX();
        sobelY();
        sobel();
        nonMaximumSuppression();
        doubleThresholding();
        hysteresis();
    }

    public double filter() {
        long start = System.nanoTime();
        doubleThresholdingAndHysteresis();
        long end = System.nanoTime();
        return (end - start) * 1.0 / 1000000;
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
        double[][] gaussian = new double[width][height];
        float[] kernel = {
            1 / 16f, 2 / 16f, 1 / 16f,
            2 / 16f, 4 / 16f, 2 / 16f,
            1 / 16f, 2 / 16f, 1 / 16f
        };
        Kernel gaussianKernel = new Kernel(3, 3, kernel);
        ConvolveOp op = new ConvolveOp(gaussianKernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    /**
     * Apply the {@code mask} to the {@code in} array to obtain the {@code out} array
     *
     * @param in   the input array
     * @param mask the mask to be apply
     * @param out  the out array
     * @return the {@code out} array
     */
    private int[][] applyMask(int[][] input, int[][] mask, int[][] output) {
        if (input == null) {
            return null;
        }
        int w = input.length;
        int h = input[0].length;
        if (w < 3 || h < 3 || mask.length != 3 || mask[0].length != 3) {
            return null;
        }
        if (output == null) {
            output = new int[w][h];
        }
        for (int x = 0; x < w - 2; x++) {
            for (int y = 0; y < h - 2; y++) {
                int sum = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        sum += (mask[i][j] * input[x + i][y + j]);
                    }
                }
                sum = sum / 9;
                output[x + 1][y + 1] = sum;
            }
        }
        return output;
    }

    public void sobelX() {
        applyMask(pixels, MASK_X, gradientsX);
    }

    public void sobelY() {
        applyMask(pixels, MASK_Y, gradientsY);
    }

    private void sobel() {
        double sum = 0;
        double varianceSum = 0; // https://en.wikipedia.org/wiki/Variance#:~:text=variance%20is%20the%20expected%20value,random%20variable
        double pixelCount = height * width;

        double radToDeg = 180 / Math.PI;
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double gX = gradientsX[x][y];
                double gY = gradientsY[x][y];
                gradients[x][y] = Math.sqrt(gX * gX + gY * gY);
                double angle = Math.atan2(gY, gX) * radToDeg;
                angles[x][y] = angle;
                sum += gradients[x][y];
            }
        }

        mean = (int) Math.round(sum / pixelCount);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double deviation = gradients[x][y] - mean;
                varianceSum += (deviation * deviation); // squared deviation from the mean
            }
        }
        double varianceMean = Math.round(varianceSum / pixelCount);

        standardDeviation = (int) Math.sqrt(varianceMean);
    }

    /**
     * The angle sections are 90° off from normal Oxy coordinate
     * 180
     * |
     * --- 90
     * |
     * 0
     * the 45° and 135° section directions are swapped
     */
    private void nonMaximumSuppression() {
        max = nonMaxSuppression[0][0];
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double grad = gradients[x][y], q = grad, r = grad;
                double angle = angles[x][y];
                if (angle < 0) {
                    angle += 180;
                }
                if (angle < 22.5 || angle >= 157.5) {
                    q = gradients[x + 1][y];
                    r = gradients[x - 1][y];
                } else if ((angle >= 22.5 && angle < 67.5)) {
                    q = gradients[x - 1][y - 1];
                    r = gradients[x + 1][y + 1];
                } else if ((angle >= 67.5 && angle < 112.5)) {
                    q = gradients[x][y - 1];
                    r = gradients[x][y + 1];
                } else if ((angle >= 112.5 && angle < 157.5)) {
                    q = gradients[x + 1][y - 1];
                    r = gradients[x - 1][y + 1];
                }
                if (grad >= q && grad >= r) {
                    nonMaxSuppression[x][y] = grad;
                    if (grad >= max) {
                        max = grad;
                    }
                } else {
                    nonMaxSuppression[x][y] = 0;
                }
            }
        }

    }
    double max;
    int[] queue;

    private void doubleThresholdingAndHysteresis() {
        double high = mean + (option.getHighFraction() * standardDeviation);
        double low = high * option.getLowFraction();
        int a = 0;
        int n = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double grad = nonMaxSuppression[x][y];
                if (grad >= high) {
                    doubleThresholding[x][y] = highPixel;
                    queue[n++] = y * width + x;
                } else if (grad >= low) {
                    doubleThresholding[x][y] = lowPixel;
                } else {
                    doubleThresholding[x][y] = 0;
                }
                hysteresis[x][y] = 0;
            }
        }

        while (a < n) {
            int p = queue[a++];
            int x = p % width;
            int y = p / width;
            hysteresis[x][y] = highPixel;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        int x1 = x + i, y1 = y + j;
                        if (doubleThresholding[x1][y1] == lowPixel && hysteresis[x1][y1] != highPixel) {
                            hysteresis[x1][y1] = highPixel;
                            queue[n++] = (y1 * width + x1);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
    }

    private void doubleThresholding() {
        double max = nonMaxSuppression[0][0];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double grad = nonMaxSuppression[x][y];
                if (grad >= max) {
                    max = grad;
                }
            }
        }
        double high = mean + (option.getHighFraction() * standardDeviation);
        double low = high * option.getLowFraction();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double grad = nonMaxSuppression[x][y];
                if (grad >= high) {
                    doubleThresholding[x][y] = highPixel;
                } else if (grad >= low) {
                    doubleThresholding[x][y] = lowPixel;
                } else {
                    doubleThresholding[x][y] = 0;
                }
            }
        }
    }

    private void hysteresis() {
        Queue<int[]> queue = new LinkedList();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                hysteresis[x][y] = doubleThresholding[x][y];
                if (doubleThresholding[x][y] == highPixel) {
                    queue.add(new int[]{x, y});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            int x = p[0], y = p[1];
            hysteresis[x][y] = highPixel;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        if (hysteresis[x + i][y + j] == lowPixel) {
                            hysteresis[x + i][y + j] = highPixel;
                            queue.add(new int[]{x + i, y + j});
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (hysteresis[x][y] != highPixel) {
                    hysteresis[x][y] = 0;
                }
            }
        }
    }

    private void hysteresis2() {
        Queue<Integer> queueX = new LinkedList();
        Queue<Integer> queueY = new LinkedList();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                hysteresis[x][y] = doubleThresholding[x][y];
                if (doubleThresholding[x][y] == highPixel) {
                    queueX.add(x);
                    queueY.add(y);
                }
            }
        }
        while (!queueX.isEmpty() || !queueY.isEmpty()) {
            int x = queueX.poll();
            int y = queueY.poll();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        int x1 = x + i, y1 = y + j;
                        if (hysteresis[x1][y1] == lowPixel) {
                            hysteresis[x1][y1] = highPixel;
                            queueX.add(x1);
                            queueY.add(y1);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (hysteresis[x][y] != highPixel) {
                    hysteresis[x][y] = 0;
                }
            }
        }
    }

    public BufferedImage getGrayScaleImage() {
        return grayScaleImage;
    }

    public BufferedImage getFilteredImage() {
        return filteredImage;
    }

    public BufferedImage getSobelXImage() {
        return getImage(gradientsX);
    }

    public BufferedImage getSobelYImage() {
        return getImage(gradientsY);
    }

    public BufferedImage getSobelImage() {
        return getImage(gradients);
    }

    public BufferedImage getAngleImage() {
        return getImage(angles);
    }

    public BufferedImage getNonMaximumSuppressionImage() {
        return getImage(nonMaxSuppression);
    }

    public BufferedImage getDoubleThesholdingImage() {
        return getImage2(doubleThresholding);
    }

    public BufferedImage getHysteresisImage() {
        return getImage2(hysteresis);
    }

    public BufferedImage getImage(double[][] pixels) {
        int w = pixels.length;
        int h = pixels[0].length;
        BufferedImage image = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        double min = pixels[1][1], max = pixels[1][1];
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                double g = pixels[x][y];
                if (g < min) {
                    min = g;
                }
                if (g > max) {
                    max = g;
                }
            }
        }
        WritableRaster raster = image.getRaster();
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                double g = pixels[x][y];
                int p = (int) interpolate(min, max, g, 0, 255);
                raster.setPixel(x, y, getGrayPixelArray(p));
            }
        }
        return image;
    }

    private BufferedImage getImage(int[][] pixels) {
        int w = pixels.length;
        int h = pixels[0].length;
        BufferedImage image = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        int min = pixels[1][1], max = pixels[1][1];
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int g = pixels[x][y];
                if (g < min) {
                    min = g;
                }
                if (g > max) {
                    max = g;
                }
            }
        }
        WritableRaster raster = image.getRaster();
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                int g = pixels[x][y];
                int p = (int) interpolate(min, max, g, 0, 255);
                raster.setPixels(x, y, 1, 1, getGrayPixelArray(p));
            }
        }
        return image;
    }

    private BufferedImage getImage2(int[][] pixels) {
        int w = pixels.length;
        int h = pixels[0].length;
        BufferedImage image = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int g = pixels[x][y];
                image.setRGB(x, y, getGrayPixel(g));
            }
        }
        return image;
    }

    private int[] getGrayPixelArray(int value) {
        return colorCache.get(value);
    }

    private int getGrayPixel(int value) {
        int v = value;
        if (v < 0) {
            v = 0;
        }
        if (v > 255) {
            v = 255;
        }
        return 0xff000000 | (v << 16) | (v << 8) | v;
    }

    private int getPixel(int pixel) {
        int p = pixel & 0xFF;
        if (p < 0) {
            p = 0;
        }
        if (p > 255) {
            p = 255;
        }
        return p;
    }

    private double inverseLerp(double a, double b, double t) {
        if (b - a == 0) {
            return 0;
        }
        return (t - a) / (b - a);
    }

    private double interpolate(double a, double b, double t, double a1, double b1) {
        double f = inverseLerp(a, b, t);
        double p = (b1 - a1) * f + a1;
        return p;
    }

}
