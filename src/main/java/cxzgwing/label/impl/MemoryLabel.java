package cxzgwing.label.impl;

import cxzgwing.label.LabelAdaptor;

public class MemoryLabel extends LabelAdaptor {
    private boolean display;

    public MemoryLabel(String text) {
        this.setText(text);
        this.display = true;
    }

    @Override
    public boolean isDisplay() {
        return display;
    }

    @Override
    public void setDisplay(boolean display) {
        this.display = display;
    }

}
