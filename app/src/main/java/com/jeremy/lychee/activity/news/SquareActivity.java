package com.jeremy.lychee.activity.news;

import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jeremy.lychee.widget.LoadingRecyclerViewFooter;
import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.news.SquareAdapter;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;
import com.jeremy.lychee.widget.recyclerview.RecyclerViewWrapper;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SquareActivity extends SlidingActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_wrapper)
    RecyclerViewWrapper recyclerView;
    @Bind(R.id.top_img)
    ImageView topImg;

    private SquareAdapter adapter;
    private int position = 1;
    private boolean topImgVisible = false;

    //ActivityAnimation
    private boolean animReverse = false;
    private int activityOpenEnterAnimation;
    private int activityOpenExitAnimation;
    private int activityCloseEnterAnimation;
    private int activityCloseExitAnimation;
    private LinearLayoutManager layoutManager;

    @SuppressWarnings("ResourceType")
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_square);
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        recyclerView.showLoadingView();
        firstRefresh();

        if (animReverse) {
            //某些系统下，style中定义的ActivityAnimation无效
            //故增加代码动态设定
            TypedArray activityStyle;
            activityStyle = getTheme().obtainStyledAttributes(
                    R.style.ReverseAnimationActivity, new int[]{
                            android.R.attr.activityOpenEnterAnimation,
                            android.R.attr.activityOpenExitAnimation,
                            android.R.attr.activityCloseEnterAnimation,
                            android.R.attr.activityCloseExitAnimation});
            activityOpenEnterAnimation = activityStyle.getResourceId(0, 0);
            activityOpenExitAnimation = activityStyle.getResourceId(1, 0);
            activityCloseEnterAnimation = activityStyle.getResourceId(2, 0);
            activityCloseExitAnimation = activityStyle.getResourceId(3, 0);
            overridePendingTransition(activityOpenEnterAnimation, activityOpenExitAnimation);
            activityStyle.recycle();
        }
    }

    private void initToolbar() {
        toolbar.setTitle("广场");
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        adapter = new SquareAdapter(this, null);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setDefaultOnRefreshListener(this::firstRefresh);
        recyclerView.enableLoadMore();
        recyclerView.setOnLoadMoreListener((itemsCount, maxLastVisiblePosition) -> updateData(position));
        recyclerView.setErrorViewClickListener(v -> {
            firstRefresh();
            recyclerView.hideErrorView();
            recyclerView.showLoadingView();
        });
        adapter.setFooterClickListener(new LoadingRecyclerViewFooter.FooterClickListener() {
            @Override
            public void onFullRefresh() {
                recyclerView.getLayoutManager().scrollToPosition(0);
                recyclerView.setIsFullData(false);
                firstRefresh();
            }

            @Override
            public void onErrorClick() {
                updateData(position);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findFirstVisibleItemPosition() >= 2
                        && !topImgVisible) {
                    topImgVisible = true;
                    topImg.setVisibility(View.VISIBLE);
                } else if (layoutManager.findFirstVisibleItemPosition() < 2
                        && topImgVisible) {
                    topImgVisible = false;
                    topImg.setVisibility(View.GONE);
                }
            }
        });
    }

    private void firstRefresh() {
        updateData(1);
    }

    private void updateData(int position) {
        this.position = position;
        OldRetroAdapter.getService().getSquareData(position)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .map(ModelBase::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (position == 1 && adapter.getContentItemCount() > 0) {
                        TopToastUtil.showTopToast(recyclerView, "又有新的内容啦");
                    }
                    this.position = model.getPos();
                    recyclerView.setRefreshing(false);
                    addData(model, position);
                }, this::onError);
    }

    private void addData(SquareModel model, int position) {
        List<SquareModel.ModularModel> list = model.getList();
        if (list == null || list.size() == 0) {
            recyclerView.setIsFullData(true);
            adapter.notifyDataSetChanged();
        } else if (position == 1) {
            recyclerView.hideErrorView();
            recyclerView.hideLoadingView();
            adapter.setDataList(model.getList());
        } else {
            adapter.insertList(model.getList());
        }
    }

    private void onError(Throwable throwable) {
        recyclerView.setRefreshing(false);
        throwable.printStackTrace();
        if (adapter.getContentItemCount() == 0) {
            recyclerView.showErrorView();
        } else {
            recyclerView.showLoadMoreError();
            TopToastUtil.showTopToast(recyclerView, "网络不给力，请检查网络刷新重试");
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (animReverse) {
            overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
        }
    }

    @OnClick(R.id.top_img)
    public void goToTop() {
        recyclerView.mRecyclerView.smoothScrollToPosition(0);
    }
}

