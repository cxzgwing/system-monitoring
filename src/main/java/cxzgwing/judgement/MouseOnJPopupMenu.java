package cxzgwing.judgement;

public class MouseOnJPopupMenu {
    private boolean value;

    public MouseOnJPopupMenu(boolean value) {
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
