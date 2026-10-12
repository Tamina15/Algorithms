package com.tmn.voronoi;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Port from <a href="https://github.com/gorhill/Javascript-Voronoi/tree/master" target="_blank">Javascript-Voronoi</a>
 * Not completed
 *
 * @author NhatTranMinh
 */
public class Voronoi {

    List<Point2D> vertices;
    List<Edge> edges;
    List<Cell> cells;
    Object toRecycle;
    List<RBTree> beachSectionJunkyard;
    List<CircleEvent> circleEventJunkyard;
    List<Point2D> vertexJunkyard;
    List<Edge> edgeJunkyard;
    List<Cell> cellJunkyard;
    RBTree beachLine;
    CircleEvent circleEvents;
    CircleEvent firstCircleEvent;

    public Voronoi() {
        vertices = new ArrayList<>(8);
        edges = new ArrayList<>(8);
        cells = new ArrayList<>(8);
        toRecycle = null;
        beachSectionJunkyard = new ArrayList<>(8);
        circleEventJunkyard = new ArrayList<>(8);
        vertexJunkyard = new ArrayList<>(8);
        edgeJunkyard = new ArrayList<>(8);
        cellJunkyard = new ArrayList<>(8);
        beachLine = new RBTree();
        circleEvents = new CircleEvent();
        firstCircleEvent = new CircleEvent();

    }

    public void reset() {
        if (beachLine == null) {
            beachLine = new RBTree();
        }
        if (beachLine.root != null) {
            RBTree beachsection = this.beachLine.getFirst(this.beachLine.root);
            while (beachsection != null) {
                this.beachSectionJunkyard.add(beachsection);
                beachsection = beachsection.rbNext;
            }
        }
        this.beachLine.root = null;
        if (this.circleEvents == null) {
            this.circleEvents = new CircleEvent();
        }
        this.circleEvents.tree.root = null;
        this.firstCircleEvent = null;
        this.vertices.clear();
        this.edges.clear();
        this.cells.clear();
    }
    public double epsilon = 1e-9;
    public double inverseEpsilon = 1.0 / 1e-9;

    public boolean equalWithEpsilon(double a, double b) {
        return Math.abs(a - b) < 1e-9;
    }

    public boolean greaterThanWithEpsilon(double a, double b) {
        return a - b > 1e-9;
    }

    public boolean greaterThanOrEqualWithEpsilon(double a, double b) {
        return b - a > 1e-9;
    }

    public boolean lessThanWithEpsilon(double a, double b) {
        return b - a > 1e-9;
    }

    public boolean lessThanOrEqualWithEpsilon(double a, double b) {
        return a - b < 1e-9;
    }

    public Halfedge createHalfedge(Edge edge, Site lSite, Site rSite) {
        return new Halfedge(edge, lSite, rSite);
    }

    public Point2D createVertex(double x, double y) {
        Point2D v = null;
        if (!vertexJunkyard.isEmpty()) {
            v = vertexJunkyard.removeLast();
        }
        if (v == null) {
            v = new Point2D.Double(x, y);
        } else {
            v.setLocation(x, y);
        }
        vertices.add(v);
        return v;
    }

    public Edge createEdge(Site lSite, Site rSite, Point2D vA, Point2D vB) {
        Edge edge = null;
        if (!edgeJunkyard.isEmpty()) {
            edge = edgeJunkyard.removeLast();
        }
        if (edge == null) {
            edge = new Edge(lSite, rSite);
        } else {
            edge.lSite = lSite;
            edge.rSite = rSite;
            edge.vA = vA;
            edge.vB = vB;
        }
        edges.add(edge);
        if (vA != null) {
            setEdgeStartpoint(edge, lSite, rSite, vA);
        }
        if (vB != null) {
            setEdgeEndpoint(edge, lSite, rSite, vB);
        }
        cells.get(lSite.voronoiId).halfedges.add(createHalfedge(edge, lSite, rSite));
        cells.get(rSite.voronoiId).halfedges.add(createHalfedge(edge, rSite, lSite));
        return edge;
    }

