package cxzgwing.task;

public class GCTask implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.gc();
            try {
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
