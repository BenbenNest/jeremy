package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

public class ExFocusNewsVHTemplate extends ContentVHTemplate<NewsListData> {

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new FocusNewsVH(view);
    }


    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
        FocusNewsVH focusNewsVH = (FocusNewsVH) viewHolder;
        bindData(focusNewsVH, data);
    }

    private void bindData(FocusNewsVH focusNewsVH, NewsListData data) {
        if (data == null) {
            return;
        }
        focusNewsVH.titleTxt.setText(data.getTitle());
        switch (itemType) {
            case 4:
                setDelListen(focusNewsVH.delImg, data);
                break;
            default:
                focusNewsVH.focusNewsImg.loadImage(data.getAlbum_pic(), (req, v) -> req
                        .centerCrop()
                        .placeholder(v.getContext().getResources().getDrawable(com.jeremy.lychee.R.drawable.placeholder_4_3))
                        .into(v));
        }
    }

    private int getLayoutId() {
//        switch (itemType) {
//            case 4:
//                return R.layout.ex_focus_news_level4;
//            default:
                return com.jeremy.lychee.R.layout.ex_focus_news;
//        }
    }

    public static class FocusNewsVH extends RecyclerView.ViewHolder {
        GlideImageView focusNewsImg;
        TextView titleTxt;
        ImageView delImg;

        public FocusNewsVH(View itemView) {
            super(itemView);
            focusNewsImg = (GlideImageView) itemView.findViewById(com.jeremy.lychee.R.id.focus_news_img);
            titleTxt = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.title_txt);
            delImg = (ImageView) itemView.findViewById(com.jeremy.lychee.R.id.del_img);
        }
    }
}
