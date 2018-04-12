package com.jeremy.library.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.jeremy.library.permission.PermissionUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by changqing.zhao on 2017/5/9.
 */

public class DeviceUtils {

    private DeviceUtils() {

    }

    public static DeviceInfo getDeviceInfo(@Nullable Context context) {
        DeviceInfo deviceInfo = new DeviceInfo();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && telephonyManager != null) {
            if (PermissionUtils.checkReadPhonestatePermission(context)) {
                deviceInfo.DeviceId = telephonyManager.getDeviceId();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceInfo.IMEI = telephonyManager.getImei();
                }
                if (TextUtils.isEmpty(deviceInfo.IMEI)) {
                    deviceInfo.IMEI = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.ANDROID_ID);
                }
                deviceInfo.IMSI = telephonyManager.getSubscriberId();
                deviceInfo.MODEL = Build.MODEL;
                deviceInfo.PhoneNum = telephonyManager.getLine1Number();
            }
        } else {
            deviceInfo.DeviceId = telephonyManager.getDeviceId();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceInfo.IMEI = telephonyManager.getImei();
            }
            if (TextUtils.isEmpty(deviceInfo.IMEI)) {
                deviceInfo.IMEI = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.ANDROID_ID);
            }
            deviceInfo.IMSI = telephonyManager.getSubscriberId();
            deviceInfo.MODEL = Build.MODEL;
            deviceInfo.PhoneNum = telephonyManager.getLine1Number();
        }
        return deviceInfo;
    }

    public static class DeviceInfo {
        //DeviceId是获得国际移动身份识别码，此码是全世界唯一。IMSI是国际移动用户识别码，是区别移动用户的标志。
        String DeviceId = "";//唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID
        String IMEI = "";//IMEI 在root的手机或者其他情况，会获取失败，使用的时候判断是否为空
        String IMSI = "";//返回用户唯一标识，比如GSM网络的IMSI编号 唯一的用户ID： 例如：IMSI(国际移动用户识别码) for a GSM phone.
        String MODEL = "";//手机型号
        String PhoneNum = "";

        @Override
        public String toString() {
            return "DeviceId=" + DeviceId + "; IMEI=" + IMEI + "; IMSI=" + IMSI + "; MODEL=" + MODEL + "; PhoneNum=" + PhoneNum;
        }
    }

    /**
     * IMEI 在root的手机或者其他情况，会获取失败，使用的时候判断是否为空
     */
    public static String getIMEI(@Nullable Context context) {
        String imei = "";
        if (PermissionUtils.checkReadPhonestatePermission(context)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
            }
        }
        return imei;
    }

    /**
     * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
     * 取得手机IMEI
     */
    @Deprecated
    public static String getDeviceId(@Nullable Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && telephonyManager != null) {
            if (PermissionUtils.checkReadPhonestatePermission(context)) {
                deviceId = telephonyManager.getDeviceId();
            }
        } else {
            deviceId = telephonyManager.getDeviceId();
        }
        return deviceId;
    }


    /**
     * 取得手机IMSI
     * 返回用户唯一标识，比如GSM网络的IMSI编号 唯一的用户ID： 例如：IMSI(国际移动用户识别码) for a GSM phone.
     * 需要权限：READ_PHONE_STATE
     */
    public static String getIMSI(@Nullable Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && telephonyManager != null) {
            if (PermissionUtils.checkReadPhonestatePermission(context)) {
                imsi = telephonyManager.getSubscriberId();
            }
        } else {
            imsi = telephonyManager.getSubscriberId();
        }
        return imsi;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    //{"device_id": "your_device_id", "mac": "your_device_mac"}
    public static String getDeviceInfoS(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
