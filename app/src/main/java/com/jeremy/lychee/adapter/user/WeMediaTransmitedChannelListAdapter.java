package com.jeremy.lychee.adapter.user;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.adapter.ListLoadHolder;
import com.jeremy.lychee.adapter.UltimateViewAdapter;
import com.jeremy.lychee.model.news.TransmitedChannel;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.List;

public class WeMediaTransmitedChannelListAdapter extends UltimateViewAdapter {
    private List<TransmitedChannel> mList;
    private Activity mActivity;
    private ListLoadHolder mLoadHolder = null;

    public WeMediaTransmitedChannelListAdapter(Activity activity, List<TransmitedChannel> list) {
        this.mActivity = activity;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
        RecyclerView.ViewHolder g = new InnerViewHolder(view, true);
        return g;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_transmited_channel_list, parent, false);
        return new InnerViewHolder(view, true);
    }

    public List<TransmitedChannel> getmList() {
        return mList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (VIEW_TYPES.HEADER == getItemViewType(position)) {
            onBindHeaderViewHolder(viewHolder, position);
        } else if (VIEW_TYPES.NORMAL == getItemViewType(position)) {
            InnerViewHolder holder = (InnerViewHolder) viewHolder;
            TransmitedChannel channel = mList.get(position);
            holder.articleNum.setText("推荐过" + channel.getChannelInfo().getNews_num() + "篇文章");
            holder.readerNum.setText(channel.getChannelInfo().getSub_num() + "名读者");
            holder.channelName.setText(channel.getChannelInfo().getName());
            if (!TextUtils.isEmpty(channel.getChannelInfo().getIcon())) {
                holder.channelIcon.loadImage(channel.getChannelInfo().getIcon(), (req, v) -> req
                        .placeholder(AppUtil.getDefaultCircleIcon(mActivity))
                        .bitmapTransform(new GlideCircleTransform(mActivity))
                        .into(v));
            }
            holder.clickable.setOnClickListener(v -> WeMediaChannelActivity.startActivity(
                    mActivity,  channel.getChannelInfo().getC_id()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new InnerViewHolder(parent, true);
    }

    public void insert(TransmitedChannel channel, int position) {
        insert(mList, channel, position);
    }

    @Deprecated
    public void insert(List<TransmitedChannel> list, int postion) {
        if (list == null || list.size() <= 0) {
            return;
        }
        if (this.mList == null || this.mList.size() == 0) {
            //当第一次拉取数据条目小于2时，显示没有更多数据了
            if (list.size() <= 2) {
                showNoDataView();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            insert(mList, mList.get(i), postion);
        }
    }

    public void remove(int position) {
        remove(mList, position);
    }

    public void clear() {
        clear(mList);
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void showErrorView() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.moreLayout.setVisibility(View.GONE);
        mLoadHolder.errorLayout.setVisibility(View.VISIBLE);
        mLoadHolder.nodataLayout.setVisibility(View.GONE);
    }

    public void showNoDataView() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.moreLayout.setVisibility(View.GONE);
        mLoadHolder.errorLayout.setVisibility(View.GONE);
        mLoadHolder.nodataLayout.setVisibility(View.VISIBLE);
    }

    public Boolean isLoadNodata() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        return false;
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {
        public GlideImageView channelIcon;
        public TextView channelName;
        public TextView articleNum;
        public TextView readerNum;
        public View clickable;

        public InnerViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                channelIcon = (GlideImageView) itemView.findViewById(R.id.channel_icon);
                channelName = (TextView) itemView.findViewById(R.id.channel_name);
                articleNum = (TextView) itemView.findViewById(R.id.article_num);
                readerNum = (TextView) itemView.findViewById(R.id.reader_num);
                clickable = itemView.findViewById(R.id.clickable);
            }
        }

    }
}
