package com.tmn.edgedetection;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

public class CannyEdgeDetection2 {

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

    public static BufferedImage filter2(BufferedImage sourceImage, boolean verbose) throws InterruptedException, ExecutionException {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        int[] dstData1 = new int[height * width];
        int[] dstData2 = new int[height * width];
        long start, end;
        double total = 0;

        start = System.nanoTime();

        byte[] srcData = ((DataBufferByte) sourceImage.getRaster().getDataBuffer()).getData();
        int si = 0;
        for (int di = 0; di < dstData1.length; di++) {
            int b = srcData[si++] & 0xFF;
            int g = srcData[si++] & 0xFF;
            int r = srcData[si++] & 0xFF;
            dstData1[di] = ((77 * r + 150 * g + 29 * b) >> 8);
        }

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;
        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("get array: " + z);
        }

        start = System.nanoTime();

        int size = 5;
        int[] gaussianKernel = CannyEdgeDetection2.gaussianPascalKernel(size);
        int norm = 1 << (size - 1);

        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;
        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("gaussian kernel: " + z);
        }

        start = System.nanoTime();

        CannyEdgeDetection2.gaussianHorizontal(dstData1, dstData2, width, height, gaussianKernel, norm);
        CannyEdgeDetection2.gaussianVertical(dstData2, dstData1, width, height, gaussianKernel, norm);
        end = System.nanoTime();
        total += (end - start) * 1.0 / 1000000;

        if (verbose) {
            double z = (end - start) * 1.0 / 1000000;
            System.out.println("blur: " + z);
        }
