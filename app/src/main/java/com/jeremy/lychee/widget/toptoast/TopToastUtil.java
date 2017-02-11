package com.jeremy.lychee.widget.toptoast;

import android.view.ViewGroup;
import android.widget.TextView;

public class TopToastUtil {

    public static synchronized void showTopToast(ViewGroup container, String content) {
        TopToast toast =
                TopToast.make(container, content, TopToast.LENGTH_SHORT);
        ((TextView) toast.getView().findViewById(com.jeremy.lychee.R.id.toast_text)).setTextColor(0xff6e91c1);
        toast.getView().setBackgroundColor(0xe6ffffff);
        toast.show();
    }

    public interface ICallBack {
        void run();
    }

}
