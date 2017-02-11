package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.activity.news.WebViewActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.WeMediaAlbumDetailActivity;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.FeedNewsEntity;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.StringUtil;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class WeMediaChannelArticleFeedListAdapter extends BaseRecyclerViewAdapter {

    private static final int REQUEST_CODE_FOR_NEW_DETAIL = 0;
    private Context context;
    private int start = 0;
    private boolean hasMore = true;
    private String mWmChannelId;
    private boolean mIsMy = false;

    public WeMediaChannelArticleFeedListAdapter(Context context, String wmChannelId) {
        super(context);
        this.context = context;
        mWmChannelId = wmChannelId;
        mIsMy = mWmChannelId.equals(Session.getSession().getReal_uid());
    }

    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    private void login() {
        context.startActivity(new Intent(context, LoginActivity.class));
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

        Observable<List<FeedNewsEntity>> newsObservable = getUserFeedList(start);
        newsObservable.subscribe(it -> {
            if (!isLoadMore) {
                clear();
                if (it.size() == 0) {
                    setDataState(DataState.EMPTY);
                }
            }
            append(it);
            if (it.size() > 0) {
                hasMore = it.size() == 10;
                start = Integer.parseInt(it.get(it.size() - 1).getTransmit().getTransmitid());
            }
        }, throwable -> {
            throwable.printStackTrace();
            setDataState(DataState.ERROR);
        });

    }

    private Observable<List<FeedNewsEntity>> getUserFeedList(int start) {
        return OldRetroAdapter.getService().getUserFeedList(start, mWmChannelId, 10)
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
                        ToastHelper.getInstance(context).toast(com.jeremy.lychee.R.string.comment_digged);
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public ArticleViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.jeremy.lychee.R.layout.wemedia_article_item, parent, false);
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
                        ((SlidingActivity)context).openActivity(WebViewActivity.class, bundle, 0);
                        ToastHelper.getInstance(mContext).toast(mContext.getString(com.jeremy.lychee.R.string.toast_zm_url_get_failed));
                    } else {
                        OldNewsDetailActivity.startActivity(
                                context, article.getTransmit().getNews(), REQUEST_CODE_FOR_NEW_DETAIL,
                                article.getSub_id(), article.getTransmit().getTransmitid(),
                                null, null);
                    }
                }
        );

        //新闻内容&评论
        String summary, source;
        if (article.getTransmit().getType() == 4) {
            //compose-news
            viewHolder.forwardPanel.setVisibility(View.GONE);
            source = article.getName();
        } else {
            //normal-news
            viewHolder.forwardPanel.setVisibility(View.VISIBLE);
            source = article.getTransmit().getNews().getSource();
            viewHolder.albumName.setText(article.getSub_name());
            viewHolder.albumName.setOnClickListener(
                    v -> WeMediaAlbumDetailActivity.startActivity(
                            mContext, article.getSub_id(), article.getSub_name(), false));
            if (TextUtils.isEmpty(article.getTransmit().getContent())) {
                viewHolder.forwardComment.setVisibility(View.GONE);
            } else {
                viewHolder.forwardComment.setVisibility(View.VISIBLE);
                viewHolder.forwardComment.setText(article.getTransmit().getContent());
            }
        }
        summary = article.getTransmit().getNews().getSummary();
        if (TextUtils.isEmpty(summary)) {
            viewHolder.articleSummary.setVisibility(View.GONE);
        } else {
            viewHolder.articleSummary.setVisibility(View.VISIBLE);
            viewHolder.articleSummary.setText(summary);
        }
        viewHolder.articleSource.setText(source);
        //当文章来源是自频道时
        String uid = article.getTransmit().getNews().getUid();
        if (!TextUtils.isEmpty(uid) && !uid.equals(mWmChannelId)) {
            viewHolder.articleSource.setEnabled(true);
            viewHolder.articleSource.setOnClickListener(v ->
                    WeMediaChannelActivity.startActivity(mContext, uid));
        } else {
            viewHolder.articleSource.setEnabled(false);
        }

        //评论数&发布时间
        String num = article.getTransmit().getNews().getComment();
        String nFormat = context.getResources().getString(com.jeremy.lychee.R.string.user_home_comment_num);
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
                    if(TextUtils.isEmpty(article.getTransmit().getNews().getVideo_length())){
                        viewHolder.playTime.setVisibility(View.GONE);
                    }else{
                        viewHolder.playTime.setVisibility(View.VISIBLE);
                        viewHolder.playTime.setText(article.getTransmit().getNews().getVideo_length());
                    }
                    break;
            }
        }

        //设置删除按钮
        if (mIsMy) {
            viewHolder.articleDelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.articleDelete.setVisibility(View.GONE);
        }
        viewHolder.articleDelete.setOnClickListener(v ->
                DialogUtil.showConfirmDialog(context, "确定要删除该条文章吗？",
                        context.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
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
                                                ToastHelper.getInstance(context.getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                                            }
                                    );
                            dialog.dismiss();
                        }, context.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss));

//        if (mIsMy) {
//            viewHolder.detailItemColumnPraise.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.detailItemColumnPraise.setText(newInfo.getTransmit().getLikes());
//            viewHolder.detailItemColumnPraise.setSelected(newInfo.getTransmit().is_ding());
//            viewHolder.detailItemColumnPraise.setOnClickListener(v -> {
//
//                if (isLogin()) {
//                    TextView num = ((TextView) v);
//                    if (!newInfo.getTransmit().is_ding()) {
//                        feedLike(newInfo.getTransmit().getTransmitid(), () -> {
//                            int dingNum = Integer.valueOf(newInfo.getTransmit().getLikes());
//                            newInfo.getTransmit().setLikes(String.valueOf(dingNum + 1));
//                            newInfo.getTransmit().setIs_ding(true);
//                            num.setText(newInfo.getTransmit().getLikes());
//                            num.setSelected(newInfo.getTransmit().is_ding());
//                        });
//                    } else {
//                        Toast.makeText(context, R.string.comment_digged, Toast.LENGTH_SHORT).show();
//                    }
//                    num.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.digg_click_scale));
//                } else {
//                    login();
//                }
//            });
//        }

    }

    @Override
    public View getEmptyView(ViewGroup parent, View convertView, int currentState) {
        if (convertView == null) {
            if (null != mContext) switch (currentState) {
                case DataState.EMPTY: {
                    convertView = LayoutInflater.from(mContext).inflate(
                            com.jeremy.lychee.R.layout.wemedia_article_nodata_layout, parent);
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
        @Bind(com.jeremy.lychee.R.id.forward_comment)
        TextView forwardComment;
        @Bind(com.jeremy.lychee.R.id.article_img)
        GlideImageView articleImg;
        @Bind(com.jeremy.lychee.R.id.article_title)
        TextView articleTitle;
        @Bind(com.jeremy.lychee.R.id.album_name)
        TextView albumName;
        @Bind(com.jeremy.lychee.R.id.article_panel)
        LinearLayout articlePanel;
        @Bind(com.jeremy.lychee.R.id.article_comment_cnt)
        TextView articleCommentCnt;
        @Bind(com.jeremy.lychee.R.id.article_delete)
        TextView articleDelete;
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


        ArticleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
