package com.tmn.voronoi;

public class Site {

    double x, y;
    int voronoiId;

    public Site(int voronoiId) {
        this.voronoiId = voronoiId;
    }

    public Site(double x, double y) {
        this.x = x;
        this.y = y;
        this.voronoiId = -1;
    }

}
