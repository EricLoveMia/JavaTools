package cn.eric.jdktools.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: 线程池监控
 * @author: 钱旭
 * @date: 2022-07-08 18:10
 **/
public class ThreadPoolMonitor {

    private final static Logger log = LoggerFactory.getLogger(ThreadPoolMonitor.class);

    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 0,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("my_thread_pool_%d").build(), new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) {
        // 每秒输出一次线程池的使用情况
        printThreadPoolState();
        // 模拟任务执行
        IntStream.rangeClosed(0, 20).forEach(i -> {
            // 每100毫秒，执行一个任务
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 每个处理一个任务耗时5秒
            threadPool.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private static void printThreadPoolState() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("Pool Size: " + threadPool.getPoolSize());
            System.out.println("Active Thread Count: " + threadPool.getActiveCount());
            System.out.println("Task Queue Size: " + threadPool.getQueue().size());
            System.out.println("Completed Task Count: " + threadPool.getCompletedTaskCount());
            System.out.println("---------------");
        }, 0, 1, TimeUnit.SECONDS);
    }
}


