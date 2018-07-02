package com.jeremy.demo.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by changqing on 2018/7/2.
 * 用 @RunWith(Parameterized.class) 来注释 test 类
   创建一个静态方法生成并返回测试数据，并注明@Parameters注解
   创建一个公共的构造函数，接受存储上一条的测试数据
   使用上述测试数据进行测试
 */
@RunWith(Parameterized.class)
public class CalculaterTest3 {
    private int expected;
    private int a;
    private int b;


    @Parameterized.Parameters//创建并返回测试数据
    public static Collection params() {
        return Arrays.asList(new Integer[][]{{3, 1, 2}, {5, 2, 3}});
    }

    //接收并存储（实例化）测试数据
    public CalculaterTest3(int expected, int a, int b) {
        this.expected = expected;
        this.a = a;
        this.b = b;
    }

    @Test//使用测试数据测试
    public void sum() throws Exception {
        Calculater calculater = new Calculater();
        System.out.println("parameters : " + a + " + "
                + b);
        int result = calculater.sum(a, b);
        assertEquals(expected, result);
    }

}
