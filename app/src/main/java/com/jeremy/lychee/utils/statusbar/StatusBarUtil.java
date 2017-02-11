package com.jeremy.lychee.utils.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Util for system statusbar
 * <p/>
 * Created by zhangyaqing on 2015/6/17.
 */
public class StatusBarUtil {

    private static int sStatusBarHeight = -1;

    /**
     * to judge whether the system status bar of the current Activity is translucent
     *
     * @param activity the current showing Activity
     * @return if the current OS doesn't support status bar translucent or not setting translucent，return false; otherwise return true
     */
    public static boolean isStatusBarTranslucent(Activity activity) {
        boolean isStatusBarTranslucent = false;

        if (supportStatusBarTranslucent() && activity != null) {
            //whether set by in style of current activity
            int[] attrs = {android.R.attr.windowTranslucentStatus};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                isStatusBarTranslucent = a.getBoolean(0, false);
            } finally {
                a.recycle();
            }
            //whether set in the code of current Activity
            Window window = activity.getWindow();
            if (window != null) {
                WindowManager.LayoutParams winParams = window.getAttributes();
                if ((winParams.flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0) {
                    isStatusBarTranslucent = true;
                }
            }
        }

        return isStatusBarTranslucent;
    }

    /**
     * to judge whether the current OS support StatusBarTranslucent
     */
    public final static boolean supportStatusBarTranslucent() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * to set whether the current Activity apple the style of StatusBarTranslucent
     */
    public static void setStatusBarTranslucent(Activity activity, boolean isTranslucent) {
        if (activity == null) {
            return;
        }

        final Window window = activity.getWindow();
        if (window != null) {
            if (isTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * to set the system status bar applying dark mode for mi OS
     *
     * @param window     the window of current Activity
     * @param isDarkMode whether to apple dark mode
     */
    private static void setStatusBarDarkModeForMIUI(Window window, boolean isDarkMode) {
        Class<? extends Window> clazzWindow = window.getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazzWindow.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isDarkMode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get the height of system status bar （unit： px）
     */
    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight < 0) {
            if (context != null) {
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    sStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
                }
            }
        }
        return sStatusBarHeight;
    }

    /**
     * to set the system status bar applying dark mode for FlyMe OS
     *
     * @param window     the window of current Activity
     * @param isDarkMode whether to apple dark mode
     */
    private static void setStatusBarDarkIconForMeizu(Window window, boolean isDarkMode) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDarkMode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * add the background tint view of system status bar and return the view.
     */
    public static View addStatusBarTintView(Activity activity) {
        if (activity == null) {
            return null;
        }

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(activity);
        View statusBarTintView = new View(activity);
        statusBarTintView.setBackgroundResource(android.R.color.transparent);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarTintView.setLayoutParams(params);
        getRootFrame(activity).addView(statusBarTintView, params);
        return statusBarTintView;
    }

    protected static FrameLayout getRootFrame(Activity activity) {
        return (FrameLayout) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
    }

    public static SystemBarTintManager getSystemBarTintManager(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(activity, true);
        }
        return new SystemBarTintManager(activity);
    }

    public static void setStatusBarTintViewColor(Activity activity,
                                                 SystemBarTintManager manager,
                                                 int color) {
        if (activity == null) {
            return;
        }
        manager.setStatusBarTintEnabled(true);
        //设置状态栏的背景颜色
        manager.setStatusBarTintColor(color);
        // 设置状态栏的文字颜色(白色)
        manager.setStatusBarDarkMode(false, activity);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity,boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
