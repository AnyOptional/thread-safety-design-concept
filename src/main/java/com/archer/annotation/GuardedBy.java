package com.archer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 标记一个变量由什么来保证线程安全
 */
@Target(ElementType.FIELD)
public @interface GuardedBy {
    /**
     * 描述变量由什么来守护
     */
    String value() default "";
}
