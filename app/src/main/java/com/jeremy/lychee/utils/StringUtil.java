
package com.jeremy.lychee.utils;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

public class StringUtil {

    public static String getString(byte[] bytes) {
        String result = "";
        try {
            result = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String convertFromUTF(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }


    public static String MD5Encode(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] digest = md.digest();
            String text;
            for (int i = 0; i < digest.length; i++) {
                text = Integer.toHexString(0xFF & digest[i]);
                if (text.length() < 2) {
                    text = "0" + text;
                }
                hexString.append(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    /**
     * @param url
     * @return
     */
    public static String splitUrlString(String url) {
        if (!TextUtils.isEmpty(url)) {
            int firstparam = url.indexOf("?");
            if (firstparam > 0) {
                return url.substring(0, firstparam);
            } else {
                return url;
            }
        }
        return "";
    }


    public static String MD5Encode(String text) {
        return MD5Encode(text.getBytes());
    }

    public static String MD5Sum(String filename) {
        InputStream fis = null;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            return toHexString(md5.digest());
        } catch (Exception e) {
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final char HEX_DIGITS[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'
    };

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String toUtf8(String str) {
        try {
            byte[] strb = null;
            strb = str.getBytes("UTF-8");
            String newStr = new String(strb);
            return newStr;
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public static boolean isChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char word = str.charAt(i);
            if ((word >= 0x4e00) && (word <= 0x9fbb)) {
                continue;// �Ǻ���
            }
            return false;
        }
        return true;
    }

    public static boolean isContainChinese(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }

        int len = str.length();
        for (int i = 0; i < len; i++) {
            char word = str.charAt(i);
            if ((word >= 0x4e00) && (word <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContainMessyCode(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }

        int len = str.length();
        for (int i = 0; i < len; i++) {
            char word = str.charAt(i);
            if (word > 0xffef) {
                return true;
            }
        }
        return false;
    }

    public static int charLength(String str) {
        int size = str.length();
        int len = 0;
        for (int i = 0; i < size; i++) {
            char c = str.charAt(i);
            if ((c >= 0x4e00) && (c <= 0x9fbb)) {
                len += 2;// �Ǻ���
            } else {
                len += 1;
            }
        }
        return len;
    }

    /**
     * strip any prefix/suffix "http://yahoo.com/" -> "yahoo.com"
     *
     * @param url
     * @return
     */
    public final static String stripUrlProtocolHeader(String url) {
        if (url.startsWith("http://")) {
            url = url.substring(7);
        } else if (url.startsWith("https://")) {
            url = url.substring(8);
        }
        final int length = url.length();
        if (url.charAt(length - 1) == '/') {
            url = url.substring(0, length - 1);
        }
        return url;
    }

    public final static String null2Empty(String str) {
        return str == null ? "" : str;
    }

    public final static String byteArrayToString(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) {
            return "null";
        }
        StringBuffer sb = new StringBuffer();
        String tmpString;
        sb.append("[");
        for (int i = 0; i < byteArray.length; i++) {
            tmpString = Integer.toHexString(i);
            if (tmpString.length() == 1) {
                tmpString = "0" + tmpString;
            }
            if (i != 0) {
                sb.append(",");
            }
            sb.append(tmpString);
        }
        sb.append("]");
        return sb.toString();
    }

    public static String toUnicode(String strText) {
        char c;
        String strRet = "";
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            if (intAsc > 128) {
                strHex = Integer.toHexString(intAsc);
                strRet = strRet + "\\u" + strHex;
            } else {
                strRet = strRet + c;
            }
        }
        return strRet;
    }

    public static boolean isInvalid(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }

        int len = str.length();
        char[] buffer = new char[len];
        str.getChars(0, len, buffer, 0);
        for (int i = 0; i < len; i++) {
            char c = buffer[i];
            if (!Character.isLetterOrDigit(c) && ((int) c != '_') && ((int) c != '-')
                    && ((int) c != '.') && ((int) c != ',') && ((int) c != '&') && ((int) c != '=')
                    && ((int) c != '(') && ((int) c != ')')) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInvalidFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return true;
        }

        char[] buffer = fileName.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            char c = buffer[i];
            if (!Character.isLetterOrDigit(c) &&
                    (((int) c == '\\') ||
                            ((int) c == '/') ||
                            ((int) c == ':') ||
                            ((int) c == '*') ||
                            ((int) c == '?') ||
                            ((int) c == '\"') ||
                            ((int) c == '<') ||
                            ((int) c == '>') ||
                            ((int) c == '|') ||
                            ((int) c == '#') ||
                            ((int) c == '$') ||
                            ((int) c == '%'))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == b) return true;
        if (a != null && b != null) {
            return a.equalsIgnoreCase(b);
        }
        return false;
    }

    public static boolean guessFileIsUTF8(String path) {
        FileInputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);

            byte[] buffer = new byte[1024 * 5];
            int length = in.read(buffer);
            int pollutedLength = 10;

            if (length - pollutedLength > 0) {
                // copyOfRange�滻Arrays.copyOf -->api level 9
                byte[] templateBytes = copyOfRange(buffer, 0, length);
                byte[] sourceBytes = copyOfRange(templateBytes, 0, length - pollutedLength);
                byte[] utfEncodedBytes = copyOfRange(new String(templateBytes, 0, length)
                        .getBytes("UTF-8"), 0, length - pollutedLength);
                byte[] gbkEncodedBytes = copyOfRange(new String(templateBytes, 0, length)
                        .getBytes("GBK"), 0, length - pollutedLength);

                if (Arrays.equals(sourceBytes, utfEncodedBytes)
                        && !Arrays.equals(sourceBytes, gbkEncodedBytes)) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static byte[] copyOfRange(byte[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        byte[] result = new byte[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.trim().length() == 0)
            return true;
        else
            return false;
    }

    public static String getCuttedDescrString(TextView textView, String txt, int length) {
        if (txt == null) {
            return "";
        }

        Paint paint = textView.getPaint();
        paint.setTextSize(textView.getTextSize());
        String temp = (String) TextUtils.ellipsize(txt, (TextPaint) paint, length, TextUtils.TruncateAt.END);

        return temp;
    }

    public static String removeBlank(String str) {
        String regStartSpace = "^[ ]*";
        String regEndSpace = "[ ]*$";
        if (str == null) {
            return null;
        }
        return str.replaceAll(regStartSpace, "").replaceAll(regEndSpace, "");
    }
}
