package com.jeremy.lychee.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.UserCenterRetroAdapter;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.customview.user.LoginInputLayout;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.qihoo.sdk.report.QHStatAgent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 短信验证码activity
 */
public class MsgAuthCodeActivity extends SlidingActivity implements TextWatcher {
    @Bind(com.jeremy.lychee.R.id.phone_num_tv)
    TextView phone_num_tv;

    @Bind(com.jeremy.lychee.R.id.msg_auth_code_layout)
    LoginInputLayout msg_auth_code_layout;

    @Bind(com.jeremy.lychee.R.id.password_input_layout)
    LoginInputLayout password_input_layout;

    @Bind(com.jeremy.lychee.R.id.commit_btn)
    Button commit_btn;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolBar;

    private String mobile;
    private boolean isForget;

    @Override
    protected void onPreInflation() {
        super.onPreInflation();
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        isForget = intent.getBooleanExtra("isForget", false);
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_msg_auth_code);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);

        String title = getString(com.jeremy.lychee.R.string.verify_code_title);
        mToolBar.setTitle(title);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        String commit_txt = isForget ? getString(com.jeremy.lychee.R.string.commit_btn_txt) : getString(com.jeremy.lychee.R.string.register_btn_txt);
        commit_btn.setText(commit_txt);

        String tish = getString(com.jeremy.lychee.R.string.tel_tishi, mobile);
        SpannableString spanStr = new SpannableString(tish);
        if(!TextUtils.isEmpty(mobile)){
            spanStr.setSpan(new ForegroundColorSpan(Color.rgb(221, 44, 36)), 5, mobile.length() + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        phone_num_tv.setText(spanStr);

        msg_auth_code_layout.addTextChangedListener(this);
        password_input_layout.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void onCommit(View view) {
        //Register or find pwd commit
        String u_salt = AppUtil.getSalt();
        String password = password_input_layout.getPassword();
        String code = msg_auth_code_layout.getText().toString();

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("code", code);
        params.put("u_salt", u_salt);
        String u_sign = UserCenterRetroAdapter.GetSign(params) + u_salt;
        params.put("u_sign", u_sign);

        OldRetroApiService service = UserCenterRetroAdapter.getUserCenterService();
        Observable<Result<User>> mObservable = isForget ? service.findPwd(mobile, password, code, u_salt, u_sign) : service.register(mobile, password, code, u_salt, u_sign);
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<Result<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                    }

                    @Override
                    public void onNext(Result<User> result) {
                        if(result==null){
                            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                            return;
                        }

                        if(result.errno==0){
                            Session.save(result.getData(), true);
                            QEventBus.getEventBus().post(new Events.LoginOk());
                            ToastHelper.getInstance(getApplicationContext()).toast(isForget ? com.jeremy.lychee.R.string.commit_success : com.jeremy.lychee.R.string.register_success);
                            QHStatAgent.onEvent(MsgAuthCodeActivity.this, QHState.PHONEREGISTER);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            QEventBus.getEventBus().post(new Events.LoginErr());
                            ToastHelper.getInstance(getApplicationContext()).toast(result.getErrmsg());
                        }
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        verifyEditText();
    }

    /**
     * check if login btn enable
     */
    private void verifyEditText(){
        String msg_auth_code = msg_auth_code_layout.getText().toString();
        String password = password_input_layout.getText().toString();
        if(TextUtils.isEmpty(msg_auth_code)|TextUtils.isEmpty(password)){
            commit_btn.setEnabled(false);
        } else {
            commit_btn.setEnabled(true);
        }
    }
}
