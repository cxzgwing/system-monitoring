package cxzgwing.utils;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.sun.management.OperatingSystemMXBean;

import cxzgwing.Window;
import cxzgwing.dll.Dll;

public class AppUtil {
    private static OperatingSystemMXBean systemMXBean;
    static {
        systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public static String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static String getMemoryLoad() {
        double totalPhysicalMemorySize = systemMXBean.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = systemMXBean.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize / totalPhysicalMemorySize;
        return "M: " + String.format("%.1f", (1 - value) * 100) + "%";
    }

    public static String getSystemCpuLoad() {
        return "C: " + String.format("%.1f", systemMXBean.getSystemCpuLoad() * 100) + "%";
    }

    public static String getBatteryPercent() {
        int value = 225;
        try {
            value = Dll.dll.BatteryPercent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "B: " + value + "%";
    }

    public static void initWindowLocation(Window window) {
        // 获得窗体宽
        int windowWidth = window.getWidth();
        // 获得窗体高
        int windowHeight = window.getHeight();
        // 定义工具包
        Toolkit kit = Toolkit.getDefaultToolkit();
        // 获取屏幕的尺寸
        Dimension screenSize = kit.getScreenSize();
        // 获取屏幕的宽
        int screenWidth = screenSize.width;
        // 获取屏幕的高
        int screenHeight = screenSize.height;
        // 获取任务栏
        Insets screenInsets = kit.getScreenInsets(window.getGraphicsConfiguration());
        window.setWindowMaxX(screenWidth - windowWidth);
        window.setWindowMaxY(screenHeight - windowHeight - screenInsets.bottom);

        // 设置窗口相对于指定组件的位置：置于屏幕的中央
        // frame.setLocationRelativeTo(null);

        // 设置窗体默认在屏幕右下角
        window.setLocation(window.getWindowMaxX(), window.getWindowMaxY());
    }
}
