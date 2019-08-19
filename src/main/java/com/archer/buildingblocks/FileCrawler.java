package com.archer.buildingblocks;

import com.archer.annotation.ThreadSafe;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者 - 检索出有效的文件
 */
@ThreadSafe
public class FileCrawler implements Runnable {

    private final BlockingQueue<File> queue;

    private final File root;

    private final FileFilter fileFilter;

    private final List<String> indexedFilePaths = new ArrayList<>();

    public FileCrawler(File file, FileFilter fileFilter, BlockingQueue<File> queue) {
        this.root = file;
        this.fileFilter = fileFilter;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            /**
             * 恢复中断状态，避免掩盖中断
             */
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File root) throws InterruptedException {
        if (root != null && fileFilter != null && fileFilter.accept(root)) {
            if (root.isDirectory()) {
                crawl(root);
            } else {
                String path = root.getAbsolutePath();
                synchronized (this) {
                    /**
                     * 这里若不同步，两个线程可能同时检测到不包含path，
                     * 从而添加了两次。
                     * 即使indexedFilePaths是同步的List仍避免不了这个问题，
                     * 因为这里要求 不存在则添加 是一个原子操作。
                     */
                    if (!indexedFilePaths.contains(path)) {
                        indexedFilePaths.add(path);
                        queue.put(root);
                    }
                }
            }
        }
    }
}
