package com.brickbreaker.PowerUps;

import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// Ball_Slow - halfs ball speed for 90 seconds
public class Ball_Slow extends PowerUp {
    // Constructor
    public Ball_Slow(Line line) {
        super(line);
    }

    @Override
    public void onHit(Object Collider, Vector<
            Paddle> paddles, Vector<Brick> brick, Vector<Ball>
                              balls, Vector<PowerUp> powerUps, AtomicInteger
                              score, Vector<Paintable> paintable, Vector<
            Updatable> updateAbles, Vector<Collider>
                              movingColliders) {
        if (Collider instanceof Paddle) {
            super.onHit(Collider, paddles, brick,
                    balls, powerUps, score, paintable, updateAbles,
                    movingColliders);
            Vector<Ball> perBalls = (Vector<Ball>)
                    balls.clone();
            for (int i = 0; i < perBalls.size(); i++) {
                perBalls.get(i).setSpeed(perBalls.
                        get(i).getSpeed() / 2);
            }
            updateAbles.add(new Updatable() {
                double finaleTime = 90;
                double currentTime = 0;

                @Override
                public void update(double dt) {
                    currentTime += dt;
                    if (finaleTime <= currentTime
                    ) {
                        updateAbles.remove(this);
                        for (int i = 0; i <
                                perBalls.size(); i++) {
                            perBalls.get(i).
                                    setSpeed(perBalls.get(i).getSpeed() * 2);
                        }
                    }
                }
            });
        }
    }

    public void paint(Graphics2D g) {
        Global.fillPoylgon(g, polygon, new Color(25
                , 25, 200));
    }
}
