package com.jeremy.demo.algorithm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeremy.demo.R;

public class MonkeyNumberOffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monkey_number_off);
        boolean people[] = new boolean[10];
        for (int i = 0; i < 10; i++) {
            people[i] = true;
        }
        rep(people);
        numberOff(people);
    }

    /**
     * 有n个人围成一个圈,顺序排号。从第一个人开始报数(从1到3报数),凡报到3的人退出圈子，输出退出顺序。
     *
     * @param people
     */
    public static void numberOff(boolean[] people) {
        int t = 0, len = people.length;
        while (len > 1) {
            for (int i = 0; i < people.length; i++) {
                if (people[i]) {
                    t++;
                    if (t == 3) {
                        t = 0;
                        people[i] = false;
                        len--;
                    }
                }
            }
        }
        for (int i = 0; i < people.length; i++) {
            if (people[i]) {
                System.out.println("最后退出的人的顺序是：" + (i + 1) % 3);
            }
        }
    }

    /**
     * 有n个人围成一个圈,顺序排号。从第一个人开始报数(从1到3报数),凡报到3的人退出圈子，输出退出顺序。
     *
     * @param people
     */
    public static void rep(boolean[] people) {
        int i = 0, j = 0, n = people.length, m = n;
        while (n > 2) {
            i = ++i % m;
            if (people[i] == true) {
                j++;
                if (j == 3) {
                    people[i] = false;
                    System.out.println(i);
                    n--;//总人数减1
                    j = 0;//到3从头数
                }
            }
        }
    }


}
