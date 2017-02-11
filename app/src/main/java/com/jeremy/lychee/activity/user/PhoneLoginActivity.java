package com.jeremy.lychee.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.model.user.ImgCodeData;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.net.UserCenterRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.customview.user.LoginImgAuthCodeLayout;
import com.jeremy.lychee.customview.user.LoginInputLayout;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PhoneLoginActivity extends SlidingActivity implements TextWatcher {
    @Bind(com.jeremy.lychee.R.id.telephone_input_layout)
    LoginInputLayout telephone_input_layout;

    @Bind(com.jeremy.lychee.R.id.password_input_layout)
    LoginInputLayout password_input_layout;

    @Bind(com.jeremy.lychee.R.id.auth_code_layout)
    LoginImgAuthCodeLayout auth_code_layout;

    @Bind(com.jeremy.lychee.R.id.login_btn)
    Button login_btn;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_phone_login);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);

        String title = getString(com.jeremy.lychee.R.string.login_title);
        mToolBar.setTitle(title);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        telephone_input_layout.addTextChangedListener(this);
        password_input_layout.addTextChangedListener(this);
        auth_code_layout.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    public void onLogin(View view) {
        ImgCodeData imgCodeData = auth_code_layout.getImgCodeData();
        if(auth_code_layout.getVisibility()==View.VISIBLE&&imgCodeData==null){
            auth_code_layout.refreshImgCode();
            return;
        }

        String u_salt = AppUtil.getSalt();
        String mobile = telephone_input_layout.getText().toString();
        String password = password_input_layout.getPassword();

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);

        String imgcode = imgCodeData!=null&&auth_code_layout.getVisibility()==View.VISIBLE ? imgCodeData.getImgcode() : "";
        params.put("imgcode", imgcode);
        String vcode = auth_code_layout.getVisibility()==View.VISIBLE  ? auth_code_layout.getText().toString() : "";
        params.put("vcode", vcode);

        params.put("u_salt", u_salt);
        String u_sign = UserCenterRetroAdapter.GetSign(params) + u_salt;
        params.put("u_sign", u_sign);

        OldRetroApiService service = UserCenterRetroAdapter.getUserCenterService();
        Observable<Result<User>> mObservable = service.login(mobile, password, imgcode, vcode, u_salt, u_sign);
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<Result<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                    }

                    @Override
                    public void onNext(Result<User> result) {
                        if (result == null) {
                            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                            return;
                        }

                        switch (result.errno) {
                            case 0:
                                Session.save(result.getData(), true);
                                QEventBus.getEventBus().post(new Events.LoginOk());
                                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.login_success);
                                setResult(RESULT_OK);
                                finish();
                                break;
                            case 50012:
                                //当连续输入三次后,需要用户输入图像验证码和密码完成登录； 约定errno=50012时，客户端出图像验证码,客户端需要将用户输入的验证码vcode和请求图像验证码接口下发的imgcode同手机号、密码提交到服务端验证
                                auth_code_layout.refreshImgCode();
                                auth_code_layout.setVisibility(View.VISIBLE);
                                verifyEditText();
                                QEventBus.getEventBus().post(new Events.LoginErr());
                                ToastHelper.getInstance(getApplicationContext()).toast(result.getErrmsg());

                                break;
                            default:
                                if(auth_code_layout.getVisibility()==View.VISIBLE){
                                    auth_code_layout.refreshImgCode();
                                }
                                QEventBus.getEventBus().post(new Events.LoginErr());
                                ToastHelper.getInstance(getApplicationContext()).toast(result.getErrmsg());
                                break;
                        }
                    }
                });
    }

    public void onRegister(View view) {
        startActivityForResult(new Intent(PhoneLoginActivity.this, PhoneRegisterActivity.class), 99);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==99){
            setResult(RESULT_OK);
            finish();
        }
    }

    public void onForget(View view) {
        startActivityForResult(new Intent(PhoneLoginActivity.this, PhoneRegisterActivity.class).putExtra("isForget", true), 99);
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
        String mobile = telephone_input_layout.getText().toString();
        String password = password_input_layout.getText().toString();
        String auth_code = auth_code_layout.getText().toString();
        if(TextUtils.isEmpty(mobile)|TextUtils.isEmpty(password)){
            login_btn.setEnabled(false);
        } else {
            if(auth_code_layout.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(auth_code)){
                login_btn.setEnabled(false);
            } else {
                login_btn.setEnabled(true);
            }
        }
    }
}
