package com.jeremy.lychee.utils;

import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.widget.TextView;

public class SnackBarUtil {

    public static synchronized void showSnackBar(ViewGroup container, ICallBack cb) {
        Snackbar snackbar =
                Snackbar.make(container, com.jeremy.lychee.R.string.news_snackbar_text, Snackbar.LENGTH_LONG)
                .setAction(com.jeremy.lychee.R.string.news_snackbar_refresh, v -> cb.run());
        ((TextView) snackbar.getView().findViewById(com.jeremy.lychee.R.id.snackbar_text)).setTextColor(
                container.getResources().getColor(com.jeremy.lychee.R.color.news_item_text_color));
        ((TextView) snackbar.getView().findViewById(com.jeremy.lychee.R.id.snackbar_action)).setTextColor(
                container.getResources().getColor(com.jeremy.lychee.R.color.swipe_refresh_color));
        snackbar.getView().setBackgroundColor(
                container.getResources().getColor(com.jeremy.lychee.R.color.news_image_default_color));
        snackbar.show();
    }

    public interface ICallBack {
        void run();
    }

}