//
//        start = System.nanoTime();
//
//        double sum = 0;
//        double pixelCount = height * width;
//        double radToDeg = 180 / Math.PI;
//        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
//            List<Future<Double>> futures = new ArrayList<>(height - k - 1);
//            for (int row = k + 1; row < height - k - 1; row++) {
//                int r = row;
//                Future<Double> future = myExecutor.submit(() -> {
//                    double sum1 = 0.;
//                    for (int col = k + 1; col < width - k - 1; col++) {
//                        int gX = 0;
//                        int gY = 0;
//                        for (int i = -1; i < 2; i++) {
//                            for (int j = -1; j < 2; j++) {
//                                double a2 = array2[r + i][col + j];
//                                gX += (MASK_X[i + 1][j + 1] * a2);
//                                gY += (MASK_Y[i + 1][j + 1] * a2);
//                            }
//                        }
//                        double gradient = Math.sqrt(gX * gX + gY * gY);
//                        array1[r][col] = gradient;
//                        sum1 += gradient;
//                        double angle = Math.atan2(gY, gX) * radToDeg;
//                        array3[r][col] = angle;
//                    }
//                    return sum1;
//                });
//                futures.add(future);
//            }
//
//            for (Future<Double> f : futures) {
//                sum += f.get();
//            }
//        }
//
//        end = System.nanoTime();
//        total += (end - start) * 1.0 / 1000000;
//
//        if (verbose) {
//            double z = (end - start) * 1.0 / 1000000;
//            System.out.println("sobel: " + z);
//        }
//
//        start = System.nanoTime();
//
//        int mean = (int) Math.round(sum / pixelCount);
//        double varianceSum = 0; // https://en.wikipedia.org/wiki/Variance#:~:text=variance%20is%20the%20expected%20value,random%20variable
//        for (int row = k + 1; row < height - k - 1; row++) {
//            for (int col = k + 1; col < width - k - 1; col++) {
//                double deviation = array1[row][col] - mean;
//                varianceSum += (deviation * deviation); // squared deviation from the mean
//            }
//        }
//        double varianceMean = Math.round(varianceSum / pixelCount);
//        int standardDeviation = (int) Math.sqrt(varianceMean);
//
//        end = System.nanoTime();
//        total += (end - start) * 1.0 / 1000000;
//
//        if (verbose) {
//            double z = (end - start) * 1.0 / 1000000;
//            System.out.println("variance: " + z);
//        }
//
//        start = System.nanoTime();
//
//        try (ExecutorService myExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
//            for (int row = k + 1; row < height - k - 1; row++) {
//                int r = row;
//                myExecutor.submit(() -> {
//                    for (int col = k + 1; col < width - k - 1; col++) {
//                        double grad = array1[r][col], q = grad, p = grad;
//                        double angle = array3[r][col];
//                        if (angle < 0) {
//                            angle += 180;
//                        }
//                        if (angle < 22.5 || angle >= 157.5) {
//                            q = array1[r + 1][col];
//                            p = array1[r - 1][col];
//                        } else if ((angle >= 22.5 && angle < 67.5)) {
//                            q = array1[r - 1][col - 1];
//                            p = array1[r + 1][col + 1];
//                        } else if ((angle >= 67.5 && angle < 112.5)) {
//                            q = array1[r][col - 1];
//                            p = array1[r][col + 1];
//                        } else if ((angle >= 112.5 && angle < 157.5)) {
//                            q = array1[r + 1][col - 1];
//                            p = array1[r - 1][col + 1];
//                        }
//                        if (grad >= q && grad >= p) {
//                            array2[r][col] = grad;
//                        } else {
//                            array2[r][col] = 0;
//                        }
//                    }
//                });
//            }
//        }
//
//        end = System.nanoTime();
//        total += (end - start) * 1.0 / 1000000;
//
//        if (verbose) {
//            double z = (end - start) * 1.0 / 1000000;
//            System.out.println("nonmax suppression: " + z);
//        }
//
//        start = System.nanoTime();
//
//        int[] arrayQueue = new int[height * width];
//        int highPixel = 255, lowPixel = 150;
//        double numDev = 1;
//        double lowFraction = 0.3;
//        double high = mean + (numDev * standardDeviation);
//        double low = high * lowFraction;
//        int a = 0;
//        int n = 0;
//
//        for (int row = k + 1; row < height - k - 1; row++) {
//            int r = row;
//            for (int col = k + 1; col < width - k - 1; col++) {
//                double grad = array2[r][col];
//                if (grad >= high) {
//                    array1[r][col] = highPixel;
//                    arrayQueue[n++] = r * width + col;
//                } else if (grad >= low) {
//                    array1[r][col] = lowPixel;
//                } else {
//                    array1[r][col] = 0;
//                }
//                array3[r][col] = 0;
//            }
//        }
//
//        end = System.nanoTime();
//        total += (end - start) * 1.0 / 1000000;
//
//        if (verbose) {
//            double z = (end - start) * 1.0 / 1000000;
//            System.out.println("add queue: " + z);
//        }
//
//        start = System.nanoTime();
//
//        int[] out = new int[height * width];
//        while (a < n) {
//            int p = arrayQueue[a++];
//            int row = p / width;
//            int col = p % width;
//            out[p] = highPixel;
//            for (int i = -1; i < 2; i++) {
//                for (int j = -1; j < 2; j++) {
//                    if (row > 0 && row < height - 1 && col > 0 && col < width - 1) {
//                        int row1 = row + i, col1 = col + j;
//                        int q = row1 * width + col1;
//                        if (array1[row1][col1] == lowPixel && out[q] != highPixel) {
//                            out[q] = highPixel;
//                            arrayQueue[n++] = (q);
//                        }
//                    }
//                }
//            }
//        }
//
//        end = System.nanoTime();
//        total += (end - start) * 1.0 / 1000000;
//
//        if (verbose) {
//            double z = (end - start) * 1.0 / 1000000;
//            System.out.println("hysteresis: " + z);
//        }
//
//        start = System.nanoTime();
//
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        output.getRaster().setPixels(0, 0, width, height, dstData1);
//
//        end = System.nanoTime();
//        total += (end - start) * 1.0 / 1000000;
//
//        if (verbose) {
//            double z = (end - start) * 1.0 / 1000000;
//            System.out.println("ouput: " + z);
//            System.out.println("total: " + total);
//        }
//
        return output;
    }

    public static double[] gaussianKernel(int size, double sigma) {
        double[] kernel = new double[size];
        int radius = size / 2;
        double sum = 0;
        for (int i = 0; i < size; i++) {
            int x = i - radius;
            kernel[i] = Math.exp(-(x * x) / (2 * sigma * sigma));
            sum += kernel[i];
        }
        for (int i = 0; i < size; i++) {
            kernel[i] /= sum;
        }
        return kernel;
    }

    public static int[] gaussianPascalKernel(int size) {
        if ((size & 1) == 0 || size < 3) {
            throw new IllegalArgumentException(
                    "Size must be odd and >= 3");
        }
        int[] kernel = new int[size];
        int n = size - 1;
        kernel[0] = 1;
        for (int i = 1; i <= n; i++) {
            kernel[i] = (int) ((long) kernel[i - 1] * (n - i + 1) / i);
        }
        return kernel;
    }

    static void gaussianHorizontal(int[] dstData1, int[] dstData2, int width, int height, int[] kernel, int kernelSum) {
        int radius = kernel.length >> 1;
        int kernelLength = kernel.length;

        for (int y = 0, row = 0; y < height; y++, row += width) {
            int rowEnd = row + width - radius;
            for (int idx = row + radius; idx < rowEnd; idx++) {
                int sum = 0;
                int srcPos = idx - radius;
                for (int k = 0; k < kernelLength; k++) {
                    sum += dstData1[srcPos++] * kernel[k];
                }
                dstData2[idx] = sum / kernelSum;
            }
        }
    }

    static void gaussianVertical(int[] dstData2, int[] dstData1, int width, int height, int[] kernel, int kernelSum) {
        final int radius = kernel.length >> 1;
        final int kernelLength = kernel.length;
        for (int y = radius; y < height - radius; y++) {
            int row = y * width;
            for (int x = 0; x < width; x++) {
                int idx = row + x;
                int sum = 0;
                int srcPos = idx - radius * width;
                for (int k = 0; k < kernelLength; k++) {
                    sum += dstData2[srcPos] * kernel[k];
                    srcPos += width;
                }
                dstData1[idx] = sum / kernelSum;
            }
        }
    }

//    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException, ExecutionException {
//        URL url = URI.create("https://picsum.photos/2500").toURL();
//        File file = new File("sample-6.jpg");
//        BufferedImage image = ImageIO.read(file);
//
//        for (int i = 0; i < 20; i++) {
//            long start = System.nanoTime();
//            CannyEdgeDetection2.filter2(image, false);
//            long end = System.nanoTime();
//            System.out.println((end - start) * 1.0 / 1000000);
//        }
//        long start = System.nanoTime();
//        BufferedImage output = CannyEdgeDetection2.filter2(image, true);
//        long end = System.nanoTime();
//        System.out.println((end - start) * 1.0 / 1000000);
//
//        File out = new File("out-sample.jpg");
//        ImageIO.write(output, "jpg", out);
//
//    }
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

    public static void main(String[] args) {
        var a = BigDecimal.valueOf(383);
        var b = BigDecimal.valueOf(493);
        System.out.println(a.pow(269).remainder(b));
    }
}
