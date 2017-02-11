package com.jeremy.lychee.adapter.user;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.activity.user.WeMediaAlbumDetailActivity;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.db.WeMediaChannel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangp on 16-1-13.
 */
public class WeMediaChannelAlbumListAdapter extends RecyclerView.Adapter {

    private List<WeMediaChannel> mData = new ArrayList<>(10);
    private Context context;

    public WeMediaChannelAlbumListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InnerViewHolder(((Activity) context).
                getLayoutInflater().inflate(com.jeremy.lychee.R.layout.wemedia_album_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InnerViewHolder h = (InnerViewHolder)holder;
        h.albumName_.setText(mData.get(position).getName());
        h.articleCount_.setText(buildArticleCountText(mData.get(position).getNews_num()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<WeMediaChannel> data) {
        mData = data;
    }

    public List<WeMediaChannel> getData() {
        return mData;
    }

    @Deprecated
    private CharSequence buildReaderCountText(int val) {
        StringBuilder sb = new StringBuilder();
        return sb.append(Integer.toString(val)).append("名读者");
    }

    private CharSequence buildArticleCountText(int val) {
        StringBuilder sb = new StringBuilder();
        return sb.append(Integer.toString(val)).append("篇文章");
    }

    class InnerViewHolder extends RecyclerView.ViewHolder{
        @Bind(com.jeremy.lychee.R.id.album_name)
        TextView albumName_;
        @Bind(com.jeremy.lychee.R.id.article_count)
        TextView articleCount_;

        public InnerViewHolder(View view) {
            super(view);
            view.setOnClickListener(v -> {
                WeMediaChannel album = mData.get(getPosition());
                try{
                    WeMediaChannelActivity activity = (WeMediaChannelActivity)context;
                    WeMediaAlbumDetailActivity.startActivity(
                            context, album.getId(), album.getName(), activity.getWeMediaInfo().getIs_my());
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            });
            ButterKnife.bind(this, view);
        }
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(WeMediaChannel data) {
        mData.add(data);
    }

    public void addAllDatas(List<WeMediaChannel> data) {
        for (WeMediaChannel d : data) {
            mData.add(d);
        }
    }
}
