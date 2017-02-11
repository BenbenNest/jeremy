package com.jeremy.lychee.adapter.user;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.activity.user.WeMediaImportArticleActivity;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.model.user.FeedNewsEntity;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.StringUtil;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WeMediaImportArticleFeedListAdapter extends BaseRecyclerViewAdapter {

    private int start = 0;
    private boolean hasMore = true;

    private WeMediaImportArticleActivity mActivity;

    private boolean mIsAllSelected = false;
    public void setIsAllSelected(boolean isAllSelected) {
        this.mIsAllSelected = mIsAllSelected;
    }
    public boolean isIsAllSelected() {

        return mIsAllSelected;
    }

    public WeMediaImportArticleFeedListAdapter(
            WeMediaImportArticleActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    public void setAllSelect() {
        mIsAllSelected = true;
        for (FeedNewsEntity i : (List<FeedNewsEntity>) mItems) {
            if (i.isIs_select() != FeedNewsEntity.SELECT) {
                i.setIs_select(FeedNewsEntity.SELECT);
            }
        }
        notifyDataSetChanged();
    }

    public void resetAllSelect(){
        mIsAllSelected = false;
        for (FeedNewsEntity i : (List<FeedNewsEntity>) mItems) {
            if (i.isIs_select() != FeedNewsEntity.NONE) {
                i.setIs_select(FeedNewsEntity.NONE);
            }
        }
        notifyDataSetChanged();
    }

    @Deprecated
    public void setAllUnSelect(){
        mIsAllSelected = false;
        for (FeedNewsEntity i : (List<FeedNewsEntity>) mItems) {
            if (i.isIs_select() != FeedNewsEntity.UNSELECT) {
                i.setIs_select(FeedNewsEntity.UNSELECT);
            }
        }
        notifyDataSetChanged();
    }

    private void updateAllSelectStatus() {
        for (FeedNewsEntity i : (List<FeedNewsEntity>) mItems) {
            if (i.isIs_select() != FeedNewsEntity.SELECT) {
                mActivity.mAllSelect.setText(com.jeremy.lychee.R.string.menu_all_select_text);
                return;
            }
        }
        if (!hasMore) {
            mIsAllSelected = true;
            mActivity.mAllSelect.setText(com.jeremy.lychee.R.string.menu_all_unselect_text);
        } else if (mIsAllSelected) {
            mActivity.mAllSelect.setText(com.jeremy.lychee.R.string.menu_all_unselect_text);
        }
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

        Observable<List<FeedNewsEntity>> newsObservable = getAlbumFeedList(start);
        newsObservable.subscribe(it -> {
            if (!isLoadMore) {
                clear();
                if (it.size() == 0) {
                    QEventBus.getEventBus().post(new Events.OnArticleEmpty());
                    setDataState(DataState.EMPTY);
                }
            }
            append(it);
            hasMore = it.size() == 10;
            if (it.size() > 0) {
                start = Integer.parseInt(it.get(it.size() - 1).getTransmit().getTransmitid());
            }
        }, throwable -> {
            throwable.printStackTrace();
            setDataState(DataState.ERROR);
        });

    }

    private Observable<List<FeedNewsEntity>> getAlbumFeedList(int start) {
        return OldRetroAdapter.getService().getFeed(start, null, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(it -> Observable.just(it.getData()));
    }

    @Override
    public ArticleViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(com.jeremy.lychee.R.layout.wemedia_article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {

        FeedNewsEntity article = (FeedNewsEntity) mItems.get(position);
        if (article.getTransmit() == null || article.getTransmit().getNews() == null) {
            return;
        }
        ArticleViewHolder viewHolder = (ArticleViewHolder) holder;

        viewHolder.articleTitle.setText(article.getTransmit().getNews().getTitle());

        //新闻内容&评论
        String summary, source;
        if (article.getTransmit().getType() == 4) {
            //compose-news
            source = article.getName();
        } else {
            //normal-news
            source = article.getTransmit().getNews().getSource();
        }
        summary = article.getTransmit().getNews().getSummary();
        if (TextUtils.isEmpty(summary)) {
            viewHolder.articleSummary.setVisibility(View.GONE);
        } else {
            viewHolder.articleSummary.setVisibility(View.VISIBLE);
            viewHolder.articleSummary.setText(summary);
        }
        viewHolder.articleSource.setText(source);

        //评论数&发布时间
        String num = article.getTransmit().getNews().getComment();
        String nFormat = mContext.getResources().getString(com.jeremy.lychee.R.string.user_home_comment_num);
        viewHolder.articleCommentCnt.setText(
                String.format(nFormat, StringUtil.isEmpty(num) ? "0" : num));
        viewHolder.articlePublishTime.setText(
                AppUtil.formatTime(article.getTransmit().getCreatetime() * 1000L));

        //链接图片
        if (TextUtils.isEmpty(article.getTransmit().getNews().getAlbum_pic())) {
            viewHolder.articleImgVisible.setVisibility(View.GONE);
        } else {
            viewHolder.articleImgVisible.setVisibility(View.VISIBLE);
            viewHolder.articleImg.loadImage(article.getTransmit().getNews().getAlbum_pic());
            switch (article.getTransmit().getType()) {
                case 1://internal-news
                case 4://compose-news
                case 3://external-news
                    viewHolder.playTime.setVisibility(View.GONE);
                    break;
                case 2://live-video
                    viewHolder.playTime.setVisibility(View.VISIBLE);
                    viewHolder.playTime.setText(article.getTransmit().getNews().getVideo_length());
                    break;
            }
        }

        //选择模式相关
        article.setIs_select(mIsAllSelected && (article.isIs_select() == FeedNewsEntity.NONE)
                ? FeedNewsEntity.SELECT : article.isIs_select());
        viewHolder.checkable.setChecked(article.isIs_select() == FeedNewsEntity.SELECT);
        viewHolder.checkable.setOnClickListener(
                v -> {
                    article.setIs_select(article.isIs_select() == FeedNewsEntity.SELECT
                            ? FeedNewsEntity.UNSELECT : FeedNewsEntity.SELECT);
                    updateAllSelectStatus();
                });

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
        @Bind(com.jeremy.lychee.R.id.article_comment_cnt)
        TextView articleCommentCnt;
        @Bind(com.jeremy.lychee.R.id.article_delete)
        TextView detailItemColumndelete;
        @Bind(com.jeremy.lychee.R.id.play_time)
        TextView playTime;
        @Bind(com.jeremy.lychee.R.id.article_img_visible)
        View articleImgVisible;
        @Bind(com.jeremy.lychee.R.id.article_source)
        TextView articleSource;
        @Bind(com.jeremy.lychee.R.id.article_summary)
        TextView articleSummary;
        @Bind(com.jeremy.lychee.R.id.forward_panel)
        View forwardPanel;
        @Bind(com.jeremy.lychee.R.id.checkable)
        CheckBox checkable;

        ArticleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            checkable.setVisibility(View.VISIBLE);
            forwardPanel.setVisibility(View.GONE);
            articleSource.setEnabled(false);
            articlePanel.setClickable(false);
            detailItemColumndelete.setVisibility(View.GONE);
        }
    }

}



