//package com.qihoo.lianxian.adapter.user;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import R;
//import WeMediaTopicDetailActivity;
//import WeMediaTopic;
//import AppUtil;
//import GlideImageView;
//
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class WeMediaTopicAvatarListAdapter extends RecyclerView.Adapter<WeMediaTopicAvatarListAdapter.InnerViewHolder> {
//
//    private Context mContext;
//
//    public void setDatas(List<WeMediaTopic> datas) {
//        this.mDatas = datas;
//    }
//
//    public List<WeMediaTopic> getDatas() {
//        return mDatas;
//    }
//
//    private List<WeMediaTopic> mDatas = null;
//
//    public WeMediaTopicAvatarListAdapter(Context context) {
//        super();
//        mContext = context;
//    }
//
//    @Override
//    public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new InnerViewHolder(
//                LayoutInflater.from(mContext).inflate(R.layout.wemedia_topic_avatar_list_item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(InnerViewHolder holder, int position) {
//        if (position == 0) {
//            holder.container.setPadding(AppUtil.dip2px(mContext, 5), 0, 0, 0);
//        } else if (position == getItemCount() - 1) {
//            holder.container.setPadding(0, 0, AppUtil.dip2px(mContext, 5), 0);
//        } else {
//            holder.container.setPadding(0, 0, 0, 0);
//        }
//        holder.imageView.loadImage(mDatas.get(position).getImage());
//        holder.imageView.setOnClickListener(v ->
//                        WeMediaTopicDetailActivity.startActivity(
//                                mContext, mDatas.get(position).getId(), mDatas.get(position).getTitle())
//        );
//        holder.textView.setText(mDatas.get(position).getTitle());
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mDatas == null) {
//            return 0;
//        }
//        return mDatas.size();
//    }
//
//    static class InnerViewHolder extends RecyclerView.ViewHolder {
//
//        @Bind(R.id.container)
//        ViewGroup container;
//        @Bind(R.id.hot_topic_icon)
//        GlideImageView imageView;
//        @Bind(R.id.hot_topic_name)
//        TextView textView;
//
//        public InnerViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this,view);
//        }
//    }
//
//}
