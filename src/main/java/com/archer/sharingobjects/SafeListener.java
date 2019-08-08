package com.archer.sharingobjects;

public class SafeListener {

    private EventListener listener;

    /**
     * 想在构造函数中注册监听或者启动线程，
     * 可以使用一个私有的构造函数和一个工厂
     * 方法，这样就可以避免不正确的构造。
     */
    private SafeListener() {
        listener = new EventListener() {
            @Override
            public void onEvent() {
                doSomething();
            }
        };
    }

    void doSomething() {
        System.out.println("do something");

    }

    public static SafeListener newInstance(EventSource source) {
        /**
         * 在工厂方法里，对象已经构造完毕了，此时再注册监听就没有问题
         */
        SafeListener listener = new SafeListener();
        source.registerListener(listener.listener);
        return listener;
    }
}
