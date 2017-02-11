package com.jeremy.lychee.adapter.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.model.user.MyFans;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.ImageOptiUrl;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaChannelReaderAdapter extends BaseRecyclerViewAdapter<WeMediaChannelReaderAdapter.MyReaderHolder, MyFans> {

    private static final int REQUEST_SIZE = 10;
    private Context context;
    private int start = 0;
    private boolean hasMore = true;
    private String mWmChannelId;

    public WeMediaChannelReaderAdapter(Context context, String wmChannelId) {
        super(context);
        this.context = context;
        mWmChannelId = wmChannelId;
    }

    @Override
    public boolean isBackwardLoadEnable() {
        return hasMore;
    }

    @Override
    public void load(boolean isLoadMore) {

        if (!isLoadMore) {
            start = 1;
        }
        getFans(mWmChannelId, start, REQUEST_SIZE)
                .subscribe(it -> {
                    if (!isLoadMore) {
                        clear();
                        if (it.size() == 0) {
                            setDataState(DataState.EMPTY);
                        }
                    }
                    append(it);
                    if (it.size() > 0) {
                        hasMore = it.size() == REQUEST_SIZE;
                        start++;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    setDataState(DataState.ERROR);
                });

    }

    @Override
    public MyReaderHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wemedia_reader_list_item, parent, false);
        return new MyReaderHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(MyReaderHolder holder, int position) {
        MyFans MyFans = mItems.get(position);
        if (!TextUtils.isEmpty(MyFans.getLikes())) {
            holder.priseNum.setText(MyFans.getLikes());
        } else {
            holder.priseNum.setText("0");
        }
        holder.articleNum.setText("推荐过" + MyFans.getTransmit_num() + "篇文章");
        holder.usernameView.setText(MyFans.getNickName());
        if (!TextUtils.isEmpty(MyFans.getUserpic())) {
            Glide.with(context)
                    .load(ImageOptiUrl.get(MyFans.getUserpic(), holder.headImg))
                    .asBitmap()
                    .placeholder(R.drawable.default_avatar)
                    .centerCrop().into(new BitmapImageViewTarget(holder.headImg) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.headImg.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        holder.clickable.setOnClickListener(v ->
                WeMediaChannelActivity.startActivity(context, mItems.get(position).getUid()));

    }

    @Override
    public View getEmptyView(ViewGroup parent, View convertView, int currentState) {
        if (convertView == null) {
            if (null != mContext) switch (currentState) {
                case DataState.EMPTY: {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.wemedia_article_nodata_layout, parent);
                    ((TextView)convertView.findViewById(R.id.no_data_text)).
                            setText(context.getString(R.string.wemedia_reader_empty_str));
                    break;
                }
                default:
                    return super.getEmptyView(parent, null, currentState);
            }
        }
        return convertView;
    }


    /********************************** net request **************************/
    private Observable<List<MyFans>> getFans(String id, int pageNo, int pageRow) {
        return OldRetroAdapter.getService().getFans(id, pageNo, pageRow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(it -> Observable.just(it.getData()));
    }

    /********************************** VH ***********************************/
    public class MyReaderHolder extends RecyclerView.ViewHolder {

        public View clickable;
        public View splitLine;
        public ImageView headImg;
        public TextView usernameView;
        public TextView articleNum;
        public TextView priseNum;

        public MyReaderHolder(View view) {
            super(view);
            clickable = view.findViewById(R.id.clickable);
            headImg = (ImageView) view.findViewById(R.id.user_icon);
            usernameView = (TextView) view.findViewById(R.id.user_name);
            articleNum = (TextView) view.findViewById(R.id.article_num);
            priseNum = (TextView) view.findViewById(R.id.prise_num);
        }

    }
}
