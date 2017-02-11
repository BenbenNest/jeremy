package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.model.user.HotWeMediaChannel;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@Deprecated
public class WeMediaHotChannelAvatarListAdapter extends RecyclerView.Adapter<WeMediaHotChannelAvatarListAdapter.InnerViewHolder> {

    private Context mContext;

    public void setDatas(List<HotWeMediaChannel> datas) {
        this.mDatas = datas;
    }

    public List<HotWeMediaChannel> getDatas() {
        return mDatas;
    }

    private List<HotWeMediaChannel> mDatas = null;

    public WeMediaHotChannelAvatarListAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InnerViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.wemedia_hot_channel_avatar_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InnerViewHolder holder, int position) {
        if (position == 0) {
            holder.container.setPadding(AppUtil.dip2px(mContext, 5), 0, 0, 0);
        } else if (position == getItemCount() - 1) {
            holder.container.setPadding(0, 0, AppUtil.dip2px(mContext, 5), 0);
        } else {
            holder.container.setPadding(0, 0, 0, 0);
        }
        holder.imageView.
                loadImage(mDatas.get(position).getIcon(), (req, v) -> req
                        .placeholder(AppUtil.getDefaultCircleIcon(mContext))
                        .bitmapTransform(new GlideCircleTransform(mContext))
                        .into(v));
        holder.imageView.setOnClickListener(v ->
                        WeMediaChannelActivity.startActivity(
                                mContext, Integer.toString(mDatas.get(position).getC_id()))
        );
        holder.textView.setText(mDatas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    static class InnerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.container)
        ViewGroup container;
        @Bind(R.id.hot_channel_icon)
        GlideImageView imageView;
        @Bind(R.id.hot_channel_name)
        TextView textView;

        public InnerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
