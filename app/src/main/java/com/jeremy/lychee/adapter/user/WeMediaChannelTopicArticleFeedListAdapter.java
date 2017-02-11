package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.DateUtils;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class WeMediaChannelTopicArticleFeedListAdapter extends BaseRecyclerViewAdapter {

    private static final int REQUEST_CODE_FOR_NEW_DETAIL = 0;
    private int start = 0;
    private boolean hasMore = true;
    private int mTopicId;
    Context mContext;

    public WeMediaChannelTopicArticleFeedListAdapter(Context context, int topicId) {
        super(context);
        this.mContext = context;
        mTopicId = topicId;
    }


    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    private void login() {
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }

    @Override
    public boolean isBackwardLoadEnable() {
        return hasMore;
    }

    @Override
    public void load(boolean isLoadMore) {

        if (!isLoadMore) {
            start = 0;
        }

        Observable<List<NewsListData>> newsObservable = getTopicFeedList(start);
        newsObservable.subscribe(it -> {
            if (!isLoadMore) {
                clear();
                if (it.size() == 0) {
                    setDataState(DataState.EMPTY);
                }
            }
            append(it);
            hasMore = it.size() == 10;
            if (it.size() > 0) {
                start = Integer.parseInt(it.get(it.size() - 1).getId());
            }
        }, throwable -> {
            throwable.printStackTrace();
            setDataState(DataState.ERROR);
        });

    }

    private Observable<List<NewsListData>> getTopicFeedList(int start) {
        return OldRetroAdapter.getService().getTopicNewsList(mTopicId, start, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(it -> Observable.just(it.getData()));
    }

    private void feedLike(String transmitId, Action0 cb) {
        String sign = AppUtil.transmitDeleteSignString(transmitId);
        OldRetroAdapter.getService().feedLike(transmitId, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getErrno() == 0) {
                        cb.call();
                    } else if (s.getErrno() == -1) {//Operation repeat
                        ToastHelper.getInstance(mContext).toast(com.jeremy.lychee.R.string.comment_digged);
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public ArticleViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(com.jeremy.lychee.R.layout.wemedia_article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {

        NewsListData article = (NewsListData) mItems.get(position);
        ArticleViewHolder viewHolder = (ArticleViewHolder) holder;

        viewHolder.articlePanel.setOnClickListener(v -> {
            OldNewsDetailActivity.startActivity(
                    mContext, article,
                    REQUEST_CODE_FOR_NEW_DETAIL,
                    null,
                    null,
                    null,
                    null);
        });

        switch (Integer.parseInt(article.getNews_type())) {
            case 1://internal-news
            case 4://compose-news
            case 3://external-news
                viewHolder.playTime.setVisibility(View.GONE);
                break;
            case 2://live-video
                viewHolder.playTime.setVisibility(View.VISIBLE);
                viewHolder.playTime.setText(article.getVideo_length());
                break;
        }

        viewHolder.articleTitle.setText(article.getTitle());
        String pdate = article.getPdate();
        if(!TextUtils.isEmpty(pdate)){
            viewHolder.articlePublishTime.setText(DateUtils.formatTime(pdate + "000"));
        }

        //链接图片
        viewHolder.articleImg.loadImage(article.getAlbum_pic());
        viewHolder.articleSummary.setText(article.getSummary());
        viewHolder.articleSource.setText(article.getSource());
        viewHolder.articleCommentCnt.setText(article.getComment()+"条评论");

    }

    @Override
    public View getEmptyView(ViewGroup parent, View convertView, int currentState) {
        if (convertView == null) {
            if (null != mContext) switch (currentState) {
                case DataState.EMPTY: {
                    convertView = LayoutInflater.from(mContext).inflate(
                            com.jeremy.lychee.R.layout.wemedia_album_article_nodata_layout, parent);
                    break;
                }
                default:
                    return super.getEmptyView(parent, null, currentState);
            }
        }
        return convertView;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.article_publish_time)
        TextView articlePublishTime;
        @Bind(com.jeremy.lychee.R.id.article_img)
        GlideImageView articleImg;
        @Bind(com.jeremy.lychee.R.id.article_title)
        TextView articleTitle;
        @Bind(com.jeremy.lychee.R.id.article_panel)
        LinearLayout articlePanel;
        @Bind(com.jeremy.lychee.R.id.article_summary)
        TextView articleSummary;
        @Bind(com.jeremy.lychee.R.id.article_source)
        TextView articleSource;
        @Bind(com.jeremy.lychee.R.id.article_delete)
        TextView ArticleDelete;
        @Bind(com.jeremy.lychee.R.id.forward_panel)
        View forwardPanel;
        @Bind(com.jeremy.lychee.R.id.article_comment_cnt)
        TextView articleCommentCnt;
        @Bind(com.jeremy.lychee.R.id.play_time)
        TextView playTime;

        ArticleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            forwardPanel.setVisibility(View.GONE);
            articleSource.setEnabled(false);
            ArticleDelete.setVisibility(View.GONE);
        }
    }

}



