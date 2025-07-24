package com.tmn.quadtree;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class MovingPoint {

    Rectangle.Double boundary, collisionBox, range;
    Point position;
    Point speed;
    double collisionLength;

    public MovingPoint(Point position, Point speed) {
        this.position = position;
        this.speed = speed;
    }

    public MovingPoint(Point position, Point speed, Rectangle.Double boundary) {
        this(position, speed);
        this.boundary = boundary;
    }

    public MovingPoint(Point position, Point speed, Rectangle.Double boundary, double collisionLength) {
        this(position, speed, boundary);
        this.collisionLength = collisionLength;

        double collisionX = position.x - collisionLength / 2;
        double collisionY = position.y - collisionLength / 2;

        collisionBox = new Rectangle2D.Double(collisionX, collisionY, collisionLength, collisionLength);
        range = new Rectangle.Double(0, 0, 3 * collisionLength, 3 * collisionLength);
        range.x = position.x - range.width / 2;
        range.y = position.y - range.height / 2;
    }

    public void update() {
        position.add(speed);
        position.wrap(boundary);
        double collisionX = position.x - collisionLength / 2;
        double collisionY = position.y - collisionLength / 2;
        collisionBox.x = collisionX;
        collisionBox.y = collisionY;
        range.x = position.x - range.width / 2;
        range.y = position.y - range.height / 2;
    }

    public boolean collide(MovingPoint other) {
        return collisionBox.intersects(other.collisionBox);
    }

}
