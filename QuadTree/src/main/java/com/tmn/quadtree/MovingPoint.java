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
        range = new Rectangle.Double(0, 0, 2 * collisionLength, 2 * collisionLength);
        range.x = position.x - range.width / 2;
        range.y = position.y - range.height / 2;
    }

    public void update() {
        position.add(speed);
        bounce(boundary);
//        position.wrap(boundary);
        double collisionX = position.x - collisionLength / 2;
        double collisionY = position.y - collisionLength / 2;
        collisionBox.x = collisionX;
        collisionBox.y = collisionY;
        range.x = position.x - range.width / 2;
        range.y = position.y - range.height / 2;
    }

    public void bounce(double minX, double minY, double maxX, double maxY) {
        if (position.x <= minX || position.x > maxX) {
            speed.x = -speed.x; // Reverse horizontal direction
        }
        if (position.y <= minY || position.y > maxY) {
            speed.y = -speed.y; // Reverse horizontal direction
        }
    }

    public void bounce(Rectangle.Double boundary) {
        if (position.x <= boundary.getMinX() || position.x > boundary.getMaxX()) {
            speed.x = -speed.x; // Reverse horizontal direction
        }
        if (position.y <= boundary.getMinY() || position.y > boundary.getMaxY()) {
            speed.y = -speed.y; // Reverse horizontal direction
        }
    }

    public boolean collide(MovingPoint other) {
        return collisionBox.intersects(other.collisionBox);
    }

}