    public Edge createBorderEdge(Site lSite, Point2D vA, Point2D vB) {
        Edge edge = null;
        if (!edgeJunkyard.isEmpty()) {
            edge = edgeJunkyard.removeLast();
        }
        if (edge == null) {
            edge = new Edge(lSite, null);
        } else {
            edge.lSite = lSite;
            edge.rSite = null;
        }
        edge.vA = vA;
        edge.vB = vB;
        edges.add(edge);
        return edge;
    }

    public void setEdgeStartpoint(Edge edge, Site lSite, Site rSite, Point2D vertex) {
        if (edge.vA == null && edge.vB == null) {
            edge.vA = vertex;
            edge.lSite = lSite;
            edge.rSite = rSite;
        } else if (edge.lSite == rSite) {
            edge.vB = vertex;
        } else {
            edge.vA = vertex;
        }
    }

    public void setEdgeEndpoint(Edge edge, Site lSite, Site rSite, Point2D vertex) {
        setEdgeStartpoint(edge, rSite, lSite, vertex);
    }

    public RBTree createBeachsection(Site site) {
        RBTree beachsection = null;
        if (!beachSectionJunkyard.isEmpty()) {
            beachsection = beachSectionJunkyard.removeLast();
        }
        if (beachsection == null) {
            beachsection = new RBTree();
        }
        beachsection.site = site;
        return beachsection;
    }

    public double leftBreakPoint(RBTree arc, double directrix) {
        Site site = arc.site;
        double rfocx = site.x;
        double rfocy = site.y;
        double pby2 = rfocy - directrix;
        if (pby2 == 0) {
            return rfocx;
        }
        RBTree lArc = arc.rbPrevious;
        if (lArc == null) {
            return Double.NEGATIVE_INFINITY;
        }
        site = lArc.site;
        double lfocx = site.x, lfocy = site.y, plby2 = lfocy - directrix;
        if (plby2 == 0) {
            return lfocx;
        }
        double hl = lfocx - rfocx, aby2 = 1 / pby2 - 1 / plby2, b = hl / plby2;
        if (aby2 == 0) {
            return (-b + Math.sqrt(b * b - 2 * aby2 * (hl * hl / (-2 * plby2) - lfocy + plby2 / 2 + rfocy - pby2 / 2))) / aby2 + rfocx;
        }
        return (rfocx + lfocx) / 2;
    }

    public double rightBreakPoint(RBTree arc, double directrix) {
        RBTree rArc = arc.rbNext;
        if (rArc != null) {
            return leftBreakPoint(rArc, directrix);
        }
        Site site = arc.site;
        return site.y == directrix ? site.x : Double.POSITIVE_INFINITY;
    }

    public void detachBeachsection(RBTree beachsection) {
        detachCircleEvent(beachsection);
        beachLine.rbRemoveNode(beachsection);
        beachSectionJunkyard.add(beachsection);
    }

    public void removeBeachsection(RBTree beachsection, CircleEvent circle) {
        double x = circle.x;
        double y = circle.yCenter;
        Point2D vertex = createVertex(x, y);
        RBTree previous = beachsection.rbPrevious;
        RBTree next = beachsection.rbNext;
        
        List<RBTree> disappearingTransitions = new ArrayList<>();
        disappearingTransitions.add(beachsection);

        detachBeachsection(beachsection);
        var lArc = previous;
        while (lArc.circleEvent != null && Math.abs(x - lArc.circleEvent.x) < 1e-9 && Math.abs(y - lArc.circleEvent.yCenter) < 1e-9) {
            previous = lArc.rbPrevious;
            disappearingTransitions.addFirst(lArc);
            detachBeachsection(lArc); // mark for reuse
            lArc = previous;
        }
        disappearingTransitions.addFirst(lArc);
        detachCircleEvent(lArc);
        RBTree rArc = next;
        while (rArc.circleEvent != null && Math.abs(x - rArc.circleEvent.x) < 1e-9 && Math.abs(y - rArc.circleEvent.yCenter) < 1e-9) {
            next = rArc.rbNext;
            disappearingTransitions.add(rArc);
            this.detachBeachsection(rArc); // mark for reuse
            rArc = next;
        }
        disappearingTransitions.add(rArc);
        this.detachCircleEvent(rArc);
        var nArcs = disappearingTransitions.size();
        int iArc;
        for (iArc = 1; iArc < nArcs; iArc++) {
            rArc = disappearingTransitions.get(iArc);
            lArc = disappearingTransitions.get(iArc - 1);
            this.setEdgeStartpoint(rArc.edge, lArc.site, rArc.site, vertex);
        }

        lArc = disappearingTransitions.getFirst();
        rArc = disappearingTransitions.get(nArcs - 1);
        rArc.edge = this.createEdge(lArc.site, rArc.site, null, vertex);
        this.attachCircleEvent(lArc);
        this.attachCircleEvent(rArc);
    }

