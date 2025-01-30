package com.brickbreaker.PowerUps;

import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// Paddle_Slow - makes the paddle slower for 90 seconds
public class Paddle_Slow extends PowerUp { // halfs the Padles speed for 90 seconds
    // Constructor
    public Paddle_Slow(Line line) {
        super(line);
    }

    // Called when it is hit, and ensures that it collided with a paddle
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
            for (int i = 0; i < paddles.size(); i
                    ++) {
                paddles.get(i).setAccelerationX(
                        paddles.get(i).getAccelerationX() / 2);
                paddles.get(i).setMaxSpeedX(paddles
                        .get(i).getMaxSpeedX() / 2);
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
                                    setAccelerationX(paddles.get(i).getAccelerationX
                                            () * 2);
                            paddles.get(i).
                                    setMaxSpeedX(paddles.get(i).getMaxSpeedX() * 2);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void paint(Graphics2D g) {
        Global.fillPoylgon(g, polygon, new Color(50
                , 150, 15));
    }
}
