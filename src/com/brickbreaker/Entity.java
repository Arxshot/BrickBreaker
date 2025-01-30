package com.brickbreaker;

import java.awt.*;

// A game entity which has useful qualities of being paintable and updatable
public abstract class Entity implements Paintable, Updatable {
    protected float paintLayer = 0;

    public void setPaintLayer(float paintLayer) {
        this.paintLayer = paintLayer;
    }

    public float getPaintLayer() {
        return paintLayer;
    }

    private interface Action {
        public void perform(double dt);
    }

    public void paint(Graphics2D g) {
    }

    @Override
    public void update(double dt) {
    }
}
