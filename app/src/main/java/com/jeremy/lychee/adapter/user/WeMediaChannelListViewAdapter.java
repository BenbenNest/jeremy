package com.jeremy.lychee.adapter.user;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class WeMediaChannelListViewAdapter extends BaseAdapter {
    private static final String TAG = WeMediaChannelListViewAdapter.class.toString();
    private List<WeMediaChannel> mData = new ArrayList<>(10);
    private Context context;

    public WeMediaChannelListViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<WeMediaChannel> data) {
        mData = data;
    }

    public List<WeMediaChannel> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public WeMediaChannel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = ((Activity) context).getLayoutInflater().inflate(R.layout.wemedia_channel_listview_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.channelName_.setText(mData.get(position).getName());
        holder.channelDescribe.setText(mData.get(position).getSummary());
        holder.articleCount_.setText(buildArticleCountText(
                mData.get(position).getNews_num() == null ? 0 : mData.get(position).getNews_num()));
        holder.readerCount_.setText(buildReaderCountText(
                mData.get(position).getSub_num() == null ? 0 : mData.get(position).getSub_num()));
        holder.channelIcon_.loadImage(mData.get(position).getIcon(), (req, v) -> req
                .placeholder(AppUtil.getDefaultCircleIcon(context))
                .crossFade()
                .bitmapTransform(new GlideCircleTransform(context))
                .into(v));

        holder.clickable_.setOnClickListener(v ->
                WeMediaChannelActivity.startActivity(context, mData.get(position).getC_id()));

        holder.subscribe_.setImageResource(
                mData.get(position).getIs_sub() ? R.mipmap.subscribed : R.mipmap.subscribe);
        holder.subscribe_.setOnClickListener(v ->
                subscribeWeMediaChannel(
                        Integer.parseInt(mData.get(position).getC_id()),
                        !mData.get(position).getIs_sub(),
                        () -> {
                            mData.get(position).setIs_sub(!mData.get(position).getIs_sub());
                            ((ImageView) v).setImageResource(
                                    mData.get(position).getIs_sub() ? R.mipmap.subscribed : R.mipmap.subscribe);
                        }));
        return view;
    }

    private CharSequence buildReaderCountText(int val) {
        StringBuilder sb = new StringBuilder();
        return sb.append(Integer.toString(val)).append("名读者");
    }

    private CharSequence buildArticleCountText(int val) {
        StringBuilder sb = new StringBuilder();
        return sb.append(Integer.toString(val)).append("篇文章");
    }

    private void subscribeWeMediaChannel(int cId, boolean isSub, Action0 cb) {
        if (isSub) {
            OldRetroAdapter.getService().subscribeColumn(cId, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getErrno() != 0) {
                            Logger.t(TAG).e("订阅失败");
                        } else {
                            cb.call();
                        }
                    }, Throwable::printStackTrace);
        } else {
            OldRetroAdapter.getService().unSubscribeColumn(cId, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getErrno() != 0) {
                            Logger.t(TAG).e("取消订阅失败");
                        } else {
                            cb.call();
                        }
                    }, Throwable::printStackTrace);
        }
    }


    static class ViewHolder {
        @Bind(R.id.channel_icon)
        GlideImageView channelIcon_;
        @Bind(R.id.channel_name)
        TextView channelName_;
        @Bind(R.id.article_count)
        TextView articleCount_;
        @Bind(R.id.reader_count)
        TextView readerCount_;
        @Bind(R.id.subscribe)
        ImageView subscribe_;
        @Bind(R.id.clickable)
        View clickable_;
        @Bind(R.id.channel_describe)
        TextView channelDescribe;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            subscribe_.setVisibility(View.GONE);//暂不显示
        }
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(WeMediaChannel data) {
        mData.add(data);
    }

    public void addAllDatas(List<WeMediaChannel> data) {
        for (WeMediaChannel d : data) {
            mData.add(d);
        }
    }
}
