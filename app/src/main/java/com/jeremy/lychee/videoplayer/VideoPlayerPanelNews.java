package com.jeremy.lychee.videoplayer;

import android.view.ViewGroup;

import com.jeremy.lychee.activity.live.LiveVideoListActivity;
import com.jeremy.lychee.manager.live.LiveVideoListManager;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangying-pd on 2016/3/15.
 * 新闻详情页 VideoPlayerPanel
 */
public class VideoPlayerPanelNews extends VideoPlayerPanel {
    private List<LiveVideoInfo> liveVideoInfos;

    @Override
    public void init() {
        super.init();
        setPanelType(PanelType.PanelType_Normal);
    }

    @Override
    public int getLayoutId() {
        return com.jeremy.lychee.R.layout.player_ctrl_panel_news;
    }

    @Override
    public void onViewCreated(ViewGroup panelView) {
        super.onViewCreated(panelView);
        ButterKnife.bind(this, panelView);
    }

    @OnClick(com.jeremy.lychee.R.id.more_videos_btn)
    void onMoreVideoClicked() {
        if(liveVideoInfos!=null&&liveVideoInfos.size()>0 && getContext()!=null){
            try {
                LiveVideoInfo liveVideoInfo = liveVideoInfos.get(0);
                LiveVideoListActivity.loadVideo((SlidingActivity) getContext(), liveVideoInfo.getVideo_type(), liveVideoInfo.getId(), liveVideoInfo.getTag(), liveVideoInfos, LiveVideoListManager.createInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setVideoList(List<LiveVideoInfo> liveVideoInfos){
        this.liveVideoInfos = liveVideoInfos;
    }
}