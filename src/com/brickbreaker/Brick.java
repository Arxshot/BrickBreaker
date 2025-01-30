package com.brickbreaker;

import com.brickbreaker.PowerUps.PowerUp;

import java.awt.*;
import java.util.Comparator;
import java.util.Vector;

// Brick is the thing that gets hit and destroyed by balls.
public class Brick extends Entity implements Updatable, Collider, OutLine {
    private final Polygon polygon;
    private final Vec[] points;
    private Rect bounds;
    private final BrickInterface[] brickInterfaces;
    private int brickNum;

    // Brick sorter which orders the bricks by how many hits they have until they break.
    public static final Comparator<Brick> hitsTillBreak_SORT = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            int a = ((Brick) o1).getHitsTillBreak();
            int b = ((Brick) o2).getHitsTillBreak();
            if (a < b) {
                return 1;
            } else if (a == b) {
                return 0;
            }
            return -1;
        }
    };

    // Constructor
    public Brick(Polygon polygon, BrickInterface brickInterface) {
        this(polygon, new BrickInterface[]{brickInterface});
    }

    // Constructor
    public Brick(Polygon polygon, BrickInterface[] brickInterfaces) {
        this.polygon = polygon;
        this.points = new Vec[polygon.npoints];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Vec(polygon.xpoints[i], polygon.ypoints[i]);
        }
        this.brickInterfaces = brickInterfaces;
    }

    // Constructor
    public Brick(Vec[] points, BrickInterface[] brickInterfaces) {
        this.points = points;
        int[] x = new int[points.length];
        int[] y = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            x[i] = (int) points[i].x;
            y[i] = (int) points[i].y;
        }
        polygon = new Polygon(x, y, points.length);
        this.brickInterfaces = brickInterfaces;
    }

    @Override
    public void update(double dt) {
        brickInterfaces[brickNum].update(dt, polygon);
    }

    private static int numDestroyed = 0;

    // Called by a ball when a brick is hit, which may increase the number of power ups
    public void onHit(Vector<PowerUp> powerUp) {
        if (brickNum < brickInterfaces.length) {
            if (brickInterfaces[brickNum] != null) {
                brickInterfaces[brickNum].onHit(polygon, powerUp);
                brickNum++;
            }
        }

        if (isDestroyed()) {
            numDestroyed++;
        }
    }

    @Override
    public void updateBounds() {
        bounds = new Rect(polygon.getBounds());
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    public int getBrickNum() {
        return brickNum;
    }

    public void setBrickNum(int brickNum) {
        if (brickNum < 0) {
            this.brickNum = 0;
        } else {
            this.brickNum = brickNum;
        }
    }

    @Override
    public void OnCollison(Collider collider) {
    }

    // Returns the outline for the brick
    private Line[] getLines() {
        Line[] lines = new Line[polygon.npoints - 1];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line(points[i], points[i + 1]);
        }
        return lines;
    }

    @Override
    public void paint(Graphics2D g) {
        if (!isDestroyed()) {
            brickInterfaces[brickNum].paint(g, polygon);
        }
    }

    public boolean isDestroyed() {
        return brickInterfaces.length <= brickNum;
    }

    // Returns the outline for the brick
    @Override
    public Line[] getOutLine() {
        Line[] lines = new Line[polygon.npoints];
        int i = 0;
        for (; i < lines.length - 1; i++) {
            lines[i] = new Line(new Vec(polygon.xpoints[i], polygon.ypoints[i]), new Vec(polygon.xpoints[i + 1],
                    polygon.ypoints[i + 1]));
        }
        lines[i] = new Line(new Vec(polygon.xpoints[i], polygon.ypoints[i]), new Vec(polygon.xpoints[0],
                polygon.ypoints[0]));
        return lines;
    }

    public int getHitsTillBreak() {
        return brickInterfaces.length - brickNum;
    }
}
