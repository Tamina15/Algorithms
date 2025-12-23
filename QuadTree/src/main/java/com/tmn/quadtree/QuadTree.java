/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.quadtree;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Tran Minh Nhat
 */
public class QuadTree {

    private final Rectangle.Double boundary;
    private final double midX, midY;
    private final double halfWidth, halfHeight;
    private final Point2D[] points;
    private int pointsIndex;
    private final int maxCapacity;
    private boolean divided;
    QuadTree nw;
    QuadTree ne;
    QuadTree se;
    QuadTree sw;

    public QuadTree(Rectangle.Double boundary, int maxCapacity) {
        this.boundary = boundary;
        midX = this.boundary.getCenterX();
        midY = this.boundary.getCenterY();
        halfWidth = this.boundary.width / 2;
        halfHeight = this.boundary.height / 2;
        this.maxCapacity = maxCapacity;
        pointsIndex = 0;
        points = new Point2D[maxCapacity];
        divided = false;
    }

    public boolean insert(Point2D point) {
        if (!boundary.contains(point)) {
            return false;
        }
        if (pointsIndex < maxCapacity) {
            points[pointsIndex] = point;
            pointsIndex++;
            return true;
        }
        if (!divided) {
            subDivide();
        }
        double x = point.getX();
        double y = point.getY();
        if (x > midX && y <= midY) {
            return ne.insert(point);
        }
        if (x >= midX && y > midY) {
            return se.insert(point);
        }
        if (x <= midX && y < midY) {
            return nw.insert(point);
        }
        if (x < midX && y >= midY) {
            return sw.insert(point);
        }
        return false;
    }

    private void subDivide() {
        divided = true;
        if (ne == null) {
            Rectangle.Double NEboundary = new Rectangle.Double(midX, boundary.y, halfWidth, halfHeight);
            ne = new QuadTree(NEboundary, maxCapacity);
        }
        if (nw == null) {
            Rectangle.Double NWboundary = new Rectangle.Double(boundary.x, boundary.y, halfWidth, halfHeight);
            nw = new QuadTree(NWboundary, maxCapacity);
        }
        if (se == null) {
            Rectangle.Double SEboundary = new Rectangle.Double(midX, midY, halfWidth, halfHeight);
            se = new QuadTree(SEboundary, maxCapacity);
        }
        if (sw == null) {
            Rectangle.Double SWboundary = new Rectangle.Double(boundary.x, midY, halfWidth, halfHeight);
            sw = new QuadTree(SWboundary, maxCapacity);
        }
    }

    private <T extends Point2D> Collection<T> query0(Rectangle.Double range, Collection<T> found) {
        if (!boundary.intersects(range)) {
            return found;
        }
        for (int i = 0; i < pointsIndex; i++) {
            Point2D mp = points[i];
            if (range.contains(mp)) {
                found.add((T) mp);
            }
        }
        if (divided) {
            ne.query0(range, found);
            nw.query0(range, found);
            se.query0(range, found);
            sw.query0(range, found);
        }

        return found;
    }

    public ArrayList<Point2D> query(Rectangle.Double range) {
        return query(range, null);
    }

    public <T extends Point2D> ArrayList<T> query(Rectangle.Double range, Collection<T> found) {
        if (found == null) {
            found = new ArrayList<>(4 * maxCapacity);
        }
        return (ArrayList<T>) query0(range, found);
    }

    private void draw0(Graphics2D g2d) {
        g2d.draw(boundary);
        Line2D line = new Line2D.Double();
        for (int i = 0; i < pointsIndex; i++) {
            Point2D p = points[i];
            line.setLine(p, p);
            g2d.draw(line);
            if (p instanceof MovingPoint mp) {
                g2d.draw(mp.collisionBox);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        draw0(g2d);
        if (divided) {
            ne.draw(g2d);
            nw.draw(g2d);
            se.draw(g2d);
            sw.draw(g2d);
        }
    }

    public void draw(Graphics2D g2d, Rectangle2D.Double viewPort) {
        if (!boundary.intersects(viewPort)) {
            return;
        }
        draw0(g2d);
        if (divided) {
            ne.draw(g2d, viewPort);
            nw.draw(g2d, viewPort);
            se.draw(g2d, viewPort);
            sw.draw(g2d, viewPort);
        }
    }

    public void drawBoundary(Graphics2D g2d, Rectangle2D.Double viewPort) {
        if (!boundary.intersects(viewPort)) {
            return;
        }
        g2d.draw(boundary);
        if (divided) {
            ne.drawBoundary(g2d, viewPort);
            nw.drawBoundary(g2d, viewPort);
            se.drawBoundary(g2d, viewPort);
            sw.drawBoundary(g2d, viewPort);
        }
    }

    public void clearTree() {
        if (divided) {
            ne.clearTree();
            nw.clearTree();
            se.clearTree();
            sw.clearTree();
        }
        pointsIndex = 0; // soft reset
        divided = false;
    }

    public Rectangle2D.Double getBoundary() {
        return boundary;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isDivided() {
        return divided;
    }

    public int getPointsIndex() {
        return pointsIndex;
    }

    public Point2D get(int index) {
        return points[index];
    }

}
