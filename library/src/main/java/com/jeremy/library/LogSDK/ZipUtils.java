package com.jeremy.library.LogSDK;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.jeremy.library.utils.StreamUtils.closeStream;

/**
 * Created by changqing on 2017/7/1.
 */

public class ZipUtils {

    public static final String TAG = "ZipUtils";

    public static void test() {
//        ZipCompress zipCom = new ZipCompress("D:\\电影.zip","F:\\电影");
        try {
            zip("", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zip(String sourceFileName, String zipFileName) throws Exception {
        //File zipFile = new File(zipFileName);
        System.out.println("压缩中...");
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        File sourceFile = new File(sourceFileName);
        //调用函数
        compress(out, bos, sourceFile, sourceFile.getName());
        bos.close();
        out.close();
        System.out.println("压缩完成");
    }


    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            if (flist.length == 0) {
                System.out.println(base + "/");
                out.putNextEntry(new ZipEntry(base + "/"));
            } else {
                //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], base + "/" + flist[i].getName());
                }
            }
        } else {
            //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            System.out.println(base);
            //将源文件写入到zip文件中
            while ((tag = bis.read()) != -1) {
                bos.write(tag);
            }
            bis.close();
            fos.close();
        }
    }


    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, java.util.zip.ZipOutputStream zipOutputSteam) throws Exception {
        Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");

        if (zipOutputSteam == null)
            return;

        java.io.File file = new java.io.File(folderString + fileString);

        // 判断是不是文件
        if (file.isFile()) {

            java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(
                    fileString);
            java.io.FileInputStream inputStream = new java.io.FileInputStream(
                    file);
            zipOutputSteam.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }

            zipOutputSteam.closeEntry();
        } else {

            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();

            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(
                        fileString + java.io.File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }

            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString + java.io.File.separator
                        + fileList[i], zipOutputSteam);
            }// end of for

        }// end of if

    }// end of func

    /**
     * 取得压缩包中的 文件列表(文件夹,文件自选)
     *
     * @param zipFileString  压缩包名字
     * @param bContainFolder 是否包括 文件夹
     * @param bContainFile   是否包括 文件
     * @return
     * @throws Exception
     */
    public static java.util.List<java.io.File> GetFileList(
            String zipFileString, boolean bContainFolder, boolean bContainFile)
            throws Exception {

        android.util.Log.v("XZip", "GetFileList(String)");

        java.util.List<java.io.File> fileList = new java.util.ArrayList<java.io.File>();
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
                new java.io.FileInputStream(zipFileString));
        java.util.zip.ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {

                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                java.io.File folder = new java.io.File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }

            } else {
                java.io.File file = new java.io.File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }// end of while

        inZip.close();

        return fileList;
    }

    /**
     * 返回压缩包中的文件InputStream
     *
     * @param zipFileString 压缩文件的名字
     * @param fileString    解压文件的名字
     * @return InputStream
     * @throws Exception
     */
    public static java.io.InputStream UpZip(String zipFileString,
                                            String fileString) throws Exception {
        android.util.Log.v("XZip", "UpZip(String, String)");
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFileString);
        java.util.zip.ZipEntry zipEntry = zipFile.getEntry(fileString);

        return zipFile.getInputStream(zipEntry);

    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString)
            throws Exception {
        android.util.Log.v("XZip", "UnZipFolder(String, String)");
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
                new java.io.FileInputStream(zipFileString));
        java.util.zip.ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {

                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                java.io.File folder = new java.io.File(outPathString
                        + java.io.File.separator + szName);
                folder.mkdirs();

            } else {

                java.io.File file = new java.io.File(outPathString
                        + java.io.File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                java.io.FileOutputStream out = new java.io.FileOutputStream(
                        file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }// end of while

        inZip.close();

    }// end of func

    /**
     * 压缩文件,文件夹
     *
     * @param srcFileString 要压缩的文件/文件夹名字
     * @param zipFileString 指定压缩的目的和名字
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString)
            throws Exception {
        android.util.Log.v("XZip", "ZipFolder(String, String)");

        // 创建Zip包
        java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(
                new java.io.FileOutputStream(zipFileString));

        // 打开要输出的文件
        java.io.File file = new java.io.File(srcFileString);

        // 压缩
        ZipFiles(file.getParent() + java.io.File.separator, file.getName(),
                outZip);

        // 完成,关闭
        outZip.finish();
        outZip.close();

    }// end of func


    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    /**
     * 文件压缩
     *
     * @param file
     * @param delete 是否删除原始文件
     * @throws Exception
     */
    public static void compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);
        compress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if (delete) {
            file.delete();
        }
    }

    /**
     * 数据压缩
     *
     * @param is
     * @param os
     * @throws IOException
     * @throws Exception
     */
    public static void compress(InputStream is, OutputStream os) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }
        gos.finish();
        gos.flush();
        gos.close();
    }

    /**
     * @param bt
     * @return
     * @description 将byte 数组压缩
     */
    public static byte[] compress(byte[] bt) {
        //将byte数据读入文件流
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gzipos = null;
        try {
            bos = new ByteArrayOutputStream();
            gzipos = new GZIPOutputStream(bos);
            gzipos.write(bt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(gzipos);
            closeStream(bos);
        }
        return bos.toByteArray();
    }


}
