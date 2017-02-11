package com.jeremy.lychee.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

	private Context mContext;

	private ToastHelper(Context context) {
		mContext = context;
	}

	public static ToastHelper getInstance(Context context) {
		return  new ToastHelper(context);
	}

	public void toast(int resourceId) {
		if(mContext == null){
	        return ;
	    }
		String message = mContext.getString(resourceId);
		toast(message);
	}

	public void toast(String message) {
		mContext = mContext.getApplicationContext();  //Toast 要使用ApplicationContext ，否则某些手机显示会出问题
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}

}
