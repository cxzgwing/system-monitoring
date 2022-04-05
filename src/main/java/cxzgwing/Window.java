package cxzgwing;

import java.awt.*;
import java.time.Clock;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cxzgwing.judgement.WindowMovable;
import cxzgwing.label.impl.BatteryLabel;
import cxzgwing.label.impl.CpuLabel;
import cxzgwing.label.impl.MemoryLabel;
import cxzgwing.label.impl.TimeLabel;
import cxzgwing.listener.FrameDragListener;
import cxzgwing.listener.ShutdownListener;
import cxzgwing.listener.TrayIconMouseListener;
import cxzgwing.model.Properties;
import cxzgwing.task.BatteryMonitorTask;
import cxzgwing.task.CpuMemoryTimeMonitorTask;
import cxzgwing.task.GCTask;
import cxzgwing.utils.AppUtil;
import cxzgwing.utils.ThreadUtil;

public class Window extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    private Menu menu;
    private WindowMovable windowMovable;
    private CpuLabel cpuLabel;
    private MemoryLabel memoryLabel;
    private BatteryLabel batteryLabel;
    private TimeLabel timeLabel;
    private Font font;
    private EmptyBorder emptyBorder;
    private GridLayout gridLayout;

    // 窗体位置最大横坐标
    private int windowMaxX;
    // 窗体位置最大纵坐标
    private int windowMaxY;

    private Properties properties;

    public Window(Properties properties) {
        logger.info("system-monitoring running...");
        Runtime.getRuntime().addShutdownHook(new ShutdownListener());

        this.properties = properties;

        // 初始化窗体是否可移动
        windowMovable = new WindowMovable(false);
        font = new Font("黑体", Font.PLAIN, 16);

        // 设置显示标签边距
        emptyBorder = new EmptyBorder(0, 5, 0, 0);

        // 初始化布局和窗体
        initLayoutAndWindow();

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

    private void initLayoutAndWindow() {
        int count = 0;
        for (Map.Entry<String, Boolean> entry : properties.getDisplayMap().entrySet()) {
            if (entry.getValue()) {
                count++;
            }
        }
        this.gridLayout = AppUtil.initLayout(count, properties, this);
        // 隐藏任务栏图标
        this.setType(JFrame.Type.UTILITY);
        // 设置窗口置顶
        this.setAlwaysOnTop(true);
        // 设置无边框
        this.setUndecorated(true);
        // 设置背景色
        this.setBackground(new Color(0, 0, 0, 80));
        // 设置窗体位置
        AppUtil.initWindowLocation(this, properties);
    }

    private void initMenu() {
        menu = new Menu(this, windowMovable, cpuLabel, memoryLabel, batteryLabel, timeLabel,
                properties);
    }

    private void setWindowDragListener() {
        // 设置鼠标监听，可通过鼠标移动窗体位置
        // 通过系统托盘菜单中的第一个按钮（移动 / 固定）来控制是否能移动窗体
        FrameDragListener frameDragListener =
                new FrameDragListener(this, windowMovable, properties);
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
        if (properties.isTimeDisplay()) {
            this.add(timeLabel);
        } else {
            timeLabel.setDisplay(false);
        }
    }

    private void initBatteryJLabel() {
        // 电池电量百分比显示标签
        batteryLabel = new BatteryLabel(AppUtil.getBatteryPercent());
        batteryLabel.setFont(font);
        batteryLabel.setForeground(Color.WHITE);
        batteryLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("batteryLabel", batteryLabel);
        if (properties.isBatteryDisplay()) {
            this.add(batteryLabel);
        } else {
            batteryLabel.setDisplay(false);
        }
    }

    private void initMemJLabel() {
        // 内存使用率显示标签
        memoryLabel = new MemoryLabel(AppUtil.getMemoryLoad());
        memoryLabel.setFont(font);
        memoryLabel.setForeground(Color.WHITE);
        memoryLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("memoryLabel", memoryLabel);
        if (properties.isMemoryDisplay()) {
            this.add(memoryLabel);
        } else {
            memoryLabel.setDisplay(false);
        }
    }

    private void initCpuJLabel() {
        // CPU使用率显示标签
        cpuLabel = new CpuLabel(AppUtil.getSystemCpuLoad());
        cpuLabel.setFont(font);
        cpuLabel.setForeground(Color.WHITE);
        cpuLabel.setBorder(emptyBorder);
        gridLayout.addLayoutComponent("cpuLabel", cpuLabel);
        if (properties.isCpuDisplay()) {
            this.add(cpuLabel);
        } else {
            cpuLabel.setDisplay(false);
        }
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
    private void setSystemTray() {
        if (SystemTray.isSupported()) {
            // 获取当前平台的系统托盘
            SystemTray systemTray = SystemTray.getSystemTray();

            // 加载一个图片用于托盘图标的显示
            Image image = Toolkit.getDefaultToolkit()
                    .getImage(Clock.class.getResource("/images/system-monitoring-icon.png"));

            // 创建点击图标时的弹出菜单方案三：JFrame + JPopupMenu
            TrayIcon trayIcon = new TrayIcon(image, "System Monitoring");
            trayIcon.addMouseListener(new TrayIconMouseListener(menu));

            // 托盘图标自适应尺寸
            trayIcon.setImageAutoSize(true);

            // 添加托盘图标到系统托盘
            try {
                systemTray.add(trayIcon);
            } catch (Exception e) {
                logger.error("systemTray add trayIcon error, system exit", e);
            }
        } else {
            logger.error("setSystemTray error, 当前系统不支持系统托盘, system exit");
            JOptionPane.showMessageDialog(null, "当前系统不支持系统托盘", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
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

}
