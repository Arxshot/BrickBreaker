package com.brickbreaker;

// Not used - unproductive and unhelpful after attempted use
public class PhysicsEntity extends Entity implements Collider {
    public PhysicsEntity() {
    }

    @Override
    public void update(double dt) {
        super.update(dt);
    }

    @Override
    public void updateBounds() {
    }

    @Override
    public Rect getBounds() {
        return null;
    }

    @Override
    public void OnCollison(Collider collider) {
    }
}
