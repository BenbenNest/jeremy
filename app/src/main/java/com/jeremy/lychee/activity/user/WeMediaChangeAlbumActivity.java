package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.customview.user.AlbumSelectRecyclerView;
import com.jeremy.lychee.R;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.base.ImmersionActivity;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.statusbar.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangp on 15-11-25.
 */
public class WeMediaChangeAlbumActivity extends ImmersionActivity {
    @Bind(R.id.transmit_toolbar)
    Toolbar mToolBar;

    @Bind(R.id.transmit_selecter)
    AlbumSelectRecyclerView mSelectRecyclerView;

    private String[] mTransmitId;
    private String mAlbumId;
    private boolean mIsAllSelect;

    @Override
    protected void setContentView() {
        QEventBus.getEventBus().register(this);
        setContentView(R.layout.activity_wemedia_change_album);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.transmitactivity_anim_enter, R.anim.newsdetailactivity_anim_out);
        mTransmitId = getIntent().getStringArrayExtra(Constants.BUNDLE_KEY_TRANSMIT_ID);
        mAlbumId = getIntent().getStringExtra(Constants.BUNDLE_KEY_ALBUM_ID);
        mIsAllSelect = getIntent().getBooleanExtra(Constants.BUNDLE_KEY_IS_ALL_SELECT, false);
    }

    public static void startActivity(Context context, boolean isAllSelect, final String[] transmitIds, String albumId ) {
        Intent intent = new Intent(context, WeMediaChangeAlbumActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_TRANSMIT_ID, transmitIds);
        intent.putExtra(Constants.BUNDLE_KEY_ALBUM_ID, albumId);
        intent.putExtra(Constants.BUNDLE_KEY_IS_ALL_SELECT, isAllSelect);
        context.startActivity(intent);
    }

    @Override
    public View onGetFitWindowView() {
        return findViewById(R.id.parent_layout);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏沉浸
            setFitsSystemWindows(false);
            setStatusBarColor(getResources().getColor(R.color.toolbar_color));
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
            mToolBar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = mToolBar.getLayoutParams();
            params.height += statusBarHeight;
            mToolBar.setLayoutParams(params);
        }

        configToolbar(R.menu.menu_change_album_confirm, R.string.title_activity_change_album, R.mipmap.ic_transmit_close);
        findViewById(R.id.menu_confirm).setOnClickListener(
                v -> {
                    if (!TextUtils.isEmpty(mSelectRecyclerView.getSubmitChannel())) {
                        moveArticleTo(mIsAllSelect, mSelectRecyclerView.getSubmitChannel());
                    }
                }
        );
        mSelectRecyclerView.setDefaultSelect(mAlbumId);
        requestAndShowAlbumList();

    }


    private Toolbar configToolbar(int menuId, int titleId, int navigationIconId) {
        if (menuId != 0) {
            mToolBar.inflateMenu(menuId);
        }
        if (titleId != 0) {
            mToolBar.setTitle(titleId);
        }
        if (navigationIconId != 0) {
            mToolBar.setNavigationIcon(navigationIconId);
            mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        }
        return mToolBar;
    }

    private void requestAndShowAlbumList() {
        OldRetroAdapter.getService().getUserCreateMediaChannelList(1, 100, "")
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSelectRecyclerView::showAlbums,
                        Throwable::printStackTrace);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    final public void onEventMainThread(Events.OnAlbumListUpdated event) {
        requestAndShowAlbumList();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.transmitactivity_anim_out);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void moveArticleTo(boolean allSelect, String desId){
        StringBuilder sb = new StringBuilder();
        for (String s : mTransmitId) {
            sb.append(",");
            sb.append(s);
        }
        if (sb.length() > 0) {
            sb.replace(0, 1, "");
        }

        OldRetroAdapter.getService().moveFeed(
                mAlbumId, desId,
                allSelect ? "all" : sb.toString(), allSelect ? sb.toString() : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getErrno() == 0) {
                        ToastHelper.getInstance(this).toast("移动成功");
                        QEventBus.getEventBus().post(new com.jeremy.lychee.eventbus.news.Events.OnNewsAlbumChanged(desId));
                        QEventBus.getEventBus().post(new Events.OnAlbumDetailUpdated(null));
                        finish();
                    } else {
                        ToastHelper.getInstance(this).toast("移动失败");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastHelper.getInstance(this).toast("移动失败");
                });
    }
}
