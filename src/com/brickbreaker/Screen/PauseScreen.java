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

// PauseScreen - pause game after which you can return to the game or start screen
public class PauseScreen extends Screen {
    private Screen returnScreen = null; // screen that it goes to after it is done pause
    private Screen backgroundScreen = null; // what is seen in the background
    private double BackgroundScreenUpdateModifier = 0; // the update Modifier for the background screen
    private BufferedImage resumeImage;
    private BufferedImage quitImage;
    private int selectedbutton = -1;
    private Rect[] highLightBoxes;

    // Constructor
    public PauseScreen(HashMap objects) {
        this.objects = objects;
        this.main = (Main) objects.get(Main.class);
        this.paintables = new Vector();
        this.updatables = new Vector();
        try {
            resumeImage = ImageIO.read(new File("Sprites/r_resume.png"));
            quitImage = ImageIO.read(new File("Sprites/q_quit.png"));
        } catch (Exception e) {
            resumeImage = null;
            quitImage = null;
        }
        highLightBoxes = new Rect[]{new Rect(75, 100, 350, 100), new Rect(125, 300, 250, 75)};
        Paintable backgroundShade = new Paintable() {
            @Override
            public void paint(Graphics2D g) {
                g.setColor(new Color(0, 0, 0, 75));
                g.fillRect(0, 0, (int) (main.getMaxX() + 0.5), (int) (main.getMaxY() + 0.5));
            }
        };
        paintables.add(backgroundShade);
        Paintable puaseTitle = new Paintable() {
            @Override
            public void paint(Graphics2D g) {
                g.drawImage(resumeImage, (int) highLightBoxes[0].x, (int) highLightBoxes[0].y,
                        (int) highLightBoxes[0].width, (int) highLightBoxes[0].height, null);
                g.drawImage(quitImage, (int) highLightBoxes[1].x, (int) highLightBoxes[1].y,
                        (int) highLightBoxes[1].width, (int) highLightBoxes[1].height, null);
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
                if (e.getKeyCode() == KeyEvent.VK_P) { // pause key
                    if (!puaseButtonWasLifted) {
                        puaseButtonWasLifted = true;
                    } else {
                        puaseButtonWasLifted = false;
                        unPause();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    selectedbutton = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    selectedbutton = 1;
                } //else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    switch (selectedbutton) {
                        case 0:
                            selectedbutton = -1;
                            puaseButtonWasLifted = false;
                            unPause();
                            break;
                        case 1:
                            puaseButtonWasLifted = false;
                            selectedbutton = -1;
                            main.getScreen().deactivate();
                            main.changeScreen(StartScreen.class);
                            main.getScreen().activate();
                    }
                }
           // }
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

    public void unPause() {
        main.getScreen().deactivate();
        main.changeScreen(returnScreen);
        main.getScreen().activate();
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
        if (selectedbutton != -1) {
            g.setColor(new Color(255, 255, 255, highlighterAlpha));
            Rect rect = highLightBoxes[selectedbutton];
            g.fillRoundRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height, 16, 16);
        }
    }
}
