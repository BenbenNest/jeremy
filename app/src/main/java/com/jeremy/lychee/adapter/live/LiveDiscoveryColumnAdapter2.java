package com.jeremy.lychee.adapter.live;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.ListLoadHolder;
import com.jeremy.lychee.adapter.LiveHotUltimateViewAdapter;
import com.jeremy.lychee.db.LiveHotItem;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.news.HotVideoBody;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chengyajun on 2016/1/10.
 */
public class LiveDiscoveryColumnAdapter2 extends LiveHotUltimateViewAdapter {
    private List<LiveHotItem> mList;
    private Context mContext;
    private ListLoadHolder mLoadHolder = null;

    public LiveDiscoveryColumnAdapter2(Activity context, List<LiveHotItem> list) {
        this.mContext = context;
        setData(list);
    }

    public List<LiveHotItem> getmList() {
        return mList;
    }

    public RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
        RecyclerView.ViewHolder g = new LiveHotViewholder(view, true);
        return g;
    }

    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_discovery_column_item, parent, false);
        return new LiveHotViewholder(view, true);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (VIEW_TYPES.NORMAL == getItemViewType(position)) {//左文右图
            LiveHotViewholder h = (LiveHotViewholder) viewHolder;
            LiveHotItem item = mList.get(position);
            h.live_hot_normal_item__video_title.setText("" + item.getVideo_name());


//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            java.util.Date dt = new Date(Long.parseLong(item.getPdate()) * 1000L);
//
//            h.live_hot_normal_item_video_time.setText("" + AppUtil.formatTime(format.format(dt)));

            h.live_hot_normal_item_duration.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_video_tpye_small,
                    0, 0, 0);
            h.live_hot_normal_item_duration.setText(item.getVideo_duration() + "");

            h.live_hot_normal_item_video_img.loadImage(item.getVideo_img());
            //点击播放视频
            h.itemView.setOnClickListener(v -> {
                NewsListData entity = new NewsListData();
                entity.setTitle(item.getVideo_name());
                entity.setUrl(item.getVideo_url());
                entity.setZm(item.getZm());
                entity.setNews_type(item.getNews_type());
                entity.setNid(item.getId() + "");

                HotVideoBody hotVideoBody = new HotVideoBody();
                hotVideoBody.setTag(item.getTag());
                hotVideoBody.setSource_type(item.getVideo_type());
                hotVideoBody.setVideo_id(item.getId());
                entity.setNews_data(GsonUtils.toJson(hotVideoBody));

//                OldNewsDetailActivity.startActivity(mContext, entity, 0);

                HitLog.hitLogVideoClick(HitLog.POSITION_LIVE_HOT,
                        item.getId() + "," + item.getNews_from() + "," + item.getNews_type() + "," + item.getIs_focus());
            });
            h.live_hot_normal_item_source.setText("" + item.getSource());
            int comment=  Integer.parseInt(""+item.getComment());
            if (comment>0) {
                h.live_hot_normal_item_comment.setText("" + item.getComment());
            }else {
                h.live_hot_normal_item_comment.setText("");
            }

        }

    }


    public void setData(List<LiveHotItem> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mList = list;
    }

    public void insert(LiveHotItem liveChannel, int position) {
        insert(mList, liveChannel, position);
    }

    public void insert(List<LiveHotItem> list, int postion, boolean isTop) {

        if (list == null || list.size() <= 0) {
            return;
        }
        if (this.mList == null || this.mList.size() == 0) {
            //当第一次拉取数据条目小于2时，显示没有更多数据了
            if (list.size() <= 2) {
                showNoDataView();
            }
        }
        if (isTop) {
            for (int i = 0; i < list.size(); i++) {
                insert(mList, list.get(i), postion++);

            }
        } else {
            for (int i = list.size() - 1; i >= 0; i--) {
                insert(mList, list.get(i), postion);
            }
        }
    }

    public void remove(int position) {
        remove(mList, position);
    }

    public void clear() {
        clear(mList);
    }


    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }


    @Override
    public long generateHeaderId(int position) {
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public void showErrorView() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.moreLayout.setVisibility(View.GONE);
        mLoadHolder.errorLayout.setVisibility(View.VISIBLE);
        mLoadHolder.nodataLayout.setVisibility(View.GONE);
    }

    public void showNoDataView() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.moreLayout.setVisibility(View.GONE);
        mLoadHolder.errorLayout.setVisibility(View.GONE);
        TextView tv = (TextView) mLoadHolder.nodataLayout.findViewById(R.id.load_nodata_text);
        tv.setText("没有了，点击刷新更多");
        mLoadHolder.nodataLayout.setVisibility(View.VISIBLE);

    }

    public void showLoadingDataView() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.moreLayout.setVisibility(View.VISIBLE);
        mLoadHolder.errorLayout.setVisibility(View.GONE);
        mLoadHolder.nodataLayout.setVisibility(View.GONE);
    }

    public void setErrorListener(View.OnClickListener listener) {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.errorLayout.setOnClickListener(listener);
    }

    public void setNoDataListener(View.OnClickListener listener) {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        mLoadHolder.nodataLayout.setOnClickListener(listener);
    }


    public Boolean isLoadNodata() {
        if (mLoadHolder == null) {
            mLoadHolder = new ListLoadHolder(getCustomLoadMoreView());
        }
        return false;
    }


    public class LiveHotViewholder extends RecyclerView.ViewHolder {

        public TextView live_hot_normal_item_source, live_hot_normal_item__video_title, live_hot_normal_item_comment, live_hot_normal_item_duration;
        public GlideImageView live_hot_normal_item_video_img;

        /*public ImageView topImg;*/
        public LiveHotViewholder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                live_hot_normal_item_video_img = (GlideImageView) itemView.findViewById(R.id.live_hot_normal_item_video_img);
                live_hot_normal_item__video_title = (TextView) itemView.findViewById(R.id.live_hot_normal_item__video_title);
                live_hot_normal_item_source = (TextView) itemView.findViewById(R.id.live_hot_normal_item_source);
                live_hot_normal_item_comment = (TextView) itemView.findViewById(R.id.live_hot_normal_item_comment);
                live_hot_normal_item_duration = (TextView) itemView.findViewById(R.id.live_hot_normal_item_duration);
            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && customLoadMoreView != null) {
            if (isLoadMoreChanged) {
                return VIEW_TYPES.CHANGED_FOOTER;
            } else {
                return VIEW_TYPES.FOOTER;
            }
        } else
            return VIEW_TYPES.NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPES.FOOTER) {
            RecyclerView.ViewHolder viewHolder = getViewHolder(customLoadMoreView, viewType);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        } else if (viewType == VIEW_TYPES.CHANGED_FOOTER) {
            RecyclerView.ViewHolder viewHolder = getViewHolder(customLoadMoreView, viewType);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        } else {

            return onCreateNormalViewHolder(parent);
        }

    }


    public View getHeaderView() {
        return customHeaderView;
    }


    public static String getWeekDay(Calendar c) {
        String week = "星期";
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            week += "六";
        }
        return week;
    }


}
