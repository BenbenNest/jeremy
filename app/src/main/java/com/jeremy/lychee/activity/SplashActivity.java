package com.jeremy.lychee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.model.update.UpdateChecker;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.net.UserCenterRetroAdapter;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.utils.AppUtil;

import java.util.HashMap;
import java.util.TimeZone;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SplashActivity extends Activity {
    private ImageView mIvStart;

    private static final float START_SCALE = 1.0f;
    private static final float END_SCALE = 1.2f;
    private static final int DURATION = /*3000*/ 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.jeremy.lychee.R.layout.splash);
        mIvStart = (ImageView) findViewById(R.id.start);
        initImage();
//        getUserInfo();
//        if (!CommonPreferenceUtil.GetIsFirstOpen()) {
//            ConfigManager.getInstance().updateConfig();
//        }
//        Observable.timer(DURATION, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(time -> startActivity(), throwable -> {
//                    throwable.printStackTrace();
//                    startActivity();
//                });
    }

    private void initImage() {
        mIvStart.setImageResource(R.drawable.splash);

        final ScaleAnimation scaleAnim = new ScaleAnimation(START_SCALE, END_SCALE, START_SCALE, END_SCALE,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(DURATION);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIvStart.startAnimation(scaleAnim);

    }

    private void startActivity() {
        Boolean isFirstOpen = CommonPreferenceUtil.GetIsFirstOpen();
        if (isFirstOpen) {
//            startActivity(new Intent(getApplicationContext(), ChannelFirstChooseActivity.class));
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        }
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 检查是否检查更新
     */
    private void checkUpgrade() {
        Time time = new Time(TimeZone.getDefault().getID());
        time.setToNow();
        int yearDay = time.yearDay;
        int currHour = time.hour;

        int timeValue = yearDay * 100 + currHour;

//        int checkUpdateHour = SharePreferenceUtil.getCheckUpdateHour(getApplicationContext(), key);


//        if (checkUpdateHour != timeValue) {
        //检查更新
//            SharePreferenceUtil.saveCheckUpdateHour(getApplicationContext(), key, timeValue);
        UpdateChecker updateChecker = new UpdateChecker(this);
//            updateChecker.setCheckOverListener(this);
        updateChecker.startCheck();

//        }

    }

    private void getUserInfo() {
        User mUser = Session.getSession();
        if (mUser != null) {
            String sid = mUser.getSid();
            String real_uid = mUser.getReal_uid();
            String u_salt = AppUtil.getSalt();
            HashMap<String, String> params = new HashMap<>();
            params.put("sid", sid);
            params.put("real_uid", real_uid);
            params.put("u_salt", u_salt);

            String u_sign = UserCenterRetroAdapter.GetSign(params) + u_salt;
            OldRetroApiService service = UserCenterRetroAdapter.getUserCenterService();
            Observable<Result<User>> mObservable = service.getinfo(mUser.getSid(), mUser.getReal_uid(), u_salt, u_sign);
            mObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Result<User>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Result<User> result) {
                            if (result == null) {
                                return;
                            }

                            if (result.errno == 0) {
                                //刷新成功
                                Session.save(result.getData(), true);
                                QEventBus.getEventBus().post(new Events.LoginOk());
                            } else if (result.errno == 50010) {
                                //用户信息已过期，请重新登录 并退出当前页面
                                Session.save(null, true);
                                QEventBus.getEventBus().post(new Events.Logout());
                            } else {
                                //其他错误暂不处理
                            }
                        }
                    });
        }


    }


}
