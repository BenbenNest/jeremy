//package com.qihoo.lianxian.adapter.user;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import R;
//import OldNewsDetailActivity;
//import LoginActivity;
//import WeMediaAlbumDetailActivity;
//import WeMediaChannelActivity;
//import BaseRecyclerViewAdapter;
//import WeMediaTopic;
//import QEventBus;
//import Events;
//import com.qihoo.lianxian.fragment.user.UserSubscribeFragment;
//import Session;
//import FeedNewsEntity;
//import RetroAdapter;
//import AppUtil;
//import StringUtil;
//import ToastHelper;
//import GlideImageView;
//import WrapContentLinearLayoutManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import rx.Observable;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action0;
//import rx.schedulers.Schedulers;
//
//public class UserSubscribeRecyclerViewAdapter extends BaseRecyclerViewAdapter {
//    private static final String TAG = UserSubscribeRecyclerViewAdapter.class.getSimpleName();
//
//    public static final int REQUEST_CODE_FOR_NEW_DETAIL = 0;
//    private static final int REQUEST_SUB_TOPIC_SIZE = 20;
//    private static final int REQUEST_ARTICLES_SIZE = 12;
//    private static final int REQUEST_ARTICLES_START = 0;
//
//    private Context context;
//    private List<WeMediaTopic> mSubscribedTopic = new ArrayList<>();
//    private List<FeedNewsEntity> mArticles = new ArrayList<>();
//    private UserSubscribeFragment mFragment;
//
//    private int mSubArticleStart = REQUEST_ARTICLES_START;
//    private boolean mHasMoreArticles = true;
//    private boolean mHasMoreTopics = false;
//    private WeMediaTopicAvatarListAdapter mSubTopicAdapter;
//
//    private class ITEM_TYPES {
//        static final int SECTIONS = 0;
//        static final int TOPICS = 1;
//        static final int ARTICLES = 2;
//    }
//
//    public UserSubscribeRecyclerViewAdapter(Context context, UserSubscribeFragment fragment) {
//        super(context);
//        this.context = context;
//        mFragment = fragment;
//    }
//
//    @Override
//    public boolean isBackwardLoadEnable() {
//        return mHasMoreArticles;
//    }
//
//    @Override
//    public void load(boolean isLoadMore) {
//
//        if (!AppUtil.isNetAvailable(mContext)) {
//            setDataState((DataState.ERROR));
//            mFragment.showSnackBar();
//            return;
//        }
//
//        if (!isLoadMore) {
//            setRecyclerViewEnabled(false);
//            mSubArticleStart = REQUEST_ARTICLES_START;
//            swipeRefreshLayout.setRefreshing(true);
//            requestAndShowSubTopics();
//        } else {
//            requestAndShowSubArticles(true);
//        }
//    }
//
//    @Deprecated
//    private void feedLike(String transmitId, Action0 cb) {
//        String sign = AppUtil.transmitDeleteSignString(transmitId);
//        RetroAdapter.getService().feedLike(transmitId, sign)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> {
//                    if (s.getErrno() == 0) {
//                        cb.call();
//                    } else if (s.getErrno() == -1) {//Operation repeat
//                        ToastHelper.getInstance(context).toast(R.string.comment_digged);
//                    }
//                }, Throwable::printStackTrace);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
//
//        switch (viewType) {
//            case ITEM_TYPES.SECTIONS:
//                return new SectionViewHolder(
//                        LayoutInflater.from(context).inflate(R.layout.wemedia_list_section_item, parent, false));
//            case ITEM_TYPES.TOPICS:
//                SubscribedTopiclListViewHolder holder =
//                        new SubscribedTopiclListViewHolder(((Activity) context).getLayoutInflater().inflate(R.layout.wemedia_topic_avatar_list, parent, false));
//                holder.recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(),
//                        LinearLayoutManager.HORIZONTAL, false));
//                return holder;
//            case ITEM_TYPES.ARTICLES:
//                View news = LayoutInflater.from(context).inflate(R.layout.user_subscribe_articles_item, parent, false);
//                return new ArticleViewHolder(news);
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        int type = super.getItemViewType(position);
//        if (type != VIEW_TYPES.FOOTER) {
//            if (position == 0 ||
//                    (mSubscribedTopic.size() > 0 && position == 2)) {
//                return ITEM_TYPES.SECTIONS;
//            } else if (mSubscribedTopic.size() > 0 && position == 1) {
//                return ITEM_TYPES.TOPICS;
//            } else {
//                return ITEM_TYPES.ARTICLES;
//            }
//        } else {
//            return type;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//
//        if (mSubscribedTopic.size() + mArticles.size() == 0) {
//            return 0;
//        }
//        if (hasFooterView) {
//            if (mSubscribedTopic.size() == 0 && mArticles.size() != 0) {
//                return mArticles.size() + 2;
//            } else if (mSubscribedTopic.size() != 0 && mArticles.size() == 0) {
//                return 3;
//            }
//            return mArticles.size() + 4;
//        } else {
//            if (mSubscribedTopic.size() == 0 && mArticles.size() != 0) {
//                return mArticles.size() + 1;
//            } else if (mSubscribedTopic.size() != 0 && mArticles.size() == 0) {
//                return 2;
//            }
//            return mArticles.size() + 3;
//        }
//    }
//
//    private boolean isLogin() {
//        return !Session.isUserInfoEmpty();
//    }
//
//    private void login() {
//        context.startActivity(new Intent(context, LoginActivity.class));
//    }
//
//    @Override
//    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        int newPosition;
//
//        switch (getItemViewType(position)) {
//            case ITEM_TYPES.SECTIONS:
//                SectionViewHolder h = (SectionViewHolder) holder;
//                if (position == 0) {
//                    if (mSubscribedTopic.size() > 0) {
//                        h.sectionTitle.setText(R.string.user_sub_topic_section_name);
//                        h.splitLine.setVisibility(View.GONE);
//                    } else {
//                        h.sectionTitle.setText(R.string.user_sub_news_title);
//                        h.splitLine.setVisibility(View.VISIBLE);
//                    }
//                    h.sectionRightBtn.setOnClickListener(v ->
//                            QEventBus.getEventBus().post(new Events.ShowSubTopics()));
//                    h.sectionRightIcon.setOnClickListener(v ->
//                            QEventBus.getEventBus().post(new Events.ShowSubTopics()));
//                    if (mHasMoreTopics) {
//                        h.sectionRightBtn.setVisibility(View.VISIBLE);
//                        h.sectionRightIcon.setVisibility(View.VISIBLE);
//                    } else {
//                        h.sectionRightBtn.setVisibility(View.GONE);
//                        h.sectionRightIcon.setVisibility(View.GONE);
//                    }
//                } else {
//                    h.sectionTitle.setText(R.string.user_sub_news_title);
//                    h.sectionRightBtn.setVisibility(View.GONE);
//                    h.sectionRightIcon.setVisibility(View.GONE);
//                    h.splitLine.setVisibility(View.VISIBLE);
//                }
//                break;
//            case ITEM_TYPES.TOPICS:
//                if (mSubTopicAdapter == null) {
//                    mSubTopicAdapter = new WeMediaTopicAvatarListAdapter(mContext);
//                    mSubTopicAdapter.setDatas(mSubscribedTopic);
//                    (((SubscribedTopiclListViewHolder) holder).recyclerView).setAdapter(mSubTopicAdapter);
//                }
//                break;
//            case ITEM_TYPES.ARTICLES:
//                if (mSubscribedTopic.size() > 0) {
//                    newPosition = position - 3;
//                } else {
//                    newPosition = position - 1;
//                }
//                FeedNewsEntity article = mArticles.get(newPosition);
//                if (article.getTransmit() == null || article.getTransmit().getNews() == null) {
//                    return;
//                }
//                ArticleViewHolder viewHolder = (ArticleViewHolder) holder;
//
//                viewHolder.articlePanel.setOnClickListener(v ->
//                        OldNewsDetailActivity.startActivity(
//                                context, article.getTransmit().getNews(), REQUEST_CODE_FOR_NEW_DETAIL));
//                viewHolder.articleTitle.setText(article.getTransmit().getNews().getTitle());
//
//                //新闻内容&评论
//                String summary, source;
//                if (article.getTransmit().getType() == 4) {
//                    //compose-news
//                    viewHolder.forwardPanel.setVisibility(View.GONE);
//                    source = article.getName();
//                } else {
//                    //normal-news
//                    viewHolder.forwardPanel.setVisibility(View.VISIBLE);
//                    source = article.getTransmit().getNews().getSource();
//                    viewHolder.wemediaChannelTitle.setText(article.getName());
//                    viewHolder.wemediaChannelTitle.setOnClickListener(v ->
//                            WeMediaChannelActivity.startActivity(mContext, article.getC_id()));
//                    viewHolder.albumName.setText(article.getSub_name());
//                    viewHolder.albumName.setOnClickListener(
//                            v -> WeMediaAlbumDetailActivity.startActivity(
//                                    context, article.getSub_id(), article.getSub_name(), false));
//                    if (TextUtils.isEmpty(article.getTransmit().getContent())) {
//                        viewHolder.forwardComment.setVisibility(View.GONE);
//                    } else {
//                        viewHolder.forwardComment.setVisibility(View.VISIBLE);
//                        viewHolder.forwardComment.setText(article.getTransmit().getContent());
//                    }
//                }
//                summary = article.getTransmit().getNews().getSummary();
//                if (TextUtils.isEmpty(summary)) {
//                    viewHolder.articleSummary.setVisibility(View.GONE);
//                } else {
//                    viewHolder.articleSummary.setVisibility(View.VISIBLE);
//                    viewHolder.articleSummary.setText(summary);
//                }
//                viewHolder.articleSource.setText(source);
//
//                //评论数&发布时间
//                String num = article.getTransmit().getNews().getComment();
//                String nFormat = context.getResources().getString(R.string.user_home_comment_num);
//                viewHolder.articleCommentCnt.setText(
//                        String.format(nFormat, StringUtil.isEmpty(num) ? "0" : num));
//                viewHolder.articlePublishTime.setText(
//                        AppUtil.formatTime(article.getTransmit().getCreatetime() * 1000L));
//
//                //链接图片
//                if (TextUtils.isEmpty(article.getTransmit().getNews().getAlbum_pic())) {
//                    viewHolder.articleImgVisible.setVisibility(View.GONE);
//                } else {
//                    viewHolder.articleImgVisible.setVisibility(View.VISIBLE);
//                    viewHolder.articleImg.loadImage(article.getTransmit().getNews().getAlbum_pic());
//                    switch (article.getTransmit().getType()) {
//                        case 1://internal-news
//                        case 4://compose-news
//                        case 3://external-news
//                            viewHolder.playTime.setVisibility(View.GONE);
//                            break;
//                        case 2://live-video
//                            viewHolder.playTime.setVisibility(View.VISIBLE);
//                            viewHolder.playTime.setText(article.getTransmit().getNews().getVideo_length());
//                            break;
//                    }
//                }
//
//                break;
//        }
//
//    }
//
//    /***************************
//     * Data Request
//     *************************************/
//
//    private void requestAndShowSubTopics() {
//        getMyTopicList(1, REQUEST_SUB_TOPIC_SIZE + 1)
//                .subscribeOn(Schedulers.io())
//                .map(s -> {
//                    if (s.size() == REQUEST_SUB_TOPIC_SIZE + 1) {
//                        mHasMoreTopics = true;
//                        return s.subList(0, REQUEST_SUB_TOPIC_SIZE);
//                    } else {
//                        mHasMoreTopics = false;
//                        return s;
//                    }
//                })
//                .subscribe(
//                        s -> {
//                            if (mSubscribedTopic != null) {
//                                mSubscribedTopic.clear();
//                                mSubscribedTopic.addAll(s);
//                            }
//                            if (mSubTopicAdapter != null) {
//                                mSubTopicAdapter.notifyDataSetChanged();
//                            }
//                            notifyDataSetChanged();
//                            requestAndShowSubArticles(false);
//                        },
//                        e -> {
//                            e.printStackTrace();
//                            setDataState((DataState.ERROR));
//                            mFragment.showSnackBar();
//                        });
//    }
//
//    private Observable<List<WeMediaTopic>> getMyTopicList(int start, int size) {
//        return RetroAdapter.getService().getUserTopicList(start, size, null)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(respones -> Observable.just(respones.getData()));
//    }
//
//    private void requestAndShowSubArticles(boolean isLoadMore) {
//        Observable.just(mSubArticleStart)
//                .flatMap(this::getHomeArticles)
//                .doOnNext(it -> {
//                    if (!isLoadMore) {
//                        mArticles.clear();
//                    }
//                    mHasMoreArticles = false;
//                    if (it.size() > 0) {
//                        mHasMoreArticles = it.size() == REQUEST_ARTICLES_SIZE;
//                        mSubArticleStart = Integer.parseInt(it.get(it.size() - 1).getTransmit().getTransmitid());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(it -> {
//                    swipeRefreshLayout.setRefreshing(false);
//                    if (!mHasMoreArticles) {
//                        if (getItemCount() == 0) {
//                            setDataState(DataState.EMPTY);
//                        }
//                        notifyDataSetChanged();//for foot_loading reset
//                    }
//                })
//                .flatMap(Observable::from)
//                .subscribe(it -> {
//                    mArticles.add(it);
//                    if (mArticles.size() < 4) {//no need anim
//                        notifyDataSetChanged();
//                    } else {
//                        notifyItemRangeInserted(getItemCount() - 2, 1);
//                    }
//                }, throwable -> {
//                    throwable.printStackTrace();
//                    setDataState(DataState.ERROR);
//                    mFragment.showSnackBar();
//                    setRecyclerViewEnabled(true);
//                }, () -> setRecyclerViewEnabled(true));
//
//    }
//
//    private Observable<List<FeedNewsEntity>> getHomeArticles(int start) {
//        return RetroAdapter.getService().getUserHomeArticle(start, REQUEST_ARTICLES_SIZE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(it -> Observable.just(it.getData()));
//    }
//
//
//    /***************************
//     * ViewHolder
//     *************************************/
//
//    static class ArticleViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.wemedia_channel_title)
//        TextView wemediaChannelTitle;
//        @Bind(R.id.article_publish_time)
//        TextView articlePublishTime;
//        @Bind(R.id.forward_comment)
//        TextView forwardComment;
//        @Bind(R.id.article_img)
//        GlideImageView articleImg;
//        @Bind(R.id.article_title)
//        TextView articleTitle;
//        @Bind(R.id.album_name)
//        TextView albumName;
//        @Bind(R.id.article_panel)
//        LinearLayout articlePanel;
//        @Bind(R.id.article_comment_cnt)
//        TextView articleCommentCnt;
//        @Bind(R.id.article_img_visible)
//        View articleImgVisible;
//        @Bind(R.id.play_time)
//        TextView playTime;
//        @Bind(R.id.article_summary)
//        TextView articleSummary;
//        @Bind(R.id.article_source)
//        TextView articleSource;
//        @Bind(R.id.forward_panel)
//        View forwardPanel;
//
//        ArticleViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    static class SubscribedTopiclListViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.topic_avatar_list)
//        RecyclerView recyclerView;
//
//        SubscribedTopiclListViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    static class SectionViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.section_title)
//        TextView sectionTitle;
//        @Bind(R.id.section_right_btn)
//        TextView sectionRightBtn;
//        @Bind(R.id.section_right_ic)
//        ImageView sectionRightIcon;
//        @Bind(R.id.split_line)
//        View splitLine;
//
//        SectionViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//}
