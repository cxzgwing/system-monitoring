package cxzgwing.dll;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Dll extends Library {
    Dll dll = Native.load("BatteryMonitor", Dll.class);

    int BatteryPercent();
}
