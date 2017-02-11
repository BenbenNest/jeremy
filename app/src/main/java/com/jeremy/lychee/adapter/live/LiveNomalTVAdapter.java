package com.jeremy.lychee.adapter.live;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.db.LiveHotItem;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.fragment.live.LiveChannelFragment3;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.live.IsPlayingLive;
import com.jeremy.lychee.model.live.TVList;
import com.jeremy.lychee.model.news.HotVideoBody;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.shareWindow.ShareManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class LiveNomalTVAdapter extends RecyclerView.Adapter {
    private final static int TYPE_HEADER = 100;
    private final static int TYPE_FOOTER = 101;
    private final static int TYPE_NORMAL = 102;

    private List<TVList> tvlist;
    private Context context;
    private boolean hasHeader;
    private boolean hasFooter;
    private View headerView;
    private View footerView;
    public String channelId;
    public String dingNum = "0";

    private List<LiveChannelVedio> liveChannelVedios;
    public LiveNomalTVAdapter(Context context,boolean hasHeader,boolean hasFooter, View header, View footer) {
        this.context = context;
        liveChannelVedios = new ArrayList<>();
        this.hasFooter = hasFooter;
        this.hasHeader = hasHeader;
        this.headerView = header;
        this.footerView = footer;
        setHasStableIds(true);
    }
    private RecyclerView.ViewHolder createHeaderViewHolder() {
        if (headerView == null) {
            throw new IllegalStateException("headerView can not be null if you had a header set");
        }
        return new RecyclerView.ViewHolder(headerView) {
        };
    }
    private RecyclerView.ViewHolder createFooterViewHolder() {
        if (footerView == null) {
            throw new IllegalStateException("footerView can not be null if you had a footer set");
        }
        return new RecyclerView.ViewHolder(footerView) {
        };
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return  createHeaderViewHolder();
        } else if (viewType == TYPE_FOOTER) {
            return  createFooterViewHolder();
        } else  {
            if (viewType==0){
                return ISPlayingViewHolder.onCreateViewHolder(context);
            }else {
                View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.live_channel_itemlayout, parent, false);
                return new ViewHolder(view);
            }

        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type =  getItemViewType(position);
        if (hasHeader && hasFooter) {
            if (position != 0 && position != getItemCount() - 1) {
                if (type==0){
                    ISPlayingViewHolder.bindViewHolder(holder,context,position, (IsPlayingLive) liveChannelVedios.get(position).data);
                }else {
                    bindNormalHolder(holder,position);
                }
                //adapterProxy.onBindViewHolder(holder, position - 1);
            }
        } else if (hasHeader) {
            if (position != 0) {
                if (type==0){
                    ISPlayingViewHolder.bindViewHolder(holder,context,position, (IsPlayingLive) liveChannelVedios.get(position).data);
                }else {
                    bindNormalHolder(holder,position);
                }
                //adapterProxy.onBindViewHolder(holder, position - 1);
            }
        } else if (hasFooter) {
            if (position != getItemCount() - 1) {
                if (type==0){
                    ISPlayingViewHolder.bindViewHolder(holder,context,position, (IsPlayingLive) liveChannelVedios.get(position).data);
                }else {
                    bindNormalHolder(holder,position);
                }
                //adapterProxy.onBindViewHolder(holder, position);
            }
        } else {
            if (type==0){
                ISPlayingViewHolder.bindViewHolder(holder,context,position, (IsPlayingLive) liveChannelVedios.get(position).data);
            }else {
                bindNormalHolder(holder,position);
            }
            //adapterProxy.onBindViewHolder(holder, position);
        }
    }
    public void bindNormalHolder(RecyclerView.ViewHolder holder, int position){
        ViewHolder viewHolder = (ViewHolder) holder;
        LiveHotItem liveHotItem = (LiveHotItem) liveChannelVedios.get(position).data;
        viewHolder.video_img.loadImage(liveHotItem.getVideo_img());
        viewHolder.how_long.setText(liveHotItem.getVideo_duration());
        viewHolder.title.setText(liveHotItem.getVideo_name());
        if (liveHotItem.getComment().equals("0")){
            viewHolder.comment_num.setVisibility(View.GONE);
        }else {
            viewHolder.comment_num.setVisibility(View.VISIBLE);
            viewHolder.comment_num.setText(liveHotItem.getComment());
        }

        viewHolder.come_from.setText(liveHotItem.getSource());
        viewHolder.itemView.setOnClickListener(v -> {
            QEventBus.getEventBus().post(new LiveChannelFragment3.StopVideoPlaying());
            NewsListData entity = new NewsListData();
            entity.setTitle(liveHotItem.getVideo_name());
            entity.setUrl(liveHotItem.getVideo_url());
            entity.setZm(liveHotItem.getZm());
            entity.setNews_type(liveHotItem.getNews_type());
            entity.setNid(liveHotItem.getId() + "");

            HotVideoBody hotVideoBody = new HotVideoBody();
            hotVideoBody.setTag(liveHotItem.getTag());
            hotVideoBody.setSource_type(liveHotItem.getVideo_type());
            hotVideoBody.setVideo_id(liveHotItem.getId());
            entity.setNews_data(GsonUtils.toJson(hotVideoBody));

            OldNewsDetailActivity.startActivity(context, entity, 0);
        });


    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && hasFooter) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
              /*  return TYPE_NORMAL;*/
                return liveChannelVedios.get(position).type;
            }
        } else if (hasHeader) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                /*return TYPE_NORMAL;*/
                return liveChannelVedios.get(position).type;
            }
        } else if (hasFooter) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                /*return TYPE_NORMAL;*/
                return liveChannelVedios.get(position).type;
            }
        } else {
            /*return TYPE_NORMAL;*/
            return liveChannelVedios.get(position).type;
        }

    }
    @Override
    public int getItemCount() {
        if (liveChannelVedios!=null){
            if (liveChannelVedios.size()==0){
                hasFooter = false;
            }else {
                hasFooter = true;
            }
            if (hasHeader && hasFooter) {
                return liveChannelVedios.size() + 2;
            } else if (hasHeader) {
                return liveChannelVedios.size() + 1;
            } else if (hasFooter) {
                return liveChannelVedios.size() + 1;
            }
            return liveChannelVedios.size();
        }else {
            return 0;
        }
    }
    public boolean isLoadOver(){
        View view = footerView.findViewById(com.jeremy.lychee.R.id.load_no_data);
        return view.getVisibility()==View.VISIBLE;
    }
    public boolean isLoadError(){
        View view = footerView.findViewById(com.jeremy.lychee.R.id.load_error);
        return view.getVisibility()==View.VISIBLE;
    }
    public void showLoadMore(){
        footerView.findViewById(com.jeremy.lychee.R.id.load_error).setVisibility(View.GONE);
        footerView.findViewById(com.jeremy.lychee.R.id.load_no_data).setVisibility(View.GONE);
        footerView.findViewById(com.jeremy.lychee.R.id.load_more).setVisibility(View.VISIBLE);
    }
    public void showLoadOver(){
        footerView.findViewById(com.jeremy.lychee.R.id.load_error).setVisibility(View.GONE);
        View view = footerView.findViewById(com.jeremy.lychee.R.id.load_no_data);
        view.setBackgroundColor(context.getResources().getColor(com.jeremy.lychee.R.color.white_press));
        TextView tv = (TextView) view.findViewById(com.jeremy.lychee.R.id.load_nodata_text);
        tv.setText("没有了，点击回到顶部");
        footerView.findViewById(com.jeremy.lychee.R.id.load_no_data).setVisibility(View.VISIBLE);
        footerView.findViewById(com.jeremy.lychee.R.id.load_more).setVisibility(View.GONE);
    }
    public void showLoadError(){
        footerView.findViewById(com.jeremy.lychee.R.id.load_error).setVisibility(View.VISIBLE);
        View view = footerView.findViewById(com.jeremy.lychee.R.id.load_error);
        view.setBackgroundColor(context.getResources().getColor(com.jeremy.lychee.R.color.white_press));
        footerView.findViewById(com.jeremy.lychee.R.id.load_no_data).setVisibility(View.GONE);
        footerView.findViewById(com.jeremy.lychee.R.id.load_more).setVisibility(View.GONE);
    }
    public boolean hasHeader() {
        return hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public View getHeader() {
        return headerView;
    }

    public View getFooter() {
        return footerView;
    }

    public void setAllList(List<IsPlayingLive> isPlayingLives,List<LiveHotItem> backwatchList) {
        liveChannelVedios.clear();
        if (isPlayingLives!=null&&isPlayingLives.size()>0){
            for (IsPlayingLive isPlayingLive : isPlayingLives){
                liveChannelVedios.add(new LiveChannelVedio(0,isPlayingLive));
            }
        }
        if (backwatchList!=null && backwatchList.size()>0){
            for (LiveHotItem liveChannel : backwatchList){
                liveChannelVedios.add(new LiveChannelVedio(1,liveChannel));
            }
        }

    }
    public List<LiveChannelVedio> getAllList(){
        return liveChannelVedios;
    }
    public void addBackWatch(List<LiveHotItem> backwatchList){
        if (liveChannelVedios==null){
            return;
        }
        if (backwatchList!=null && backwatchList.size()>0){
            for (LiveHotItem liveChannel : backwatchList){
                liveChannelVedios.add(new LiveChannelVedio(1,liveChannel));
            }
        }

    }

    public void addIsPlayVideo(List<IsPlayingLive> isPlayingLives){
        if (liveChannelVedios==null){
            return;
        }
        if (isPlayingLives!=null&&isPlayingLives.size()>0){
            for (IsPlayingLive isPlayingLive : isPlayingLives){
                liveChannelVedios.add(new LiveChannelVedio(0,isPlayingLive));
            }
        }

    }
    @Override
    public long getItemId(int position) {
        if (liveChannelVedios == null || liveChannelVedios.size() <= position) {
            return -1;
        }

        if (liveChannelVedios.get(position).type == 0) {
            IsPlayingLive isPlayingLive = (IsPlayingLive) liveChannelVedios.get(position).data;
            return isPlayingLive.getVideo_key().hashCode();
        } else {
            LiveHotItem hotItem = (LiveHotItem) liveChannelVedios.get(position).data;
            return hotItem.getVideo_key().hashCode();
        }
    }

 /*   public void onEventMainThread(LiveChannelFragment2.CloseMiniPlayer event) {
        LiveChannelFragment2.playingItemId = -1;
        notifyDataSetChanged();
    }*/

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;

        @Bind(com.jeremy.lychee.R.id.row_title)
        TextView title;
        @Bind(com.jeremy.lychee.R.id.how_long)
        TextView how_long;
        @Bind(com.jeremy.lychee.R.id.video_img)
        public GlideImageView video_img;
        @Bind(com.jeremy.lychee.R.id.comment_num)
        TextView comment_num;
        @Bind(com.jeremy.lychee.R.id.come_from)
        TextView come_from;
        // each data item is just a string in this case
        public ViewHolder(View view) {
            super(view);
            rootView = view;
            rootView.setClickable(true);
            ButterKnife.bind(this, view);
        }
    }

    public static class LiveChannelVedio<T>{
        public LiveChannelVedio(int type,T data){
            this.type = type;
            this.data = data;
        }
        public int type;
        public T data;
    }

    public static class ISPlayingViewHolder{
        public static RecyclerView.ViewHolder onCreateViewHolder(Context context){
            View view =  View.inflate(context, com.jeremy.lychee.R.layout.live_channel_isplay_item, null);
            return new RowHolder(view);
        }
        public static void  bindViewHolder(RecyclerView.ViewHolder holder,Context context, int position,IsPlayingLive isPlayingLive){
            RowHolder viewholder = (RowHolder) holder;
            viewholder.title.setText(isPlayingLive.getTitle());
            if (TextUtils.isEmpty(isPlayingLive.getTitle())){
                viewholder.title.setVisibility(View.GONE);
            }
            viewholder.come_from.setText("");
            viewholder.comment.setText(isPlayingLive.getComment());
            if (isPlayingLive.getComment().equals("0")){
                viewholder.comment.setVisibility(View.GONE);
            }
            viewholder.introduction.setText(isPlayingLive.getSummary());
            if (TextUtils.isEmpty(isPlayingLive.getSummary())){
                viewholder.introduction.setVisibility(View.GONE);
            }
            viewholder.share.setOnClickListener(v -> {
                ShareInfo shareInfo = new ShareInfo(isPlayingLive.getShare(), isPlayingLive.getId(), isPlayingLive.getVideo_name(), "", isPlayingLive.getVideo_img(), null,ShareInfo.SHARECONTENT_LIVE);
                new ShareManager((Activity) context, shareInfo, true,
                        () -> {}) //分享打点
                        .show();
            });

        }
        static class RowHolder extends RecyclerView.ViewHolder{
            @Bind(com.jeremy.lychee.R.id.comment)
            TextView comment;
            @Bind(com.jeremy.lychee.R.id.title)
            TextView title;
            @Bind(com.jeremy.lychee.R.id.introduction)
            TextView introduction;
            @Bind(com.jeremy.lychee.R.id.come_from)
            TextView come_from;
            @Bind(com.jeremy.lychee.R.id.share)
            TextView share;

            View rootView;
            public RowHolder(View itemView) {
                super(itemView);
                rootView = itemView;
                rootView.setClickable(true);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}


