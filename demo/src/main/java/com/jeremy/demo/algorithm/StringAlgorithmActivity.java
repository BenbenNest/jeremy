package com.jeremy.demo.algorithm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.jeremy.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 常见的字符串算法集合
 */
public class StringAlgorithmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_algorithm);



    }

    /**
     * 求字符串中最长的无重复子串
     */
    public static List getLongestNoRepeatSubString(String s) {
        char[] arr = s.toCharArray();
        int len = arr.length;//数组长
        int max;//最长子段长度
        int start;//最长子段的开始位置
        max = start = 0;
        List<String> subList = new ArrayList<String>();//最长的子串可能会有多少，用这个临时保存
        Map<Character, Integer> charMap = new HashMap();//保存对应字符及其最后出现的位置

        int i;
        for (i = 0; i < len; i++) {
            if (!charMap.containsKey(arr[i])) {//不包含的话就加入到map中
                charMap.put(arr[i], i);
            } else {//包含的话就得处理了,看在重复出现前的子串是否是更长
                int pre = charMap.get(arr[i]);
                if (pre < start)
                    continue;
                int tempLen = i - start;//当前子串长度
                if (tempLen >= max) {//找到新的最长子串了
                    if (tempLen > max) subList.clear();//需要刷新列表
                    max = tempLen;
                    subList.add(s.substring(start, i));//保存这个最长子串
                    charMap.put(arr[i], i);//置成新的位置
                } else {
                    charMap.put(arr[i], i);//置成新的位置
                }
                start = pre + 1;//只要重复了，都得重新开始设置start的位置(如果遇到重复字符x，就将 start的位置 改为上一个x 位置+1.)
            }
        }
        if (i - start >= max) {
            if (i - start > max) subList.clear();//这个主要是处理这种情况：abcd 没有重复的~~
            subList.add(s.substring(start, i));
        }
        return subList;
    }


    /**
     * 求字符串的最长回文子字符串
     *
     * @return
     */
    public static String getLongestPalinDrome(String s) {
        if (TextUtils.isEmpty(s)) return "";
        int len = s.length();
        String longest = s.substring(0, 1);
        for (int i = 0; i < len - 1; i++) {
            String s1 = getLongestPalinDrome(s, i, i);
            if (s1.length() > longest.length()) longest = s1;
            String s2 = getLongestPalinDrome(s, i, i + 1);
            if (s2.length() > longest.length()) longest = s2;
        }
        return longest;
    }

    public static String getLongestPalinDrome(String s, int left, int right) {
        if (left != right && s.charAt(left) != s.charAt(right)) return "";
        int len = s.length();
        while (left > 0 && right < len - 1 && s.charAt(left) == s.charAt(right) && s.charAt(left - 1) == s.charAt(right + 1)) {
            left--;
            right++;
        }
        return s.substring(left, right + 1);
    }

    /**
     * 字符串的递归反转
     *
     * @return
     */
    public static String stringReverseRecurse(String s) {
        if (TextUtils.isEmpty(s)) return "";
        return s.charAt(s.length() - 1) + stringReverseRecurse(s.substring(0, s.length() - 1));
    }

    public static String stringReverse(String s) {
        if (TextUtils.isEmpty(s)) return "";
        StringBuffer sb = new StringBuffer();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            stack.push(s.charAt(i));
        }
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    /**
     * 顺序输入字符，输出每个字母第一次出现的顺序，例如，输入abacbedgb,输出：abcedg
     */
    public static String stringFilter(String s) {
        if (TextUtils.isEmpty(s)) return "";
        StringBuffer sb = new StringBuffer();
        int[] arr = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int num = ch - 'a';
            if (arr[num] == 0) {
                sb.append(ch);
                arr[num] = 1;
            }
        }
        //由于Set默认会自动排序，因此下面的代码不是按先后顺序，而是按字母排序的，所以不能选择用Set
//        TreeSet<Character> set = new TreeSet<>();
//        for (int i = 0; i < s.length(); i++) {
//            set.add(s.charAt(i));
//        }
//        Iterator<Character> it = set.iterator();
//        while (it.hasNext()) {
//            char ch = it.next();
//            sb.append(ch);
//        }
//        for (Character ch : set) {
//            sb.append(ch);
//        }
        return sb.toString();
    }

    /**
     * 将字符串中连续出现的重复字母进行压缩，比如输入aaabbccccdaa，输出a3b2c4d1a2
     */
    public static String stringZip(String s) {
        if (TextUtils.isEmpty(s)) return "";

        char[] chars = s.toCharArray();
        ArrayList<Character> charList = new ArrayList<>();
        ArrayList<Integer> numList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        char pre = chars[0];
        char ch;
        for (int i = 0; i < chars.length; i++) {
            ch = chars[i];
            if (i == 0) {
                charList.add(ch);
                numList.add(1);
                pre = ch;
            } else {
                if (ch == pre) {
                    int num = numList.get(numList.size() - 1) + 1;
                    numList.set(numList.size() - 1, num);
                    pre = ch;
                } else {
                    charList.add(ch);
                    numList.add(1);
                    pre = ch;
                }
            }
        }
        for (int i = 0; i < charList.size(); i++) {
            sb.append(charList.get(i).toString() + numList.get(i).toString());
        }
        return sb.toString();
    }


}
