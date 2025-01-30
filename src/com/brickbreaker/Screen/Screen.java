package com.brickbreaker.Screen;

import com.brickbreaker.Paintable;
import com.brickbreaker.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Screen is the base class for all the rendered Screens. It handles updates,
// paints, input handling and collision bound updates.
// BrickBreakerScreen - the main game screen where the game runs
// GameOverScreen - screen that shows the final score and returns you back to the start screen
// PauseScreen - pause game after which you can return to the game or start screen
// StartScreen - screen to show (p)lay or (q)uit or (r)esume
// TestScreen - Used for debugging ray tracing and looks nice
public abstract class Screen implements Updatable, Activatable, Deactivable, Paintable, Inputs {
    protected HashMap objects;
    protected Main main;
    protected Vector<Paintable> paintables = new Vector<>();
    protected Vector<Updatable> updatables = new Vector<>();
    protected Vector<Collider> colliders = new Vector<>();
    protected Vector<Collider> movingColliders = new Vector<>();// thing that move and will Collide with things
    protected boolean newEventsOnly = false;
    protected ComponentListener componentListener = Global.Inputs_NULL;
    protected FocusListener focusListener = Global.Inputs_NULL;
    protected HierarchyListener hierarchyListener = Global.Inputs_NULL;
    protected HierarchyBoundsListener hierarchyBoundsListener = Global.Inputs_NULL;
    protected KeyListener keyListener = Global.Inputs_NULL;
    protected MouseListener mouseListener = Global.Inputs_NULL;
    protected MouseMotionListener mouseMotionListener = Global.Inputs_NULL;
    protected MouseWheelListener mouseWheelListener = Global.Inputs_NULL;
    protected InputMethodListener inputMethodListener = Global.Inputs_NULL;
    protected WindowFocusListener windowFocusListener = Global.Inputs_NULL;
    protected WindowListener windowListener = Global.Inputs_NULL;

    @Override
    public void paint(Graphics2D g) {
        for (int i = 0; i < paintables.size(); i++) {
            paintables.get(i).paint(g);
        }
    }

    @Override
    public void update(double dt) {
        for (Collider collider : movingColliders) {
            collider.updateBounds();
        }
        for (int i = 0; i < updatables.size(); i++) {
            updatables.get(i).update(dt);
        }
    }

    // Activates this screen and plugs in all the listeners for events
    @Override
    public void activate() {
        if (main != null) {
            main.addComponentListener(this);
            main.addFocusListener(this);
            main.addHierarchyListener(this);
            main.addHierarchyBoundsListener(this);
            main.addKeyListener(this);
            main.addMouseListener(this);
            main.addMouseMotionListener(this);
            main.addMouseWheelListener(this);
            main.addInputMethodListener(this);
            main.addWindowFocusListener(this);
            main.addWindowListener(this);
        }
        updateColliders();
    }

    public void updateColliders() {
        for (Collider collider : colliders) {
            collider.updateBounds();
        }
    }

    // Deactivates this screen and removes all the listeners for events
    @Override
    public void deactivate() {
        if (main != null) {
            main.removeComponentListener(this);
            main.removeFocusListener(this);
            main.removeHierarchyListener(this);
            main.removeHierarchyBoundsListener(this);
            main.removeKeyListener(this);
            main.removeMouseListener(this);
            main.removeMouseMotionListener(this);
            main.removeMouseWheelListener(this);
            main.removeInputMethodListener(this);
            main.removeWindowFocusListener(this);
            main.removeWindowListener(this);
        }
    }

    /// ///////////////////////////////////////////////
    /// ///
    /// ////// code beyond this point is not my own
    /// //////
    /// ////// it is code from Window class with
    /// //////
    /// ////// small tweaks
    /// //////
    /// ///////////////////////////////////////////////
    /// ///
    protected <T extends ComponentListener> void addComponentListener(ComponentListener l) {
        if (l == null) {
            return;
        }
        componentListener = AWTEventMulticaster.add(componentListener, l);
        newEventsOnly = true;
    }

    protected void removeComponentListener(ComponentListener l) {
        if (l == null) {
            return;
        }
        componentListener = AWTEventMulticaster.remove(componentListener, l);
    }

    protected void addFocusListener(FocusListener l) {
        if (l == null) {
            return;
        }
        focusListener = AWTEventMulticaster.add(focusListener, l);
        newEventsOnly = true;
    }

