package com.brickbreaker.Screen;

import com.brickbreaker.Main;
import com.brickbreaker.Paintable;
import com.brickbreaker.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;

// StartScreen - screen to show (p)lay or (q)uit or (r)esume
public class StartScreen extends Screen {
    private BufferedImage brickBreackerImage;
    private BufferedImage startImage;
    private BufferedImage resumetImage;
    private BufferedImage quittImage;
    private Rect[] highLightBoxes;
    private int selectedButton = -1;

    // Constructor
    public StartScreen(HashMap objects) {
        this.objects = objects;
        this.main = (Main) objects.get(Main.class);
        this.paintables = new Vector();
        this.updatables = new Vector();
        try {
            brickBreackerImage = ImageIO.read(new File("Sprites/brick_breaker.png"));
            startImage = ImageIO.read(new File("Sprites/p_play.png"));
            resumetImage = ImageIO.read(new File("Sprites/r_resume.png"));
            quittImage = ImageIO.read(new File("Sprites/q_quit.png"));
        } catch (Exception e) {
            startImage = null;
        }
        highLightBoxes = new Rect[]{new Rect(50, 50, 400, 75), new Rect(150, 200, 175, 75), new Rect(100, 295, 300,
                65), new Rect(150, 380, 200, 65)};
        paintables.add(new Paintable() {
            @Override
            public void paint(Graphics2D g) {
                Rect rect = highLightBoxes[0];
                g.drawImage(brickBreackerImage, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, null);
                rect = highLightBoxes[1];
                g.drawImage(startImage, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, null);
                rect = highLightBoxes[2];
                g.drawImage(resumetImage, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, null);
                rect = highLightBoxes[3];
                g.drawImage(quittImage, (int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, null);
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    selectedButton = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    if (((BrickBreakerScreen) main.getScreens()[2]).hasBeenSeen) {
                        selectedButton = 2;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    selectedButton = 3;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    switch (selectedButton) {
                        case 1:
                            selectedButton = -1;
                            main.getScreen().deactivate();
                            main.changeScreen(BrickBreakerScreen.class);
                            ((BrickBreakerScreen) main.getScreen()).reset();
                            ((BrickBreakerScreen) main.getScreen()).getScore().set(0);
                            main.getScreen().activate();
                            break;
                        case 2:
                            selectedButton = -1;
                            main.getScreen().deactivate();
                            main.changeScreen(BrickBreakerScreen.class);
                            main.getScreen().activate();
                            break;
                        case 3:
                            selectedButton = -1;
                            main.exit();
                            break;
                    }
                }
            }
        });
    }

    private int highlighterAlpha = 0;
    private double time = 0;

    @Override
    public void update(double dt) {
        super.update(dt);
        time += dt;
        highlighterAlpha = (int) (Math.sin(time / 5) * 17) + 75;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, highlighterAlpha));
        if (selectedButton != -1) {
            g.setColor(new Color(255, 255, 255, highlighterAlpha));
            Rect rect = highLightBoxes[selectedButton];
            g.fillRoundRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, 16, 16);
        }
        super.paint(g);
        if (((BrickBreakerScreen) main.getScreens()[1]).hasBeenSeen) {
            g.setColor(new Color(0, 0, 0, highlighterAlpha + 100));
            Rect rect = highLightBoxes[2];
            g.fillRoundRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, 16, 16);
        }
    }
}
