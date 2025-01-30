package com.brickbreaker;

import java.awt.*;

// An inanimate object that does nothing when hit besides giving a
// surface to bounce off of.
public class Wall extends PhysicsEntity implements OutLine {
    private Rect bounds;
    private Line wall;

    // Constructor
    public Wall(Line wall) {
        super();
        this.wall = wall;
    }

    @Override
    public void updateBounds() {
        bounds = new Rect(wall.getX1(), wall.getY1(), wall.getWidth(), wall.getHeight()).removeNeg();
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public void OnCollison(Collider collider) {
    }

    public Line getLine() {
        return wall;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(Color.BLACK);
        wall.paint(g);
    }

    @Override
    public Line[] getOutLine() {
        return new Line[]{wall};
    }
}
