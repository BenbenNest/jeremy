package com.jeremy.lychee.activity.news;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jeremy.lychee.adapter.news.SubjectListAdapter;
import com.jeremy.lychee.adapter.news.SubjectTopItem;
import com.jeremy.lychee.model.news.NewsSubjectObject;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.news.SubjectColumnGroup;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubjectLlistActivity extends SlidingActivity {

    @Bind(com.jeremy.lychee.R.id.subject_list_recyleview)
    RecyclerView recyclerView;
    @Bind(com.jeremy.lychee.R.id.subject_list_toolbar)
    Toolbar subject_list_toolbar;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_layout;
    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_layout;
    LinearLayoutManager layoutManager;
    SubjectListAdapter mAdapter;
    private String nid;
    private String url;
    private int currentY = 0;
    private TextView shareItem;
    private View toolbar_share;


    public static void startActivity(Context context, String id, String url) {
        Intent intent = new Intent(context, SubjectLlistActivity.class);
        intent.putExtra("nid", id);
        intent.putExtra("url", url);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    private void getExtraParameters() {
        if (getIntent() != null) {
            this.nid = getIntent().getStringExtra("nid");
            this.url = getIntent().getStringExtra("url");
        } else {
            finish();
        }
    }


    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_subject_llist);
        ButterKnife.bind(this);
        getExtraParameters();
        initToolbar();
        initView();
        queryDataFromNet();
    }


    private void initToolbar() {
        subject_list_toolbar.setNavigationIcon(com.jeremy.lychee.R.drawable.ic_actionbar_white_back);
        subject_list_toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (findViewById(com.jeremy.lychee.R.id.menu) != null) {
            return;
        }
        subject_list_toolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_media_channel_activity);
        toolbar_share = findViewById(com.jeremy.lychee.R.id.menu);
        shareItem = (TextView) toolbar_share.findViewById(com.jeremy.lychee.R.id.menu_text);
        shareItem.setBackgroundResource(com.jeremy.lychee.R.drawable.zt_icon_share_white);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        error_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDataFromNet();
                loading_layout.setVisibility(View.VISIBLE);
                error_layout.setVisibility(View.GONE);
            }
        });
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SubjectListAdapter(null, this);
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int scrollY) {
                super.onScrolled(recyclerView, dx, scrollY);
                int oldy = currentY;
                currentY += scrollY;
                float p = (float) (currentY * 1.0 / 300);
                if (p > 1) {
                    p = 1;
                }


                int alphaN = (int) (255 * p);
                int redNew = (int) (247);
                int greenNew = (int) (249);
                int blueNew = (int) (250);

                int newColor = Color.argb(alphaN, redNew, greenNew, blueNew);

                subject_list_toolbar.setBackgroundColor(newColor);

                if (alphaN >= 128) {
                    subject_list_toolbar.setNavigationIcon(com.jeremy.lychee.R.mipmap.ic_nav_back);
                    if (shareItem != null) {
                        shareItem.setBackgroundResource(com.jeremy.lychee.R.drawable.zt_icon_share_black);
                    }
                } else {
                    if (shareItem != null) {
                        shareItem.setBackgroundResource(com.jeremy.lychee.R.drawable.zt_icon_share_white);
                    }
                    subject_list_toolbar.setNavigationIcon(com.jeremy.lychee.R.drawable.ic_actionbar_white_back);
                }

            }
        });

    }

    private void queryDataFromNet() {
        OldRetroAdapter.getService().getNewsSubjectList(nid, url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
//                .doOnNext(data -> cacheData(data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setViewAdapter, Throwable -> {
//                    swipeLayout.setRefreshing(false);
//                    recyclerView.scrollVerticallyToPosition(1);
                    if (loading_layout != null) {
                        loading_layout.setVisibility(View.GONE);
                    }

                    if (mAdapter.getmList() != null && mAdapter.getmList().size() == 0) {
                        error_layout.setVisibility(View.VISIBLE);
                    } else {
                        TopToastUtil.showTopToast(
                                recyclerView, String.format("网络不给力，请检查网络刷新重试"));
                    }

                });

    }


    private void setViewAdapter(ModelBase<NewsSubjectObject> listResult) {
//        swipeLayout.setRefreshing(false);
//        recyclerView.scrollVerticallyToPosition(1);
        if (listResult != null && listResult.getData() != null) {//网络有数据
            NewsSubjectObject data = listResult.getData();
            if (data != null) {
                List list = new ArrayList<>();
                list.add(new SubjectTopItem(data.getNews(), data.getTitle(), data.getSummary(), data.getTopic(),data.getImage(),data.getType(),data.getUrl()));

                List<SubjectColumnGroup> lanmulist = data.getLanmu();
                if (lanmulist == null) return;
                for (int i = 0; i < lanmulist.size(); i++) {
                    SubjectColumnGroup item = lanmulist.get(i);
                    if (item != null) {
                        list.add(item.getName());
                        list.addAll(item.getData());
                    }
                }

                if (loading_layout != null) {
                    loading_layout.setVisibility(View.GONE);
                }
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();


                toolbar_share.setOnClickListener(v -> {
                    //分享按钮
//                    ToastHelper.getInstance(this).toast(data.getTitle() + "的分享");
                    ShareInfo shareInfo = new ShareInfo(data.getShare(), nid, data.getTitle(), data.getSummary(), data.getImage(), null, ShareInfo.SHARECONTENT_LIVE);
                    new ShareManager(this, shareInfo, true,
                            () -> HitLog.hitLogShare("null", "null", nid, data.getNews().getNews_from() + "")) //分享打点
                            .show();
                });


            }
        } else {//网络无数据
            if (mAdapter.getmList() != null && mAdapter.getmList().size() == 0) {//本地无数据
//                mAdapter.showNoDataView();
                loading_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.GONE);
            } else {//本地有数据
                TopToastUtil.showTopToast(
                        recyclerView, String.format("暂时没有新的内容了…"));
            }
        }

    }

}
