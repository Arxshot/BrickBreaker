package com.brickbreaker;

// Collider is a mix between bounds and onCollision. It allows bounds to determine if they
// intersect and if so invoke the OnCollision().
public interface Collider extends Bounds {
    public void OnCollison(Collider collider);
}