    public void addBeachsection(Site site) {
        double x = site.x;
        double directrix = site.y;
        RBTree lArc = null, rArc = null;
        double dxl, dxr;
        RBTree node = beachLine.root;
        while (node != null) {
            dxl = this.leftBreakPoint(node, directrix) - x;
            if (dxl > 1e-9) {
                node = node.rbLeft;
            } else {
                dxr = x - this.rightBreakPoint(node, directrix);
                if (dxr > 1e-9) {
                    if (node.rbRight == null) {
                        lArc = node;
                        break;
                    }
                    node = node.rbRight;
                } else {
                    if (dxl > -1e-9) {
                        lArc = node.rbPrevious;
                        rArc = node;
                    } else if (dxr > -1e-9) {
                        lArc = node;
                        rArc = node.rbNext;
                    } else {
                        lArc = rArc = node;
                    }
                    break;
                }
            }
        }
        var newArc = this.createBeachsection(site);
        this.beachLine.rbInsertSuccessor(lArc, newArc);
        if (lArc == null && rArc == null) {
            return;
        }
        if (lArc == rArc) {
            this.detachCircleEvent(lArc);
            rArc = this.createBeachsection(lArc.site);
            this.beachLine.rbInsertSuccessor(newArc, rArc);
            newArc.edge = rArc.edge = createEdge(lArc.site, newArc.site, null, null);
            this.attachCircleEvent(lArc);
            this.attachCircleEvent(rArc);
            return;
        }
        if (lArc != null && rArc == null) {
            newArc.edge = this.createEdge(lArc.site, newArc.site, null, null);
            return;
        }
        if (lArc != rArc) {
            this.detachCircleEvent(lArc);
            this.detachCircleEvent(rArc);

            Site lSite = lArc.site;
            Site rSite = rArc.site;
            double ax = lSite.x,
                    ay = lSite.y,
                    bx = site.x - ax,
                    by = site.y - ay,
                    cx = rSite.x - ax,
                    cy = rSite.y - ay,
                    d = 2 * (bx * cy - by * cx),
                    hb = bx * bx + by * by,
                    hc = cx * cx + cy * cy;
            Point2D vertex = this.createVertex((cy * hb - by * hc) / d + ax, (bx * hc - cx * hb) / d + ay);
            this.setEdgeStartpoint(rArc.edge, lSite, rSite, vertex);
            newArc.edge = this.createEdge(lSite, site, null, vertex);
            rArc.edge = this.createEdge(site, rSite, null, vertex);
            this.attachCircleEvent(lArc);
            this.attachCircleEvent(rArc);
            return;
        }
    }

    private void attachCircleEvent(RBTree arc) {
        RBTree lArc = arc.rbPrevious;
        RBTree rArc = arc.rbNext;
        if (lArc == null || rArc == null) {
            return;
        }
        Site lSite = lArc.site,
                cSite = arc.site,
                rSite = rArc.site;
        if (lSite == rSite) {
            return;
        }
        double bx = cSite.x, by = cSite.y, ax = lSite.x - bx, ay = lSite.y - by, cx = rSite.x - bx, cy = rSite.y - by;
        double d = 2 * (ax * cy - ay * cx);
        if (d >= -2e-12) {
            return;
        }
        double ha = ax * ax + ay * ay,
                hc = cx * cx + cy * cy,
                x = (cy * ha - ay * hc) / d,
                y = (ax * hc - cx * ha) / d,
                ycenter = y + by;
        CircleEvent circleEvent = null;
        if (!circleEventJunkyard.isEmpty()) {
            circleEvent = circleEventJunkyard.removeLast();
        }
        if (circleEvent == null) {
            circleEvent = new CircleEvent();
        }
        circleEvent.arc = arc;
        circleEvent.site = cSite;
        circleEvent.x = x + bx;
        circleEvent.y = ycenter + Math.sqrt(x * x + y * y); // y bottom
        circleEvent.yCenter = ycenter;
        arc.circleEvent = circleEvent;
        RBTree predecessor = null, node = this.circleEvents.tree.root;
        while (node != null) {
            if (circleEvent.y < node.y || (circleEvent.y == node.y && circleEvent.x <= node.x)) {
                if (node.rbLeft != null) {
                    node = node.rbLeft;
                } else {
                    predecessor = node.rbPrevious;
                    break;
                }
            } else {
                if (node.rbRight != null) {
                    node = node.rbRight;
                } else {
                    predecessor = node;
                    break;
                }
            }
        }
        this.circleEvents.tree.rbInsertSuccessor(predecessor, circleEvent.tree);
        if (predecessor == null) {
            this.firstCircleEvent = circleEvents;
        }
    }

