package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.utils.DateUtils;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class NormalNews1VHTemplate extends ContentVHTemplate<NewsListData> {

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new NormalNewsViewVH(view);
    }

    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
        NormalNewsViewVH normalNewsViewVH = (NormalNewsViewVH) viewHolder;
        bindDate(data, normalNewsViewVH);
    }

    private void bindDate(NewsListData data, NormalNewsViewVH normalNewsViewVH) {
        normalNewsViewVH.newsImg.loadImage(data.getAlbum_pic());
        normalNewsViewVH.newsTitle.setText(data.getTitle());
        String news_type = data.getNews_type();
        if ("3".equals(news_type)) {
            try {
                JSONObject jsonObject = new JSONObject(data.getNews_data());
                normalNewsViewVH.newsTypeTxt.setText(jsonObject.getString("video_duration"));
                normalNewsViewVH.newsTypeTxt.setVisibility(View.VISIBLE);
                normalNewsViewVH.newsTypeTxt.setCompoundDrawablesWithIntrinsicBounds(com.jeremy.lychee.R.mipmap.ic_video_tpye_small,
                        0, 0, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if ("4".equals(news_type)) {
            normalNewsViewVH.newsTypeTxt.setVisibility(View.VISIBLE);
            normalNewsViewVH.newsTypeTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            normalNewsViewVH.newsTypeTxt.setText("直播");
        }else if ("5".equals(news_type)) {
            normalNewsViewVH.newsTypeTxt.setVisibility(View.VISIBLE);
            normalNewsViewVH.newsTypeTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            normalNewsViewVH.newsTypeTxt.setText("专题");
        } else {
            normalNewsViewVH.newsTypeTxt.setVisibility(View.GONE);
        }
        String source = data.getSource();
        normalNewsViewVH.newsSourceTxt.setVisibility(TextUtils.isEmpty(source) ? View.GONE : View.VISIBLE);
        normalNewsViewVH.newsSourceTxt.setText(source);
        String comment = data.getComment();
        normalNewsViewVH.delImg.setClickable(true);
        setDelListen(normalNewsViewVH.delImg, data);
        normalNewsViewVH.commentNumTxt.setText(comment + "评");
        if ("0".equals(comment)) {
            normalNewsViewVH.commentNumTxt.setVisibility(View.GONE);
        } else {
            normalNewsViewVH.commentNumTxt.setVisibility(View.VISIBLE);
        }

        if (data.getTime() != null) {
            if ((System.currentTimeMillis() - data.getTime()) > 86400000) {
                normalNewsViewVH.updateTime.setVisibility(View.GONE);
            } else {
                normalNewsViewVH.updateTime.setVisibility(View.VISIBLE);
                try {
                    normalNewsViewVH.updateTime.setText(DateUtils.formatTime(data.getTime()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } else if (!TextUtils.isEmpty(data.getPdate())){
            normalNewsViewVH.updateTime.setText(DateUtils.formatTime(data.getPdate() + "000"));
        }

//        if (news_type.equals("4")) {
//            normalNewsViewVH.newsTypeImg.setVisibility(View.VISIBLE);
//            normalNewsViewVH.videoTimeTv.setVisibility(View.GONE);
//        } else if (news_type.equals("3")) {
//            normalNewsViewVH.newsTypeImg.setVisibility(View.GONE);
//            normalNewsViewVH.videoTimeTv.setVisibility(View.VISIBLE);
//            if (!TextUtils.isEmpty(data.getNews_data())) {
//                try {
//                    JSONObject jsonObject = new JSONObject(data.getNews_data());
//                    normalNewsViewVH.videoTimeTv.setText(jsonObject.getString("video_duration"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } else {
//            normalNewsViewVH.videoTimeTv.setVisibility(View.GONE);
//            normalNewsViewVH.newsTypeImg.setVisibility(View.GONE);
//        }

    }

    public static class NormalNewsViewVH extends RecyclerView.ViewHolder {

        GlideImageView newsImg;
        TextView newsTitle;
        TextView updateTime;
        TextView newsSourceTxt;
        TextView commentNumTxt;
        ImageView delImg;
        TextView newsTypeTxt;

        public NormalNewsViewVH(View itemView) {
            super(itemView);
            newsImg = (GlideImageView) itemView.findViewById(com.jeremy.lychee.R.id.news_img);
            newsTitle = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.title_txt);
            updateTime = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.update_time_txt);
            newsSourceTxt = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.news_source_txt);
            commentNumTxt = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.comment_num_txt);
            delImg = (ImageView) itemView.findViewById(com.jeremy.lychee.R.id.del_img);
            newsTypeTxt = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.news_type_txt);
        }
    }

    private int getLayoutId() {
//        switch (itemType) {
//            case 4:
//                return R.layout.rv_normal_news_level4;
//            case 3:
//                return R.layout.rv_normal_news_level3;
//            case 0:
//                return R.layout.rv_normal_news_level0;
//            case 1:
//                return R.layout.rv_normal_news_level1;
//            case 2:
//            default:
//                return R.layout.rv_normal_news_level2;
//        }
        switch (itemType) {
            case 1:
                return com.jeremy.lychee.R.layout.rv_normal_news2;
            case 0:
            default:
                return com.jeremy.lychee.R.layout.rv_normal_news1;

        }
    }
}
