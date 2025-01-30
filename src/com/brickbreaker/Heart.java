package com.brickbreaker;

import java.awt.*;

// The generated heart shape life indicator
public class Heart implements Paintable {
    private Vec pos;
    private double size;
    private Polygon polygon;

    // Constructor
    public Heart(Vec pos, double size) {
        this.pos = pos;
        this.size = size;
        int[] x = new int[8];
        int[] y = new int[8];
        x[0] = (int) (0 * size);
        x[1] = (int) (1 * size);
        x[2] = (int) (2 * size);
        x[3] = (int) (1.6 * size);
        x[4] = (int) (0 * size);/////
        x[5] = (int) (-1.6 * size);
        x[6] = (int) (-2 * size);
        x[7] = (int) (-1 * size);
        y[0] = (int) (-0.75 * size);
        y[1] = (int) (-1.5 * size);
        y[2] = (int) (-0.5 * size);
        y[3] = (int) (1 * size);
        y[4] = (int) (2 * size);
        y[5] = (int) (1 * size);
        y[6] = (int) (-0.5 * size);
        y[7] = (int) (-1.5 * size);
        this.polygon = new Polygon(x, y, 8);
        polygon.translate((int) pos.x, (int) pos.y);
    }

    public Color getColor() {
        return new Color(255, 50, 50);
    }

    @Override
    public void paint(Graphics2D g) {
        Polygon[] triagnles = getSegmentedTriangles(polygon);
        Vec[] noramls = getTriangleNoraml(triagnles);
        int red = getColor().getRed();
        int green = getColor().getGreen();
        int blue = getColor().getBlue();
        for (int i = 0; i < triagnles.length; i++) {
            double dot = Vec.dotProduct(Global.lightDirectionVec, noramls[i]);
            dot = ((dot + 1) / 4) + 0.5;
            g.setColor(new Color((int) (red * dot), (int) (green * dot), (int) (blue * dot)));
            g.fillPolygon(triagnles[i]);
        }
    }

    // Returns the center point of the heart polygon
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

    // Converts a polygon into sub-triangles
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

    // Returns the normals for all the polygons.
    private Vec[] getTriangleNoraml(Polygon[] polygons) {
        Vec[] noramls = new Vec[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            int x = ((polygons[i].xpoints[0] + polygons[i].xpoints[2]) / 2) - polygons[i].xpoints[1];
            int y = ((polygons[i].ypoints[0] + polygons[i].ypoints[2]) / 2) - polygons[i].ypoints[1];
            noramls[i] = new Vec(x, y).normalize();
        }
        return noramls;
    }
}
