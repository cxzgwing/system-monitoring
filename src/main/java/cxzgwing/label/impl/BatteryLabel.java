package cxzgwing.label.impl;

import cxzgwing.label.LabelAdaptor;

public class BatteryLabel extends LabelAdaptor {
    private boolean display;

    public BatteryLabel(String text) {
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
