package com.archer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 标记一个类是不可变的
 */
@Target(ElementType.TYPE)
public @interface Immutable {
}
