package com.jeremy.lychee.customview.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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

import com.jeremy.lychee.activity.user.MessageActivity;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.SettingsActivity;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.base.BaseLayout;
import com.jeremy.lychee.fragment.user.UserDiscoveryFragment2;
import com.jeremy.lychee.fragment.user.UserSubscribeFragment2;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.customview.live.VerticalMenu;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.widget.slidingtab.SlidingTabLayout;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserView extends BaseLayout {
    private static final String TAG = UserView.class.getSimpleName();

    @Bind(R.id.appbar)
    AppBarLayout mAppBar;

    @Bind(R.id.user_page_tab_layout)
    SlidingTabLayout mTabLayout;

    @Bind(R.id.user_viewpager)
    ViewPager mPager;

    @Bind(R.id.user_icon)
    GlideImageView mIvAvatar;

    @Bind(R.id.user_page_toolbar_collapse)
    CollapsingToolbarLayout mToolbarLayout;

    @Bind(R.id.id_menu)
    VerticalMenu mArcMenu;

    @Bind(R.id.message_red_pot)
    View mMessageRedPot;

    private ViewPagerAdapter mAdapter;
    private Fragment mParentFragment;
    private Toolbar mToolBar;

    public UserView(Context context, Fragment fragment) {
        super(context);
        mParentFragment = fragment;
    }



    @OnClick(R.id.user_icon)
    void CallOnClick(View view) {
        if (!Session.isUserInfoEmpty()) {
            WeMediaChannelActivity.startActivity(getContext(), Session.getSession().getReal_uid());
            return;
        }
        ((SlidingActivity) getContext()).openActivity(LoginActivity.class);

    }

    @Override
    protected void onInflate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.user_view_layout, this);

        //点击设置按钮，跳转到设置界面
        SlidingActivity userActivity = (SlidingActivity) context;
        findViewById(R.id.user_setting_btn).setOnClickListener(v -> {
            //跳转到设置界面
            userActivity.openActivity(SettingsActivity.class);
        });
        findViewById(R.id.user_message_btn).setOnClickListener(v -> {
            //跳转到消息界面
            userActivity.openActivity(MessageActivity.class);
            clearMsgNotification();
        });

    }

    @Override
    protected void initUI(Activity activity) {
        mAdapter = new ViewPagerAdapter(mParentFragment.getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(mAdapter.fragmentsInfo.length);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter != null) {
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        if (i != position) {
                            mAdapter.getItem(i).onHiddenChanged(true);
                        } else {
                            mAdapter.getItem(i).onHiddenChanged(false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTabLayout.setViewPager(mPager);

        clearMsgNotification();

        bindUserInfo(activity);
        updateUserInfo();

        //disable parallax slide
        ((AppBarLayout.LayoutParams) mToolbarLayout.getLayoutParams()).setScrollFlags(0);

    }

    @Deprecated
    public void onShown() {
        mAdapter.getItem(mPager.getCurrentItem()).onHiddenChanged(false);
    }

    public void bindUserInfo(Context context) {
        if (!Session.isUserInfoEmpty()) {
            User user = Session.getSession();
            mIvAvatar.loadImage(user.getUserpic(), (req, v) -> req
                    .crossFade()
                    .bitmapTransform(new GlideCircleTransform(context))
                    .placeholder(R.drawable.default_avatar)
                    .into(v));
        } else {
            mToolbarLayout.setTitle(getResources().getString(R.string.login_hint));
            mIvAvatar.setImageResource(R.drawable.default_avatar);
            mPager.setCurrentItem(0);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            //登录状态变更后，刷新最新文章
            ((UserDiscoveryFragment2)mAdapter.getItem(0)).loadData(false);
        }
    }

    public void updateUserInfo() {
        if (Session.getSession() != null) {
            OldRetroAdapter.getService()
                    .getUserChannel(Session.getSession().getReal_uid())
                    .filter(s -> s.getErrno() == 0)
                    .map(ModelBase::getData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        mIvAvatar.loadImage(s.getIcon(), (req, v) -> req
                                .crossFade()
                                .bitmapTransform(new GlideCircleTransform(getContext()))
                                .placeholder(R.drawable.default_avatar)
                                .into(v));
                    }, Throwable::printStackTrace);
        }
    }

    @SuppressLint("ValidFragment")
    public static class NotLoginFragment extends android.support.v4.app.Fragment {

        private int mType = 0;
        public NotLoginFragment(int type){
            mType = type;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup v;
            switch (mType){
                case 1://消息
                    v = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.not_login_layout_user_msg, null, false);
                    break;
                case 0://订阅
                default:
                    v = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.not_login_layout_user_sub, null, false);
            }
            TextView textView = (TextView) v.findViewById(R.id.no_data_text);
            textView.setText(R.string.live_mine_login_txt);
            textView.setOnClickListener(e -> startActivity(new Intent(getContext(), LoginActivity.class)));
            return v;
        }
    }

    private void showMsgNotification() {
        mMessageRedPot.setVisibility(VISIBLE);
    }

    private void clearMsgNotification() {
        if (mMessageRedPot.isShown()) {
            mMessageRedPot.setVisibility(GONE);
        }
    }

    public void onEvent(Events.OnMessageReceived event) {
        showMsgNotification();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final FragmentInfo fragmentsInfo[] = {
                new FragmentInfo("最新", UserDiscoveryFragment2.class),
                new FragmentInfo("订阅", UserSubscribeFragment2.class),
//                new FragmentInfo("消息", UserMessageFragment.class),
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
            if (Session.isUserInfoEmpty()) {//未登录
                switch (position) {
                    case 1:
                        return new NotLoginFragment(0);
                    case 2:
                        return new NotLoginFragment(1);
                }
            }
            if (position == 2) {
                clearMsgNotification();
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

        //偶现No view found for id  (id/view_pager) for fragment
        //使用container_id之前，先对其进行判断
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (UserView.this.findViewById(R.id.user_viewpager) == null) {
                Logger.t(TAG).e("viewpager id not found");
                return getItem(position);
            }
            return super.instantiateItem(container, position);
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

}

