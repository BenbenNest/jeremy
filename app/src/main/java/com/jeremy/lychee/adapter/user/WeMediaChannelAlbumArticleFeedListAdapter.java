package com.jeremy.lychee.adapter.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.activity.news.WebViewActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.WeMediaAlbumDetailActivity;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.FeedNewsEntity;
import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.StringUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class WeMediaChannelAlbumArticleFeedListAdapter extends BaseRecyclerViewAdapter {

    private static final int REQUEST_CODE_FOR_NEW_DETAIL = 0;
    private int start = 0;
    private boolean hasMore = true;
    private String mAlbumId;
    private boolean mIsMy = false;

    private WeMediaAlbumDetailActivity mActivity;

    private boolean mIsSelectMode = false;
    private boolean mIsAllSelected = false;

    public void setIsAllSelected(boolean isAllSelected) {
        this.mIsAllSelected = mIsAllSelected;
    }

    public boolean isIsAllSelected() {

        return mIsAllSelected;
    }

    public WeMediaChannelAlbumArticleFeedListAdapter(
            WeMediaAlbumDetailActivity activity, String albumId, boolean isMy) {
        super(activity);
        this.mActivity = activity;
        mAlbumId = albumId;
        mIsMy = isMy;
    }

    public void setSelectMode(boolean val) {
        mIsSelectMode = val;
        notifyDataSetChanged();
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

    public void resetAllSelect() {
        mIsAllSelected = false;
        for (FeedNewsEntity i : (List<FeedNewsEntity>) mItems) {
            if (i.isIs_select() != FeedNewsEntity.NONE) {
                i.setIs_select(FeedNewsEntity.NONE);
            }
        }
        notifyDataSetChanged();
    }

    @Deprecated
    public void setAllUnSelect() {
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

    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    private void login() {
        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
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
                    setDataState(DataState.EMPTY);
                }
                mActivity.setArticlesEditMenuVisibility((it.size() != 0));
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
        return OldRetroAdapter.getService().getFeed(start, mAlbumId, 10)
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
                        ToastHelper.getInstance(mActivity).toast(com.jeremy.lychee.R.string.comment_digged);
                    }
                }, Throwable::printStackTrace);
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
        viewHolder.articlePanel.setOnClickListener(v -> {
                    if (article.getTransmit().getNews().getNews_type().equals("4")) {//转码失败
                        Bundle bundle = new Bundle();
                        bundle.putString("url", article.getTransmit().getNews().getUrl());
                        mActivity.openActivity(WebViewActivity.class, bundle, 0);
                        ToastHelper.getInstance(mContext).toast(mContext.getString(com.jeremy.lychee.R.string.toast_zm_url_get_failed));
                    } else {
                        OldNewsDetailActivity.startActivity(
                                mActivity, article.getTransmit().getNews(), REQUEST_CODE_FOR_NEW_DETAIL,
                                mAlbumId, article.getTransmit().getTransmitid(),
                                null, null);
                    }
                }
        );


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

        //设置删除按钮
        if (mIsMy) {
            viewHolder.detailItemColumndelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.detailItemColumndelete.setVisibility(View.GONE);
        }
        viewHolder.detailItemColumndelete.setOnClickListener(v ->
                DialogUtil.showConfirmDialog(mContext, "确定要删除该条文章吗？",
                        mContext.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                            //远程删除
                            String sign = AppUtil.transmitDeleteSignString(article.getTransmit().getTransmitid());
                            OldRetroAdapter.getService().deleteTransmit(article.getTransmit().getTransmitid(), sign)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            s -> {
                                                if (s.getErrno() == 0) {
                                                    //成功删除后：本地删除
                                                    mItems.remove(position);
                                                    notifyDataSetChanged();
                                                    if (mItems.size() == 0) {
                                                        setDataState(DataState.EMPTY);
                                                    }
                                                }
                                            },
                                            e -> {
                                                e.printStackTrace();
                                                //网络错误
                                                ToastHelper.getInstance(mContext.getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                                            }
                                    );
                            dialog.dismiss();
                        }, mContext.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss));

        //选择模式相关
        article.setIs_select(mIsAllSelected && (article.isIs_select() == FeedNewsEntity.NONE)
                ? FeedNewsEntity.SELECT : article.isIs_select());
        viewHolder.checkable.setChecked(article.isIs_select() == FeedNewsEntity.SELECT);
        viewHolder.checkable.setVisibility(mIsSelectMode ? View.VISIBLE : View.GONE);
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
            forwardPanel.setVisibility(View.GONE);
            articleSource.setEnabled(false);
        }
    }

}



