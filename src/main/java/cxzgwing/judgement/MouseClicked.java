package cxzgwing.judgement;

public class MouseClicked {
    private boolean value;

    public MouseClicked(boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }

    public boolean getValue() {
        return value;
    }

    public synchronized void setValue(boolean value) {
        this.value = value;
    }

}
