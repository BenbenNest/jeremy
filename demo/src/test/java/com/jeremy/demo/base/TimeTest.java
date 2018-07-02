package com.jeremy.demo.base;

import org.junit.Test;

/**
 * Created by changqing on 2018/7/2.
 * 时间测试
 JUnit 提供了一个暂停的方便选项。如果一个测试用例比起指定的毫秒数花费了更多的时间，那么 Junit 将自动将它标记为失败。
 */

public class TimeTest {
    @Test(timeout=1000)
    public void testTime() throws Exception {
        System.out.println("test timeout");
        Thread.currentThread().sleep(500);
    }
}
