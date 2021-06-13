package cxzgwing.judgement;

public class FrameMovable {
    private boolean value;

    public FrameMovable(boolean value) {
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
