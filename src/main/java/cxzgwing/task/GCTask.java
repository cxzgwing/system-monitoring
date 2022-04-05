package cxzgwing.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GCTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GCTask.class);

    @Override
    public void run() {
        while (true) {
            System.gc();
            try {
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                logger.error("GCTask error", e);
                break;
            }
        }
    }
}
