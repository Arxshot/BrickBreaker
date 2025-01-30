package com.brickbreaker.Screen;

import com.brickbreaker.*;
import com.brickbreaker.PowerUps.PowerUp;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// BrickBreakerScreen - the main game screen where the game runs
public class BrickBreakerScreen extends Screen {
    private Vector<Ball> balls;
    private Vector<Paddle> paddles;
    private Vector<Wall> walls;
    private Vector<Brick> bricks;
    private Vector<PowerUp> powerUps;
    private AtomicInteger score = new AtomicInteger(0);
    private Score scoreObject = new Score(score, new Rect());
    private int startDestroyedBrickIndex;
    private int level = 1;
    private Heart[] hearts = createHearts(20);
    private int lives = 0;


    public enum GameState {
        UNINITIALIZED, BRICKS_BEING_MADE, SHOOT_BALL, BREAKING_BRICK, GAME_OVER
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public Score getScoreObject() {
        return scoreObject;
    }

    private static final Heart[] createHearts(int maxNumHearts) {
        Heart[] hearts = new Heart[maxNumHearts];
        for (int i = 0; i < maxNumHearts; i++) {
            int x = 480 - 25 * i;
            int y = 480;
            hearts[i] = new Heart(new Vec(x, y), 5);
        }
        return hearts;
    }

    private GameState gameState = GameState.UNINITIALIZED;
    public boolean hasBeenSeen = false;
    private Polygon playingBoard;
    private Polygon brickSpace;

