package com.jeremy.lychee.utils;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.jeremy.lychee.base.ApplicationStatus;


/**
 * BitmapOptions管理器
 * @author Ying
 *
 */
public class OptionsManager {
	//原图***
	public static final int ORIGINAL_IMAGE = -1;
	//大图***
	public static final int BIG_IMAGE = 0;
	//缩略图
	public static final int THUMB_IMAGE = 1;
	//微信THUMB_IMAGE
	public static final int WEIXIN_THUMB_IMAGE = 2;
	/**
	 * 获取BitmapOptions
	 * @param data
	 * @return Options
	 */
	public static BitmapFactory.Options getBitmapOptions(byte[] data, int type){
		if(data==null){
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		return doneOptions(options, type);
	}
	/**
	 * 根据文件path加载BitmapOptions
	 * @param pathName
	 * @return Options
	 */
	public static BitmapFactory.Options getBitmapOptions(String pathName, int type){
		Log.v("sub","pathname  "+pathName);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		return doneOptions(options, type);
	}
	/**
	 * 设定BitmapOptions
	 * @param options
	 * @return Options
	 */
	private static BitmapFactory.Options doneOptions(BitmapFactory.Options options, int type) {
		options.inJustDecodeBounds = false;
		int w = options.outWidth;
		int h = options.outHeight;
		int inSampleSize = 1;
		
		int maxSize = 1080*1920;
		if(type==THUMB_IMAGE){
			maxSize = 320*320;
		}
		
		if(type == WEIXIN_THUMB_IMAGE){
			maxSize = 100*100;
		}
		
		Context context = ApplicationStatus.getApplicationContext();
		if(context!=null){
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics displayMetrics = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(displayMetrics);
			int sW = displayMetrics.widthPixels;
			int sH = displayMetrics.heightPixels;
			if(sW!=0&&sH!=0){
				maxSize = sW * sH * 2;
			}
			if(type==THUMB_IMAGE){
				if(sW!=0&&sH!=0){
					maxSize = sW * sH/4;
				}
			}
			if(type == WEIXIN_THUMB_IMAGE){
				if(sW!=0&&sH!=0){
					maxSize = sW * sH/32;
				}
			}
		}
		while(w*h/(inSampleSize*inSampleSize)>maxSize){
			inSampleSize*=2;
		}
		options.inSampleSize = inSampleSize;
		//原始图片不限定大小
//		if(type != ORIGINAL_IMAGE){
//			options.inSampleSize = inSampleSize;
//		}
		options.inPreferredConfig = Config.RGB_565;
		return options;
	}
}
