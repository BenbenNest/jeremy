package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.news.ImageNewsDataModel;
import com.jeremy.lychee.utils.DateUtils;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageNews1VHTemplate extends ContentVHTemplate<NewsListData> {
    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new ImageNewsType1VH(view);
    }

    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
        if (data == null) {
            return;
        }
        String newsData = data.getNews_data();
        ImageNewsType1VH imageNewsType1VH = (ImageNewsType1VH) viewHolder;
        imageNewsType1VH.newsTitleTxt.setText(data.getTitle());
        imageNewsType1VH.commentNumTxt.setText(data.getComment() + "评");
        String source = data.getSource();
        imageNewsType1VH.newsSourceTxt.setText(source);
        imageNewsType1VH.newsSourceTxt.setVisibility(TextUtils.isEmpty(source) ? View.GONE : View.VISIBLE);
        imageNewsType1VH.updateTimeTxt.setText(DateUtils.formatTime(data.getTime()));
        Gson gson = new Gson();
        ImageNewsDataModel imageNewsDataModel = null;
        try {
            imageNewsDataModel = gson.fromJson(newsData, ImageNewsDataModel.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return;
        }
        List<String> pics = imageNewsDataModel.getPics();
        if (pics.size() > 0 && (imageNewsType1VH.img1.getVisibility() == View.VISIBLE)) {
            imageNewsType1VH.img1.loadImage(pics.get(0));
        }
        if (pics.size() > 1 && (imageNewsType1VH.img2.getVisibility() == View.VISIBLE)) {
            imageNewsType1VH.img2.loadImage(pics.get(1));
        }
        if (pics.size() > 2 && (imageNewsType1VH.img3.getVisibility() == View.VISIBLE)) {
            imageNewsType1VH.img3.loadImage(pics.get(2));
        }
        imageNewsType1VH.delImg.setClickable(true);
        setDelListen(imageNewsType1VH.delImg, data);
        String comment = data.getComment();
        if ("0".equals(comment)) {
            imageNewsType1VH.commentNumTxt.setVisibility(View.GONE);
        } else {
            imageNewsType1VH.commentNumTxt.setVisibility(View.VISIBLE);
        }

        if ((System.currentTimeMillis() - data.getTime()) > 86400000) {
            imageNewsType1VH.updateTimeTxt.setVisibility(View.GONE);
        } else {
            imageNewsType1VH.updateTimeTxt.setVisibility(View.VISIBLE);
            try {
                imageNewsType1VH.updateTimeTxt.setText(DateUtils.formatTime(data.getTime()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ImageNewsType1VH extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.news_title_txt)
        TextView newsTitleTxt;
        @Bind(com.jeremy.lychee.R.id.img_1)
        GlideImageView img1;
        @Bind(com.jeremy.lychee.R.id.img_2)
        GlideImageView img2;
        @Bind(com.jeremy.lychee.R.id.img_3)
        GlideImageView img3;
        @Bind(com.jeremy.lychee.R.id.news_source_txt)
        TextView newsSourceTxt;
        @Bind(com.jeremy.lychee.R.id.update_time_txt)
        TextView updateTimeTxt;
        @Bind(com.jeremy.lychee.R.id.comment_num_txt)
        TextView commentNumTxt;
        @Bind(com.jeremy.lychee.R.id.del_img)
        ImageView delImg;
        @Bind(com.jeremy.lychee.R.id.content_layout)
        RelativeLayout contentLayout;

        public ImageNewsType1VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adjustByItemType();
        }

        private void adjustByItemType() {

        }
    }

    private int getLayoutId() {
        switch (itemType) {
            case 0:
                return com.jeremy.lychee.R.layout.rv_content_pic_news1;
            case 1:
            default:
                return com.jeremy.lychee.R.layout.rv_content_pic_news2;
        }
    }

}
