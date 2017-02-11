package com.jeremy.lychee.activity.user;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jeremy.lychee.activity.news.WebViewActivity;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.preference.UserPreference;
import com.jeremy.lychee.R;
import com.jeremy.lychee.model.update.UpdateChecker;
import com.jeremy.lychee.preference.TestAddressPreference;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.widget.MaterialCheckBox;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends SlidingActivity {

    @Bind(R.id.mobile_net_enable_checkbox)
    MaterialCheckBox mMobileCheckBox;

    @Bind(R.id.mobile_push_enable_checkbox)
    MaterialCheckBox mMobilePushEnableCheckbox;

    @Bind(R.id.settings_item_upgrade_version_name)
    TextView settings_item_upgrade_version_name;
    @Bind(R.id.is_test_address)
    MaterialCheckBox is_test_address;
    private View aboutItem, complain, userExit;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initListener();

    }

    private void initToolbar() {
        configToolbar(0, R.string.title_activity_settings);
    }

    private void initView() {
        aboutItem = findViewById(R.id.settings_item_about);
        ((TextView) aboutItem.findViewById(R.id.settings_item_title))
                .setText("关于");
        complain = findViewById(R.id.settings_item_complain);
        ((TextView) complain.findViewById(R.id.settings_item_title))
                .setText("权利人保护指引");
        complain.setVisibility(View.GONE);
        userExit = findViewById(R.id.settings_item_exit);
        if (!Session.isUserInfoEmpty()) {
            userExit.setVisibility(View.VISIBLE);
        } else {
            userExit.setVisibility(View.GONE);
        }

        mMobileCheckBox.setChecked(UserPreference.getInstance().getUseMobileNetEnabled(), true);
        mMobilePushEnableCheckbox.setChecked(UserPreference.getInstance().getUseMobilePushEnabled(), true);
        is_test_address.setChecked(TestAddressPreference.getInstance().getIsTestAddress(), false);
        String version_name = AppUtil.getVersionName(getApplicationContext());
        String version_txt = getResources().getString(R.string.settings_item_upgrade_version_name, version_name);
        settings_item_upgrade_version_name.setText(version_txt);
    }


    private void initListener() {
        aboutItem.setOnClickListener(listener);
        complain.setOnClickListener(listener);
        mMobileCheckBox.setOnClickListener(v -> {
            mMobileCheckBox.setChecked(!mMobileCheckBox.isChecked(), false);
            UserPreference.getInstance().setUseMobileNetEnabled(mMobileCheckBox.isChecked());
        });
        mMobilePushEnableCheckbox.setOnClickListener(v -> {
            mMobilePushEnableCheckbox.setChecked(!mMobilePushEnableCheckbox.isChecked(), false);
            UserPreference.getInstance().setUseMobilePushEnabled(mMobilePushEnableCheckbox.isChecked());
        });
        is_test_address.setOnClickListener(v -> {
            is_test_address.setChecked(!is_test_address.isChecked(), false);
            TestAddressPreference.getInstance().setIsTestAddress(is_test_address.isChecked());
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.settings_item_about: {
                    //跳转到关于界面
                    openActivity(AboutActivity.class);
                }
                break;
                case R.id.settings_item_complain: {

                    Bundle bundle = new Bundle();
                    bundle.putString("url", "http://www.so.com/help/help_right.html");
                    openActivity(WebViewActivity.class, bundle, 0);


                    //跳转网页
//                    openUrl("http://www.so.com/help/help_right.html");
                }
                break;
            }

        }
    };

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
        toolbar.setNavigationOnClickListener(v -> finish());
        return toolbar;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void checkVersionUpdate(View view) {
        new UpdateChecker(this, true).startCheck();
    }

    public void onClickExit(View view) {
        DialogUtil.showConfirmDialog(SettingsActivity.this, "确定要退出登录吗？",
                getString(R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                    dialog.dismiss();
                    Session.save(null, true);
                    QEventBus.getEventBus().post(new Events.Logout());
                    userExit.setVisibility(View.GONE);
                }, getString(R.string.dialog_button_cancel), DialogInterface::dismiss);
    }
}
