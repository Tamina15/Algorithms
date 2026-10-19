package com.tmn.flockingsimulation;

import com.tmn.graphic.NPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MainPanel extends NPanel {

    public MainPanel(int width, int height) {
        super(width, height);
        bound = new Rectangle(0, 0, width * 5, height * 5);
        boids = new Boid[400];
        for (int i = 0; i < boids.length; i++) {
            boids[i] = new Boid(i, bound);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for (Boid boid : boids) {
            boid.draw(g2d, boids);
        }
        g2d.drawRect((int) bound.x, (int) bound.y, (int) bound.getMaxX(), (int) bound.getMaxY());
    }

    Boid[] boids;
    Rectangle bound;

    @Override
    public void update() {
        for (Boid b : boids) {
            b.update(boids);
        }
    }
}
