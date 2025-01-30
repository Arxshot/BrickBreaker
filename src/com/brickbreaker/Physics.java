package com.brickbreaker;

// Not used - unproductive and unhelpful after attempted use
public class Physics {
    private Vec position;
    private Vec velocity;
    private Vec acceleration;
    private double maxVelocity = 90;//Double.MAX_VALUE;

    public Physics() {
        this(new Vec(0, 0), new Vec(0, 0), new Vec(0, 0));
    }

    public Physics(Vec pos) {
        this(pos, new Vec(0, 0), new Vec(0, 0));
    }

    public Physics(Vec position, Vec velocity, Vec acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Physics setPosition(Vec position) {
        this.position = position;
        return this;
    }

    public void setVelocity(Vec velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(Vec acceleration) {
        this.acceleration = acceleration;

    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public Vec getPosition() {
        return position;
    }

    public Vec getVelocity() {
        return velocity;
    }

    public Vec getAcceleration() {
        return acceleration;
    }

    public Physics add(Physics physics) {
        velocity.add(physics.getVelocity());
        acceleration.add(physics.getAcceleration());
        return this;
    }

    public Physics subtract(Physics physics) {
        velocity.add(physics.getVelocity());
        acceleration.add(physics.getAcceleration());
        return this;
    }

    public Physics add(Physics a, Physics b) {
        return new Physics(null, Vec.add(a.velocity, b.velocity), Vec.add(a.acceleration, b.acceleration));
    }

    public static Physics subtract(Physics a, Physics b) {
        return new Physics(null, Vec.subtract(a.velocity, b.velocity), Vec.subtract(a.acceleration, b.acceleration));
    }

    public void update(double dt) {
        velocity.add(acceleration.clone().multiply(dt));
        if (velocity.length() > maxVelocity) {
            velocity.normalize();
            velocity.multiply(maxVelocity);
        }
        position.add(velocity.clone().multiply(dt));
    }
}
