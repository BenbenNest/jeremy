package com.jeremy.lychee.manager;

import android.graphics.Typeface;

import com.jeremy.lychee.base.ContentApplication;

public class FontManager {

    private static volatile Typeface ktTypeface = null;
    private static volatile Typeface htTypeface = null;
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static Typeface getKtTypeface() {
        if (ktTypeface == null) {
            synchronized (lock1) {
                if (ktTypeface == null) {
                    ktTypeface = Typeface.createFromAsset(ContentApplication.getInstance().getApplicationContext().getAssets(), "fonts/fzktjt.ttf");
                }
            }
        }
        return ktTypeface;
    }

    public static Typeface getHtTypeface() {
//        if (htTypeface == null) {
//            synchronized (lock2) {
//                if (htTypeface == null) {
//                    htTypeface = Typeface.createFromAsset(ContentApplication.getInstance().getApplicationContext().getAssets(), "fonts/DQHT_W3.ttf");
//                }
//            }
//        }
//        return htTypeface;
        return null;
    }
}
