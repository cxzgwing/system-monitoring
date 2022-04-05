package cxzgwing.model;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

import cxzgwing.utils.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class Properties {
    // 1-单列布局 2-双列布局（默认）
    @JSONField(name = "layout")
    private int layout;

    // 显示标签：1-CPU 2-Memory 3-Battery 4-Time 默认全选
    @JSONField(name = "display")
    private Map<String, Boolean> displayMap;

    @JSONField(name = "x")
    private int x;
    @JSONField(name = "y")
    private int y;

    public Properties() {
        this.layout = 2;
        // 窗体默认在屏幕右下角，x和y均设置为-1
        this.x = -1;
        this.y = -1;
        this.displayMap = new HashMap<>();
        this.displayMap.put(Constants.FIELD_CPU, true);
        this.displayMap.put(Constants.FIELD_MEMORY, true);
        this.displayMap.put(Constants.FIELD_BATTERY, true);
        this.displayMap.put(Constants.FIELD_TIME, true);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isSingleLayout() {
        return this.layout == Constants.Single_Layout;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isDoubleLayout() {
        return this.layout == Constants.Double_Layout;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isCpuDisplay() {
        return this.displayMap.get(Constants.FIELD_CPU);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isMemoryDisplay() {
        return this.displayMap.get(Constants.FIELD_MEMORY);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isBatteryDisplay() {
        return this.displayMap.get(Constants.FIELD_BATTERY);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isTimeDisplay() {
        return this.displayMap.get(Constants.FIELD_TIME);
    }

    @JSONField(serialize = false, deserialize = false)
    public void put(String name, boolean isDisplay) {
        this.displayMap.put(name, isDisplay);
    }

}
