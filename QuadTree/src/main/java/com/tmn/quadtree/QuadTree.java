/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.quadtree;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Tran Minh Nhat
 */
public class QuadTree {

    Rectangle.Double boundary;
    double midX, midY;
    double halfWidth, halfHeight;
    MovingPoint[] points;
    int pointsIndex, maxCapacity;
    boolean isDivided;
    QuadTree nw;
    QuadTree ne;
    QuadTree se;
    QuadTree sw;

    public QuadTree(Rectangle.Double boundary, int maxCapacity) {
        this.boundary = boundary;
        midX = this.boundary.x + this.boundary.width / 2;
        midY = this.boundary.y + this.boundary.height / 2;
        halfWidth = this.boundary.width / 2;
        halfHeight = this.boundary.height / 2;
        this.maxCapacity = maxCapacity;
        pointsIndex = 0;
        points = new MovingPoint[maxCapacity];
        isDivided = false;
    }

    public boolean insert(MovingPoint movingPoint) {
        if (!boundary.contains(movingPoint.position)) {
            return false;
        }
        if (pointsIndex < maxCapacity) {
            points[pointsIndex] = movingPoint;
            pointsIndex++;
            return true;
        }
        if (!isDivided) {
            subDivide();
        }
        Point.Double p = movingPoint.position;
        if (p.x > midX && p.y <= midY) {
            return ne.insert(movingPoint);
        }
        if (p.x >= midX && p.y > midY) {
            return se.insert(movingPoint);
        }
        if (p.x <= midX && p.y < midY) {
            return nw.insert(movingPoint);
        }
        if (p.x < midX && p.y >= midY) {
            return sw.insert(movingPoint);
        }
        return false;
    }

    private void subDivide() {
        isDivided = true;
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

    public ArrayList<MovingPoint> query(Rectangle.Double range) {
        return (ArrayList<MovingPoint>) query(range, new ArrayList<>());
    }

    public Collection<MovingPoint> query(Rectangle.Double range, Collection<MovingPoint> found) {
        if (!boundary.intersects(range)) {
            return found;
        }
        for (int i = 0; i < pointsIndex; i++) {
            MovingPoint mp = points[i];
            Point.Double p = mp.position;
            if (range.contains(p)) {
                found.add(mp);
            }
        }
        if (isDivided) {
            ne.query(range, found);
            nw.query(range, found);
            se.query(range, found);
            sw.query(range, found);
        }

        return found;
    }

    private void draw0(Graphics2D g2d) {
//        g2d.draw(boundary);
        for (int i = 0; i < pointsIndex; i++) {
            MovingPoint mp = points[i];
            Point.Double p = mp.position;
            g2d.drawLine((int) p.x, (int) p.y, (int) p.x, (int) p.y);
            g2d.draw(mp.collisionBox);
        }
    }

    public void draw(Graphics2D g2d) {
        draw0(g2d);
        if (isDivided) {
            ne.draw(g2d);
            nw.draw(g2d);
            se.draw(g2d);
            sw.draw(g2d);
        }
    }

    public void draw(Graphics2D g2d, Rectangle2D.Double viewPort) {
        if (boundary.intersects(viewPort)) {
            draw0(g2d);
        }
        if (isDivided) {
            ne.draw(g2d, viewPort);
            nw.draw(g2d, viewPort);
            se.draw(g2d, viewPort);
            sw.draw(g2d, viewPort);
        }
    }

    public void clearTree() {
        if (isDivided) {
            ne.clearTree();
            nw.clearTree();
            se.clearTree();
            sw.clearTree();
        }
        pointsIndex = 0; // soft reset
        isDivided = false;
    }

}
