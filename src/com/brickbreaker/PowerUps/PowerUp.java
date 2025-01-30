package com.brickbreaker.PowerUps;

import com.brickbreaker.OutLine;
import com.brickbreaker.Updatable;
import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// Provides the base class of the power up features for the balls that drop
// from the bricks. There are multiple implementations of PowerUp.
// Ball_2 - it triples the balls, if there was one there will be 3 after
// Brick_Hit1 - breaks all the bricks by 1
// Brick_Unhit1 - adds a piece back to all the bricks
// Pladle_Fast - makes the paddle faster for 90 seconds
// Pladle_Slow - makes the paddle slower for 90 seconds
// Pladle_NoFriction - deaccelerates the paddle by 1000 for 90 seconds
// Pladle_OnIce - deaccelerates the paddle by 10 for 90 seconds
public abstract class PowerUp implements Paintable
        , Collider {
    protected double speed = 5;
    protected Ray ray = null;
    private Rect bounds = null;
    private final double r = 5;
    protected Polygon polygon;

    // Constructor
    public PowerUp(Line line) {
// Creates the direction and the collider for the powerup
        ray = new Ray(line);
// Creates the shape for the powerup for rendering
        polygon = new Polygon(
                new int[]{(int) (-1 * r), (int) (-0.5 * r), (int) (0.5 * r), (int) (1 * r), (int) (1
                        * r), (int) (0.5 * r), (int) (-0.5 * r), (int) (-1
                        * r)},
                new int[]{(int) (0.5 * r), (int) (1
                        * r), (int) (1 * r), (int) (0.5 * r), (int) (-0.5
                        * r), (int) (-1 * r), (int) (-1 * r), (int) (-0.5
                        * r)},
                8);
    }

    public Ray getRay() {
        return ray;
    }

    @Override
    public void updateBounds() {
        double x = ray.getLine().getX1();
        double y = ray.getLine().getY1();
        double w = ray.getLine().getWidth();
        double h = ray.getLine().getHeight();
        bounds = new Rect(x, y, w, h);
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public void OnCollison(Collider collider) {
    }

    // Called when it is hit and removes the power up
    public void onHit(Object Collider, Vector<
            Paddle> paddles, Vector<Brick> brick, Vector<Ball
            > balls, Vector<PowerUp> powerUps, AtomicInteger
                              score, Vector<Paintable> paintable, Vector<
            Updatable> updateAbles, Vector<Collider>
                              movingColliders) {
        powerUps.remove(this);
    }

    // Moves and checks for collisions of this power up
    public void update(double dt, Vector<Paddle>
                               paddles, Vector<PowerUp> powerUps, Vector<Ball>
                               balls, Vector<Brick> bricks, int
                               startDestroyedBrickIndex, AtomicInteger score,
                       Vector<Paintable> paintable, Vector<Updatable>
                               updateAbles, Vector<Collider> movingColliders) {
// Moves by distance = delta time * speed
        double multi = speed * dt;
        Vec dir = Vec.subtract(ray.getLine().getP2
                (), ray.getLine().getP1()).normalize();
        dir.multiply(multi);
        ray.getLine().setP2(Vec.add(ray.getLine().
                getP1(), dir));
// Recalculates the boundaries
        updateBounds();
// Checks to see if it intersected with a paddle
        Vector<Paddle> possiblePaddles = new
                Vector<>(1); // bricks that could be hit
        for (int i = 0; i < paddles.size(); i++) {
            if (getBounds().Collision(paddles.get(
                    i).getBounds())) {
                possiblePaddles.add(paddles.get(i
                ));
            }
        }
// Checks to see if it intersected with any bricks
        Vector<Brick> possibleBricks = new Vector
                <>(bricks.size() / 6); // bricks that could be hit
        for (int i = 0; i <
                startDestroyedBrickIndex; i++) {
            if (getBounds().Collision(bricks.get(i
            ).getBounds())) {
                possibleBricks.add(bricks.get(i));
            }
        }
        OutLine outLine = null; // the outline with closest collision
        double dis = Double.MAX_VALUE; // distance from start of ray to the line of intersect
////////////////////////////
///// Get closest Line /////
////////////////////////////
        for (Paddle paddle : possiblePaddles) {
// closet brick - closet line
            Line[] lines = paddle.getOutLine();
            for (int i = 0; i < lines.length; i++) {
                Vec temp = ray.cast(lines[i]);
                if (temp == null) {
                    continue;
                }
                double tempLength = temp.length();
                if (dis > tempLength) {
                    outLine = paddle;
                    dis = tempLength;
                }
            }
        }
        for (Brick brick : possibleBricks) { // closet brick - closet line
            Line[] lines = brick.getOutLine();
            for (int i = 0; i < lines.length; i
                    ++) {
                Vec temp = ray.cast(lines[i]);
                if (temp == null) {
                    continue;
                }
                double tempLength = temp.length();
                if (dis > tempLength) {
                    outLine = brick;
                    dis = tempLength;
                }
            }
        }
        Vec v = new Vec(polygon.xpoints[0] + r,
                polygon.ypoints[0] - (0.5 * r));
        Vec def = Vec.subtract(ray.getLine().getP1(), v);
        polygon.translate((int) def.getX(), (int)
                def.getY());
/////////////////////////////
//// Collision Handling ////
/////////////////////////////
        if (outLine != null) { // reflection move
            onHit(outLine, paddles, bricks, balls
                    , powerUps, score, paintable, updateAbles,
                    movingColliders);
        } // normal move
        dir = Vec.subtract(ray.getLine().getP2(),
                ray.getLine().getP1()).normalize();
        ray.getLine().getP1().set(ray.getLine().
                getP2());
        ray.getLine().getP2().set(Vec.add(ray.
                getLine().getP1(), dir.multiply(speed * dt)));
    }

    @Override
    public void paint(Graphics2D g) {
        Global.fillPoylgon(g, polygon, Color.red);
    }
}
