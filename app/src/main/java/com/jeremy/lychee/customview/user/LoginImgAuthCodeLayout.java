package com.jeremy.lychee.customview.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jeremy.lychee.model.user.ImgCodeData;
import com.jeremy.lychee.net.UserCenterRetroAdapter;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangying-pd on 2016/2/17.
 * login img auth code layout
 */
public class LoginImgAuthCodeLayout extends FrameLayout implements View.OnFocusChangeListener {
    private EditText input_et;
    private View input_bottom_line;
    private ImageView img_auth_code_iv;

    private ImgCodeData imgCodeData;

    public LoginImgAuthCodeLayout(Context context) {
        super(context);
    }

    public LoginImgAuthCodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginImgAuthCodeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View login_input_layout = View.inflate(getContext(), com.jeremy.lychee.R.layout.login_img_auth_code_layout, null);
        input_et = (EditText) login_input_layout.findViewById(com.jeremy.lychee.R.id.input_et);
        img_auth_code_iv = (ImageView) login_input_layout.findViewById(com.jeremy.lychee.R.id.img_auth_code_iv);
        img_auth_code_iv.setOnClickListener(v -> refreshImgCode());

        login_input_layout.setOnClickListener(v -> input_et.requestFocus());
        input_et.setOnFocusChangeListener(this);

        input_bottom_line = login_input_layout.findViewById(com.jeremy.lychee.R.id.input_bottom_line);
        addView(login_input_layout, LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(com.jeremy.lychee.R.dimen.input_height));

        refreshImgCode();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case com.jeremy.lychee.R.id.input_et:
                input_bottom_line.setSelected(hasFocus);
                break;
        }
    }

    public Editable getText(){
        return input_et.getText();
    }

    public ImgCodeData getImgCodeData(){
        return imgCodeData;
    }

    public void refreshImgCode(){
        String rand = String.valueOf(System.currentTimeMillis() / 1000);
        String u_salt = AppUtil.getSalt();
        HashMap<String, String> params = new HashMap<>();
        params.put("rand", rand);
        params.put("u_salt", u_salt);
        String u_sign = UserCenterRetroAdapter.GetSign(params) + u_salt;

        OldRetroApiService service = UserCenterRetroAdapter.getUserCenterService();
        Observable<Result<ImgCodeData>> mObservable = service.getImgCode(rand, u_salt, u_sign);
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Result<ImgCodeData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastHelper.getInstance(getContext().getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                    }

                    @Override
                    public void onNext(Result<ImgCodeData> result) {
                        if(result==null){
                            ToastHelper.getInstance(getContext().getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                            return;
                        }
                        if(result.errno==0){
                            imgCodeData = result.getData();
                            if (imgCodeData != null) {
                                String base64Str = imgCodeData.getResult();
                                if (base64Str != null) {
                                    byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
                                    if (data != null) {
                                        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        img_auth_code_iv.setImageBitmap(bm);
                                    }
                                }
                            }
                        } else {
                            ToastHelper.getInstance(getContext().getApplicationContext()).toast(result.getErrmsg());
                        }
                    }
                });
    }

    public void addTextChangedListener(TextWatcher watcher){
        if(input_et!=null){
            input_et.addTextChangedListener(watcher);
        }
    }
}
