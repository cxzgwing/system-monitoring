package cxzgwing.task;

import cxzgwing.label.impl.BatteryLabel;
import cxzgwing.utils.AppUtil;

public class BatteryMonitorTask implements Runnable {
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
                e.printStackTrace();
                break;
            }
        }
    }
}
