/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Tran Minh Nhat
 */
public class KhoangCachNganNhat {

    Scanner scanner = new Scanner(System.in);
    Point[] points;
    ArrayList<Point> ps = new ArrayList();
    Random random = new Random();
    File f = new File("src/main/java/com/mycompany/algorithm/point.txt");

    public KhoangCachNganNhat() {
    }

    public KhoangCachNganNhat(int n, int dis) {
        //RandomArray(n, dis);
    }

    public void Random() throws IOException {
        FileWriter w = new FileWriter(f);
        for (int i = 0; i < 10000; i++) {
            w.write((random.nextInt(Integer.MAX_VALUE)) + " " + (random.nextInt(Integer.MAX_VALUE)) + "\n");
            w.flush();
        }
        w.close();
    }

    public void File() throws IOException {
        Scanner sc = new Scanner(f);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            StringTokenizer st = new StringTokenizer(s, " ");
            Point p = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            ps.add(p);
        }
        points = new Point[ps.size()];
        points = ps.toArray(points);
    }

    public void RandomArray(int n, int dis) {
        points = new Point[n];
        
        for (int i = 0; i < n; i++) {
            points[i] = new Point();
            points[i].x = random.nextInt(dis) + 1;
            points[i].y = random.nextInt(dis) + 1;
            //System.out.println(points[i].toString());
        }
        p_x = points[0];
        p_y = points[1];
        this.dis = p_x.distance(p_y);
    }

    public void Input() {
        for (int i = 0; i < 3; i++) {
            Point p = new Point();
            System.out.println("Point " + i + ":");
            System.out.print("Coordinate X: ");
            p.x = scanner.nextInt();
            System.out.print("Coordinate Y: ");
            p.y = scanner.nextInt();
            ps.add(p);
        }
        points = ps.toArray(points);
    }

    public double ShortestPair(Point[] array, int start, int end) {
        int pa = 0, pb = 1;
        double min = array[pa].distanceSq(array[pb]);
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j <= end; j++) {
                double d = array[i].distanceSq(array[j]);
                if (d < min) {
                    min = d;
                    pa = i;
                    pb = j;
                }
            }
        }
        System.out.print("Shortest Distance: " + Math.sqrt(min) + " from (" + array[pa].x + "," + array[pa].y + ") to (" + array[pb].x + "," + array[pb].y + ")");
        return Math.sqrt(min);
    }
    Point p_x, p_y;
    double dis;

    public double ShortestPairFastReturnPoints(Point[] array, int start, int end) {
        if (end - start == 1) {
            double d = array[start].distance(array[end]);
            if (d < dis) {
                p_x = points[start];
                p_y = points[end];
                dis = p_x.distance(p_y);
            }
            return d;
        }
        if (end - start == 2) {
            int mid = start + (end - start) / 2;
            double dsm = array[start].distanceSq(array[mid]);
            double dme = array[mid].distanceSq(array[end]);
            double dse = array[start].distanceSq(array[end]);
            double d1 = dsm < dme ? dsm : dme;
//            double d = d1 < dse ? d1 : dse;
            double d;
            if (d1 < dse) {
                d = d1;
                if (d < dis) {
                    p_x = points[start];
                    p_y = points[end];
                    dis = p_x.distance(p_y);
                }
            } else {
                d = dse;
                if (d < dis) {
                    p_x = points[start];
                    p_y = points[end];
                    dis = p_x.distance(p_y);
                }
            }
//            double d = dsm < dme ? dsm : dme < dse ? dsm < dme ? dsm : dme : dse;
            return Math.sqrt(d);
        }

        int mid = start + (end - start) / 2;
        Point midPoint = array[mid];
        double dl = ShortestPairFast(array, start, mid);
        double dr = ShortestPairFast(array, mid, end);
        double d = dl <= dr ? dl : dr;

        ArrayList<Point> ArrayStrip = new ArrayList();
        for (int i = start; i <= end; i++) {
            if (Math.abs(array[i].x - midPoint.x) <= d) {
                ArrayStrip.add(array[i]);
            }
        }
        Point[] strip = new Point[ArrayStrip.size()];
        strip = ArrayStrip.toArray(strip);

        // Find the closest points in strip.
        // Return the minimum of d and closest
        // distance is strip[]
        double min = stripClosest(strip, strip.length, d);
        if (min < d) {
            if (min < dis) {
                p_x = points[start];
                p_y = points[end];
                dis = p_x.distance(p_y);
            }
            return min;

        } else {
            if (d < dis) {
                p_x = points[start];
                p_y = points[end];
                dis = p_x.distance(p_y);
            }
            return d;
        }
//        return min < d ? min : d;
    }

    public double ShortestPairFast(Point[] array, int start, int end) {
        if (end - start == 1) {
            return array[start].distance(array[end]);
        }
        if (end - start == 2) {
            int mid = start + (end - start) / 2;
            double dsm = array[start].distanceSq(array[mid]);
            double dme = array[mid].distanceSq(array[end]);
            double dse = array[start].distanceSq(array[end]);
            double d1 = dsm < dme ? dsm : dme;
            double d = d1 < dse ? d1 : dse;
//            double d = dsm < dme ? dsm : dme < dse ? dsm < dme ? dsm : dme : dse;
            return Math.sqrt(d);
        }

        int mid = start + (end - start) / 2;
        Point midPoint = array[mid];
        double dl = ShortestPairFast(array, start, mid);
        double dr = ShortestPairFast(array, mid, end);
        double d = dl <= dr ? dl : dr;

        ArrayList<Point> ArrayStrip = new ArrayList();
        for (int i = start; i <= end; i++) {
            if (Math.abs(array[i].x - midPoint.x) <= d) {
                ArrayStrip.add(array[i]);
            }
        }
        Point[] strip = new Point[ArrayStrip.size()];
        strip = ArrayStrip.toArray(strip);

        // Find the closest points in strip.
        // Return the minimum of d and closest
        // distance is strip[]
        double min = stripClosest(strip, strip.length, d);

        return min < d ? min : d;
    }

    public double stripClosest(Point[] strip, int size, double d) {
        double min = d; // Initialize the minimum distance as d
        sort.quickSortbyY(strip, 0, size - 1);
        // Pick all points one by one and try the next points till the difference
        // between y coordinates is smaller than d.
        // This is a proven fact that this loop runs at most 6 times
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if ((strip[j].y - strip[i].y) > d) {
                    break;
                }
                double a1 = strip[i].distance(strip[j]);
                if (a1 < min) {
                    p_x = strip[i];
                    p_y = strip[j];
                    min = a1;
                }
            }
        }
        return min;
    }

    public static void main(String[] args) throws IOException {
        KhoangCachNganNhat dnn = new KhoangCachNganNhat();
//        dnn.Random();
//        dnn.File();
//        dnn.Input();
//        for (int j = 0; j < 100; j++) {
        int n = 10000;
        dnn.RandomArray(n, n * 2);
        double start1 = System.nanoTime();
        sort.quickSortbyX(dnn.points, 0, dnn.points.length - 1);
        double d = dnn.ShortestPairFastReturnPoints(dnn.points, 0, dnn.points.length - 1);
        System.out.print("Shortest Distance: " + d + " from (" + dnn.p_x.x + "," + dnn.p_x.y + ") to (" + dnn.p_y.x + "," + dnn.p_y.y + ")");
        double end1 = System.nanoTime();
        double time1 = (end1 - start1) / 1000000000f;
        System.out.println(" in: " + time1);

        double start2 = System.nanoTime();
        double d1 = dnn.ShortestPair(dnn.points, 0, dnn.points.length - 1);
        double end2 = System.nanoTime();
        double time2 = (end2 - start2) / 1000000000f;
        System.out.println(" in " + time2);

        System.out.println(dnn.p_x.distance(dnn.p_y));

//        }
    }
}

