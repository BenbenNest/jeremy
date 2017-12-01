package com.jeremy.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by changqing on 2017/11/26.
 */

public class FileUtils {

    public static boolean isImage(String path) {
        String type = path.substring(path.lastIndexOf(".") + 1, path.length());
        String[] imageType = {".jpg", ".png", ".bmp", ".gif", "jpeg"};
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
            java.io.File myDelFile = new java.io.File(filePath);
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

}