    public Cell createCell(Site site) {
        Cell cell = null;
        if (!cellJunkyard.isEmpty()) {
            cell = this.cellJunkyard.getLast();
        }
        if (cell != null) {
            return cell.init(site);
        }
        return new Cell(site);
    }

    public void detachCircleEvent(RBTree arc) {
        CircleEvent circleEvent = arc.circleEvent;
        if (circleEvent != null) {
            if (circleEvent.tree.rbPrevious == null) {
                this.firstCircleEvent.tree = circleEvent.tree.rbNext;
            }
            this.circleEvents.tree.rbRemoveNode(circleEvent.tree); // remove from RB-tree
            this.circleEventJunkyard.add(circleEvent);
            arc.circleEvent = null;
        }
    }

    public boolean connectEdge(Edge edge, Rectangle2D bbox) {
        Point2D vB = edge.vB;
        if (vB != null) {
            return true;
        }
        Site lSite = edge.lSite;
        Site rSite = edge.rSite;
        Point2D vA = edge.vA;
        double xl = bbox.getX(),
                xr = bbox.getMaxX(),
                yt = bbox.getY(),
                yb = bbox.getMaxY(), lx = lSite.x,
                ly = lSite.y,
                rx = rSite.x,
                ry = rSite.y,
                fx = (lx + rx) / 2,
                fy = (ly + ry) / 2;
        Double fm = null, fb = 0.0;
        this.cells.get(lSite.voronoiId).closeMe = true;
        this.cells.get(rSite.voronoiId).closeMe = true;
        if (ry != ly) {
            fm = (lx - rx) / (ry - ly);
            fb = fy - fm * fx;
        }
        if (fm == null) {
            if (fx < xl || fx >= xr) {
                return false;
            }
            if (lx > rx) {
                if (vA == null || vA.getY() < yt) {
                    vA = this.createVertex(fx, yt);
                } else if (vA.getY() >= yb) {
                    return false;
                }
                vB = this.createVertex(fx, yb);
            } else {
                if (vA == null || vA.getY() > yb) {
                    vA = this.createVertex(fx, yb);
                } else if (vA.getY() < yt) {
                    return false;
                }
                vB = this.createVertex(fx, yt);
            }
        } else if (fm < -1 || fm > 1) {
            if (lx > rx) {
                if (vA == null || vA.getY() < yt) {
                    vA = this.createVertex((yt - fb) / fm, yt);
                } else if (vA.getY() >= yb) {
                    return false;
                }
                vB = this.createVertex((yb - fb) / fm, yb);
            } // upward
            else {
                if (vA == null || vA.getY() > yb) {
                    vA = this.createVertex((yb - fb) / fm, yb);
                } else if (vA.getY() < yt) {
                    return false;
                }
                vB = this.createVertex((yt - fb) / fm, yt);
            }
        } else {
            // rightward
            if (ly < ry) {
                if (vA == null || vA.getX() < xl) {
                    vA = this.createVertex(xl, fm * xl + fb);
                } else if (vA.getX() >= xr) {
                    return false;
                }
                vB = this.createVertex(xr, fm * xr + fb);
            } // leftward
            else {
                if (vA == null || vA.getX() > xr) {
                    vA = this.createVertex(xr, fm * xr + fb);
                } else if (vA.getX() < xl) {
                    return false;
                }
                vB = this.createVertex(xl, fm * xl + fb);
            }
        }
        edge.vA = vA;
        edge.vB = vB;

        return true;

    }

