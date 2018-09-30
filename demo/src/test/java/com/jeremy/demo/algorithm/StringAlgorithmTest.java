package com.jeremy.demo.algorithm;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by changqing on 2018/8/21.
 */

public class StringAlgorithmTest {

    //#############  start  #############
    /**
     * 题目1：翻转句子

     题目： 给定一个英文句子，每个单词之间都是由一个或多个空格隔开，请翻转句子中的单词顺序（包括空格的顺序），但单词内字符的顺序保持不变。例如输入"www google com "，则应输出" com google www"。

     */
    @Test
    public void testReverseSuite(){
        reverse_suite1(" hello world");
    }

    private char[] reverse(char[] chars, int start, int end) {
        // str 判断null， 索引有效值判断
        if (chars == null || start < 0 || end >= chars.length || start >= end) {
            return chars;
        }

        while (start < end) {
            // 收尾字符互换，直到替换完成。
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            start++;
            end--;
        }
        return chars;
    }

    public String reverse_suite1(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence;
        }

        int length = sentence.length();
        // 第一步翻转所有字符
        char[] chars = reverse(sentence.toCharArray(), 0, length - 1);
        System.out.println(new String(chars));

        // 第二步翻转每个单词（重点：怎么找到单词）
        int start = 0, end = 0;
        while (start < length) {
            if (chars[start] == ' ') {
                // 遇到空格就向右边继续查找
                start++;
                end++;
            } else if (end == length || chars[end] == ' ') {
                // 遇到空格或者已经到了字符串末尾，此时翻转找到的单词内部字符，这里需要注意end-1
                chars = reverse(chars, start, end - 1);
                System.out.println(new String(chars));
                // 重新制定检查索引start
                start = end++;
            } else {
                end++;
            }
        }
        return new String(chars);
    }
    //#############  end  #############


    //#############  start  #############

// 题目：接上题，面试官继续提问，我们得到的" com google www"需要被用作一个URL的参数，
// 所以这里需要的处理是去掉开头结尾的无效空格，并将两个单词中间的每一个空格都替换为"%20"。
// 例如" com google www"应被转换为"com%20%20google%20www"，请给出转换函数。

    private String spaceTrimReplace(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence;
        }

        // 去掉字符串首尾的空格
        sentence = trim(sentence);
        int len = sentence.length();
        char[] chars = sentence.toCharArray();
        int count = getSpaceCount(sentence);
        int newLen = 2 * count + len;
        // 扩容，内部使用System.arraycopy 方法实现。
        chars = Arrays.copyOf(chars, newLen);

        int index = len - 1;
        int newIndex = newLen - 1;
        while (index >= 0 && newIndex > index) {
            if (chars[index] == ' ') {
                chars[newIndex--] = '0';
                chars[newIndex--] = '2';
                chars[newIndex--] = '%';
            } else {
                chars[newIndex--] = chars[index];
            }
            index--;
        }

        return new String(chars);
    }

    /**
     * 剔除字符串首尾的空格
     *
     * @param origin
     * @return
     */
    private String trim(String origin) {
        char[] chars = origin.toCharArray();
        int length = chars.length;
        int st = 0;
        while (st < length && chars[st] == ' ') {
            st++;
        }

        while (st < length && chars[length - 1] == ' ') {
            length--;
        }

        // 如果收尾有空格，就截取生成新的字符串
        if (st > 0 || length < chars.length) {
            origin = new String(chars, st, (length - st));
        }
        return origin;
    }

    private int getSpaceCount(String sentence) {
        char[] chars = sentence.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (c == ' ') {
                count++;
            }
        }
        return count;
    }


    //#############  end  #############

}
