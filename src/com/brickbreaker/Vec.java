package com.brickbreaker;

// Holds a position of something in doubles.
public class Vec implements Cloneable {
    double x, y;

    // Constructor
    public Vec() {
        this.x = 0;
        this.y = 0;
    }

    // Constructor
    public Vec(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    // Constructor
    public Vec(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(Vec vec) {
        setX(vec.getX());
        setY(vec.getY());
    }

    public void moveX(double x) {
        this.x += x;
    }

    public void moveY(double y) {
        this.y += y;
    }

    public Vec move(double x, double y) {
        moveX(x);
        moveY(y);
        return this;
    }

    public Vec move(Vec p) {
        moveX(p.x);
        moveY(p.y);
        return this;
    }

    public Vec multiply(double m) {
        x *= m;
        y *= m;
        return this;
    }

    // Rotates the point around the origin
    public Vec rotate(double radian) {
        double preX = x;
        double preY = y;
        x = preX * Math.cos(radian) - preY * Math.sin(radian);
        y = preX * Math.sin(radian) + preY * Math.cos(radian);
        return this;
    }

    // Rotates the point around the point p
    public Vec rotateAround(Vec p, double radian) {
        move(-p.x, -p.y);
        double preX = x;
        double preY = y;
        x = preX * Math.cos(radian) - preY * Math.sin(radian);
        y = preX * Math.sin(radian) + preY * Math.cos(radian);
        move(p.x, p.y);
        return this;
    }

    public Vec add(Vec p) {
        x += p.x;
        y += p.y;
        return this;
    }

    public Vec subtract(Vec p) {
        x -= p.x;
        y -= p.y;
        return this;
    }

    public static Vec add(Vec a, Vec b) {
        return new Vec(a.x + b.x, a.y + b.y);
    }

    public static Vec subtract(Vec a, Vec b) {
        return new Vec(a.x - b.x, a.y - b.y);
    }


    public static double dotProduct(Vec a, Vec b) {
        return a.x * b.x + a.y * b.y;
    }
    // Finds the unit length relative to the origin

    public Vec normalize() {
        //double den = Math.sqrt(x*x + y*y);
        double den = MMath.invSqrt(x * x + y * y);
        if (den == 0) {
            System.err.println("Normal denominator is zero");
            assert (false);
            return new Vec(0, 0);
        }
        x *= den;
        y *= den;
        return this;
    }

    // Length from origin
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    // Distance from another point
    public double distance(Vec point) {
        double x = point.x - this.x;
        double y = point.y - this.y;
        return Math.sqrt(x * x + y * y);
    }

    // Returns a random direction, or vector of unit length one

    public static Vec randomDirections() {
        double x = Math.random();
        double y = Math.sqrt(1 - (x * x));
        switch ((int) (Math.random() * 4)) {
            case 0:
                return new Vec(x, y);
            case 1:
                return new Vec(-x, y);
            case 2:
                return new Vec(x, -y);
            case 3:
                return new Vec(-x, -y);
        }
        return null;
    }

    public Vec clone() {
        return new Vec((double) x, (double) y);
    }
}
