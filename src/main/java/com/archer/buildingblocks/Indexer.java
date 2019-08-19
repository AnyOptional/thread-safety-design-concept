package com.archer.buildingblocks;

import com.archer.annotation.ThreadSafe;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞队列支持生产者-消费者设计模式。
 * 一个生产者-消费者设计分离了"识别需要完成的工作"
 * 和"执行工作"。该模式不会发现一个工作便立即处理，
 * 而是把任务放置到一个代办清单中，以备后期处理。
 */

/**
 * 消费者 - 生成对应的文件索引
 */
@ThreadSafe
public class Indexer implements Runnable {

    private final BlockingQueue<File> queue;

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (queue == null) break;
                File file = queue.take();
                indexFile(file);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File file) {
        // do something
    }
}
