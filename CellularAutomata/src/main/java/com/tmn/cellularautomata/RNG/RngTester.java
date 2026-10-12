package com.tmn.cellularautomata.RNG;

import java.util.Random;

public class RngTester {

    public static void test(Random rng, int samples) {

        System.out.println("Testing " + rng.getClass().getName());
        System.out.println("Samples = " + samples);
        System.out.println();

        meanAndVarianceTest(rng, samples);
        chiSquareTest(rng, samples);
        bitFrequencyTest(rng, samples);
        serialCorrelationTest(rng, samples);
        bitTransitionTest(rng, samples);
        hammingWeightTest(rng, samples);
        matrixRankTest(rng, samples / 10);
    }

    private static void meanAndVarianceTest(Random rng, int samples) {
        System.out.println("=== Mean / Variance ===");
        double sum = 0;
        double sumSq = 0;

        for (int i = 0; i < samples; i++) {
            double x = rng.nextDouble();
            sum += x;
            sumSq += x * x;
        }

        double mean = sum / samples;
        double variance = sumSq / samples - mean * mean;

        System.out.printf("Mean      = %.8f (expected 0.5)%n", mean);
        System.out.printf("Variance  = %.8f (expected %.8f)%n", variance, 1.0 / 12.0);
        System.out.println();
    }

    private static void chiSquareTest(Random rng, int samples) {
        System.out.println("=== Chi Square ===");
        int buckets = 100;
        long[] freq = new long[buckets];

        for (int i = 0; i < samples; i++) {
            freq[rng.nextInt(buckets)]++;
        }

        double expected = (double) samples / buckets;
        double chiSquare = 0;

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (long f : freq) {
            double diff = f - expected;
            chiSquare += diff * diff / expected;
            min = Math.min(min, f);
            max = Math.max(max, f);
        }

        System.out.printf("Buckets    = %d%n", buckets);
        System.out.printf("ChiSquare  = %.3f%n", chiSquare);
        System.out.printf("Min bucket = %d%n", min);
        System.out.printf("Max bucket = %d%n", max);
        System.out.println();
    }

    private static void bitFrequencyTest(Random rng, int samples) {
        System.out.println("=== Bit Frequency ===");
        long[] ones = new long[64];
        for (int i = 0; i < samples; i++) {
            long x = rng.nextLong();
            for (int bit = 0; bit < 64; bit++) {
                if (((x >>> bit) & 1L) != 0) {
                    ones[bit]++;
                }
            }
        }
        double ideal = samples / 2.0;
        double largestError = 0;
        int worstBit = 0;

        for (int bit = 0; bit < 64; bit++) {
            double errorPct = Math.abs(ones[bit] - ideal) * 100.0 / ideal;
            if (errorPct > largestError) {
                largestError = errorPct;
                worstBit = bit;
            }
        }

        double sigmaPct = 50.0 / Math.sqrt(samples);

        System.out.printf("Worst bit        = %d%n", worstBit);
        System.out.printf("Largest error    = %.4f%%%n", largestError);
        System.out.printf("Expected 1phi    = %.4f%%%n", sigmaPct);
        System.out.printf("Expected max about %.4f%%%n", sigmaPct * Math.sqrt(2.0 * Math.log(64.0)));
        System.out.printf("Z-score          = %.2f%n", largestError / sigmaPct);
        System.out.println();
    }

    private static void serialCorrelationTest(Random rng, int samples) {
        System.out.println("=== Serial Correlation ===");
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        double sumY2 = 0;

        double prev = rng.nextDouble();

        for (int i = 1; i < samples; i++) {
            double curr = rng.nextDouble();
            sumX += prev;
            sumY += curr;
            sumXY += prev * curr;
            sumX2 += prev * prev;
            sumY2 += curr * curr;
            prev = curr;
        }

        long n = samples - 1;
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        double correlation = numerator / denominator;

        System.out.printf("Lag-1 correlation = %.8f%n", correlation);
        System.out.println();
    }

