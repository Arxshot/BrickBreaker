package com.brickbreaker;

import java.awt.*;

// Line is two points, used by walls and rays for collision
// detection and reflections
public class Line implements Paintable, Cloneable {
    private Vec p1;
    private Vec p2;

    // Constructor
    public Line(Vec p1, Vec p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    // Constructor
    public Line(Vec p1, double x2, double y2) {
        this.p1 = p1;
        this.p2 = new Vec(x2, y2);
    }

    // Constructor
    public Line(double x1, double y1, Vec p2) {
        this.p1 = new Vec(x1, y1);
        this.p2 = p2;
    }

    // Constructor
    public Line(double x1, double y1, double x2, double y2) {
        this.p1 = new Vec(x1, y1);
        this.p2 = new Vec(x2, y2);
    }

    /// /////////////////////////////
    /// ////////// Set //////////////
    /// /////////////////////////////
    public void setP1(Vec p1) {
        this.p1 = p1;
    }

    public void setP2(Vec p2) {
        this.p2 = p2;
    }

    public void setX1(double x1) {
        this.p1.setX(x1);
    }

    public void setY1(double y1) {
        this.p1.setY(y1);
    }

    public void setX2(double x2) {
        this.p2.setX(x2);
    }

    public void setY2(double y2) {
        this.p2.setY(y2);
    }

    public void setWidth(double width) {
        p2.setX(p1.getX() + width);
    }

    public void setHeight(double height) {
        p2.setX(-p1.getX() + height);
    }

    /// /////////////////////////////
    /// ////////// Get //////////////
    /// /////////////////////////////
    public Vec getP1() {
        return p1;
    }

    public Vec getP2() {
        return p2;
    }

    public double getX1() {
        return p1.getX();
    }

    public double getY1() {
        return p1.getY();
    }

    public double getX2() {
        return p2.getX();
    }

    public double getY2() {
        return p2.getY();
    }

    public double getWidth() {
        return p2.getX() - p1.getX();
    }

    public double getHeight() {
        return p2.getY() - p1.getY();
    }

    /// /////////////////////////////
    /// ///////// Maths /////////////
    /// /////////////////////////////
    public static Line multiply(Line a, Line b) {
        // x1 , x2
        // y1 , y2
        double x1 = a.getX1() * b.getX1() + a.getX2() * b.getY1();
        double y1 = a.getY1() * b.getX1() + a.getY2() * b.getY1();
        double x2 = a.getX1() * b.getX2() + a.getX2() * b.getY2();
        double y2 = a.getY1() * b.getX2() + a.getY2() * b.getY2();
        return new Line(x1, y1, x2, y2);
    }

    public void multiply(double m) {
        p1.x *= m;
        p1.y *= m;
        p1.x *= m;
        p2.y *= m;
    }

    public static double dotProduct(Line a, Line b) {
        return a.getWidth() * b.getWidth() + a.getHeight() * b.getHeight();
    }

    /// /////////////////////////////
    /// //////// Methods ////////////
    /// /////////////////////////////
    public void move(Vec p) {
        p1.move(p);
        p2.move(p);
    }

    public Vec getNormal() {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return new Vec(-dy, dx).normalize();
    }

    public Line rotate(double radian) {
        p1.rotate(radian);
        p2.rotate(radian);
        return this;
    }

    public Line rotateAround(Vec p, double radian) {
        p1.rotateAround(p, radian);
        p2.rotateAround(p, radian);
        return this;
    }

    public double length() {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public Vec getCenter() {
        return new Vec((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    public Line clone() {
        return new Line(p1.clone(), p2.clone());
    }

    public Vec getDirection() {
        return new Vec(getWidth(), getHeight()).normalize();
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawLine((int) getX1(), (int) getY1(), (int) getX2(), (int) getY2());
    }
}
