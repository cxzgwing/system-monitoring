package cxzgwing.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownListener extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownListener.class);

    @Override
    public void run() {
        logger.info("system-monitoring exit...");
    }
}
