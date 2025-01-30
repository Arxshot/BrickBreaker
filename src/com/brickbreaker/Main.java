package com.brickbreaker;

import com.brickbreaker.Screen.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

// Main is the startup application which contains the primary JFrame window.
// It uses double buffering to an image with custom painting, scaling and my
// own components.
public class Main extends JFrame {
    private static final String objects_DataFile = "hashmap.ser";
    private JPanel panel;
    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;
    private HashMap objects;
    private int maxX;
    private int maxY;
    private Scaler scale;
    private Integer gameScreen;
    private Screen[] screens;
    private HashMap<Class, Integer> screenName;

    // Constructor
    public Main() {
        this.maxX = 500;
        this.maxY = 500;
        this.gameScreen = 1;
        this.scale = new Scaler(maxX, maxY, this);
        if (objects == null) {
            this.objects = new HashMap<String, Object>();
        }

        this.objects.put("GameState", gameScreen);
        this.objects.put(Scaler.class, scale);
        this.objects.put(Main.class, this);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setTitle("Brick Breaker");
        setSize(500, 575);
        setLocation(50, 0);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(true);
        add(panel);
        setBackground(Color.BLACK);
        createScreen();
        createInputKeys();
        changeScreen(StartScreen.class);
        addKeyListener(new KeyListener() { // press tab to move to the testing screen
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (KeyEvent.VK_0 <= e.getKeyCode() && e.getKeyCode() <= KeyEvent.VK_9) {//48 -> 57
                    int num = e.getKeyCode() - 0x30;
                    if (screens.length > num) {// 0x30 = 48
                        getScreen().deactivate();
                        gameScreen = num;
                        getScreen().activate();
                    }
                }
            }
        });
        try {
            URL url = new URL(new URL("file:"), "./Sounds/Drum.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
            //loop continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Creates and registers all the screens for the game so they can be switched
    // during game play
    public void createScreen() {
        StartScreen start = new StartScreen(objects);
        addObject(StartScreen.class, start);
        BrickBreakerScreen brickBeacker = new BrickBreakerScreen(objects);
        addObject(BrickBreakerScreen.class, brickBeacker);
        PauseScreen pauseScreen = new PauseScreen(objects);
        addObject(PauseScreen.class, pauseScreen);
        GameOverScreen gameOverScreen = new GameOverScreen(objects);
        addObject(GameOverScreen.class, gameOverScreen);
        screens = new Screen[]{start, brickBeacker, pauseScreen, gameOverScreen};
        screenName = new HashMap<>(screens.length);
        for (int i = 0; i < screens.length; i++) {
            screenName.put(screens[i].getClass(), i);
        }
    }

    public Screen[] getScreens() {
        return screens;
    }

    // Registers the keys used for starting and pausing the game

    public void createInputKeys() {
        objects.put("Pause_InputKeys", KeyEvent.VK_P);
        objects.put("Start_InputKeys", KeyEvent.VK_SPACE);
    }

    // The application entry point which starts the game
    public static void main(String[] args) throws IOException {
        // write your code here
        Main main = new Main();
        long time = System.currentTimeMillis();
        long preTime;
        long dt;
        main.screens[main.gameScreen].activate();

        while (true) {
            preTime = time;
            time = System.currentTimeMillis();
            dt = time - preTime;
            if (0 < dt) {
                main.screens[main.gameScreen].update(0.16);//dt/1000.0);
            }

            main.fullUpdate();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Called when Java determines that the screen should be painted
    public void paint(Graphics g) {
        // checks the buffersize with the current panelsize
        // or initialises the image with the first paint
        if (bufferWidth != getSize().width || bufferHeight != getSize().height || bufferImage == null || bufferGraphics == null) {
            resetBuffer();
            scale.newRatioFrame();
        }
        if (bufferGraphics != null) {
            //this clears the offscreen image, not the onscreen one
            bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);
            //calls the paintbuffer method with
            //the offscreen graphics as a param
            paintBuffer(bufferGraphics);
            //we finally paint the offscreen image onto the onscreen image
            g.drawImage(bufferImage, 0, 0, bufferWidth, bufferHeight, this);
        }
    }

    // Called when the Window is resized and the the double buffering image needs
    // to be resized.
    private void resetBuffer() {
        // always keep track of the image size
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;
        // clean up the previous image
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (bufferImage != null) {
            bufferImage.flush();
            bufferImage = null;
        }
        System.gc();
        // create the new image with the size of the panel
        bufferImage = createImage(bufferWidth, bufferHeight);
        bufferGraphics = bufferImage.getGraphics();
    }

    // Called to paint the game components to the double buffering image
    public void paintBuffer(Graphics g) {
        //in classes extended from this one, add something to paint here
        //always remember, g is the offscreen graphics
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        Image tempImage = createImage(maxX, maxY);
        Graphics2D tempImageG2 = (Graphics2D) tempImage.getGraphics();
        //tempImageG2.setColor(new Color(0,0,0,0));
        //tempImageG2.fillRect(0,0,maxX,maxY);
        if (screens != null && screens[gameScreen] != null) {
            screens[gameScreen].paint((Graphics2D) tempImage.getGraphics());
        }
        g2.drawImage(tempImage, scale.getRatioFrame().x, scale.getRatioFrame().y, scale.getRatioFrame().width,
                scale.getRatioFrame().height, this);
    }

    public Scaler getScale() {
        return this.scale;
    }

    public int getGameScreen() {
        return gameScreen;
    }

    public void changeScreen(Class c) {
        gameScreen = screenName.get(c);
    }

    // public void changeScreen(int i) {
    // gameStatue = i;
    // }
    public void changeScreen(Screen s) {
        for (int i = 0; i < screens.length; i++) {
            if (screens[i] == s) {
                gameScreen = i;
                return;
            }
        }
        System.err.println("ERROR: Screen not found");
    }

    public Screen getScreen() {
        return screens[gameScreen];
    }

    public HashMap getObjects() {
        return objects;
    }

    public void addObject(Object str, Object object) {
        objects.put(str, object);
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    /// //////////////////////////////////////
    public void fullUpdate() {
        Scaler scale = (Scaler) objects.get(Scaler.class);
        scale.newRatioFrame();
        repaint();
    }

    public void exit() {
        dispose();
        System.exit(0);
    }
}
