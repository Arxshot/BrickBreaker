package com.brickbreaker;

// Ray - very useful. It is for reflections of the ball off all the bricks, paddles, and walls.
public class Ray {
    private Line ray;

    // Constructor
    public Ray(Line ray) {
        this.ray = ray;
    }

    /// /////////////////////////////
    /// //////// Methods ////////////
    /// /////////////////////////////
    // Casts the ray and sees if it collides with the wall, if it does it
    // will return the point of collision
    // or null if it does not collide
    // Casting algorithm is from
    // https://www.youtube.com/watch?v=TOEi6T2mtHo
    public Vec cast(Line wall) {
        final double x1 = wall.getP1().x;
        final double y1 = wall.getP1().y;
        final double x2 = wall.getP2().x;
        final double y2 = wall.getP2().y;
        final double x3 = ray.getP1().x;
        final double y3 = ray.getP1().y;
        final double x4 = ray.getP2().x;
        final double y4 = ray.getP2().y;
        final double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den == 0) {
            return null;
        }
        final double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den; // wall
        final double u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / den; // ray
        if ((0 <= t && t <= 1) && (0 <= u && u <= 1)) {
            return new Vec(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        }
        return null;
    }

    // Casts the ray against multiple walls and sees if it collides with a wall,
    // if it does it will return
    // the point of the closest collision or null if it does not collide
    public Vec cast(Line[] walls) {
        Vec closest = null;
        double length = ray.length() * 2;
        for (int i = 0; i < walls.length; i++) {
            Vec temp = cast(walls[i]);
            if (temp == null) {
                continue;
            }
            double tempLength = ray.getP1().distance(temp);
            if (length > tempLength) {
                length = tempLength;
                closest = temp;
            }
        }
        return closest;
    }

    // It will return the index of the line that got intercepted
    public int castIndex(Line[] walls) {
        int index = -1;
        double length = ray.length() * 2;
        for (int i = 0; i < walls.length; i++) {
            Vec temp = cast(walls[i]);
            if (temp == null) {
                continue;
            }
            double tempLength = ray.getP1().distance(temp);
            if (length > tempLength) {
                length = tempLength;
                index = i;
            }
        }
        return index;
    }

    // Gives an array of points with the first point being the origin of the ray, the
    // second is the point
    // of intersection and the third point is where the ray would bounce off to.
    public Vec[] reflection(Line wall) {
        Vec origin = ray.getP1().clone();
        Vec intersection;
        //////////////////////////////////////
        final double x1 = wall.getP1().x;
        final double y1 = wall.getP1().y;
        final double x2 = wall.getP2().x;
        final double y2 = wall.getP2().y;
        final double x3 = ray.getP1().x;
        final double y3 = ray.getP1().y;
        final double x4 = ray.getP2().x;
        final double y4 = ray.getP2().y;
        final double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den == 0) {
            return new Vec[]{origin, null, null};
            //, ray.getP2(), Vec.add(origin,ray.getP2())};
        }
        final double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den; // wall
        final double u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / den; // ray
        if ((0 <= t && t <= 1) && (0 <= u && u <= 1)) {
            intersection = new Vec(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        } else {
            return new Vec[]{origin, null, null};
            //ray.getP2(), Vec.add(origin,ray.getP2())};
        }
        ///////////////////////
        // the formula -
        // Rr = 2 N (R1 . N) - R1
        // is from http://paulbourke.net/geometry/reflected/
        Vec R1 = ray.getP1().clone();
        R1.subtract(intersection).normalize();
        //Vec Ri = R1.clone().multiply(-1);
        Vec N = wall.getNormal();
        double prep = 2.0 * Vec.dotProduct(R1, N);
        Vec Rr = N.multiply(prep).subtract(R1);
        double dis = Math.sqrt(Math.pow(ray.getX2() - intersection.x, 2) + Math.pow(ray.getY2() - intersection.y, 2));
        Vec end = Rr.multiply(dis).add(intersection);
        return new Vec[]{origin, intersection, end};
    }

    // Gives an array of points with the first point being the origin of the ray,
    // the second is the point of intersection and the third point is
    // where the ray would bounce off to. Similar to the previous,
    // but returns the closest from a bunch of lines.
    public Vec[] reflection(Line[] walls) {
        Vec origin = ray.getP1().clone();
        Vec intersection;
        //////////////////////////////////////
        int index = 0;
        double length = ray.length() * 2;
        for (int i = 0; i < walls.length; i++) {
            Vec temp = cast(walls[i]);
            double tempLength = temp.length();
            if (length > tempLength) {
                length = tempLength;
                index = i;
            }
        }
        ///////////////////////
        return reflection(walls[index]);
    }

    /// /////////////////////////////
    /// ////////// Set //////////////
    /// /////////////////////////////
    public void setRay(Line ray) {
        this.ray = ray;
    }

    /// /////////////////////////////
    /// ////////// Get //////////////
    /// /////////////////////////////
    public Line getLine() {
        return this.ray;
    }
}
