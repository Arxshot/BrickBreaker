package com.brickbreaker;

// Balls are of a circle shape
public class Circle implements Bounds {
    public Vec p;
    public double r;
    private Rect bounds;

    // Constructor
    public Circle(Vec p, Double r) {
        this.p = p;
        this.r = r;
    }

    @Override
    public void updateBounds() {
        bounds = new Rect(p.x - r, p.y - r, p.x + r, p.y + r).removeNeg();
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }
}
