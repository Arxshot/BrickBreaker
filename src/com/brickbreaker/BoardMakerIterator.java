package com.brickbreaker;

import java.awt.*;
import java.util.Vector;

// Segments the polygons into smaller polygons to create multiple bricks
public class BoardMakerIterator {
    Vector<Polygon> polygons = null;

    public BoardMakerIterator(Vector<Polygon> polygons) {
        this.polygons = polygons;
    }

    // Averages all the vertices of the polygon to find the middle point
    public Vec getCenter(int index) {
        int x = 0;
        for (int i = 0; i < polygons.get(index).npoints; i++) {
            x += polygons.get(index).xpoints[i];
        }
        int y = 0;
        for (int i = 0; i < polygons.get(index).npoints; i++) {
            y += polygons.get(index).ypoints[i];
        }
        x /= polygons.get(index).npoints;
        y /= polygons.get(index).npoints;
        return new Vec(x, y);
    }

    // Segments two adjacent vectors with the middle point and does this until it has
    // done it with all adjacent vertexes at least once.
    public BoardMakerIterator triangleSegmentation() {
        Vector<Polygon> newPolygons = new Vector<>();
        for (int i = 0; i < polygons.size(); i++) {
            Vec center = getCenter(i);
            for (int j = 0; j < polygons.get(i).npoints; j++) {
                int[] x = new int[3];
                x[0] = polygons.get(i).xpoints[(j + 1) % polygons.get(i).npoints];
                x[1] = (int) center.x;
                x[2] = polygons.get(i).xpoints[j % polygons.get(i).npoints];
                int[] y = new int[3];
                y[0] = polygons.get(i).ypoints[(j + 1) % polygons.get(i).npoints];
                y[1] = (int) center.y;
                y[2] = polygons.get(i).ypoints[j % polygons.get(i).npoints];
                newPolygons.add(new Polygon(x, y, 3));
            }
        }
        return new BoardMakerIterator(newPolygons);
    }

    // Segments two adjacent vectors with the middle point and does this until it has
    // done it with all adjacent vertexes at least once in a fan like motion.
    public BoardMakerIterator squareSegmentation() {
        Vector<Polygon> newPolygons = new Vector<>();
        for (int i = 0; i < polygons.size(); i++) {
            Vec center = getCenter(i);
            for (int j = 0; j < polygons.get(i).npoints; j++) {
                int[] x = new int[4];
                int[] y = new int[4];
                x[0] = polygons.get(i).xpoints[j % polygons.get(i).npoints];
                //System.out.println((j - 1) % polygons.get(i).npoints);
                x[1] = (polygons.get(i).xpoints[j % polygons.get(i).npoints] + polygons.get(i).xpoints[(j + polygons.get(i).npoints - 1) % polygons.get(i).npoints]) / 2;
                x[2] = (int) center.x;
                x[3] = (polygons.get(i).xpoints[j % polygons.get(i).npoints] + polygons.get(i).xpoints[(j + 1) % polygons.get(i).npoints]) / 2;
                y[0] = polygons.get(i).ypoints[j % polygons.get(i).npoints];
                y[1] = (polygons.get(i).ypoints[j % polygons.get(i).npoints] + polygons.get(i).ypoints[(j + polygons.get(i).npoints - 1) % polygons.get(i).npoints]) / 2;
                y[2] = (int) center.y;
                y[3] = (polygons.get(i).ypoints[j % polygons.get(i).npoints] + polygons.get(i).ypoints[(j + 1) % polygons.get(i).npoints]) / 2;
                newPolygons.add(new Polygon(x, y, 4));
            }
        }
        return new BoardMakerIterator(newPolygons);
    }

    // Creates a "flower" by segmenting two adjacent vectors with the middle point
    // and does this until it has done it with all adjacent vertexes at least once in
    // a fan like motion.
    public BoardMakerIterator flowerSegmentation() {
        Vector<Polygon> newPolygons = new Vector<>();
        for (int polygonIndex = 0; polygonIndex < polygons.size(); polygonIndex++) {
            int[] x = new int[polygons.get(polygonIndex).npoints];
            int[] y = new int[polygons.get(polygonIndex).npoints];
            // Find midpoints of sides
            for (int vertexIndex = 0; vertexIndex < polygons.get(polygonIndex).npoints; vertexIndex
                    ++) {
                x[vertexIndex] =
                        (polygons.get(polygonIndex).xpoints[vertexIndex % polygons.get(polygonIndex).npoints] + polygons.get(polygonIndex).xpoints[(vertexIndex + 1) % polygons.get(polygonIndex).npoints]) / 2;
                y[vertexIndex] =
                        (polygons.get(polygonIndex).ypoints[vertexIndex % polygons.get(polygonIndex).npoints] + polygons.get(polygonIndex).ypoints[(vertexIndex + 1) % polygons.get(polygonIndex).npoints]) / 2;
            }
            Polygon core = new Polygon(x, y, polygons.get(polygonIndex).npoints);
            newPolygons.add(core);
            // Using core and original corners, make new triangles around core
            for (int vertexIndex = 0; vertexIndex < polygons.get(polygonIndex).npoints; vertexIndex
                    ++) {
                x = new int[]{core.xpoints[vertexIndex],
                        polygons.get(polygonIndex).xpoints[(vertexIndex + 1) % polygons.get(polygonIndex).npoints],
                        core.xpoints[(vertexIndex + 1) % polygons.get(polygonIndex).npoints]};
                y = new int[]{core.ypoints[vertexIndex],
                        polygons.get(polygonIndex).ypoints[(vertexIndex + 1) % polygons.get(polygonIndex).npoints],
                        core.ypoints[(vertexIndex + 1) % polygons.get(polygonIndex).npoints]};
                newPolygons.add(new Polygon(x, y, 3));
            }
        }
        return new BoardMakerIterator(newPolygons);
    }

    public Vector<Polygon> getPolygons() {
        return polygons;
    }


    public Vector<Brick> getBricks(BrickInterface brickInterfaces) {
        return getBricks(new BrickInterface[]{brickInterfaces});
    }

    // Converts the brick interface into a set bricks to broken
    public Vector<Brick> getBricks(BrickInterface[] brickInterfaces) {
        Vector<Brick> bricks = new Vector(polygons.size());
        for (int i = 0; i < polygons.size(); i++) {
            bricks.add(new Brick(polygons.get(i), brickInterfaces));
        }
        return bricks;
    }

    // Converts the brick interface into a set bricks to broken
    public Vector<Brick> getBricks(BrickInterface[][] brickInterfaces) {
        Vector<Brick> bricks = new Vector(polygons.size());
        for (int i = 0; i < polygons.size(); i++) {
            bricks.add(new Brick(polygons.get(i), brickInterfaces[i % brickInterfaces.length]));
        }
        return bricks;
    }
}
