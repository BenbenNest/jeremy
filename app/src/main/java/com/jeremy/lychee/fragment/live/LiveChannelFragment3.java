package com.jeremy.lychee.fragment.live;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.live.SelectChannelActivity;
import com.jeremy.lychee.adapter.live.LiveChannelsAdapter;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.preference.LiveSubscriptedChannelPreference;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.customview.live.PagerSlidingTabStrip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class LiveChannelFragment3 extends BaseFragment {
    private View mContentView;
    private RecyclerView channels_recyclerview;
    private LiveChannelsAdapter liveChannelsAdapter;
    private ImageView morechannel;
    private List<ColumnChannel> allChannels;  //总的list
    private List<ColumnChannel> orderchannels;  //必须订阅的
    private List<ColumnChannel> subscriptedChannels; //已经订阅了的
    public static String subscriptionkey = "subscriptedchannel";
    //最后一次订阅保持
    private String lastCids = "";
    private Fragment fragment;
    private View errorLayout;
    private View loadingLayout;

    PagerSlidingTabStrip tabStrip;
    ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;
    private LinearLayout tabsContainer;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView != null) {
            ViewParent mParent = mContentView.getParent();
            if (mParent != null && mParent instanceof ViewGroup) {
                ((ViewGroup) mParent).removeView(mContentView);
            }
            return mContentView;
        }
        mContentView = inflater.inflate(R.layout.fragment_livechannel3, container, false);
        tabStrip = (PagerSlidingTabStrip) mContentView.findViewById(R.id.tab_strip);
        viewPager = (ViewPager) mContentView.findViewById(R.id.viewpager);
        morechannel = (ImageView) mContentView.findViewById(R.id.morechannel);
        loadingLayout = mContentView.findViewById(R.id.loading_layout);
        errorLayout = mContentView.findViewById(R.id.error_layout);
        errorLayout.setOnClickListener(v1 -> {
            initChannels();
        });
        subscriptedChannels = new ArrayList<>();
        morechannel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SelectChannelActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectedChannels", (Serializable) subscriptedChannels);
            bundle.putSerializable("allchannels", (Serializable) allChannels);
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        });
        orderchannels = new ArrayList<>();
        initTab();
        initPager();

        QEventBus.getEventBus().register(this, QEventBus.EVENTBUS_PRIORITY_HIGH);
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initChannels();
        super.onViewCreated(view, savedInstanceState);
    }
    public void initPager(){
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), null);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                QEventBus.getEventBus().post(new LiveChannelFragment3.StopVideoPlaying());
                tabsContainer.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.white));
                for (int i = 0; i < fragmentAdapter.getmChannels().size(); i++) {
                    if (position == i) {
                        fragmentAdapter.getmChannels().get(i).setIsClicked(true);
                    } else {
                        fragmentAdapter.getmChannels().get(i).setIsClicked(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(fragmentAdapter);

    }

    private void initTab() {
        tabStrip.setIndicatorHeight(DensityUtils.dip2px(getContext(), 0f));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.livechannel_name_color));
        tabStrip.setTabPaddingLeftRight(10);
        tabStrip.setTextSize(18);
        tabStrip.setTextColor(Color.parseColor("#999999"));
        tabStrip.setSelectedTabTextColor(getResources().getColor(R.color.livechannel_name_color));
        tabStrip.setSelectedTabTextSize(18);
        tabStrip.setUnderlineHeight(AppUtil.dip2px(getContext(), 0));
        tabStrip.setDividerColor(0);
        tabStrip.setFullIndicatorWidth(false);
        tabsContainer = tabStrip.getTabsContainer();
    }

    private Observable<List<String>> getSubcriptedChannelCids() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                LiveSubscriptedChannelPreference subscriptedChannelPreference = new LiveSubscriptedChannelPreference();
                lastCids = subscriptedChannelPreference.getStringValue(subscriptionkey);
                List<String> lastcidList = new ArrayList<String>();
                if (TextUtils.isEmpty(lastCids)) {
                    for (int i = 0; i < orderchannels.size(); i++) {
                        lastcidList.add(orderchannels.get(i).getChannel_id());
                    }
                    subscriber.onNext(lastcidList);
                    subscriber.onCompleted();
                    return;
                }
                String[] subscriptedChannels = lastCids.split("\\|");
                for (int i = 0; i < subscriptedChannels.length; i++) {
                    lastcidList.add(subscriptedChannels[i]);
                }
                subscriber.onNext(lastcidList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    private void initChannels() {
        loadingLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        OldRetroAdapter.getService().getLiveChannels()
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<List<ColumnChannel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingLayout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ModelBase<List<ColumnChannel>> listModelBase) {
                        if (listModelBase.getErrno() == 0) {
                            loadingLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                            List<ColumnChannel> list = listModelBase.getData();
                            allChannels = list;
                            for (ColumnChannel columnChannel : list) {
                                if (!columnChannel.getChannel_type().equals(ColumnChannel.ChannelType.NOMAL)) {
                                    orderchannels.add(columnChannel);
                                }
                            }
                            getSubcriptedChannelCids()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(strings -> checkChannels(strings), Throwable::printStackTrace);
                        }

                    }
                });

    }

    private void checkChannels(List<String> subcriptedList) {
        //将获取下来的channel与已经订阅的进行对比   如果已经订阅的某个频道在获取下来的channel中没有，说明这个频道已经下线了
        if (allChannels != null && allChannels.size() > 0 && subcriptedList.size() > 0) {
            for (int i = 0; i < subcriptedList.size(); i++) {
                int index = 0;
                for (int j = 0; j < allChannels.size(); j++) {
                    if (subcriptedList.get(i).equals(allChannels.get(j).getChannel_id())) {

                    } else {
                        index++;
                    }
                }
                if (index == allChannels.size()) {
                    subcriptedList.remove(i);
                }
            }
            //服务端增加了必须订阅的频道以后  本地也需要自动给订阅上
            for (int i = 0; i < allChannels.size(); i++) {
                if (!allChannels.get(i).getChannel_type().equals(ColumnChannel.ChannelType.NOMAL)) {  //必须订阅的
                    int index = 0;
                    for (int j = 0; j < subcriptedList.size(); j++) {
                        if (allChannels.get(i).getChannel_id().equals(subcriptedList.get(j))) {
                        } else {
                            index++;
                        }
                    }
                    if (index == subcriptedList.size()) {
                        subcriptedList.add(allChannels.get(i).getChannel_id());
                    }
                }
            }
        }

        subscriptionChannels(subcriptedList);
    }

    private void subscriptionChannels(List<String> subcriptedCid) {
        List<ColumnChannel> unorderdelAndMoveList = new ArrayList<>();
        List<ColumnChannel> unorderdelList = new ArrayList<>();
        List<ColumnChannel> nomalList = new ArrayList<>();
        for (int i = 0; i < subcriptedCid.size(); i++) {
            for (int j = 0; j < allChannels.size(); j++) {
                if (subcriptedCid.get(i).equals(allChannels.get(j).getChannel_id())) {
                    if (allChannels.get(j).getChannel_type().equals(ColumnChannel.ChannelType.UNORDERDELANDMOVE)) {
                        unorderdelAndMoveList.add(allChannels.get(j));
                    } else if (allChannels.get(j).getChannel_type().equals(ColumnChannel.ChannelType.UNORDERDEL)) {
                        unorderdelList.add(allChannels.get(j));
                    } else if (allChannels.get(j).getChannel_type().equals(ColumnChannel.ChannelType.NOMAL)) {
                        nomalList.add(allChannels.get(j));
                    }
                }
            }
        }
        List<ColumnChannel> subscriptionChannels = new ArrayList<>();
        subscriptionChannels.addAll(unorderdelAndMoveList);
        subscriptionChannels.addAll(unorderdelList);
        subscriptionChannels.addAll(nomalList);
        savaSubscriptinChannels(subscriptionChannels);

    }

    private void savaSubscriptinChannels(List<ColumnChannel> list) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null && list.size() > 0) {
            subscriptedChannels = list;
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i).getChannel_id());
                if (i != list.size() - 1) {
                    stringBuilder.append("|");
                }
            }
        }

        fragmentAdapter.setChannels(list);
        tabStrip.setViewPager(viewPager);
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            ImageView imageButton = (ImageView) tabsContainer.getChildAt(i);
            Glide.with(getActivity())
                    .load(list.get(i).getChannel_icon())
                    .asBitmap()
                    .fitCenter()
                    .placeholder(getResources().getDrawable(R.drawable.livechannel_item_logo))
                    .override(AppUtil.dp2px(getContext(), 30), AppUtil.dp2px(getContext(), 30))
                    .into(new BitmapImageViewTarget(imageButton) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageButton.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isClicked()) {
                index = i;
                break;
            }
        }
        list.get(index).setIsClicked(true);
        tabsContainer.getChildAt(index).setBackgroundColor(getResources().getColor(R.color.white));
        /*tabsContainer.getChildAt(index).performClick();*/
        viewPager.setCurrentItem(index);
        LiveSubscriptedChannelPreference.getInstance().saveStringValue(subscriptionkey, stringBuilder.toString());

    }

    public void onEvent(LiveEvent.SubscriptedChannel event) {
        if (event != null && event.selectChannels != null && event.selectChannels.size() > 0) {
            savaSubscriptinChannels(event.selectChannels);
        }
    }

    @Override
    public void onDestroy() {
        QEventBus.getEventBus().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        QEventBus.getEventBus().post(new LiveChannelFragment3.StopVideoPlaying());
        super.onPause();
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private List<ColumnChannel> mChannels;
        private HashMap<Integer, BaseFragment> mPageReferenceMap = new HashMap<>();

        public FragmentAdapter(FragmentManager fm, List<ColumnChannel> channels) {
            super(fm);
            this.mChannels = channels;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment;
            String type = mChannels.get(position).getChannel_ctype();
            if (type.equals(ColumnChannel.ChannelCType.WATER)) {
                fragment = new ShuiDiFragment();
            } else if (type.equals(ColumnChannel.ChannelCType.HOTVIDEO)) {
                fragment = new LiveDiscoveryColumnFragment2();
                // fragmentHashMap.put(channel_id, fragment);
            } else {
                fragment = new NomalTVFragment();
            }
            ColumnChannel columnChannel = mChannels.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("channel_id", columnChannel.getChannel_id());
            bundle.putSerializable("lanmu", (Serializable) columnChannel.getLanmu());
            fragment.setArguments(bundle);
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            if (mChannels != null) {
                return mChannels.size();
            } else {
                return 0;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        public void setChannels(List<ColumnChannel> channelList) {
            this.mChannels = channelList;
            notifyDataSetChanged();
            mPageReferenceMap.clear();
        }

        public List<ColumnChannel> getmChannels() {
            return this.mChannels;
        }

        public BaseFragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mChannels.get(position).getChannel_name();
        }


        @Override
        public int getPageIconResId(int position) {
            return R.drawable.livechannel_item_logo;
        }
    }

    public static class StopVideoPlaying {

    }

    public void onEventMainThread(StopVideoPlaying event) {
        VideoPlayer.getInstance().stop();
    }
}
