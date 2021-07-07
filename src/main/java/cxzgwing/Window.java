package cxzgwing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.management.ManagementFactory;
import java.time.Clock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import com.sun.management.OperatingSystemMXBean;

import cxzgwing.judgement.MouseClicked;
import cxzgwing.judgement.MouseOnJPopupMenu;
import cxzgwing.judgement.WindowMovable;
import cxzgwing.listener.FrameDragListener;
import cxzgwing.listener.TrayIconMouseListener;
import cxzgwing.utils.AppUtil;

/**
 * <p>
 * 由于“创建点击图标时的弹出菜单方案二”中，弹出的菜单无法消失，会一直显示，所以需要根据鼠标是否悬浮于菜单上以及鼠标左键是否点<br>
 * 击来判断是否需要隐藏菜单：若鼠标不在菜单上并且鼠标左键点击了并且菜单是显示状态，则隐藏菜单。默认设置为鼠标未悬浮于菜单之上、<br>
 * 鼠标未点击、菜单未显示
 * </P>
 *
 * @see #initMouseClicked()
 * @see #initMouseOnJPopupMenu()
 */
public class Window extends JFrame implements NativeMouseInputListener {
    private OperatingSystemMXBean operatingSystemMXBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private Font font;

    private MouseOnJPopupMenu mouseOnJPopupMenu;
    private MouseClicked mouseClicked;

    private WindowMovable windowMovable;

    private JPopupMenu jPopupMenu;
    private JMenuItem movableButton;
    private JMenuItem exitButton;
    private JMenuItem cancelButton;
    private JMenuItem refreshButton;

    // jPopupMenu width
    private static final int WIDTH = 75;
    // jPopupMenu height
    private static final int HEIGHT = 89;

    // 窗体位置最大横坐标
    private int windowMaxX;
    // 窗体位置最大纵坐标
    private int windowMaxY;

    public Window() {
        // 关闭日志
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);

        initFont();

        initMouseOnJPopupMenu();
        initMouseClicked();

        initWindowMovable();

        initJPopupMenuAndButton();

