package cxzgwing.utils;

import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sun.management.OperatingSystemMXBean;

import cxzgwing.Window;
import cxzgwing.dll.Dll;
import cxzgwing.model.Properties;

public class AppUtil {
    private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);
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
            logger.error("getBatteryPercent error", e);
        }
        return "B: " + value + "%";
    }

    public static void initWindowLocation(Window window, Properties properties) {

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
        // 获取窗体边界位置
        int windowMaxX = screenWidth - windowWidth;
        int windowMaxY = screenHeight - windowHeight - screenInsets.bottom;
        window.setWindowMaxX(windowMaxX);
        window.setWindowMaxY(windowMaxY);
        // 设置窗体位置坐标
        int x;
        int y;
        int propertiesX = properties.getX();
        if (propertiesX > -1 && propertiesX <= windowMaxX) {
            x = propertiesX;
        } else {
            x = windowMaxX;
        }
        int propertiesY = properties.getY();
        if (propertiesY > -1 && propertiesY <= windowMaxY) {
            y = propertiesY;
        } else {
            y = windowMaxY;
        }

        // 设置窗口相对于指定组件的位置：置于屏幕的中央
        // frame.setLocationRelativeTo(null);

        // 设置窗体位置（默认在屏幕右下角）
        if (x != window.getX() || y != window.getY()) {
            window.setLocation(x, y);
        }
    }

    public static Properties getProperties() {
        Properties properties;
        String propertiesPath = getPropertiesPath();
        String content = readFile(propertiesPath);
        if (StringUtils.isBlank(content)) {
            properties = new Properties();
            writeFile(propertiesPath, properties, false);
        } else {
            properties = JSON.parseObject(content, Properties.class);
        }
        return properties;
    }

    public static String readFile(String path) {
        String value = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    stringBuilder.append(s);
                }
                value = stringBuilder.toString();
            }
        } catch (Exception e) {
            logger.error("readFile error, return empty string", e);
        } finally {
            closeResource(bufferedReader);
            closeResource(fileReader);
        }
        return value;
    }

    public static boolean writeFile(String filePath, Object o, boolean append) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, append);
            fos.write(JSON.toJSONString(o).getBytes());
            return true;
        } catch (Exception e) {
            logger.error("writeFile error", e);
            return false;
        } finally {
            closeResource(fos);
        }
    }

    private static void closeResource(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.error("closeResource error", e);
            }
        }
    }

    public static String getPropertiesPath() {
        String folder = getCurrentFolder();
        return folder + "properties.conf";
    }

    /**
     * 获取程序当前所在的文件夹
     * 
     * @return eg:/E:/Projects/GitHub/MyProjects/system-monitoring/target/classes/
     */
    private static String getCurrentFolder() {
        String locationPath =
                AppUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (locationPath.toUpperCase().contains(".JAR")) {
            // 截取.JAR第一次出现前的字符串
            String StrPath = locationPath.substring(0, locationPath.toUpperCase().indexOf(".JAR"));
            // 获取jar包的上一层文件夹
            locationPath = StrPath.substring(0, StrPath.lastIndexOf(File.separator) + 1);
        } else if (locationPath.toUpperCase().contains(".EXE")) {
            String StrPath = locationPath.substring(0, locationPath.toUpperCase().indexOf(".EXE"));
            locationPath = StrPath.substring(0, StrPath.lastIndexOf(File.separator) + 1);
        }
        return locationPath;
    }

    public static GridLayout initLayout(int displayLabelCount, Properties properties,
            Window window) {
        GridLayout gridLayout = null;
        if (properties.isSingleLayout()) {
            // 单列布局
            window.setLayout(gridLayout = new GridLayout(displayLabelCount, 1));
            window.setSize(75, 20 * displayLabelCount);
        } else if (properties.isDoubleLayout()) {
            // 双列布局
            switch (displayLabelCount) {
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
        return gridLayout;
    }
}
