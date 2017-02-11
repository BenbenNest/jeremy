//package com.qihoo.lianxian.adapter.news;
//
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import R;
//import NewsListData;
//import CustomRatioImageView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class NormalNewsVHTemplate extends ContentVHTemplate<NewsListData> {
//    @Override
//    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
//        return new NormalNewsViewVH(view);
//    }
//
//    @Override
//    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
//        NormalNewsViewVH normalNewsViewVH = (NormalNewsViewVH) viewHolder;
//        normalNewsViewVH.image.loadImage(data.getAlbum_pic());
//        normalNewsViewVH.title.setText(data.getTitle());
//        String news_type = data.getNews_type();
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
//        normalNewsViewVH.delImg.setClickable(true);
//        setDelListen(normalNewsViewVH.delImg, data);
//    }
//
//    public static class NormalNewsViewVH extends RecyclerView.ViewHolder {
//
//        @Bind(R.id.title)
//        TextView title;
//        @Bind(R.id.image)
//        CustomRatioImageView image;
//        @Bind(R.id.news_type_img)
//        ImageView newsTypeImg;
//        @Bind(R.id.del_img)
//        ImageView delImg;
//        @Bind(R.id.video_time_tv)
//        TextView videoTimeTv;
//
//        public NormalNewsViewVH(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//
//    private int getLayoutId() {
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
//    }
//}
