package com.jeremy.lychee.adapter.news;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaTopicDetailActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.news.ImageNewsDataModel;
import com.jeremy.lychee.model.news.Topic;
import com.jeremy.lychee.presenter.ChannelFragmentPresenter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.videoplayer.VideoPlayerBottomProgressDecorator;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelBasic;
import com.jeremy.lychee.videoplayer.VideoPlayerView;
import com.jeremy.lychee.widget.FlowLayout;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by chengyajun on 2016/3/16.
 */
public class SubjectListAdapter extends RecyclerView.Adapter {
    List list = new ArrayList<>();
    Context mContext;

    public SubjectListAdapter(List data, Context context) {
        this.list = list;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (0 == viewType) {//头部
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_focus_item, parent, false);
            RecyclerView.ViewHolder g = new SubjectListFocusViewHolder(view, true);
            return g;
        } else if (1 == viewType) {//栏目列表item--视频
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_oneimg_item, parent, false);
            RecyclerView.ViewHolder g = new SubjectListOneImageViewHolder(view, true);
            return g;
        } else if (2 == viewType) {//栏目列表item--图集
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_threeimg_item, parent, false);
            RecyclerView.ViewHolder g = new SubjectListThreeImageViewHolder(view, true);
            return g;

        } else if (3 == viewType) {//栏目列表item--普通
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_oneimg_item, parent, false);
            RecyclerView.ViewHolder g = new SubjectListOneImageViewHolder(view, true);
            return g;

        } else if (4 == viewType) {//栏目列表--title
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_title_item, parent, false);
            RecyclerView.ViewHolder g = new SubjectListTitleViewHolder(view, true);
            return g;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (0 == getItemViewType(position)) {//头部
            SubjectTopItem topItem = (SubjectTopItem) list.get(position);
            if (topItem == null) return;
            SubjectListFocusViewHolder vh = (SubjectListFocusViewHolder) holder;
            //标题
            vh.subject_title.setText(topItem.getTitle());
            //摘要
//            vh.subject_zhaiyao.setText(topItem.getSummary());
            SpannableStringBuilder style = new SpannableStringBuilder("摘要：" + topItem.getSummary());
            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.discovery_column_book)), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            vh.subject_zhaiyao.setText(style);

            //图片
            vh.video_img.loadImage(topItem.getImage());

            NewsListData item = topItem.getNews();
            if (item != null) {
                if ("1".equals(topItem.getType())) {//普通新闻
                    vh.video_img_paly.setVisibility(View.GONE);
                    vh.video_title.setVisibility(View.GONE);
                    vh.subject_focus_play_bg.setVisibility(View.GONE);


                } else if ("2".equals(item.getNews_type())) {//图集新闻
                    vh.video_img_paly.setVisibility(View.GONE);
                    vh.video_title.setText(item.getTitle());
                    vh.subject_focus_play_bg.setVisibility(View.VISIBLE);

                    vh.video_img.setOnClickListener(v -> {
                        ChannelFragmentPresenter.OpenNewsDetailActivity(
                                (SlidingActivity) mContext, item, null, null);
                    });

                } else if ("3".equals(item.getNews_type())) {//视频
                    vh.video_img_paly.setVisibility(View.VISIBLE);
                    vh.video_title.setText(item.getTitle());
                    vh.subject_focus_play_bg.setVisibility(View.VISIBLE);
                    vh.video_img.setOnClickListener(v -> {
                        playVideo(vh, topItem);
//                        ChannelFragmentPresenter.OpenNewsDetailActivity(
//                                (SlidingActivity) mContext, item, null, null);
                    });
                }

                //相关话题

                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.rightMargin = AppUtil.dip2px(mContext, 15);
                lp.topMargin = AppUtil.dip2px(mContext, 8);

                List<Topic> topics = topItem.getTopic();
                if (topics != null && topics.size() > 0) {
                    for (int i = 0; i < topics.size(); i++) {
                        Topic topic = topics.get(i);
                        View view = View.inflate(mContext, R.layout.subject_topic_item, null);
                        ((TextView) view.findViewById(R.id.subject_topic_item)).setText("" + topic.getName());
                        //话题点击
                        view.setOnClickListener(v -> {
                            WeMediaTopicDetailActivity.startActivity(mContext, Integer.parseInt(topic.getId() + ""), topic.getName());
                        });
                        vh.topic_layout.addView(view, lp);

                    }

//                    //点击跳转
//                    vh.video_img.setOnClickListener(v -> {
//                        ChannelFragmentPresenter.OpenNewsDetailActivity(
//                                (SlidingActivity) mContext, item, null, null);
//                    });
                }
            }


        } else if (1 == getItemViewType(position)) {//栏目列表item--视频
            NewsListData videoItem = (NewsListData) list.get(position);
            if (videoItem != null) {
                SubjectListOneImageViewHolder vh = (SubjectListOneImageViewHolder) holder;
                vh.subject_oneimg_title.setText("" + videoItem.getTitle());
                vh.live_hot_normal_item__video_title.setText("" + videoItem.getSummary());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                java.util.Date dt = new Date(Long.parseLong(videoItem.getPdate()) * 1000L);
                vh.live_hot_normal_item_video_time.setText("" + AppUtil.formatTime(format.format(dt)));
                vh.live_hot_normal_item_video_img.loadImage(videoItem.getAlbum_pic());



                try {
                    JSONObject jsonObject = new JSONObject(videoItem.getNews_data());
                    vh.live_hot_normal_item_video_duration.setText(jsonObject.getString("video_duration"));
                    vh.live_hot_normal_item_video_duration.setVisibility(View.VISIBLE);
                    vh.live_hot_normal_item_video_duration.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_video_tpye_small,
                            0, 0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                vh.live_hot_normal_item_video_duration.setText(videoItem.getNews_data());
                //点击跳转
                vh.itemView.setOnClickListener(v -> {
                    ChannelFragmentPresenter.OpenNewsDetailActivity(
                            (SlidingActivity) mContext, videoItem, null, null);
                });
            }


        } else if (2 == getItemViewType(position)) {//栏目列表item--图集
            NewsListData mutiImageItem = (NewsListData) list.get(position);
            if (mutiImageItem != null) {
                SubjectListThreeImageViewHolder vh = (SubjectListThreeImageViewHolder) holder;
                vh.subject_threeimg_title.setText("" + mutiImageItem.getTitle());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                java.util.Date dt = new Date(Long.parseLong("" + mutiImageItem.getPdate()) * 1000L);
                vh.title_txt.setText("" + AppUtil.formatTime(format.format(dt)));

                String newsData = mutiImageItem.getNews_data();
                ImageNewsDataModel imageNewsDataModel = null;
                try {
                    imageNewsDataModel = GsonUtils.fromJson(newsData, ImageNewsDataModel.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }
                List<String> pics = imageNewsDataModel.getPics();
                vh.img_1.loadImage(pics.get(0));
                vh.img_2.loadImage(pics.get(1));
                vh.img_3.loadImage(pics.get(2));

                vh.itemView.setOnClickListener(v -> {
                    ChannelFragmentPresenter.OpenNewsDetailActivity(
                            (SlidingActivity) mContext, mutiImageItem, null, null);
                });
            }


        } else if (3 == getItemViewType(position)) {//栏目列表item--普通
            NewsListData oneImageItem = (NewsListData) list.get(position);
            if (oneImageItem != null) {
                SubjectListOneImageViewHolder vh = (SubjectListOneImageViewHolder) holder;
                vh.subject_oneimg_title.setText("" + oneImageItem.getTitle());
                vh.live_hot_normal_item_video_duration.setVisibility(View.GONE);
                vh.live_hot_normal_item__video_title.setText("" + oneImageItem.getSummary());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                java.util.Date dt = new Date(Long.parseLong(oneImageItem.getPdate()) * 1000L);
                vh.live_hot_normal_item_video_time.setText("" + AppUtil.formatTime(format.format(dt)));
                vh.live_hot_normal_item_video_img.loadImage(oneImageItem.getAlbum_pic());
                vh.itemView.setOnClickListener(v -> {
                    ChannelFragmentPresenter.OpenNewsDetailActivity(
                            (SlidingActivity) mContext, oneImageItem, null, null);
                });
            }
        } else if (4 == getItemViewType(position)) {//栏目列表--title
            String title = (String) list.get(position);
            SubjectListTitleViewHolder vh = (SubjectListTitleViewHolder) holder;
            vh.subject_column_name.setText("" + title);
        }

    }

    private void playVideo(SubjectListFocusViewHolder vh, SubjectTopItem topItem){
        NewsListData news = topItem.getNews();
        if(news==null){
            return;
        }

        VideoPlayer.getInstance().getLiveCloudCallback().addOnCompletionAction(() -> vh.player_view.unBindToVideoPlayer(), AndroidSchedulers.mainThread());
        VideoPlayer.getInstance().registerErrorHandler(errorCode -> {
            if (VideoPlayerView.isFullScreen()) {
                VideoPlayerView.exitFullScreen();
            }
            if (vh.player_view != null) {
                vh.player_view.unBindToVideoPlayer();
            }
            Toast.makeText(mContext, VideoPlayer.textMessageForErrorCode(errorCode), Toast.LENGTH_SHORT).show();
        });
        VideoPlayer.getInstance().setRendererContainer(vh.player_area);
        vh.player_view.addControlPanel(new VideoPlayerPanelBasic());
        vh.player_view.addDecorator(new VideoPlayerBottomProgressDecorator());

        vh.player_view.bindToVideoPlayer();
        String data = news.getNews_data();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String video_id= jsonObject.getString("video_id");
            String type = jsonObject.getString("source_type");
            String tag = jsonObject.getString("tag");
            VideoPlayer.getInstance().load((Activity) mContext, video_id, type, tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position && getmList().get(position) != null && getmList().get(position) instanceof SubjectTopItem) {//头部
            return 0;
        } else if (getmList().get(position) != null && getmList().get(position) instanceof NewsListData && "3".equals(((NewsListData) getmList().get(position)).getNews_type())) {//栏目列表item--视频
            return 1;
        } else if (getmList().get(position) != null && getmList().get(position) instanceof NewsListData && "2".equals(((NewsListData) getmList().get(position)).getNews_type())) {//栏目列表item--图集
            return 2;
        } else if (getmList().get(position) != null && getmList().get(position) instanceof NewsListData && "1".equals(((NewsListData) getmList().get(position)).getNews_type())) {//栏目列表item--普通
            return 3;
        } else {//栏目列表--title
            return 4;
        }
    }

    public List getmList() {
        return list;
    }

    public void setData(List list) {
        this.list = list;
    }


    private class SubjectListFocusViewHolder extends RecyclerView.ViewHolder {
        ViewGroup player_area;
        GlideImageView video_img;
        ImageView video_img_paly, subject_focus_play_bg;
        TextView video_title, subject_title, subject_zhaiyao, subject_relate_topic;
        VideoPlayerView player_view;
        FlowLayout topic_layout;

        public SubjectListFocusViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                player_area = (ViewGroup) itemView.findViewById(R.id.player_area);
                video_img = (GlideImageView) itemView.findViewById(R.id.video_img);
                video_img_paly = (ImageView) itemView.findViewById(R.id.video_img_paly);
                video_title = (TextView) itemView.findViewById(R.id.video_title);
                subject_title = (TextView) itemView.findViewById(R.id.subject_title);
                subject_zhaiyao = (TextView) itemView.findViewById(R.id.subject_zhaiyao);
                subject_relate_topic = (TextView) itemView.findViewById(R.id.subject_relate_topic);
                player_view = (VideoPlayerView) itemView.findViewById(R.id.player_view);
                topic_layout = (FlowLayout) itemView.findViewById(R.id.topic_layout);
                subject_focus_play_bg = (ImageView) itemView.findViewById(R.id.subject_focus_play_bg);
            }
        }
    }

    private class SubjectListOneImageViewHolder extends RecyclerView.ViewHolder {
        TextView subject_oneimg_title, live_hot_normal_item_video_duration, live_hot_normal_item__video_title, live_hot_normal_item_video_time;
        GlideImageView live_hot_normal_item_video_img;

        public SubjectListOneImageViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                live_hot_normal_item_video_img = (GlideImageView) itemView.findViewById(R.id.live_hot_normal_item_video_img);
                subject_oneimg_title = (TextView) itemView.findViewById(R.id.subject_oneimg_title);
                live_hot_normal_item_video_duration = (TextView) itemView.findViewById(R.id.live_hot_normal_item_video_duration);
                live_hot_normal_item__video_title = (TextView) itemView.findViewById(R.id.live_hot_normal_item__video_title);
                live_hot_normal_item_video_time = (TextView) itemView.findViewById(R.id.live_hot_normal_item_video_time);
            }
        }
    }

    private class SubjectListThreeImageViewHolder extends RecyclerView.ViewHolder {
        TextView subject_threeimg_title, title_txt;
        GlideImageView img_1, img_2, img_3;

        public SubjectListThreeImageViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                img_1 = (GlideImageView) itemView.findViewById(R.id.img_1);
                img_2 = (GlideImageView) itemView.findViewById(R.id.img_2);
                img_3 = (GlideImageView) itemView.findViewById(R.id.img_3);
                subject_threeimg_title = (TextView) itemView.findViewById(R.id.subject_threeimg_title);
                title_txt = (TextView) itemView.findViewById(R.id.title_txt);
            }
        }
    }

    private class SubjectListTitleViewHolder extends RecyclerView.ViewHolder {
        TextView subject_column_name;

        public SubjectListTitleViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                subject_column_name = (TextView) itemView.findViewById(R.id.subject_column_name);
            }
        }
    }

}
