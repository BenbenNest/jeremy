package com.jeremy.lychee.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.R;

import rx.functions.Action0;

public class AlertDialog extends Dialog {

    private TextView title;
    private TextView content;
    private Button confirm;
    private Action0 action;

    public AlertDialog(Context context) {
        this(context, R.style.dialog_view_theme);
        init();
    }

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init() {
        super.setTitle(null);
        setContentView(R.layout.layout_dialog_alert);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        confirm = (Button) findViewById(R.id.confirm);

        confirm.setOnClickListener(v -> {
            dismiss();
            if(action != null)action.call();
        });

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public AlertDialog setCustomTitle(String title) {
        this.title.setText(title);
        this.title.setVisibility(title.isEmpty() ? View.GONE : View.VISIBLE);
        return this;
    }

    public AlertDialog setCustomContent(String content, Action0 action) {
        this.action = action;
        this.content.setText(content);
        this.content.setVisibility(content.isEmpty() ? View.GONE : View.VISIBLE);
        return this;
    }

    public AlertDialog setCustomContentView(View content) {
        LinearLayout root = (LinearLayout) getWindow().getDecorView().findViewById(R.id.root);
        root.addView(content, 2);
        return this;
    }


    public AlertDialog setOnConfirmListener(View.OnClickListener listener) {
        confirm.setOnClickListener(listener);
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
