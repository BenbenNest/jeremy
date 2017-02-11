package com.jeremy.lychee.activity.user;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.news.WebViewActivity;
import com.jeremy.lychee.activity.news.SubjectLlistActivity;
import com.jeremy.lychee.utils.SystemInfo;
import com.jeremy.lychee.widget.MaterialCheckBox;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

public class AboutActivity extends SlidingActivity implements View.OnClickListener {
    private MaterialCheckBox mJoinSwitch = null;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_about);
        initToolBar();
        initViews();
        ImageView btn = (ImageView) findViewById(R.id.about_check_update);
        btn.setOnClickListener(v -> {
            //跳转到专题页
//            openActivity(SubjectLlistActivity.class);
            SubjectLlistActivity.startActivity(this,"1","http://cmsapi.kandian.360.cn/zt/1.html");

        });

    }

    @SuppressLint("SetTextI18n")
    private void initViews() {

        String versionName = getString(R.string.about_version_name, getVersionName(this));
        ((TextView) findViewById(R.id.about_version_name)).setText(versionName + (SystemInfo.getBuildNumber().length() == 0 ? "" : "构建号: " + SystemInfo.getBuildNumber()));

        ViewGroup declare = (ViewGroup) findViewById(R.id.about_item_declare);
        TextView item = (TextView) declare.findViewById(
                R.id.settings_item_title);
        item.setText(R.string.settings_about_declare);
        declare.setOnClickListener(this);

        ViewGroup permit = (ViewGroup) findViewById(R.id.about_item_permit);
        item = ((TextView) permit.findViewById(
                R.id.settings_item_title));
        item.setText(R.string.settings_about_permit);
        permit.setOnClickListener(this);

        ViewGroup improve = (ViewGroup) findViewById(R.id.about_item_improve);
        item = ((TextView) improve.findViewById(
                R.id.settings_item_title));
        item.setText(R.string.settings_about_improve);
        improve.setOnClickListener(this);

        ViewGroup join = (ViewGroup) findViewById(R.id.about_item_improve_join);
        item = ((TextView) join.findViewById(
                R.id.settings_item_title));
        item.setText(R.string.settings_about_improve_join);
        join.setOnClickListener(this);


        //加入用户计划
        mJoinSwitch = (MaterialCheckBox) join.findViewById(R.id.settings_checkbox);

//        mJoinSwitch.setChecked(Settings.joinOn(this), true);
//        mJoinSwitch.setOncheckListener(new MaterialCheckBox.OnCheckListener() {
//            @Override
//            public void onCheck(boolean check, boolean suddenly) {
//                Settings.toggleSwitchSetting(AboutActivity.this, Switch.IMPROVE_JOIN, check);
//            }
//        });

    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.about_item_declare:
//                openUrl("http://kandian.me/btime_privacy.html");
                bundle.putString("url", "http://kandian.me/btime_privacy.html");
                openActivity(WebViewActivity.class, bundle, 0);
                break;
            case R.id.about_item_permit:
//                openUrl("http://kandian.me/btime_agreement.html");
                bundle.putString("url", "http://kandian.me/btime_agreement.html");
                openActivity(WebViewActivity.class, bundle, 0);
                break;
            case R.id.about_item_improve:
//                openUrl("http://kandian.me/btime_experience.html");
                bundle.putString("url", "http://kandian.me/btime_experience.html");
                openActivity(WebViewActivity.class, bundle, 0);

                break;
            case R.id.about_item_improve_join:
//                toggleSetting(Switch.IMPROVE_JOIN, mJoinSwitch);
                break;

            default:
                break;
        }
    }

    private void initToolBar() {

        configToolbar(0, R.string.title_activity_about);
    }

    /*
    * 获取应用VersionName
    */
    public static String getVersionName(Context ctx) {
        String res = "";
        if (ctx == null) {
            return res;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            res = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Toolbar configToolbar(int menuId, int titleId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (menuId != 0) {
            toolbar.inflateMenu(menuId);
        }
        if (titleId != 0) {
            toolbar.setTitle(titleId);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        return toolbar;
    }
}
