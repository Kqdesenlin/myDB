package com.Test.CLU;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:
 */
public class CLHNode {
    private volatile boolean locked = true;

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
