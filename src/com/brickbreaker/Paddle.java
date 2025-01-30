package com.brickbreaker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

// The primary paddle used to hit the balls to break the bricks.
public class Paddle extends PhysicsEntity implements KeyListener, OutLine {

    final int ACTION_STATE_STOPPED = 0;
    final int ACTION_STATE_INIT_ACCELERATE_LEFT = 1;
    final int ACTION_STATE_ACCELERATE_LEFT = 2;
    final int ACTION_STATE_INIT_DEACCELERATE_LEFT = 3;
    final int ACTION_STATE_DEACCELERATE_LEFT = 4;
    final int ACTION_STATE_INIT_ACCELERATE_RIGHT = 5;
    final int ACTION_STATE_ACCELERATE_RIGHT = 6;
    final int ACTION_STATE_INIT_DECELERATE_LEFT = 7;
    final int ACTION_STATE_DECELERATING_LEFT = 8;

    private Vec pos;
    private double width = 100;
    private double hight = 25;
    private double curve = 25; // 0 - 1 : that is the limit : 0 is line : 1 is a cirle
    private Polygon polygon;
    private Vec[] points;
    private Rect bounds;
    private double maxSpeedX = 7;
    private double accelerationX = 3;
    private double deccelerationX = 3;
    private double velocityX = 0;
    private double maxSpeedY = 0;
    private double accelerationY = 0;
    private double deccelerationY = 2;
    private double velocityY = 0;
    private final int numOfActions = 5;
    private int actionState = ACTION_STATE_STOPPED;
    private int[] actionKeys = new int[numOfActions];//move left, move right, shoot ball

    // Constructor
    public Paddle() {
        this(0, 0);
    }

    // Constructor
    public Paddle(int x, int y) {
        this(x, y, new int[]{KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Z, -1, -1});
    }

    // Constructor
    public Paddle(int x, int y, int[] actionKeys) {
        this.pos = new Vec(x, y);
        this.actionKeys = actionKeys;
        this.polygon = new Polygon(new int[]{0, (int) (width / 3), (int) (width / 3) * 2, (int) width,
                (int) (width / 3) * 2, (int) (width / 3)}, new int[]{0, (int) (-hight / 2), (int) (-hight / 2), 0,
                (int) (hight / 2), (int) (hight / 2)}, 6);
    }

    // Adjusts the maximum allowable speed
    public void setMaxSpeedX(double maxSpeedX) {
        this.maxSpeedX = maxSpeedX;
        if (actionState == ACTION_STATE_ACCELERATE_LEFT) {
            actionState = ACTION_STATE_INIT_ACCELERATE_LEFT;
        } else if (actionState == ACTION_STATE_ACCELERATE_RIGHT) {
            actionState = ACTION_STATE_INIT_ACCELERATE_RIGHT;
        }
    }

    public Vec getPos() {
        return pos;
    }

    public double getWidth() {
        return width;
    }

    public double getMaxSpeedX() {
        return maxSpeedX;
    }

    public void setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public void setDeccelerationX(double deccelerationX) {
        this.deccelerationX = deccelerationX;
    }

