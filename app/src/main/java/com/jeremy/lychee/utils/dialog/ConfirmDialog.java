package com.jeremy.lychee.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by houwenchang
 * 15/12/22.
 */
public class ConfirmDialog extends Dialog {

    private TextView title;
    private TextView content;
    private Button positive;
    private Button negative;
    private View splite;

    public ConfirmDialog(Context context) {
        this(context, com.jeremy.lychee.R.style.dialog_view_theme);
        init();
    }

    public ConfirmDialog(Context context, int themeResId) {
        super(context, themeResId);
    }



    private void init() {
        super.setTitle(null);
        setContentView(com.jeremy.lychee.R.layout.layout_dialog_confirm);
        title = (TextView) findViewById(com.jeremy.lychee.R.id.title);
        content = (TextView) findViewById(com.jeremy.lychee.R.id.content);
        positive = (Button) findViewById(com.jeremy.lychee.R.id.positive);
        negative = (Button) findViewById(com.jeremy.lychee.R.id.negative);
        splite = findViewById(com.jeremy.lychee.R.id.split_line);
//        positive.setOnTouchListener(touchListener);
//        negative.setOnTouchListener(touchListener);

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public ConfirmDialog setCustomTitle(String title) {
        this.title.setText(title);
        this.title.setVisibility(title.isEmpty() ? View.GONE : View.VISIBLE);
        splite.setVisibility(title.isEmpty() ? View.GONE : View.VISIBLE);
        return this;
    }

    public ConfirmDialog setCustomContent(String content) {
        this.content.setText(content);
        this.content.setVisibility(content.isEmpty() ? View.GONE : View.VISIBLE);
        return this;
    }

    public ConfirmDialog setCustomContentView(View content) {
        LinearLayout root = (LinearLayout) getWindow().getDecorView().findViewById(com.jeremy.lychee.R.id.root);
        root.addView(content, 2);
        return this;
    }

    public ConfirmDialog setPositiveButton(String text, View.OnClickListener listener) {
        positive.setText(text);
        positive.setOnClickListener(listener);
        return this;
    }

    public ConfirmDialog setNegativeButton(String text, View.OnClickListener listener) {
        negative.setText(text);
        negative.setOnClickListener(listener);
        return this;
    }

    public ConfirmDialog setOnConfirmListener(View.OnClickListener listener) {
        positive.setOnClickListener(listener);
        return this;
    }

    public ConfirmDialog setOnCancleListener(View.OnClickListener listener) {
        negative.setOnClickListener(listener);
        return this;
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(content.getText())) {
            content.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(title.getText())) {
            title.setVisibility(View.GONE);
        }
        super.show();
    }

    public TextView getTitleView(){
        return title;
    }


}
