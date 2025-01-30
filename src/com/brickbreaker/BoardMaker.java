package com.brickbreaker;

import java.awt.*;
import java.util.Vector;

// Creates all the bricks and walls for the game. It may create one of several game spaces.

public class BoardMaker {
    // Square game board or a triangular game board
    private static final Polygon[] Possible_PlayBoard = new Polygon[]{new Polygon( //square
            new int[]{0, 0, 500, 500}, // x
            new int[]{500, 0, 0, 500}, // y
            4), new Polygon( // triangle
            new int[]{0, 250, 500}, // x
            new int[]{500, 0, 500}, // y
            3)};

    // Possible brick layouts to place in the square layout
    private static final Polygon[][] Possible_Square_BrickSpaces = new Polygon[][]{{new Polygon(new int[]{0, 0, 500,
            500}, // x
            new int[]{250, 0, 0, 250}, // y
            4), null, null, null}, {new Polygon(new int[]{0, 250, 500}, // x
            new int[]{250, 0, 250}, // y
            3), null, null, null}, {new Polygon(new int[]{0, 0, 227, 227}, // x
            new int[]{114, 0, 0, 114}, // y
            4), new Polygon(new int[]{272, 272, 500, 500}, // x
            new int[]{114, 0, 0, 114}, // y
            4), new Polygon(new int[]{0, 0, 227, 227}, // x
            new int[]{250, 136, 136, 250}, // y
            4), new Polygon(new int[]{272, 272, 500, 500}, // x
            new int[]{250, 136, 136, 250}, // y
            4), null}

    };

    // Possible brick layouts to place in the triangular layout
    private static final Polygon[][] Possible_Triangle_BrickSpaces = new Polygon[][]{{new Polygon(new int[]{125, 250,
            375}, // x
            new int[]{250, 0, 250}, // y
            3), null, null, null}, {new Polygon(new int[]{208, 250, 292}, // x
            new int[]{84, 0, 84}, // y
            3), new Polygon(new int[]{125, 166, 334, 375}, // x
            new int[]{250, 166, 166, 250}, // y
            4), null, null}, {new Polygon(new int[]{208, 250, 292}, // x
            new int[]{84, 0, 84}, // y
            3), new Polygon(new int[]{208, 166, 334, 292}, // x
            new int[]{84, 166, 166, 84}, // y
            4), new Polygon(new int[]{125, 166, 334, 375}, // x
            new int[]{250, 166, 166, 250}, // y
            4), null}};

    // Combines the different permutations of brick layouts and spaces so they
    // can be randomly selected.
    private static final Polygon[][][] Possible_BrickSpaces = new Polygon[][][]{Possible_Square_BrickSpaces,
            Possible_Triangle_BrickSpaces};
    private Polygon playBoard;
    private Polygon[] brickSpaces;
    private Vector<Brick> bricks;
    private Vector<Paddle> paddles;
    private Vector<Wall> walls;

    // Initializes all the possible allowable permutations of game board layouts.
    public BoardMaker() {
        int playBoardIndex = (int) (Possible_PlayBoard.length * Math.random());
        int BrickSpacesIndex = (int) (Possible_BrickSpaces.length * Math.random());
        this.playBoard = Possible_PlayBoard[playBoardIndex];
        for (int i = 0; i < 4; i++) {
            if (Possible_BrickSpaces[playBoardIndex][BrickSpacesIndex][i] == null) {
                brickSpaces = new Polygon[i];
                break;
            }
        }
        if (this.brickSpaces == null) {
            brickSpaces = new Polygon[4];
        }
        for (int i = 0; i < this.brickSpaces.length; i++) {
            this.brickSpaces[i] = Possible_BrickSpaces[playBoardIndex][BrickSpacesIndex][i];
        }
    }

    ;

    // Makes an iterator for cutting larger brick spaces into smaller brick spaces
    public static BoardMakerIterator iterator(Polygon brickSpaces) {
        Vector vector = new Vector();
        vector.add(brickSpaces);
        return new BoardMakerIterator(vector);
    }

