package com.tmn.cellularautomata.RNG;

import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OccupancyTest {

    public static void test2DOccupancy(Random rng, int samples) {

        final int GRID = 256;

        long[][] cells = new long[GRID][GRID];

        double prev = rng.nextDouble();

        for (int i = 1; i < samples; i++) {

            double curr = rng.nextDouble();

            int x = (int) (prev * GRID);
            int y = (int) (curr * GRID);

            cells[x][y]++;

            prev = curr;
        }

        long totalPairs = samples - 1;

        double expected = (double) totalPairs / (GRID * GRID);

        double chiSquare = 0;

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        int empty = 0;

        for (int x = 0; x < GRID; x++) {
            for (int y = 0; y < GRID; y++) {

                long count = cells[x][y];

                if (count == 0) {
                    empty++;
                }

                min = Math.min(min, count);
                max = Math.max(max, count);

                double diff = count - expected;
                chiSquare += diff * diff / expected;
            }
        }

        System.out.println("=== 2D Occupancy Test ===");
        System.out.printf("Grid          = %d x %d%n", GRID, GRID);
        System.out.printf("Pairs         = %d%n", totalPairs);
        System.out.printf("Expected/cell = %.2f%n", expected);
        System.out.printf("Min           = %d%n", min);
        System.out.printf("Max           = %d%n", max);
        System.out.printf("Empty cells   = %d%n", empty);
        System.out.printf("ChiSquare     = %.2f%n", chiSquare);
        System.out.println();
    }

    public static long[][] buildOccupancyGrid(Random rng, int samples, int shift) {
        final int GRID = 256;
        long[][] cells = new long[GRID][GRID];
        long prev = rng.nextLong();
        for (int i = 1; i < samples; i++) {
            long curr = rng.nextLong();
            int x = (int) ((prev >>> shift) & 0xFF);
            int y = (int) ((curr >>> shift) & 0xFF);
            cells[x][y]++;
            prev = curr;
        }
        return cells;
    }

    public static void saveHeatmap(long[][] cells, String filename) throws IOException {
        int width = cells.length;
        int height = cells[0].length;

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (long[] row : cells) {
            for (long v : row) {
                min = Math.min(min, v);
                max = Math.max(max, v);
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double t = (cells[x][y] - min) / (double) (max - min);
                int rgb = heatColor(t);
                image.setRGB(x, height - 1 - y, rgb);
            }
        }

        ImageIO.write(image, "png", new File(filename));

        System.out.println("Heatmap saved to " + filename + " (min=" + min + ", max=" + max + ")");
    }

    private static int heatColor(double t) {

        t = Math.max(0, Math.min(1, t));

        int r, g, b;

        if (t < 0.25) {
            r = 0;
            g = (int) (255 * (t / 0.25));
            b = 255;
        } else if (t < 0.50) {
            r = 0;
            g = 255;
            b = (int) (255 * (1 - (t - 0.25) / 0.25));
        } else if (t < 0.75) {
            r = (int) (255 * ((t - 0.50) / 0.25));
            g = 255;
            b = 0;
        } else {
            r = 255;
            g = (int) (255 * (1 - (t - 0.75) / 0.25));
            b = 0;
        }

        return (r << 16) | (g << 8) | b;
    }

    public static void main(String[] args) throws IOException {
        OccupancyTest.test2DOccupancy(new CARandom2(1234), 10_000_000);
        long[][] cells = buildOccupancyGrid(new CARandom2(1234), 10_000_000, 16);
        saveHeatmap(cells, "occupancy.png");
    }
}
