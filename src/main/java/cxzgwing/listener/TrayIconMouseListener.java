package cxzgwing.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import cxzgwing.Menu;

/**
 * 任务栏图标鼠标监听
 */
public class TrayIconMouseListener extends MouseAdapter {
    private Menu menu;

    public TrayIconMouseListener(Menu menu) {
        this.menu = menu;
    }

    // 仅监听鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            // 托盘图标被鼠标右键被点击
            case MouseEvent.BUTTON3: {
                menu.display(e);
                break;
            }
            default: {
                break;
            }
        }
    }
}
