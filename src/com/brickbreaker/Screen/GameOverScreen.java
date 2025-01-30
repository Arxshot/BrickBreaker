package com.brickbreaker.Screen;

import com.brickbreaker.Main;
import com.brickbreaker.Paintable;
import com.brickbreaker.Rect;
import com.brickbreaker.Score;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

// GameOverScreen - screen that shows the final score and returns you back to the start screen
public class GameOverScreen extends Screen {
    private Screen returnScreen = null; // screen that it goes to after it is done pause
    private Screen backgroundScreen = null; // what is seen in the background
    private double BackgroundScreenUpdateModifier = 0; // the update Modifier for the background screen
    private BufferedImage quitImage;
    private Rect hightLight;

    // Constructor
    public GameOverScreen(HashMap objects) {
        this.objects = objects;
        this.main = (Main) objects.get(Main.class);
        this.paintables = new Vector();
        this.updatables = new Vector();
        try {
            quitImage = ImageIO.read(new File("Sprites/q_quit.png"));
        } catch (Exception e) {
            quitImage = null;
        }
        this.hightLight = new Rect(125, 300, 250, 75);
        Paintable backgroundShade = new Paintable() {
            @Override
            public void paint(Graphics2D g) {
                g.setColor(new Color(0, 0, 0, 50));
                g.fillRect(0, 0, (int) (main.getMaxX() + 0.5), (int) (main.getMaxY() + 0.5));
            }
        };
        paintables.add(backgroundShade);
        Paintable puaseTitle = new Paintable() {
            @Override
            public void paint(Graphics2D g) {
                g.drawImage(quitImage, (int) hightLight.x, (int) hightLight.y, (int) hightLight.width,
                        (int) hightLight.height, null);
            }
        };
        paintables.add(puaseTitle);
        addKeyListener(new KeyListener() {
            boolean puaseButtonWasLifted = false;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // pause key
                    main.getScreen().deactivate();
                    main.changeScreen(StartScreen.class);
                    main.getScreen().activate();
                }
            }
        });
    }

    public void setReturnScreen(Screen returnScreen) {
        this.returnScreen = returnScreen;
    }

    public void setBackgroundScreen(Screen backgroundScreen) {
        this.backgroundScreen = backgroundScreen;
    }

    public void setBackgroundScreenUpdateModifier(double BackgroundScreenUpdateModifier) {
        this.BackgroundScreenUpdateModifier = BackgroundScreenUpdateModifier;
    }

    private int highlighterAlpha = 0;
    private double time = 0;

    @Override
    public void update(double dt) {
        if (backgroundScreen != null && 0 < BackgroundScreenUpdateModifier) {
            backgroundScreen.update(dt * BackgroundScreenUpdateModifier);
        }
        time += dt;
        highlighterAlpha = (int) (Math.sin(time / 5) * 17) + 75;
        super.update(dt);
    }

    @Override
    public void paint(Graphics2D g) {
        if (backgroundScreen != null) {
            backgroundScreen.paint(g); // paint background
        }
        super.paint(g); // paint foreground
        g.setColor(new Color(255, 255, 255, highlighterAlpha));
        g.fillRoundRect((int) hightLight.x, (int) hightLight.y, (int) hightLight.width, (int) hightLight.height, 16,
                16);
    }

    private Score score;

    public void setScore(AtomicInteger score) {
        paintables.remove(this.score);
        this.score = new Score(score, new Rect(25, 100, 0, 50));
        paintables.add(this.score);
    }
}
