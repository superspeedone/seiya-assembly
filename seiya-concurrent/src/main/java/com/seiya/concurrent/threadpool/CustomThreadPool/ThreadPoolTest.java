package com.seiya.concurrent.threadpool.CustomThreadPool;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolTest {

    public static void main(String[] args) throws IOException {
        ThreadPool threadPool = new DefaultThreadPool(10);

        AtomicLong taskNum = new AtomicLong();

        for (int i = 0; i < 20; i++) {
            threadPool.submit(new MyTask(taskNum));
        }

        System.in.read();

    }

    static class MyTask implements Runnable {

        private AtomicLong taskNum;

        public MyTask(AtomicLong taskNum) {
            this.taskNum = taskNum;
        }

        @Override
        public void run() {
            System.out.printf("[%s  %d]任务-%d 正在执行\n", Thread.currentThread().getName(),
                    Thread.currentThread().getId(), taskNum.incrementAndGet());
        }
    }

}