    public double getDeccelerationX() {
        return deccelerationX;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    boolean[] inputKeysPressed = new boolean[actionKeys.length];

    final int LEFT_KEY_INDEX = 0;
    final int RIGHT_KEY_INDEX = 1;
    final int CENTER_KEY_INDEX = 2;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

//        System.out.println("Key pressed: " + keyCode);

        for (int actionIndex = 0; actionIndex < numOfActions; actionIndex++) {
            if (actionKeys[actionIndex] == keyCode) {
                if (actionIndex == LEFT_KEY_INDEX) { // move left key press
                    // if (inputKeysPressed[LEFT_KEY_INDEX]) {
                    actionState = ACTION_STATE_INIT_ACCELERATE_LEFT;
                    //   }
                }
                if (actionIndex == RIGHT_KEY_INDEX) { // move right key press
                    //  if (inputKeysPressed[RIGHT_KEY_INDEX]) {
                    actionState = ACTION_STATE_INIT_ACCELERATE_RIGHT;
                    // }
                }
                inputKeysPressed[actionIndex] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        for (int actionIndex = 0; actionIndex < numOfActions; actionIndex++) {
            if (actionKeys[actionIndex] == keyCode) {
                if (actionIndex == LEFT_KEY_INDEX) { // move left key release
                    if (actionState == ACTION_STATE_INIT_ACCELERATE_LEFT || actionState == ACTION_STATE_ACCELERATE_LEFT) {
                        if (inputKeysPressed[1]) {
                            actionState = ACTION_STATE_INIT_ACCELERATE_RIGHT;
                        } else {
                            actionState = ACTION_STATE_INIT_DEACCELERATE_LEFT;
                        }
                    }
                }
                if (actionIndex == RIGHT_KEY_INDEX) { // move right key release
                    if (actionState == ACTION_STATE_INIT_ACCELERATE_RIGHT || actionState == ACTION_STATE_ACCELERATE_RIGHT) {
                        if (inputKeysPressed[0]) {
                            actionState = ACTION_STATE_INIT_ACCELERATE_LEFT;
                        } else {
                            actionState = ACTION_STATE_INIT_DECELERATE_LEFT;
                        }
                    }
                }

                if (actionIndex == CENTER_KEY_INDEX) {
                    this.pos.x = 200;
                }
                inputKeysPressed[actionIndex] = false;
            }
        }
    }

    private double s = 0; // start speed
    private double f = 0; // finale speed // won't ever be reached
    private double t = 0; // time that it has bin accelerating for
    private double t_OffSet = 2; //for the formula

    // Check if any balls are inside of the paddle and need to be reflected or "pushed out"
    public void update(double dt, Vector<Ball> balls) {
        switch (actionState) {
            case ACTION_STATE_STOPPED: // not moving
                break;
            case ACTION_STATE_INIT_ACCELERATE_LEFT: // Initialize accelerating left
                s = velocityX;
                f = -maxSpeedX;
                t = 0;
                actionState = ACTION_STATE_ACCELERATE_LEFT;
                break;
            case ACTION_STATE_ACCELERATE_LEFT: // Accelerating left
                if (velocityX > 0) {
                    t += (accelerationX + deccelerationX) * dt;
                } else {
                    t += accelerationX * dt;
                }
                velocityX = (f - s) / (1 + Math.pow(Math.E, (-t + t_OffSet))) + s;
                pos.x += velocityX;
                break;
            case ACTION_STATE_INIT_DEACCELERATE_LEFT: // Initialize Deccelerating left
                s = velocityX;
                f = 0;
                t = 0;
                actionState = ACTION_STATE_DEACCELERATE_LEFT;
                break;
            case ACTION_STATE_DEACCELERATE_LEFT: // Deccelerating left
                t += deccelerationX * dt;
                velocityX = (f - s) / (1 + Math.pow(Math.E, (-t + t_OffSet))) + s;
                pos.x += velocityX;
                if (t > 10) {
                    velocityX = 0;
                    actionState = ACTION_STATE_STOPPED;
                }
                break;
            case ACTION_STATE_INIT_ACCELERATE_RIGHT: // Initialize Accelerating right
                s = velocityX;
                f = maxSpeedX;
                t = 0;
                actionState = ACTION_STATE_ACCELERATE_RIGHT;
                break;
            case ACTION_STATE_ACCELERATE_RIGHT: // Accelerating right
                if (velocityX < 0) {
                    t += (accelerationX + deccelerationX) * dt;
                } else {
                    t += accelerationX * dt;
                }
                velocityX = (f - s) / (1 + Math.pow(Math.E, (-t + t_OffSet))) + s;
                pos.x += velocityX;
                break;
            case ACTION_STATE_INIT_DECELERATE_LEFT: // Initialize Deccelerating left
                s = velocityX;
                f = 0;
                t = 0;
                actionState = ACTION_STATE_DECELERATING_LEFT;
                break;
            case ACTION_STATE_DECELERATING_LEFT: // Deaccelerating left
                t += deccelerationX * dt;
                velocityX = (f - s) / (1 + Math.pow(Math.E, (-t + t_OffSet))) + s;
                pos.x += velocityX;
                if (t > 10) {
                    velocityX = 0;
                    actionState = ACTION_STATE_STOPPED;
                }
                break;
        }

        for (Ball ball : balls) {
            Vec ballPos = ball.getRay().getLine().getP1();
            if (polygon.contains(ballPos.x, ballPos.y)) {
                Vec pos = ball.getRay().getLine().getP1().clone();
                Line[] lines = getOutLine();
                Ray ray = new Ray(new Line(pos, Vec.add(pos, new Vec(velocityX * 2, velocityY * 2))));
                int lineIntersectionIndex = ray.castIndex(lines);
                if (lineIntersectionIndex == -1) {
                    for (int i = 0; i < 5 && polygon.contains(ballPos.x, ballPos.y); i++) {
                        ball.getRay().getLine().move(new Vec(ball.getRay().getLine().getWidth(),
                                ball.getRay().getLine().getHeight()));
                    }
                    continue;
                }

                Line intersection_Line = lines[lineIntersectionIndex];

                Vec intersection_Vec = ray.cast(intersection_Line);
                if (Vec.dotProduct(intersection_Line.getNormal(), ball.getRay().getLine().getDirection()) > 0) { //
                    // ball moving towards
                    ball.getRay().getLine().move(Vec.subtract(intersection_Vec, ball.getRay().getLine().getP2()));
                    //pushs the ball out
                } else { // ball moving away
                    ball.getRay().getLine().move(Vec.subtract(intersection_Vec, ball.getRay().getLine().getP1()).multiply(2));
                }
            }
        }
        if (pos.x < 0) {
            pos.x = 0;
            velocityX = 0;
        } else if (pos.x + width > 500) {
            pos.x = 500 - width;
            velocityX = 0;
        }
        updatePolygonMesh();
    }

    public void updatePolygonMesh() {
        int xDef = (int) pos.x - polygon.xpoints[0];
        int yDef = (int) pos.y - polygon.ypoints[0];
        polygon.translate(xDef, yDef);
    }

    public static final BrickInterface paddlething = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(90, 90, 90);
        }
    };

    @Override
    public void paint(Graphics2D g) {
        g.setColor(Color.cyan);
        paddlething.paint(g, polygon);
    }

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

    public Vec getCenter() {
        double x = 0;
        for (int i = 0; i < polygon.npoints; i++) {
            x += polygon.xpoints[i];
        }
        x /= polygon.npoints;
        double y = 0;
        for (int i = 0; i < polygon.npoints; i++) {
            y += polygon.ypoints[i];
        }
        y /= polygon.npoints;
        return new Vec(x, y);
    }

    @Override
    public void updateBounds() {
        bounds = new Rect(polygon.getBounds());
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }
}
