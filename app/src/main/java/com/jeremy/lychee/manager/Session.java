package com.jeremy.lychee.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.utils.GsonUtils;

import java.lang.reflect.Type;

/**
 * A utility class for storing and retrieving session data.
 */
public class Session {

	private static final String KEY = "session";
	private static final String KEY_USR = "usr";
	private static User mUsrInfo;
	
	public static boolean isUserInfoEmpty() {
		return Session.mUsrInfo == null;
	}
	
//	public static boolean loginIfNeeded(Context ctx) {
//		boolean res = isUserInfoEmpty();
//		if (res) {
//			ToastHelper.getInstance(ContentApplication.getInstance()).toast(R.string.no_login);
//		}
//		return res;
//	}
	
	
	public static User getSession() {
		return Session.mUsrInfo;
	}
	
	public static String getSid() {
	    if (Session.mUsrInfo != null) {
	        return Session.mUsrInfo.getSid();
	    }
	    return "";
	}
	
    /**
     * Stores the session data on disk.
     */
    public static void save(final String usrString) {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				Editor editor = ContentApplication.getInstance().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
				editor.putString(KEY_USR, usrString);
				editor.commit();
			}
		}).start();
    }
    
    public static void save(User wrapper, boolean needFlush) {
    	Session.mUsrInfo = wrapper;
    	if (needFlush) {
    		if (wrapper == null) {
    			clearSavedSession();
    		} else {
    			save(GsonUtils.toJson(wrapper));
    		}
    	}
    }

    /**
     * Loads the session data from disk.
     */
    public static User restore(Context context) {
		User wrapper = null;
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        String usrString = prefs.getString(KEY_USR, null);
        if (!TextUtils.isEmpty(usrString)) {
        	Type type = new TypeToken<User>(){}.getType();
        	try {
        		wrapper = GsonUtils.fromJson(usrString, type);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        mUsrInfo = wrapper;
        return wrapper;
    }

    /**
     * Clears the saved session data.
     */
    public static void clearSavedSession() {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				Editor editor = ContentApplication.getInstance().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
				editor.clear().commit();
			}
		}).start();
    }
}