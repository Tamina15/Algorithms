package com.tmn.edgedetection;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;

public class CannyEdgeDetection1 {

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

    public static BufferedImage filter(BufferedImage sourceImage, boolean verbose) throws InterruptedException, ExecutionException {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        double[][] array1 = new double[height][width];
        double[][] array2 = new double[height][width];
        double[][] array3 = new double[height][width];

        Raster raster = sourceImage.getData();
        long start, end;
        double total = 0;

        start = System.nanoTime();

        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int row = 0; row < height; row++) {
                int r = row;
                myExecutor.submit(() -> {
                    int[] temp = new int[width * 3];
                    raster.getPixels(0, r, width, 1, temp);
                    for (int col = 0; col < width; col++) {
                        int t = col * 3;
                        int red = temp[t];
                        int green = temp[t + 1];
                        int blue = temp[t + 2];
                        array1[r][col] = (red + blue + green) / 3;
                    }
                });
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;
        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("get array: " + z);
        }

        start = System.nanoTime();

        int k = 3;
        double sigma = Math.sqrt(2);
        double two_sigma_squared = 2 * sigma * sigma;
        double invesred_two_pi_sigma_squared = 1 / (Math.PI * two_sigma_squared);
        double norm = 0;
        int l = 2 * k + 1;
        double[][] gaussianKernel = new double[l][l];
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                int x = i - k;
                int y = j - k;
                double exp = Math.exp(-((x * x + y * y) / two_sigma_squared));
                gaussianKernel[i][j] = invesred_two_pi_sigma_squared * exp;
                norm += gaussianKernel[i][j];
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("gaussian kernel: " + z);
        }

        start = System.nanoTime();

        double norm1 = norm;
        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int row = 0; row < height - 2 * k; row++) {
                int r = row;
                myExecutor.submit(() -> {
                    for (int col = 0; col < width - 2 * k; col++) {
                        int sum = 0;
                        for (int i = 0; i < l; i++) {
                            for (int j = 0; j < l; j++) {
                                sum += (gaussianKernel[i][j] * array1[r + i][col + j]);
                            }
                        }
                        array2[r + k][col + k] = sum / norm1;
                    }
                });
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("blur: " + z);
        }

        start = System.nanoTime();

        double sum = 0;
        double pixelCount = height * width;
        double radToDeg = 180 / Math.PI;
        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Double>> futures = new ArrayList<>(height - k - 1);
            for (int row = k + 1; row < height - k - 1; row++) {
                int r = row;
                Future<Double> future = myExecutor.submit(() -> {
                    double sum1 = 0.;
                    for (int col = k + 1; col < width - k - 1; col++) {
                        int gX = 0;
                        int gY = 0;
                        for (int i = -1; i < 2; i++) {
                            for (int j = -1; j < 2; j++) {
                                double a2 = array2[r + i][col + j];
                                gX += (MASK_X[i + 1][j + 1] * a2);
                                gY += (MASK_Y[i + 1][j + 1] * a2);
                            }
                        }
                        double gradient = Math.sqrt(gX * gX + gY * gY);
                        array1[r][col] = gradient;
                        sum1 += gradient;
                        double angle = Math.atan2(gY, gX) * radToDeg;
                        array3[r][col] = angle;
                    }
                    return sum1;
                });
                futures.add(future);
            }

            for (Future<Double> f : futures) {
                sum += f.get();
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("sobel: " + z);
        }

        start = System.nanoTime();

        int mean = (int) Math.round(sum / pixelCount);
        double varianceSum = 0; // https://en.wikipedia.org/wiki/Variance#:~:text=variance%20is%20the%20expected%20value,random%20variable
        for (int row = k + 1; row < height - k - 1; row++) {
            for (int col = k + 1; col < width - k - 1; col++) {
                double deviation = array1[row][col] - mean;
                varianceSum += (deviation * deviation); // squared deviation from the mean
            }
        }
        double varianceMean = Math.round(varianceSum / pixelCount);
        int standardDeviation = (int) Math.sqrt(varianceMean);

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("variance: " + z);
        }

        start = System.nanoTime();

        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int row = k + 1; row < height - k - 1; row++) {
                int r = row;
                myExecutor.submit(() -> {
                    for (int col = k + 1; col < width - k - 1; col++) {
                        double grad = array1[r][col], q = grad, p = grad;
                        double angle = array3[r][col];
                        if (angle < 0) {
                            angle += 180;
                        }
                        if (angle < 22.5 || angle >= 157.5) {
                            q = array1[r + 1][col];
                            p = array1[r - 1][col];
                        } else if ((angle >= 22.5 && angle < 67.5)) {
                            q = array1[r - 1][col - 1];
                            p = array1[r + 1][col + 1];
                        } else if ((angle >= 67.5 && angle < 112.5)) {
                            q = array1[r][col - 1];
                            p = array1[r][col + 1];
                        } else if ((angle >= 112.5 && angle < 157.5)) {
                            q = array1[r + 1][col - 1];
                            p = array1[r - 1][col + 1];
                        }
                        if (grad >= q && grad >= p) {
                            array2[r][col] = grad;
                        } else {
                            array2[r][col] = 0;
                        }
                    }
                });
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("nonmax suppression: " + z);
        }

        start = System.nanoTime();

        int[] arrayQueue = new int[height * width];
        int highPixel = 255, lowPixel = 150;
        double numDev = 1;
        double lowFraction = 0.3;
        double high = mean + (numDev * standardDeviation);
        double low = high * lowFraction;
        int a = 0;
        int n = 0;

        for (int row = k + 1; row < height - k - 1; row++) {
            int r = row;
            for (int col = k + 1; col < width - k - 1; col++) {
                double grad = array2[r][col];
                if (grad >= high) {
                    array1[r][col] = highPixel;
                    arrayQueue[n++] = r * width + col;
                } else if (grad >= low) {
                    array1[r][col] = lowPixel;
                } else {
                    array1[r][col] = 0;
                }
                array3[r][col] = 0;
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("add queue: " + z);
        }

        start = System.nanoTime();

        int[] out = new int[height * width];
        while (a < n) {
            int p = arrayQueue[a++];
            int row = p / width;
            int col = p % width;
            out[p] = highPixel;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (row > 0 && row < height - 1 && col > 0 && col < width - 1) {
                        int row1 = row + i, col1 = col + j;
                        int q = row1 * width + col1;
                        if (array1[row1][col1] == lowPixel && out[q] != highPixel) {
                            out[q] = highPixel;
                            arrayQueue[n++] = (q);
                        }
                    }
                }
            }
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("hysteresis: " + z);
        }

        start = System.nanoTime();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        output.getRaster().setPixels(0, 0, width, height, out);

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("ouput: " + z);
            System.out.println("total: " + total);
        }

        return output;
    }

    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException, ExecutionException {
        URL url = URI.create("https://picsum.photos/2500").toURL();
        File file = new File("sample.jpg");
        BufferedImage image = ImageIO.read(file);

//        for (int i = 0; i < 20; i++) {
//            long start = System.nanoTime();
//            CannyEdgeDetection1.filter(image, false);
//            long end = System.nanoTime();
//            System.out.println((end - start) * 1.0 / 1000000);
//        }

        long start = System.nanoTime();
        BufferedImage output = CannyEdgeDetection1.filter(image, true);
        long end = System.nanoTime();
        System.out.println((end - start) * 1.0 / 1000000);

        File out = new File("out-sample.jpg");
        ImageIO.write(output, "jpg", out);

    }

    public static BufferedImage getImage(double[][] pixels) {
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
    private static final Map<Integer, int[]> grayArrayCache = new HashMap<>();

    static {
        for (int i = 0; i <= 255; i++) {
            grayArrayCache.put(i, new int[]{i, i, i});
        }
    }

    private static int[] getGrayPixelArray(int value) {
        return grayArrayCache.get(value);
    }

    private static double inverseLerp(double a, double b, double t) {
        if (b - a == 0) {
            return 0;
        }
        return (t - a) / (b - a);
    }

    private static double interpolate(double a, double b, double t, double a1, double b1) {
        double f = inverseLerp(a, b, t);
        double p = (b1 - a1) * f + a1;
        return p;
    }

}
