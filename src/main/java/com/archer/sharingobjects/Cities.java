package com.archer.sharingobjects;

import com.archer.annotation.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * 虽然可以增/删cityNames，但是Cities类的设计使得它在
 * 构造之后就无法修改cityNames的引用，因为它是final的，
 * 也无法增/删cityNames，因为并没有对外提供任何接口。
 */
@Immutable
public class Cities {

    /**
     * 在不可变对象的内部，同样可以使用可变对象来管理它们的状态
     */
    private final Set<String> cityNames = new HashSet<>();

    public Cities() {
        cityNames.add("Hefei");
        cityNames.add("Anqing");
        cityNames.add("Shenzhen");

        /**
         * 构造函数也没有做多余的事，this引用不会逸出
         */
    }

    /**
     * 没有直接返回cityNames而是返回了它的一份拷贝
     * 避免了在外部环境中直接修改cityNames
     */
    public Set<String> getCityNames() {
        return new HashSet<>(cityNames);
    }

    /**
     * 只是简单的查询，并不会影响到cityNames
     */
    public boolean containsCity(String city) {
        return cityNames.contains(city);
    }
}
