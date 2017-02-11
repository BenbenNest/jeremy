package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.customview.news.SquareArticleView;
import com.jeremy.lychee.customview.news.SquareChannelView;
import com.jeremy.lychee.widget.recyclerview.BaseRVAdapter;

import java.util.List;

public class SquareAdapter extends BaseRVAdapter {

    private Context mContext;
    private List<SquareModel.ModularModel> mSquareModelList;
    private static final int channel_type = 0;
    private static final int article_type = 1;

    public SquareAdapter(Context mContext, List<SquareModel.ModularModel> mSquareModelList) {
        this.mContext = mContext;
        this.mSquareModelList = mSquareModelList;
    }

    @Override
    public int getContentItemCount() {
        if (mSquareModelList == null) {
            return 0;
        } else {
            return mSquareModelList.size();
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        switch (contentViewType) {
            case channel_type:
                return new SquareChannelVH(new SquareChannelView(parent.getContext()));
            default:
                return new SquareArticleVH(new SquareArticleView(mContext));
        }
    }

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        SquareModel.ModularModel modularNewsList = mSquareModelList.get(position);
        int type = getContentItemViewType(position);
        switch (type) {
            case channel_type:
                SquareChannelVH squareChannelVH = (SquareChannelVH) contentViewHolder;
                squareChannelVH.squareChannelView.setTitle(modularNewsList.getName());
                squareChannelVH.squareChannelView.setFeatureList(modularNewsList.getData());
                break;
            default:
                SquareArticleVH squareArticleVH = (SquareArticleVH) contentViewHolder;
                squareArticleVH.submodularView.setModularNewsList(modularNewsList);
                break;
        }


    }

    @Override
    protected int getContentItemViewType(int position) {
        SquareModel.ModularModel modularModel = mSquareModelList.get(position);
        if (modularModel.getModule().equals("2")) {
            return channel_type;
        } else {
            return article_type;
        }
    }

    public void setDataList(List<SquareModel.ModularModel> modelList) {
        if (mSquareModelList != null) {
            mSquareModelList.clear();
        }
        mSquareModelList = modelList;
        notifyDataSetChanged();
    }

    public void insertList(List<SquareModel.ModularModel> subModelList) {
        if (subModelList == null) {
            return;
        }
        if (mSquareModelList == null) {
            mSquareModelList = subModelList;
            notifyDataSetChanged();
            return;
        }
        int position = mSquareModelList.size();
        mSquareModelList.addAll(subModelList);
        notifyContentItemRangeInserted(position, subModelList.size());
    }

    static class SquareArticleVH extends RecyclerView.ViewHolder {
        SquareArticleView submodularView;

        public SquareArticleVH(View itemView) {
            super(itemView);
            submodularView = (SquareArticleView) itemView;
        }
    }

    static class SquareChannelVH extends RecyclerView.ViewHolder {
        SquareChannelView squareChannelView;

        public SquareChannelVH(View itemView) {
            super(itemView);
            squareChannelView = (SquareChannelView) itemView;
        }
    }
}
