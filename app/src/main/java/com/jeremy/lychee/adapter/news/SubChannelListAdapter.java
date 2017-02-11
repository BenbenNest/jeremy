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
import com.jeremy.lychee.manager.FontManager;
import com.jeremy.lychee.model.news.ChannelCategory;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.utils.ImageOptiUrl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubChannelListAdapter extends RecyclerView.Adapter<SubChannelListAdapter.ChannelItemViewHolder> {

    private Context context;
    private List<ChannelCategory.ChannelInfo.SubChannel> newsChannelList;
//    private static Typeface typeFace = Typeface.createFromAsset(ContentApplication.getInstance().getApplicationContext().getAssets(),
//            "fonts/fzktjt.ttf");
    private String[] cids;

    public SubChannelListAdapter(Context context, List<ChannelCategory.ChannelInfo.SubChannel> newsChannelList) {
        this.context = context;
        this.newsChannelList = newsChannelList;
        getCids();
    }

    @Override
    public ChannelItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.sub_channel_list, parent, false);
        return new ChannelItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelItemViewHolder holder, int position) {
        ChannelCategory.ChannelInfo.SubChannel newsChannel = newsChannelList.get(position);
        Glide.with(context).load(ImageOptiUrl.get(newsChannel.getRec_img(), holder.channelIcon)).into(holder.channelIcon);
        holder.channelNameTxt.setText(newsChannel.getCname());
        holder.channelNameTxt.setTypeface(FontManager.getKtTypeface());
        boolean flag = false;
        for (String cid : cids) {
            if (cid.equals(newsChannel.getCid())) {
                flag = true;
                break;
            }
        }
        holder.orderedTxt.setVisibility(flag ? View.VISIBLE : View.GONE);
        holder.channelIcon.setOnClickListener(view -> SubChannelActivity.StartActivity(context,
                newsChannel.getCid(), newsChannel.getCname(), newsChannel.getIcon()));
    }

    @Override
    public int getItemCount() {
        if (newsChannelList == null) {
            return 0;
        } else {
            return newsChannelList.size();
        }
    }

    public void getCids() {
        String newsChannelCids = new NewsChannelPreference().getNewsChannelCids();
        cids = newsChannelCids.split("\\|");
    }

    public void updateCids() {
        getCids();
        notifyDataSetChanged();
    }

    public static class ChannelItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(com.jeremy.lychee.R.id.channel_icon)
        ImageView channelIcon;
        @Bind(com.jeremy.lychee.R.id.channel_name_txt)
        TextView channelNameTxt;
        @Bind(com.jeremy.lychee.R.id.ordered_txt)
        TextView orderedTxt;

        public ChannelItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
