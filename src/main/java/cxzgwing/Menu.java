package cxzgwing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import cxzgwing.judgement.WindowMovable;
import cxzgwing.label.LabelAdaptor;
import cxzgwing.label.impl.BatteryLabel;
import cxzgwing.label.impl.CpuLabel;
import cxzgwing.label.impl.MemoryLabel;
import cxzgwing.label.impl.TimeLabel;
import cxzgwing.utils.AppUtil;
import cxzgwing.utils.LabelLayout;

public class Menu {
    private JFrame jFrame;
    private JPopupMenu jPopupMenu;
    private Font font;

    private Window window;
    private WindowMovable windowMovable;
    private CpuLabel cpuLabel;
    private MemoryLabel memoryLabel;
    private BatteryLabel batteryLabel;
    private TimeLabel timeLabel;

    private static final int WIDTH = 75;
    private static final int HEIGHT = 110;

    public Menu(Window window, WindowMovable windowMovable, CpuLabel cpuLabel,
            MemoryLabel memoryLabel, BatteryLabel batteryLabel, TimeLabel timeLabel) {
        this.window = window;
        this.windowMovable = windowMovable;
        this.cpuLabel = cpuLabel;
        this.memoryLabel = memoryLabel;
        this.batteryLabel = batteryLabel;
        this.timeLabel = timeLabel;
        this.font = new Font("宋体", Font.PLAIN, 13);

        init();
        addJPopupMenuListener();
    }

    private void hiddenFrameIfShowing() {
        if (jFrame.isShowing()) {
            jFrame.setVisible(false);
        }
    }

    private void hiddenJPopupMenuIfShowing() {
        if (jPopupMenu.isShowing()) {
            jPopupMenu.setVisible(false);
        }
    }

