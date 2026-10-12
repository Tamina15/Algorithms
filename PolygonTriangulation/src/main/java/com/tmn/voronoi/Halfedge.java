package com.tmn.voronoi;

import java.awt.geom.Point2D;

public class Halfedge {

    Site site;
    Edge edge;
    double angle;

    public Halfedge(Edge edge, Site lSite, Site rSite) {
        this.site = lSite;
        this.edge = edge;
        if (rSite != null) {
            this.angle = Math.atan2(rSite.y - lSite.y, rSite.x - lSite.x);
        } else {
            Point2D vA = edge.vA;
            Point2D vB = edge.vB;
            this.angle = edge.lSite == lSite
                    ? Math.atan2(vB.getX() - vA.getX(), vA.getY() - vB.getY())
                    : Math.atan2(vA.getX() - vB.getX(), vB.getY() - vA.getY());
        }
    }

    public Point2D getStartpoint() {
        return this.edge.lSite == this.site ? this.edge.vA : this.edge.vB;
    }

    public Point2D getEndpoint() {
        return this.edge.lSite == this.site ? this.edge.vB : this.edge.vA;
    }
}
