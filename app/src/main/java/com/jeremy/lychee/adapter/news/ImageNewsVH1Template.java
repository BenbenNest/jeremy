//package com.qihoo.lianxian.adapter.news;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import R;
//import NewsListData;
//import ImageNewsDataModel;
//import AppUtil;
//import DateUtils;
//import GlideImageView;
//
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class ImageNewsVH1Template extends ContentVHTemplate<NewsListData> {
//    @Override
//    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
//        return new ImageNewsType1VH(view);
//    }
//
//    @Override
//    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
//        if (data == null) {
//            return;
//        }
//        String newsData = data.getNews_data();
//        ImageNewsType1VH imageNewsType1VH = (ImageNewsType1VH) viewHolder;
//        imageNewsType1VH.titleTxt.setText(data.getTitle());
//        Gson gson = new Gson();
//        ImageNewsDataModel imageNewsDataModel = null;
//        try {
//            imageNewsDataModel = gson.fromJson(newsData, ImageNewsDataModel.class);
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//        List<String> pics = imageNewsDataModel.getPics();
//        if (pics.size() > 0 && (imageNewsType1VH.img1.getVisibility() == View.VISIBLE)) {
//            imageNewsType1VH.img1.loadImage(pics.get(0));
//        }
//        if (pics.size() > 1 && (imageNewsType1VH.img2.getVisibility() == View.VISIBLE)) {
//            imageNewsType1VH.img2.loadImage(pics.get(1));
//        }
//        if (pics.size() > 2 && (imageNewsType1VH.img3.getVisibility() == View.VISIBLE)) {
//            imageNewsType1VH.img3.loadImage(pics.get(2));
//        }
//        setDelListen(imageNewsType1VH.delImg, data);
//    }
//
//    public static class ImageNewsType1VH extends RecyclerView.ViewHolder {
//        @Bind(R.id.title_txt)
//        TextView titleTxt;
//        @Bind(R.id.img_1)
//        GlideImageView img1;
//        @Bind(R.id.img_2)
//        GlideImageView img2;
//        @Bind(R.id.img_3)
//        GlideImageView img3;
//        @Bind(R.id.del_img)
//        ImageView delImg;
//
//        public ImageNewsType1VH(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            adjustByItemType();
//        }
//
//        private void adjustByItemType() {
//
//        }
//    }
//
//    private int getLayoutId() {
//        switch (itemType) {
//            case 0:
//                return R.layout.rv_content_pic1_news_level0;
//            case 1:
//                return R.layout.rv_content_pic1_news_level1;
//            case 3:
//                return R.layout.rv_content_pic1_news_level3;
//            case 4:
//                return R.layout.rv_content_pic1_news_level4;
//            case 2:
//            default:
//                return R.layout.rv_content_pic1_news_level2;
//        }
//    }
//
//}
