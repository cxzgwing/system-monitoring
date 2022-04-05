package cxzgwing.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cxzgwing.label.impl.BatteryLabel;
import cxzgwing.utils.AppUtil;

public class BatteryMonitorTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BatteryMonitorTask.class);
    private BatteryLabel batteryLabel;

    public BatteryMonitorTask(BatteryLabel batteryLabel) {
        this.batteryLabel = batteryLabel;
    }

    @Override
    public void run() {
        while (true) {
            if (batteryLabel.isDisplay()) {
                batteryLabel.setText(AppUtil.getBatteryPercent());
            }
            try {
                Thread.sleep(15 * 1000);
            } catch (InterruptedException e) {
                logger.error("BatteryMonitorTask error", e);
                break;
            }
        }
    }
}
