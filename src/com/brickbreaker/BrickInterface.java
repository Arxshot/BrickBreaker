package com.brickbreaker;

import com.brickbreaker.PowerUps.*;

import java.awt.*;
import java.util.Vector;

// Provides the visuals, onhit effects and the update effects for a brick
public abstract class BrickInterface {
    // A brick which has the ball triple power up
    public static final BrickInterface PowerUpBall2 = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(255, 100, 100);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Ball_2(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpBall_Fast = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(225, 50, 255);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Ball_Fast(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpBall_Slow = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(25, 25, 200);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Ball_Slow(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpPlade_Slow = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(80, 170, 255);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Paddle_OnIce(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpPlade_OnIce = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(80, 170, 255);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Paddle_OnIce(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpPlade_NoFriction = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(80, 170, 255);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Paddle_NoFriction(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpPladle_Fast = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(225, 225, 100);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Paddle_Fast(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };

    public static final BrickInterface PowerUpPladle_Slow = new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(225, 50, 150);
        }

        @Override
        public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
            super.onHit(polygon, powerUps);
            Vec pos = getCenter(polygon);
            powerUps.add(new Paddle_Slow(new Line(pos, Vec.add(pos, new Vec(0, 1)))));
        }
    };
    public static final BrickInterface[] PowerUpArray = new BrickInterface[]{ // 6
            PowerUpBall2, PowerUpPlade_OnIce, PowerUpPladle_Fast, PowerUpPladle_Slow, PowerUpPlade_NoFriction,
            PowerUpBall_Slow, PowerUpBall_Fast};

    public static BrickInterface[] getNormal(int index) {
        switch (index) {
            case 0:
                return Normal_1.clone();
            case 1:
                return Normal_1.clone();
            case 2:
                return Normal_2.clone();
            case 3:
                return Normal_3.clone();
            case 4:
                return Normal_4.clone();
            case 5:
                return Normal_5.clone();
            case 6:
                return Normal_6.clone();
        }
        return null;
    }

    public static final BrickInterface[] Normal_1 = new BrickInterface[]{new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 50);
        }
    }};

    public static final BrickInterface[] Normal_2 = new BrickInterface[]{new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 50);
        }
    }};

    public static final BrickInterface[] Normal_3 = new BrickInterface[]{new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 50);
        }
    }};

    public static final BrickInterface[] Normal_4 = new BrickInterface[]{new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 200);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 50);
        }
    }};

    public static final BrickInterface[] Normal_5 = new BrickInterface[]{new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 50, 200);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 200);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 50);
        }
    }};

    public static final BrickInterface[] Normal_6 = new BrickInterface[]{new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 200);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 50, 200);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 200);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(50, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 200, 50);
        }
    }, new BrickInterface() {
        @Override
        public Color getColor() {
            return new Color(200, 50, 50);
        }
    }};

    public void paint(Graphics2D g, Polygon polygon) {
        Polygon[] triangles = getSegmentedTriangles(polygon);
        Vec[] noramls = getTriangleNoraml(triangles);
        int red = getColor().getRed();
        int green = getColor().getGreen();
        int blue = getColor().getBlue();
        for (int i = 0; i < triangles.length; i++) {
            double dot = Vec.dotProduct(Global.lightDirectionVec, noramls[i]);
            dot = ((dot + 1) / 4) + 0.5;
            g.setColor(new Color((int) (red * dot), (int) (green * dot), (int) (blue * dot)));
            g.fillPolygon(triangles[i]);
        }
    }

    public Vec getCenter(Polygon polygon) {
        int x = 0;
        for (int i : polygon.xpoints) {
            x += i;
        }
        int y = 0;
        for (int i : polygon.ypoints) {
            y += i;
        }
        x /= polygon.npoints;
        y /= polygon.npoints;
        return new Vec(x, y);
    }

    private Polygon[] getSegmentedTriangles(Polygon polygon) {
        Vec center = getCenter(polygon);
        Polygon[] triangles = new Polygon[polygon.npoints];
        for (int i = 0; i < polygon.npoints; i++) {
            int[] x = new int[]{polygon.xpoints[i % polygon.npoints], (int) center.x,
                    polygon.xpoints[(i + 1) % polygon.npoints]};
            int[] y = new int[]{polygon.ypoints[i % polygon.npoints], (int) center.y,
                    polygon.ypoints[(i + 1) % polygon.npoints]};
            triangles[i] = new Polygon(x, y, 3);
        }
        return triangles;
    }

    private Vec[] getTriangleNoraml(Polygon[] polygons) {

        Vec[] noramls = new Vec[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            int x = ((polygons[i].xpoints[0] + polygons[i].xpoints[2]) / 2) - polygons[i].xpoints[1];
            int y = ((polygons[i].ypoints[0] + polygons[i].ypoints[2]) / 2) - polygons[i].ypoints[1];
            noramls[i] = new Vec(x, y).normalize();
        }
        return noramls;
    }

    public Color getColor() {
        return new Color(255, 0, 0);
    }

    public void update(double dt, Polygon polygon) {
    }

    public void onHit(Polygon polygon, Vector<PowerUp> powerUps) {
    }
}
