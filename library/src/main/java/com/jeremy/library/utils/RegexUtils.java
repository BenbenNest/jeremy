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


    /**
     * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式:验证手机号
     */
    public static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 正则表达式:验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";

    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";

    /**
     * 正则表达式:验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式:验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUserName(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPAddress(String ipAddress) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddress);
    }


}
