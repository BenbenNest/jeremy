package com.jeremy.lychee.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.base.BaseLayout;

import butterknife.Bind;

public class BottomMenuBar extends BaseLayout {

    @Bind(R.id.view_tab_news)
    TextView  mTabNews;
    @Bind(R.id.view_tab_live)
    TextView mTabLive;
    @Bind(R.id.view_tab_user)
    TextView mTabUser;

    public BottomMenuBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInflate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.bottom_layout, this);
    }

    @Override
    protected void initUI(Activity activity) {
//        mTabNews.setTypeface(FontManager.getKtTypeface());
//        mTabLive.setTypeface(FontManager.getKtTypeface());
//        mTabUser.setTypeface(FontManager.getKtTypeface());
        mTabNews.setSelected(true);
    }

    public void setTabSelected(View view){
        mTabNews.setSelected(false);
        mTabLive.setSelected(false);
        mTabUser.setSelected(false);
        view.setSelected(true);
    }
}
