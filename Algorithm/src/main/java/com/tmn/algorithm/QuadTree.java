/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Tran Minh Nhat
 */
public class QuadTree {

    ArrayList<Point.Double> points;
    Rectangle.Double boundary;
    double midX, midY;
    double halfWidth, halfHeight;
    int maxCapacity;
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
        points = new ArrayList();
        isDivided = false;
    }

    public boolean insert(Point.Double p) {
        if (!boundary.contains(p)) {
            return false;
        }
        if (points.size() < maxCapacity) {
            points.add(p);
            return true;
        }
        if (!isDivided) {
            subdivide();
            isDivided = true;
        }
        if (p.x > midX && p.y <= midY) {
            return ne.insert(p);
        }
        if (p.x >= midX && p.y > midY) {
            return se.insert(p);
        }
        if (p.x <= midX && p.y < midY) {
            return nw.insert(p);
        }
        if (p.x < midX && p.y >= midY) {
            return sw.insert(p);
        }
        return false;
    }

    public void subdivide() {
        Rectangle.Double NEboundary = new Rectangle.Double(midX, boundary.y, halfWidth, halfHeight);
        Rectangle.Double NWboundary = new Rectangle.Double(boundary.x, boundary.y, halfWidth, halfHeight);
        Rectangle.Double SEboundary = new Rectangle.Double(midX, midY, halfWidth, halfHeight);
        Rectangle.Double SWboundary = new Rectangle.Double(boundary.x, midY, halfWidth, halfHeight);
        ne = new QuadTree(NEboundary, maxCapacity);
        nw = new QuadTree(NWboundary, maxCapacity);
        se = new QuadTree(SEboundary, maxCapacity);
        sw = new QuadTree(SWboundary, maxCapacity);
    }

    public ArrayList<Point.Double> query(Rectangle.Double range, ArrayList<Point.Double> found) {
        if (!boundary.intersects(range)) {
            return found;
        }
        for (Point.Double p : points) {
            if (range.contains(p)) {
                found.add(p);
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

    public void draw(Graphics2D g2d) {
        g2d.draw(boundary);
        for (Point.Double p : points) {
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine((int) p.x, (int) p.y, (int) p.x, (int) p.y);
        }
        g2d.setStroke(new BasicStroke(1));
        if (this.isDivided) {
            ne.draw(g2d);
            nw.draw(g2d);
            se.draw(g2d);
            sw.draw(g2d);
        }
    }
}
