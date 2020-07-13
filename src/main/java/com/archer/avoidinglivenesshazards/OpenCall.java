package com.archer.avoidinglivenesshazards;

import com.archer.annotation.Immutable;
import com.archer.annotation.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

public class OpenCall {

    /**
     * 以下几个互相协作的类都是线程安全的，
     * 但是它们一起使用的时候却有可能导致死锁。
     *
     * 这是因为Taxi和Dispatcher的方法都是同步方法，
     * 需要获得各自的内置锁。Taxi的setLocation方法先获得
     * Taxi的内置锁，还有可能获得Dispatcher的内置锁，而Dispatcher
     * 的snapshotImage也有同样的问题，这样就可能发生死锁。
     *
     * 开放调用：调用的方法不需要持有锁。
     *
     * 可以通过使用synchronized代码块来取代synchronized方法，
     * 只锁住需要保护的共享变量。
     */

    @Immutable
    static class Point {
        public final int x;
        public final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @ThreadSafe
    static class Taxi {

        private Point srcPoint, dstPoint;

        private final Dispatcher dispatcher;

        Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        synchronized Point getLocation() {
            return srcPoint;
        }

        synchronized void setLocation(Point point) {
            this.srcPoint = point;
            if (point.equals(dstPoint)) {
                dispatcher.notifyAvailable(this);
            }

            /**
             * 改为synchronized代码块后，可能会破坏原子性，
             * 这就需要具体的分析了。
             */
//            boolean reached = false;
//            synchronized (this)  {
//                this.srcPoint = point;
//                reached = point.equals(dstPoint);
//            }
//            if (reached) {
//                dispatcher.notifyAvailable(this);
//            }
        }

    }

    @ThreadSafe
    static class Dispatcher {

        private final Set<Taxi> taxis = new HashSet<>();

        synchronized void notifyAvailable(Taxi taxi) {
            taxis.add(taxi);
        }

        synchronized void snapshotImage() {
            for (Taxi taxi : taxis) {
                Point point = taxi.getLocation();
                // draw image
            }
            // return image

            /**
             * 使用一份拷贝
             */
//            Set<Taxi> taxis = null;
//            synchronized (this) {
//                taxis = new HashSet<>(this.taxis);
//            }
//            for (Taxi taxi : taxis) {
//                Point point = taxi.getLocation();
//                // draw image
//            }
//            // return image
        }
    }

}
