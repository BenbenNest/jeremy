package com.jeremy.lychee.adapter.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class LiveChannelsAdapter extends RecyclerView.Adapter<LiveChannelsAdapter.ViewHolder> {
    private List<ColumnChannel> list;
    private Context context;
    public  int newIndex = 0;
    public  int oldIndex = -1;
    public LiveChannelsAdapter(Context context,List<ColumnChannel> channels) {
        this.context = context;
        this.list = channels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.layout_channel_channelsitem, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.channel_logo.loadImageOriginalSize(list.get(position).getChannel_icon(), (req, v) -> req
                    .placeholder(context.getResources().getDrawable(com.jeremy.lychee.R.drawable.livechannel_item_logo))
                    .bitmapTransform(new GlideCircleTransform(context))
                    .into(v));
        if (list.get(position).isClicked()){
            holder.item_layout.setBackgroundResource(com.jeremy.lychee.R.drawable.background_live_channels_item);
            holder.channel_name.setTextColor(context.getResources().getColor(com.jeremy.lychee.R.color.livechannel_name_color));
            holder.channel_name.setText(list.get(position).getChannel_name());
            holder.channel_name.setVisibility(View.VISIBLE);
        }else {
            holder.item_layout.setBackgroundResource(0);
            holder.channel_name.setText(list.get(position).getChannel_name());
            holder.channel_name.setTextColor(context.getResources().getColor(com.jeremy.lychee.R.color.live_column_titlte));
            holder.channel_name.setVisibility(View.GONE);
        }
        holder.rootView.setOnClickListener(v -> {
                oldIndex = newIndex;
                newIndex = position;
                if (oldIndex !=newIndex){
                    list.get(position).setIsClicked(true);
                    if (list.size()>newIndex){
                        notifyItemChanged(position);
                        LiveEvent.LiveChannelClick channelClick = new LiveEvent.LiveChannelClick();
                        channelClick.positon = position;
                        channelClick.type = list.get(position).getChannel_ctype();
                        channelClick.channel_id = list.get(position).getChannel_id();
                        QEventBus.getEventBus().post(channelClick);
                    }
                    if (list.size()>oldIndex){
                        list.get(oldIndex).setIsClicked(false);
                        notifyItemChanged(oldIndex);
                    }
                }else {

                }
        });

    }

    public void setNewIndex(int position){
        oldIndex = newIndex;
        newIndex = position;
    }
    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void setChannelList(List<ColumnChannel> channelList) {
        list = channelList;
    }

    public List<ColumnChannel> getChannelList() {
        return list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.channel_logo)
        GlideImageView channel_logo;
        @Bind(com.jeremy.lychee.R.id.item_layout)
        View item_layout;
        @Bind(com.jeremy.lychee.R.id.channel_name)
        TextView channel_name;
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


