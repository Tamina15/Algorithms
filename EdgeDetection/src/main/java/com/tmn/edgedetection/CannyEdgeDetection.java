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

    private static final Map<Integer, int[]> grayArrayCache = new HashMap<>();
    private static final Map<Integer, Integer> grayCache = new HashMap<>();
    private final BufferedImage original;
    private BufferedImage grayScaleImage, doubleThresholdingImage, hysteresisImage, filteredImage;
    private final int width, height;

    private final Option option;

    private final int[][] pixels, gradientsX, gradientsY, doubleThresholding, hysteresis;
    private final double[][] gradients, angles, nonMaxSuppression;

    // https://github.com/rstreet85/JCanny
    private int mean; // https://en.wikipedia.org/wiki/Arithmetic_mean#:~:text=mean%20or%20average,numbers%20in%20the%20collection
    // The standard deviation of a random variable is the square root of its variance.
    private int standardDeviation;// https://en.wikipedia.org/wiki/Standard_deviation

    private final int highPixel = 255, lowPixel = 150;

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
            grayArrayCache.put(i, new int[]{i, i, i});
            grayCache.put(i, 0xff000000 | (i << 16) | (i << 8) | i);
        }
    }

    public CannyEdgeDetection(BufferedImage original, Option option) {
        this.original = original;
        this.option = option;

        this.width = original.getWidth();
        this.height = original.getHeight();

        this.pixels = new int[height][width];
        this.gradientsX = new int[height][width];
        this.gradientsY = new int[height][width];
        this.gradients = new double[height][width];
        this.angles = new double[height][width];
        this.nonMaxSuppression = new double[height][width];
        this.doubleThresholding = new int[height][width];
        this.hysteresis = new int[height][width];

        doubleThresholdingImage = new BufferedImage(width, height, TYPE_BYTE_GRAY);
        hysteresisImage = new BufferedImage(width, height, TYPE_BYTE_GRAY);

        arrayQueue = new int[width * height];

        setPixels();
        filter0();
    }

    private void setPixels() {
        grayScaleImage = getGrayScaleImage(original);
        grayScaleImage = applyGaussianBlur(grayScaleImage);
        Raster raster = grayScaleImage.getRaster();
        int[] data = raster.getPixels(0, 0, width, height, (int[]) null);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixels[row][col] = data[row * width + col];
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
     * @param out  the output array
     * @return the {@code out} array
     */
    private int[][] applyMask(int[][] input, int[][] mask, int[][] output) {
        if (input == null) {
            return null;
        }
        int w = input.length;
        int h = input[0].length;
        if (w < 3 || h < 3) {
            return null;
        }
        if (output == null) {
            output = new int[w][h];
        }
        for (int row = 0; row < height - 2; row++) {
            for (int col = 0; col < width - 2; col++) {
                int sum = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        sum += (mask[i][j] * input[row + i][col + j]);
                    }
                }
                sum = sum / 9;
                output[row + 1][col + 1] = sum;
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

    public void sobel() {
        double sum = 0;
        double varianceSum = 0; // https://en.wikipedia.org/wiki/Variance#:~:text=variance%20is%20the%20expected%20value,random%20variable
        double pixelCount = height * width;

        double radToDeg = 180 / Math.PI;
        for (int col = 1; col < height - 1; col++) {
            for (int row = 1; row < width - 1; row++) {
                double gX = gradientsX[col][row];
                double gY = gradientsY[col][row];
                gradients[col][row] = Math.sqrt(gX * gX + gY * gY);
                double angle = Math.atan2(gY, gX) * radToDeg;
                angles[col][row] = angle;
                sum += gradients[col][row];
            }
        }

        mean = (int) Math.round(sum / pixelCount);
        for (int col = 1; col < height - 1; col++) {
            for (int row = 1; row < width - 1; row++) {
                double deviation = gradients[col][row] - mean;
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
    public void nonMaximumSuppression() {
        for (int col = 1; col < height - 1; col++) {
            for (int row = 1; row < width - 1; row++) {
                double grad = gradients[col][row], q = grad, r = grad;
                double angle = angles[col][row];
                if (angle < 0) {
                    angle += 180;
                }
                if (angle < 22.5 || angle >= 157.5) {
                    q = gradients[col + 1][row];
                    r = gradients[col - 1][row];
                } else if ((angle >= 22.5 && angle < 67.5)) {
                    q = gradients[col - 1][row - 1];
                    r = gradients[col + 1][row + 1];
                } else if ((angle >= 67.5 && angle < 112.5)) {
                    q = gradients[col][row - 1];
                    r = gradients[col][row + 1];
                } else if ((angle >= 112.5 && angle < 157.5)) {
                    q = gradients[col + 1][row - 1];
                    r = gradients[col - 1][row + 1];
                }
                if (grad >= q && grad >= r) {
                    nonMaxSuppression[col][row] = grad;
                } else {
                    nonMaxSuppression[col][row] = 0;
                }
            }
        }

    }

    int[] arrayQueue;

    private void doubleThresholdingAndHysteresis() {
        double high = mean + (option.getNumDev() * standardDeviation);
        double low = high * option.getLowFraction();
        int a = 0;
        int n = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double grad = nonMaxSuppression[row][col];
                if (grad >= high) {
                    doubleThresholding[row][col] = highPixel;
                    arrayQueue[n++] = row * width + col;
                } else if (grad >= low) {
                    doubleThresholding[row][col] = lowPixel;
                } else {
                    doubleThresholding[row][col] = 0;
                }
                hysteresis[row][col] = 0;
            }
        }

        while (a < n) {
            int p = arrayQueue[a++];
            int row = p / width;
            int col = p % width;
            hysteresis[row][col] = highPixel;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        int row1 = row + i, col1 = col + j;
                        if (doubleThresholding[row1][col1] == lowPixel && hysteresis[row1][col1] != highPixel) {
                            hysteresis[row1][col1] = highPixel;
                            arrayQueue[n++] = (row1 * width + col1);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    public void doubleThresholding() {
        double high = mean + (option.getNumDev() * standardDeviation);
        double low = high * option.getLowFraction();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double grad = nonMaxSuppression[row][col];
                if (grad >= high) {
                    doubleThresholding[row][col] = highPixel;
                } else if (grad >= low) {
                    doubleThresholding[row][col] = lowPixel;
                } else {
                    doubleThresholding[row][col] = 0;
                }
            }
        }
    }

    public void hysteresis() {
        Queue<int[]> queue = new LinkedList();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                hysteresis[row][col] = doubleThresholding[row][col];
                if (doubleThresholding[row][col] == highPixel) {
                    queue.add(new int[]{row, col});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            int row = p[0], col = p[1];
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {
                        int row1 = row + i, col1 = col + j;
                        if (hysteresis[row1][col1] == lowPixel) {
                            hysteresis[row1][col1] = highPixel;
                            queue.add(new int[]{row1, col1});
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (hysteresis[row][col] != highPixel) {
                    hysteresis[row][col] = 0;
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
        return getImage2(doubleThresholding, doubleThresholdingImage);
    }

    public BufferedImage getHysteresisImage() {
        return getImage2(hysteresis, hysteresisImage);
    }

    public BufferedImage getImage(double[][] pixels) {
        int h = pixels.length;
        int w = pixels[0].length;
        BufferedImage image = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        double min = pixels[1][1], max = pixels[1][1];
        for (int row = 1; row < h - 1; row++) {
            for (int col = 1; col < w - 1; col++) {
                double g = pixels[row][col];
                if (g < min) {
                    min = g;
                }
                if (g > max) {
                    max = g;
                }
            }
        }
        WritableRaster raster = image.getRaster();
        for (int row = 1; row < h - 1; row++) {
            for (int col = 1; col < w - 1; col++) {
                double g = pixels[row][col];
                int p = (int) interpolate(min, max, g, 0, 255);
                raster.setPixel(col, row, getGrayPixelArray(p));
            }
        }
        return image;
    }

    private BufferedImage getImage(int[][] pixels) {
        int h = pixels.length;
        int w = pixels[0].length;
        BufferedImage image = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        double min = pixels[1][1], max = pixels[1][1];
        for (int row = 1; row < h - 1; row++) {
            for (int col = 1; col < w - 1; col++) {
                double g = pixels[row][col];
                if (g < min) {
                    min = g;
                }
                if (g > max) {
                    max = g;
                }
            }
        }
        WritableRaster raster = image.getRaster();
        for (int row = 1; row < h - 1; row++) {
            for (int col = 1; col < w - 1; col++) {
                double g = pixels[row][col];
                int p = (int) interpolate(min, max, g, 0, 255);
                raster.setPixel(col, row, getGrayPixelArray(p));
            }
        }
        return image;
    }

    private BufferedImage getImage2(int[][] pixels, BufferedImage image) {
        int h = pixels.length;
        int w = pixels[0].length;
        if (image == null) {
            image = new BufferedImage(w, h, TYPE_BYTE_GRAY);
        }
        WritableRaster raster = image.getRaster();
        for (int row = 0; row < h; row++) {
            int[] g = pixels[row];
            raster.setPixels(0, row, w, 1, g);
        }
        return image;
    }

    private int[] getGrayPixelArray(int value) {
        return grayArrayCache.get(value);
    }

    private int getGrayPixel(int value) {
        if (value < 0) {
            return grayCache.get(0);
        }
        if (value > 255) {
            return grayCache.get(0);
        }
        return grayCache.get(value);
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
