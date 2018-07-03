package com.jeremy.demo.base;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by changqing on 2018/7/2.
 * Junit3中每个测试方法必须以test打头，Junit4中增加了注解，对方法名没有要求。
 */
public class CalculaterMockTest {

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
        // 第二个参数： 3 期望得到的结果
        // 第三个参数  result：实际返回的结果
        // 第四个参数  0 误差范围（可省略）
        // 如果期望值和实际值在误差范围内，测试通过，否则测试不通过！
        assertEquals("sum(a, b)", 3, result, 0);
    }

    @Test
    public void substract() throws Exception {
        int result = mCalculator.substract(a, b);
        assertEquals(-3, result, 2);
    }

    @Test
    public void mockTest() {
        List list = mock(List.class);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(3);
        //验证是否被调用一次，等效于下面的times(1)
        verify(list).add(1);
        verify(list, times(1)).add(1);
        //验证是否被调用2次
        verify(list, times(2)).add(2);
        //验证是否被调用3次
        verify(list, times(3)).add(3);
        //验证是否从未被调用过
        verify(list, never()).add(4);
        //验证至少调用一次
        verify(list, atLeastOnce()).add(1);
        //验证至少调用2次
        verify(list, atLeast(2)).add(2);
        //验证至多调用3次
        verify(list, atMost(3)).add(3);


        // you can mock concrete classes, not only interfaces
        LinkedList mockedList = mock(LinkedList.class);

// stubbing appears before the actual execution
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenReturn(null);//预设结果为null

// the following prints "first"
        System.out.println(mockedList.get(0));

// the following prints null
        System.out.println(mockedList.get(1));

// the following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));


        // mock creation
        List mockedList2 = mock(List.class);
        // using mock object - it does not throw any "unexpected interaction" exception
        mockedList2.add("one");
        //预设当List清空自身时抛出空指针异常
        doThrow(new NullPointerException()).when(mockedList2).clear();
        mockedList2.clear();

        Comparable comparable = mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        when(comparable.compareTo("Test")).thenReturn(1);
        when(comparable.compareTo("Omg")).thenReturn(2);
        assertEquals(1, comparable.compareTo("Test"));
        assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        assertEquals(0, comparable.compareTo("Not stub"));


        Comparator comparator = mock(Comparator.class);
        comparator.compare("nihao","hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
        verify(comparator).compare(anyString(),eq("hello"));
//        下面的代码报错： Invalid use of argument matchers!
//        verify(comparator).compare(anyString(),"hello");
    }

    @Test
    public void calculatorMockTest() {
        Calculater mCalculator = mock(Calculater.class);//模拟创建一个Calculater对象
        when(mCalculator.substract(3,1)).thenReturn(3);//预设substract的返回值为3
        System.out.println(mCalculator.substract(3,1));//输出3
        assertEquals(3, mCalculator.substract(3,1));//3==3测试通过

        when(mCalculator.substract(3, 1)).thenCallRealMethod();  //通过thenCallRealMethod来调用真实的api
        System.out.println(mCalculator.substract(3, 1));//输出2
        assertEquals(2, mCalculator.substract(3, 1));

        //反复调用，我们希望对于每次调用都返回不同的mock值
        Calculater demo = mock(Calculater.class);//模拟创建一个Calculater对象
        //Mockito.anyInt()表示传入任何整数值，返回值都是预设值
        Mockito.when(demo.substract(Mockito.anyInt(), Mockito.anyInt())).thenReturn(12).thenReturn(1024).thenReturn(2048);
        System.out.println(demo.substract(2,3));//12
        System.out.println(demo.substract(2,3));//1024
        System.out.println(demo.substract(2,3));//2048



    }


//    @Test
//    public void sum() throws Exception {
//        Calculater mCalculator = mock(Calculater.class);//模拟创建一个Calculater对象
//        mCalculator.sum(1, 3);//使用模拟对象调用对象里面的方法
//        verify(mCalculator).sum(1, 3);//验证sum方法是否发生
//    }


    @Test
    public void testAssertions() {
        //test data
        String str1 = new String("abc");
        String str2 = new String("abc");
        String str3 = null;
        String str4 = "abc";
        String str5 = "abc";
        int val1 = 5;
        int val2 = 6;
        String[] expectedArray = {"one", "two", "three"};
        String[] resultArray = {"one", "two", "three"};

        //Check that two objects are equal
        assertEquals(str1, str2);

        //Check that a condition is true
        assertTrue(val1 < val2);

        //Check that a condition is false
        assertFalse(val1 > val2);

        //Check that an object isn't null
        assertNotNull(str1);

        //Check that an object is null
        assertNull(str3);

        //Check if two object references point to the same object
        assertSame(str4, str5);

        //Check if two object references not point to the same object
        assertNotSame(str1, str3);

        //Check whether two arrays are equal to each other.
        assertArrayEquals(expectedArray, resultArray);
    }
}
