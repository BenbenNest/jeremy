package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaSubscribedTopicsActivity;
import com.jeremy.lychee.activity.user.WeMediaTopicDetailActivity;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideRoundRectTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class WeMediaTopicAvatarListAdapter2 extends BaseAdapter {
    private static final String TAG = WeMediaTopicListViewAdapter.class.toString();
    private List<WeMediaTopic> mData = new ArrayList<>();
    private Context context;

    public WeMediaTopicAvatarListAdapter2(Context context) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size() - 1
                && mData.get(position).getId() == 0) {
            return FootViewHolder.ITEM_VIEW_TYPE;
        }
        return ItemViewHolder.ITEM_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO:NestedListView下，貌似由于item的height计算不准确，导致
        // getView调用次数激增，ConvertView为null，以下代码也许不能正常运行，但目前需求只显示5条，故暂无问题，留待今后解决
        if (this.getItemViewType(position) == FootViewHolder.ITEM_VIEW_TYPE) {//footer
            FootViewHolder holder;
            if (convertView != null) {
                holder = (FootViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.wemedia_sub_topic_listview_foot_item, null);
                holder = new FootViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder.clickable.setOnClickListener(v -> WeMediaSubscribedTopicsActivity.startActivity(context));
            return convertView;
        }

        ItemViewHolder holder;
        if (convertView != null) {
            holder = (ItemViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.wemedia_topic_avatar_list_item2, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }

        WeMediaTopic topic = mData.get(position);
        holder.topicName.setText(topic.getTitle());
        holder.topicIcon.loadImage(topic.getImage(), (req, v) -> req
                .placeholder(AppUtil.getDefaultSquareIcon(context))
                .crossFade()
                .bitmapTransform(new GlideRoundRectTransform(context))
                .into(v));
        holder.clickable.setOnClickListener(v ->
                WeMediaTopicDetailActivity.startActivity(context,topic.getId(), topic.getTitle()));

        return convertView;
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

    static class FootViewHolder {
        static final int ITEM_VIEW_TYPE = 1;
        @Bind(R.id.clickable)
        View clickable;

        public FootViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    static class ItemViewHolder {
        static final int ITEM_VIEW_TYPE = 0;
        @Bind(R.id.topic_icon)
        GlideImageView topicIcon;
        @Bind(R.id.topic_name)
        TextView topicName;
        @Bind(R.id.sub_count_parent)
        View subCountParent;
        @Bind(R.id.subscribe_btn)
        ImageView subscribeBtn;
        @Bind(R.id.clickable)
        View clickable;

        public ItemViewHolder(View view) {
            ButterKnife.bind(this, view);
            subscribeBtn.setVisibility(View.GONE);
            subCountParent.setVisibility(View.GONE);
        }
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(WeMediaTopic data) {
        mData.add(data);
    }

    public void addData(int idx , WeMediaTopic data) {
        mData.add(idx, data);
    }

    public void addAllDatas(List<WeMediaTopic> data) {
        for (WeMediaTopic d : data) {
            mData.add(d);
        }
    }
}
