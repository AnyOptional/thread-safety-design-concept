package com.archer.composingobjects;

import com.archer.annotation.ThreadSafe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 这种方式虽然是线程安全的，但是返回的locations是某一时刻的快照，
 * 若vehicle的位置一直在更新，就需要一直获取新的locations去反应
 * 这一变化。
 */
@ThreadSafe
public class MonitorVehicleTracker {

    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint point = locations.get(id);
        if (point == null) return;
        locations.put(id, new MutablePoint(x, y));
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint point = locations.get(id);
        if (point == null) return null;
        /**
         * 返回拷贝版本，避免外界修改
         */
        return new MutablePoint(point);
    }

    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> map) {
        /**
         * 返回的是参数的拷贝。
         */
        Map<String, MutablePoint> copy = new HashMap<>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            /**
             * 由于MutablePoint是可变的，添加的同样是Point的拷贝
             * 防止外界更改Point的值
             */
            copy.put(key, new MutablePoint(map.get(key)));
        }
        /**
         * 返回一份不可修改的Map
         */
        return Collections.unmodifiableMap(copy);
    }
}
