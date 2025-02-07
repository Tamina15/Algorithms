package com.tmn.flockingsimulation;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class Point extends Point2D.Double {

    public Point() {
        super();
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point(double x, double y) {
        super(x, y);
    }

    public static Point add(Point term1, Point term2) {
        return new Point(term1.x + term2.x, term1.y + term2.y);
    }

    public static Point add(Point term1, double term2) {
        return new Point(term1.x + term2, term1.y + term2);
    }

    public static Point sub(Point term1, Point term2) {
        return new Point(term1.x - term2.x, term1.y - term2.y);
    }

    public static Point sub(Point term1, double term2) {
        return new Point(term1.x - term2, term1.y - term2);
    }

    public static Point mul(Point term1, Point term2) {
        return new Point(term1.x * term2.x, term1.y * term2.y);
    }

    public static Point mul(Point term1, double term2) {
        return new Point(term1.x * term2, term1.y * term2);
    }

    public static Point div(Point term1, Point term2) {
        return new Point(term1.x / term2.x, term1.y / term2.y);
    }

    public static Point div(Point term1, double term2) {
        return new Point(term1.x / term2, term1.y / term2);
    }

    public Point add(Point addend) {
        this.x += addend.x;
        this.y += addend.y;
        return this;
    }

    public Point add(double addend) {
        this.x += addend;
        this.y += addend;
        return this;
    }

    public Point add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Point sub(Point subtrahend) {
        this.x -= subtrahend.x;
        this.y -= subtrahend.y;
        return this;
    }

    public Point sub(double subtrahend) {
        this.x -= subtrahend;
        this.y -= subtrahend;
        return this;
    }

    public Point sub(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Point mul(Point multiplicand) {
        this.x *= multiplicand.x;
        this.y *= multiplicand.y;
        return this;
    }

    public Point mul(double multiplicand) {
        this.x *= multiplicand;
        this.y *= multiplicand;
        return this;
    }

    public Point mul(double x, double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Point div(int divisor) {
        this.x /= divisor;
        this.y /= divisor;
        return this;
    }

    public void wrap(Rectangle bound) {
        wrap(bound.x, bound.y, bound.x + bound.width, bound.y + bound.height);
    }

    public void wrap(int minX, int minY, int maxX, int maxY) {
        if (x < minX) {
            x = maxX;
        } else if (x > maxX) {
            x = minX;
        }
        if (y < minY) {
            y = maxY;
        } else if (y > maxY) {
            y = minY;
        }
    }

    public void cap(int max) {
        x = Math.max(-max, Math.min(x, max));
        y = Math.max(-max, Math.min(y, max));
    }

    public void cap(double max) {
        x = Math.max(-max, Math.min(x, max));
        y = Math.max(-max, Math.min(y, max));
    }

    public void cap(int x, int y) {
        this.x = Math.max(-x, Math.min(this.x, x));
        this.y = Math.max(-y, Math.min(this.y, y));
    }

    public void cap(Point max) {
        x = Math.max(-max.x, Math.min(x, max.x));
        y = Math.max(-max.y, Math.min(y, max.y));
    }

}