    protected void removeFocusListener(FocusListener l) {
        if (l == null) {
            return;
        }
        focusListener = AWTEventMulticaster.remove(focusListener, l);
    }

    protected void addKeyListener(Object l) {
        if (l == null) {
            return;
        }
        keyListener = AWTEventMulticaster.add(keyListener, (KeyListener) l);
        newEventsOnly = true;
    }

    protected void removeKeyListener(Object l) {
        if (l == null) {
            return;
        }
        keyListener = AWTEventMulticaster.remove(keyListener, (KeyListener) l);
    }

    protected void addMouseListener(Object l) {
        if (l == null) {
            return;
        }
        mouseListener = AWTEventMulticaster.add(mouseListener, (MouseListener) l);
        newEventsOnly = true;
    }

    protected void removeMouseListener(Object l) {
        if (l == null) {
            return;
        }
        mouseListener = AWTEventMulticaster.remove(mouseListener, (MouseListener) l);
    }

    protected void addMouseMotionListener(MouseMotionListener l) {
        if (l == null) {
            return;
        }
        mouseMotionListener = AWTEventMulticaster.add(mouseMotionListener, l);
        newEventsOnly = true;
    }

    protected void removeMouseMotionListener(MouseMotionListener l) {
        if (l == null) {
            return;
        }
        mouseMotionListener = AWTEventMulticaster.remove(mouseMotionListener, l);
    }

    protected void addMouseWheelListener(MouseWheelListener l) {
        if (l == null) {
            return;
        }
        mouseWheelListener = AWTEventMulticaster.add(mouseWheelListener, l);
        newEventsOnly = true;
    }

    protected void removeMouseWheelListener(MouseWheelListener l) {
        if (l == null) {
            return;
        }
        mouseWheelListener = AWTEventMulticaster.remove(mouseWheelListener, l);
    }

    protected void addInputMethodListener(InputMethodListener l) {
        if (l == null) {
            return;
        }
        inputMethodListener = AWTEventMulticaster.add(inputMethodListener, l);
        newEventsOnly = true;
    }

    protected void removeInputMethodListener(InputMethodListener l) {
        if (l == null) {
            return;
        }
        inputMethodListener = AWTEventMulticaster.remove(inputMethodListener, l);
    }

    /// //////////////////////////////
    /// //////////////////////////////
    /// //////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        //actionListener.actionPerformed(e);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        //adjustmentListener.
        adjustmentValueChanged(e);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        componentListener.componentResized(e);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        componentListener.componentMoved(e);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        componentListener.componentShown(e);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        componentListener.componentHidden(e);
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        //containerListener.componentAdded(e);
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        //containerListener.componentRemoved(e);
    }

    @Override
    public void focusGained(FocusEvent e) {
        focusListener.focusGained(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        focusListener.focusLost(e);
    }

    @Override
    public void ancestorMoved(HierarchyEvent e) {
        hierarchyBoundsListener.ancestorMoved(e);
    }

    @Override
    public void ancestorResized(HierarchyEvent e) {
        hierarchyBoundsListener.ancestorResized(e);
    }

    @Override
    public void hierarchyChanged(HierarchyEvent e) {
        hierarchyListener.hierarchyChanged(e);
    }

    @Override
    public void inputMethodTextChanged(InputMethodEvent event) {
        inputMethodListener.inputMethodTextChanged(event);
    }

    @Override
    public void caretPositionChanged(InputMethodEvent event) {
        inputMethodListener.caretPositionChanged(event);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        //itemListener.itemStateChanged(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyListener.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyListener.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyListener.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseListener.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseListener.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseListener.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseListener.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseListener.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMotionListener.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseMotionListener.mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelListener.mouseWheelMoved(e);
    }

    @Override
    public void textValueChanged(TextEvent e) {
        //InputMethodListener
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        windowFocusListener.windowGainedFocus(e);
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        windowFocusListener.windowLostFocus(e);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        windowListener.windowOpened(e);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        windowListener.windowClosing(e);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        windowListener.windowClosed(e);
    }

    @Override
    public void windowIconified(WindowEvent e) {
        windowListener.windowIconified(e);
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        windowListener.windowDeiconified(e);
    }

    @Override
    public void windowActivated(WindowEvent e) {
        windowListener.windowActivated(e);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        windowListener.windowDeactivated(e);
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        //windowListener.windowStateChanged(e);
    }
}
