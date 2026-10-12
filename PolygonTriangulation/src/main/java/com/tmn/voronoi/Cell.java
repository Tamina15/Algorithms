package com.tmn.voronoi;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Cell {

    Site site;
    List<Halfedge> halfedges;
    boolean closeMe;
    List<Cell> cellJunkyard = new ArrayList<>();

    public Cell(Site site) {
        this.site = site;
        this.halfedges = new ArrayList<>();
        this.closeMe = false;
    }

    public Cell init(Site site) {
        this.site = site;
        this.halfedges = new ArrayList<>();
        this.closeMe = false;
        return this;
    }

    public int prepareHalfedges() {
        List<Halfedge> halfedges = this.halfedges;
        int iHalfedge = halfedges.size();
        Edge edge;
        while (iHalfedge-- > 0) {
            edge = halfedges.get(iHalfedge).edge;
            if (edge.vB == null || edge.vA == null) {
                halfedges.remove(iHalfedge); // check this method
            }
        }
        halfedges.sort((a, b) -> (int) Math.signum(b.angle - a.angle));
        return halfedges.size();
    }

    public List<Integer> getNeighborIds() {
        int iHalfedge = halfedges.size();
        List<Integer> neighbors = new ArrayList<>(iHalfedge);
        Edge edge;
        while (iHalfedge-- > 0) {
            edge = this.halfedges.get(iHalfedge).edge;
            if (edge.lSite != null && edge.lSite.voronoiId != this.site.voronoiId) {
                neighbors.add(edge.lSite.voronoiId);
            } else if (edge.rSite != null && edge.rSite.voronoiId != this.site.voronoiId) {
                neighbors.add(edge.rSite.voronoiId);

            }
        }
        return neighbors;
    }

    public Rectangle2D getBbox() {
        List<Halfedge> halfedges = this.halfedges;
        int iHalfedge = halfedges.size();
        double xmin = Double.POSITIVE_INFINITY;
        double ymin = Double.POSITIVE_INFINITY;
        double xmax = Double.NEGATIVE_INFINITY;
        double ymax = Double.NEGATIVE_INFINITY;
        Point2D v;
        double vx, vy;
        while (iHalfedge-- > 0) {
            v = halfedges.get(iHalfedge).getStartpoint();
            vx = v.getX();
            vy = v.getY();
            if (vx < xmin) {
                xmin = vx;
            }
            if (vy < ymin) {
                ymin = vy;
            }
            if (vx > xmax) {
                xmax = vx;
            }
            if (vy > ymax) {
                ymax = vy;
            }
        }
        return new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
    }

    public int pointIntersection(double x, double y) {
        List<Halfedge> halfedges = this.halfedges;
        int iHalfedges = halfedges.size();
        Halfedge halfedge;
        Point2D p0, p1;
        double r;
        while (iHalfedges-- > 0) {
            halfedge = halfedges.get(iHalfedges);
            p0 = halfedge.getStartpoint();
            p1 = halfedge.getEndpoint();
            r = (y - p0.getY()) * (p1.getX() - p0.getX()) - (x - p0.getX()) * (p1.getY() - p0.getY());
            if (r == 0) {
                return 0;
            }
            if (r > 0) {
                return -1;
            }
        }
        return 1;
    }
}
