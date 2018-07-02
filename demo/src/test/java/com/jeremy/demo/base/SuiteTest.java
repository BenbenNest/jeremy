package com.jeremy.demo.base;

import com.jeremy.demo.base.CalculaterTest;
import com.jeremy.demo.base.CalculaterTest2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by changqing on 2018/7/2.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CalculaterTest.class, CalculaterTest2.class})//被测试类
public class SuiteTest {


}
