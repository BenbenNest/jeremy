package com.jeremy.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
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

    /**
     * 获取状态栏高度(通过系统尺寸资源获取)
     *
     * @return
     */
    public static int getStatusBarHeight1(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取状态栏高度(通过R类的反射)
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取状态栏高度(借助应用区域的top属性)
     * 注意*该方法不能在初始化的时候用
     * *注意* 如果单单获取statusBar高度而不获取titleBar高度时，这种方法并不推荐大家使用，因为这种方法依赖于WMS（窗口管理服务的回调）。
     * 正是因为窗口回调机制，所以在Activity初始化时执行此方法得到的高度是0，这就是很多人获取到statusBar高度为0的原因。
     * 这个方法推荐在回调方法onWindowFocusChanged()中执行，才能得到预期结果。
     *
     * @return
     */
    public static int getStatusBarHeight3(Activity context) {
        Rect rectangle = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    /**
     * 获取状态栏高度——方法4
     * 状态栏高度 = 屏幕高度 - 应用区高度
     * *注意*该方法同样不能在初始化的时候用
     */
    public static int getStatusBarHeight4(Activity context) {
        //屏幕
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //应用区域
        Rect outRect1 = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        int statusBar = dm.heightPixels - outRect1.height();  //状态栏高度=屏幕高度-应用区域高度
        return statusBar;
    }

    public static int getTibleBarHeight(Activity activity) {
        //屏幕
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        //View绘制区域
        Rect outRect2 = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect2);
        Log.e("jeremy", "View绘制区域顶部-错误方法：" + outRect2.top);   //不能像上边一样由outRect2.top获取，这种方式获得的top是0，可能是bug吧
        int viewTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();   //要用这种方法
        Log.e("jeremy", "View绘制区域顶部-正确方法：" + viewTop);
        Log.e("jeremy", "View绘制区域高度：" + outRect2.height());
        /**
         * 获取标题栏高度-方法1
         * 标题栏高度 = View绘制区顶端位置 - 应用区顶端位置(也可以是状态栏高度，获取状态栏高度方法3中说过了)
         * */
        int titleHeight1 = viewTop - outRect1.top;
        Log.e("WangJ", "标题栏高度-方法1：" + titleHeight1);
        /**
         * 获取标题栏高度-方法2
         * 标题栏高度 = 应用区高度 - View绘制区高度
         * */
        int titleHeight2 = outRect1.height() - outRect2.height();
        Log.e("WangJ", "标题栏高度-方法2：" + titleHeight2);
        return titleHeight1;
    }


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

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.density;
        } catch (Throwable e) {
            e.printStackTrace();
            return 1.0f;
        }
    }

    public static float dip2pxFloat(Context context, float dp) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static int dip2px(Context context, float dp) {
        return (int) (dip2pxFloat(context, dp) + 0.5f);
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
