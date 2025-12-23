package com.tmn.polygontriangulation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Delaunay triangulation class port from Bathlamos JS implementation.
 *
 * @see <a href = "https://github.com/Bathlamos/delaunay-triangulation">Bathlamos's delaunay-triangulation on GitHub</a>
 * @author Bathlamos
 * @author NhatTranMinh
 */
public class BathlamosDelaunayTriangulation implements DelaunayTriangulation {

    @Override
    public List<Triangle> triangulate(Point2D[] points) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Point2D> triangulate1(Point2D[] ps) {
        Arrays.sort(ps, (a, b) -> {
            if (a.getX() == b.getX()) {
                return (int) Math.signum(a.getY() - b.getY());
            }
            return (int) Math.signum(a.getX() - b.getX());
        });

        int j = 0;
        for (Point2D p : ps) {
            if (!p.equals(ps[j])) {
                j++;
                ps[j] = p;
            }
        }
        Point2D[] points = Arrays.copyOfRange(ps, 0, j + 1);
        if (points.length < 2) {
            return new ArrayList<>();
        }

        QuadEdge quadEdge = delaunay1(points, 0, points.length - 1).le;

        // All edges marked false
        List<Point2D> faces = new ArrayList<>();
        int queueIndex = 0;
        List<QuadEdge> queue = new ArrayList<>();
        queue.add(quadEdge);

        // Mark all outer edges as visited
        while (leftOf(quadEdge.onext.dest(), quadEdge)) {
            quadEdge = quadEdge.onext;
        }

        var curr = quadEdge;
        do {
            queue.add(curr.sym());
            curr.mark = true;
            curr = curr.lnext();
        } while (curr != quadEdge);

        do {
            var edge = queue.get(queueIndex++);
            if (!edge.mark) {
                // Stores the edges for a visited triangle. Also pushes sym (neighbour) edges on stack to visit later.
                curr = edge;
                do {
                    faces.add(curr.orig);
                    if (!curr.sym().mark) {
                        queue.add(curr.sym());
                    }
                    curr.mark = true;
                    curr = curr.lnext();
                } while (curr != edge);
            }
        } while (queueIndex < queue.size());
        return faces;
    }

    /**
     * Direct port from Bathlamos repository.
     *
     * @param ps a array of 2D points
     * @return an array of points: every triplet denotes the vertices of a triangle in the Delaunay triangulation.
     */
    public List<Point2D> triangulate0(Point2D[] ps) {
        List<Point2D> pointsList = Arrays.asList(ps);
        // Initial sorting of the points
        pointsList.sort((a, b) -> {
            if (a.getX() == b.getX()) {
                return (int) Math.signum(a.getY() - b.getY());
            }
            return (int) Math.signum(a.getX() - b.getX());
        });

        // Remove duplicates
        for (int i = pointsList.size() - 1; i >= 1; i--) {
            if (pointsList.get(i).equals(pointsList.get(i - 1))) {
                pointsList.remove(i);
                i++;
            }
        }
        Point2D[] points = pointsList.toArray(Point2D[]::new);

        if (points.length < 2) {
            return new ArrayList<>();
        }

        QuadEdge quadEdge = delaunay(points).le;

        // All edges marked false
        List<Point2D> faces = new ArrayList<>();
        int queueIndex = 0;
        List<QuadEdge> queue = new ArrayList<>();
        queue.add(quadEdge);

        // Mark all outer edges as visited
        while (leftOf(quadEdge.onext.dest(), quadEdge)) {
            quadEdge = quadEdge.onext;
        }

        var curr = quadEdge;
        do {
            queue.add(curr.sym());
            curr.mark = true;
            curr = curr.lnext();
        } while (curr != quadEdge);

        do {
            var edge = queue.get(queueIndex++);
            if (!edge.mark) {
                // Stores the edges for a visited triangle. Also pushes sym (neighbour) edges on stack to visit later.
                curr = edge;
                do {
                    faces.add(curr.orig);
                    if (!curr.sym().mark) {
                        queue.add(curr.sym());
                    }
                    curr.mark = true;
                    curr = curr.lnext();
                } while (curr != edge);
            }
        } while (queueIndex < queue.size());
        return faces;
    }

