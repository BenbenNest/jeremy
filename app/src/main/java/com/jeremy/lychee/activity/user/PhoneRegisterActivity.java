package com.jeremy.lychee.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.jeremy.lychee.model.user.ImgCodeData;
import com.jeremy.lychee.net.UserCenterRetroAdapter;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.net.OldRetroApiService;
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

/**
 * telephone num login activity
 */
public class PhoneRegisterActivity extends SlidingActivity implements TextWatcher {
    @Bind(com.jeremy.lychee.R.id.telephone_input_layout)
    LoginInputLayout telephone_input_layout;

    @Bind(com.jeremy.lychee.R.id.auth_code_layout)
    LoginImgAuthCodeLayout auth_code_layout;

    @Bind(com.jeremy.lychee.R.id.next_step_btn)
    Button next_step_btn;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolBar;

    private boolean isForget;

    @Override
    protected void onPreInflation() {
        super.onPreInflation();
        isForget = getIntent().getBooleanExtra("isForget", false);
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_phone_register);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);

        String title = isForget ? getString(com.jeremy.lychee.R.string.find_password_title) : getString(com.jeremy.lychee.R.string.register_title);
        mToolBar.setTitle(title);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        telephone_input_layout.addTextChangedListener(this);
        auth_code_layout.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    public void nextStep(View view) {
        ImgCodeData imgCodeData = auth_code_layout.getImgCodeData();
        if(imgCodeData==null){
            auth_code_layout.refreshImgCode();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        String mobile = telephone_input_layout.getText().toString();

        params.put("mobile", mobile);

        String rtime = imgCodeData.getRtime();
        params.put("rtime", rtime);

        String imgcode = imgCodeData.getImgcode();
        params.put("imgcode", imgcode);

        String vcode = auth_code_layout.getText().toString();
        params.put("vcode", vcode);

        String ac = isForget ? "findpwd" : "reg";
        params.put("ac", ac);

        String u_salt = AppUtil.getSalt();
        params.put("u_salt", u_salt);

        String u_sign = UserCenterRetroAdapter.GetSign(params) + u_salt;
        params.put("u_sign", u_sign);
        OldRetroApiService service = UserCenterRetroAdapter.getUserCenterService();
        Observable<Result> mObservable = service.getMsgCode(rtime, mobile, imgcode, vcode, ac, u_salt, u_sign);
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                    }

                    @Override
                    public void onNext(Result result) {
                        auth_code_layout.refreshImgCode();
                        if(result==null){
                            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                            return;
                        }
                        if(result.errno==0){
                            Intent intent = new Intent(PhoneRegisterActivity.this, MsgAuthCodeActivity.class);
                            intent.putExtra("isForget", isForget);
                            intent.putExtra("mobile", mobile);
                            startActivityForResult(intent, 99);
                        }else {
                            ToastHelper.getInstance(getApplicationContext()).toast(result.getErrmsg());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==99){
            setResult(RESULT_OK);
            finish();
        }
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
        String auth_code = auth_code_layout.getText().toString();
        if(TextUtils.isEmpty(mobile)|TextUtils.isEmpty(auth_code)){
            next_step_btn.setEnabled(false);
        } else {
            next_step_btn.setEnabled(true);
        }
    }
}
