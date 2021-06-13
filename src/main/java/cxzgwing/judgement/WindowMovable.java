package cxzgwing.judgement;

public class WindowMovable {
    private boolean value;

    public WindowMovable(boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }

    public boolean getValue() {
        return value;
    }

    synchronized public void setValue(boolean value) {
        this.value = value;
    }

}
