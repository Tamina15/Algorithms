package com.tmn.beziercurve;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class BezierSpline implements Shape {

    private static final int CLOSED_LOOP = 1, OPEN_LOOP = 2; // Do not change this, it's used in calculation
    private Path2D path;
    private Point2D[] points;
    private int looped;

    public BezierSpline(Point2D[] points) {
        this(points, CLOSED_LOOP);
    }

    public BezierSpline(Point2D[] points, int looped) {
        check(points);
        this.points = points;
        this.looped = looped;
        path = new Path2D.Double();
        initPath();
    }

    private void check(Point2D[] points) {
        Objects.requireNonNull(points, "Points can not be null");

        if (points.length == 0) {
            throw new IllegalArgumentException("Points length can not be 0");
        }

        for (int i = 0; i < points.length; i++) {
            Objects.requireNonNull(points[i], "Point at index " + i + " is null");
        }
    }

    private void initPath() {
        Shape s;
        switch (points.length) {
            case 0 -> {
                throw new IllegalArgumentException("Points length can not be 0");
            }
            case 1 -> {
                s = new Line2D.Double(points[0], points[0]);
            }
            case 2 -> {
                s = new Line2D.Double(points[0], points[1]);
            }
            case 3 -> {
                QuadCurve2D curve = new QuadCurve2D.Double();
                curve.setCurve(points, 0);
                s = curve;
            }
            case 4 -> {
                CubicCurve2D curve = new CubicCurve2D.Double();
                curve.setCurve(points, 0);
                s = curve;
            }
            default -> {
                s = PiecewiseQuadraticBezierCurves();
            }
        }
        path.reset();
        path.append(s, true);
    }

    private Path2D PiecewiseQuadraticBezierCurves() {
        Path2D curve = new Path2D.Double();
        Point2D p0 = points[0];
        Point2D p1 = points[looped % 2];
        double xA = p0.getX() + (p1.getX() - p0.getX()) / 2;
        double yA = p0.getY() + (p1.getY() - p0.getY()) / 2;
        double xB, yB;
        curve.moveTo(xA, yA);
        int l = points.length - looped;
        int i = 1;
        for (; i < l; i++) {
            Point2D pF = points[i];
            Point2D pS = points[i + 1];
            xB = pF.getX() + (pS.getX() - pF.getX()) / 2;
            yB = pF.getY() + (pS.getY() - pF.getY()) / 2;
            curve.quadTo(pF.getX(), pF.getY(), xB, yB);
        }
        if (isOpenLoop()) {
            Point2D secondLast = points[i++];
            Point2D last = points[i];
            curve.quadTo(secondLast.getX(), secondLast.getY(), last.getX(), last.getY());
        } else {
            Point2D pF = points[i];
            Point2D pS = points[0];
            xB = pF.getX() + (pS.getX() - pF.getX()) / 2;
            yB = pF.getY() + (pS.getY() - pF.getY()) / 2;
            curve.quadTo(pF.getX(), pF.getY(), xB, yB);

            curve.quadTo(p0.getX(), p0.getY(), xA, yA);
        }
        return curve;
    }

    public void newPath() {
        initPath();
    }

    public Point2D[] getPoints() {
        return points;
    }

    public void setPoints(Point2D[] points) {
        check(points);
        this.points = points;
        initPath();
    }

    public void setWindingRule(int rule) {
        path.setWindingRule(rule);
    }

    public void toggleWindingRule() {
        if (path.getWindingRule() == PathIterator.WIND_EVEN_ODD) {
            path.setWindingRule(PathIterator.WIND_NON_ZERO);
        } else {
            path.setWindingRule(PathIterator.WIND_EVEN_ODD);
        }
    }

    public boolean isOpenLoop() {
        return looped == OPEN_LOOP;
    }

    public void openLoop() {
        looped = OPEN_LOOP;
        initPath();
    }

    public void closeLoop() {
        looped = CLOSED_LOOP;
        initPath();
    }

    public void toggleLoop() {
        looped = looped % 2 + 1;
        initPath();
    }

    @Override
    public Rectangle getBounds() {
        return path.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return path.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return path.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return path.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return path.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return path.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return path.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return path.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return path.getPathIterator(at, flatness);
    }

}
