package com.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Used to handle Window resizing and mouse input resizing to ensure that the game board
// maintains the correct aspect ratio.
public class Scaler {
    private JFrame frame;
    Rectangle ratioFrame;
    private int maxX;
    private int maxY;

    // Constructor
    public Scaler(int maxX, int maxY, JFrame frame) {
        this.frame = frame;
        this.maxX = maxX;
        this.maxY = maxY;
        this.ratioFrame = new Rectangle(0, 0, maxX, maxY);
    }

    // Updates the frame ratios based on the window dimensions
    public void newRatioFrame() {
        //Resizing happens here
        double ratio = (double) maxX / maxY;
        ratioFrame.x = (0);
        ratioFrame.y = (0);
        if (frame.getSize().width > frame.getSize().height * ratio) {
            ratio = (double) maxY / maxX;
            ratioFrame.height = (frame.getSize().height);
            ratioFrame.width = ((int) (frame.getSize().height * ratio));
            //Recentering happens here
            ratioFrame.x = ((int) ((frame.getSize().width - (int) (frame.getSize().height * ratio)) / 2));
        } else {
            ratio = (double) maxY / maxX;
            ratioFrame.width = (frame.getSize().width);
            ratioFrame.height = ((int) (frame.getSize().width * ratio));
            //Recentering happens here
            ratioFrame.y = ((int) (((frame.getSize().height) - (int) (frame.getSize().width * ratio)) / 2));
        }
    }

    public int scaleToFrameX(int x) {
        return (int) (((double) ratioFrame.width / maxX * x) + ratioFrame.getX());
    }

    public int scaleToFrameW(int w) {
        return (int) ((double) ratioFrame.width / maxX * w);
    }

    public int scaleToFrameY(int y) {
        return (int) (((double) ratioFrame.height / maxY * y) + ratioFrame.getY());
    }

    public int scaleToFrameH(int h) {
        return (int) ((double) ratioFrame.height / maxY * h);
    }

    /// //////////////////////////////
    public double scaleDownX(double x) {
        return (((double) (x - ratioFrame.getX()) * maxX) / ratioFrame.width);
    }

    public double scaleDownY(double y) {
        return (((double) (y - ratioFrame.getY()) * maxY) / ratioFrame.width);
    }

    /// //////////////////////////////
    public int scaleToFrameX(double x) {
        return (int) (((double) ratioFrame.width / maxX * x) + ratioFrame.getX());
    }

    public int scaleToFrameW(double w) {
        return (int) ((double) ratioFrame.width / maxX * w);
    }

    public int scaleToFrameY(double y) {
        return (int) (((double) ratioFrame.height / maxY * y) + ratioFrame.getY());
    }

    public int scaleToFrameH(double h) {
        return (int) ((double) ratioFrame.height / maxY * h);
    }

    /// //////////////////////////////
    public double getScale() {
        return (double) ratioFrame.height / maxY;
    }

    public Rectangle getRatioFrame() {
        return ratioFrame;
    }

    public void ScaleImage(BufferedImage image) {
        //image.getScaledInstance(ratioFrame.x, ratioFrame.y, 0);
    }
}
