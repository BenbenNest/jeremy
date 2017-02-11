package com.jeremy.lychee.adapter.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.live.LivePlayerActivity;
import com.jeremy.lychee.activity.news.CommentActivity;
import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.db.LiveHotItem;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.ShareInfo;

import com.jeremy.lychee.model.news.HotVideoBody;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.ZanPreference;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.shareWindow.ShareManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaChannelVideoListAdapter extends BaseRecyclerViewAdapter<WeMediaChannelVideoListAdapter.ViewHolder, LiveHotItem> {

    private static final int REQUEST_SIZE = 10;
    private Context context;
    private int start = 0;
    private boolean hasMore = true;
    private String uid;

    public WeMediaChannelVideoListAdapter(Context context, String uid) {
        super(context);
        this.context = context;
        this.uid = uid;
    }

    @Override
    public boolean isBackwardLoadEnable() {
        return hasMore;
    }

    @Override
    public void load(boolean isLoadMore) {

        if (!isLoadMore) {
            start = 1;
        }
        requestNetData(start)
                .subscribe(it -> {
                    if (!isLoadMore) {
                        clear();
                        if (it.size() == 0) {
                            setDataState(DataState.EMPTY);
                        }
                    }
                    append(it);
                    if (it.size() > 0) {
                        hasMore = it.size() == REQUEST_SIZE;
                        start++;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    setDataState(DataState.ERROR);
                });

    }

    @Override
    public ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wemedia_video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(ViewHolder holder, int position) {
        LiveHotItem video = getItem(position);
        holder.title.setText(video.getVideo_name());
        holder.time.setText(AppUtil.formatTime(Long.parseLong(video.getPdate()) * 1000L));
        holder.video_img.loadImage(video.getVideo_img());
        holder.zan.setEnabled(false);
        holder.share.setEnabled(false);
        holder.comment_num.setEnabled(false);
        switch(Integer.parseInt(video.getVideo_publish_status())){
            case 0:
                holder.publicState.setText("安全审核中...");
                holder.publicState.setTextColor(getContext().getResources().getColor(R.color.public_checking));
                holder.video_img.setOnClickListener(
                        v -> LivePlayerActivity.loadVideo(context, video.getId(), video.getVideo_type(), video.getTag()));
                break;
            case 1:
                holder.zan.setEnabled(true);
                holder.share.setEnabled(true);
                holder.comment_num.setEnabled(true);
                holder.publicState.setText("");
                holder.video_img.setOnClickListener(
                        v -> {
                            NewsListData entity = new NewsListData();
                            entity.setTitle(video.getVideo_name());
                            entity.setUrl(video.getVideo_url());
                            entity.setZm(video.getZm());
                            entity.setNews_type(video.getNews_type());

                            HotVideoBody hotVideoBody = new HotVideoBody();
                            hotVideoBody.setTag(video.getTag());
                            hotVideoBody.setSource_type(video.getVideo_type());
                            hotVideoBody.setVideo_id(video.getId());
                            entity.setNews_data(GsonUtils.toJson(hotVideoBody));
                            OldNewsDetailActivity.startActivity(mContext, entity, 0);
                        });
                break;
            default:
                holder.publicState.setText("审核失敗");
                holder.publicState.setTextColor(getContext().getResources().getColor(R.color.public_ng));
                holder.video_img.setOnClickListener(
                        v -> LivePlayerActivity.loadVideo(context, video.getId(), video.getVideo_type(), video.getTag()));
                break;

        }
        holder.time_howlong.setText(video.getVideo_duration());
        holder.comment_num.setText(video.getComment() + "评论");
        holder.share.setOnClickListener(v -> {
            LiveEvent.ShareClick  shareClick = new LiveEvent.ShareClick();
            shareClick.position = position;
            shareClick.url = video.getVideo_play_url();
            QEventBus.getEventBus().post(shareClick);
        });

        //点赞
        boolean is_zan = ZanPreference.getInstance().getBooleanValue(video.getVideo_play_url());
        if (is_zan) {
            Drawable right_drable = context.getResources().getDrawable(R.drawable.article_icon_good_selected);
            holder.zan.setCompoundDrawablesWithIntrinsicBounds(right_drable, null, null, null);
            holder.zan.setText(video.getDing());
        } else {
            Drawable right_drable = mContext.getResources().getDrawable(R.drawable.article_icon_good_default);
            holder.zan.setCompoundDrawablesWithIntrinsicBounds(null, null, right_drable, null);
            holder.zan.setText("");
            ZanPreference.getInstance().remove(video.getVideo_key());
        }

        holder.zan.setOnClickListener(v -> {
            zan_btn(video.getVideo_key(),position,holder,video.getDing());
        });

        //分享
        holder.share.setOnClickListener(v -> {
            ShareInfo shareInfo = new ShareInfo(
                    video.getVideo_play_url(), video.getId(), video.getVideo_name(), "", video.getVideo_img(), null, ShareInfo.SHARECONTENT_LIVE);
            new ShareManager((Activity) getContext(), shareInfo, true,
                    () -> HitLog.hitLogShare("null", "null", video.getId(), video.getNews_from())) //分享打点
                    .show();
        });

        //评论点击
        holder.comment_num.setOnClickListener(v1 -> {
            Intent intent = new Intent(mContext, CommentActivity.class);
            intent.putExtra(Constants.BUNDLE_KEY_TYPE, 2);
            Bundle bundle = new Bundle();
            bundle.putSerializable("comments_url", video.getVideo_key());
            bundle.putSerializable("comments_title", video.getVideo_name());
            intent.putExtra(CommentActivity.COMMENTS_NID, video.getId());
            intent.putExtra(CommentActivity.COMMENTS_FROM, video.getNews_from());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });

        //设置删除按钮
        if (uid.equals(Session.getSession().getReal_uid())) {
            holder.videoDelete.setVisibility(View.VISIBLE);
        } else {
            holder.videoDelete.setVisibility(View.GONE);
        }
        //TODO
        holder.videoDelete.setOnClickListener(v ->
                DialogUtil.showConfirmDialog(context, "确定要删除该条视频吗？",
                        context.getString(R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                            //远程删除
                            String sign = AppUtil.transmitDeleteSignString(video.getId());
                            OldRetroAdapter.getService().delHistoryLive(video.getId(), sign)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            s -> {
                                                if (s.errno == 0) {
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
                                                ToastHelper.getInstance(context.getApplicationContext()).toast(R.string.net_error);
                                            }
                                    );
                            dialog.dismiss();
                        }, context.getString(R.string.dialog_button_cancel), DialogInterface::dismiss));
    }

    @Override
    public View getEmptyView(ViewGroup parent, View convertView, int currentState) {
        if (convertView == null) {
            if (null != mContext) switch (currentState) {
                case DataState.EMPTY: {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.wemedia_article_nodata_layout, parent);
                    ((TextView)convertView.findViewById(R.id.no_data_text)).
                            setText(context.getString(R.string.wemedia_video_empty_str));
                    break;
                }
                default:
                    return super.getEmptyView(parent, null, currentState);
            }
        }
        return convertView;
    }

    private void zan_btn(String url,int position,ViewHolder viewHolder,String ding){
        boolean is_zan =  ZanPreference.getInstance().getBooleanValue(url);
        if (is_zan){
            ToastHelper.getInstance(context).toast("已经赞过啦！");
        }else {
            OldRetroAdapter.getService().punch(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBase>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastHelper.getInstance(context).toast("点赞失败！");
                        }

                        @Override
                        public void onNext(ModelBase modelBase) {
                            if (modelBase.getErrno() == 0) {
                                ToastHelper.getInstance(context).toast("点赞成功！");
                                ZanPreference.getInstance().saveBooleanValue(url, true);
                                Drawable right_drable = context.getResources().getDrawable(R.drawable.article_icon_good_selected);
                                viewHolder.zan.setCompoundDrawablesWithIntrinsicBounds(right_drable, null, null, null);
                                viewHolder.zan.setText("" + (Integer.parseInt(ding) + 1));
                                LiveHotItem hotItem = getItems().get(position);
                                hotItem.setDing((Integer.parseInt(ding) + 1) + "");

                            } else {
                            }
                        }
                    });
        }

    }

    /********************************** net request **************************/
    /**
     * 获取网络上传历史、直播历史
     */
    private Observable<List<LiveHotItem>> requestNetData(int start){
        return OldRetroAdapter.getService().getHistoryLives(start, REQUEST_SIZE, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(it -> Observable.just(it.getData()));
    }


    /********************************** VH ***********************************/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.video_img)
        GlideImageView video_img;
        @Bind(R.id.time_howlong)
        TextView time_howlong;
        @Bind(R.id.comment_num)
        TextView comment_num;
        @Bind(R.id.share)
        TextView share;
        @Bind(R.id.zan)
        TextView zan;
        @Bind(R.id.public_state)
        TextView publicState;
        @Bind(R.id.video_delete)
        View videoDelete;

        // each data item is just a string in this case
        public ViewHolder(View view) {
            super(view);
            rootView = view;
            rootView.setClickable(true);
            ButterKnife.bind(this, view);
        }
    }
}
