package cxzgwing.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import cxzgwing.Window;
import cxzgwing.judgement.WindowMovable;

public class FrameDragListener extends MouseAdapter {

    private final Window window;
    private WindowMovable windowMovable;
    private Point mouseDownCompCoords = null;

    public FrameDragListener(Window window, WindowMovable windowMovable) {
        this.window = window;
        this.windowMovable = windowMovable;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!windowMovable.isTrue()) {
            mouseDownCompCoords = null;
            return;
        }
        Point currCoords = e.getLocationOnScreen();
        int x = currCoords.x - mouseDownCompCoords.x;
        int y = currCoords.y - mouseDownCompCoords.y;
        if (x < 0) {
            x = 0;
        }
        int windowMaxX = window.getWindowMaxX();
        if (x > windowMaxX) {
            x = windowMaxX;
        }
        if (y < 0) {
            y = 0;
        }
        int windowMaxY = window.getWindowMaxY();
        if (y > windowMaxY) {
            y = windowMaxY;
        }
        window.setLocation(x, y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (windowMovable.isTrue()) {
            window.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (windowMovable.isTrue()) {
            window.setCursor(Cursor.getDefaultCursor());
        }
    }

}
