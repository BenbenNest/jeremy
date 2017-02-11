package com.jeremy.lychee.customview.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserMenuItemView extends FrameLayout {
    @Bind(com.jeremy.lychee.R.id.menu_icon)
    ImageView mIconView;
    @Bind(com.jeremy.lychee.R.id.menu_title)
    TextView mTitlelView;
    private Integer mIconRes;
    private String mTitleRes;

    public UserMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("Recycle") TypedArray a = context.obtainStyledAttributes(attrs, com.jeremy.lychee.R.styleable.UserMenuItemView);
        mIconRes = a.getResourceId(com.jeremy.lychee.R.styleable.UserMenuItemView_menuIcon, com.jeremy.lychee.R.color.news_image_default_color);
        mTitleRes = a.getString(com.jeremy.lychee.R.styleable.UserMenuItemView_menuTitle);
        initUI();
    }

    private void initUI() {
        ButterKnife.bind(
                this, LayoutInflater.from(getContext()).inflate(
                        com.jeremy.lychee.R.layout.layout_user_menu_item, this, true));
        mIconView.setImageDrawable(getResources().getDrawable(mIconRes));
        mTitlelView.setText(mTitleRes);
    }

    @Override
    protected void finalize() throws Throwable {
        ButterKnife.unbind(this);
        super.finalize();
    }
}
