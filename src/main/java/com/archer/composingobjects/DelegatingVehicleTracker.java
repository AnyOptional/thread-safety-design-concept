package com.archer.composingobjects;

import com.archer.annotation.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将VehicleTracker的线程安全代理给了线程安全的ConcurrentHashMap类.
 * 相比MonitorVehicleTracker，DelegatingVehicleTracker返回的是对
 * location的实时引用而不是某一时刻的快照。
 */
@ThreadSafe
public class DelegatingVehicleTracker {

    private final Map<String, Point> locations;
    private final Map<String, Point> unmodifiableLocations;

    DelegatingVehicleTracker(Map<String, Point> locations) {
        /**
         * unmodifiableLocations和locations建立了依赖关系。
         * 可以认为返回的是locations，只不过限制了不可修改。
         */
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiableLocations = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, Point> getLocations() {
        /**
         * 其他线程对Point的更新可以实时得到反馈。
         */
        return unmodifiableLocations;
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        /**
         * locations是初始化就设定好的，而且设计上没有
         * 任何途径可以添加新的key-value pair。因此
         * locations.get(id)的返回值不会因其他线程的
         * 任意动作而产生变化，以下两行代码就构不成原子操作了。
         */
        if(locations.get(id) == null) return;
        locations.put(id, new Point(x, y));
    }

}