class sort {

    public static void quickSortbyX(Point[] array, int start, int end) {
        if (start < end) {
            int index = partitionbyX(array, start, end);
            quickSortbyX(array, start, index - 1);
            quickSortbyX(array, index + 1, end);
        }
    }

    private static int partitionbyX(Point[] array, int start, int end) {
        int pivotIndex = start;
        int pivotValue = array[end].x;
        for (int i = start; i < end; i++) {
            if (array[i].x < pivotValue) {
                swap(array, i, pivotIndex);
                pivotIndex++;
            }
        }
        swap(array, pivotIndex, end);
        return pivotIndex;
    }

    public static void quickSortbyY(Point[] array, int start, int end) {
        if (start < end) {
            int index = partitionbyY(array, start, end);
            quickSortbyY(array, start, index - 1);
            quickSortbyY(array, index + 1, end);
        }
    }

    private static int partitionbyY(Point[] array, int start, int end) {
        int pivotIndex = start;
        int pivotValue = array[end].y;
        for (int i = start; i < end; i++) {
            if (array[i].y < pivotValue) {
                swap(array, i, pivotIndex);
                pivotIndex++;
            }
        }
        swap(array, pivotIndex, end);
        return pivotIndex;
    }

    private static void swap(Point[] array, int a, int b) {
        Point temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
