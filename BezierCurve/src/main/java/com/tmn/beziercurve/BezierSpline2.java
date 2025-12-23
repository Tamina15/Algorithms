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

public class BezierSpline2 implements Shape {

    private Path2D path = new Path2D.Double();
    private Point2D[] points;
    private static final int CLOSED_LOOP = 1, OPEN_LOOP = 2; // Do not change this, it's used in calculation
    private int looped = OPEN_LOOP;

    public BezierSpline2(Point2D[] points) {
        this.points = points;
        initPath();
    }

    private void initPath() {
        Shape s;
        switch (points.length) {
            case 0 -> {
                return;
            }
            case 1 -> {
                s = new Line2D.Double(points[0], points[0]);
                break;
            }
            case 2 -> {
                s = new Line2D.Double(points[0], points[1]);
                break;
            }
            case 3 -> {
                QuadCurve2D curve = new QuadCurve2D.Double();
                curve.setCurve(points, 0);
                s = curve;
                break;
            }
            case 4 -> {
                CubicCurve2D curve = new CubicCurve2D.Double();
                curve.setCurve(points, 0);
                s = curve;
                break;
            }
            default -> {
                s = PiecewiseQuadraticBezierCurves();
                break;
            }
        }
        int windingRule = path.getWindingRule();
        path = new Path2D.Double(s);
        path.setWindingRule(windingRule);
    }

    private Path2D PiecewiseQuadraticBezierCurves() {
        Path2D path = new Path2D.Double();
        Point2D p0 = points[0];
        Point2D p1 = points[looped % 2];
        double xA = p0.getX() + (p1.getX() - p0.getX()) / 2;
        double yA = p0.getY() + (p1.getY() - p0.getY()) / 2;
        double xB, yB;
        path.moveTo(xA, yA);
        int l = points.length - looped;
        int i = 1;
        for (; i < l; i++) {
            Point2D pF = points[i];
            Point2D pS = points[i + 1];
            xB = pF.getX() + (pS.getX() - pF.getX()) / 2;
            yB = pF.getY() + (pS.getY() - pF.getY()) / 2;
            path.quadTo(pF.getX(), pF.getY(), xB, yB);
        }
        if (isOpenLoop()) {
            Point2D secondLast = points[i++];
            Point2D last = points[i];
            path.quadTo(secondLast.getX(), secondLast.getY(), last.getX(), last.getY());
        } else {
            Point2D pF = points[i];
            Point2D pS = points[0];
            xB = pF.getX() + (pS.getX() - pF.getX()) / 2;
            yB = pF.getY() + (pS.getY() - pF.getY()) / 2;
            path.quadTo(pF.getX(), pF.getY(), xB, yB);

            path.quadTo(p0.getX(), p0.getY(), xA, yA);
        }
        return path;
    }

    public void newPath() {
        initPath();
    }

    public Point2D[] getPoints() {
        return points;
    }

    public void setPoints(Point2D[] points) {
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
