package com.seiya.concurrent.thread;

/**
 * 线程终端测试
 */
public class ThreadInterruptTest {

    public static void main(String[] args) {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.currentThread().sleep(5000);
                    boolean isAlive = Thread.currentThread().isAlive();
                    System.out.println(Thread.currentThread().getName() + " is " + (isAlive ? "Alive" : "Dead"));
                    Thread.currentThread().interrupt();
                    isAlive = Thread.currentThread().isAlive();
                    System.out.println(Thread.currentThread().getName() + " is " + (isAlive ? "Alive" : "Dead"));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, "Thread Interrupt User");


        thread.start();

    }

}
