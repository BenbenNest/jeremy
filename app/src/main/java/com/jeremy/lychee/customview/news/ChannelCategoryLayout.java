package com.jeremy.lychee.customview.news;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.activity.news.SubChannelActivity;
import com.jeremy.lychee.activity.news.SubChannelListActivity;
import com.jeremy.lychee.adapter.news.SubChannelAdapter;
import com.jeremy.lychee.model.news.ChannelCategory;
import com.jeremy.lychee.customview.NoScrollGridView;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChannelCategoryLayout extends LinearLayout {

    @Bind(com.jeremy.lychee.R.id.channel_icon)
    ImageView channelIcon;
    @Bind(com.jeremy.lychee.R.id.channel_name_chn)
    TextView channelNameChn;
    @Bind(com.jeremy.lychee.R.id.channel_name_eng)
    TextView channelNameEng;
    @Bind(com.jeremy.lychee.R.id.sub_channel_count)
    TextView subChannelCount;
    @Bind(com.jeremy.lychee.R.id.sub_grid_view)
    NoScrollGridView subGridView;

    private ChannelCategory.ChannelInfo info;
    private SubChannelAdapter adapter;

    public ChannelCategoryLayout(Context context) {
        super(context);
        init();
    }

    public ChannelCategoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChannelCategoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(com.jeremy.lychee.R.layout.channel_category_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setChannelInfo(ChannelCategory.ChannelInfo info) {
        this.info = info;
        Glide.with(getContext()).load(ImageOptiUrl.get(info.getIcon(), channelIcon)).into(channelIcon);
        channelNameChn.setText(info.getCname());
        channelNameEng.setText(info.getEnname());
        try {
            int subCount = Integer.parseInt(info.getSub_num());
            subChannelCount.setText(String.format(getContext().getString(com.jeremy.lychee.R.string.see_all_sub_channel), subCount));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        setSubs(info.getSubs());
    }

    private void setSubs(List<ChannelCategory.ChannelInfo.SubChannel> subs) {
        adapter = new SubChannelAdapter(getContext(), subs);
        subGridView.setAdapter(adapter);
        subGridView.setOnItemClickListener((adapterView, view, i, l) -> {
            ChannelCategory.ChannelInfo.SubChannel subChannel = subs.get(i);
            SubChannelActivity.StartActivity(getContext(),
                    subChannel.getCid(), subChannel.getCname(), subChannel.getIcon());
        });
    }

    @OnClick(com.jeremy.lychee.R.id.title_layout)
    public void goToSubChannel(View view) {
        if (info == null) {
            return;
        }
        if (view.getContext() instanceof SlidingActivity) {
            SlidingActivity slidingActivity = (SlidingActivity) view.getContext();
            Bundle bundle = new Bundle();
            bundle.putString("cid", info.getCid());
            bundle.putString("title", info.getCname());
            slidingActivity.openActivity(SubChannelListActivity.class, bundle, 0);
        }
    }

}
