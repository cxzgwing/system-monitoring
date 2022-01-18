package cxzgwing.task;

import cxzgwing.label.impl.CpuLabel;
import cxzgwing.label.impl.MemoryLabel;
import cxzgwing.label.impl.TimeLabel;
import cxzgwing.utils.AppUtil;

public class CpuMemoryTimeMonitorTask implements Runnable {
    private CpuLabel cpuLabel;
    private MemoryLabel memoryLabel;
    private TimeLabel timeLabel;

    public CpuMemoryTimeMonitorTask(CpuLabel cpuLabel, MemoryLabel memoryLabel,
            TimeLabel timeLabel) {
        this.cpuLabel = cpuLabel;
        this.memoryLabel = memoryLabel;
        this.timeLabel = timeLabel;
    }

    @Override
    public void run() {
        while (true) {
            if (cpuLabel.isDisplay()) {
                cpuLabel.setText(AppUtil.getSystemCpuLoad());
            }
            if (memoryLabel.isDisplay()) {
                memoryLabel.setText(AppUtil.getMemoryLoad());
            }
            if (timeLabel.isDisplay()) {
                timeLabel.setText(AppUtil.getTime());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
