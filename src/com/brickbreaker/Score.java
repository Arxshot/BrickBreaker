package com.brickbreaker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

// The score text at the bottom of the screen to provide user feedback. Tracks the actual score with
// AtomicIntegers so that it can be shared between the game thread and the rendering thread.
public class Score implements Paintable {
    private AtomicInteger score;
    private Rect rect;
    private BufferedImage scoreImage;
    private BufferedImage[] numberImages;

    // Constructor
    public Score(AtomicInteger score, Rect rect) {
        this.score = score;
        this.rect = rect;
        numberImages = new BufferedImage[10];
        try {
            scoreImage = ImageIO.read(new File("Sprites/Score.png"));
            for (int i = 0; i <= 9; i++) {
                numberImages[i] = ImageIO.read(new File("Sprites/" + i + ".png"));
            }
        } catch (Exception e) {
        }
    }

    public AtomicInteger getScore() {
        return score;
    }

    public Rect getRect() {
        return rect;
    }

    @Override
    public void paint(Graphics2D g) {
        int s = score.intValue();
        int[] values;
        if (s > 0) {
            values = new int[(int) Math.log10(s) + 1];
            int i = values.length - 1;
            while (s != 0) {
                values[i] = s % 10;
                i--;
                s /= 10;
            }
        } else {
            values = new int[]{0};
        }
        final double scoreRatio = 4;
        final double numberRatio = 1;
        g.drawImage(scoreImage, (int) rect.x, (int) rect.y, (int) (rect.height * scoreRatio), (int) rect.height, null);
        int offset = (int) (rect.height * scoreRatio);
        for (int i = 0; i < values.length; i++) {
            BufferedImage number = numberImages[values[i]];
            int xPosition = (int) rect.x + offset + (int) (rect.height * numberRatio * i);
            int yPosition = (int) rect.y;
            int width = (int) (rect.height * numberRatio);
            int height = (int) rect.height;
            //System.out.println( "Draw: " + values [i] + " at " + xPosition + "," + yPosition + ", width=" + width +
            // ", height=" + height );
            g.drawImage(number, xPosition, yPosition, width, height, null);
        }
    }
}
