package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.qihoo.sdk.report.QHStatAgent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class ContentVHTemplate<T> {
    protected int itemType;

    public abstract RecyclerView.ViewHolder getViewHolder(ViewGroup parent);

    public void setItemType(int type) {
        this.itemType = type;
    }

    public abstract void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, T data);

    protected static void setDelListen(View view, NewsListData data) {
        view.setOnClickListener(v -> {
            Context context = v.getContext();
            DialogUtil.showConfirmDialog(context, "如果确认对此内容不感兴趣，\n将为你减少此类推荐",
                    "确定", dialogInterface -> {
                        String sign = AppUtil.getNewsFeedbackSign("notlike",
                                data.getUrl(), data.getNid(), data.getNews_type());
                        OldRetroAdapter.getService().newsFeedback("notlike",
                                data.getUrl(), data.getNid(), data.getNews_type(), sign)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(base -> {
                                    ToastHelper.getInstance(context).toast("成功反馈");
                                }, throwable -> {
                                    throwable.printStackTrace();
                                    ToastHelper.getInstance(context).toast("网络不给力，反馈失败");
                                });
                        dialogInterface.dismiss();
                        //Hitlog 2005
                        QHStatAgent.onEvent(context, QHState.UNINTERESTED);
                        QEventBus.getEventBus().post(new Events.OnNewsDeleted(data));
                    }, "取消", DialogInterface::dismiss);
        });
    }
}
