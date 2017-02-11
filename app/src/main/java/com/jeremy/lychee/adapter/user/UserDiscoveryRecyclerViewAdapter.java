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
//import android.widget.AdapterView;
//import android.widget.Gallery;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import R;
//import OldNewsDetailActivity;
//import LoginActivity;
//import BaseRecyclerViewAdapter;
//import NewsListData;
//import WeMediaTopic;
//import QEventBus;
//import Events;
//import com.qihoo.lianxian.fragment.user.UserDiscoveryFragment;
//import Session;
//import HotWeMediaChannel;
//import OldRetroAdapter;
//import AppUtil;
//import DateUtils;
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
///**
// * @author leikang on 15-11-23 13:55
// * @mail leikang@360.cn
// * 首页发现
// */
//public class UserDiscoveryRecyclerViewAdapter extends BaseRecyclerViewAdapter {
//    private static final String TAG = UserDiscoveryRecyclerViewAdapter.class.getSimpleName();
//
//    public static final int REQUEST_CODE_FOR_NEW_DETAIL = 0;
//    private static final int REQUEST_HOT_CHANNELS_SIZE = 20;
//    private static final int REQUEST_HOT_TOPICS_SIZE = 20;
//    private static final int REQUEST_SUBJECTS_SIZE = 10;
//    private static final int REQUEST_ARTICLES_SIZE = 12;
//    private static final int REQUEST_HOT_TOPICS_START = 1;
//    private static final int REQUEST_HOT_ARTICLES_START = 1;
//
//    private List<HotWeMediaChannel> mHotChannels = new ArrayList<>();
//    private List<WeMediaTopic> mHotTopics = new ArrayList<>();
//    private List<NewsListData> mArticles = new ArrayList<>();
//    private List<WeMediaTopic> mSubjects = new ArrayList<>();
//    private UserDiscoveryFragment mFragment;
//
//    private int mHotTopicStart = REQUEST_HOT_TOPICS_START;
//    private int mHotArticleStart = REQUEST_HOT_ARTICLES_START;
//    private boolean mHasMoreArticles = true;
//    private boolean mHasMoreChannels = false;
//    private WeMediaHotChannelAvatarListAdapter mHotChannelAdapter;
//    private WeMediaTopicAvatarListAdapter mHotTopicAdapter;
//    private WeMediaSubjectListAdapter mSubjectAdapter;
//
//    private class ITEM_TYPES {
//        static final int SECTIONS = 0;
//        static final int TOPICS = 1;
//        static final int CHANNELS = 2;
//        static final int ARTICLES = 3;
//        static final int SUBJECTS = 4;
//    }
//
//    public UserDiscoveryRecyclerViewAdapter(Context context, UserDiscoveryFragment fragment) {
//        super(context);
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
//        if (!isLoadMore) {
//            setRecyclerViewEnabled(false);
//            mHotTopicStart = REQUEST_HOT_TOPICS_START;
//            mHotArticleStart = REQUEST_HOT_ARTICLES_START;
//            swipeRefreshLayout.setRefreshing(true);
//            requestAndShowSubjects();
//        } else {
//            requestAndShowHotArticles(true);
//        }
//    }
//
//    @Deprecated
//    private void feedLike(String transmitId, Action0 cb) {
//        String sign = AppUtil.transmitDeleteSignString(transmitId);
//        OldRetroAdapter.getService().feedLike(transmitId, sign)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> {
//                    if (s.getErrno() == 0) {
//                        cb.call();
//                    } else if (s.getErrno() == -1) {//Operation repeagit git citll
//
//                        ToastHelper.getInstance(mContext).toast(R.string.comment_digged);
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
//                        LayoutInflater.from(mContext)
//                                .inflate(R.layout.wemedia_list_section_item, parent, false));
//            case ITEM_TYPES.TOPICS: {
//                HotTopicListViewHolder holder =
//                        new HotTopicListViewHolder(((Activity) mContext).getLayoutInflater()
//                                .inflate(R.layout.wemedia_topic_avatar_list, parent, false));
//                holder.recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(),
//                        LinearLayoutManager.HORIZONTAL, false));
//                return holder;
//            }
//            case ITEM_TYPES.CHANNELS:
//                HotChannelListViewHolder holder =
//                        new HotChannelListViewHolder(((Activity) mContext).getLayoutInflater()
//                                .inflate(R.layout.wemedia_hot_channel_list, parent, false));
//                holder.recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(),
//                        LinearLayoutManager.HORIZONTAL, false));
//                return holder;
//            case ITEM_TYPES.ARTICLES:
//                View news = LayoutInflater.from(mContext)
//                        .inflate(R.layout.user_discovery_articles_item, parent, false);
//                return new ArticleViewHolder(news);
//            case ITEM_TYPES.SUBJECTS:
//                return new SubjectListViewHolder(
//                        LayoutInflater.from(mContext)
//                                .inflate(R.layout.wemedia_subject_list, parent, false));
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        int type = super.getItemViewType(position);
//        if (type != VIEW_TYPES.FOOTER) {
//            if (mSubjects.size() == 0){
//                if (position == 0) {
//                    return ITEM_TYPES.SECTIONS;
//                } else if (position == 2 && (mHotTopics.size() > 0 || mHotChannels.size() > 0)) {
//                    return ITEM_TYPES.SECTIONS;
//                } else if (position == 4 && mHotTopics.size() > 0 && mHotChannels.size() > 0) {
//                    return ITEM_TYPES.SECTIONS;
//                } else if (position == 1 && mHotTopics.size() > 0) {
//                    return ITEM_TYPES.TOPICS;
//                } else if (position == 1 && mHotTopics.size() == 0 && mHotChannels.size() > 0) {
//                    return ITEM_TYPES.CHANNELS;
//                } else if (position == 3 && mHotTopics.size() > 0 && mHotChannels.size() > 0) {
//                    return ITEM_TYPES.CHANNELS;
//                } else {
//                    return ITEM_TYPES.ARTICLES;
//                }
//            }else{
//                if (position == 0) {
//                    return ITEM_TYPES.SUBJECTS;
//                } else if (position == 1) {
//                    return ITEM_TYPES.SECTIONS;
//                } else if (position == 3 && (mHotTopics.size() > 0 || mHotChannels.size() > 0)) {
//                    return ITEM_TYPES.SECTIONS;
//                } else if (position == 5 && mHotTopics.size() > 0 && mHotChannels.size() > 0) {
//                    return ITEM_TYPES.SECTIONS;
//                } else if (position == 2 && mHotTopics.size() > 0) {
//                    return ITEM_TYPES.TOPICS;
//                } else if (position == 2 && mHotTopics.size() == 0 && mHotChannels.size() > 0) {
//                    return ITEM_TYPES.CHANNELS;
//                } else if (position == 4 && mHotTopics.size() > 0 && mHotChannels.size() > 0) {
//                    return ITEM_TYPES.CHANNELS;
//                } else {
//                    return ITEM_TYPES.ARTICLES;
//                }
//            }
//
//        } else {
//            return type;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mSubjects.size() + mHotTopics.size() + mHotChannels.size() + mArticles.size() == 0) {
//            return 0;
//        }
//        if (hasFooterView) {
//            if (mHotTopics.size() == 0 && mHotChannels.size() == 0) {
//                return mArticles.size() + 2 + increase();
//            } else if (mHotChannels.size() == 0 || mHotTopics.size() == 0) {
//                if (mArticles.size() > 0) {
//                    return mArticles.size() + 4 + increase();
//                } else {
//                    return 2 + increase();
//                }
//            }
//            return (mArticles.size() == 0 ? 5 : mArticles.size() + 6) + increase();
//        } else {
//            if (mHotTopics.size() == 0 && mHotChannels.size() == 0) {
//                return mArticles.size() + 1 + increase();
//            } else if (mHotChannels.size() == 0 || mHotTopics.size() == 0) {
//                if (mArticles.size() > 0) {
//                    return mArticles.size() + 3 + increase();
//                } else {
//                    return 1 + increase();
//                }
//            }
//            return (mArticles.size() == 0 ? 4 : mArticles.size() + 5) + increase();
//        }
//    }
//
//    private boolean isLogin() {
//        return !Session.isUserInfoEmpty();
//    }
//
//    private void login() {
//        mContext.startActivity(new Intent(mContext, LoginActivity.class));
//    }
//
//    private void showSectionView(SectionViewHolder h, int type) {
//        switch (type) {
//            case 0://hot topics
//                h.sectionTitle.setText(R.string.user_discovery_hot_topic);
//                h.sectionRightBtn.setVisibility(View.GONE);
//                h.sectionRightIcon.setVisibility(View.GONE);
//                h.splitLine.setVisibility(View.GONE);
//                break;
//            case 1://hot channels
//                h.sectionTitle.setText(R.string.user_discovery_hot_channel);
//                h.sectionRightBtn.setOnClickListener(v ->
//                        QEventBus.getEventBus().post(new Events.ShowHotChannels()));
//                h.sectionRightIcon.setOnClickListener(v ->
//                        QEventBus.getEventBus().post(new Events.ShowHotChannels()));
//                h.splitLine.setVisibility(View.GONE);
//                if (mHasMoreChannels) {
//                    h.sectionRightBtn.setVisibility(View.VISIBLE);
//                    h.sectionRightIcon.setVisibility(View.VISIBLE);
//                    QEventBus.getEventBus().post(new Events.HasMoreHotChannels(true));
//                } else {
//                    h.sectionRightBtn.setVisibility(View.GONE);
//                    h.sectionRightIcon.setVisibility(View.GONE);
//                    QEventBus.getEventBus().post(new Events.HasMoreHotChannels(false));
//                }
//                break;
//            case 2://articles
//                h.sectionTitle.setText(R.string.user_discovery_article);
//                h.sectionRightBtn.setVisibility(View.GONE);
//                h.sectionRightIcon.setVisibility(View.GONE);
//                h.splitLine.setVisibility(View.VISIBLE);
//                break;
//            default://none
//                h.sectionTitle.setVisibility(View.GONE);
//                h.sectionRightBtn.setVisibility(View.GONE);
//                h.sectionRightIcon.setVisibility(View.GONE);
//                h.splitLine.setVisibility(View.GONE);
//                break;
//        }
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
//                if (position == (increase()) ) {
//                    if (mHotTopics.size() > 0) {
//                        showSectionView(h, 0);
//                    } else if (mHotChannels.size() > 0) {
//                        showSectionView(h, 1);
//                    }
//                } else if (position == (2 + increase())) {
//                    if (mHotTopics.size() > 0 && mHotChannels.size() > 0) {
//                        showSectionView(h, 1);
//                    } else {
//                        showSectionView(h, 2);
//                    }
//                } else if (mArticles.size() > 0) {
//                    showSectionView(h, 2);
//                }
//                break;
//            case ITEM_TYPES.SUBJECTS:
//                if (mSubjectAdapter == null) {
//                    mSubjectAdapter = new WeMediaSubjectListAdapter(mContext);
//                    mSubjectAdapter.setData(mSubjects);
//                    ((SubjectListViewHolder) holder).subjectList.setAdapter(mSubjectAdapter);
//                    ((SubjectListViewHolder) holder).subjectList.setSelection(mSubjectAdapter.getCount() / 2);
//                    ((SubjectListViewHolder) holder).subjectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            mSubjectAdapter.setSelectItem(position);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                    mSubjectAdapter.notifyDataSetChanged();
//                }
//                break;
//            case ITEM_TYPES.TOPICS:
//                if (mHotTopicAdapter == null) {
//                    mHotTopicAdapter = new WeMediaTopicAvatarListAdapter(mContext);
//                    mHotTopicAdapter.setDatas(mHotTopics);
//                    (((HotTopicListViewHolder) holder).recyclerView).setAdapter(mHotTopicAdapter);
//                }
//                break;
//            case ITEM_TYPES.CHANNELS:
//                if (mHotChannelAdapter == null) {
//                    mHotChannelAdapter = new WeMediaHotChannelAvatarListAdapter(mContext);
//                    mHotChannelAdapter.setDatas(mHotChannels);
//                    (((HotChannelListViewHolder) holder).recyclerView).setAdapter(mHotChannelAdapter);
//                }
//                break;
//            case ITEM_TYPES.ARTICLES:
//                if (mHotChannels.size() > 0 && mHotTopics.size() > 0) {
//                    newPosition = position - 5 - increase();
//                } else if (mHotChannels.size() > 0 || mHotTopics.size() > 0) {
//                    newPosition = position - 3 - increase();
//                } else {
//                    newPosition = position - 1 - increase();
//                }
//
//                if (mArticles.size() <= newPosition) return;
//
//                NewsListData article = mArticles.get(newPosition);
//                ArticleViewHolder viewHolder = (ArticleViewHolder) holder;
//                viewHolder.articlePanel.setOnClickListener(v ->
//                        OldNewsDetailActivity.startActivity(
//                                mContext, article, REQUEST_CODE_FOR_NEW_DETAIL));
//
//                switch (Integer.parseInt(article.getNews_type())) {
//                    case 1://news
//                        viewHolder.playTime.setVisibility(View.GONE);
//                        break;
//                    case 2://live-video
//                        viewHolder.playTime.setVisibility(View.VISIBLE);
//                        viewHolder.playTime.setText(article.getVideo_length());
//                        break;
//                }
//
//                viewHolder.articleTitle.setText(article.getTitle());
//                viewHolder.articleCommentCnt.setText(article.getComment() + "条评论");
//                String pdate = article.getPdate();
//                if (!TextUtils.isEmpty(pdate)) {
//                    viewHolder.articlePublicTime.setText(DateUtils.formatTime(pdate + "000"));
//                }
//                viewHolder.articleSummary.setText(article.getSummary());
//                viewHolder.articleSource.setText(article.getSource());
//
//                //链接图片
//                if (TextUtils.isEmpty(article.getAlbum_pic())) {
//                    viewHolder.articleImgVisible.setVisibility(View.GONE);
//                } else {
//                    viewHolder.articleImgVisible.setVisibility(View.VISIBLE);
//                    viewHolder.articleImg.loadImage(article.getAlbum_pic());
//                }
//
//                break;
//        }
//
//    }
//
//    private int increase() {
//        return mSubjects.size() > 0 ? 1 : 0;
//    }
//
//    /***************************
//     * Data Request
//     *************************************/
//    private void requestAndShowSubjects() {
//        Observable.just(0)
//                .flatMap(this::getSubjectList)
//                .subscribe(it -> {
//                    mSubjects.clear();
//                    mSubjects.addAll(it);
//                    requestAndShowHotTopics();
//                    swipeRefreshLayout.setRefreshing(false);
//                    if (mSubjectAdapter != null) {
//                        mSubjectAdapter.notifyDataSetChanged();
//                    }
//                }, throwable -> {
//                    throwable.printStackTrace();
//                    setDataState(DataState.ERROR);
//                    mFragment.showSnackBar();
//                });
//    }
//
//    private void requestAndShowHotTopics() {
//        Observable.just(mHotTopicStart)
//                .flatMap(this::getHotTopicList)
//                .subscribe(it -> {
//                    if (mHotTopicStart == 0) {//首次清空
//                        mHotTopics.clear();
//                    }
//                    mHotTopics.addAll(it);
//                    if (it.size() == REQUEST_HOT_TOPICS_SIZE) {
//                        mHotTopicStart++;
//                        requestAndShowHotTopics();
//                    } else {
//                        requestAndShowHotChannels();
//                    }
//                    swipeRefreshLayout.setRefreshing(false);
//                    if (mHotTopicAdapter != null) {
//                        mHotTopicAdapter.notifyDataSetChanged();
//                    }
//                }, throwable -> {
//                    throwable.printStackTrace();
//                    setDataState(DataState.ERROR);
//                    mFragment.showSnackBar();
//                });
//
//    }
//
//    private void requestAndShowHotChannels() {
//        getHotChannelList(1, REQUEST_HOT_CHANNELS_SIZE + 1)
//                .subscribeOn(Schedulers.io())
//                .map(s -> {
//                    if (s.size() == REQUEST_HOT_CHANNELS_SIZE + 1) {
//                        mHasMoreChannels = true;
//                        return s.subList(0, REQUEST_HOT_CHANNELS_SIZE);
//                    } else {
//                        mHasMoreChannels = false;
//                        return s;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        s -> {
//                            mHotChannels.clear();
//                            mHotChannels.addAll(s);
//                            if (mHotChannelAdapter != null) {
//                                mHotChannelAdapter.notifyDataSetChanged();
//                            }
//                            notifyDataSetChanged();
//                            requestAndShowHotArticles(false);
//                        },
//                        e -> {
//                            e.printStackTrace();
//                            setDataState((DataState.ERROR));
//                            mFragment.showSnackBar();
//                        });
//    }
//
//    private void requestAndShowHotArticles(boolean isLoadMore) {
//        Observable.just(mHotArticleStart)
//                .flatMap(this::getHotArticles)
//                .doOnNext(it -> {
//                    if (!isLoadMore) {
//                        mArticles.clear();
//                    }
//                    mHasMoreArticles = false;
//                    if (it.size() > 0) {
//                        mHasMoreArticles = it.size() == REQUEST_ARTICLES_SIZE;
//                        mHotArticleStart++;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(it -> {
//                    swipeRefreshLayout.setRefreshing(false);
//                    if (!mHasMoreArticles) {
//                        notifyDataSetChanged();//for foot_loading reset
//                    }
//                })
//                .flatMap(Observable::from)
//                .subscribe(it -> {
//                    mArticles.add(it);
//                    if( mArticles.size() < 4 ){//no need anim
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
//    }
//
//    private Observable<List<WeMediaTopic>> getSubjectList(int start) {
//        return OldRetroAdapter.getService().getHotTopicList(start, REQUEST_SUBJECTS_SIZE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(respones -> Observable.just(respones.getData()));
//    }
//
//    private Observable<List<WeMediaTopic>> getHotTopicList(int start) {
//        return OldRetroAdapter.getService().getHotTopicList(start, REQUEST_HOT_TOPICS_SIZE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(respones -> Observable.just(respones.getData()));
//    }
//
//    private Observable<List<HotWeMediaChannel>> getHotChannelList(int start, int size) {
//        return OldRetroAdapter.getService().getUserHotChannelList(start, size)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .flatMap(respones -> Observable.just(respones.getData()));
//    }
//
//    private Observable<List<NewsListData>> getHotArticles(int start) {
//        return OldRetroAdapter.getService().getUserHotArticle(start, REQUEST_ARTICLES_SIZE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(respones -> Observable.just(respones.getData()));
//    }
//
//
//    /***************************
//     * ViewHolder
//     *************************************/
//
//    static class ArticleViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.article_publish_time)
//        TextView articlePublicTime;
//        @Bind(R.id.article_img)
//        GlideImageView articleImg;
//        @Bind(R.id.article_title)
//        TextView articleTitle;
//        @Bind(R.id.article_panel)
//        LinearLayout articlePanel;
//        @Bind(R.id.article_comment_cnt)
//        TextView articleCommentCnt;
//        @Bind(R.id.article_img_visible)
//        View articleImgVisible;
//        @Bind(R.id.forward_panel)
//        View forwardPanel;
//        @Bind(R.id.article_summary)
//        TextView articleSummary;
//        @Bind(R.id.article_source)
//        TextView articleSource;
//        @Bind(R.id.play_time)
//        TextView playTime;
//
//        ArticleViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//            forwardPanel.setVisibility(View.GONE);
//        }
//    }
//
//    static class HotChannelListViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.hot_channel_avatar_list)
//        RecyclerView recyclerView;
//
//        HotChannelListViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    static class HotTopicListViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.topic_avatar_list)
//        RecyclerView recyclerView;
//
//        HotTopicListViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    static class SubjectListViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.subject_list)
//        Gallery subjectList;
//
//        SubjectListViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//            subjectList.setSpacing(20);
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
