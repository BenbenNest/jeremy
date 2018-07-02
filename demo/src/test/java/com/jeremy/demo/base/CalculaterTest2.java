package com.jeremy.demo.base;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by changqing on 2018/7/2.
 */

public class CalculaterTest2 {
    private Calculater mCalculator;
    private int a = 1;
    private int b = 3;

    @Before//在测试开始之前回调的方法
    public void setUp() throws Exception {
        mCalculator = new Calculater();
    }

    @Test
    public void divide() throws Exception {
        int result = mCalculator.divide(a, b);
        // 第一个参数："sum(a, b)" 打印的tag信息 （可省略）
        // 第二个参数： 4 期望得到的结果
        // 第三个参数  result：实际返回的结果
        // 第四个参数  0 误差范围（可省略）
        assertEquals("divide(a, b)", 0, result, 0);
    }

    @Ignore//表示该方法禁用
    public void multiply() throws Exception {
        int result = mCalculator.multiply(a, b);
        assertEquals(3, result);
    }
}
