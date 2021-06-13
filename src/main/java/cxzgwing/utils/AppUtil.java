package cxzgwing.utils;

import cxzgwing.judgement.MouseClicked;
import cxzgwing.judgement.MouseOnJPopupMenu;

public class AppUtil {

    /**
     * 初始化默认状态：鼠标未悬浮于菜单之上、鼠标未点击
     */
    public static void initDefaultStatus(MouseOnJPopupMenu mouseOnJPopupMenu,
            MouseClicked mouseClicked) {
        mouseOnJPopupMenu.setValue(false);
        mouseClicked.setValue(false);
    }

}
