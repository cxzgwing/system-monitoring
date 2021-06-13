package cxzgwing.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * 任务栏图标鼠标监听
 */
public class TrayIconMouseListener extends MouseAdapter {
    private JPopupMenu jPopupMenu;
    private int jPopupMenuWidth;
    private int jPopupMenuHeight;

    public TrayIconMouseListener(JPopupMenu jPopupMenu, int jPopupMenuWidth, int jPopupMenuHeight) {
        this.jPopupMenu = jPopupMenu;
        this.jPopupMenuWidth = jPopupMenuWidth;
        this.jPopupMenuHeight = jPopupMenuHeight;
    }

    // 仅监听鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            // 托盘图标被鼠标右键被点击
            case MouseEvent.BUTTON3: {
                jPopupMenu.setLocation(e.getX() - jPopupMenuWidth, e.getY() - jPopupMenuHeight);
                jPopupMenu.setVisible(true);
                break;
            }
            default: {
                break;
            }
        }
    }
}
