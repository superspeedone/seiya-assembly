package com.seiya.concurrent.threadpool.CustomThreadPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 默认线程池实现
 *
 * @param <Job>
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    // 线程池最大限制数
    private static final int MAX_WORKER_NUMBER = 10;
    // 线程池默认限制数
    private static final int DEFAULT_WORKER_NUMBER = 5;
    // 线程池最小限制数
    private static final int MIN_WORKER_NUMBER = 1;

    // 工作任务列表，将会向里面插入任务
    private LinkedList<Job> jobs = new LinkedList<Job>();
    // 工作者列表
    private List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
    // 工作者线程数量
    private int workerNum = DEFAULT_WORKER_NUMBER;
    // 线程编号生成
    private AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool() {
        initializeWorkers(DEFAULT_WORKER_NUMBER);
    }

    public DefaultThreadPool(int num) {
        this.workerNum = num > MAX_WORKER_NUMBER ? MAX_WORKER_NUMBER : (num < MIN_WORKER_NUMBER ? MIN_WORKER_NUMBER : num);
        initializeWorkers(this.workerNum);
    }

    @Override
    public void submit(Job job) {
        if (job != null) {
            // 添加一个工作任务，然后进行通知
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        // 添加工作者Worker时先获取任务对象的锁monitor，防止继续生成和消费工作任务job
        synchronized (jobs) {
            // 限制新增的Worker数量不能超过最大值
            if (num + this.workerNum > MAX_WORKER_NUMBER) {
                num = MAX_WORKER_NUMBER - this.workerNum;
            }
            initializeWorkers(num);
        }
    }

    @Override
    public void removeWorkers(int num) {
        // 删除工作者Worker时先获取任务对象的锁monitor，防止继续生成和消费工作任务job
        synchronized (jobs) {
            if (num <= 0) {
                throw new IllegalArgumentException("the num must be greater than zero.");
            }
            if (num >= this.workerNum) {
                throw new IllegalArgumentException("beyond workNum.");
            }

            // 按照给定的数量停止Worker
            int count = 0;
            while (count < num) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return this.jobs.size();
    }

    //初始化工作者线程
    private void initializeWorkers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    // 工作者，负责消费任务
    class Worker implements Runnable {
        // 是否工作
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    // 如果工作任务列表是空的，那么久wait
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 感知到外部对WorkThread的中断操作，返回
                            Thread.currentThread().interrupt();;
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception e) {
                        // 忽略job执行中的Exception
                    }
                }
            }
        }

        public void shutdown() {
            running = false;
        }
     }
}
