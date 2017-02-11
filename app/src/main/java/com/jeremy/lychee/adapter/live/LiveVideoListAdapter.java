package com.jeremy.lychee.adapter.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.news.CommentActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.manager.live.LiveVideoListManager;
import com.jeremy.lychee.manager.live.LiveVideoManager;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelBasic;
import com.jeremy.lychee.videoplayer.VideoPlayerView;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LiveVideoListAdapter extends BaseRecyclerViewAdapter<LiveVideoListAdapter.VideoListViewHolder, LiveVideoInfo> {
    private String topicId;
    private String id;
    private String tag;
    private List<LiveVideoInfo> presetVideoList;
    private List<LiveVideoInfo> videoList = new ArrayList<>();
    private boolean hasData = false;

    public interface LiveVideoListAdapterDelegate {
        void onPlayVideo(int nIndex);
    }

    private LiveVideoListManager videoListManager;
    private LiveVideoListAdapterDelegate delegate;

    public LiveVideoListAdapter(Context context, String topicId, String id, String tag, List<LiveVideoInfo> presetVideoList, LiveVideoListManager videoListManager, LiveVideoListAdapterDelegate delegate) {
        super(context);
        this.topicId = topicId;
        this.id = id;
        this.tag = tag;
        this.presetVideoList = presetVideoList;
        this.videoListManager = videoListManager;
        this.delegate = delegate;
    }

    @Override
    public boolean isBackwardLoadEnable() {
        return !videoListManager.isNoMoreData(topicId, id, tag);
    }

    @Override
    public void load(boolean isLoadMore) {
        if (!isLoadMore) {
            swipeRefreshLayout.setRefreshing(true);
        }
        updateLiveList(isLoadMore);
    }

    public void updateLiveList(boolean loadMore) {
        if (!loadMore && presetVideoList != null && presetVideoList.size() > 0) {
            append(presetVideoList);
            videoList.addAll(presetVideoList);
            presetVideoList.clear();
            return;
        }

        videoListManager.getLiveVideoList(topicId, id, tag, loadMore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((RxAppCompatActivity) getContext()).bindToLifecycle())
                .doOnNext(it -> {
                    if (it.first == 1 || !loadMore) {
                        hasData = false;
                        clear();
                        videoList.clear();
                    }
                    if (it.second.size() == 0) {
                        setDataState(hasData ? BaseRecyclerViewAdapter.DataState.NORMAL : BaseRecyclerViewAdapter.DataState.ERROR);
                    }
                })
                .subscribe(it -> {
                    hasData = true;
                    append(it.second);
                    videoList.addAll(it.second);
                }, e -> {
                    setDataState(BaseRecyclerViewAdapter.DataState.ERROR);
                });
    }

    public List<LiveVideoInfo> getVideoList() {
        return videoList;
    }

    @Override
    public VideoListViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.live_video_list_item, parent, false);
        return new VideoListViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(VideoListViewHolder holder, int position) {
        LiveVideoInfo videoInfo = getItem(position);
        holder.mUsername.setText(videoInfo.getVideo_name());
        holder.mLiveTitle.setText(videoInfo.getVideo_column_name());
        int thumbsUpCount = 0;
        try {
            thumbsUpCount = Integer.parseInt(videoInfo.getDing());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        holder.mLiveThumbsUpIcon.setImageResource(LiveVideoManager.getInstance().isThumbsUped(videoInfo) ? R.drawable.article_icon_good_selected : R.drawable.article_icon_good_default);
        holder.mThumbsUpCount.setText(thumbsUpCount == 0 ? getContext().getResources().getText(R.string.live_thumbs_up_empty) : String.format("%d", thumbsUpCount));
        holder.mThumbsUp.setOnClickListener(v -> LiveVideoManager.getInstance().thumbsUp(getContext(), videoInfo));
        holder.mComment.setOnClickListener(v -> {
            CommentActivity.startActivity(getContext(), 0, 2, videoInfo.getVideo_key(), videoInfo.getVideo_name(), videoInfo.getId(), null);
        });
        holder.mShare.setOnClickListener(v -> LiveVideoManager.getInstance().shareVideo(getContext(), videoInfo));

        holder.mLiveThumbnail.loadImage(getItem(position).getVideo_img());
        holder.mPlayBtn.setOnClickListener(v -> {
            if (delegate != null) {
                delegate.onPlayVideo(position);
            }
        });
        holder.mLiveThumbnail.setOnClickListener(v -> {
            if (delegate != null) {
                delegate.onPlayVideo(position);
            }
        });

        if (holder.videoPlayerView == VideoPlayerView.getBindingView()) {
            holder.videoPlayerView.bindToVideoPlayer();
        }
    }

    public void onThumbsUpSuccess(Object o) {
        notifyDataSetChanged();
    }

    public static class VideoListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.video_player_view)
        VideoPlayerView videoPlayerView;
        @Bind(R.id.channel_name)
        TextView mUsername;
        @Bind(R.id.live_title)
        TextView mLiveTitle;
        @Bind(R.id.live_thumbnail)
        GlideImageView mLiveThumbnail;
        @Bind(R.id.thumbs_up_icon_in_list)
        ImageView mLiveThumbsUpIcon;
        @Bind(R.id.thumbs_up_in_list)
        RelativeLayout mThumbsUp;
        @Bind(R.id.comment_in_list)
        RelativeLayout mComment;
        @Bind(R.id.share_in_list)
        RelativeLayout mShare;
        @Bind(R.id.thumbs_up_text_in_list)
        TextView mThumbsUpCount;
        @Bind(R.id.play_btn)
        ImageView mPlayBtn;

        VideoListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            videoPlayerView.addControlPanel(new VideoPlayerPanelBasic());
        }
    }
}
