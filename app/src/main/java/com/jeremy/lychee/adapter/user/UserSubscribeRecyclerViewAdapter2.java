package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.WeMediaSubscribedChannelsActivity;
import com.jeremy.lychee.activity.user.WeMediaTopicDetailActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.fragment.user.UserSubscribeFragment2;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.widget.GlideTransforms.GlideRoundRectTransform;
import com.jeremy.lychee.activity.user.WeMediaHotTopicsActivity;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideBorderCircleTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class UserSubscribeRecyclerViewAdapter2 extends BaseRecyclerViewAdapter {
    private static final String TAG = UserSubscribeRecyclerViewAdapter2.class.getSimpleName();

    private static final int REQUEST_SUB_CHANNELS_SIZE = 5;
    private static final int REQUEST_SUB_TOPICS_SIZE = 5;
    private static final int REQUEST_HOT_TOPICS_SIZE = 10;
    private static final int REQUEST_HOT_TOPICS_START = 1;
    private static final int REQUEST_HOT_TOPICS_MAX = 20;

    private Context context;
    private List<WeMediaTopic> mSubscribedTopic = new ArrayList<>();
    private List<WeMediaTopic> mHotTopic = new ArrayList<>();
    private List<WeMediaChannel> mSubscribedChannels = new ArrayList<>();
    private UserSubscribeFragment2 mFragment;

    private int mHotTopicStart = REQUEST_HOT_TOPICS_START;
    private boolean mHasMoreHotTopics = true;
    private boolean mHasMoreSubTopics = false;
    private volatile boolean mSubChannelUpdated = false;
    private volatile boolean mSubTopicsUpdated = false;
    private volatile int mRemovedCnt = 0;
    private WeMediaTopicAvatarListAdapter2 mSubTopicAdapter;


    private class ITEM_TYPES {
        static final int SECTIONS = 0;
        static final int SUB_TOPICS = 1;
        static final int HOT_TOPICS = 2;
        static final int CHANNELS = 3;
    }

    public UserSubscribeRecyclerViewAdapter2(Context context, UserSubscribeFragment2 fragment) {
        super(context);
        this.context = context;
        mFragment = fragment;
        setCustomFooterView(com.jeremy.lychee.R.layout.wemedia_hot_topic_listview_foot_item);
        setOnCustomFooterClickLinstener(() -> WeMediaHotTopicsActivity.startActivity(mContext));
    }

    @Override
    public boolean isBackwardLoadEnable() {
        return mHasMoreHotTopics;
    }

    @Override
    public void load(boolean isLoadMore) {

        if (!AppUtil.isNetAvailable(mContext)) {
            setDataState((DataState.ERROR));
            mFragment.showSnackBar();
            return;
        }

        if (!isLoadMore) {
            setRecyclerViewEnabled(false);
            mHotTopicStart = REQUEST_HOT_TOPICS_START;
            mRemovedCnt = 0;
            swipeRefreshLayout.setRefreshing(true);
            requestAndShowSubChannels();
        } else {
            requestAndShowHotTopics(true);
        }
    }

    @Deprecated
    private void feedLike(String transmitId, Action0 cb) {
        String sign = AppUtil.transmitDeleteSignString(transmitId);
        OldRetroAdapter.getService().feedLike(transmitId, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getErrno() == 0) {
                        cb.call();
                    } else if (s.getErrno() == -1) {//Operation repeat
                        ToastHelper.getInstance(context).toast(com.jeremy.lychee.R.string.comment_digged);
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ITEM_TYPES.SECTIONS:
                return new SectionViewHolder(
                        LayoutInflater.from(context).inflate(com.jeremy.lychee.R.layout.wemedia_list_section_item, parent, false));
            case ITEM_TYPES.CHANNELS: {
                return new SubChannelAvatarViewHolder(
                        LayoutInflater.from(context).inflate(com.jeremy.lychee.R.layout.wemedia_sub_channel_avatar, parent, false));
            }
            case ITEM_TYPES.SUB_TOPICS:
                return new SubscribedTopiclListViewHolder(
                        LayoutInflater.from(context).inflate(com.jeremy.lychee.R.layout.wemedia_sub_topic_avatar_list, parent, false));
            case ITEM_TYPES.HOT_TOPICS:
                return new TopicItemViewHolder(
                        LayoutInflater.from(context).inflate(com.jeremy.lychee.R.layout.wemedia_topic_avatar_list_item2, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (type != VIEW_TYPES.FOOTER) {
            if (position == 0) {
                return ITEM_TYPES.SECTIONS;
            } else if (position == 2 && (mSubscribedTopic.size() > 0 || mSubscribedChannels.size() > 0)) {
                return ITEM_TYPES.SECTIONS;
            } else if (position == 4 && mSubscribedTopic.size() > 0 && mSubscribedChannels.size() > 0) {
                return ITEM_TYPES.SECTIONS;
            } else if (position == 1 && mSubscribedTopic.size() > 0) {
                return ITEM_TYPES.CHANNELS;
            } else if (position == 1 && mSubscribedTopic.size() == 0 && mSubscribedChannels.size() > 0) {
                return ITEM_TYPES.SUB_TOPICS;
            } else if (position == 3 && mSubscribedTopic.size() > 0 && mSubscribedChannels.size() > 0) {
                return ITEM_TYPES.SUB_TOPICS;
            } else {
                return ITEM_TYPES.HOT_TOPICS;
            }

        } else {
            return type;
        }
    }


    private int getPositionByTopicItem(WeMediaTopic item) {
        int position = mHotTopic.indexOf(item);
        if (position == -1) {
            return -1;
        }
        if (mSubscribedChannels.size() > 0 && mSubscribedTopic.size() > 0) {
            return mHotTopic.indexOf(item) + 5;
        } else if (mSubscribedChannels.size() > 0 || mSubscribedTopic.size() > 0) {
            return mHotTopic.indexOf(item) + 3;
        } else {
            return mHotTopic.indexOf(item) + 1;
        }
    }

    @Override
    public int getItemCount() {
        if (mSubscribedTopic.size() + mSubscribedChannels.size() + mHotTopic.size() == 0) {
            return 0;
        }
        if (hasFooterView) {
            if (mSubscribedTopic.size() == 0 && mSubscribedChannels.size() == 0) {
                return mHotTopic.size() + 2;
            } else if (mSubscribedChannels.size() == 0 || mSubscribedTopic.size() == 0) {
                if (mHotTopic.size() > 0) {
                    return mHotTopic.size() + 4;
                } else {
                    return 2;
                }
            }
            return mHotTopic.size() == 0 ? 5 : mHotTopic.size() + 6;
        } else {
            if (mSubscribedTopic.size() == 0 && mSubscribedChannels.size() == 0) {
                return mHotTopic.size() + 1;
            } else if (mSubscribedChannels.size() == 0 || mSubscribedTopic.size() == 0) {
                if (mHotTopic.size() > 0) {
                    return mHotTopic.size() + 3;
                } else {
                    return 1;
                }
            }
            return mHotTopic.size() == 0 ? 4 : mHotTopic.size() + 5;
        }
    }

    @Deprecated
    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    @Deprecated
    private void login() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {

        int newPosition;

        switch (getItemViewType(position)) {
            case ITEM_TYPES.SECTIONS:
                switch (getItemViewType(position)) {
                    case ITEM_TYPES.SECTIONS:
                        SectionViewHolder h = (SectionViewHolder) holder;
                        if (position == 0) {
                            if (mSubscribedTopic.size() > 0) {
                                showSectionView(h, 0);
                            } else if (mSubscribedChannels.size() > 0) {
                                showSectionView(h, 1);
                            }
                        } else if (position == 2) {
                            if (mSubscribedTopic.size() > 0 && mSubscribedChannels.size() > 0) {
                                showSectionView(h, 1);
                            } else {
                                showSectionView(h, 2);
                            }
                        } else if (mHotTopic.size() > 0) {
                            showSectionView(h, 2);
                        }
                        break;
                }
                break;
            case ITEM_TYPES.SUB_TOPICS:
                if (mSubTopicAdapter == null) {
                    mSubTopicAdapter = new WeMediaTopicAvatarListAdapter2(mContext);
                    mSubTopicAdapter.setData(mSubscribedTopic);
                    (((SubscribedTopiclListViewHolder) holder).topicAvatarList).setAdapter(mSubTopicAdapter);
                }
                if (mSubTopicsUpdated) {
                    mSubTopicsUpdated = false;
                    Observable.timer(100, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(e -> {
//                                requestAndShowSubTopics(false);
                                mSubTopicAdapter.notifyDataSetChanged();
                            }, Throwable::printStackTrace);
                }
                break;
            case ITEM_TYPES.CHANNELS:
                if (!mSubChannelUpdated) return;
                mSubChannelUpdated = false;
                SubChannelAvatarViewHolder viewHolder = (SubChannelAvatarViewHolder) holder;
                //channel avatars
                int margin = AppUtil.dip2px(context, 20);
                for (int i = 0; i < mSubscribedChannels.size(); i++) {
                    WeMediaChannel channel = mSubscribedChannels.get(i);
                    GlideImageView iv = new GlideImageView(context);
                    int size = AppUtil.dip2px(context, 33);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
                    lp.setMargins(margin * i, 0, 0, 0);
                    viewHolder.channelAvatars.addView(iv, lp);
                    iv.loadImage(channel.getIcon(), (req, v) -> req
                            .placeholder(AppUtil.getDefaultCircleIcon(context))
                            .crossFade()
                            .bitmapTransform(new GlideBorderCircleTransform(context, 0xffffffff, AppUtil.dip2px(context, 4)))
                            .into(v));
                }
                if (Session.getSession() != null) {
                    //sub count
                    OldRetroAdapter.getService()
                            .getUserChannel(Session.getSession().getReal_uid())
                            .filter(s -> s.getErrno() == 0)
                            .map(ModelBase::getData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                viewHolder.subChannelCount.setText(s.getFollow());
                            }, Throwable::printStackTrace);
                    //click event
                    viewHolder.clickable.setOnClickListener(
                            v -> WeMediaSubscribedChannelsActivity.startActivity(
                                    context, Session.getSession().getReal_uid()));
                }
                break;
            case ITEM_TYPES.HOT_TOPICS:
                if (mSubscribedChannels.size() > 0 && mSubscribedTopic.size() > 0) {
                    newPosition = position - 5;
                } else if (mSubscribedChannels.size() > 0 || mSubscribedTopic.size() > 0) {
                    newPosition = position - 3;
                } else {
                    newPosition = position - 1;
                }
                WeMediaTopic topic = mHotTopic.get(newPosition);
                TopicItemViewHolder viewHolder1 = (TopicItemViewHolder) holder;

                viewHolder1.topicName.setText(topic.getTitle());
                viewHolder1.topicIcon.loadImage(topic.getImage(), (req, v) -> req
                        .placeholder(AppUtil.getDefaultSquareIcon(context))
                        .crossFade()
                        .bitmapTransform(new GlideRoundRectTransform(context))
                        .into(v));
                viewHolder1.subCount.setText("" + topic.getWeight());//TODO
                viewHolder1.clickable.setOnClickListener(v ->
                        WeMediaTopicDetailActivity.startActivity(context, topic.getId(), topic.getTitle()));
                viewHolder1.subscribeBtn.setOnClickListener(v ->
                        subscribeWeMediaTopic(
                                topic.getId(), true,
                                () -> {
                                    int newPos = getPositionByTopicItem(topic);
                                    mHotTopic.remove(topic);
                                    if (newPos != -1) {
                                        notifyItemRemoved(newPos);
                                        if (mSubTopicAdapter != null) {
                                            mSubTopicsUpdated = true;
                                            mSubTopicAdapter.addData(0, topic);
                                            mSubTopicAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Logger.t(TAG).e("getPositionByTopicItem(topic) = -1");
                                    }

                                }));

                break;
        }

    }

    private void showSectionView(SectionViewHolder h, int type) {
        switch (type) {
            case 0://sub channels
                h.sectionTitle.setText(com.jeremy.lychee.R.string.user_sub_channel_section_name);
                h.sectionRightBtn.setVisibility(View.GONE);
                h.sectionRightIcon.setVisibility(View.GONE);
                break;
            case 1://sub topics
                h.sectionTitle.setText(com.jeremy.lychee.R.string.user_sub_topic_section_name);
                h.sectionRightBtn.setVisibility(View.GONE);
                h.sectionRightIcon.setVisibility(View.GONE);
                break;
            case 2://hot topics
                h.sectionTitle.setText(com.jeremy.lychee.R.string.user_sub_hot_topic_section_name);
                h.sectionRightBtn.setVisibility(View.GONE);
                h.sectionRightIcon.setVisibility(View.GONE);
                break;
            default://none
                h.sectionTitle.setVisibility(View.GONE);
                h.sectionRightBtn.setVisibility(View.GONE);
                h.sectionRightIcon.setVisibility(View.GONE);
                break;
        }
    }

    private void subscribeWeMediaTopic(int cId, boolean toSub, Action0 cb) {
        if (toSub) {
            OldRetroAdapter.getService().subscribeColumn(cId, 2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getErrno() != 0) {
                            ToastHelper.getInstance(context).toast(s.getErrmsg());
                            Logger.t(TAG).e("订阅失败");
                        } else {
                            cb.call();
                        }
                    }, Throwable::printStackTrace);
        } else {
            OldRetroAdapter.getService().unSubscribeColumn(cId, 2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getErrno() != 0) {
                            ToastHelper.getInstance(context).toast(s.getErrmsg());
                            Logger.t(TAG).e("取消订阅失败");
                        } else {
                            cb.call();
                        }
                    }, Throwable::printStackTrace);
        }
    }

    /***************************
     * Data Request
     *************************************/

    private void requestAndShowSubChannels() {
        getSubMediaChannelList(1, REQUEST_SUB_CHANNELS_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            mSubscribedChannels.clear();
                            mSubscribedChannels.addAll(s);
                            mSubChannelUpdated = true;
                            notifyDataSetChanged();
                            requestAndShowSubTopics();
                        },
                        e -> {
                            e.printStackTrace();
                            setDataState((DataState.ERROR));
                            mFragment.showSnackBar();
                        });
    }


    private void requestAndShowSubTopics() {
        getMyTopicList(1, REQUEST_SUB_TOPICS_SIZE + 1)
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    if (s.size() == REQUEST_SUB_TOPICS_SIZE + 1) {
                        mHasMoreSubTopics = true;
                        return s.subList(0, REQUEST_SUB_TOPICS_SIZE);
                    } else {
                        mHasMoreSubTopics = false;
                        return s;
                    }
                })
                .subscribe(
                        s -> {
                            if (mSubscribedTopic != null) {
                                mSubscribedTopic.clear();
                                mSubscribedTopic.addAll(s);
                                if (mHasMoreSubTopics) {//tag for show more btn
                                    mSubscribedTopic.add(new WeMediaTopic());
                                }
                            }
                            if (mSubTopicAdapter != null) {
                                mSubTopicAdapter.notifyDataSetChanged();
                            }
                            notifyDataSetChanged();
                            requestAndShowHotTopics(false);
                        },
                        e -> {
                            e.printStackTrace();
                            setDataState((DataState.ERROR));
                            mFragment.showSnackBar();
                        });
    }

    private Observable<List<WeMediaTopic>> getMyTopicList(int start, int size) {
        return OldRetroAdapter.getService().getUserTopicList(start, size, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(respones -> Observable.just(respones.getData()));
    }


    private void requestAndShowHotTopics(boolean isLoadMore) {
        Observable.just(mHotTopicStart)
                .flatMap(this::getHotTopicList)
                .doOnNext(it -> {
                    if (!isLoadMore) {
                        mHotTopic.clear();
                    }
                    mHasMoreHotTopics = false;
                    if (it.size() > 0) {
                        mHasMoreHotTopics = it.size() == REQUEST_HOT_TOPICS_SIZE;
                        mHotTopicStart++;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(it -> {
                    swipeRefreshLayout.setRefreshing(false);
                    if (!mHasMoreHotTopics) {
                        if (getItemCount() == 0) {
                            setDataState(DataState.EMPTY);
                        }
                        notifyDataSetChanged();//for foot_loading reset
                    }
                })
                .flatMap(Observable::from)
                .subscribe(it -> {
                    if (mHotTopic.size() < REQUEST_HOT_TOPICS_MAX) {
                        mHotTopic.add(it);
                        if (mHotTopic.size() < 8) {//no need anim
                            notifyDataSetChanged();
                        } else {
                            notifyItemRangeInserted(getItemCount() - 2, 1);
                        }
                    } else if (mHotTopic.size() == REQUEST_HOT_TOPICS_MAX) {
                        mHasMoreHotTopics = false;
                        notifyDataSetChanged();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    setDataState(DataState.ERROR);
                    mFragment.showSnackBar();
                    setRecyclerViewEnabled(true);
                }, () -> setRecyclerViewEnabled(true));

    }

    private Observable<List<WeMediaTopic>> getHotTopicList(int start) {
        return OldRetroAdapter.getService().getHotTopicList(start, REQUEST_HOT_TOPICS_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(respones -> Observable.just(respones.getData()));
    }

    private Observable<List<WeMediaChannel>> getSubMediaChannelList(int start, int size) {
        return OldRetroAdapter.getService().getUserSubMediaChannelList(start, size, Session.getSession().getReal_uid())
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /***************************
     * ViewHolder
     *************************************/

    static class TopicItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.topic_icon)
        GlideImageView topicIcon;
        @Bind(com.jeremy.lychee.R.id.topic_name)
        TextView topicName;
        @Bind(com.jeremy.lychee.R.id.sub_count)
        TextView subCount;
        @Bind(com.jeremy.lychee.R.id.subscribe_btn)
        ImageView subscribeBtn;
        @Bind(com.jeremy.lychee.R.id.arrow)
        View arrow;
        @Bind(com.jeremy.lychee.R.id.clickable)
        View clickable;

        public TopicItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            arrow.setVisibility(View.GONE);
        }
    }


    static class SubscribedTopiclListViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.topic_avatar_list)
        ListView topicAvatarList;

        SubscribedTopiclListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class SubChannelAvatarViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.sub_channel_count)
        TextView subChannelCount;
        @Bind(com.jeremy.lychee.R.id.clickable)
        View clickable;
        @Bind(com.jeremy.lychee.R.id.channel_avatars)
        FrameLayout channelAvatars;

        SubChannelAvatarViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.section_title)
        TextView sectionTitle;
        @Bind(com.jeremy.lychee.R.id.section_right_btn)
        TextView sectionRightBtn;
        @Bind(com.jeremy.lychee.R.id.section_right_ic)
        ImageView sectionRightIcon;

        SectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
