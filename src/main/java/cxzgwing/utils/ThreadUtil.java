package cxzgwing.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    private static ThreadPoolExecutor executor;
    static {
        executor = new ThreadPoolExecutor(3, 3, 200, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));
    }

    public static void addTask(Runnable task) {
        executor.execute(task);
    }
}
