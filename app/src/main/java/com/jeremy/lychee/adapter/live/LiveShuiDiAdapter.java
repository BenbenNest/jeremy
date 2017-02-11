package com.jeremy.lychee.adapter.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.fragment.live.LiveChannelFragment2;
import com.jeremy.lychee.model.live.ShuiDiVideo;
import com.jeremy.lychee.model.news.HotVideoBody;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class LiveShuiDiAdapter extends RecyclerView.Adapter {
    private final static int TYPE_HEADER = 100;
    private final static int TYPE_FOOTER = 101;
    private final static int TYPE_NORMAL = 102;
    private Context context;
    private boolean hasHeader;
    private boolean hasFooter;
    private View headerView;
    private View footerView;
    private List<ShuiDiVideo> shuidiVideos;
    public String channelId;

    public LiveShuiDiAdapter(Context context, boolean hasHeader, boolean hasFooter, View header, View footer) {
        this.context = context;
        shuidiVideos = new ArrayList<>();
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
            return createHeaderViewHolder();
        } else if (viewType == TYPE_FOOTER) {
            return createFooterViewHolder();
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_shuidi_itemlayout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasHeader && hasFooter) {
            if (position != 0 && position != getItemCount() - 1) {
                bindNormalHolder(holder, position);
                //adapterProxy.onBindViewHolder(holder, position - 1);
            }
        } else if (hasHeader) {
            if (position != 0) {
                bindNormalHolder(holder, position);
                //adapterProxy.onBindViewHolder(holder, position - 1);
            }
        } else if (hasFooter) {
            if (position != getItemCount() - 1) {
                bindNormalHolder(holder, position);
                //adapterProxy.onBindViewHolder(holder, position);
            }
        } else {
            bindNormalHolder(holder, position);
            //adapterProxy.onBindViewHolder(holder, position);
        }
    }

    public void bindNormalHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ShuiDiVideo shuiDiVideo = shuidiVideos.get(position);
        viewHolder.title.setText("" + shuiDiVideo.getVideo_name());
        viewHolder.video_img.loadImage(shuiDiVideo.getVideo_img());
        viewHolder.user_head_img.loadImage(shuiDiVideo.getAuthor_img(), (req, v) -> req
                .placeholder(context.getResources().getDrawable(R.drawable.livechannel_item_logo))
                .bitmapTransform(new GlideCircleTransform(context))
                .into(v));
        viewHolder.user_name.setText(shuiDiVideo.getAuthor_nickname());
        if (TextUtils.isEmpty(shuiDiVideo.getLocation())){
            viewHolder.area.setVisibility(View.GONE);
        }else {
            viewHolder.area.setText(shuiDiVideo.getLocation());
            viewHolder.area.setVisibility(View.VISIBLE);
        }
        viewHolder.onlookers.setText(shuiDiVideo.getWatches()+"人围观");
        viewHolder.itemView.setOnClickListener(v -> {
            NewsListData entity = new NewsListData();
            entity.setTitle(shuiDiVideo.getVideo_name());
            entity.setUrl(shuiDiVideo.getVideo_play_url());
            entity.setZm("");
            entity.setNews_type(shuiDiVideo.getVideo_type());
            entity.setNid(shuiDiVideo.getId() + "");

            HotVideoBody hotVideoBody = new HotVideoBody();
            hotVideoBody.setTag("");
            hotVideoBody.setSource_type(shuiDiVideo.getVideo_type());
            hotVideoBody.setVideo_id(shuiDiVideo.getId());
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
                return TYPE_NORMAL;
            }
        } else if (hasHeader) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        } else if (hasFooter) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_NORMAL;
            }
        } else {
            return TYPE_NORMAL;
        }

    }

    @Override
    public int getItemCount() {
        if (shuidiVideos != null) {
            if (shuidiVideos.size() == 0) {
                hasFooter = false;
            } else {
                hasFooter = true;
            }
            if (hasHeader && hasFooter) {
                return shuidiVideos.size() + 2;
            } else if (hasHeader) {
                return shuidiVideos.size() + 1;
            } else if (hasFooter) {
                return shuidiVideos.size() + 1;
            }
            return shuidiVideos.size();
        } else {
            return 0;
        }
    }

    public boolean isLoadOver() {
        View view = footerView.findViewById(R.id.load_no_data);
        return view.getVisibility() == View.VISIBLE;
    }

    public boolean isLoadError() {
        View view = footerView.findViewById(R.id.load_error);
        return view.getVisibility() == View.VISIBLE;
    }

    public void showLoadMore() {
        footerView.findViewById(R.id.load_error).setVisibility(View.GONE);
        footerView.findViewById(R.id.load_no_data).setVisibility(View.GONE);
        footerView.findViewById(R.id.load_more).setVisibility(View.VISIBLE);
    }

    public void showLoadOver() {
        footerView.findViewById(R.id.load_error).setVisibility(View.GONE);
        View view = footerView.findViewById(R.id.load_no_data);
        view.setBackgroundColor(context.getResources().getColor(R.color.white_press));
        TextView tv = (TextView) view.findViewById(R.id.load_nodata_text);
        tv.setText("没有了，点击回到顶部");
        footerView.findViewById(R.id.load_no_data).setVisibility(View.VISIBLE);
        footerView.findViewById(R.id.load_more).setVisibility(View.GONE);
    }

    public void showLoadError() {
        footerView.findViewById(R.id.load_error).setVisibility(View.VISIBLE);
        View view = footerView.findViewById(R.id.load_error);
        view.setBackgroundColor(context.getResources().getColor(R.color.white_press));
        footerView.findViewById(R.id.load_no_data).setVisibility(View.GONE);
        footerView.findViewById(R.id.load_more).setVisibility(View.GONE);
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

    public List<ShuiDiVideo> getShuidiVideos() {
        return shuidiVideos;
    }

    public void setShuidiVideos(List<ShuiDiVideo> shuidiVideos) {
        this.shuidiVideos = shuidiVideos;
    }


    @Override
    public long getItemId(int position) {
        if (shuidiVideos == null || shuidiVideos.size() <= position) {
            return -1;
        }
        return shuidiVideos.get(position).getId().hashCode();
    }

    public void onEventMainThread(LiveChannelFragment2.CloseMiniPlayer event) {
        LiveChannelFragment2.playingItemId = -1;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.video_img)
        public GlideImageView video_img;

        @Bind(R.id.user_head_img)
        public GlideImageView user_head_img;

        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.area)
        TextView area;
        @Bind(R.id.onlookers)
        TextView onlookers;

        // each data item is just a string in this case
        public ViewHolder(View view) {
            super(view);
            rootView = view;
            rootView.setClickable(true);
            ButterKnife.bind(this, view);
            //videoPlayerView.addControlPanel(new VideoPlayerPanelBasic());
        }
    }
}


