package com.brickbreaker;

import java.awt.*;

// Rectangle shape
public class Rect implements Paintable, Bounds {
    public double x, y, width, height;

    // Constructor
    public Rect() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    // Constructor
    public Rect(Rectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    // Constructor
    public Rect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /// /////////////////////////////
    /// ////////// Set //////////////
    /// /////////////////////////////
    public void setRect(Rect rect) {
        x = rect.x;
        y = rect.y;
        width = rect.width;
        height = rect.height;
    }

    public Rect setX(double x) {
        this.x = x;
        return this;
    }

    public Rect setY(double y) {
        this.y = y;
        return this;
    }

    public Rect setWidth(double width) {
        this.width = width;
        return this;
    }

    public Rect setHeight(double height) {
        this.height = height;
        return this;
    }

    /// /////////////////////////////
    /// //////// Methods ////////////
    /// /////////////////////////////
    // Keep the rectangle size, but shift to ensure that the width and height are both positive
    public Rect removeNeg() {
        if (width < 0) {
            x += width;
            width = Math.abs(width);
        }
        if (height < 0) {
            y += height;
            height = Math.abs(height);
        }
        return this;
    }

    // Check if a point is inside the rectangle
    public boolean Collision(Vec point) {
        if (x < point.x && point.x < x + width && y < point.y && point.y < y + height) {
            return true;
        }
        return false;
    }

    // Check if a rectangle is inside another rectangle
    public boolean Collision(Rect rect) { // take code from Rectangle class for collisions
        if (this.x < rect.x + rect.width && this.x + this.width > rect.x && this.y < rect.y + rect.height && this.y + this.height > rect.y) {
            return true;
        }
        return false;
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawRect((int) x, (int) y, (int) width, (int) height);
    }

    private Rect bonds;

    @Override
    public void updateBounds() {
        bonds = new Rect(x, y, width, height);
    }

    @Override
    public Rect getBounds() {
        return new Rect(x, y, width, height);
    }
}
