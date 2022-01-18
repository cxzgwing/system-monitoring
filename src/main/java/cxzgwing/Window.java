package cxzgwing;

import java.awt.*;
import java.time.Clock;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import cxzgwing.judgement.WindowMovable;
import cxzgwing.label.impl.BatteryLabel;
import cxzgwing.label.impl.CpuLabel;
import cxzgwing.label.impl.MemoryLabel;
import cxzgwing.label.impl.TimeLabel;
import cxzgwing.listener.FrameDragListener;
import cxzgwing.listener.TrayIconMouseListener;
import cxzgwing.task.BatteryMonitorTask;
import cxzgwing.task.CpuMemoryTimeMonitorTask;
import cxzgwing.task.GCTask;
import cxzgwing.utils.AppUtil;
import cxzgwing.utils.LabelLayout;
import cxzgwing.utils.ThreadUtil;

public class Window extends JFrame {
    private Menu menu;
    private WindowMovable windowMovable;
    private CpuLabel cpuLabel;
    private MemoryLabel memoryLabel;
    private BatteryLabel batteryLabel;
    private TimeLabel timeLabel;
    private Font font;
    private EmptyBorder emptyBorder;
    private GridLayout gridLayout;
    // 1-单列布局，2-双列布局
    private int labelLayout;

    // 窗体位置最大横坐标
    private int windowMaxX;
    // 窗体位置最大纵坐标
    private int windowMaxY;

    public Window() throws Exception {
        // 初始化窗体是否可移动
        windowMovable = new WindowMovable(false);
        font = new Font("黑体", Font.PLAIN, 16);
        labelLayout = LabelLayout.DOUBLE;

        // 设置显示标签边距
        emptyBorder = new EmptyBorder(0, 5, 0, 0);
        // 设置布局
        gridLayout = new GridLayout(2, 2);

        // 初始化窗体
        initWindow();

        // 设置CPU使用率显示标签
        initCpuJLabel();

        // 内存使用率显示标签
        initMemJLabel();

        // 电池电量百分比显示标签
        initBatteryJLabel();

        // 时间显示标签
        initTimeJLabel();

        // 初始化托盘菜单
        initMenu();

        // 设置窗体是否可移动监听
        setWindowDragListener();

        // 添加窗体状态监听，使窗体一直显示
        setWindowAlwaysShow();

        // 添加系统托盘
        setSystemTray();

        // 显示
        this.setVisible(true);

        // 监控
        monitoring();
    }

    private void initMenu() {
        menu = new Menu(this, windowMovable, cpuLabel, memoryLabel, batteryLabel, timeLabel);
    }

    /**
     * 初始化窗体，显示CPU使用率和Mem使用率
     */
    private void initWindow() {
        // 隐藏任务栏图标
        this.setType(JFrame.Type.UTILITY);
        // 设置窗口置顶
        this.setAlwaysOnTop(true);
        // 设置无边框
        this.setUndecorated(true);
        // 设置背景色
        this.setBackground(new Color(0, 0, 0, 80));
        // 设置窗体大小
        this.setSize(150, 40);
        // 设置布局
        this.setLayout(gridLayout);

        AppUtil.initWindowLocation(this);
    }

    private void setWindowDragListener() {
        // 设置鼠标监听，可通过鼠标移动窗体位置
        // 通过系统托盘菜单中的第一个按钮（移动 / 固定）来控制是否能移动窗体
        FrameDragListener frameDragListener = new FrameDragListener(this, windowMovable);
        this.addMouseListener(frameDragListener);
        this.addMouseMotionListener(frameDragListener);
    }

    private void initTimeJLabel() {
        // 时间显示标签
        timeLabel = new TimeLabel(AppUtil.getTime());
        timeLabel.setFont(font);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("timeLabel", timeLabel);
        this.add(timeLabel);
    }

    private void initBatteryJLabel() {
        // 电池电量百分比显示标签
        batteryLabel = new BatteryLabel(AppUtil.getBatteryPercent());
        batteryLabel.setFont(font);
        batteryLabel.setForeground(Color.WHITE);
        batteryLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("batteryLabel", batteryLabel);
        this.add(batteryLabel);
    }

    private void initMemJLabel() {
        // 内存使用率显示标签
        memoryLabel = new MemoryLabel(AppUtil.getMemoryLoad());
        memoryLabel.setFont(font);
        memoryLabel.setForeground(Color.WHITE);
        memoryLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("memoryLabel", memoryLabel);
        this.add(memoryLabel);
    }

    private void initCpuJLabel() {
        // CPU使用率显示标签
        cpuLabel = new CpuLabel(AppUtil.getSystemCpuLoad());
        cpuLabel.setFont(font);
        cpuLabel.setForeground(Color.WHITE);
        cpuLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("cpuLabel", cpuLabel);
        this.add(cpuLabel);
    }

    /**
     * 监控
     */
    private void monitoring() {
        ThreadUtil.addTask(new CpuMemoryTimeMonitorTask(cpuLabel, memoryLabel, timeLabel));
        ThreadUtil.addTask(new BatteryMonitorTask(batteryLabel));
        ThreadUtil.addTask(new GCTask());
    }

    /**
     * 添加系统托盘
     */
    private void setSystemTray() throws Exception {
        try {
            if (SystemTray.isSupported()) {
                // 获取当前平台的系统托盘
                SystemTray tray = SystemTray.getSystemTray();

                // 加载一个图片用于托盘图标的显示
                Image image = Toolkit.getDefaultToolkit()
                        .getImage(Clock.class.getResource("/images/system-monitoring-icon.png"));

                // 创建点击图标时的弹出菜单方案三：JFrame + JPopupMenu
                TrayIcon trayIcon = new TrayIcon(image, "System Monitoring");
                trayIcon.addMouseListener(new TrayIconMouseListener(menu));

                // 托盘图标自适应尺寸
                trayIcon.setImageAutoSize(true);

                // 添加托盘图标到系统托盘
                tray.add(trayIcon);

            } else {
                throw new Exception("当前系统不支持系统托盘");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 添加窗体状态监听，使窗体一直显示
     */
    private void setWindowAlwaysShow() {
        this.addWindowStateListener(e -> {
            int newState = e.getNewState();
            if (JFrame.ICONIFIED == newState) {
                this.setState(JFrame.NORMAL);
            }
        });
    }

    public void setWindowMaxX(int windowMaxX) {
        this.windowMaxX = windowMaxX;
    }

    public void setWindowMaxY(int windowMaxY) {
        this.windowMaxY = windowMaxY;
    }

    public int getWindowMaxX() {
        return this.windowMaxX;
    }

    public int getWindowMaxY() {
        return this.windowMaxY;
    }

    public int getLabelLayout() {
        return labelLayout;
    }

    public void setLabelLayout(int labelLayout) {
        this.labelLayout = labelLayout;
    }

}
