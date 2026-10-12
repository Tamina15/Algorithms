package com.tmn.voronoi;

import java.awt.geom.Point2D;

public class Edge {

    Site lSite, rSite;
    Point2D vB, vA;

    public Edge(Site lSite, Site rSite) {
        this.lSite = lSite;
        this.rSite = rSite;
        vA = null;
        vB = null;
    }

}
