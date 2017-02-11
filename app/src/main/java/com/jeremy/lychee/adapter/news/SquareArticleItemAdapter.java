package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.model.ModolHelper;
import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.presenter.ChannelFragmentPresenter;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SquareArticleItemAdapter extends RecyclerView.Adapter<SquareArticleItemAdapter.SubmodularViewHolder> {

    private Context context;
    private List<SquareModel.ElementModel> list;

    public SquareArticleItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public SubmodularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.square_article_item, parent, false);
        return new SubmodularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubmodularViewHolder holder, int position) {
        SquareModel.ElementModel item = list.get(position);
        holder.thumbnail.loadImage(item.getAlbum_pic());
        holder.name.setText(item.getTitle());
        holder.layout.setOnClickListener(v -> {
            if (context instanceof SlidingActivity ) {
                ChannelFragmentPresenter.OpenNewsDetailActivity(
                        (SlidingActivity) context, ModolHelper.SquareElementModelToNewsListData(item), null, null);
            }
        });
    }

    public void setList(List<SquareModel.ElementModel> news) {
        this.list = news;
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public static class SubmodularViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.name_txt)
        TextView name;
        @Bind(com.jeremy.lychee.R.id.thumbnail_img)
        GlideImageView thumbnail;
        @Bind(com.jeremy.lychee.R.id.layout)
        View layout;
        public SubmodularViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
