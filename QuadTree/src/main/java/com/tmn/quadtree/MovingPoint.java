package com.tmn.quadtree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MovingPoint extends Point2D.Double {

    Rectangle2D.Double boundary, collisionBox, range;
    double speedX, speedY;
    double collisionLength;

    public MovingPoint(Point2D position, Point2D speed) {
        this.x = position.getX();
        this.y = position.getY();
        speedX = speed.getX();
        speedY = speed.getY();
    }

    public MovingPoint(Point2D position, Point2D speed, Rectangle2D.Double boundary) {
        this(position, speed);
        this.boundary = boundary;
    }

    public MovingPoint(Point2D position, Point2D speed, Rectangle2D.Double boundary, double collisionLength) {
        this(position, speed, boundary);
        this.collisionLength = collisionLength;

        double collisionX = position.getX() - collisionLength / 2;
        double collisionY = position.getY() - collisionLength / 2;

        collisionBox = new Rectangle2D.Double(collisionX, collisionY, collisionLength, collisionLength);
        range = new Rectangle2D.Double(0, 0, 2 * collisionLength, 2 * collisionLength);
        range.x = position.getX() - range.getWidth() / 2;
        range.y = position.getY() - range.getHeight() / 2;
    }

    public void update() {
        x += speedX;
        y += speedY;

        bounce(boundary);
//        position.wrap(boundary);

        double collisionX = x - collisionLength / 2;
        double collisionY = y - collisionLength / 2;
        collisionBox.x = collisionX;
        collisionBox.y = collisionY;

        range.x = x - range.width / 2;
        range.y = y - range.height / 2;
    }

    public void bounce(double minX, double minY, double maxX, double maxY) {
        if (x <= minX || x > maxX) {
            speedX = -speedX; // Reverse horizontal direction
        }
        if (y <= minY || y > maxY) {
            speedY = -speedY; // Reverse horizontal direction
        }
    }

    public void bounce(Rectangle2D.Double boundary) {
        if (x <= boundary.getMinX() || x > boundary.getMaxX()) {
            speedX = -speedX; // Reverse horizontal direction
        }
        if (y <= boundary.getMinY() || y > boundary.getMaxY()) {
            speedY = -speedY; // Reverse horizontal direction
        }
    }

    public boolean collide(MovingPoint other) {
        return collisionBox.intersects(other.collisionBox);
    }

}
