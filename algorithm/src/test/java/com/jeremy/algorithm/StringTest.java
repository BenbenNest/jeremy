package com.jeremy.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class StringTest {

    @Test
    public void string_test(){
//        String source = "www google com ";
//        StringAlgorithm.reverse_suite(source);

//        StringAlgorithm.stringFilter("abacbedgb");
//
//        String[] strArr = new String[]{"flower","flow","flight"};
//
//        String s = longestCommonSubStr(strArr);
//        s=s;

        permutation("ab");
        if(res!=null && res.size()>0){
            for(String s : res){
                System.out.println(s+"/n");
            }
        }

    }


    /**
     * 题目描述
     * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。例如输入字符串abc,则打印出由字符a,b,c所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。
     *
     * 输入描述:
     * 输入一个字符串,长度不超过9(可能有字符重复),字符只包括大小写字母。
     *
     * 解题思路
     * 刚看题目的时候，可能会觉得这个问题很复杂，不能一下子想出解决方案。那我们就要学会把复杂的问题分解成小问题。我们求整个字符串的排列，其实可以看成两步：
     *
     * 第一步求所有可能出现在第一个位置的字符（即把第一个字符和后面的所有字符交换[相同字符不交换]）；
     * 第二步固定第一个字符，求后面所有字符的排列。这时候又可以把后面的所有字符拆成两部分（第一个字符以及剩下的所有字符），依此类推。这样，我们就可以用递归的方法来解决。
     */
    ArrayList<String> res = new ArrayList<>();
    public ArrayList<String> permutation(String str) {
        if(str == null)
            return res;
        permutationHelper(str.toCharArray(), 0);
        Collections.sort(res);
        return res;
    }
    public void permutationHelper(char[] str, int i){
        if(i == str.length - 1){
            res.add(String.valueOf(str));
        }else{
            for(int j = i; j < str.length; j++){
                if(j!=i && str[i] == str[j])
                    continue;
                swap(str, i, j);
                permutationHelper(str, i+1);
                swap(str, i, j);
            }
        }
    }
    public void swap(char[] str, int i, int j) {
        if(i==j)return;
        char temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }




    /**
     * 最长公共子串
     * @param strs
     * @return
     */
    public String longestCommonSubStr(String[] strs) {
        if (strs.length == 0)
            return "";
        String ans = strs[0];
        int len = strs.length;
        for (int i = 1; i < len; i++)
            while (strs[i].indexOf(ans) != 0)
                ans = ans.substring(0, ans.length() - 1);
        return ans;
    }


    /**
     * 最长公共前缀
     * 首先对字符串数组进行排序，然后拿数组中的第一个和最后一个字符串进行比较，
     * 从第 0 位开始，如果相同，把它加入 res 中，不同则退出。最后返回 res
     * @param strs
     * @return
     */
    public String longestCommonPrefix(String[] strs) {
        if(strs == null || strs.length == 0)
            return "";
        Arrays.sort(strs);
        char [] first = strs[0].toCharArray();
        char [] last = strs[strs.length - 1].toCharArray();
        StringBuilder res = new StringBuilder();
        int len = first.length < last.length ? first.length : last.length;
        int i = 0;
        while(i < len){
            if(first[i] == last[i]){
                res.append(first[i]);
                i++;
            }
            else
                break;
        }
        return res.toString();
    }

    /**
     * 最长回文串
     * 统计字母出现的次数即可，双数才能构成回文。因为允许中间一个数单独出现，比如“abcba”，所以如果最后有字母落单，总长度可以加 1。
     * @param s
     * @return
     */
    public int longestPalindrome(String s) {
        HashSet<Character> hs = new HashSet<>();
        int len = s.length();
        int count = 0;
        if(len == 0)
            return 0;
        for(int i = 0; i<len; i++){
            if(hs.contains(s.charAt(i))){
                hs.remove(s.charAt(i));
                count++;
            }else{
                hs.add(s.charAt(i));
            }
        }
        return hs.isEmpty() ? count * 2 : count * 2 + 1;
    }

    /**
     * 验证回文串
     * 两个指针比较头尾。要注意只考虑字母和数字字符，可以忽略字母的大小写。
     * 可以考虑一下不只是考虑字母和数组，也不忽略大小写的情况
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        if(s.length() == 0)
            return true;
        int l = 0, r = s.length() - 1;
        while(l < r){
            if(!Character.isLetterOrDigit(s.charAt(l))){
                l++;
            }else if(!Character.isLetterOrDigit(s.charAt(r))){
                r--;
            }else{
                if(Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r)))
                    return false;
                l++;
                r--;
            }
        }
        return true;
    }


    /**
     * 最长回文子串
     * 以某个元素为中心，分别计算偶数长度的回文最大长度和奇数长度的回文最大长度。
     */
    private int index, len;
    public String longestPalindromeSub(String s) {
        if(s.length() < 2)
            return s;
        for(int i = 0; i < s.length()-1; i++){
            PalindromeHelper(s, i, i);
            PalindromeHelper(s, i, i+1);
        }
        return s.substring(index, index+len);
    }
    public void PalindromeHelper(String s, int l, int r){
        while(l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)){
            l--;
            r++;
        }
        if(len < r - l - 1){
            index = l + 1;
            len = r - l - 1;
        }
    }



}
