package com.Test.CLU;

import java.util.concurrent.locks.Lock;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:
 */
public class DemoTask implements Runnable {
    private Lock lock;
    private String taskId;

    public DemoTask(final Lock lock, final String taskId){
        this.lock = lock;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            Thread.sleep(500);
            System.out.println(String.format("Thread %s Completed", taskId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
