package com.jeremy.lychee.fragment.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.jeremy.lychee.activity.news.SearchActivity;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.news.ChannelManagerActivity;
import com.jeremy.lychee.activity.news.CityListActivity;
import com.jeremy.lychee.activity.news.OrderedChannelsActivity;
import com.jeremy.lychee.adapter.news.ContentAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.db.NewsChannelDao;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.manager.ConfigManager;
import com.jeremy.lychee.manager.NewsChannelManager;
import com.jeremy.lychee.customview.live.PagerSlidingTabStrip;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsFragment extends BaseFragment {

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.viewStub)
    ViewStub viewStub;
    @Bind(R.id.tab_strip)
    PagerSlidingTabStrip tabStrip;

    private ContentAdapter contentAdapter;
    private View errorView;
    private View loadingView;
    //    private TextView titleName;
//    private View moreImg;
    @Bind(R.id.channel_add_img)
    View channelAddImg;
    //    private View leftCoverView;
//    private View rightCoverView;
    @Bind(R.id.logo_img)
    View appLogo;
    private boolean isChannelCoverShow = true;
    private float pressedX;
    private float pressedY;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
//        tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tab_strip);
//        titleName.setTypeface(FontManager.getKtTypeface());
//        moreImg = view.findViewById(R.id.more_img);
//        appLogo = view.findViewById(R.id.logo_img);
//        moreImg.setOnClickListener(v -> togglePopupWindow(false));
        channelAddImg.setOnClickListener(v -> {
            if (getContext() instanceof SlidingActivity) {
                SlidingActivity activity = (SlidingActivity) getContext();
                Bundle bundle = new Bundle();
                bundle.putString("currentCid", getCurrentFragment().getCid());
                activity.openActivity(OrderedChannelsActivity.class, bundle, 0);
                QEventBus.getEventBus().post(new Events.HidePopWinEvent());
            }
        });
        appLogo.setOnClickListener(v -> {
            if (getContext() instanceof SlidingActivity) {
                SlidingActivity activity = (SlidingActivity) getContext();
                activity.openActivity(CityListActivity.class);

                QEventBus.getEventBus().post(new Events.HidePopWinEvent());
            }
//            if (getCurrentFragment() != null) {
//                getCurrentFragment().goRefresh(true);
//            }
        });
        initViewPager();
        initChannelWheel();
