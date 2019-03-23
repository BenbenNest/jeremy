package com.jeremy.algorithm;

public class NumTest {

    /**
     * 给定一个整数n，求解它的阶乘的乘积里末尾0的个数。举个例子，比如3! = 1 * 2 * 3 = 6,末尾0的个数为0,
     * 5! = 1 * 2
     */
    public int calcuZero(int n) {

        int count = 0;

        for (int i = 1; i <= n; i++) {
            int cur = i;
            //如果因数中有一个5那么乘积中就会有一个0，所以计算每一个i中因数5的个数
            while (cur % 5 == 0) {
                count++;
                cur /= 5;
            }
        }
        return count;
    }

}
