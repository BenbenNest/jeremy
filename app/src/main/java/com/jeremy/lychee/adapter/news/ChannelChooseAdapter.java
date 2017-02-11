package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.widget.GlideTransforms.GlideRoundRectTransform;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChannelChooseAdapter extends RecyclerView.Adapter<ChannelChooseAdapter.ViewHolder> {

    private List<NewsChannel> newsChannelList;
    private Context context;
    private int checkedIndex = -1;

    public ChannelChooseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.channel_choose_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsChannel bean = newsChannelList.get(position);
        Glide.with(context)
                .load(ImageOptiUrl.get(bean.getIcon(), AppUtil.dip2px(context, 70),AppUtil.dip2px(context, 70)))
                .placeholder(AppUtil.getDefaultSquareIcon(context))
                .bitmapTransform(new GlideRoundRectTransform(context))
                .into(holder.channelIcon);
        holder.channelName.setText(bean.getCname());
        holder.channelName.setSelected(position == checkedIndex);
        holder.rootView.setOnClickListener(view -> QEventBus.getEventBus().post(new Events.GoToChannelByIndex(position)));
    }

    @Override
    public int getItemCount() {
        if (newsChannelList != null) {
            return newsChannelList.size();
        } else {
            return 0;
        }
    }

    public void setNewsChannelList(List<NewsChannel> newsChannelList) {
        this.newsChannelList = newsChannelList;
    }

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.channel_icon)
        ImageView channelIcon;
        @Bind(com.jeremy.lychee.R.id.channel_name)
        TextView channelName;
        View rootView;

        // each data item is just a string in this case
        public ViewHolder(View view) {
            super(view);
            rootView = view;
            rootView.setClickable(true);
            ButterKnife.bind(this, view);
        }
    }
}
