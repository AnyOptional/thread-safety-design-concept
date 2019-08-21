package com.archer.cancellation;

import com.archer.annotation.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理好失败、关闭和取消是好的软件和勉强能运行的软件的最大区别。
 *                                       - I do agree.
 */
@ThreadSafe
public class PrimeGenerator implements Runnable {

    private List<BigInteger> primes = new ArrayList<>();

    private volatile boolean isCancelled = false;

    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        /**
         * 这里的取消是一种协作机制，通过设置一个标记，
         * 任务代码定期查看这个标记。
         * 显然，这种做法不能做到精确的取消，会有延迟。
         */
        while (!isCancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }

    void cancel() {
        /**
         * isCancelled在设计上就不可能设置为false，
         * 因此无论哪个线程调用了cancel，只要保证isCancelled
         * 的可见性就可以了。
         */
        isCancelled = true;
    }

    public synchronized List<BigInteger> primes() {
        return new ArrayList<>(primes);
    }
}
