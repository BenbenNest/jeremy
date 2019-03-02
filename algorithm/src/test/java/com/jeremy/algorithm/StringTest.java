package com.jeremy.algorithm;

import org.junit.Test;

public class StringTest {

    @Test
    public void string_test(){
        String source = "www google com ";
//        StringAlgorithm.reverse_suite(source);

        StringAlgorithm.stringFilter("abacbedgb");

        String[] strArr = new String[]{"flower","flow","flight"};

        String s = longestCommonPrefix(strArr);
        s=s;
    }

    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0)
            return "";
        String ans = strs[0];
        int len = strs.length;
        for (int i = 1; i < len; i++)
            while (strs[i].indexOf(ans) != 0)
                ans = ans.substring(0, ans.length() - 1);
        return ans;
    }

}
