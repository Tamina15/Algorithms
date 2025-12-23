package com.tmn.polygontriangulation;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PolygonUtils {

    public static boolean doTriangulation = true;

    public static List<Point2D> triangulate(Point2D[] points) {
        int length = points.length;
        if (length < 3) {
            throw new IllegalArgumentException("Coordinates length must larger or equal to 3");
        }
        if (length == 3) {
            return List.of(points);
        }
        int clockwiseness = clockwiseness(points);
        if (clockwiseness == 0) {
            throw new IllegalArgumentException("Polygon area is zero");
        }
        List<Integer> indexes = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            indexes.add(i);
        }
        if (clockwiseness == -1) {
            indexes = indexes.reversed();
        }
        ArrayList<Point2D> vertices = new ArrayList(length * 3);
        int cl = indexes.size();
        outer:
        while (indexes.size() > 3) {
            int l = indexes.size();
            for (int i = 0; i < l; i++) {
                if (!doTriangulation) {
                    break outer;
                }
                Point2D a = points[indexes.get(i)];
                Point2D b = points[indexes.get((i - 1 + l) % l)];
                Point2D c = points[indexes.get((i + 1) % l)];

                Point2D ab = new Point2D.Double(b.getX() - a.getX(), b.getY() - a.getY());
                Point2D ac = new Point2D.Double(c.getX() - a.getX(), c.getY() - a.getY());

                double cross = crossProduct(ab, ac);
                if (cross < 0) {
                    continue;
                }

                boolean inside = false;
                for (int j = 0; j < l; j++) {
                    Point2D d = points[indexes.get(j)];
                    if (d == a || d == b || d == c) {
                        continue;
                    }
                    if (isPointInTriangle(d, a, b, c)) {
                        inside = true;
                        break;
                    }
                }
                if (!inside) {
                    vertices.add(a);
                    vertices.add(b);
                    vertices.add(c);
                    indexes.remove(i);
                    break;
                }
            }
            // force remove
            if (cl == indexes.size()) {
                indexes.remove(0);
            } else {
                cl = indexes.size();
            }
        }

        // Add last triangle
        vertices.add(points[indexes.get(0)]);
        vertices.add(points[indexes.get(1)]);
        vertices.add(points[indexes.get(2)]);

        return vertices;
    }

    /**
     * Calculate the cross product of vector {@code a} and {@code b}.
     * <p/>
     * The result is comforted to normal Oxy coordinate,
     * as oppose to Java graphics coordinate, where y-axis is downward.
     * <p/>
     * The cross product of vector a and b is negative
     * when vector {@code a} is to the left of the vector {@code b},
     * and it’s greater than 0 if it’s to the right.
     *
     * @param a The 2D point (xA, yA) represents a 2D vector.
     * @param b The 2D point (xB, yB) represents a 2D vector.
     * @return The cross product of a and b (a × b).
     */
    public static double crossProduct(Point2D a, Point2D b) {
        return crossProduct(a.getX(), a.getY(), b.getX(), b.getY());
    }

    /**
     * Calculate the cross product of vector {@code a} and {@code b}.
     * <p/>
     * The result is comforted to normal Oxy coordinate,
     * as oppose to Java graphics coordinate, where y-axis is downward.
     * <p/>
     * The cross product of vector a and b is negative
     * when vector {@code a} is to the left of the vector {@code b},
     * and it’s greater than 0 if it’s to the right.
     *
     * @param ax x-coordinate of vector a.
     * @param ay y-coordinate of vector a.
     * @param bx x-coordinate of vector b.
     * @param by y-coordinate of vector b.
     * @return The cross product of a and b (a × b).
     */
    public static double crossProduct(double ax, double ay, double bx, double by) {
        double product = ax * by - ay * bx;
        return -product;
    }

    /**
     * Test if a point {@code P} lies inside the triangle {@code ABC}.
     * <pre>
     * Clockwise ↻ or Counterclockwise ↺
     *  A                  A_____C
     *  |\                 |    /
     *  | \                | P /
     *  |  \               |  /
     *  | P \              | /
     *  |    \             |/
     *  C¯¯¯¯¯B            B
     * </pre>
     *
     * @param p The point to be tested.
     * @param a The triangle vertices.
     * @param b The triangle vertices.
     * @param c The triangle vertices.
     * @return {@code true} if point lies inside triangle.
     *         {@code false} if point lies outside triangle.
     */
    public static boolean isPointInTriangle(Point2D p, Point2D a, Point2D b, Point2D c) {
        return isPointInTriangle(p.getX(), p.getY(), a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());
    }

    /**
     * Test if a point {@code P} lies inside the triangle {@code ABC}.
     * <pre>
     * Clockwise ↻ or Counterclockwise ↺
     *  A                  A_____C
     *  |\                 |    /
     *  | \                | P /
     *  |  \               |  /
     *  | P \              | /
     *  |    \             |/
     *  C¯¯¯¯¯B            B
     * </pre>
     *
     * @param px x-coordinate of point p.
     * @param py y-coordinate of point p.
     * @param ax x-coordinate of point a.
     * @param ay y-coordinate of point a.
     * @param bx x-coordinate of point b.
     * @param by y-coordinate of point b.
     * @param cx x-coordinate of point c.
     * @param cy y-coordinate of point c.
     * @return {@code true} if point lies inside triangle.
     *         {@code false} if point lies outside triangle.
     */
    public static boolean isPointInTriangle(double px, double py, double ax, double ay, double bx, double by, double cx, double cy) {
        double xAB = bx - ax, yAB = by - ay;
        double xBC = cx - bx, yBC = cy - by;
        double xCA = ax - cx, yCA = ay - cy;

        double xAP = px - ax, yAP = py - ay;
        double xBP = px - bx, yBP = py - by;
        double xCP = px - cx, yCP = py - cy;

        double crossA = crossProduct(xAB, yAB, xAP, yAP);
        double crossB = crossProduct(xBC, yBC, xBP, yBP);
        double crossC = crossProduct(xCA, yCA, xCP, yCP);

        int sig = (int) (Math.signum(crossA) + Math.signum(crossB) + Math.signum(crossC));
        return sig == 3 || sig == -3;
    }

    /**
     * Calculate the area of a polygon.
     *
     * @param points the points define the polygon
     * @return the area of the polygon, always positive.
     */
    public static double area(Point2D[] points) {
        double result = areaSigned(points);
        return Math.abs(result);
    }

    /**
     * Calculate the area of a polygon, can be negative.
     * <p/>
     * Formula:
     * <br>
     * CrossSum(P) = P[i] × P[i+1] from i=0 to n-1 with i {@code mod} n. /
     * Area(P) = 1/2 * CrossSum(P)
     *
     * @param points the points define the polygon.
     * @return the area of the polygon. Can be negative.
     */
    public static double areaSigned(Point2D[] points) {
        double crossSum = 0;
        int l = points.length - 1;
        for (int i = 0; i < l; i++) {
            crossSum += crossProduct(points[i], points[i + 1]);
        }
        crossSum += crossProduct(points[l], points[0]);
        return crossSum * 1 / 2;
    }

    /**
     * Check the clockwise-ness of a polygon
     *
     * @param points the points define the polygon.
     * @return 0 if the polygon's area is zero,
     *         -1 if the points are oriented counterclockwise,
     *         1 if the points are oriented clockwise
     */
    public static int clockwiseness(Point2D[] points) {
        double area = areaSigned(points);
        if (area == 0) {
            return 0;
        } else {
            if (area > 0) {
                return -1; // counter clockwise
            } else {
                return 1; // clockwise
            }
        }
    }

    /**
     * Check the clockwise-ness of a triangle.
     *
     * @param a The triangle vertices.
     * @param b The triangle vertices.
     * @param c The triangle vertices.
     * @return true if the triangle is counterclockwise.
     *         false if the triangle is clockwise.
     */
    public static boolean ccw(Point2D a, Point2D b, Point2D c) {
        return ccw(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());
    }

    /**
     * Check the clockwise-ness of a triangle.
     *
     * @param ax x-coordinate of point a.
     * @param ay y-coordinate of point a.
     * @param bx x-coordinate of point b.
     * @param by y-coordinate of point b.
     * @param cx x-coordinate of point c.
     * @param cy y-coordinate of point c.
     * @return true if the triangle is counterclockwise.
     *         false if the triangle is clockwise.
     */
    public static boolean ccw(double ax, double ay, double bx, double by, double cx, double cy) {
        double ccw = (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);
        return ccw < 0;
    }

    public static boolean isPointInCircumcircle(Point2D P, Point2D A, Point2D B, Point2D C) {
        return isPointInCircumcircle(P.getX(), P.getY(), A.getX(), A.getY(), B.getX(), B.getY(), C.getX(), C.getY());
    }

    /**
     * https://stackoverflow.com/questions/39984709/how-can-i-check-wether-a-point-is-inside-the-circumcircle-of-3-points
     *
     * @param px
     * @param py
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @param cx
     * @param cy
     * @return
     */
    public static boolean isPointInCircumcircle(double px, double py, double ax, double ay, double bx, double by, double cx, double cy) {
        double ax_ = ax - px;
        double ay_ = ay - py;
        double bx_ = bx - px;
        double by_ = by - py;
        double cx_ = cx - px;
        double cy_ = cy - py;
        double r = ((ax_ * ax_ + ay_ * ay_) * (bx_ * cy_ - cx_ * by_)
                - (bx_ * bx_ + by_ * by_) * (ax_ * cy_ - cx_ * ay_)
                + (cx_ * cx_ + cy_ * cy_) * (ax_ * by_ - bx_ * ay_));
        boolean ccw = ccw(ax, ay, bx, by, cx, cy);
        if (ccw) {
            return (int) Math.signum(r) == -1;
        } else {
            return (int) Math.signum(r) == 1;
        }
    }

    public static double[] circumcircle(Point2D A, Point2D B, Point2D C) {
        return circumcircle(A.getX(), A.getY(), B.getX(), B.getY(), C.getX(), C.getY(), null);
    }

    public static double[] circumcircle(Point2D A, Point2D B, Point2D C, double[] out) {
        return circumcircle(A.getX(), A.getY(), B.getX(), B.getY(), C.getX(), C.getY(), out);
    }

    /**
     * Calculate the circumcenter and circumradius of triangle ABC.
     *
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @param cx
     * @param cy
     * @param out
     * @return length-3 {@code double} array contains [center x-coordinate, center y-coordinate, radius]
     */
    public static double[] circumcircle(double ax, double ay, double bx, double by, double cx, double cy, double[] out) {
        double bcy = by - cy;
        double cay = cy - ay;
        double aby = ay - by;

        double d = 2 * (ax * bcy + bx * cay + cx * aby);
        double oneOverD = 1 / d;

        double a2 = ax * ax + ay * ay;
        double b2 = bx * bx + by * by;
        double c2 = cx * cx + cy * cy;

        double cbx = cx - bx;
        double acx = ax - cx;
        double bax = bx - ax;

        double ux = oneOverD * (a2 * bcy + b2 * cay + c2 * aby);
        double uy = oneOverD * (a2 * cbx + b2 * acx + c2 * bax);

        double ur = Point2D.distance(ux, uy, ax, ay);
        if (out == null || out.length < 3) {
            out = new double[]{ux, uy, ur};
        } else {
            out[0] = ux;
            out[1] = uy;
            out[2] = ur;
        }
        return out;
    }

    public static boolean isPointInCircumcircle2(double px, double py, double ax, double ay, double bx, double by, double cx, double cy) {
        double[] d = new double[3];
        circumcircle(ax, ay, bx, by, cx, cy, d);
        return Point2D.distance(px, py, d[0], d[1]) < d[2];
    }

    public static boolean isPointInCircumcircle2(Point2D P, Point2D A, Point2D B, Point2D C) {
        return isPointInCircumcircle2(P.getX(), P.getY(), A.getX(), A.getY(), B.getX(), B.getY(), C.getX(), C.getY());
    }

    public static Rectangle2D getMinRect(Point2D[] points) {
        double x = points[0].getX();
        double y = points[0].getY();
        double leftX = x, rightX = x, topY = y, bottomY = y;
        for (int i = 1; i < points.length; i++) {
            x = points[i].getX();
            y = points[i].getY();
            if (x < leftX) {
                leftX = x;
            }
            if (x > rightX) {
                rightX = x;
            }
            if (y < topY) {
                topY = y;
            }
            if (y > bottomY) {
                bottomY = y;
            }
        }
        return new Rectangle2D.Double(leftX, topY, rightX - leftX, bottomY - topY);
    }

    public static DelaunayTriangulation.Triangle getCoveredTriangle(Point2D[] points) {
        return getCoveredTriangle(getMinRect(points));
    }

    public static DelaunayTriangulation.Triangle getCoveredTriangle(Rectangle2D rect) {
        Point2D A = new Point2D.Double(rect.getX() - 10, rect.getY() - 10);
        double length = rect.getWidth() > rect.getHeight() ? 2 * rect.getWidth() : 2 * rect.getHeight();
        Point2D B = new Point2D.Double(A.getX() + 30 + length, A.getY());
        Point2D C = new Point2D.Double(A.getX(), A.getY() + 30 + length);
        return new DelaunayTriangulation.Triangle(A, B, C);
    }

    public static Path2D getCoveredTrianglePath(Rectangle2D rect) {
        Point2D A = new Point2D.Double(rect.getX() - 10, rect.getY() - 10);
        double length = rect.getWidth() > rect.getHeight() ? 2 * rect.getWidth() : 2 * rect.getHeight();
        Point2D B = new Point2D.Double(A.getX() + 30 + length, A.getY());
        Point2D C = new Point2D.Double(A.getX(), A.getY() + 30 + length);
        Path2D path = new Path2D.Double();
        path.moveTo(A.getX(), A.getY());
        path.lineTo(B.getX(), B.getY());
        path.lineTo(C.getX(), C.getY());
        path.closePath();
        return path;
    }

    /**
     * https://stackoverflow.com/questions/8997099/algorithm-to-generate-random-2d-polygon
     *
     * @author NhatTranMinh
     */
    static public class SimplePolygon {

        static Random r = new Random();

        public static Point2D[] generate(int numVetices) {
            return generate(numVetices, 0, 0, 100, 0, 0);
        }

        public static Point2D[] generate(int numVetices, double centerX, double centerY, double avgRadius) {
            return generate(numVetices, centerX, centerY, avgRadius, 0, 0);
        }

        public static Point2D[] generateRandom(int numVetices, double centerX, double centerY, double avgRadius) {
            return generate(numVetices, centerX, centerY, avgRadius, r.nextDouble(Math.nextUp(0), Math.nextDown(1)), r.nextDouble(Math.nextUp(0), Math.nextDown(1)));
        }

        public static Point2D[] generateRandom(int numVetices, int min, int max) {
            Point2D[] points = new Point2D[numVetices];
            for (int i = 0; i < numVetices; i++) {
                points[i] = new Point2D.Double(r.nextInt(min, max + 1), r.nextInt(min, max + 1));
            }
            return points;
        }

        public static Point2D[] generate(int numVetices, double centerX, double centerY, double avgRadius, double irregularity, double spikiness) {
            if (numVetices <= 0) {
                throw new IllegalArgumentException("Number of vertices must be positive");
            }
            if (irregularity < 0 || irregularity > 1) {
                throw new IllegalArgumentException("Irregularity must be between 0 and 1.");
            }
            if (spikiness < 0 || spikiness > 1) {
                throw new IllegalArgumentException("Spikiness must be between 0 and 1.");
            }
            irregularity *= 2 * Math.PI / numVetices;
            spikiness *= avgRadius;
            double[] angle_steps = randomAngleStep(numVetices, irregularity);
            Point2D[] points = new Point2D[numVetices];
            double angle = r.nextDouble(2 * Math.PI);
            double maxRadius = 2 * avgRadius;
            for (int i = 0; i < numVetices; i++) {
                double g = r.nextGaussian(avgRadius, spikiness);
                double radius = Math.clamp(g, 0, maxRadius);
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);
                Point2D point = new Point2D.Double(x, y);
                points[i] = point;
                angle += angle_steps[i];
            }
            return points;
        }

        private static double[] randomAngleStep(int steps, double irregularity) {
            double[] angles = new double[steps];
            double lower = (2 * Math.PI / steps) - irregularity;
            double upper = (2 * Math.PI / steps) + irregularity;
            double nextUpper = Math.nextUp(upper);
            double cumsum = 0;
            for (int i = 0; i < steps; i++) {
                double angle = r.nextDouble(lower, nextUpper);
                angles[i] = angle;
                cumsum += angle;
            }
            cumsum /= (2 * Math.PI);
            for (int i = 0; i < steps; i++) {
                angles[i] = angles[i] / cumsum;
            }
            return angles;
        }

    }

}
