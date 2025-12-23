package com.tmn.polygontriangulation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteforceDelaunayTriangulation implements DelaunayTriangulation {

    public List<Triangle> triangulate(Point2D[] points) {
        List<Triangle> triangulation = new ArrayList<>();

        Triangle superTriangle = PolygonUtils.getCoveredTriangle(points);
        triangulation.add(superTriangle);

        Set<Triangle> badTriangles = new HashSet<>();
        Set<Edge> polygon = new HashSet<>();
        for (Point2D p : points) {
            badTriangles.clear();
            for (Triangle triangle : triangulation) {
                if (triangle.circumcircleContains(p)) {
                    badTriangles.add(triangle);
                }
            }
            polygon.clear();
            for (Triangle triangle : badTriangles) {
                boolean sharedA = false, sharedB = false, sharedC = false;
                for (Triangle other : badTriangles) {
                    if (other == triangle) {
                        continue;
                    }
                    if (!sharedA && other.containsEdge(triangle.A)) {
                        sharedA = true;
                    }
                    if (!sharedB && other.containsEdge(triangle.B)) {
                        sharedB = true;
                    }

                    if (!sharedC && other.containsEdge(triangle.C)) {
                        sharedC = true;
                    }
                }
                if (!sharedA) {
                    polygon.add(triangle.A);
                }
                if (!sharedB) {
                    polygon.add(triangle.B);
                }
                if (!sharedC) {
                    polygon.add(triangle.C);
                }
            }
            triangulation.removeAll(badTriangles);
            for (Edge edge : polygon) {
                Triangle t = new Triangle(edge.a, edge.b, p);
                triangulation.add(t);
            }
        }
        triangulation.removeIf((triangle)
                -> superTriangle.containsVertex(triangle.a)
                || superTriangle.containsVertex(triangle.b)
                || superTriangle.containsVertex(triangle.c)
        );

        return triangulation;
    }
}
