package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.fragment.user.WeMediaChannelAlbumsFragment;
import com.jeremy.lychee.fragment.user.WeMediaChannelArticlesFragment;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.widget.slidingtab.SlidingTabLayout;
import com.jeremy.lychee.fragment.user.WeMediaChannelVideosFragment;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.statusbar.StatusBarUtil;
import com.jeremy.lychee.customview.live.VerticalMenu;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaChannelActivity extends SlidingActivity {

    private static final String TAG = WeMediaChannelActivity.class.getSimpleName();

    @Bind(com.jeremy.lychee.R.id.user_page_tab_layout)
    SlidingTabLayout mTabLayout;
    @Bind(com.jeremy.lychee.R.id.user_viewpager)
    ViewPager mPager;
    @Bind(com.jeremy.lychee.R.id.column_icon)
    GlideImageView columnIcon;
    @Bind(com.jeremy.lychee.R.id.column_bg_image)
    GlideImageView columnBgPanel;
    @Bind(com.jeremy.lychee.R.id.column_sub)
    TextView columnSub;
    @Bind(com.jeremy.lychee.R.id.column_detail_toolbar)
    Toolbar columnDetailToolbar;
    @Bind(com.jeremy.lychee.R.id.column_detail_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(com.jeremy.lychee.R.id.sub_num)
    TextView mSubNum;
    @Bind(com.jeremy.lychee.R.id.reader_num)
    TextView mReaderNum;
    @Bind(com.jeremy.lychee.R.id.id_menu)
    VerticalMenu mArcMenu;

    private ViewPagerAdapter mAdapter;
    public WeMediaChannel getWeMediaInfo() {
        return mWeMediaInfo;
    }

    private WeMediaChannel mWeMediaInfo = new WeMediaChannel();

    private static final String USER_ID = "USER_ID";
    public static void startActivity(Context context, String userId) {
        Intent intent = new Intent(context, WeMediaChannelActivity.class);
        intent.putExtra(USER_ID, userId);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    @Override
    protected void setContentView() {
        QEventBus.getEventBus().register(this);
        setContentView(com.jeremy.lychee.R.layout.activity_wemedia_channel);
        ButterKnife.bind(this);
        getExtraParameters();
        updateChannelInfo(true);
    }

    private void getExtraParameters() {
        if (getIntent() != null) {
            mWeMediaInfo.setUid(getIntent().getStringExtra(USER_ID));
        } else {
            finish();
        }
    }

    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    private void login() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initToolBar(){
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        columnDetailToolbar.setNavigationIcon(com.jeremy.lychee.R.drawable.ic_actionbar_white_back);
        columnDetailToolbar.setNavigationOnClickListener(v -> onBackPressed());
        if( findViewById(com.jeremy.lychee.R.id.menu) != null ) {
            return;
        }
        columnDetailToolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_media_channel_activity);
        View view = findViewById(com.jeremy.lychee.R.id.menu);
        if (mWeMediaInfo.getIs_my()) {
            ((TextView) view.findViewById(com.jeremy.lychee.R.id.menu_text)).setText("编辑");
            view.setOnClickListener(v -> {
                //跳转到编辑界面
                WeMediaEditChannelActivity.startActivity(
                        WeMediaChannelActivity.this,
                        mWeMediaInfo.getBackimg(),
                        mWeMediaInfo.getIcon(),
                        mWeMediaInfo.getC_id(),
                        mWeMediaInfo.getName(),
                        mWeMediaInfo.getSummary(),
                        mWeMediaInfo.getIs_public()
                );
            });
        } else {
            ((TextView) view.findViewById(com.jeremy.lychee.R.id.menu_text)).setText(
                    mWeMediaInfo.getIs_sub() ? com.jeremy.lychee.R.string.channel_booked : com.jeremy.lychee.R.string.channel_unbook);
            view.setOnClickListener(v -> {
                //订阅按钮
                if (isLogin()) {
                    subscribeWeMediaChannel(
                            Integer.parseInt(mWeMediaInfo.getC_id()), !mWeMediaInfo.getIs_sub());
                    //更新个人自频道主页的阅读数量
                    QEventBus.getEventBus().post(new Events.OnWeMediaChannelInfoUpdated());
                } else {
                    login();
                }
            });
        }
    }

    private void updateChannelInfo(boolean loadData) {
        requestWeMediaChannelInfo(mWeMediaInfo.getUid())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mCollapsingToolbarLayout.setTitle(s.getName());
                    mSubNum.setText(s.getFollow() != null ? s.getFollow() : "0");
                    mReaderNum.setText(s.getFans() != null ? s.getFans() : "0");
                    columnSub.setText(s.getSummary());
                    columnIcon.loadImage(s.getIcon(), (req, v) -> req
                            .placeholder(AppUtil.getDefaultCircleIcon(this))
                            .crossFade()
                            .bitmapTransform(new GlideCircleTransform(this))
                            .into(v));
                    columnBgPanel.loadImage(s.getBackimg(), (req, v) -> req
                            .placeholder(com.jeremy.lychee.R.drawable.topic_head_backimage)
                            .error(com.jeremy.lychee.R.drawable.topic_head_backimage)
                            .centerCrop()
                            .crossFade()
                            .into(v));
                    mWeMediaInfo = s;
                    if (loadData) {
                        initToolBar();
                        initViewpager();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastHelper.getInstance(this).toast("网络错误");
                });

    }


    @OnClick(com.jeremy.lychee.R.id.sub_num_clickable)
    void OnClickSubNum(){
        WeMediaSubscribedChannelsActivity.startActivity(this, mWeMediaInfo.getUid());
//        openActivity(WeMediaSubscribedChannelsActivity.class);
    }

    @OnClick(com.jeremy.lychee.R.id.reader_num_clickable)
    void OnClickSubReaderNum() {
        WeMediaMyReadersActivity.startActivity(this, mWeMediaInfo.getC_id());
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            //状态栏沉浸
            setFitsSystemWindows(false);
            setStatusBarTransparent(true);
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
            columnDetailToolbar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = columnDetailToolbar.getLayoutParams();
            params.height += statusBarHeight;
            columnDetailToolbar.setLayoutParams(params);
            //4.4沉浸，上面必须android:fitsSystemWindows="false"
            findViewById(com.jeremy.lychee.R.id.column_detail_toolbar_bg).setFitsSystemWindows(false);
            //Bug 78036: CollapsingToolbarLayout toolbar title offset
            try {
                Field field = mCollapsingToolbarLayout.getClass().
                        getDeclaredField("mExpandedMarginTop");
                field.setAccessible(true);
                field.set(mCollapsingToolbarLayout, AppUtil.dip2px(this, 28));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上状态栏沉浸
            setFitsSystemWindows(false);
            setStatusBarTransparent(true);
            //5.0以上沉浸，上面必须android:fitsSystemWindows="true"
            findViewById(com.jeremy.lychee.R.id.column_detail_toolbar_bg).setFitsSystemWindows(true);
        }
        //ArcMenu init
        if (!Session.isUserInfoEmpty()) {
            mArcMenu.setVisibility(View.VISIBLE);
        } else {
            mArcMenu.setVisibility(View.GONE);
        }

    }

    private void initViewpager() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(mAdapter.fragmentsInfo.length);
        mTabLayout.setViewPager(mPager);
    }

    public static class NoPublicFragment extends android.support.v4.app.Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return LayoutInflater.from(getContext()).inflate(com.jeremy.lychee.R.layout.wemedia_nopublic_layout, null, false);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final FragmentInfo fragmentsInfo[] = {
                new FragmentInfo("文章", WeMediaChannelArticlesFragment.class),
                new FragmentInfo("专辑", WeMediaChannelAlbumsFragment.class),
                new FragmentInfo("视频直播", WeMediaChannelVideosFragment.class),
        };
        private Fragment fragments[] = new Fragment[fragmentsInfo.length];
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentsInfo.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (!mWeMediaInfo.getIs_my()
                    && !mWeMediaInfo.getIs_public().equals(0)) {//非公开
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        return new NoPublicFragment();
                }
            }
            if (fragments[position] == null) {
                try {
                    fragments[position] =
                            (Fragment) fragmentsInfo[position].fragmentClazz_.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsInfo[position].title_;
        }

        private class FragmentInfo {
            CharSequence title_;
            Class fragmentClazz_;

            FragmentInfo(CharSequence title, Class clazz) {
                title_ = title;
                fragmentClazz_ = clazz;
            }
        }

    }

    private void subscribeWeMediaChannel(int cId, boolean isSub) {
        if (isSub) {
            OldRetroAdapter.getService().subscribeColumn(cId, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if(s.getErrno() == 0){
                            mWeMediaInfo.setIs_sub(true);
                            ((TextView) findViewById(com.jeremy.lychee.R.id.menu_text)).setText(com.jeremy.lychee.R.string.channel_booked);
                            QEventBus.getEventBus().post(new Events.OnSubscribedChannelUpdated(cId, true));
                        }else{
                            ToastHelper.getInstance(this).toast("订阅失败");
                        }
                    }, e -> {
                        ToastHelper.getInstance(this).toast("订阅失败");
                        e.printStackTrace();
                    });
        } else {
            OldRetroAdapter.getService().unSubscribeColumn(cId, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getErrno() == 0) {
                            mWeMediaInfo.setIs_sub(false);
                            ((TextView) findViewById(com.jeremy.lychee.R.id.menu_text)).setText(com.jeremy.lychee.R.string.channel_unbook);
                            QEventBus.getEventBus().post(new Events.OnSubscribedChannelUpdated(cId, false));
                        } else {
                            ToastHelper.getInstance(this).toast("取消订阅失败");
                        }
                    }, e -> {
                        ToastHelper.getInstance(this).toast("取消订阅失败");
                        e.printStackTrace();
                    });
        }
    }

    private Observable<WeMediaChannel> requestWeMediaChannelInfo(String uid){
        return OldRetroAdapter.getService()
                .getUserChannel(uid)
                .filter(s -> s.getErrno() == 0)
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle());
    }

    final public void onEventMainThread(Events.OnWeMediaChannelInfoUpdated event) {
        if (isLogin() && mWeMediaInfo.getUid().equals(Session.getSession().getReal_uid())) {
            updateChannelInfo(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }
}