    public static void bitTransitionTest(Random rng, int samples) {
        System.out.println("=== Bit Transition Test ===");
        long prev = rng.nextLong();
        for (int bit = 0; bit < 64; bit++) {
            long[] counts = new long[4];
            long p = (prev >>> bit) & 1L;
            long last = prev;
            for (int i = 1; i < samples; i++) {
                long curr = rng.nextLong();
                long c = (curr >>> bit) & 1L;
                counts[(int) ((p << 1) | c)]++;
                p = c;
                last = curr;
            }
            long total = counts[0] + counts[1] + counts[2] + counts[3];
            double worst = 0;
            for (long count : counts) {
                double err = Math.abs(count * 4.0 / total - 1);
                worst = Math.max(worst, err);
            }
            if (worst > 0.01) {
                System.out.printf("bit=%2d worstError=%.4f%%%n", bit, worst * 100);
            }
            prev = last;
        }
        System.out.println();
    }

    public static void hammingWeightTest(Random rng, int samples) {
        System.out.println("=== Hamming Weight Test ===");
        double sum = 0;
        double sumSq = 0;

        for (int i = 0; i < samples; i++) {
            int bits = Long.bitCount(rng.nextLong());
            sum += bits;
            sumSq += bits * bits;
        }

        double mean = sum / samples;
        double variance = sumSq / samples - mean * mean;
        double stddev = Math.sqrt(variance);

        System.out.printf("Mean   = %.4f (expected 32)%n", mean);
        System.out.printf("StdDev = %.4f (expected 4)%n", stddev);
        System.out.println();
    }

    private static int rank32(long[] rows) {
        int rank = 0;
        for (int col = 31; col >= 0; col--) {
            int pivot = -1;
            for (int r = rank; r < 32; r++) {
                if (((rows[r] >>> col) & 1) != 0) {
                    pivot = r;
                    break;
                }
            }
            if (pivot == -1) {
                continue;
            }
            long tmp = rows[rank];
            rows[rank] = rows[pivot];
            rows[pivot] = tmp;
            for (int r = 0; r < 32; r++) {
                if (r == rank) {
                    continue;
                }
                if (((rows[r] >>> col) & 1) != 0) {
                    rows[r] ^= rows[rank];
                }
            }
            rank++;
        }
        return rank;
    }

    public static void matrixRankTest(Random rng, int samples) {
        long rank32 = 0;
        long rank31 = 0;
        long lower = 0;

        for (int m = 0; m < samples; m++) {
            long[] rows = new long[32];
            for (int r = 0; r < 32; r++) {
                rows[r] = rng.nextInt();
            }
            int rank = rank32(rows);
            switch (rank) {
                case 32 ->
                    rank32++;
                case 31 ->
                    rank31++;
                default ->
                    lower++;
            }
        }

        double p32 = 0.288788095;
        double p31 = 0.577576191;
        double p30 = 0.133635714;

        double e32 = samples * p32;
        double e31 = samples * p31;
        double e30 = samples * p30;

        System.out.println("=== Matrix Rank Test ===");
        System.out.printf("Full rank      = %,d (expected %,d)%n", rank32, Math.round(e32));
        System.out.printf("Rank 31        = %,d (expected %,d)%n", rank31, Math.round(e31));
        System.out.printf("Rank <= 30     = %,d (expected %,d)%n", lower, Math.round(e30));
        double chi = sq(rank32 - e32) / e32 + sq(rank31 - e31) / e31 + sq(lower - e30) / e30;
        System.out.printf("ChiSquare      = %.3f%n", chi);
    }

    private static double sq(double x) {
        return x * x;
    }

    public static void main(String[] args) {

//        test(new CARandom2(0), 10_000_000);
//        test(new CARandom2(1), 10_000_000);
//        test(new CARandom2(2), 10_000_000);
        test(new Random(~1515151515151515151L), 10_000_000);
    }
}
