package com.tmn.beziercurve;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.NoSuchElementException;

public class MyPoint extends Point2D.Double implements Shape {

    Line2D line;

    public MyPoint() {
        super();
        line = new Line2D.Double();
    }

    public MyPoint(double x, double y) {
        super(x, y);
        line = new Line2D.Double(x, y, x, y);
    }

    public MyPoint(Point2D p) {
        super(p.getX(), p.getY());
        line = new Line2D.Double(x, y, x, y);
    }

    /**
     * {@inheritDoc}
     *
     * @param p
     */
    @Override
    public void setLocation(Point2D p) {
        super.setLocation(p);
        line.setLine(x, y, x, y);
    }

    @Override
    public void setLocation(double x, double y) {
        super.setLocation(x, y);
        line.setLine(x, y, x, y);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(((int) x) - 1, ((int) y) - 1, 3, 3);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Double(x - 1, y - 1, 2, 2);
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public boolean contains(Point2D p) {
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return line.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return line.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return line.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return line.getPathIterator(at, flatness);
    }

    @Override
    public String toString() {
        return "MyPoint[" + x + ", " + y + "]";
    }

    private class MyPointPathIterator implements PathIterator {

        MyPoint point;
        AffineTransform at;
        int index;

        public MyPointPathIterator(MyPoint point, AffineTransform at) {
            this.point = point;
            this.at = at;
            index = 0;
        }

        public MyPointPathIterator(MyPoint point) {
            this(point, null);
        }

        @Override
        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        @Override
        public boolean isDone() {
            return index > 1;
        }

        @Override
        public void next() {
            index++;
        }

        @Override
        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException("point iterator out of bounds");
            }
            coords[0] = (float) point.x;
            coords[1] = (float) point.y;
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            if (index == 0) {
                return SEG_MOVETO;
            }
            return SEG_LINETO;
        }

        @Override
        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException("point iterator out of bounds");
            }
            coords[0] = point.x;
            coords[1] = point.y;
            if (at != null) {
                at.transform(coords, 0, coords, 0, 1);
            }
            if (index == 0) {
                return SEG_MOVETO;
            }
            return SEG_LINETO;
        }

    }
}
