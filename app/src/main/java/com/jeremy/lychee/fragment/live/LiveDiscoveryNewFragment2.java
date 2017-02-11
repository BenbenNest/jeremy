package com.jeremy.lychee.fragment.live;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Scroller;

import com.google.gson.reflect.TypeToken;
import com.jeremy.lychee.activity.live.LiveRecordShortVideoActivity;
import com.jeremy.lychee.activity.live.LiveRecordingActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.NewsListPreference;
import com.jeremy.lychee.preference.UserPreference;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.customview.live.ArcMenu;
import com.jeremy.lychee.customview.live.PagerSlidingTabStrip;
import com.jeremy.lychee.model.live.LiveDiscoveryTitle;
import com.jeremy.lychee.utils.AppUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by chengyajun on 2016/1/11.
 */
public class LiveDiscoveryNewFragment2 extends BaseFragment {
    @Bind(com.jeremy.lychee.R.id.id_menu)
    ArcMenu mArcMenu;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_layout;
    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_layout;


    View mContentView;
    @Bind(com.jeremy.lychee.R.id.tabs)
    PagerSlidingTabStrip tabs;


    @Bind(com.jeremy.lychee.R.id.vPager)
    LiveViewPager vPager;

    LiveDiscoveryViewpagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(com.jeremy.lychee.R.layout.live_discovery_new_layout2, container, false);
        ButterKnife.bind(this, mContentView);
        initView();
        initCache();
        initEvent();
        QEventBus.getEventBus().register(this);
        return mContentView;
    }

    private void initEvent() {
        mArcMenu.setOnMenuItemClickListener(

                (view, pos) -> checkNetStatus(getContext(), () -> {
                            if (view.getTag().equals("live")) {
                                if (!Session.isUserInfoEmpty()) {
                                    Intent intent = new Intent(
                                            LiveDiscoveryNewFragment2.this.getActivity(),
                                            LiveRecordingActivity.class);
                                    getActivity().startActivity(intent);
                                    return;
                                }
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(
                                        LiveDiscoveryNewFragment2.this.getActivity(),
                                        LiveRecordShortVideoActivity.class);
                                getActivity().startActivity(intent);
                            }
                        }

                ));
    }

    private static void checkNetStatus(Context context, Action0 cb) {
        if (AppUtil.isNetTypeMobile(context) &&
                !UserPreference.getInstance().getUseMobileNetEnabled()) {
            DialogUtil.showConfirmDialog(context, context.getString(com.jeremy.lychee.R.string.dialog_use_mobile_net_record),
                    context.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                        cb.call();
                        UserPreference.getInstance().setUseMobileNetEnabled(true);
                        dialog.dismiss();
                    }, context.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss
            );
            return;
        }
        cb.call();
    }

    /**
     * 初始化ViewPager
     */
    private void initView() {

        error_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDataFromNet(false);
                loading_layout.setVisibility(View.VISIBLE);
                error_layout.setVisibility(View.GONE);
            }
        });


        adapter = new LiveDiscoveryViewpagerAdapter(this.getChildFragmentManager());