    private boolean ccw(Point2D a, Point2D b, Point2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()) > 0;
    }

    private boolean rightOf(Point2D x, QuadEdge e) {
        return ccw(x, e.dest(), e.orig);
    }

    private boolean leftOf(Point2D x, QuadEdge e) {
        return ccw(x, e.orig, e.dest());
    }

    private boolean valid(QuadEdge e, QuadEdge basel) {
        return rightOf(e.dest(), basel);
    }

    private boolean inCircle(Point2D a, Point2D b, Point2D c, Point2D d) {
        return PolygonUtils.isPointInCircumcircle(d, a, b, c);
    }

    private QuadEdge makeEdge(Point2D orig, Point2D dest) {
        var q0 = new QuadEdge(null, null, orig);
        var q1 = new QuadEdge(null, null, null);
        var q2 = new QuadEdge(null, null, dest);
        var q3 = new QuadEdge(null, null, null);

        // create the segment
        q0.onext = q0;
        q2.onext = q2; // lonely segment: no "next" quadedge
        q1.onext = q3;
        q3.onext = q1; // in the dual: 2 communicating facets

        // dual switch
        q0.rot = q1;
        q1.rot = q2;
        q2.rot = q3;
        q3.rot = q0;
        return q0;

    }

    /**
     * Attach/detach the two edges = combine/split the two rings in the dual space
     *
     * @param a the first QuadEdge to attach/detach
     * @param b the second QuadEdge to attach/detach
     */
    private void splice(QuadEdge a, QuadEdge b) {
        QuadEdge alpha = a.onext.rot, beta = b.onext.rot;

        QuadEdge t2 = a.onext, t3 = beta.onext, t4 = alpha.onext;

        a.onext = b.onext;
        b.onext = t2;
        alpha.onext = t3;
        beta.onext = t4;
    }

    /**
     * Create a new QuadEdge by connecting 2 QuadEdges
     *
     * @param a the first QuadEdges to connect
     * @param b the second QuadEdges to connect
     * @return the new QuadEdge
     */
    private QuadEdge connect(QuadEdge a, QuadEdge b) {
        var q = makeEdge(a.dest(), b.orig);
        splice(q, a.lnext());
        splice(q.sym(), b);
        return q;
    }

    private void deleteEdge(QuadEdge q) {
        splice(q, q.oprev());
        splice(q.sym(), q.sym().oprev());
    }

    private Delaunay delaunay(Point2D[] s) {
        QuadEdge a, b, c, t;
        switch (s.length) {
            case 2 -> {
                a = makeEdge(s[0], s[1]);
                return new Delaunay(a, a.sym());
            }
            case 3 -> {
                a = makeEdge(s[0], s[1]);
                b = makeEdge(s[1], s[2]);
                splice(a.sym(), b);
                if (ccw(s[0], s[1], s[2])) {
                    c = connect(b, a);
                    return new Delaunay(a, b.sym());
                } else if (ccw(s[0], s[2], s[1])) {
                    c = connect(b, a);
                    return new Delaunay(c.sym(), c);
                } else { // All three points are colinear
                    return new Delaunay(a, b.sym());
                }
            }
            default -> { // |S| >= 4
                int half_length = s.length / 2;
                var left = delaunay(Arrays.copyOfRange(s, 0, half_length));
                var right = delaunay(Arrays.copyOfRange(s, half_length, s.length));

                var ldo = left.le;
                var ldi = left.re;
                var rdi = right.le;
                var rdo = right.re;

                // Compute the lower common tangent of L and R
                do {
                    if (leftOf(rdi.orig, ldi)) {
                        ldi = ldi.lnext();
                    } else if (rightOf(ldi.orig, rdi)) {
                        rdi = rdi.rprev();
                    } else {
                        break;
                    }
                } while (true);

                var basel = connect(rdi.sym(), ldi);
                if (ldi.orig == ldo.orig) {
                    ldo = basel.sym();
                }
                if (rdi.orig == rdo.orig) {
                    rdo = basel;
                }

                // This is the merge loop.
                do {
                    // Locate the first L point (lcand.Dest) to be encountered by the rising bubble,
                    // and delete L edges out of base1.Dest that fail the circle test.
                    var lcand = basel.sym().onext;
                    if (valid(lcand, basel)) {
                        while (inCircle(basel.dest(), basel.orig, lcand.dest(), lcand.onext.dest())) {
                            t = lcand.onext;
                            deleteEdge(lcand);
                            lcand = t;
                        }
                    }

                    // Symmetrically, locate the first R point to be hit, and delete R edges
                    var rcand = basel.oprev();
                    if (valid(rcand, basel)) {
                        while (inCircle(basel.dest(), basel.orig, rcand.dest(), rcand.oprev().dest())) {
                            t = rcand.oprev();
                            deleteEdge(rcand);
                            rcand = t;
                        }
                    }

                    // If both lcand and rcand are invalid, then basel is the upper common tangent
                    if (!valid(lcand, basel) && !valid(rcand, basel)) {
                        break;
                    }

                    // The next cross edge is to be connected to either lcand.Dest or rcand.Dest
                    // If both are valid, then choose the appropriate one using the InCircle test
                    if (!valid(lcand, basel) || (valid(rcand, basel) && inCircle(lcand.dest(), lcand.orig, rcand.orig, rcand.dest()))) // Add cross edge basel from rcand.Dest to basel.Dest
                    {
                        basel = connect(rcand, basel.sym());
                    } else {
                        // Add cross edge base1 from basel.Org to lcand. Dest
                        basel = connect(basel.sym(), lcand.sym());
                    }

                } while (true);
                return new Delaunay(ldo, rdo);
            }
        }
    }

    private Delaunay delaunay1(Point2D[] s, int start, int end) {
        QuadEdge a, b, c, t;
        switch (end - start) {
            case 2 -> {
                a = makeEdge(s[start], s[start + 1]);
                return new Delaunay(a, a.sym());
            }
            case 3 -> {
                int zero = start, one = start + 1, two = start + 2;
                a = makeEdge(s[zero], s[one]);
                b = makeEdge(s[one], s[two]);
                splice(a.sym(), b);
                if (ccw(s[zero], s[one], s[two])) {
                    c = connect(b, a);
                    return new Delaunay(a, b.sym());
                } else if (ccw(s[zero], s[two], s[one])) {
                    c = connect(b, a);
                    return new Delaunay(c.sym(), c);
                } else { // All three points are colinear
                    return new Delaunay(a, b.sym());
                }
            }
            default -> { // |S| >= 4
                int half_length = start + (end - start) / 2;
                var left = delaunay1(s, start, half_length);
                var right = delaunay1(s, half_length, end);

                var ldo = left.le;
                var ldi = left.re;
                var rdi = right.le;
                var rdo = right.re;

                // Compute the lower common tangent of L and R
                do {
                    if (leftOf(rdi.orig, ldi)) {
                        ldi = ldi.lnext();
                    } else if (rightOf(ldi.orig, rdi)) {
                        rdi = rdi.rprev();
                    } else {
                        break;
                    }
                } while (true);

                var basel = connect(rdi.sym(), ldi);
                if (ldi.orig == ldo.orig) {
                    ldo = basel.sym();
                }
                if (rdi.orig == rdo.orig) {
                    rdo = basel;
                }

                // This is the merge loop.
                do {
                    // Locate the first L point (lcand.Dest) to be encountered by the rising bubble,
                    // and delete L edges out of base1.Dest that fail the circle test.
                    var lcand = basel.sym().onext;
                    if (valid(lcand, basel)) {
                        while (inCircle(basel.dest(), basel.orig, lcand.dest(), lcand.onext.dest())) {
                            t = lcand.onext;
                            deleteEdge(lcand);
                            lcand = t;
                        }
                    }

                    // Symmetrically, locate the first R point to be hit, and delete R edges
                    var rcand = basel.oprev();
                    if (valid(rcand, basel)) {
                        while (inCircle(basel.dest(), basel.orig, rcand.dest(), rcand.oprev().dest())) {
                            t = rcand.oprev();
                            deleteEdge(rcand);
                            rcand = t;
                        }
                    }

                    // If both lcand and rcand are invalid, then basel is the upper common tangent
                    if (!valid(lcand, basel) && !valid(rcand, basel)) {
                        break;
                    }

                    // The next cross edge is to be connected to either lcand.Dest or rcand.Dest
                    // If both are valid, then choose the appropriate one using the InCircle test
                    if (!valid(lcand, basel) || (valid(rcand, basel) && inCircle(lcand.dest(), lcand.orig, rcand.orig, rcand.dest()))) // Add cross edge basel from rcand.Dest to basel.Dest
                    {
                        basel = connect(rcand, basel.sym());
                    } else {
                        // Add cross edge base1 from basel.Org to lcand. Dest
                        basel = connect(basel.sym(), lcand.sym());
                    }

                } while (true);
                return new Delaunay(ldo, rdo);
            }
        }
    }

    record Delaunay(QuadEdge le, QuadEdge re) {

    }

    class QuadEdge {

        QuadEdge onext;
        QuadEdge rot;
        Point2D orig;
        boolean mark;

        public QuadEdge(QuadEdge onext, QuadEdge rot, Point2D orig) {
            this.onext = onext;
            this.rot = rot;
            this.orig = orig;
            mark = false;
        }

        public QuadEdge sym() {
            return this.rot.rot;
        }

        public Point2D dest() {
            return this.sym().orig;
        }

        public QuadEdge rotSym() {
            return this.rot.sym();
        }

        public QuadEdge oprev() {
            return this.rot.onext.rot;
        }

        public QuadEdge dprev() {
            return this.rotSym().onext.rotSym();
        }

        public QuadEdge lnext() {
            return this.rotSym().onext.rot;
        }

        public QuadEdge lprev() {
            return this.onext.sym();
        }

        public QuadEdge rprev() {
            return this.sym().onext;
        }
    }
}
