package com.jeremy.lychee.customview.user;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jeremy.lychee.utils.logger.Logger;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by zhangying-pd on 2016/2/17.
 * login edittext view
 */
public class LoginInputLayout extends FrameLayout implements View.OnFocusChangeListener {
    private EditText input_et;
    private View input_bottom_line;
    private View input_clear_btn;

    private String input_name;
    private String input_hint;
    private int inputType;

    public LoginInputLayout(Context context) {
        super(context);
    }

    public LoginInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoginInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        if(context==null||attrs==null){
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, com.jeremy.lychee.R.styleable.LoginInputLayout);
        input_name = a.getString(com.jeremy.lychee.R.styleable.LoginInputLayout_input_name);
        input_hint = a.getString(com.jeremy.lychee.R.styleable.LoginInputLayout_hint);
        inputType = a.getInt(com.jeremy.lychee.R.styleable.LoginInputLayout_inputType, 0);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View login_input_layout = View.inflate(getContext(), com.jeremy.lychee.R.layout.login_input_layout, null);
        input_clear_btn= login_input_layout.findViewById(com.jeremy.lychee.R.id.input_clear_btn);
        input_clear_btn.setOnClickListener(v -> input_et.setText(""));
        TextView input_name_tv = (TextView) login_input_layout.findViewById(com.jeremy.lychee.R.id.input_name_tv);
        input_name_tv.setText(input_name);
        input_et = (EditText) login_input_layout.findViewById(com.jeremy.lychee.R.id.input_et);
        input_et.setInputType(inputType);

        login_input_layout.setOnClickListener(v -> input_et.requestFocus());
        input_et.setOnFocusChangeListener(this);
        input_et.setHint(input_hint);
        input_bottom_line = login_input_layout.findViewById(com.jeremy.lychee.R.id.input_bottom_line);
        addView(login_input_layout, LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(com.jeremy.lychee.R.dimen.input_height));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case com.jeremy.lychee.R.id.input_et:
                input_bottom_line.setSelected(hasFocus);
                input_clear_btn.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public Editable getText(){
        return input_et.getText();
    }

    public String getPassword(){
        String pwd = input_et.getText().toString();
        try {
            String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDaMWeSroknflaOH2aKc3otA0oe\n" +
                    "l/dI4jPXAGNrC3Ecyp8BmDJLg5zOWU0AVNUuU+xjq95AUKOs5n2cNVordqU53qRg\n" +
                    "LC9pxcvLf893k3Q58emdfmgHfgsCczKn7uvg2hwDsFXttbDXNc6j6QIQatQUFNXL\n" +
                    "HHSrlQMnpVB/sj0dKQIDAQAB\n";
            String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANoxZ5KuiSd+Vo4f\n" +
                    "Zopzei0DSh6X90jiM9cAY2sLcRzKnwGYMkuDnM5ZTQBU1S5T7GOr3kBQo6zmfZw1\n" +
                    "Wit2pTnepGAsL2nFy8t/z3eTdDnx6Z1+aAd+CwJzMqfu6+DaHAOwVe21sNc1zqPp\n" +
                    "AhBq1BQU1cscdKuVAyelUH+yPR0pAgMBAAECgYAvmxXpCBG+vy7tZWZ/EMD4bz5N\n" +
                    "vIRkr431/Ay0/1MXoBHfTDC1Dys4Tn2hc+Drcb4i5Q3h5HZBtCx116Ir14Am3/LF\n" +
                    "OXbKx+fiKi7uuwKHMtc845EuhRCEqeGpCUHIVYiOLW01hWZMGqTCkBPdn4Avkbl4\n" +
                    "PlUF+n6AtIfcJ895MQJBAPdPzKRSjfqkHRf1SCBuzVntgMSV4e0hvqCpj3asgLcs\n" +
                    "BLIR3lql4uHuhLeHr/AYQWA6POn+jaZTwyc7wNsXc+0CQQDh27nACwt5wSXEH9lM\n" +
                    "plFfvhmPems0p5CDYQeESCfOj5wPRGIYEdH9dVxITBD2zqyOGMo7CEY5Od8TfgXV\n" +
                    "Rh6tAkEAtsdvg3LQJBHuvqo1aGL7uh7iMGhl1ZA/BxbKRPcxGi+aONEj+PLpP/m8\n" +
                    "dLf/u/7xMmFYa8gKgIcfkHuFCWfH8QJAR5mIgUD/GlIIzuvpz0rKcTeqkrmUUtTs\n" +
                    "EzonT+Av/mO4bX6YOlkr4NXbs2+p0RQjz8g8FArDeFDITz2cS8g24QJAPEnb5b8o\n" +
                    "2dmFlzgL6a7b15UupW5GR6TP4T2bkDg3fJsQUGB2ZnszrLFBZtb9Li3Yj0tYmYL7\n" +
                    "RwH4bpILjtvEow==\n";
            byte[] data = encryptByPublicKey(pwd.getBytes(), PUCLIC_KEY);
            pwd = new String(Base64.encode(data, Base64.DEFAULT));
//            pwd = "oSrY4+0Iqz6jiiv2m7rwEpqS4JkDmDKUhhnkE9E5LkT1iizptCz+zXL9FtyqhAzEF2Wh7v/cMA1XTDyVpRDz0JW0Iq0a4e/bVitqSGWTkXB+IcYJdl89o1JrAMMv2nF6duuXIhwWrnYQA8wyRAdgM/Iny18tiiASbABjcCV9yjY=";
//            Logger.e("encryptByPublicKey: "+pwd);
            Logger.e("decryptByPrivateKey: " + new String(decryptByPrivateKey(Base64.decode(pwd.getBytes(), Base64.DEFAULT), PRIVATE_KEY)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.e("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            Logger.e("私/公钥非法");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Logger.e("私/公钥数据为空");
        } catch (Exception e){
            e.printStackTrace();
            Logger.e("错误");
        }

        return pwd;
    }

    public void addTextChangedListener(TextWatcher watcher){
        if(input_et!=null){
            input_et.addTextChangedListener(watcher);
        }
    }

    private static String RSA = "RSA";
    /**
     * 用公钥加密
     * @param data   加密数据
     * @param publicKeyStr    公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data,String publicKeyStr)throws Exception{
        //对公钥解密
        byte[] keyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT);
        //取公钥
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密 * @param data    加密数据
     * @param privateKeyStr    私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data,String privateKeyStr)throws Exception{
        //对私钥解密
        byte[] keyBytes = Base64.decode(privateKeyStr, Base64.DEFAULT);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }


}
