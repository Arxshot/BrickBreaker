package com.brickbreaker;

import com.brickbreaker.PowerUps.*;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// A ball which bounces around the screen, breaking bricks and bouncing off walls and
// the paddle. There can be multiple balls at play. It provides the points for the game.
public class Ball extends PhysicsEntity implements Cloneable {
    private Circle circle;
    private Ray ray;
    private Line line;
    private double speed = 15;
    private Rect bounds;
    private double r;
    private Polygon polygon;
    private static int lastPlayedPop = 0;
    private static final Clip[] popSounds = new Clip[10];

    // Plays the next pop sound available, or restarts and already running one.
    public void playNextPop() {
        Clip clip = popSounds[lastPlayedPop++ % popSounds.length];
        clip.setMicrosecondPosition(250);
        clip.start();
    }

    // Loads the pop sound clips from a file.
    public Clip createPopSound() {
        try {
            URL url = new URL(new URL("file:"), "./Sounds/PopSound1.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
            return clip;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Constructor which takes a position, radius
    // and a direction of travel
    public Ball(Vec pos, double r, Vec dir) {
        this.r = r;
        this.circle = new Circle(pos, r);
        this.line = new Line(pos, Vec.add(pos, dir.normalize()));
        this.ray = new Ray(line);
        polygon = new Polygon(new int[]{(int) (-1 * r), (int) (-0.5 * r), (int) (0.5 * r), (int) (1 * r),
                (int) (1 * r), (int) (0.5 * r), (int) (-0.5 * r), (int) (-1 * r)}, new int[]{(int) (0.5 * r),
                (int) (1 * r), (int) (1 * r), (int) (0.5 * r), (int) (-0.5 * r), (int) (-1 * r), (int) (-1 * r),
                (int) (-0.5 * r)}, 8);

        // Load all the pop sounds
        for (int i = 0; i < popSounds.length; i++) {
            popSounds[i] = createPopSound();
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void updateBounds() {
        double length = line.length();
        double x = line.getX1() - length;
        double y = line.getY1() - length;
        double w = length * 2;
        double h = length * 2;
        bounds = new Rect(x, y, w, h);
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    // Repositions the ball based on the the time
    // delta and reacts to collisions with the walls
    // and paddles.
    public void update(double dt, Vector<Paddle> paddles, Vector<Wall> walls, Vector<Brick> bricks,
                       int startDestroyedBrickIndex, Vector<PowerUp> powerUps, AtomicInteger score) {
        double multi = speed * dt;
        Vec dir = Vec.subtract(line.getP2(), line.getP1()).normalize();
        dir.multiply(multi);
        line.setP2(Vec.add(line.getP1(), dir));
        updateBounds();

        Vector<Paddle> possiblePaddles = new Vector<>(1); // bricks that could be hit
        for (int i = 0; i < paddles.size(); i++) {
            if (getBounds().Collision(paddles.get(i).getBounds())) {
                possiblePaddles.add(paddles.get(i));
            }
        }

        Vector<Wall> possibleWalls = new Vector<>(walls.size() / 3); // walls that could be hit
        for (int i = 0; i < walls.size(); i++) {
            if (getBounds().Collision(walls.get(i).getBounds())) {
                possibleWalls.add(walls.get(i));
            }
        }

        Vector<Brick> possibleBricks = new Vector<>(bricks.size() / 6); // bricks that could be hit
        for (int i = 0; i < startDestroyedBrickIndex; i++) {
            if (getBounds().Collision(bricks.get(i).getBounds())) {
                possibleBricks.add(bricks.get(i));
            }
        }

        OutLine lastReflectionOutLine = null; // so the ray dosn't bounce off the same line twice
        int lastReflectionOutLineIndex = 0;

        while (true) { // repeats until no more reflections
            OutLine outLine = null; // the outline with closest collision
            int lineIndex = 0; // the outline with closest collision
            double dis = Double.MAX_VALUE; // distance from start of ray to the line of intersect

            ////////////////////////////
            ///// Get closest Line /////
            ////////////////////////////
            for (Paddle paddle : possiblePaddles) { // closet brick - closet line
                if (paddle == lastReflectionOutLine) {
                    Line[] lines = paddle.getOutLine();
                    for (int i = 0; i < lines.length; i++) {
                        if (i == lastReflectionOutLineIndex) {
                            continue;
                        }
                        Vec temp = getRay().cast(lines[i]);
                        if (temp == null) {
                            continue;
                        }
                        double tempLength = temp.length();
                        if (dis > tempLength) {
                            outLine = paddle;
                            lineIndex = i;
                            dis = tempLength;
                        }
                    }
                } else {
                    Line[] lines = paddle.getOutLine();
                    for (int i = 0; i < lines.length; i++) {
                        Vec temp = getRay().cast(lines[i]);
                        if (temp == null) {
                            continue;
                        }
                        double tempLength = temp.length();
                        if (dis > tempLength) {
                            outLine = paddle;
                            lineIndex = i;
                            dis = tempLength;
                        }
                    }
                }
            }

            for (Brick brick : possibleBricks) {
                // closet brick - closet line
                if (brick == lastReflectionOutLine) {
                    Line[] lines = brick.getOutLine();
                    for (int i = 0; i < lines.length; i++) {
                        if (i == lastReflectionOutLineIndex) {
                            continue;
                        }
                        Vec temp = getRay().cast(lines[i]);
                        if (temp == null) {
                            continue;
                        }
                        double tempLength = temp.length();
                        if (dis > tempLength) {
                            outLine = brick;
                            lineIndex = i;
                            dis = tempLength;
                        }
                    }
                } else {
                    Line[] lines = brick.getOutLine();

                    for (int i = 0; i < lines.length; i++) {
                        Vec temp = getRay().cast(lines[i]);
                        if (temp == null) {
                            continue;
                        }
                        double tempLength = temp.length();
                        if (dis > tempLength) {
                            outLine = brick;
                            lineIndex = i;
                            dis = tempLength;
                        }
                    }
                }
            }

            for (Wall wall : possibleWalls) { // closet brick - closet line
                if (wall == lastReflectionOutLine) {
                    Line[] lines = wall.getOutLine();
                    for (int i = 0; i < lines.length; i++) {
                        if (i == lastReflectionOutLineIndex) {
                            continue;
                        }
                        Vec temp = getRay().cast(lines[i]);
                        if (temp == null) {
                            continue;
                        }
                        double tempLength = temp.length();
                        if (dis > tempLength) {
                            outLine = wall;
                            lineIndex = i;
                            dis = tempLength;
                        }
                    }
                } else {
                    Line[] lines = wall.getOutLine();

                    for (int i = 0; i < lines.length; i++) {
                        Vec temp = getRay().cast(lines[i]);
                        if (temp == null) {
                            continue;
                        }
                        double tempLength = temp.length();
                        if (dis > tempLength) {
                            outLine = wall;
                            lineIndex = i;
                            dis = tempLength;
                        }
                    }
                }
            }

            /////////////////////////////
            //// Reflection Handling ////
            //// && ////
            //// Collision Handling ////
            /////////////////////////////
            boolean collision = false;
            if (outLine != null) { // reflection move
                collision = true;
                Vec[] vecs = getRay().reflection(outLine.getOutLine()[lineIndex]);
                line.getP1().set(vecs[1]);
                double length = line.length();
                dir = vecs[2].clone().subtract(vecs[1]).normalize();
                dir.multiply(length);
                dir.add(Vec.randomDirections().multiply(0.1 / speed));
                line.setP2(Vec.add(line.getP1(), dir));
                if (outLine instanceof Brick) {
                    ((Brick) outLine).onHit(powerUps);

                    if (((Brick) outLine).isDestroyed()) {
                        possibleBricks.remove(((Brick) outLine));
                        startDestroyedBrickIndex -= 1;
                    }
                    score.set(score.get() + 10);
                }
                if (outLine instanceof Paddle) {
                    //((Pladle) outLine).onHit();
                }
            } else { // normale move
                dir = Vec.subtract(line.getP2(), line.getP1()).normalize();
                line.getP1().set(line.getP2());
                line.getP2().set(Vec.add(line.getP1(), dir.multiply(speed * dt)));
                break;
            }
            lastReflectionOutLine = outLine; // so the ray dosn't bounce off the same line twice
            lastReflectionOutLineIndex = lineIndex;
            if (collision) {
                playNextPop();
            }
        }

        Vec v = new Vec(polygon.xpoints[0] + r, polygon.ypoints[0] - (0.5 * r));
        Vec def = Vec.subtract(ray.getLine().getP1(), v);

        polygon.translate((int) def.x, (int) def.y);
        //System.out.println("x: " + line.getP1().x + "\t,y: " + line.getP1().y);
    }

    public Ray getRay() {
        return ray;
    }


    public void setDirection(Vec direction) {
        Line l = ray.getLine();
        l.getP2().set(Vec.add(l.getP1(), direction));
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        Global.fillPoylgon(g, polygon, new Color(100, 100, 100));
        //bounds.paint(g);
    }

    // Used by the PowerUp Ball doubler feature
    public Ball clone() {
        Line line = this.line.clone();
        Ball clone = new Ball(line.getP1(), circle.r, line.getDirection());
        clone.speed = speed;
        clone.paintLayer = paintLayer;
        return clone;
    }
}

