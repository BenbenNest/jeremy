package com.jeremy.library.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.Stack;

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



    private static final int BUF_SIZE = 4096;

    public static void safeClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static final byte[] toBytes(InputStream is) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[BUF_SIZE];
            int count;
            while ((count = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, count);
            }
            baos.flush();
            return baos.toByteArray();

        } catch (IOException e) {
        } finally {
            safeClose(baos);
        }
        return null;
    }

    public static final long pipe(InputStream is, OutputStream os) {
        long total = -1;
        if (is == null || os == null) {
            return total;
        }
        try {
            byte[] buf = new byte[BUF_SIZE];
            int count = 0;
            total = 0;
            while ((count = is.read(buf)) > 0) {
                os.write(buf, 0, count);
                total += count;
            }
        } catch (IOException e) {

        }
        return total;
    }

    public static int writeToFile(byte[] data, String fileName, boolean append) {
        if (TextUtils.isEmpty(fileName)) {
            return 0;
        }
        return writeToFile(data, new File(fileName), append);
    }

    public static int writeToFile(byte[] data, File file, boolean append) {
        if (data == null || data.length == 0 || file == null || file.isDirectory()) {
            return 0;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
            fos.write(data);
            fos.flush();
            return data.length;
        } catch (IOException e) {
        } finally {
            safeClose(fos);
        }
        return 0;
    }


    public static boolean ensureDir(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File f = new File(path);
            if (f.exists() && f.isDirectory()) {
                return true;
            } else if (f.exists() && f.isFile()) {
                f.delete();
            }
            return f.mkdirs();
        } catch (Exception e) {

        }
        return false;
    }

    public static final InputStream getInputStream(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        try {
            File f = new File(fileName);
            if (f.exists() && f.isFile() && f.canRead()) {
                return new FileInputStream(f);
            }
        } catch (IOException e) {

        }
        return null;
    }

    public static void safeRecycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
        }
        bitmap = null;
    }

    public static boolean rename(String oldPath, String newPath) {
        File file = new File(oldPath);
        boolean success;
        if (file.exists()) {
            success = file.renameTo(new File(newPath));
            return success;
        }
        return false;
    }


    public static boolean deleteFileOrFolder(String rootPath) {
        if (TextUtils.isEmpty(rootPath)) {
            return false;
        }

        Stack<String> stack = new Stack<String>();
        stack.push(rootPath);
        while (!stack.isEmpty()) {
            String current = stack.peek();
            File f = new File(current);
            if (f.exists()) {
                if (f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files == null || files.length == 0) {
                        f.delete();
                        stack.pop();
                    } else {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                stack.push(file.getAbsolutePath());
                            } else {
                                file.delete();
                            }
                        }
                    }
                } else {
                    f.delete();
                    stack.pop();
                }
            } else {
                stack.pop();
            }
        }
        return true;
    }



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