//        Field mField = null;
//        try {
//            mField = ViewPager.class.getDeclaredField("mScroller");
//            mField.setAccessible(true);
//            MyScroller mScroller = new MyScroller(vPager.getContext(),
//                    new AccelerateInterpolator());
//            mField.set(vPager, mScroller);
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }


        vPager.setAdapter(adapter);
        vPager.setCurrentItem(0);
        vPager.setScanScroll(true);
        tabs.setIndicatorHeight(0);
        tabs.setIndicatorColor(Color.RED);
        tabs.setDividerColor(0);
        tabs.setTabBackground(com.jeremy.lychee.R.drawable.background_tab);
        tabs.setUnderlineHeight(1);
        tabs.setUnderlineColor(getResources().getColor(com.jeremy.lychee.R.color.live_column_title_underline));

        tabs.setTextSize(13);
        tabs.setTextColor(getResources().getColor(com.jeremy.lychee.R.color.live_column_titlte));
        tabs.setSelectedTabTextSize(13);
        tabs.setSelectedTabTextColor(getResources().getColor(com.jeremy.lychee.R.color.live_column_titlte_selected));
        tabs.setTabPaddingLeftRight(20);

    }


    //加载本地数据
    private void initCache() {
        loading_layout.setVisibility(View.VISIBLE);
        Observable
                .create(new Observable.OnSubscribe<List<LiveDiscoveryTitle>>() {
                    @Override
                    public void call(Subscriber<? super List<LiveDiscoveryTitle>> subscriber) {
                        subscriber.onNext(getCacheData());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LiveDiscoveryTitle>>() {
                    @Override
                    public void onCompleted() {
                        loading_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(List<LiveDiscoveryTitle> list) {
                        if (list == null || list.size() == 0) {
                            //本地无数据则网络请求数据
                            queryDataFromNet(false);
                        } else {
                            loading_layout.setVisibility(View.GONE);
                            adapter.setTitles(list);
                            adapter.notifyDataSetChanged();
                            tabs.setViewPager(vPager);
                            queryDataFromNet(true);

                        }
                    }
                });
    }

    //网络加载数据
    private void queryDataFromNet(boolean hasCache) {

        //网络读取数据
        OldRetroAdapter.getService().getLiveDiscoveryTitleList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(data -> cacheData(data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<List<LiveDiscoveryTitle>>>() {

                    @Override
                    public void onCompleted() {
                        loading_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading_layout.setVisibility(View.GONE);
                        if (!hasCache) {//本地无数据
                            error_layout.setVisibility(View.VISIBLE);
                        } else {
                            //本地有数据，但此时无网络
//                            if (isRefresh) {
//                                liveDiscoverySwipeRefreshLayout.setRefreshing(false);
//                            }
//
//                            ToastHelper.getInstance(LiveDiscoveryFragment.this.getContext()).toast("网络出错");
                        }
                    }

                    @Override
                    public void onNext(ModelBase<List<LiveDiscoveryTitle>> listModelBase) {
                        if (listModelBase != null && listModelBase.getErrno() == 0) {


                            loading_layout.setVisibility(View.GONE);
                            List<LiveDiscoveryTitle> titles = listModelBase.getData();
                            adapter.setTitles(titles);
                            adapter.notifyDataSetChanged();
                            tabs.setViewPager(vPager);
                        } else {
                            //TODO 数据错误
                            loading_layout.setVisibility(View.GONE);
                            if (!hasCache) {//本地无数据
                                error_layout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private List<LiveDiscoveryTitle> getCacheData() {
        String key = "faxiantitlecache";
        String value = NewsListPreference.getInstance().getStringValue(key);
        if (!TextUtils.isEmpty(value)) {
            Type type = new TypeToken<ModelBase<List<LiveDiscoveryTitle>>>() {
            }.getType();
            ModelBase<List<LiveDiscoveryTitle>> modelBase = GsonUtils.fromJson(value, type);
            if (modelBase != null) {
                return modelBase.getData();
            }
        }
        return null;
    }

    private void cacheData(ModelBase<List<LiveDiscoveryTitle>> listModelBase) {
        String value = GsonUtils.toJson(listModelBase);
        String key = "faxiantitlecache";
        NewsListPreference.getInstance().saveStringValue(key, value);
    }


    public static class LiveDiscoveryViewpagerAdapter extends FragmentPagerAdapter {

        List<LiveDiscoveryTitle> mTitles = new ArrayList<>();

        public LiveDiscoveryViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setTitles(List<LiveDiscoveryTitle> titles) {
            this.mTitles = titles;
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Override
        public Fragment getItem(int position) {
//            if (mTitles.get(position).getId().equals("rec")) {//推荐
//                LiveDiscoveryFragment liveDiscoveryFragment = new LiveDiscoveryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("cid", mTitles.get(position).getId());
//                liveDiscoveryFragment.setArguments(bundle);
//                return liveDiscoveryFragment;
//            } else if (mTitles.get(position).getId().equals("live")) {//我在现场
//                LiveDiscoveryFragment liveDiscoveryFragment = new LiveDiscoveryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("cid", mTitles.get(position).getId());
//                liveDiscoveryFragment.setArguments(bundle);
//                return liveDiscoveryFragment;
//            } else {
//                return LiveDiscoveryColumnFragment2.newInstance(mTitles.get(position).getId());
//            }
            return new LiveDiscoveryColumnFragment2();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position).getName();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }


    public class MyScroller extends Scroller {
        // 设置你需要的平移时间
        private int animTime = 200;

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, animTime);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, animTime);
        }

        public void setmDuration(int animTime) {
            this.animTime = animTime;
        }
    }


    final public void onEventMainThread(LiveEvent.showIntroLiveDiscoveryColse event) {
        mArcMenu.toggleMenu(400);

       Animation alphaAnimation= new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                mArcMenu.setBackgroundColor(AppUtil.transferAlpha(Color.WHITE,interpolatedTime));
            }
        };

        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(400);
        mArcMenu.setAnimation(alphaAnimation);
    }
}
