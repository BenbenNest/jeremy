package com.jeremy.library.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by benbennest on 16/9/28.
 */
public class ScreenUtil {
    static TelephonyManager sTelephonyManager;
    static WifiManager sWifiManager;

    public static String getDeviceSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    /**
     * 获取屏幕信息
     *
     * @param activity
     * @return
     */
    public static String getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density + "," + dm.widthPixels + "," + dm.heightPixels;
    }

    // Suppress default constructor for noninstantiability
    private ScreenUtil() {
        throw new AssertionError();
    }

    ;

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int w = displayMetrics.widthPixels;
        if (w <= 0) {
            w = 720;
        }
        return w;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int w = displayMetrics.heightPixels;
        if (w <= 0) {
            w = 1280;
        }
        return w;
    }

    public static String getSubscriberId(Context ctx) {
        if (sTelephonyManager == null) {
            sTelephonyManager = (TelephonyManager) ctx
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }
        return sTelephonyManager.getSubscriberId();
    }


    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public static String getProvidersName(Context ctx) {
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String imsi = getSubscriberId(ctx);
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(imsi);
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (imsi.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (imsi.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 获取wifi ssid
     *
     * @param ctx
     * @return
     */
    public static String getWifiSSID(Context ctx) {
        if (sWifiManager == null) {
            sWifiManager = (WifiManager) ctx
                    .getSystemService(Context.WIFI_SERVICE);
        }
        String ssid = sWifiManager.getConnectionInfo().getSSID();
        if (Build.VERSION.SDK_INT >= 17) {
            //取出双引号
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
        }
        return ssid;
    }

    /**
     * 获取wifi ip
     *
     * @return
     */
    public static String getWifiIpAddress(Context ctx) {
        if (sWifiManager == null) {
            sWifiManager = (WifiManager) ctx
                    .getSystemService(Context.WIFI_SERVICE);
        }
        // 获取32位整型IP地址
        int ipAddress = sWifiManager.getConnectionInfo().getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    /**
     * 获取mobile ip
     *
     * @return
     */
    public static String getCarrierIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
