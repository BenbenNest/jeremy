package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.customview.user.PublishChannelsLayout;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.qihoo.sdk.report.QHStatAgent;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PublishActivity extends SlidingActivity {
    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolbar;

    @Bind(com.jeremy.lychee.R.id.channels_lay)
    PublishChannelsLayout channels_lay;

    @Bind(com.jeremy.lychee.R.id.rec_et)
    EditText rec_et;

    private String title;
    private String article;

    @Override
    protected void onPreInflation() {
        super.onPreInflation();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        article = intent.getStringExtra("article");
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_publish);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);

        mToolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_publish_article);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setTitle(com.jeremy.lychee.R.string.publish_title);

        requestAndShowChannelData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void onClickSure(MenuItem item) {
        if(TextUtils.isEmpty(title)){
            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.title_must_not_null);
            return;
        }
        String content = rec_et.getText().toString();
//        if(TextUtils.isEmpty(content)){
//            ToastHelper.getInstance(getApplicationContext()).toast(R.string.publish_rec);
//            return;
//        }
        WeMediaChannel checked_channel = channels_lay.getCheckedChannel();
        if(checked_channel==null){
            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.publish_check_channel);
            return;
        }
        String sub_id = checked_channel.getId();
        int type = 4;
        String sign = getSign(sub_id, content, type, title, article);

        Observable<ModelBase<Object>> mObservabe = OldRetroAdapter.getService().createArticle(sub_id, content, type, title, article, sign);
        mObservabe.subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.publish_error);
                    }

                    @Override
                    public void onNext(ModelBase<Object> objectModelBase) {
                        if(objectModelBase!=null){
                            if(objectModelBase.getErrno()==0){
                                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.publish_ok);
                                QHStatAgent.onEvent(PublishActivity.this, QHState.WRITINGARTICLE);
                                setResult(RESULT_OK);
                                finish();
                            }else {
                                ToastHelper.getInstance(getApplicationContext()).toast(objectModelBase.getErrmsg());
                            }

                        }
                    }
                });
    }

    private String getSign(String sub_id, String content, int type, String title, String article){
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        treeMap.put("channel", "cid");
        treeMap.put("os", Build.DISPLAY);
        treeMap.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        treeMap.put("os_type", "Android");
        treeMap.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        treeMap.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        treeMap.put("sid", Session.getSid());
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));

        treeMap.put("sub_id", sub_id);
        treeMap.put("content", content);
        treeMap.put("type", String.valueOf(type));
        treeMap.put("title", title);
        treeMap.put("article", article);
        Set<String> keySet = treeMap.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            stringBuilder.append(key);
            stringBuilder.append("=");
            if (treeMap.get(key) != null) {
                stringBuilder.append(treeMap.get(key));
            } else {
                stringBuilder.append("");
            }
            if (key != treeMap.lastKey()) {
                stringBuilder.append("&");
            }
        }
        stringBuilder.append("shi!@#$%^&*[xian!@#]*");
        String md5Str = AppUtil.getMD5code(stringBuilder.toString());
        return md5Str != null ? md5Str.substring(3, 10) : null;
    }

    public void onClickAdd(View view) {
        startActivityForResult(new Intent(getApplicationContext(), WeMediaCreateAlbumActivity.class), 9);
    }

    private void requestAndShowChannelData(){
        OldRetroAdapter.getService().getUserCreateMediaChannelList(1, 100, "")
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(channels_lay::setChannels, Throwable::printStackTrace);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9){
            OldRetroAdapter.getService().getUserCreateMediaChannelList(1, 100, "")
                    .map(ModelBase::getData)
                    .subscribeOn(Schedulers.io())
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(channels_lay::notifyChannelsChange, Throwable::printStackTrace);
        }
    }
}
