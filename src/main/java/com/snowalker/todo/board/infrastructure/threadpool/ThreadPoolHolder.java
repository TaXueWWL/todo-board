package com.snowalker.todo.board.infrastructure.threadpool;

import com.snowalker.todo.board.infrastructure.file.FileHandler;

import java.util.concurrent.*;

/**
 * @author snowalker
 * @createTime: 2021-02-08 15:53
 * @ClassName: ThreadPoolHolder
 * @description: ThreadPoolHolder
 */
public class ThreadPoolHolder {

    private ThreadPoolHolder() {
    }

    /**
     * 文件操作线程池
     */
    private ThreadPoolExecutor fileProcessThreadPool = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            2 * Runtime.getRuntime().availableProcessors() + 1,
            100,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public ThreadPoolExecutor getFileProcessThreadPool() {
        return fileProcessThreadPool;
    }

    public static ThreadPoolHolder getInstance() {
        return Holder.singleton;
    }

    /**
     * 内部类，为单例提供
     */
    private static class Holder {
        private static ThreadPoolHolder singleton = new ThreadPoolHolder();
    }
}
