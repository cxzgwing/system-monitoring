package cxzgwing.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import cxzgwing.judgement.MouseClicked;
import cxzgwing.judgement.MouseOnJPopupMenu;
import cxzgwing.judgement.WindowMovable;

public class FrameDragListener extends MouseAdapter {

    private final JFrame frame;
    private WindowMovable windowMovable;
    private Point mouseDownCompCoords = null;

    private MouseOnJPopupMenu mouseOnJPopupMenu;
    private MouseClicked mouseClicked;

    // 窗体位置最大横坐标
    private int windowMaxX;
    // 窗体位置最大纵坐标
    private int windowMaxY;

    public FrameDragListener(JFrame frame, WindowMovable windowMovable,
            MouseOnJPopupMenu mouseOnJPopupMenu, MouseClicked mouseClicked, int windowMaxX,
            int windowMaxY) {
        this.frame = frame;
        this.windowMovable = windowMovable;
        this.mouseOnJPopupMenu = mouseOnJPopupMenu;
        this.mouseClicked = mouseClicked;
        this.windowMaxX = windowMaxX;
        this.windowMaxY = windowMaxY;
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
        if (x > windowMaxX) {
            x = windowMaxX;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > windowMaxY) {
            y = windowMaxY;
        }
        frame.setLocation(x, y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (windowMovable.isTrue()) {
            frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (windowMovable.isTrue()) {
            frame.setCursor(Cursor.getDefaultCursor());
        }
    }

}