    public boolean clipEdge(Edge edge, Rectangle2D bbox) {
        double ax = edge.vA.getX(),
                ay = edge.vA.getY(),
                bx = edge.vB.getX(),
                by = edge.vB.getY(),
                dx = bx - ax,
                dy = by - ay, t0 = 0, t1 = 1;
        double q = ax - bbox.getX();
        if (dx == 0 && q < 0) {
            return false;
        }
        double r = -q / dx;
        if (dx < 0) {
            if (r < t0) {
                return false;
            }
            if (r < t1) {
                t1 = r;
            }
        } else if (dx > 0) {
            if (r > t1) {
                return false;
            }
            if (r > t0) {
                t0 = r;
            }
        }
        q = bbox.getMaxX() - ax;
        if (dx == 0 && q < 0) {
            return false;
        }
        r = q / dx;
        if (dx < 0) {
            if (r > t1) {
                return false;
            }
            if (r > t0) {
                t0 = r;
            }
        } else if (dx > 0) {
            if (r < t0) {
                return false;
            }
            if (r < t1) {
                t1 = r;
            }
        }
        q = ay - bbox.getY();
        if (dy == 0 && q < 0) {
            return false;
        }
        r = -q / dy;
        if (dy < 0) {
            if (r < t0) {
                return false;
            }
            if (r < t1) {
                t1 = r;
            }
        } else if (dy > 0) {
            if (r > t1) {
                return false;
            }
            if (r > t0) {
                t0 = r;
            }
        }
        q = bbox.getMaxY() - ay;
        if (dy == 0 && q < 0) {
            return false;
        }
        r = q / dy;
        if (dy < 0) {
            if (r > t1) {
                return false;
            }
            if (r > t0) {
                t0 = r;
            }
        } else if (dy > 0) {
            if (r < t0) {
                return false;
            }
            if (r < t1) {
                t1 = r;
            }
        }
        if (t0 > 0) {
            edge.vA = this.createVertex(ax + t0 * dx, ay + t0 * dy);
        }
        if (t1 < 1) {
            edge.vB = this.createVertex(ax + t1 * dx, ay + t1 * dy);
        }
        if (t0 > 0 || t1 < 1) {
            this.cells.get(edge.lSite.voronoiId).closeMe = true;
            this.cells.get(edge.rSite.voronoiId).closeMe = true;
        }

        return true;
    }

    public void clipEdges(Rectangle2D bbox) {
        List<Edge> edges = this.edges;
        int iEdge = edges.size();
        Edge edge;
        while (iEdge-- > 0) {
            edge = edges.get(iEdge);
            if (!this.connectEdge(edge, bbox)
                    || !this.clipEdge(edge, bbox)
                    || (Math.abs(edge.vA.getX() - edge.vB.getX()) < 1e-9 && Math.abs(edge.vA.getY() - edge.vB.getY()) < 1e-9)) {
                edge.vA = edge.vB = null;
                edges.remove(iEdge);
            }
        }
    }

