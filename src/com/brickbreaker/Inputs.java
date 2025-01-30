package com.brickbreaker;

import java.awt.event.*;

// Wrapper Component Input interface for all possible Input listener interfaces.
public interface Inputs extends ComponentListener, ContainerListener, FocusListener, KeyListener, MouseListener,
        MouseMotionListener, WindowListener, WindowFocusListener, WindowStateListener, ActionListener, ItemListener,
        AdjustmentListener, TextListener, InputMethodListener, HierarchyListener, HierarchyBoundsListener,
        MouseWheelListener {
}
