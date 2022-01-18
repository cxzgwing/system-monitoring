package cxzgwing.label.impl;

import cxzgwing.label.LabelAdaptor;

public class CpuLabel extends LabelAdaptor {
    private boolean display;

    public CpuLabel(String text) {
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
