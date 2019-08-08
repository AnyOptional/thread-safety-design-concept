package com.archer.sharingobjects;

public class ThisEscape {

    public ThisEscape(EventSource source) {

        /**
         * 发布一个内部类时会隐式的发布自身，
         * 因为内部类会持有外部类的引用。
         *
         * 当外部类被隐式发布时，可能还没有被完全构造(此时构造函数
         * 还没有执行完)，被发布的可能只是一个部分构造的对象，这会
         * 产生问题。
         */
        source.registerListener(new EventListener() {
            @Override
            public void onEvent() {
                /**
                 * 这里连带着将外部类也发布出去了，然而外部类此时还没有正确构造。
                 *
                 * NOTE: 不要让this引用在构造期间逸出。
                 * WARNING：在构造函数中启动新线程也会使得this逸出。构造函数中可以创建新线程，
                 * 但应该提供单独的start调用来启动新线程。
                 * 在构造函数中调用非private非final的实例方法，同样会使得this逸出。
                 */
                doSomething();
            }
        });
    }

    void doSomething() {
        System.out.println("do something");
    }
}

interface EventListener {
    void onEvent();
}

class EventSource {
    void registerListener(EventListener listener) {
        // no-op
    }
}