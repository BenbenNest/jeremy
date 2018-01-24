package com.jeremy.library.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by changqing on 2018/1/13.
 */

public class RegexUtils {


    public static void main(String[] args) {
        getStrings(); //用正则表达式获取指定字符串内容中的指定内容
        System.out.println("********************");
        replace(); //用正则表达式替换字符串内容
        System.out.println("********************");
        strSplit(); //使用正则表达式切割字符串
        System.out.println("********************");
        strMatch(); //字符串匹配
    }

    /**
     * 查找crash异常中的位置，如果是混淆过的，查找到之后，再去混淆文件mapping.txt中去查找对应的源码
     *
     * @param error:java.lang.NullPointerException: Attempt to invoke interface method 'java.lang.String com.didi.navi.b.b.m.g()' on a null object reference
     * @return
     */
    public String findLastPosition(String error) {
//        \"(.*)\"
        Pattern p = Pattern.compile("\\'(.*)\\'");
        Matcher m = p.matcher(error);
        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(1));
        }
        for (String s : list) {
            System.out.println(s);
        }
        if (list.size() > 0) {
            error = list.get(0).substring(1, list.get(0).length() - 2);
            printMsg(error);

        }
        return "";
    }

    public void printMsg(String msg) {
        System.out.println("********************" + msg + "********************");
    }

    private static void strMatch() {
        String phone = "13539770000";
        //检查phone是否是合格的手机号(标准:1开头，第二位为3,5,8，后9位为任意数字)
        System.out.println(phone + ":" + phone.matches("1[358][0-9]{9,9}")); //true

        String str = "abcd12345efghijklmn";
        //检查str中间是否包含12345
        System.out.println(str + ":" + str.matches("\\w+12345\\w+")); //true
        System.out.println(str + ":" + str.matches("\\w+123456\\w+")); //false
    }

    private static void strSplit() {
        String str = "asfasf.sdfsaf.sdfsdfas.asdfasfdasfd.wrqwrwqer.asfsafasf.safgfdgdsg";
        String[] strs = str.split("\\.");
        for (String s : strs) {
            System.out.println(s);
        }
    }

    private static void getStrings() {
        String str = "rrwerqq84461376qqasfdasdfrrwerqq84461377qqasfdasdaa654645aafrrwerqq84461378qqasfdaa654646aaasdfrrwerqq84461379qqasfdasdfrrwerqq84461376qqasfdasdf";
        Pattern p = Pattern.compile("qq(.*?)qq");
        Matcher m = p.matcher(str);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));
        }
        for (String s : strs) {
            System.out.println(s);
        }
    }

    private static void replace() {
        String str = "asfas5fsaf5s4fs6af.sdaf.asf.wqre.qwr.fdsf.asf.asf.asf";
        //将字符串中的.替换成_，因为.是特殊字符，所以要用\.表达，又因为\是特殊字符，所以要用\\.来表达.
        str = str.replaceAll("\\.", "_");
        System.out.println(str);
    }


}
