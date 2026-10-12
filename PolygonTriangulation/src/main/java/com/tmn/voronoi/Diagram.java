package com.tmn.voronoi;

import java.awt.geom.Point2D;
import java.util.List;

public class Diagram {

    Object site;
    List<Cell> cells;
    List<Edge> edges;
    List<Point2D> vertices;
    long execTime;

    @Override
    public String toString() {
        return "Diagram{" + "site=" + site.toString() + ", cells=" + cells.toString()+ ", edges=" + edges.toString() + ", vertices=" + vertices.toString() + ", execTime=" + execTime + '}';
    }


}
