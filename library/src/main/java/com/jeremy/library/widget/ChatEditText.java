package com.jeremy.library.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Calendar;

/**
 * Created by changqing.zhao on 2017/3/31.
 * 安卓手机软键盘弹出后不响应onKeyDown、onBackPressed的解决方法
 */
public class ChatEditText extends AppCompatEditText {
    private String TAG = "ChatEditText";
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private OnCancelListener mOnCancelDialogImp;

    public ChatEditText(Context context) {
        super(context);
    }

    public ChatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            Log.v(TAG, "dispatchKeyEventPreIme");
            lastClickTime = currentTime;
            if (mOnCancelDialogImp != null) {
                mOnCancelDialogImp.onCancel();
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setOnCancelListener(OnCancelListener onCancelDialogImp) {
        mOnCancelDialogImp = onCancelDialogImp;
    }

    public interface OnCancelListener {
        void onCancel();
    }

}
