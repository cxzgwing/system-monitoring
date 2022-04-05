package cxzgwing.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cxzgwing.Window;
import cxzgwing.judgement.WindowMovable;
import cxzgwing.model.Properties;
import cxzgwing.utils.AppUtil;

public class FrameDragListener extends MouseAdapter {
    private static final Logger logger = LoggerFactory.getLogger(FrameDragListener.class);

    private final Window window;
    private WindowMovable windowMovable;
    private Properties properties;
    private Point mouseDownCompCoords = null;

    public FrameDragListener(Window window, WindowMovable windowMovable, Properties properties) {
        this.window = window;
        this.windowMovable = windowMovable;
        this.properties = properties;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
        if (this.windowMovable.isTrue()
                && (properties.getX() != window.getX() || properties.getY() != window.getY())) {
            properties.setX(window.getX());
            properties.setY(window.getY());
            try {
                AppUtil.writeFile(AppUtil.getPropertiesPath(), properties, false);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("mouseReleased error", ex);
            }
        }
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
