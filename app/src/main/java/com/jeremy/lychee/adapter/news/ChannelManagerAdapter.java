package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jeremy.lychee.activity.news.OrderedChannelsActivity;
import com.jeremy.lychee.activity.news.SubChannelListActivity;
import com.jeremy.lychee.manager.FontManager;
import com.jeremy.lychee.model.news.ChannelCategory;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.customview.news.ChannelCategoryLayout;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChannelManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int channelNum = -1;
    private ChannelCategory.Intro intro;
    private List<ChannelCategory.ChannelInfo> channelInfoList = new ArrayList<>();

    public ChannelManagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 0:
                View view = inflater.inflate(com.jeremy.lychee.R.layout.channel_manager_title, parent, false);
                return new TitleViewHolder(view);
            case 1:
                view = inflater.inflate(com.jeremy.lychee.R.layout.channel_manager_intro, parent, false);
                return new IntroViewHolder(view);
            default:
                view = inflater.inflate(com.jeremy.lychee.R.layout.channel_manager_sub_channel, parent, false);
                return new SubChannelViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case 0:
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.ordered.setText(String.format(context.getString(com.jeremy.lychee.R.string.ordered_channel), channelNum));
                RxView.clicks(titleViewHolder.itemView)
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .subscribe(o -> {
                            Context context = holder.itemView.getContext();
                            if (context instanceof SlidingActivity) {
                                SlidingActivity slidingActivity = (SlidingActivity) context;
                                slidingActivity.openActivity(OrderedChannelsActivity.class);
                            }
                        });
                break;
            case 1:
                IntroViewHolder introViewHolder = (IntroViewHolder) holder;
                if (intro != null) {
                    introViewHolder.recommendedImg.loadImage(intro.getPic());
                    introViewHolder.recommendedTxt.setTypeface(FontManager.getKtTypeface());
                    introViewHolder.recommendedTxt.setText(intro.getText());
                    RxView.clicks(introViewHolder.recommendedImg)
                            .throttleFirst(500, TimeUnit.MILLISECONDS)
                            .subscribe(o -> {
                                Context context = holder.itemView.getContext();
                                if (context instanceof SlidingActivity) {
                                    SlidingActivity slidingActivity = (SlidingActivity) context;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cid", "0");
                                    bundle.putString("title", "推荐频道");
                                    slidingActivity.openActivity(SubChannelListActivity.class, bundle, 0);
                                }
                            });
                }
                break;
            default:
                SubChannelViewHolder subChannelViewHolder = (SubChannelViewHolder) holder;
                ChannelCategory.ChannelInfo info = channelInfoList.get(getInfoListPosition(position));
                if (info != null) {
                    subChannelViewHolder.channelCategoryLayout.setChannelInfo(info);
                }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (channelNum != -1) {
            count++;
        }
        if (intro != null) {
            count++;
        }
        if (channelInfoList != null) {
            count += channelInfoList.size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                if (channelNum != -1) {
                    return 0;
                } else if (intro != null) {
                    return 1;
                } else {
                    return 2;
                }
            case 1:
                if (channelNum != -1 && intro != null) {
                    return 1;
                } else {
                    return 2;
                }
            default:
                return 2;
        }
    }

    private int getInfoListPosition(int position) {
        if (channelNum != -1) {
            position--;
        }
        if (intro != null) {
            position--;
        }
        if (position < 0) {
            position = 0;
        }
        return position;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public ChannelCategory.Intro getIntro() {
        return intro;
    }

    public void setIntro(ChannelCategory.Intro intro) {
        this.intro = intro;
    }

    public List<ChannelCategory.ChannelInfo> getChannelInfoList() {
        return channelInfoList;
    }

    public void setChannelInfoList(List<ChannelCategory.ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
        notifyItemChanged(0);
    }

    public void notifyChannelCategoryLayout() {
        String cids = new NewsChannelPreference().getNewsChannelCids();
        SubChannelAdapter.setCidArray(cids.split("\\|"));
        int itemCount = getItemCount();
        int size = channelInfoList.size();
        notifyItemRangeChanged(itemCount - size - 1, itemCount - 1);
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {

        @Bind(com.jeremy.lychee.R.id.ordered_txt)
        TextView ordered;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class IntroViewHolder extends RecyclerView.ViewHolder {

        @Bind(com.jeremy.lychee.R.id.recommended_img)
        GlideImageView recommendedImg;
        @Bind(com.jeremy.lychee.R.id.recommended_txt)
        TextView recommendedTxt;

        public IntroViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class SubChannelViewHolder extends RecyclerView.ViewHolder {

        @Bind(com.jeremy.lychee.R.id.sub_channel_layout)
        ChannelCategoryLayout channelCategoryLayout;

        public SubChannelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
