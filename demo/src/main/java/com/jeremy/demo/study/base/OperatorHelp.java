package com.jeremy.demo.study.base;

/**
 * Created by changqing on 2018/4/27.
 */

public class OperatorHelp {

    /**
     * 运行结果为：1010  10100  1010
     * <p>
     * 无符号右移，忽略符号位，空位都以0补齐
     * value >>> num     --   num 指定要移位值value 移动的位数。
     * 无符号右移的规则只记住一点：忽略了符号位扩展，0补最高位  无符号右移运算符>>> 只是对32位和64位的值有意义
     */
    public static void test() {
        int number = 10;
        //原始数二进制
        printInfo(number);
        number = number << 1;
        //左移一位
        printInfo(number);
        number = number >> 1;
        //右移一位
        printInfo(number);
    }

    /**
     * 输出一个int的二进制数
     *
     * @param num
     */
    private static void printInfo(int num) {
        System.out.println(Integer.toBinaryString(num));
    }


}
