package com.jeremy.lychee.adapter.live;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.jeremy.lychee.manager.live.LiveBrowseHistoryManager;
import com.jeremy.lychee.activity.live.LivePlayerActivity;
import com.jeremy.lychee.fragment.live.LiveMineListFragment;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.utils.dialog.DialogUtil;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 直播  -> 我的 历史list adapter
 */
public class LiveVideoHistoryListAdapter extends RecyclerView.Adapter<LiveVideoHistoryListAdapter.ViewHolder> {
    /**
     * live 推荐 tag
     */
    public final static String RECOMMEND_TAG = "RECOMMEND";

    private Context mContext;
    private String tag;
    private RequestManager mRequestManager;
    private final List<LiveVideoInfo> mValues;

    public LiveVideoHistoryListAdapter(Context mContext, List<LiveVideoInfo> items, String tag) {
        this.mContext = mContext;
        mValues = items;
        mRequestManager = Glide.with(mContext);
        this.tag = tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.fragment_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LiveVideoInfo item = mValues.get(position);
        if (item == null) {
            return;
        }

        switch (tag) {
            case RECOMMEND_TAG:
                //推荐
                holder.video_del.setVisibility(View.GONE);
                break;
            default:
                holder.video_del.setVisibility(View.VISIBLE);
                break;
        }

        String video_publish_status = item.getVideo_publish_status();
        if ("0".equals(video_publish_status)&&(LiveMineListFragment.UPLOAD_TAG.equals(tag)||LiveMineListFragment.RECORD_TAG.equals(tag))) {
            //正在审核
            holder.living_shenhe.setVisibility(View.VISIBLE);
        } else {
            //审核通过
            holder.living_shenhe.setVisibility(View.GONE);
        }
        holder.video_del.setOnClickListener(v -> removeItem(item));
        holder.itemView.setOnClickListener(v -> LivePlayerActivity.loadVideo(mContext, item.getId(), item.getVideo_type(), item.getTag()));
        holder.video_title.setText(item.getVideo_name());
        try {
            long pdate_timestamp = item.getPdate()*1000;
            if (pdate_timestamp == 0) {
                holder.video_time.setVisibility(View.GONE);
            } else {
                holder.video_time.setVisibility(View.VISIBLE);
                String video_time = AppUtil.formatTime(pdate_timestamp);
                holder.video_time.setText(video_time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.video_duration.setText(item.getVideo_duration());
        mRequestManager.load(ImageOptiUrl.get(item.getVideo_img(), holder.video_img))
                .centerCrop()
                .crossFade()
                .into(holder.video_img);
    }

    private void removeItem(LiveVideoInfo item) {
        DialogUtil.showConfirmDialog(mContext, mContext.getString(com.jeremy.lychee.R.string.live_mine_del_confirm),
                mContext.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {

                    switch (tag) {
                        case LiveMineListFragment.BROWSE_TAG:
                            int index = mValues.indexOf(item);
                            mValues.remove(item);
                            notifyItemRemoved(index);
                            notifyItemRangeChanged(index, getItemCount());
                            LiveBrowseHistoryManager.getInstance().removeBrowseItem(item);
                            break;
                        case LiveMineListFragment.UPLOAD_TAG:
                            delItem("replay", item);
                            break;
                        case LiveMineListFragment.RECORD_TAG:
                            delItem("live", item);
                            break;
                        case RECOMMEND_TAG:
                            break;
                    }
                    dialog.dismiss();
                }, mContext.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
    }

    /**
     * 删除上传、直播历史item
     */
    private void delItem(String his_type, LiveVideoInfo item) {
        if (item == null) {
            return;
        }
        String vedioId = item.getId();
        String sign = AppUtil.transmitDeleteSignString(vedioId);
        OldRetroApiService mRetroApiService = OldRetroAdapter.getService();
        mRetroApiService.delHistoryLive(vedioId, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO net error
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result != null && result.errno == 0) {
                            int index = mValues.indexOf(item);
                            mValues.remove(item);
                            notifyItemRemoved(index);
                            notifyItemRangeChanged(index, getItemCount());
                        } else {
                            //TODO data error
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        if(mValues!=null){
            return mValues.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView video_title;
        public final TextView video_time;
        public final TextView video_duration;
        public final ImageView video_img;
        public final ImageButton video_del;
        public final View living_shenhe;

        public ViewHolder(View view) {
            super(view);
            video_title = (TextView) view.findViewById(com.jeremy.lychee.R.id.video_title);
            video_time = (TextView) view.findViewById(com.jeremy.lychee.R.id.video_time);
            video_duration = (TextView) view.findViewById(com.jeremy.lychee.R.id.video_duration);
            video_img = (ImageView) view.findViewById(com.jeremy.lychee.R.id.video_img);
            video_del = (ImageButton) view.findViewById(com.jeremy.lychee.R.id.video_del);
            living_shenhe = view.findViewById(com.jeremy.lychee.R.id.living_shenhe);
        }

    }
}
