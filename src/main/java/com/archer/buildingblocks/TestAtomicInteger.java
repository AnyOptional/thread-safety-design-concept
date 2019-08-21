package com.archer.buildingblocks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomicInteger {


    private AtomicInteger integer = new AtomicInteger(0);

    private final CountDownLatch startGate = new CountDownLatch(1);

    public void test() {

        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        boolean ret = integer.compareAndSet(0, 1);
                        if (!ret) {
                            System.out.println("compareAndSet failed");
                        }
                    } catch (InterruptedException e) {}
                }
            }).start();
        }

        startGate.countDown();
    }

    public static void main(String[] args) {
        /**
         * Java对象的创建过程：
         * 1、类加载检查： 虚拟机遇到一条 new 指令时，
         *   首先将去检查这个指令的参数是否能在常量池中定位到这个类的符号引用，
         *   并且检查这个符号引用代表的类是否已被加载、解析和初始化过。如果没有，那必须先执行相应的类加载过程。
         * 2、分配内存： 在类加载检查通过后，接下来虚拟机将为新生对象分配内存。
         *   对象所需的内存大小在类加载完成后便可确定，为对象分配空间的任务等同于把一块确定大小的内存从 Java 堆中划分出来。
         * 3、初始化默认值： 内存分配完成后，虚拟机需要将分配到的内存空间都初始化为默认值（基本类型为0引用类型为null，不包括对象头），
         *   这一步操作保证了对象的实例字段在 Java 代码中可以不赋初始值就直接使用，程序能访问到这些字段的数据类型所对应的零值。
         * 4、设置对象头： 初始化默认值完成之后，虚拟机要对对象进行必要的设置，例如这个对象是哪个类的实例、如何才能找到类的元数据信息、
         *   对象的哈希码、对象的 GC 分代年龄等信息。 这些信息存放在对象头中。 另外，根据虚拟机当前运行状态的不同，
         *   如是否启用偏向锁等，对象头会有不同的设置方式
         * 5、执行 init 方法： 在上面工作都完成之后，从虚拟机的视角来看，一个新的对象已经产生了，但从 Java 程序的视角来看，对象创建才刚开始，
         *   <init> 方法还没有执行，所有的字段都还为零。所以一般来说，执行 new 指令之后会接着执行 <init> 方法，
         *   把对象按照程序员的意愿进行初始化，这样一个真正可用的对象才算完全产生出来。
         */
        new TestAtomicInteger().test();
    }
}