    // Create all the game pieces
    public void create() {
        createPaddles(playBoard);
        createWalls(playBoard);
        createBricks();
    }

    // Creates the bricks of the game filling the layout by segmenting the layouts
    public void createBricks() {
        bricks = new Vector<>();
        Vector<Polygon>[] newPolygon = new Vector[brickSpaces.length];
        for (int i = 0; i < brickSpaces.length; i++) {
            Vector<Polygon> temp = new Vector<>();
            temp.add(brickSpaces[i]);

            BoardMakerIterator Iterator = new BoardMakerIterator(temp);

            final int numOfIters = 2;
            final int numOfSegmentationType = 3;

            int[] arr = new int[numOfIters];

            for (int j = 0; j < numOfIters; j++) {
                arr[j] = (int) (Math.random() * numOfSegmentationType);
                if (j != 0 && arr[j] == arr[j - 1]) {
                    j--;
                }
            }

            for (int j = 0; j < numOfIters; j++) {
                switch (arr[j]) {
                    case 0:
                        Iterator = Iterator.triangleSegmentation();
                        break;
                    case 1:
                        Iterator = Iterator.squareSegmentation();
                        break;
                    case 2:
                        Iterator = Iterator.flowerSegmentation();
                        break;
                }
            }
            newPolygon[i] = Iterator.getPolygons();
        }

        for (int i = 0; i < newPolygon.length; i++) {
            int maxBrickLevel = (int) (Math.random() * 6) + 1;
            int variability = (int) (Math.random() * maxBrickLevel);
            double probabilityOfPowerUps = Math.random() * maxBrickLevel;
            addBrickInterface(newPolygon[i], maxBrickLevel, variability, probabilityOfPowerUps);
        }
    }


    // Each brick contains features for changing the color, number of required hits (brick level)
    // variability changes the probability of mixing different levels of bricks
    public void addBrickInterface(Vector<Polygon> polygons, int maxBrickLevel, int variability,
                                  double probabilityOfPowerUps) {
        for (int i = 0; i < polygons.size(); i++) {
            BrickInterface[] bi = BrickInterface.getNormal(maxBrickLevel - (int) (Math.random() * variability));
            int num = (int) (Math.random() * probabilityOfPowerUps);
            for (int j = 0; j < num; j++) {
                assert bi != null;
                bi[(int) (Math.random() * bi.length)] =
                        BrickInterface.PowerUpArray[(int) (Math.random() * BrickInterface.PowerUpArray.length)];
            }
            bricks.add(new Brick(polygons.get(i), bi));
        }
    }

    public Vector<Brick> getBricks() {
        return bricks;
    }

    // Creates the paddle which the player moves back and forth

    private void createPaddles(Polygon playBoard) {
        paddles = new Vector<>();
        int num = 1;
        for (int i = 0; i < num; i++) {
            Paddle paddle = new Paddle(76, 450);
            paddle.getPos().setX(((playBoard.xpoints[0] + playBoard.xpoints[playBoard.npoints - 1]) / 2) - (paddle.getWidth() / 2));
            paddles.add(paddle);
        }
    }

    public Vector<Paddle> getPaddles() {
        return paddles;
    }

    // Creates the side walls of the game, it will be either square walls
    // or triangular walls depending upon
    // the game board shape.
    private void createWalls(Polygon playBoard) {
        walls = new Vector<>();
        for (int i = 0; i < playBoard.npoints - 1; i++) {
            Wall wall = new Wall(new Line(playBoard.xpoints[i], playBoard.ypoints[i], playBoard.xpoints[i + 1],
                    playBoard.ypoints[i + 1]));
            walls.add(wall);
        }
    }

    public Vector<Wall> getWalls() {
        return walls;
    }

    public Polygon getPlayBoard() {
        return playBoard;
    }

    public Polygon[] getBrickSpaces() {
        return brickSpaces;
    }
}