//        leftCoverView = view.findViewById(R.id.left_cover_view);
//        rightCoverView = view.findViewById(R.id.right_cover_view);
        return view;
    }

    private void initChannelWheel() {
//        channelWheel.setTypeface(FontManager.getKtTypeface());
        tabStrip.setIndicatorHeight(DensityUtils.dip2px(getContext(), 2f));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.livechannel_name_color));
        tabStrip.setTabPaddingLeftRight(10);
        tabStrip.setTextSize(18);
        tabStrip.setTextColor(Color.parseColor("#999999"));
        tabStrip.setSelectedTabTextColor(getResources().getColor(R.color.livechannel_name_color));
        tabStrip.setSelectedTabTextSize(18);
        tabStrip.setUnderlineColor(getResources().getColor(R.color.livechannel_name_color));
        tabStrip.setUnderlineHeight(0);
        tabStrip.setUnderlineColor(getResources().getColor(R.color.live_column_title_underline));
      /*  tabs.setIndicatorColor();*/
        tabStrip.setDividerColor(0);
        tabStrip.setFullIndicatorWidth(false);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        NewsChannelManager.getInstance().reset();
        super.onDestroyView();
    }

    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logger.d("content fragment position:" + position);
                ContentFragment currentFragment = contentAdapter.getFragment(viewPager.getCurrentItem());
                if (currentFragment == null) {
                    contentAdapter.setChannels(NewsChannelManager.getInstance().getNewsChannelList());
                    currentFragment = contentAdapter.getFragment(viewPager.getCurrentItem());
                }
                if (currentFragment != null) {
                    currentFragment.goRefresh();
//                    titleName.setText(currentFragment.getChannelName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        contentAdapter = new ContentAdapter(getChildFragmentManager(), null);
        viewPager.setAdapter(contentAdapter);

        getNewsChannelObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(this::validCheck)
                .subscribe(newsChannels -> {
                    contentAdapter.setChannels(newsChannels);
                    NewsChannelManager.getInstance().setNewsChannelList(newsChannels);
//					channelWheel.setViewPager(viewPager);
                    tabStrip.setViewPager(viewPager);
                    ContentFragment currentFragment = contentAdapter.getFragment(viewPager.getCurrentItem());
                    if (currentFragment == null) {
                        Logger.t("NewsFragment").d("init-> get current channel fragment is null");
                    }
                    if (newsChannels != null && newsChannels.size() > 0) {
                        NewsChannel newsChannel = newsChannels.get(0);
//                        titleName.setText(newsChannel.getChannelName());
                    }
                    ContentFragment currentFragment1 = contentAdapter.getFragment(viewPager.getCurrentItem());
                    if (currentFragment1 != null) {
                        currentFragment1.goRefresh();
                    }

//                    viewPager.postDelayed(() -> {
//
//                    }, 100);
                }, Throwable::printStackTrace);
    }

    @NonNull
    private Boolean validCheck(List<NewsChannel> list) {
        if (list == null) {
            showErrorAndLoading();
            return false;
        } else {
            if (errorView != null) {
                errorView.setVisibility(View.GONE);
            }
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
            return true;
        }
    }

    private void showErrorAndLoading() {
        if (errorView == null || loadingView == null) {
            View view = viewStub.inflate();
            errorView = view.findViewById(R.id.error_layout);
            loadingView = view.findViewById(R.id.loading_layout);

            errorView.setOnClickListener(v -> {
                ConfigManager.getInstance().updateNewsChannel();
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
            });
        }
        errorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    private void doSearch() {
        ((SlidingActivity) getActivity()).openActivity(SearchActivity.class);
    }

    public Observable<List<NewsChannel>> getNewsChannelObservable() {
        return Observable.create(new Observable.OnSubscribe<List<NewsChannel>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannel>> subscriber) {
                NewsChannelPreference newsChannelPreference = new NewsChannelPreference();
                String cids = newsChannelPreference.getNewsChannelCids();
                if (TextUtils.isEmpty(cids)) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    return;
                }
                String[] newsChannels = cids.split("\\|");
                NewsChannelDao dao = ContentApplication.getDaoSession().getNewsChannelDao();
                List<NewsChannel> newsChannelList = new ArrayList<>();
                for (String cid : newsChannels) {
                    if (TextUtils.isEmpty(cid)) {
                        continue;
                    }
/*                    List<NewsChannel> list = dao.queryBuilder()
                            .where(NewsChannelDao.Properties.Cid.eq(cid))
//                            .where(NewsChannelDao.Properties.IsShow.eq(Boolean.TRUE))
                            .build().list();*/

                    List<NewsChannel> list = Stream.of(dao.queryBuilder().where(NewsChannelDao.Properties.Cid.eq(cid)).build().list()).map(item -> {
                        NewsChannel resultItem = new NewsChannel();
                        resultItem.setCid(item.getCid());
                        resultItem.setCname(item.getCname());
                        resultItem.setIcon(item.getIcon());
                        resultItem.setInit(item.getInit());
                        resultItem.setIs_start(item.getIs_start());
                        resultItem.setTagname(item.getTagname());
                        resultItem.setIsShow(item.getIsShow());
                        return resultItem;
                    }).collect(Collectors.toList());
                    if (list != null && list.size() == 1) {
                        newsChannelList.add(list.get(0));
                    }
                }
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onNext(newsChannelList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public ContentFragment getCurrentFragment() {
        if (contentAdapter.getCount() == 0) {
            return null;
        } else {
            return contentAdapter.getFragment(viewPager.getCurrentItem());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QEventBus.getEventBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    public void onEventMainThread(Events.GoToChannelByIndex event) {
        if (event == null) {
            return;
        }
        viewPager.setCurrentItem(event.index, true);
    }

    public void onEventMainThread(Events.OnNewsDeleted event) {
        //Hitlog 2005打点
        HitLog.hitDislike(
                getCurrentFragment().getSceneId(), getCurrentFragment().getCid(), "dislike", event.data);
    }


    public void onEventMainThread(Events.UpdateNewsChannelListEvent event) {
        if (event == null) {
            return;
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        getNewsChannelObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsChannels -> {
                    NewsChannelManager.getInstance().setNewsChannelList(newsChannels);
                    contentAdapter.setChannels(newsChannels);
//                    channelWheel.setViewPager(viewPager);
                    tabStrip.setViewPager(viewPager);
                    if (event.currentChannelIndex != -1) {
                        viewPager.setCurrentItem(event.currentChannelIndex);
                    }
                }, Throwable::printStackTrace);
    }

    public void onEventMainThread(Events.UpdateNewsChannelError event) {
        if (event == null) {
            return;
        }
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(Events.GoToChannelManage event) {
        if (getActivity() instanceof SlidingActivity) {
            SlidingActivity activity = (SlidingActivity) getActivity();
            Bundle bundle = new Bundle();
            if (getCurrentFragment() != null) {
                bundle.putString(ContentFragment.EXTRA_CHANNEL_ALIAS, getCurrentFragment().getCid());
            }
            activity.openActivity(ChannelManagerActivity.class, bundle, 0);
        } else {
            Intent intent = new Intent(getContext(), ChannelManagerActivity.class);
            if (getCurrentFragment() != null) {
                intent.putExtra(ContentFragment.EXTRA_CHANNEL_ALIAS, getCurrentFragment().getCid());
            }
            startActivity(intent);
        }
    }

    public void onEventMainThread(Events.UpdateNewsEvent event) {
        if (getCurrentFragment() != null) {
            getCurrentFragment().goRefresh(true);
        }
    }
}
