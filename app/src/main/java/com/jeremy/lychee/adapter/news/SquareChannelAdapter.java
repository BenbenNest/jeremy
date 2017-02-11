package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.activity.news.SubChannelActivity;
import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SquareChannelAdapter extends RecyclerView.Adapter<SquareChannelAdapter.VH> {

    private Context context;
    private List<SquareModel.ElementModel> featureChannelList;

    public SquareChannelAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.square_characteristic_channel_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        SquareModel.ElementModel channel = featureChannelList.get(position);
        Glide.with(context).load(ImageOptiUrl.get(channel.getIcon(), AppUtil.dip2px(context, 70), AppUtil.dip2px(context, 70)))
                .into(holder.icChannelImg);
        holder.channelNameTxt.setText(channel.getCname());
        holder.layout.setOnClickListener(v -> SubChannelActivity.StartActivity(context,
                channel.getCid(), channel.getCname(), channel.getIcon(), false));
    }

    @Override
    public int getItemCount() {
        if (featureChannelList == null) {
            return 0;
        } else {
            return featureChannelList.size();
        }
    }

    public void setFeatureChannelList(List<SquareModel.ElementModel> featureChannelList) {
        this.featureChannelList = featureChannelList;
        notifyDataSetChanged();
    }

    public static class VH extends RecyclerView.ViewHolder {

        @Bind(com.jeremy.lychee.R.id.ic_channel_img)
        ImageView icChannelImg;
        @Bind(com.jeremy.lychee.R.id.channel_name_txt)
        TextView channelNameTxt;
        @Bind(com.jeremy.lychee.R.id.layout)
        View layout;

        public VH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
