package com.tmn.polygontriangulation;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;

public interface DelaunayTriangulation {

    public List<Triangle> triangulate(Point2D[] points);

    record Pair<U, V>(U first, V second) {
    }

    record Triplet<T, U, V>(T first, U second, V third) {

    }

    public class Edge {

        Point2D a, b;

        public Edge(Point2D a, Point2D b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Edge e) {
                return (a.equals(e.a) && b.equals(e.b)) || (a.equals(e.b) && b.equals(e.a));
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.a);
            hash = 37 * hash + Objects.hashCode(this.b);
            return hash;
        }

    }

    public class Triangle {

        Point2D a, b, c;
        Edge A, B, C;

        public Triangle(Point2D a, Point2D b, Point2D c) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.A = new Edge(a, b);
            this.B = new Edge(b, c);
            this.C = new Edge(c, a);
        }

        public boolean contains(Point2D p) {
            return PolygonUtils.isPointInTriangle(p, a, b, c);
        }

        public boolean ccw() {
            return PolygonUtils.ccw(a, b, c);
        }

        public boolean circumcircleContains(Point2D p) {
            return PolygonUtils.isPointInCircumcircle(p, a, b, c);
        }

        public boolean containsEdge(Edge edge) {
            return A.equals(edge) || B.equals(edge) || C.equals(edge);
        }

        public boolean containsVertex(Point2D p) {
            return a.equals(p) || b.equals(p) || c.equals(p);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Triangle t) {
                return a.equals(t.a) && b.equals(t.b) && c.equals(t.c);
            }
            return Objects.equals(this, obj);
        }

        public boolean sameVertex(Triangle t) {
            return vertexHashcode() == t.vertexHashcode();
        }

        private int vertexHashcode() {
            return a.hashCode() + b.hashCode() + c.hashCode();
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 67 * hash + Objects.hashCode(this.a);
            hash = 67 * hash + Objects.hashCode(this.b);
            hash = 67 * hash + Objects.hashCode(this.c);
            return hash;
        }

        public void draw(Graphics2D g2d) {
            Path2D path = new Path2D.Double();
            path.moveTo(a.getX(), a.getY());
            path.lineTo(b.getX(), b.getY());
            path.lineTo(c.getX(), c.getY());
            path.closePath();
            g2d.draw(path);
        }

        public void fill(Graphics2D g2d) {
            Path2D path = new Path2D.Double();
            path.moveTo(a.getX(), a.getY());
            path.lineTo(b.getX(), b.getY());
            path.lineTo(c.getX(), c.getY());
            path.closePath();
            g2d.fill(path);
        }

        @Override
        public String toString() {
            return "Triangle: " + a.toString() + ", " + b.toString() + ", " + c.toString();
        }

    }
}
