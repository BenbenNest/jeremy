package com.jeremy.library.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by changqing on 2017/9/13.
 * 我们在 Java 中经常会碰到如何把 InputStream 转换成 String 的情形，比如从文件或网络得到一个 InputStream，需要转换成字符串输出或赋给别的变量。
 * <p>
 * 未真正关注这个问题之前我常用的办法就是按字节一次次读到缓冲区，或是建立 BufferedReader 逐行读取。
 * 其实大可不必费此周折，我们可以用 Apache commons IOUtils，或者是 JDK 1.5 后的 Scanner，
 * 还可用 Google  Guava 库的 CharStreams。
 * 到了 JDK7，若要从文件中直接得到字符串还能用 java.nio.file.Files#readAllLines 和 java.nio.file.Files#readAllBytes 方法。
 * <p>
 * 注意处理输入输出流时有涉及到字符集，字符集乱了就乱码了，
 * 默认字符集是 System.getProperty("file.encoding")，
 * 通常我们都用 UTF-8，异常 UnsupportedEncodingException 继承自 IOException。
 */

public class IOUtils {

    public static String getStringFromStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int readBytes = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((readBytes = inputStream.read(buffer)) > 0) {
            stringBuilder.append(new String(buffer, 0, readBytes));
        }
        return stringBuilder.toString();
    }

    public static String getStringFromStreamByScanner(InputStream inputStream) throws FileNotFoundException {
//        InputStream inputStream = new FileInputStream("d:/sample.txt");
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }

    public static String getStringFromStreamByBufferReader(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean firstLine = true;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!firstLine) {
                stringBuilder.append(System.getProperty("line.separator"));
            } else {
                firstLine = false;
            }
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
