package com.jeremy.lychee.fragment.live;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.live.SelectChannelActivity;
import com.jeremy.lychee.adapter.live.LiveChannelsAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.preference.LiveSubscriptedChannelPreference;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.videoplayer.VideoPlayerView;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class LiveChannelFragment2 extends BaseFragment {
    private View mContentView;
    private RecyclerView channels_recyclerview;
    private LiveChannelsAdapter liveChannelsAdapter;
    private ImageView morechannel;
    private ImageView miniPlayerPlaceHolder;
    private VideoPlayerView miniPlayerView;
    private RelativeLayout miniPlayerContainer;

    private List<ColumnChannel> allChannels;  //总的list
    private List<ColumnChannel> orderchannels;  //必须订阅的
    private List<ColumnChannel> subscriptedChannels; //已经订阅了的
    private String subscriptionkey = "subscriptedchannel";
    //最后一次订阅保持
    private String lastCids = "";
    private Fragment fragment;
    private View errorLayout;
    private View loadingLayout;

    public static int playingItemId = -1;
    public static boolean isShownInMiniPlayer = false;
    public static String channelIdForCurrentVideo;
    public static WeakReference<Drawable> drawableForCurrentVideo;

    private Map<String, Fragment> fragmentHashMap;

    public static class PlayVideoInMiniPlayer {
    }

    public static class PlayVideoInList {
    }

    public static class CloseMiniPlayer {
    }

    public static class PositionToPlayingVideo {
        public String channelId;

        public PositionToPlayingVideo(String channelId) {
            this.channelId = channelId;
        }
    }

    public static class StopVideoPlaying {
    }

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
        fragmentHashMap = new HashMap<>();
        mContentView = inflater.inflate(R.layout.fragment_livechannel2, container, false);
        morechannel = (ImageView) mContentView.findViewById(R.id.morechannel);
        miniPlayerContainer = (RelativeLayout) mContentView.findViewById(R.id.mini_player);
        miniPlayerContainer.setVisibility(View.INVISIBLE);

        miniPlayerPlaceHolder = (ImageView) mContentView.findViewById(R.id.mini_place_holder);
        miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);

        miniPlayerView = (VideoPlayerView) mContentView.findViewById(R.id.mini_player_view);
        miniPlayerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector mDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (!TextUtils.isEmpty(channelIdForCurrentVideo)) {
                        int index = -1;
                        for (int i = 0; i < liveChannelsAdapter.getChannelList().size(); i++) {
                            if (liveChannelsAdapter.getChannelList().get(i).getChannel_id().equals(channelIdForCurrentVideo)) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            channels_recyclerview.scrollToPosition(index);
                            final int finalIndex = index;
                            new Handler().postDelayed(() -> {
                                View view = channels_recyclerview.getLayoutManager().findViewByPosition(finalIndex);
                                if (view != null) {
                                    view.performClick();
                                    QEventBus.getEventBus().post(new PositionToPlayingVideo(channelIdForCurrentVideo));
                                }

                            }, 250);

                            // channels_recyclerview.findViewHolderForAdapterPosition(index).itemView.performClick();
                            //getFragment(channelIdForCurrentVideo, ColumnChannel.ChannelCType.NOMAL);

                        }

                    }
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (e2.getX() - e1.getX() > 20 && Math.abs(velocityX) > 0) {
                        QEventBus.getEventBus().post(new CloseMiniPlayer());
                    }
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                mDetector.onTouchEvent(event);
                return false;
            }
        });


        mContentView.findViewById(R.id.close_mini_player).setOnClickListener(v -> {
            QEventBus.getEventBus().post(new CloseMiniPlayer());
        });

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
            //QEventBus.getEventBus().post(new Events.GoToActivity(SelectChannelActivity.class, bundle));
        });
        channels_recyclerview = (RecyclerView) mContentView.findViewById(R.id.channels_recyclerview);
        channels_recyclerview.getItemAnimator().setChangeDuration(100);
        orderchannels = new ArrayList<>();
        liveChannelsAdapter = new LiveChannelsAdapter(getContext(), orderchannels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        channels_recyclerview.setLayoutManager(linearLayoutManager);
        channels_recyclerview.setAdapter(liveChannelsAdapter);
        QEventBus.getEventBus().register(this, QEventBus.EVENTBUS_PRIORITY_HIGH);
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initChannels();
    }

    private void getFragment(String channel_id, String type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (fragmentHashMap == null && fragmentHashMap.size() < 0) {
            return;
        } else {
            if (TextUtils.isEmpty(channelIdForCurrentVideo)) {
                Iterator iter = fragmentHashMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                        transaction.remove((Fragment) entry.getValue());
                }
                fragmentHashMap.clear();

            } else {
                Iterator iter = fragmentHashMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (!channelIdForCurrentVideo.equals(entry.getKey().toString())){
                        transaction.remove((Fragment) entry.getValue());
                    }
                }
                Fragment channelIdForCurrent = fragmentHashMap.get(channelIdForCurrentVideo);
                fragmentHashMap.clear();
                fragmentHashMap.put(channelIdForCurrentVideo,channelIdForCurrent);
            }
            if (fragmentHashMap.containsKey(channel_id)) {
                fragment = fragmentHashMap.get(channel_id);
            } else {
                if (type.equals(ColumnChannel.ChannelCType.WATER)) {
                    fragment = new ShuiDiFragment();
                    // fragmentHashMap.put(channel_id, fragment);
                } else if (type.equals(ColumnChannel.ChannelCType.HOTVIDEO)) {
                    fragment = new LiveDiscoveryColumnFragment2();
                    // fragmentHashMap.put(channel_id, fragment);
                } else {
                    fragment = new NomalTVFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("channel_id", channel_id);
                fragment.setArguments(bundle);
            }
        }



        if (!fragment.isAdded()) {
            for (Fragment frag : fragmentHashMap.values()) {
                transaction.hide(frag);
            }
            fragmentHashMap.put(channel_id, fragment);
            transaction.add(R.id.channel_data_view, fragment).show(fragment).commitAllowingStateLoss();
        } else {
            for (Fragment frag : fragmentHashMap.values()) {
                transaction.hide(frag);
            }
            transaction.show(fragment).commitAllowingStateLoss();
        }
       /* int index=0;
        for (int i=0;i<liveChannelsAdapter.getChannelList().size();i++){
            if (liveChannelsAdapter.getChannelList().get(i).getChannel_id().equals(channel_id)){
                index=i;
                break;
            }
        }*/
        /*liveChannelsAdapter.setNewIndex(index);
        int oldindex = liveChannelsAdapter.oldIndex;
        liveChannelsAdapter.getChannelList().get(index).setIsClicked(true);
        liveChannelsAdapter.getChannelList().get(oldindex).setIsClicked(false);
        liveChannelsAdapter.notifyItemChanged(index);
        liveChannelsAdapter.notifyItemChanged(oldindex);*/
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
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getChannel_ctype().equals(ColumnChannel.ChannelCType.TWENTFOUR)) {
                                    list.remove(i);
                                }
                            }
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

        SubscriptionChannels(subcriptedList);
    }

    private void SubscriptionChannels(List<String> subcriptedCid) {
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
        liveChannelsAdapter.setChannelList(list);
        liveChannelsAdapter.notifyDataSetChanged();
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isClicked()) {
                index = i;
                break;
            }

        }
        liveChannelsAdapter.getChannelList().get(index).setIsClicked(true);
        liveChannelsAdapter.notifyItemChanged(index);
        liveChannelsAdapter.setNewIndex(index);
        channels_recyclerview.scrollToPosition(index);
        boolean isCleared = false;
        for (int i = 0; i < liveChannelsAdapter.getChannelList().size(); i++) {
            if (liveChannelsAdapter.getChannelList().get(i).getChannel_id().equals(channelIdForCurrentVideo)) {
                isCleared = true;
            }
        }

        if (!isCleared) {
            QEventBus.getEventBus().post(new StopVideoPlaying());
        }
        getFragment(liveChannelsAdapter.getChannelList().get(index).getChannel_id(), liveChannelsAdapter.getChannelList().get(index).getChannel_ctype());
        LiveSubscriptedChannelPreference.getInstance().saveStringValue(subscriptionkey, stringBuilder.toString());

    }

    public void onEvent(LiveEvent.LiveChannelClick event) {
        if (event == null) {
            return;
        }
        getFragment(event.channel_id, event.type);
    }

    public void onEvent(LiveEvent.SubscriptedChannel event) {
        if (event != null && event.selectChannels != null && event.selectChannels.size() > 0) {
            savaSubscriptinChannels(event.selectChannels);
        }
    }

    public void onEventMainThread(PlayVideoInMiniPlayer event) {
        if (miniPlayerView != null) {
            miniPlayerView.bindToVideoPlayer();
            if (drawableForCurrentVideo != null && drawableForCurrentVideo.get() != null) {
                miniPlayerView.setPlaceHolderDrawable(drawableForCurrentVideo.get());
            }
        }

        LiveChannelFragment2.isShownInMiniPlayer = true;

        if (miniPlayerContainer != null) {
            TranslateAnimation animation = new TranslateAnimation(miniPlayerContainer.getMeasuredWidth() + DensityUtils.dip2px(getContext(), 20), 0, 0, 0);
            animation.setDuration(300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    miniPlayerContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            miniPlayerContainer.startAnimation(animation);
        }
    }

    public void onEventMainThread(PlayVideoInList event) {
        if (miniPlayerContainer == null || miniPlayerContainer.getVisibility() != View.VISIBLE) {
            return;
        }

        if (VideoPlayer.getInstance().isPlaying()) {
            Bitmap snapShot = VideoPlayer.getInstance().capture();
            if (snapShot != null) {
                miniPlayerPlaceHolder.setImageDrawable(new BitmapDrawable(snapShot));
                miniPlayerPlaceHolder.setVisibility(View.VISIBLE);
            }
        } else if (drawableForCurrentVideo != null && drawableForCurrentVideo.get() != null) {
            miniPlayerPlaceHolder.setImageDrawable(drawableForCurrentVideo.get());
            miniPlayerPlaceHolder.setVisibility(View.VISIBLE);
        }

        LiveChannelFragment2.isShownInMiniPlayer = false;

        TranslateAnimation animation = new TranslateAnimation(0, miniPlayerContainer.getMeasuredWidth() + DensityUtils.dip2px(getContext(), 20), 0, 0);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                miniPlayerContainer.setVisibility(View.INVISIBLE);
                miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        miniPlayerContainer.startAnimation(animation);
    }

    public void onEventMainThread(CloseMiniPlayer event) {
        if (miniPlayerContainer == null) {
            return;
        }

        if (VideoPlayer.getInstance().isPlaying()) {
            Bitmap snapShot = VideoPlayer.getInstance().capture();
            if (snapShot != null) {
                miniPlayerPlaceHolder.setImageDrawable(new BitmapDrawable(snapShot));
                miniPlayerPlaceHolder.setVisibility(View.VISIBLE);
            }
        } else if (drawableForCurrentVideo != null && drawableForCurrentVideo.get() != null) {
            miniPlayerPlaceHolder.setImageDrawable(drawableForCurrentVideo.get());
            miniPlayerPlaceHolder.setVisibility(View.VISIBLE);
        }
        LiveChannelFragment2.isShownInMiniPlayer = false;

        VideoPlayer.getInstance().stop();
        if (miniPlayerView != null) {
            miniPlayerView.unBindToVideoPlayer();
        }

        TranslateAnimation animation = new TranslateAnimation(0, miniPlayerContainer.getMeasuredWidth() + DensityUtils.dip2px(getContext(), 20), 0, 0);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                miniPlayerContainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        miniPlayerContainer.startAnimation(animation);
    }

    public void onEventMainThread(StopVideoPlaying event) {
        VideoPlayer.getInstance().stop();

        if (miniPlayerView != null) {
            miniPlayerView.bindToVideoPlayer();
            miniPlayerView.unBindToVideoPlayer();
        }

        if (miniPlayerContainer != null) {
            miniPlayerContainer.setVisibility(View.INVISIBLE);
        }

        playingItemId = -1;
        isShownInMiniPlayer = false;
        channelIdForCurrentVideo = "";
    }

    @Override
    public void onDestroy() {
        QEventBus.getEventBus().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        QEventBus.getEventBus().post(new LiveChannelFragment2.StopVideoPlaying());
        super.onPause();
    }
}
