package com.brickbreaker.PowerUps;

import com.brickbreaker.Updatable;
import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// Ball_2 - it triples the balls, if there was one there will be 3 after
public class Ball_2 extends PowerUp { // adds 2 more ball per ball
    // Constructor
    public Ball_2(Line line) {
        super(line);
    }

    // Called when it is hit, and ensures that it collided with a paddle
    public void onHit(Object Collider, Vector<
                              Paddle> paddles, Vector<Brick> brick, Vector<Ball>
                              balls,
                      Vector<PowerUp> powerUps,
                      AtomicInteger score, Vector<Paintable> paintable,
                      Vector<Updatable> updateAbles
            , Vector<Collider> movingColliders) {
        if (Collider instanceof Paddle) {
            super.onHit(Collider, paddles, brick,
                    balls, powerUps, score, paintable, updateAbles,
                    movingColliders);
            Vector<Ball> perBalls = (Vector<Ball>)
                    balls.clone();
            for (Ball ball : perBalls) {
// Loop over all existing balls twice to double the number of balls
                for (int i = 0; i < 2; i++) {
                    Ball newball = ball.clone();
                    newball.getRay().getLine().
                            getP2().add(Vec.randomDirections().multiply(5 /
                                    ball.getSpeed()));
                    balls.add(newball);
                    paintable.add(newball);
                    updateAbles.add(newball);
                    movingColliders.add(newball);
                }
            }
        }
    }

    @Override
    public void paint(Graphics2D g) {
        Global.fillPoylgon(g, polygon, new Color(
                255, 100, 100));
    }
}
