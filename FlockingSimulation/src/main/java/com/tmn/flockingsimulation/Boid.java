package com.tmn.flockingsimulation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class Boid {

    Point position;
    Point velocity;
    Point acceleration;
    int index;
    Rectangle bound;

    public Boid(int index) {
        this.index = index;
        this.bound = new Rectangle();
        this.position = new Point();
        this.velocity = new Point();
        this.acceleration = new Point();
    }

    public Boid(Boid b) {
        this.index = b.index;
        this.bound = new Rectangle(b.bound);
        this.position = new Point(b.position);
        this.velocity = new Point(b.velocity);
        this.acceleration = new Point(b.acceleration);
    }

    public Boid(int index, Rectangle bound, Point position, Point velocity, Point acceleration) {
        this.index = index;
        this.bound = bound;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Boid(int index, Rectangle bound) {
        this.index = index;
        this.bound = new Rectangle(bound);
        Random r = new Random();
        this.position = new Point(r.nextInt(this.bound.x, (int) this.bound.getMaxX() + 1), r.nextInt(this.bound.y, (int) this.bound.getMaxY() + 1));
        this.velocity = new Point(r.nextInt(-5, 5), r.nextInt(-5, 5));
        this.acceleration = new Point(r.nextInt(-5, 5), r.nextInt(-5, 5));
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.drawLine((int) position.x, (int) position.y, (int) position.x, (int) position.y);
    }

    public void draw(Graphics2D g2d, Boid[] flock) {
//        g2d.setColor(Color.white);
//        for (Boid b : flock) {
//            if (!this.equals(b)) {
//                if (position.distanceSq(b.position) < Option.perceptionSq) {
//                    g2d.setColor(Color.red);
//                    break;
//                }
//            }
//        }
//        if (index == 0) {
//            g2d.setColor(Color.red);
//            g2d.drawOval((int) position.x - Option.alignPerception / 2, (int) position.y - Option.alignPerception / 2, Option.alignPerception, Option.alignPerception);
//            g2d.setColor(Color.green);
//            g2d.drawOval((int) position.x - Option.matchingPerception / 2, (int) position.y - Option.matchingPerception / 2, Option.matchingPerception, Option.matchingPerception);
//            g2d.setColor(Color.blue);
//            g2d.drawOval((int) position.x - Option.avoidPerception / 2, (int) position.y - Option.avoidPerception / 2, Option.avoidPerception, Option.avoidPerception);
//        }
        g2d.drawLine((int) position.x, (int) position.y, (int) position.x, (int) position.y);
        g2d.drawLine((int) position.x, (int) position.y, (int) (position.x + velocity.x * 2), (int) (position.y + velocity.y * 2));
    }

    public void update(Boid[] boids) {
        align(boids);
        avoid(boids);
        matchVelocity(boids);
//        position.wrap(bound);
        position.add(velocity);
        velocity.cap(Option.maxVelocity);
        avoidEdge();
    }

    public void avoidEdge() {
        int margin = 50;
        double turnFactor = 10;

        if (position.x < bound.x + margin) {
            velocity.x += turnFactor;
        }
        if (position.x > bound.getMaxX() - margin) {
            velocity.x -= turnFactor;
        }
        if (position.y < bound.y + margin) {
            velocity.y += turnFactor;
        }
        if (position.y > bound.getMaxY() - margin) {
            velocity.y -= turnFactor;
        }
    }

    public void align(Boid[] flock) {
        double alignFactor = Option.alignFactor / 100; // adjust velocity by this %
        Point centerPoint = new Point(0, 0);
        int neighborsCount = 0;
        for (Boid b : flock) {
            if (position.distanceSq(b.position) < Option.alignPerceptionSq) {
                centerPoint.add(b.position);
                neighborsCount++;
            }
        }
        if (neighborsCount > 0) {
            centerPoint.div(neighborsCount);
            velocity.add(Point.mul(Point.sub(centerPoint, position), alignFactor));
        }
    }

    public void avoid(Boid[] flock) {
        double avoidFactor = Option.avoidFactor / 100;
        Point move = new Point(0, 0);
        for (Boid b : flock) {
            if (!this.equals(b)) {
                if (this.position.distanceSq(b.position) < Option.avoidPerceptionSq) {
                    move.add(Point.sub(position, b.position));
                }
            }
        }
        this.velocity.add(Point.mul(move, avoidFactor));
    }

    public void matchVelocity(Boid[] flock) {
        double matchingFactor = Option.matchingFactor / 100;
        Point avg = new Point(0, 0);
        int neightborCount = 0;
        for (Boid b : flock) {
            if (position.distanceSq(b.position) < Option.matchingPerceptionSq) {
                avg.add(b.velocity);
                neightborCount++;
            }
        }
        if (neightborCount > 0) {
            avg.div(neightborCount);
            velocity.add(Point.mul(Point.sub(avg, velocity), matchingFactor));
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.index;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Boid other = (Boid) obj;
        return this.index == other.index;
    }

}
