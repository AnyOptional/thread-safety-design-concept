package com.archer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 标记一个类不是线程安全的
 */
@Target(ElementType.TYPE)
public @interface NotThreadSafe {
}
