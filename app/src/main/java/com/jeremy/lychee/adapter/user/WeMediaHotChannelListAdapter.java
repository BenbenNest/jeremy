package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.model.user.HotWeMediaChannel;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaHotChannelListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private static final int REQUEST_HOT_CHANNELS_SIZE = 30;
    private int mStart = 1;
    private boolean mHasMore = false;

    private List<HotWeMediaChannel> mDatas = new ArrayList<>();

    @Override
    public boolean isBackwardLoadEnable() {
        return mHasMore;
    }

    @Override
    public void load(boolean isLoadMore) {
        if(!isLoadMore){
            refresh();
        }else{
            requestAndShowHotChannels();
        }
    }

    public void refresh(){
        mStart = 1;
        mHasMore = false;
        mDatas.clear();
        requestAndShowHotChannels();
    }

    public WeMediaHotChannelListAdapter(Context context) {
        super(context);
        mContext = context;
        setHasFooterView(false);
    }

    private CharSequence buildReaderCountText(String val) {
        StringBuilder sb = new StringBuilder();
        return sb.append(val).append("名读者");
    }

    private CharSequence buildArticleCountText(String val) {
        StringBuilder sb = new StringBuilder();
        return sb.append(val).append("篇文章");
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        return new InnerViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.user_hot_channel_list_item, parent, false));
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        InnerViewHolder h = (InnerViewHolder)holder;
        h.channelName_.setText(mDatas.get(position).getName());
        h.channelIcon_.loadImage(mDatas.get(position).getIcon(), (req, v) -> req
                .crossFade()
                .bitmapTransform(new GlideCircleTransform(mContext))
                .placeholder(R.drawable.default_avatar)
                .into(v));
        h.channelIcon_.setOnClickListener(v ->
                WeMediaChannelActivity.startActivity(
                        mContext, Integer.toString(mDatas.get(position).getC_id())
        ));

    }


    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    /*************************** VH *************************************/

    class InnerViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.channel_icon)
        GlideImageView channelIcon_;
        @Bind(R.id.channel_name)
        TextView channelName_;

        public InnerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /***************************Data Request*************************************/

    private void requestAndShowHotChannels() {
        getHotChannelList(mStart, REQUEST_HOT_CHANNELS_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            mDatas.addAll(s);
                            if(s.size() > 0){
                                mHasMore = true;
                                mStart = s.get(s.size() - 1).getC_id();
                            }else{
                                mHasMore = false;
                            }
                            swipeRefreshLayout.setRefreshing(false);
                            notifyDataSetChanged();
                        },
                        e -> {
                            e.printStackTrace();
                            setDataState((DataState.ERROR));
                        });
    }

    private Observable<List<HotWeMediaChannel>> getHotChannelList(int start, int size) {
        return OldRetroAdapter.getService().getUserHotChannelList(start, size)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(respones -> Observable.just(respones.getData()));
    }
}
