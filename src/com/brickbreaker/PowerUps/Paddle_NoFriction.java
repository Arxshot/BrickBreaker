package com.brickbreaker.PowerUps;

import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// Paddle_NoFriction - deaccelerates the paddle by 1000 for 90 seconds
public class Paddle_NoFriction extends PowerUp {
    // Constructor
    public Paddle_NoFriction(Line line) {
        super(line);
    }

    // Called when it is hit, and ensures that it collided with a paddle
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
            for (int i = 0; i < paddles.size(); i
                    ++) {
                paddles.get(i).setDeccelerationX(
                        paddles.get(i).getDeccelerationX() / 1000);
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
                        for (int i = 0; i < paddles
                                .size(); i++) {
                            paddles.get(i).
                                    setDeccelerationX(paddles.get(i).getDeccelerationX
                                            () * 1000);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void paint(Graphics2D g) {
        Global.fillPoylgon(g, polygon, new Color(
                100, 100, 255));
    }
}
