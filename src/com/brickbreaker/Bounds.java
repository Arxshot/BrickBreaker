package com.brickbreaker;

// The rectangle around a shape, used for collisions.
public interface Bounds {
    public void updateBounds();

    public Rect getBounds();
}