        initWindow();

    }

    /**
     * 初始化字体
     */
    private void initFont() {
        font = new Font("宋体", Font.PLAIN, 13);
    }

    /**
     * 初始化鼠标是否悬浮于菜单之上，默认设置鼠标未悬浮于菜单之上
     */
    private void initMouseOnJPopupMenu() {
        mouseOnJPopupMenu = new MouseOnJPopupMenu(false);
    }

    /**
     * 初始化鼠标是否点击，默认设置鼠标左键未点击
     */
    private void initMouseClicked() {
        mouseClicked = new MouseClicked(false);
    }

    /**
     * 初始化窗体是否可移动， 默认设置窗体不可移动
     */
    private void initWindowMovable() {
        windowMovable = new WindowMovable(false);
    }

    /**
     * 初始化系统托盘菜单及其按钮
     */
    private void initJPopupMenuAndButton() {
        // 创建菜单
        jPopupMenu = new JPopupMenu();
        jPopupMenu.setSize(WIDTH, HEIGHT);
        setJPopupMenuMouseListener();

        // “移动”按钮
        movableButton = new JMenuItem("移动");
        movableButton.setFont(font);
        setMovableButtonMouseListener();
        jPopupMenu.add(movableButton);

        // “刷新”按钮
        refreshButton = new JMenuItem("刷新");
        refreshButton.setFont(font);
        setRefreshButtonMouseListener();
        jPopupMenu.add(refreshButton);

        // “取消”按钮
        cancelButton = new JMenuItem("取消");
        cancelButton.setFont(font);
        setCancelButtonMouseListener();
        jPopupMenu.add(cancelButton);

        // “退出程序”按钮
        exitButton = new JMenuItem("退出程序");
        exitButton.setFont(font);
        setExitButtonMouseListener();
        jPopupMenu.add(exitButton);
    }

    /**
     * 设置“刷新”按钮鼠标左击监听
     */
    private void setRefreshButtonMouseListener() {
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    // JMenuItem被鼠标左键点击
                    case MouseEvent.BUTTON1: {
                        refreshWindow();
                        hideMenu();
                        refreshButton.setBackground(new Color(238, 238, 238));
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 设置鼠标悬浮时按钮的背景颜色
                refreshButton.setBackground(Color.GRAY);
                // 设置鼠标悬浮于菜单之上
                mouseOnJPopupMenu.setValue(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 设置鼠标离开后按钮的背景颜色
                refreshButton.setBackground(new Color(238, 238, 238));
                // 设置鼠标未悬浮于菜单之上
                mouseOnJPopupMenu.setValue(false);
            }
        });
    }

    /**
     * 刷新窗口
     */
    private void refreshWindow() {
        this.setVisible(false);
        this.setVisible(true);
    }

    /**
     * 隐藏菜单
     */
    private void hideMenu() {
        jPopupMenu.setVisible(false);
    }

    /**
     * 设置“取消”按钮鼠标左击监听
     */
    private void setCancelButtonMouseListener() {
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    // JMenuItem被鼠标左键点击
                    case MouseEvent.BUTTON1: {
                        if (jPopupMenu.isVisible()) {
                            cancelButton.setBackground(new Color(238, 238, 238));
                            hideMenu();
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 设置鼠标悬浮时按钮的背景颜色
                cancelButton.setBackground(Color.GRAY);
                // 设置鼠标悬浮于菜单之上
                mouseOnJPopupMenu.setValue(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 设置鼠标离开后按钮的背景颜色
                cancelButton.setBackground(new Color(238, 238, 238));
                // 设置鼠标未悬浮于菜单之上
                mouseOnJPopupMenu.setValue(false);
            }
        });
    }

    /**
     * 设置菜单的鼠标监听，移入菜单时将鼠标是否悬浮于菜单之上（mouseOnJPopupMenu）设置为true，移出菜单时mouseOnJPopupMenu设置<br>
     * 为false。
     */
    private void setJPopupMenuMouseListener() {
        jPopupMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOnJPopupMenu.setValue(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOnJPopupMenu.setValue(false);
            }
        });
    }

    /**
     * <p>
     * 设置“移动 / 固定”按钮鼠标左击监听
     * </P>
     *
     * <p>
     * 窗体默认位置在屏幕右下角，且默认不能移动窗体。 当右击系统托盘弹出菜单后，点击第一个按钮后（此时为“移动”按钮）， 即可移<br>
     * 动窗体位置， 窗体移动有边界限制，不能移出屏幕外，且此时鼠标若悬浮于窗体之上，则将变为十字状态，菜单中的“移动”按钮变成了<br>
     * “固定”按钮。当点击“固定”按钮时，窗体固定在当前位置，无法移动。
     * </P>
     */
    private void setMovableButtonMouseListener() {
        movableButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    // JMenuItem被鼠标左键点击
                    case MouseEvent.BUTTON1: {
                        // 点击“移动”后方可移动窗体，点击“固定”后方可固定窗体位置
                        windowMovable.setValue(!windowMovable.getValue());
                        if (windowMovable.isTrue()) {
                            movableButton.setText("固定");
                        } else {
                            movableButton.setText("移动");
                        }
                        movableButton.setBackground(new Color(238, 238, 238));
                        hideMenu();
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 设置鼠标悬浮时按钮的背景颜色
                // 注：此处使用系统预设颜色，如果使用new Color，则会出现重影以及两个JMenuItem互相干扰的情况（具体原因不明）
                movableButton.setBackground(Color.GRAY);
                // 设置鼠标悬浮于菜单之上
                mouseOnJPopupMenu.setValue(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 设置鼠标离开后按钮的背景颜色
                movableButton.setBackground(new Color(238, 238, 238));
                // 设置鼠标未悬浮于菜单之上
                mouseOnJPopupMenu.setValue(false);
            }
        });
    }

    /**
     * <p>
     * 设置“退出程序”按钮鼠标左击监听
     * </p>
     */
    private void setExitButtonMouseListener() {
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    // JMenuItem被鼠标左键点击
                    case MouseEvent.BUTTON1: {
                        System.exit(0);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 设置鼠标悬浮时按钮的背景颜色
                exitButton.setBackground(Color.GRAY);
                // 设置鼠标悬浮于菜单之上
                mouseOnJPopupMenu.setValue(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 设置鼠标离开后按钮的背景颜色
                exitButton.setBackground(new Color(238, 238, 238));
                // 设置鼠标未悬浮于菜单之上
                mouseOnJPopupMenu.setValue(false);
            }
        });
    }

    /**
     * 初始化窗体，显示CPU使用率和Mem使用率
     */
    private void initWindow() {
        try {
            // 隐藏任务栏图标
            this.setType(JFrame.Type.UTILITY);

            // 设置窗口置顶
            this.setAlwaysOnTop(true);
            // 设置网格包布局
            GridBagLayout gridBagLayout = new GridBagLayout();
            this.setLayout(gridBagLayout);
            // 显示区域独占一行（换行），组件填充显示区域
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            // 设置无边框
            this.setUndecorated(true);
            // 设置背景色
            this.setBackground(new Color(0, 0, 0, 85));

            // CPU使用率显示标签
            String cpuMsg = "CPU: 0.0%";
            JLabel cpuJLabel = new JLabel(cpuMsg);
            cpuJLabel.setForeground(Color.WHITE);
            Font font = new Font("黑体", Font.PLAIN, 16);
            cpuJLabel.setFont(font);
            gridBagLayout.addLayoutComponent(cpuJLabel, gridBagConstraints);
            this.add(cpuJLabel);

            // 内存使用率显示标签
            String memMsg = "Mem: 0.0%";
            JLabel memoryJLabel = new JLabel(memMsg);
            memoryJLabel.setForeground(Color.WHITE);
            memoryJLabel.setFont(font);
            this.add(memoryJLabel);

            // 设置窗体大小
            this.setSize(100, 40);

            // 设置窗体最大位置坐标
            initWindowMaxLocation();

            // 设置窗体默认位置
            setDefaultLocation();

            // 设置鼠标监听，可通过鼠标移动窗体位置
            // 通过系统托盘菜单中的第一个按钮（移动 / 固定）来控制是否能移动窗体
            FrameDragListener frameDragListener =
                    new FrameDragListener(this, windowMovable, windowMaxX, windowMaxY);
            this.addMouseListener(frameDragListener);
            this.addMouseMotionListener(frameDragListener);

            // 添加窗体状态监听，使窗体一直显示
            setWindowAlwaysShow();

            // 设置点击关闭按钮退出程序
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // 添加系统托盘
            setSystemTray();

            // 全局监听鼠标点击
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(this);

            // 显示
            this.setVisible(true);

            // 监控
            monitoring(cpuJLabel, memoryJLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWindowMaxLocation() {
        // 获得窗体宽
        int windowWidth = this.getWidth();
        // 获得窗体高
        int windowHeight = this.getHeight();
        // 定义工具包
        Toolkit kit = Toolkit.getDefaultToolkit();
        // 获取屏幕的尺寸
        Dimension screenSize = kit.getScreenSize();
        // 获取屏幕的宽
        int screenWidth = screenSize.width;
        // 获取屏幕的高
        int screenHeight = screenSize.height;
        // 获取任务栏
        Insets screenInsets = kit.getScreenInsets(this.getGraphicsConfiguration());
        windowMaxX = screenWidth - windowWidth;
        windowMaxY = screenHeight - windowHeight - screenInsets.bottom;
    }

    /**
     * 监控
     *
     * @param cpuJLabel CPU使用率显示标签
     * @param memoryJLabel 内存使用率显示标签
     */
    private void monitoring(JLabel cpuJLabel, JLabel memoryJLabel) {
        int times = 0;
        while (true) {
            // 获取CPU使用率
            String cpuLoad = String.format("%.1f", operatingSystemMXBean.getSystemCpuLoad() * 100);
            // 获取内存使用率
            double totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
            double freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
            double value = freePhysicalMemorySize / totalPhysicalMemorySize;
            String memoryLoad = String.format("%.1f", (1 - value) * 100);
            cpuJLabel.setText("CPU: " + cpuLoad + "%");
            memoryJLabel.setText("Mem: " + memoryLoad + "%");
            // 每10分钟gc一次
            times++;
            if (times >= 600) {
                times = 0;
                System.gc();
            }
            // System.out.println("CPU: " + cpuLoad + "%, Mem: " + memoryLoad + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
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

                // 创建一个托盘图标：new TrayIcon

                // 创建点击图标时的弹出菜单方案一：PopupMenu
                // TrayIcon trayIcon =
                // new TrayIcon(image, "System Monitoring", createPopupMenu(frameMovable));
                // trayIcon.addActionListener(e -> System.out.println("addActionListener:
                // 托盘图标被右键点击"));

                // 创建点击图标时的弹出菜单方案二：JPopupMenu
                TrayIcon trayIcon = new TrayIcon(image, "System Monitoring");
                trayIcon.addMouseListener(new TrayIconMouseListener(jPopupMenu, WIDTH, HEIGHT));

                // 托盘图标自适应尺寸
                trayIcon.setImageAutoSize(true);

                // 添加托盘图标到系统托盘
                tray.add(trayIcon);

            } else {
                throw new Exception("当前系统不支持系统托盘");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 创建点击图标时的弹出菜单方案一：PopupMenu
     * 
     * @return 菜单
     */
    private PopupMenu createPopupMenu() {
        PopupMenu popupMenu = new PopupMenu();
        // 移动 / 固定
        MenuItem movableItem = new MenuItem("移动");
        movableItem.addActionListener(e -> {
            windowMovable.setValue(!windowMovable.getValue());
            if (windowMovable.isTrue()) {
                movableItem.setLabel("固定");
            } else {
                movableItem.setLabel("移动");
            }
        });
        popupMenu.add(movableItem);

        // 退出
        MenuItem exitItem = new MenuItem("退出");
        // 点击退出菜单时退出程序
        exitItem.addActionListener(e -> System.exit(0));
        popupMenu.add(exitItem);
        return popupMenu;
    }

    /**
     * 设置窗体默认位置
     */
    private void setDefaultLocation() {
        // 设置窗口相对于指定组件的位置：置于屏幕的中央
        // frame.setLocationRelativeTo(null);

        // 设置窗体默认在屏幕右下角
        this.setLocation(windowMaxX, windowMaxY);
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

    /**
     * 鼠标点击全局监听，设置隐藏菜单：若鼠标未悬浮于菜单之上（实际为若鼠标未悬浮于菜单或按钮之上）且鼠标左键点击了且菜单为显示<br>
     * 状态，则隐藏菜单
     */
    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
        int button = nativeMouseEvent.getButton();
        // 鼠标左键点击
        if (MouseEvent.BUTTON1 == button) {
            mouseClicked.setValue(true);
            if (!mouseOnJPopupMenu.isTrue() && mouseClicked.isTrue() && jPopupMenu.isVisible()) {
                hideMenu();
                // 菜单隐藏后，需要初始化判断状态，设置鼠标未悬浮于菜单之上、鼠标未点击
                AppUtil.initDefaultStatus(mouseOnJPopupMenu, mouseClicked);
            }
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {}

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {}

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {}

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {}
}
