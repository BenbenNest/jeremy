//package com.qihoo.lianxian.adapter.news;
//
//import android.graphics.Typeface;
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
//public class ImageNewsVH2Template extends ContentVHTemplate<NewsListData> {
//    @Override
//    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
//        return new ImageNewsType2VH(view);
//    }
//
//    @Override
//    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, NewsListData data) {
//        if (data == null) {
//            return;
//        }
//        String newsData1 = data.getNews_data();
//        ImageNewsType2VH imageNewsType2VH = (ImageNewsType2VH) viewHolder;
//        imageNewsType2VH.titleTxt.setText(data.getTitle());
//        ImageNewsDataModel imageNews1DataModel1 = null;
//        try {
//            Gson gson1 = new Gson();
//            imageNews1DataModel1 = gson1.fromJson(newsData1, ImageNewsDataModel.class);
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//        imageNewsType2VH.imgCountTxt.setText(String.valueOf(imageNews1DataModel1.getCount()));
//        List<String> pics1 = imageNews1DataModel1.getPics();
//        if (pics1.size() > 0) {
//            imageNewsType2VH.img1.loadImage(pics1.get(0));
//        }
//        if (pics1.size() > 1) {
//            imageNewsType2VH.img2.loadImage(pics1.get(1));
//        }
//        if (pics1.size() > 2) {
//            imageNewsType2VH.img3.loadImage(pics1.get(2));
//        }
//        setDelListen(imageNewsType2VH.delImg, data);
//    }
//
//    public static class ImageNewsType2VH extends RecyclerView.ViewHolder {
//        @Bind(R.id.title_txt)
//        TextView titleTxt;
//        @Bind(R.id.img_1)
//        GlideImageView img1;
//        @Bind(R.id.img_2)
//        GlideImageView img2;
//        @Bind(R.id.img_3)
//        GlideImageView img3;
//        @Bind(R.id.img_count_txt)
//        TextView imgCountTxt;
//        @Bind(R.id.del_img)
//        ImageView delImg;
//
//        public ImageNewsType2VH(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            imgCountTxt.setTypeface(null, Typeface.BOLD);
//        }
//    }
//
//    private int getLayoutId() {
//        switch (itemType) {
//            case 0:
//                return R.layout.rv_content_pic2_news_level0;
//            case 1:
//                return R.layout.rv_content_pic2_news_level1;
//            case 3:
//                return R.layout.rv_content_pic2_news_level3;
//            case 4:
//                return R.layout.rv_content_pic2_news_level4;
//            case 2:
//            default:
//                return R.layout.rv_content_pic2_news_level2;
//        }
//    }
//}