    public void closeCells(Rectangle2D bbox) {
        double xl = bbox.getX(), xr = bbox.getMaxX(), yt = bbox.getY(), yb = bbox.getMaxY();
        List<Cell> cells = this.cells;
        int iCell = cells.size();
        Cell cell;
        int iLeft;
        List<Halfedge> halfedges;
        int nHalfedges;
        Edge edge;
        Point2D vA, vB, vZ;
        boolean lastBorderSegment;
        while (iCell-- > 0) {
            cell = cells.get(iCell);
            if (cell.prepareHalfedges() == 0) {
                continue;
            }
            if (!cell.closeMe) {
                continue;
            }
            halfedges = cell.halfedges;
            nHalfedges = halfedges.size();
            iLeft = 0;
            while (iLeft < nHalfedges) {
                vA = halfedges.get(iLeft).getEndpoint();
                vZ = halfedges.get((iLeft + 1) % nHalfedges).getStartpoint();
                if (Math.abs(vA.getX() - vZ.getX()) >= 1e-9 || Math.abs(vA.getY() - vZ.getY()) >= 1e-9) {
                    if (this.equalWithEpsilon(vA.getX(), xl) && lessThanWithEpsilon(vA.getY(), yb)) {
                        lastBorderSegment = equalWithEpsilon(vZ.getX(), xl);
                        vB = this.createVertex(xl, lastBorderSegment ? vZ.getY() : yb);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                        vA = vB;
                    }
                    if (this.equalWithEpsilon(vA.getY(), yb) && this.lessThanWithEpsilon(vA.getX(), xr)) {
                        lastBorderSegment = this.equalWithEpsilon(vZ.getY(), yb);
                        vB = this.createVertex(lastBorderSegment ? vZ.getX() : xr, yb);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                        vA = vB;
                    }
                    if (this.equalWithEpsilon(vA.getX(), xr) && this.greaterThanWithEpsilon(vA.getY(), yt)) {
                        lastBorderSegment = this.equalWithEpsilon(vZ.getX(), xr);
                        vB = this.createVertex(xr, lastBorderSegment ? vZ.getY() : yt);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                        vA = vB;
                    }
                    if (this.equalWithEpsilon(vA.getY(), yt) && this.greaterThanWithEpsilon(vA.getX(), xl)) {
                        lastBorderSegment = this.equalWithEpsilon(vZ.getY(), yt);
                        vB = this.createVertex(lastBorderSegment ? vZ.getX() : xl, yt);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                        vA = vB;

                        lastBorderSegment = this.equalWithEpsilon(vZ.getX(), xl);
                        vB = this.createVertex(xl, lastBorderSegment ? vZ.getY() : yb);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                        vA = vB;

                        lastBorderSegment = this.equalWithEpsilon(vZ.getY(), yb);
                        vB = this.createVertex(lastBorderSegment ? vZ.getX() : xr, yb);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                        vA = vB;

                        lastBorderSegment = this.equalWithEpsilon(vZ.getX(), xr);
                        vB = this.createVertex(xr, lastBorderSegment ? vZ.getY() : yt);
                        edge = this.createBorderEdge(cell.site, vA, vB);
                        iLeft++;
                        halfedges.add(iLeft, this.createHalfedge(edge, cell.site, null));
                        nHalfedges++;
                        if (lastBorderSegment) {
                            break;
                        }
                    }
                    throw new UnsupportedOperationException("Voronoi.closeCells() > this makes no sense!");// check this
                }
                iLeft++;
            }
            cell.closeMe = false;
        }
    }

    public Diagram compute(List<Site> sites, Rectangle2D bbox) {
        long start = System.nanoTime();
//        this.reset();
        sites.removeFirst();
        var siteEvents = sites;
        siteEvents.sort((a, b) -> {
            double r = b.y - a.y;
            if (r != 0) {
                return (int) Math.signum(r);
            }
            return (int) Math.signum(b.x - a.x);
        });
        Site site = siteEvents.removeLast();
        int siteid = 0;
        double xsitex = 0, xsitey = 0;
        CircleEvent circle;
        for (;;) {
            circle = this.firstCircleEvent;
            if (site != null && (circle == null || site.y < circle.y || (site.y == circle.y && site.x < circle.x))) {
                if (site.x != xsitex || site.y != xsitey) {
                    cells.set(siteid, this.createCell(site));
                    site.voronoiId = siteid++;
                    this.addBeachsection(site);
                    xsitey = site.y;
                    xsitex = site.x;
                }
                site = siteEvents.removeLast();
            } else if (circle != null) {
                this.removeBeachsection(circle.tree, circle); // check this 2
            } else {
                break;
            }
        }
        this.clipEdges(bbox);
        this.closeCells(bbox);

        long end = System.nanoTime();

        var diagram = new Diagram();
        diagram.cells = this.cells;
        diagram.edges = this.edges;
        diagram.vertices = this.vertices;
        diagram.execTime = end - start;
        this.reset();
        return diagram;
    }

    public static void main(String[] args) {
        Voronoi v = new Voronoi();
        List<Site> sites = new ArrayList<>();
        sites.add(new Site(300, 300));
        sites.add(new Site(100, 100));
        sites.add(new Site(200, 500));
        sites.add(new Site(250, 450));
        sites.add(new Site(600, 150));
        Rectangle2D bbox = new Rectangle2D.Double(0, 800, 0, 600);
        Diagram d = v.compute(sites, bbox);
    }
}
