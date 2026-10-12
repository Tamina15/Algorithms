package com.tmn.voronoi;

public class CircleEvent {
    
    double x, y, yCenter;
    Object arc;
    Site site = null;
    RBTree tree = null;
    
    public CircleEvent() {
        this(new RBTree());
    }
    
    public CircleEvent(RBTree tree) {
        this.arc = 0;
        this.tree = tree;
        this.site = null;
        this.x = this.y = this.yCenter = 0;
    }
    
}