    public BrickBreakerScreen(HashMap objects) {
        this.objects = objects;
        this.main = (Main) objects.get(Main.class);
        this.paintables = new Vector();
        this.updatables = new Vector();
        this.balls = new Vector<>();
        this.paddles = new Vector<>();
        this.walls = new Vector<>();
        this.bricks = new Vector<>();
        this.powerUps = new Vector<>();
        createLevel();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (int) objects.get("Pause_InputKeys")) { // pause key
                    Screen s = main.getScreen();
                    s.deactivate();
                    main.changeScreen(PauseScreen.class);
                    ((PauseScreen) main.getScreen()).setBackgroundScreen(s);
                    ((PauseScreen) main.getScreen()).setReturnScreen(s);
                    main.getScreen().activate();
                } else if (e.getKeyCode() == (int) KeyEvent.VK_R) {
                    reset();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == (int) objects.get("Start_InputKeys") && getGameState() == GameState.SHOOT_BALL) {
                    // Start key
                    setGameState(GameState.BREAKING_BRICK);
                    updatables.add(new Updatable() { // fixes a threading bug
                        @Override
                        public void update(double dt) {
                            Rect pladlerect = paddles.get(0).getBounds();
                            Vec pos = new Vec(pladlerect.x + (pladlerect.width / 2),
                                    pladlerect.y - (pladlerect.height / 3));
                            Ball ball = new Ball(pos, 4, new Vec(0, 0));
                            paintables.add(ball);
                            updatables.add(ball);
                            movingColliders.add(ball);
                            balls.add(ball);
                            ball.setDirection(Vec.add(new Vec(0, -1), Vec.randomDirections().multiply(0.1)));
                            //System.out.println(balls.size());
                            updatables.remove(this);
                        }
                    });

                    updatables.add(new Updatable() { // fixes a threading bug
                        @Override
                        public void update(double dt) {

                            if (balls.size() == 0) {
                                setGameState(GameState.SHOOT_BALL);
                                lives -= 1;
                                if (lives <= 0) {
                                    setGameState(GameState.GAME_OVER);
                                    gameOver();
                                }
                                updatables.remove(this);
                            } else if (startDestroyedBrickIndex == 0) {
                                onLevelComplete();
                                reset();
                                updatables.remove(this);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Update is called by the main thread game
     * loop whenever the game component locations need to
     * change
     * their physical location on the game board.
     * This will occur roughly every 16ms to achieve
     * a frame rate of 60fps. It checks for
     * collisions, whether components need to be removed
     * and
     * handles appropriately.
     *
     * @param dt delta time since last udpate
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        for (Paddle paddle : paddles) {
            paddle.update(dt, balls);
        }

        for (int i = balls.size() - 1; i >= 0; i--) {
            Vec pos = balls.get(i).getRay().getLine().getP1();

            // Check ball is in play
            if (!playingBoard.contains(pos.getX(), pos.getY())) {
                // If the ball is no longer in play, remove from:
                paintables.remove(balls.get(i)); // no longer paintable
                updatables.remove(balls.get(i)); // no longer updatable
                movingColliders.remove(balls.get(i)); // no longer collidable
                balls.remove(i); // destroy the ball
                continue;
            }

            // Move the ball the appropriate distance, checking for
            // padding and wall collisions and reflect.
            // If it hits a brick, break by one, reflect and adjust the
            // score or add power ups to board.
            balls.get(i).update(dt, paddles, walls, bricks, startDestroyedBrickIndex, powerUps, score);

            // If we have bricks which exist
            if (bricks.size() != 0) {
                bricks.sort(Brick.hitsTillBreak_SORT); // Sort by number of required hits to break them
                if (bricks.get(0).isDestroyed()) { // Check if the last brick has been destroyed
                    onLevelComplete();
                    // When last broken, we are done the level
                    startDestroyedBrickIndex = 0;
                } else {
                    // If we still have bricks to destroy
                    for (int j = startDestroyedBrickIndex - 1; j >= 0; j--) {
                        // Find where the start of the non-broken bricks are in the array
                        if (bricks.get(j).isDestroyed()) {
                            startDestroyedBrickIndex = j;
                        }
                    }
                }
            }
        }

        // Handle the power ups by checking still in play, and removing if no longer in play.
        for (int i = 0; i < powerUps.size(); i++) {
            Vec pos = powerUps.get(i).getRay().getLine().getP1();
            if (playingBoard.contains(pos.getX(), pos.getY())) { // If power up is no longer in game play
                paintables.remove(powerUps.get(i)); // no longer paintable
                updatables.remove(powerUps.get(i)); // no longer updatable
                movingColliders.remove(powerUps.get(i)); // no longer collidable
                powerUps.remove(i); // destroy the powerup
                continue;
            }

            // Check if the powerups have been hit by a paddle and enable if power up feature
            powerUps.get(i).update(dt, paddles, powerUps, balls, bricks, startDestroyedBrickIndex, score, paintables,
                    updatables, movingColliders);
        }
    }

    public void clearLevel() {
        paintables.removeAll(walls);
        movingColliders.removeAll(walls);
        colliders.removeAll(walls);
        updatables.removeAll(walls);
        walls.removeAll(walls);
        paintables.removeAll(paddles);
        movingColliders.removeAll(paddles);
        colliders.removeAll(paddles);
        updatables.removeAll(paddles);
        paddles.removeAll(paddles);//
        paintables.removeAll(balls);
        movingColliders.removeAll(balls);
        colliders.removeAll(balls);
        updatables.removeAll(balls);
        balls.removeAll(balls); //
        paintables.removeAll(bricks);
        movingColliders.removeAll(bricks);
        colliders.removeAll(bricks);
        updatables.removeAll(bricks);
        bricks.removeAll(bricks); //
        paintables.removeAll(powerUps);
        movingColliders.removeAll(powerUps);
        colliders.removeAll(powerUps);
        updatables.removeAll(powerUps);
        powerUps.removeAll(powerUps); //
        updatables.clear();
        playingBoard = null;
        brickSpace = null;
        startDestroyedBrickIndex = 0;
        for (int i = 0; i < paddles.size(); i++) {
            removeKeyListener(paddles.get(i));
        }
    }

    @Override
    public void activate() {
        super.activate();
        hasBeenSeen = true;
    }

    public void createLevel() {
        setGameState(GameState.BRICKS_BEING_MADE);
        lives = 3;
        scoreObject.getRect().setRect(new Rect(10, 470, 0, 15));
        paintables.add(scoreObject);
        Paddle paddle = new Paddle(76, 450);
        paddle.getPos().setX(250 - (paddle.getWidth() / 2));
        paintables.add(paddle);
        //updateAbles.add(pladle);
        movingColliders.add(paddle);
        paddles.add(paddle);
        addKeyListener(paddle);
        BoardMaker boardMaker = new BoardMaker();
        boardMaker.create();
        playingBoard = boardMaker.getPlayBoard();

        for (int i = 0; i < playingBoard.npoints - 1; i++) {
            Wall wall = new Wall(new Line(playingBoard.xpoints[i], playingBoard.ypoints[i],
                    playingBoard.xpoints[i + 1], playingBoard.ypoints[i + 1]));
            //paintAbles.add(wall);
            colliders.add(wall);
            walls.add(wall);
        }

        brickSpace = new Polygon(new int[]{0, 0, 500, 500}, // x
                new int[]{250, 0, 0, 250}, // y
                4);
        bricks.addAll(boardMaker.getBricks());
        colliders.addAll(bricks);
        // bricks.addAll(bricks);
        updateColliders();
        startDestroyedBrickIndex = bricks.size();
        setGameState(GameState.SHOOT_BALL);
    }

    public void reset() {
        clearLevel();
        createLevel();
    }

    public void onLevelComplete() {
        lives += 1;
        if (lives > 19) {
            lives = 19;
        }
    }

    public AtomicInteger getScore() {
        return score;
    }


    public void paint(Graphics2D g) {
        Global.lightDirectionVec.rotate(0.01);

        if (getGameState() != GameState.BRICKS_BEING_MADE && getGameState() != GameState.GAME_OVER) {
            g.setColor(new Color(25, 25, 25));
            g.fillPolygon(playingBoard);

            for (int i = 0; i < lives; i++) {
                hearts[i].paint(g);
            }

            super.paint(g);

            for (int i = 0; i < startDestroyedBrickIndex; i++) {
                bricks.get(i).paint(g);
            }

            for (PowerUp powerUp : powerUps) {
                powerUp.paint(g);
            }
        }
    }

    public void gameOver() {
        main.getScreen().deactivate();
        main.changeScreen(GameOverScreen.class);
        ((GameOverScreen) main.getScreen()).setScore(score);
        main.getScreen().activate();
        hasBeenSeen = false;
    }
}
