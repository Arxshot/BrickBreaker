package com.brickbreaker;

import java.awt.*;
import java.awt.event.*;

// Holder of Global references and general useful functions.
public class Global {
    public static final Vec lightDirectionVec = new Vec(0.15, -1).normalize();
    public static final ActionListener ActionListener_NULL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // it's Null nothing happens
        }
    };

    public static final Updatable UPDATABLE___NULL = new Updatable() {
        @Override
        public void update(double dt) {
            // it's Null nothing happens
        }
    };

    // Handles the lighting for all the brick polygons, power ups and etc.
    public static void fillPoylgon(Graphics2D g, Polygon polygon, Color color) {
        Polygon[] triagnles = getSegmentedTriangles(polygon);
        Vec[] noramls = getTriangleNoraml(triagnles);
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        for (int i = 0; i < triagnles.length; i++) {
            double dot = Vec.dotProduct(Global.lightDirectionVec, noramls[i]);
            dot = ((dot + 1) / 4) + 0.5;
            g.setColor(new Color((int) (red * dot), (int) (green * dot), (int) (blue * dot)));
            g.fillPolygon(triagnles[i]);
        }
    }

    // Returns the center point for a polygon
    public static Vec getCenter(Polygon polygon) {
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
    private static Polygon[] getSegmentedTriangles(Polygon polygon) {
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
    private static Vec[] getTriangleNoraml(Polygon[] polygons) {
        Vec[] normals = new Vec[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            int x = ((polygons[i].xpoints[0] + polygons[i].xpoints[2]) / 2) - polygons[i].xpoints[1];
            int y = ((polygons[i].ypoints[0] + polygons[i].ypoints[2]) / 2) - polygons[i].ypoints[1];

            normals[i] = new Vec(x, y).normalize();
        }
        return normals;
    }

    public static final Inputs Inputs_NULL = new Inputs() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public void componentAdded(ContainerEvent e) {
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
        }

        @Override
        public void ancestorMoved(HierarchyEvent e) {
        }

        @Override
        public void ancestorResized(HierarchyEvent e) {
        }

        @Override
        public void hierarchyChanged(HierarchyEvent e) {
        }

        @Override
        public void inputMethodTextChanged(InputMethodEvent event) {
        }

        @Override
        public void caretPositionChanged(InputMethodEvent event) {
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }

        @Override
        public void textValueChanged(TextEvent e) {
        }

        @Override
        public void windowGainedFocus(WindowEvent e) {
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        @Override
        public void windowStateChanged(WindowEvent e) {
        }
    };
}
