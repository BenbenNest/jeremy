package com.jeremy.library.utils.encrypt;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by changqing.zhao on 2017/3/21.
 *
 * 参考：Android数据加密之Base64编码算法  http://www.cnblogs.com/whoislcj/p/5887859.html
 */

public class Base64Utils {
    /**
     * BASE64编码
     */
    public static String encode(String str) throws Exception {
        String result = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        Log.v("Base64", "Base64---->" + result);
        return result;
    }

    /**
     * BASE6解码
     */
    public static String decode(String str) throws Exception {
        String result = new String(Base64.decode(str, Base64.DEFAULT));
        Log.e("Base64", "Base64---->" + result);
        return result;
    }

    /**
     * 对文件进行编码，返回编码后的字符串
     *
     * @return
     */
    public static String encodeFile(String path) {
        String result = "";
        File file = new File(path);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            result = Base64.encodeToString(buffer, Base64.DEFAULT);
            Log.e("Base64", "Base64---->" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对编码的文件字符串进行解码，并写入到文件中去
     *
     * @return
     */
    public static void decodeFile(String content, String path) {
        File desFile = new File(path);
        FileOutputStream fos = null;
        try {
            byte[] decodeBytes = Base64.decode(content.getBytes(), Base64.DEFAULT);
            fos = new FileOutputStream(desFile);
            fos.write(decodeBytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    针对Base64.DEFAULT参数说明
//
//    无论是编码还是解码都会有一个参数Flags，Android提供了以下几种
//
//    DEFAULT 这个参数是默认，使用默认的方法来加密
//
//    NO_PADDING 这个参数是略去加密字符串最后的”=”
//
//    NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
//
//    CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
//
//    URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/

}
