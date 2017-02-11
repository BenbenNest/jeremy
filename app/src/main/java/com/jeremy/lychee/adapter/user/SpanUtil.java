package com.jeremy.lychee.adapter.user;

import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by houwenchang
 * 16/3/1.
 */
public class SpanUtil {


    interface SpanClickListener {
        void onClick(String span);
    }

    public static void parse(TextView tv, String[] spans, SpanClickListener listener) {

        String string = tv.getText().toString();
        for (int i = 0; i < spans.length; i++) {
            String span = spans[i];
            if (!TextUtils.isEmpty(span)) {
                string = string.replaceAll(Pattern.quote(span), "<a href=\"" + i + ":" + span + "\">" + span + "</a>");
            }
        }
        Spanned fromHtml = new SpannableString(Html.fromHtml(string));
        tv.setText(fromHtml);
        handleStyle(tv, listener);
    }

    private static void handleStyle(TextView tv, SpanClickListener listener) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();

        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();//should clear old spans

            for (URLSpan url : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), listener);
                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setText(style);
        }
    }

    private static class MyURLSpan extends ClickableSpan {

        private final SpanClickListener listener;
        private final String span;

        MyURLSpan(String span, SpanClickListener listener) {
            this.listener = listener;
            this.span = span;
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onClick(span.substring(span.indexOf(":") + 1));
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            int index = Integer.parseInt(span.substring(0, span.indexOf(":")));
            if (index == 0) {
                ds.setColor(Color.parseColor("#5f7fa6"));
            } else {
                ds.setColor(Color.parseColor("#262525"));
            }
        }
    }
}
