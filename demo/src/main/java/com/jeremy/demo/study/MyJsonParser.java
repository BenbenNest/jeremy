package com.jeremy.demo.study;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by changqing on 2018/2/22.
 */

public class MyJsonParser {

    public static void parse() {
        String json = "{name:\"jeremy\",age:30}";


        //name:"jeremy"
        //age:30
        Pattern pattern = Pattern.compile("\\w+:(\"\\w+\"|\\d+)");
        //解析value为{}json对象的方法
//        Pattern pattern = Pattern.compile("\\w+:(\"\\w+\"|\\d+|\\{\\S+\\})");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String text = matcher.group();
            int pos = text.indexOf(":");
            String key = text.substring(0, pos);
            String value = text.substring(pos + 1, text.length());
            //替换字符串开始和结束的字符串
            value = value.replaceAll("^\"|\"$", "");
//            value = value.replaceAll("^\\\"|\\\"$", "");
            System.out.print(key + " ");
            System.out.println(value);
        }


    }


}
