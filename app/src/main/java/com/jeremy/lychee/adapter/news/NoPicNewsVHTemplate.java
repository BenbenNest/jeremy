package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoPicNewsVHTemplate extends ContentVHTemplate<NewsListData> {
    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new NoPicNewsVH(view);
    }

    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
        if (data == null) {
            return;
        }
        NoPicNewsVH noPicNewsVH = (NoPicNewsVH) viewHolder;
        noPicNewsVH.title.setText(data.getTitle());
        try {
            JSONObject jsonObject = new JSONObject(data.getNews_data());
            noPicNewsVH.contentTxt.setText(jsonObject.getString("summary"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setDelListen(noPicNewsVH.delImg, data);
    }

    public static class NoPicNewsVH extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.content_txt)
        TextView contentTxt;
        @Bind(R.id.del_img)
        ImageView delImg;

        public NoPicNewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    private int getLayoutId() {
        switch (itemType) {
            case 3:
                return R.layout.rv_content_no_pic_news_level3;
            case 4:
                return R.layout.rv_content_no_pic_news_level4;
            case 0:
                return R.layout.rv_content_no_pic_news_level0;
            case 1:
                return R.layout.rv_content_no_pic_news_level1;
            case 2:
            default:
                return R.layout.rv_content_no_pic_news_level2;
        }
    }

}
