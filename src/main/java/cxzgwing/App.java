package cxzgwing;

import cxzgwing.utils.AppUtil;

public class App {

    public static void main(String[] args) {
        System.setProperty("jna.encoding", "GBK");
        new Window(AppUtil.getProperties());
    }

}
