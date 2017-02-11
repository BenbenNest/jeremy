package com.jeremy.lychee.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jeremy.lychee.widget.shareWindow.ShareConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 从sd卡获取|保存图片
 *
 * @author Ying
 *
 */
public class ImageToSdcard {
	/**
	 * 从sdcard获取图片
	 *
	 * @param imageUrl
	 */
	public static Bitmap getBitmap(String imageUrl, int type) {
		if (TextUtils.isEmpty(imageUrl)){
			return null;
		}
		if(!isExist(imageUrl)){
			return null;
		}
		if (AppUtil.isMounted()) {
			String pathName = getFilePath(imageUrl);
			BitmapFactory.Options opts = OptionsManager.getBitmapOptions(pathName, type);
			return BitmapFactory.decodeFile(pathName, opts);
		}
		return null;
	}
	/**
	 * 获取图片数据流
	 * @param imageUrl
	 * @return
	 */
	public static InputStream getBitmapStream(String imageUrl) {
		if (imageUrl == null)
			return null;
		if(!isExist(imageUrl)){
			return null;
		}
		if (AppUtil.isMounted()) {
			try {
				String pathName = getFilePath(imageUrl);
				return new FileInputStream(pathName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 从sdcard获取图片
	 *
	 * @param
	 * @param type   OptionsManager.BIG_IMAGE | OptionsManager.THUMB_IMAGE
	 */
	public static Bitmap getBitmapFromPath(String pathName, int type) {
		if (pathName == null)
			return null;
		File file = new File(pathName);
		if(!file.exists()){
			return null;
		}
		if (AppUtil.isMounted()) {
			BitmapFactory.Options options = OptionsManager.getBitmapOptions(pathName, type);
			return BitmapFactory.decodeFile(pathName, options);
		}
		return null;
	}

	/**
	 * 获取图片文件的Dir
	 *
	 * @return fileDir
	 */
	public static String getFileDir() {
		String dir = AppUtil.getAppSdRootPath() + ShareConfig.SD_IMAGE_PATH + File.separator;
		return dir;
	}
	/**
	 * 获取图片name
	 * @param imageUrl
	 * @return
	 */
	public static String getFileName(String imageUrl) {
		if(TextUtils.isEmpty(imageUrl)){
			return null;
		}

		String fileName = AppUtil.getMD5code(imageUrl);
		String type = ".jpg";
		if(imageUrl.contains(".gif")){
			type = ".gif";
		}else if(imageUrl.contains(".webp")){
			type = ".webp";
		}else if(imageUrl.contains(".png")){
			type = ".png";
		}
		fileName += type;
		return fileName;
	}
	/**
	 * 获取图片path
	 * @param imageUrl
	 * @return
	 */
	public static String getFilePath(String imageUrl) {
		String path = getFileDir() + getFileName(imageUrl);
		return path;
	}

	/**
	 * 保存图片到sd卡
	 *
	 * @param imageUrl
	 * @param data
	 * @return 是否保存成功
	 */
	public static String saveFile(String imageUrl, byte[] data) {
		if (AppUtil.isMounted() && data != null) {
			String dirPath = getFileDir();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = getFileName(imageUrl);
			File file = new File(dirPath, fileName);
			String path = file.getAbsolutePath();
			FileOutputStream fileOutputStream = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(data);
				return path;
			} catch (Exception e) {
				file.delete();
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				} catch (Exception e) {
					file.delete();
					e.printStackTrace();
				}
			}

		}
		return null;
	}
	/**
	 * 保存图片到sd卡
	 * @param imageUrl
	 * @param inStream  完成保存是不关闭
	 * @return
	 */
	public static String saveInputStream(String imageUrl, InputStream inStream) {
		if (AppUtil.isMounted()&&inStream!=null) {
			String dirPath = getFileDir();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = getFileName(imageUrl);
			File file = new File(dirPath, fileName);
			FileOutputStream fileOutputStream = null;
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fileOutputStream = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				while(inStream.read(buffer)!=-1){
					fileOutputStream.write(buffer);
				}
				return file.getAbsolutePath();
			} catch (Exception e) {
				file.delete();
				e.printStackTrace();
			} finally {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					if (fileOutputStream != null) {
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		return null;
	}

	/**
	 * 判断图片是否存在
	 *
	 * @param imageUrl
	 * @return
	 */
	public static boolean isExist(String imageUrl) {
		if (imageUrl == null)
			return false;
		if (AppUtil.isMounted()) {
			String pathName = getFilePath(imageUrl);
			File file = new File(pathName);
			return file.exists();
		}
		return false;
	}

	/**
	 * 删除目录下的文件
	 *
	 * @param file
	 * @return
	 */
	public static boolean deleteDir(File file) {
		boolean success = true;

		if (file == null) {
			return success;
		}

		if (!file.exists()) {
			return success;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list == null) {
				return success;
			}
			for (File childFile : list) {
				success = deleteDir(childFile) ? success : false;
			}
		}

		return success;
	}

	/**
	 * 统计目录文件数量
	 *
	 * @param file
	 * @return
	 */
	public static int countFile(File file) {
		int count = 0;

		if (file == null) {
			return 0;
		}

		if (!file.exists()) {
			return count;
		}

		if (file.isFile()) {
			return 1;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list == null) {
				return count;
			}
			for (File childFile : list) {
				count += countFile(childFile);
			}
		}

		return count;
	}

	/*
	 * 根据图片url获取图片的路径
	 */
	public static String getPathByUrl(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl)){
			return null;
		}
		if (!AppUtil.isMounted()) {
			return null;
		}
		if(!ImageToSdcard.isExist(imageUrl)) {
			return null;
		}
		String pathName = getFilePath(imageUrl);
		File file = new File(pathName);
		return file.getAbsolutePath();
	}
	/*
	 * 根据图片url获取图片的路径
	 */
	public static File getFileByUrl(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl)){
			return null;
		}
		if (!AppUtil.isMounted()) {
			return null;
		}
		String pathName = getFilePath(imageUrl);
		File file = new File(pathName);
		if(file!=null&&file.exists()){
			return file;
		}
		return null;
	}
}
