package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.utils.DateUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoPicNews1VHTemplate extends ContentVHTemplate<NewsListData> {
    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new NoPicNewsVH(view);
    }

    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
        if (data == null) {
            return;
        }
        NoPicNewsVH noPicNewsVH = (NoPicNewsVH) viewHolder;
        noPicNewsVH.newsTitle.setText(data.getTitle());
        try {
            noPicNewsVH.updateTime.setText(DateUtils.formatTime(data.getTime()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String source = data.getSource();
        noPicNewsVH.newsSourceTxt.setVisibility(TextUtils.isEmpty(source) ? View.GONE : View.VISIBLE);
        noPicNewsVH.newsSourceTxt.setText(source);
        String comment = data.getComment();
        noPicNewsVH.commentNumTxt.setText(comment+"评");
    }

    public static class NoPicNewsVH extends RecyclerView.ViewHolder {


        @Bind(com.jeremy.lychee.R.id.title_txt)
        TextView newsTitle;
        @Bind(com.jeremy.lychee.R.id.update_time_txt)
        TextView updateTime;
        @Bind(com.jeremy.lychee.R.id.news_source_txt)
        TextView newsSourceTxt;
        @Bind(com.jeremy.lychee.R.id.comment_num_txt)
        TextView commentNumTxt;

        public NoPicNewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private int getLayoutId() {
//        switch (itemType) {
//            case 3:
//                return R.layout.rv_content_no_pic_news_level3;
//            case 4:
//                return R.layout.rv_content_no_pic_news_level4;
//            case 0:
//                return R.layout.rv_content_no_pic_news_level0;
//            case 1:
//                return R.layout.rv_content_no_pic_news_level1;
//            case 2:
//            default:
//                return R.layout.rv_content_no_pic_news_level2;
//        }
        return com.jeremy.lychee.R.layout.rv_content_no_pic_news1;
    }

}
