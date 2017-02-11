package com.jeremy.lychee.adapter.user;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaTopicDetailActivity;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class WeMediaTopicListViewAdapter extends BaseAdapter {
    private static final String TAG = WeMediaTopicListViewAdapter.class.toString();
    private List<WeMediaTopic> mData = new ArrayList<>();
    private Context context;

    public WeMediaTopicListViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<WeMediaTopic> data) {
        mData = data;
    }

    public List<WeMediaTopic> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public WeMediaTopic getItem(int position) {
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
            view = ((Activity) context).getLayoutInflater().inflate(R.layout.wemedia_topic_listview_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.topicIcon.loadImage(mData.get(position).getImage());
        holder.topicName.setText(mData.get(position).getTitle());
        holder.subCount.setText(""+mData.get(position).getWeight());//TODO
        holder.clickable.setOnClickListener(v ->
                WeMediaTopicDetailActivity.startActivity(context, mData.get(position).getId(), mData.get(position).getTitle()));

        //TODO:此处额外请求会造成性能问题，需要服务端修改直接返回订阅状态
        requestIsTopicSub(mData.get(position).getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.get(0).equals("1")) {//订阅
                        mData.get(position).setIs_sub(true);
                        holder.subscribe.setImageResource(R.mipmap.subscribed);
                    } else {//未订阅

                        mData.get(position).setIs_sub(false);
                        holder.subscribe.setImageResource(R.mipmap.subscribe);
                    }
                }, Throwable::printStackTrace);
        holder.subscribe.setOnClickListener(v ->
                subscribeWeMediaTopic(
                        mData.get(position).getId(),
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

    private void subscribeWeMediaTopic(int cId, boolean isSub, Action0 cb) {
        if (isSub) {
            OldRetroAdapter.getService().subscribeColumn(cId, 2)
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
            OldRetroAdapter.getService().unSubscribeColumn(cId, 2)
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

    private Observable<List<String>> requestIsTopicSub(int cid){
        return OldRetroAdapter.getService()
                .getIsSub(cid, 2)
                .filter(s -> s.getErrno() == 0)
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io());
    }


    static class ViewHolder {
        @Bind(R.id.topic_icon)
        GlideImageView topicIcon;
        @Bind(R.id.topic_name)
        TextView topicName;
        @Bind(R.id.subscribe)
        ImageView subscribe;
        @Bind(R.id.clickable)
        View clickable;
        @Bind(R.id.sub_count)
        TextView subCount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(WeMediaTopic data) {
        mData.add(data);
    }

    public void addAllDatas(List<WeMediaTopic> data) {
        for (WeMediaTopic d : data) {
            mData.add(d);
        }
    }
}
