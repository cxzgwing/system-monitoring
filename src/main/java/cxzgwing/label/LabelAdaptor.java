package cxzgwing.label;

import javax.swing.*;

public abstract class LabelAdaptor extends JLabel implements Displayable {
    @Override
    public boolean isDisplay() {
        return false;
    }

    @Override
    public void setDisplay(boolean display) {}

}
