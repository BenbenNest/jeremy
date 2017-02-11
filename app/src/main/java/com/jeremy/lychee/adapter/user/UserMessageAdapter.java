package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.model.user.UserMessage;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;
import com.jeremy.lychee.widget.recyclerview.BaseRVAdapter;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;

import java.util.List;

public class UserMessageAdapter extends BaseRVAdapter {

    private List<UserMessage> list;

    public UserMessageAdapter(List<UserMessage> list) {
        this.list = list;
    }

    private final static int MESSAGE_TYPE_FOLLOW = 1;
    private final static int MESSAGE_TYPE_COMMENT = 2;
    private final static int MESSAGE_TYPE_RECOMMEN = 3;

    @Override
    protected int getContentItemViewType(int position) {
        UserMessage item = list.get(position);
        return item.type;
    }

    @Override
    protected int getContentItemCount() {
        return list.size();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        switch (contentViewType) {
            case MESSAGE_TYPE_FOLLOW: {
                View view = View.inflate(parent.getContext(), com.jeremy.lychee.R.layout.row_my_message_follow, null);
                HolderFollow holder = new HolderFollow(view);
                ImageView head = (ImageView) view.findViewById(com.jeremy.lychee.R.id.head);
                TextView title = (TextView) view.findViewById(com.jeremy.lychee.R.id.name);
                TextView time = (TextView) view.findViewById(com.jeremy.lychee.R.id.time);

                holder.head = head;
                holder.title = title;
                holder.time = time;
                return holder;
            }
            case MESSAGE_TYPE_COMMENT: {
                View view = View.inflate(parent.getContext(), com.jeremy.lychee.R.layout.row_my_message_comment, null);
                HolderComment holder = new HolderComment(view);
                ImageView head = (ImageView) view.findViewById(com.jeremy.lychee.R.id.head);
                TextView title = (TextView) view.findViewById(com.jeremy.lychee.R.id.name);
                TextView time = (TextView) view.findViewById(com.jeremy.lychee.R.id.time);
                TextView comment = (TextView) view.findViewById(com.jeremy.lychee.R.id.comment);

                holder.head = head;
                holder.head = head;
                holder.title = title;
                holder.time = time;
                holder.comment = comment;
                return holder;
            }
            case MESSAGE_TYPE_RECOMMEN: {
                View view = View.inflate(parent.getContext(), com.jeremy.lychee.R.layout.row_my_message_recommend, null);
                HolderRecommend holder = new HolderRecommend(view);
                ImageView head = (ImageView) view.findViewById(com.jeremy.lychee.R.id.head);
                TextView title = (TextView) view.findViewById(com.jeremy.lychee.R.id.name);
                TextView time = (TextView) view.findViewById(com.jeremy.lychee.R.id.time);

                holder.head = head;
                holder.head = head;
                holder.title = title;
                holder.time = time;
                return holder;
            }
            default:
                return new DefaultHolder(new View(parent.getContext()));
        }
    }

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        int viewType = getContentItemViewType(position);
        switch (viewType) {
            case MESSAGE_TYPE_FOLLOW: {
                HolderFollow holder = (HolderFollow) contentViewHolder;
                Context context = contentViewHolder.itemView.getContext();
                UserMessage message = list.get(position);

                Glide.with(context).load(ImageOptiUrl.get(message.userpic, holder.head))
                        .crossFade()
                        .bitmapTransform(new GlideCircleTransform(context))
                        .placeholder(com.jeremy.lychee.R.drawable.default_avatar)
                        .into(holder.head);
                holder.title.setText(message.user + "订阅了你");
                holder.title.setTypeface(message.isUnRead ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                String[] array = {message.user};
                SpanUtil.parse(holder.title, array, new SpanUtil.SpanClickListener() {
                    @Override
                    public void onClick(String url) {
                        message.isUnRead = false;
                        if (message.user.equals(url)) {
                            WeMediaChannelActivity.startActivity(context, message.uid);
                        }
                    }
                });
                holder.time.setText(AppUtil.formatTime(message.time * 1000));
                holder.clickable.setOnClickListener(v -> {
                    message.isUnRead = false;
                    WeMediaChannelActivity.startActivity(context, message.uid);
                });
                break;
            }
            case MESSAGE_TYPE_COMMENT: {
                HolderComment holder = (HolderComment) contentViewHolder;
                Context context = contentViewHolder.itemView.getContext();
                UserMessage message = list.get(position);

                Glide.with(context).load(ImageOptiUrl.get(message.userpic, holder.head))
                        .crossFade()
                        .bitmapTransform(new GlideCircleTransform(context))
                        .placeholder(com.jeremy.lychee.R.drawable.default_avatar)
                        .into(holder.head);
                String article = (message.extData == null ? "" : message.extData.getTitle());
                holder.title.setText(message.user + "评论了你的文章" + article);
                holder.title.setTypeface(message.isUnRead ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                holder.comment.setText(message.extData.commentData);

                String[] array = {message.user, article};

                SpanUtil.parse(holder.title, array, new SpanUtil.SpanClickListener() {
                    @Override
                    public void onClick(String url) {
                        message.isUnRead = false;
                        if (message.user.equals(url)) {
                            WeMediaChannelActivity.startActivity(context, message.uid);
                        } else if (message.extData.getTitle().equals(url)) {
                            OldNewsDetailActivity.startActivity(
                                    context, message.extData, 0);
                        }
                    }
                });
                holder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OldNewsDetailActivity.startActivity(
                                context, message.extData, 0);
                    }
                });

                holder.time.setText(AppUtil.formatTime(message.time * 1000));
                holder.clickable.setOnClickListener(v -> {
                    message.isUnRead = false;
                    WeMediaChannelActivity.startActivity(context, message.uid);
                });
                break;
            }
            case MESSAGE_TYPE_RECOMMEN: {
                HolderRecommend holder = (HolderRecommend) contentViewHolder;
                Context context = contentViewHolder.itemView.getContext();
                UserMessage message = list.get(position);

                Glide.with(context).load(ImageOptiUrl.get(message.userpic, holder.head))
                        .crossFade()
                        .bitmapTransform(new GlideCircleTransform(context))
                        .placeholder(com.jeremy.lychee.R.drawable.default_avatar)
                        .into(holder.head);

                String article = (message.extData == null ? "" : message.extData.getTitle());
                holder.title.setText(message.user + "转推了你的文章" + article);
                holder.title.setTypeface(message.isUnRead ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);


                String[] array = {message.user, article};
                SpanUtil.parse(holder.title, array, new SpanUtil.SpanClickListener() {
                    @Override
                    public void onClick(String url) {
                        message.isUnRead = false;
                        if (message.user.equals(url)) {
                            WeMediaChannelActivity.startActivity(context, message.uid);
                        } else if (message.extData.getTitle().equals(url)) {
                            OldNewsDetailActivity.startActivity(
                                    context, message.extData, 0);
                        }
                    }
                });

                holder.time.setText(AppUtil.formatTime(message.time * 1000));
                holder.clickable.setOnClickListener(v -> {
                    message.isUnRead = false;
                    WeMediaChannelActivity.startActivity(context, message.uid);
                });
                break;
            }
        }
    }


    class HolderFollow extends RecyclerView.ViewHolder {
        private ImageView head;
        private TextView title;
        private TextView time;
        private View clickable;

        public HolderFollow(View itemView) {
            super(itemView);
            clickable = itemView;
        }
    }

    class HolderRecommend extends RecyclerView.ViewHolder {
        private ImageView head;
        private TextView title;
        private TextView time;
        private View clickable;

        public HolderRecommend(View itemView) {
            super(itemView);
            clickable = itemView;
        }
    }

    class HolderComment extends RecyclerView.ViewHolder {
        private ImageView head;
        private TextView title;
        private TextView time;
        private TextView comment;
        private View clickable;

        public HolderComment(View itemView) {
            super(itemView);
            clickable = itemView;
        }
    }

    class DefaultHolder extends RecyclerView.ViewHolder {
        public DefaultHolder(View itemView) {
            super(itemView);
        }
    }


}
