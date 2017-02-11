package com.jeremy.lychee.customview.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaCreateAlbumActivity;
import com.jeremy.lychee.db.WeMediaChannel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangp on 15-11-25.
 */
public class AlbumSelectRecyclerView extends RecyclerView {

    public AlbumSelectRecyclerView(Context context) {
        super(context);
    }

    public AlbumSelectRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumSelectRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private RecyclerView.Adapter mAdapter = new InnerAdapter();
    private List<WeMediaChannel> mDisplayChannels;
    private String clickedWeMediaChannelID = "";

    public String getSubmitChannel() {
        return clickedWeMediaChannelID;
    }

    public void showAlbums(List<WeMediaChannel> list) {
        if (list == null) return;
        mDisplayChannels = handleData(list);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private List<WeMediaChannel> handleData(List<WeMediaChannel> list) {
        WeMediaChannel d = new WeMediaChannel();
        d.setName("创建新的专辑");
        d.setIcon(null);
        /*if (list!=null&&list.size()>0){
            clickedWeMediaChannelID = list.get(0).getId();
        }*/

        list.add(d);

        return list;
    }

    public void setDefaultSelect(String select) {
        clickedWeMediaChannelID = select;
    }

    class InnerAdapter extends RecyclerView.Adapter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InnerViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.transmit_channel_select_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            InnerViewHolder viewHolder = (InnerViewHolder) holder;
            if (mDisplayChannels.get(position).getId() != null
                    && mDisplayChannels.get(position).getId().equals(clickedWeMediaChannelID)) {
                viewHolder.img_.setImageResource(R.mipmap.wenz_icon_xuanz_pressed);
            } else {
                viewHolder.img_.setImageResource(R.mipmap.wenz_icon_wxuanz_default);
            }
            if (mDisplayChannels.size() - 1 == position) {
                viewHolder.img_.setImageResource(R.mipmap.wenz_icon_jiahd_default);
            }
            viewHolder.txt_.setText(mDisplayChannels.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mDisplayChannels.size();
        }
    }

    class InnerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        @Bind(R.id.transmit_channel_checkbox_icon)
        ImageView img_;
        @Bind(R.id.transmit_channel_name)
        TextView txt_;

        InnerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getPosition() == mDisplayChannels.size() - 1) {
                //添加频道
                getContext().startActivity(new Intent(getContext(), WeMediaCreateAlbumActivity.class));
            } else {
                if (clickedWeMediaChannelID.equals(mDisplayChannels.get(getPosition()).getId())) {
                    clickedWeMediaChannelID = "";
                } else {
                    clickedWeMediaChannelID = mDisplayChannels.get(getPosition()).getId();
                }
                mAdapter.notifyDataSetChanged();
            }

        }
    }

}
