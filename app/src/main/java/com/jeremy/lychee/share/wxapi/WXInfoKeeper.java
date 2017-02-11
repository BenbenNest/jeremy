//package com.qihoo.lianxian.share.wxapi;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.text.TextUtils;
//import android.util.Base64;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import SDKUtils;
//
//import java.lang.reflect.Type;
//
///**
// * 微信相关信息读写类
// *
// * @author zourongbo
// *
// */
//public class WXInfoKeeper {
//    private static final String PREFERENCES_NAME = "weixin_android";
//    private static final String KEY_USER_INFO_WX = "userJson4WX";
//    private static final String KEY_TOKEN_INFO_WX = "tokenJson4WX";
//
//
//    /**
//     * 保存微信用户信息
//     *
//     * @param context
//     * @param userJson
//     */
//    public static void saveUserInfo4WX(Context context, String userJson) {
//        if (!TextUtils.isEmpty(userJson)) {
//            userJson = Base64.encodeToString(userJson.getBytes(), Base64.DEFAULT);
//        }
//        SharePreferenceUtil.saveStringValueMultiProcess(context, PREFERENCES_NAME, KEY_USER_INFO_WX, userJson);
//    }
//
//    /**
//     * 保存微信TOKEN信息
//     *
//     * @param context
//     * @param userJson
//     */
//    public static void saveTokenInfo4WX(Context context, String tokenJson) {
//        if (!TextUtils.isEmpty(tokenJson)) {
//        	tokenJson = Base64.encodeToString(tokenJson.getBytes(), Base64.DEFAULT);
//        }
//        SharePreferenceUtil.saveStringValueMultiProcess(context, PREFERENCES_NAME, KEY_TOKEN_INFO_WX, tokenJson);
//    }
//
//    public static WXToken getTokenInfo4WX(Context context) {
//    	WXToken wxToken = null;
//    	if(context != null) {
//    		try {
//                String info = SharePreferenceUtil.getStringValueMultiProcess(context, PREFERENCES_NAME, KEY_TOKEN_INFO_WX);
//                if (TextUtils.isEmpty(info)) {
//                    return wxToken;
//                }
//                info = new String(Base64.decode(info, Base64.DEFAULT));
//                Gson gson = Application.getGson();
//                Type type = new TypeToken<WXToken>() {}.getType();
//                wxToken = gson.fromJson(info, type);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    	}
//    	return wxToken;
//    }
//
//    /**
//     * 获取微信用户信息
//     *
//     * @param context
//     * @return
//     */
//    public static WXUser getUserInfo4WX(Context context) {
//    	WXUser user = null;
//        if (context == null) {
//            return user;
//        }
//        try {
//            String info = SharePreferenceUtil.getStringValueMultiProcess(context, PREFERENCES_NAME, KEY_USER_INFO_WX);
//            if (TextUtils.isEmpty(info)) {
//                return user;
//            }
//            info = new String(Base64.decode(info, Base64.DEFAULT));
//            Gson gson = Application.getGson();
//            Type type = new TypeToken<WXUser>() {}.getType();
//            user = gson.fromJson(info, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
//    }
//
//    /**
//     * 删除微信用户的信息
//     *
//     * @param context
//     */
//    @SuppressLint("InlinedApi")
//    public static void clearWeiXinInfo(Context context) {
//        SharedPreferences sp = null;
//        if (SDKUtils.hasHoneycomb()) {
//            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
//        } else {
//            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//        }
//        Editor editor = sp.edit();
//        editor.remove(KEY_USER_INFO_WX);
//        editor.remove(KEY_TOKEN_INFO_WX);
//        editor.commit();
//    }
//}