    private void addJPopupMenuListener() {
        jPopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                hiddenFrameIfShowing();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    private void init() {
        setJFrame();
        setJPopupMenu();
    }

    private void setJPopupMenu() {
        jPopupMenu = new JPopupMenu();
        jPopupMenu.setSize(WIDTH, HEIGHT);

        setMovableJMenu();
        setRefreshJMenu();
        setLayoutJMenu();
        setDisplayJMenu();
        setExitJMenu();
    }

    private void setExitJMenu() {
        JMenuItem exitJMenuItem = new JMenuItem("退出程序");
        exitJMenuItem.setFont(font);
        exitJMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(0);
            }
        });
        jPopupMenu.add(exitJMenuItem);
    }

    private void setDisplayJMenu() {
        JMenu displayJMenu = new JMenu("显示");
        displayJMenu.setFont(font);

        JCheckBox cpuJCheckBox = new JCheckBox("CPU");
        cpuJCheckBox.setFont(font);
        cpuJCheckBox.setSelected(true);
        cpuJCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                stateChange(cpuLabel, cpuJCheckBox.isSelected());
            }
        });

        JCheckBox memoryJCheckBox = new JCheckBox("内存");
        memoryJCheckBox.setFont(font);
        memoryJCheckBox.setSelected(true);
        memoryJCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                stateChange(memoryLabel, memoryJCheckBox.isSelected());
            }
        });

        JCheckBox batteryJCheckBox = new JCheckBox("电量");
        batteryJCheckBox.setFont(font);
        batteryJCheckBox.setSelected(true);
        batteryJCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                stateChange(batteryLabel, batteryJCheckBox.isSelected());
            }
        });

        JCheckBox timeJCheckBox = new JCheckBox("时间");
        timeJCheckBox.setFont(font);
        timeJCheckBox.setSelected(true);
        timeJCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                stateChange(timeLabel, timeJCheckBox.isSelected());
            }
        });

        displayJMenu.add(cpuJCheckBox);
        displayJMenu.add(memoryJCheckBox);
        displayJMenu.add(batteryJCheckBox);
        displayJMenu.add(timeJCheckBox);

        jPopupMenu.add(displayJMenu);
    }

    private void stateChange(LabelAdaptor label, boolean isSelected) {
        // 在复选框除按下鼠标，移除复选框区域后松开鼠标，此时复选框状态不变，无需操作
        if ((label.isDisplay() && isSelected) || (!label.isDisplay() && !isSelected)) {
            return;
        }
        label.setDisplay(isSelected);
        windowRemoveAll();
        reloadWindow();
        refreshWindow();
    }

    private void reloadWindow() {
        int count = getDisplayLabelCount();
        GridLayout gridLayout = updateWindowLabelLayout(count);
        reloadLabel(gridLayout);
    }

    private void reloadLabel(GridLayout gridLayout) {
        // 由于不显示标签时，不读取相关数据，所以重新显示时需要更新数据
        if (cpuLabel.isDisplay()) {
            cpuLabel.setText(AppUtil.getSystemCpuLoad());
            if (LabelLayout.DOUBLE == window.getLabelLayout() && !Objects.isNull(gridLayout)) {
                gridLayout.addLayoutComponent("cpuLabel", cpuLabel);
            }
            window.add(cpuLabel);
        }
        if (memoryLabel.isDisplay()) {
            memoryLabel.setText(AppUtil.getMemoryLoad());
            if (LabelLayout.DOUBLE == window.getLabelLayout() && !Objects.isNull(gridLayout)) {
                gridLayout.addLayoutComponent("memoryLabel", memoryLabel);
            }
            window.add(memoryLabel);
        }
        if (batteryLabel.isDisplay()) {
            batteryLabel.setText(AppUtil.getBatteryPercent());
            if (LabelLayout.DOUBLE == window.getLabelLayout() && !Objects.isNull(gridLayout)) {
                gridLayout.addLayoutComponent("batteryLabel", batteryLabel);
            }
            window.add(batteryLabel);
        }
        if (timeLabel.isDisplay()) {
            timeLabel.setText(AppUtil.getTime());
            if (LabelLayout.DOUBLE == window.getLabelLayout() && !Objects.isNull(gridLayout)) {
                gridLayout.addLayoutComponent("timeLabel", timeLabel);
            }
            window.add(timeLabel);
        }
    }

    private int getDisplayLabelCount() {
        int count = 0;
        if (cpuLabel.isDisplay()) {
            count++;
        }
        if (memoryLabel.isDisplay()) {
            count++;
        }
        if (batteryLabel.isDisplay()) {
            count++;
        }
        if (timeLabel.isDisplay()) {
            count++;
        }
        return count;
    }

    private GridLayout updateWindowLabelLayout(int count) {
        GridLayout gridLayout = null;
        if (LabelLayout.SINGLE == window.getLabelLayout()) {
            // 单列布局
            window.setSize(window.getWidth(), 20 * count);
        } else if (LabelLayout.DOUBLE == window.getLabelLayout()) {
            // 双列布局
            switch (count) {
                case 1:
                    window.setLayout(gridLayout = new GridLayout(1, 1));
                    window.setSize(75, 20);
                    break;
                case 2:
                    window.setLayout(gridLayout = new GridLayout(1, 2));
                    window.setSize(150, 20);
                    break;
                case 3:
                case 4:
                    window.setLayout(gridLayout = new GridLayout(2, 2));
                    window.setSize(150, 40);
                    break;
                default:
                    window.setLayout(gridLayout = new GridLayout(2, 2));
                    window.setSize(150, 40);
            }
        }
        AppUtil.initWindowLocation(window);
        return gridLayout;
    }

    private void windowRemoveAll() {
        window.remove(cpuLabel);
        window.remove(memoryLabel);
        window.remove(batteryLabel);
        window.remove(timeLabel);
    }

    private void setLayoutJMenu() {
        JMenu layoutJMenu = new JMenu("布局");
        layoutJMenu.setFont(font);
        JMenuItem singleJMenuItem = new JMenuItem("单列布局");
        singleJMenuItem.setFont(font);
        singleJMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                window.setLabelLayout(LabelLayout.SINGLE);
                window.setSize(85, 80);
                window.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 1));
                reloadWindow();
                AppUtil.initWindowLocation(window);
                refreshWindow();
            }
        });
        JMenuItem doubleJMenuItem = new JMenuItem("双列布局");
        doubleJMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                window.setLabelLayout(LabelLayout.DOUBLE);
                window.setSize(150, 40);
                window.setLayout(new GridLayout(2, 2));
                reloadWindow();
                AppUtil.initWindowLocation(window);
                refreshWindow();
            }
        });
        doubleJMenuItem.setFont(font);
        layoutJMenu.add(singleJMenuItem);
        layoutJMenu.add(doubleJMenuItem);
        jPopupMenu.add(layoutJMenu);
    }

    private void setRefreshJMenu() {
        JMenuItem refreshJMenuItem = new JMenuItem("刷新");
        refreshJMenuItem.setFont(font);
        refreshJMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                refreshWindow();
            }
        });
        jPopupMenu.add(refreshJMenuItem);
    }

    private void refreshWindow() {
        window.setVisible(false);
        window.setVisible(true);
    }

    private void setMovableJMenu() {
        JMenuItem movableJMenuItem = new JMenuItem("移动");
        movableJMenuItem.setFont(font);
        movableJMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                windowMovable.setValue(!windowMovable.getValue());
                if (windowMovable.isTrue()) {
                    movableJMenuItem.setText("固定");
                } else {
                    movableJMenuItem.setText("移动");
                }
            }
        });
        jPopupMenu.add(movableJMenuItem);
    }

    private void setJFrame() {
        this.jFrame = new JFrame();
        // 设置无边框
        jFrame.setUndecorated(true);
        // 隐藏任务栏图标
        jFrame.setType(JFrame.Type.UTILITY);
        // 设置窗口置顶
        jFrame.setAlwaysOnTop(true);
        // 设置背景颜色
        jFrame.setBackground(new Color(255, 255, 255, 255));
        // 设置窗体大小
        jFrame.setSize(WIDTH, HEIGHT);
        // 设置布局
        jFrame.setLayout(new GridLayout(5, 1));

    }

    public void display(MouseEvent mouseEvent) {
        hiddenFrameIfShowing();
        hiddenJPopupMenuIfShowing();
        jFrame.setLocation(mouseEvent.getX() - WIDTH, mouseEvent.getY() - HEIGHT);
        jFrame.setVisible(true);
        jPopupMenu.show(jFrame, 0, 0);
        // System.out.println("Width=" + jPopupMenu.getWidth() + "Height=" +
        // jPopupMenu.getHeight());
    }

}
