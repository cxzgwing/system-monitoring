package cxzgwing;

public class App {

    public static void main(String[] args) {
        System.setProperty("jna.encoding", "GBK");
        try {
            new Window();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
