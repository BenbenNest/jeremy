package com.jeremy.library.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by changqing on 2017/11/26.
 */

public class FileUtils {
    String charset_code = "utf-8";

    /**
     * 读取assets目录下的文件
     * @return
     */
    public static byte[] readAssetsFile(Context context){
        try {
            InputStream is = context.getResources().getAssets().open("asset.txt");
            byte[] bytes = new byte[is.available()];
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String str_asset = "";
            while ((str_asset = br.readLine()) != null) {

            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return null;
    }






    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            in = new FileInputStream(fileName);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {

                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void save(Context context){
        String data= "Data to save";
        FileOutputStream outputStream= null;
        BufferedWriter writer = null;
        try {
            outputStream = context.openFileOutput("data",Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(data);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String load(Context context){
        FileInputStream inputStream = null;
        BufferedReader reader =null;
        StringBuilder content=new StringBuilder();
        try {
            inputStream = context.openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while ((line=reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public byte[] getContent(String filePath) {
        File file = new File(filePath);
        if (file == null) {
            return null;
        }
        long fileSize = file.length();
        byte[] buffer = new byte[(int) fileSize];
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...; path=" + filePath);
            return null;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int offset = 0;
            int numRead ;
            while (offset < buffer.length && (numRead = fileInputStream.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        } catch (Exception e) {

        }
        return buffer;
    }

    /**
     * the traditional io way
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }




    /**
     * 文件排序:按文件最后一次被修改的时间排序
     *
     * @param dir
     */
    public static void sortFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, new FileOrederComparator());
        }
    }

    /**
     * 往文件中追加内容
     *
     * @param fileName
     * @param content
     */
    public void writeContent(String fileName, String content) {
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(randomFile!=null){
                try {
                    randomFile.close();
                }catch (IOException e){

                }
            }
        }
    }

    public static boolean isImage(String path) {
        String type = path.substring(path.lastIndexOf("."), path.length());
        String[] imageType = {".jpg", ".png", ".bmp", ".gif", ".jpeg"};
        List<String> imageTypeLists = Arrays.asList(imageType);
        if (imageTypeLists.contains(type)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 新建目录
     *
     * @param path 路径
     * @return boolean
     */
    public void newFolder(String path) {
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
        } catch (Exception e) {
            System.out.println("新建目录操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 新建文件
     *
     * @param filePathAndName String  文件路径及名称
     * @param fileContent     String  文件内容
     * @return boolean
     */
    public void newFile(String filePathAndName, String fileContent) {
        try {
            File myFilePath = new File(filePathAndName);
            /**如果文件不存在就建一个新文件*/
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);  //用来写入字符文件的便捷类, 在给出 File 对象的情况下构造一个 FileWriter 对象
            PrintWriter myFile = new PrintWriter(resultFile);  //向文本输出流打印对象的格式化表示形式,使用指定文件创建不具有自动行刷新的新 PrintWriter。
            myFile.println(fileContent);
            resultFile.close();
        } catch (Exception e) {
            System.out.println("新建文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param filePathAndName String  文件路径及名称
     * @return boolean
     */
    public void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath String  文件夹路径
     * @return boolean
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);  //删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete();  //删除空文件夹
        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String  文件夹路径
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String  原文件路径
     * @param newPath String  复制后路径
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);  //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String  原文件路径
     * @param newPath String  复制后路径
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }
    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String
     * @param newPath String
     */
    public void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String
     * @param newPath String
     */
    public void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    private void copyFile2(String source, String dest) {
        try {
            File in = new File(source);
            File out = new File(dest);
            FileInputStream inFile = new FileInputStream(in);
            FileOutputStream outFile = new FileOutputStream(out);
            byte[] buffer = new byte[1024];
            int i = 0;
            while ((i = inFile.read(buffer)) != -1) {
                outFile.write(buffer, 0, i);
            }
            inFile.close();
            outFile.close();
        } catch (Exception e) {

        }
    }

    static final String[] acceptedExtensions = new String[]{".jpg", ".png", ".bmp", ".jpeg"};

    static class ImageFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory())
                return false;
            for (String extension : acceptedExtensions) {
                if (file.getName().toLowerCase().endsWith(extension))
                    return true;
            }
            return false;
        }
    }

    static class FileOrederComparator implements Comparator<File> {
        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }

        public boolean equals(Object obj) {
            return true;
        }
    }

}
