package com.seiya.concurrent.threadpool.CustomThreadPool;

/**
 * 定义线程池接口，所有任务都需要实现Runnable接口
 *
 * @param <Job>
 */
public interface ThreadPool<Job extends Runnable> {

    // 提交一个job，这个job需要实现Runnable接口
    void submit(Job job);

    // 关闭线程池
    void shutdown();

    // 增加工作者线程
    void addWorkers(int num);

    // 减少工作者线程
    void removeWorkers(int num);

    // 得到正在执行任务的数量
    int getJobSize();

}
