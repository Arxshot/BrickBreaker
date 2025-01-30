package com.brickbreaker.PowerUps;

import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// seconds Paddle_Fast - makes the paddle faster for 90
public class Paddle_Fast extends PowerUp {
    // Constructor
    public Paddle_Fast(Line line) {
        super(line);
    }

    // Called when it is hit, and ensures that it collided with a paddle
    @Override
    public void onHit(Object Collider, Vector<Paddle> paddles, Vector<Brick> brick, Vector<Ball>
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
                paddles.get(i).setAccelerationX(paddles.get(i).getAccelerationX() * 2);
                paddles.get(i).setMaxSpeedX(paddles.get(i).getMaxSpeedX() * 2);
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
                        for (int i = 0; i < paddles.size(); i++) {
                            paddles.get(i).setAccelerationX(paddles.get(i).getAccelerationX() / 2);
                            paddles.get(i).setMaxSpeedX(paddles.get(i).getMaxSpeedX() / 2);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void paint(Graphics2D g) {
        Global.fillPoylgon(g, polygon, new Color(
                225, 225, 40));
    }
}
