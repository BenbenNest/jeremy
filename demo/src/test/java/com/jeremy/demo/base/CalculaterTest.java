package com.jeremy.demo.base;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by changqing on 2018/7/2.
 */

public class CalculaterTest {
    private Calculater mCalculator;
    private int a = 1;
    private int b = 3;

    @Before//在测试开始之前回调的方法
    public void setUp() throws Exception {
        mCalculator = new Calculater();
    }

    @Test//我们需要测试的方法
    public void sum() throws Exception {
        int result = mCalculator.sum(a, b);
        // 第一个参数："sum(a, b)" 打印的tag信息 （可省略）
        // 第二个参数： 4 期望得到的结果
        // 第三个参数  result：实际返回的结果
        // 第四个参数  0 误差范围（可省略）
        assertEquals("sum(a, b)", 4, result, 0);
    }

    @Test
    public void substract() throws Exception {
        int result = mCalculator.substract(a, b);
        assertEquals(-2, result);
    }

}
