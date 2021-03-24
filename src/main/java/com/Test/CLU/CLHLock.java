package com.Test.CLU;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:
 */
public class CLHLock implements Lock {
    private AtomicReference<CLHNode> tail;
    private ThreadLocal<CLHNode> threadLocal;

    public CLHLock() {
        this.tail = new AtomicReference<>();
        this.threadLocal = new ThreadLocal<>();
    }

    @Override
    public void lock() {
        CLHNode curNode = threadLocal.get();
        if (curNode == null) {
            curNode = new CLHNode();
            threadLocal.set(curNode);
        }

        CLHNode predNode = tail.getAndSet(curNode);
        if (predNode != null) {
            while (predNode.getLocked()) {
            }
        }
    }

    @Override
    public void unlock() {
        CLHNode curNode = threadLocal.get();
        threadLocal.remove();

        if (curNode == null || curNode.getLocked() == false) {
            return;
        }

        if (!tail.compareAndSet(curNode, null)) {
            curNode.setLocked(false);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {
        final Lock clhLock = new CLHLock();

        for (int i = 0; i < 10; i++) {
            new Thread(new DemoTask(clhLock, i + "")).start();
        }
    }
}
