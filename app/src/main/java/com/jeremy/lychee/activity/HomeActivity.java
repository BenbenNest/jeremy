package com.jeremy.lychee.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.camnter.easyrecyclerview.widget.EasyRecyclerView;
import com.camnter.easyrecyclerview.widget.decorator.EasyBorderDividerItemDecoration;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.function.FunctionListActivity;
import com.jeremy.lychee.adapter.MainAdapter;
import com.jeremy.lychee.bean.BaseGankData;
import com.jeremy.lychee.bean.GankDaily;
import com.jeremy.lychee.core.BaseDrawerLayoutActivity;
import com.jeremy.lychee.gank.GankType;
import com.jeremy.lychee.gank.GankTypeDict;
import com.jeremy.lychee.presenter.presenter.MainPresenter;
import com.jeremy.lychee.presenter.presenter.iview.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class HomeActivity extends BaseDrawerLayoutActivity implements MainView, MainAdapter.OnClickListener {

    @Bind(R.id.main_rv)
    EasyRecyclerView mainRv;

    private EasyBorderDividerItemDecoration dataDecoration;
    private EasyBorderDividerItemDecoration welfareDecoration;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private MainAdapter mainAdapter;
    private MainPresenter presenter;

    private int emptyCount = 0;
    private static final int EMPTY_LIMIT = 5;

    private int gankType;

    /**
     * Fill in layout id
     *
     * @return layout id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    /**
     * 刷新的时候
     */
    @Override
    public void onSwipeRefresh() {
        this.refreshData(this.gankType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_about:
//                AboutActivity.startActivity(this);
                return true;
            case R.id.menu_main_home_page:
//                EasyWebViewActivity.toUrl(this, GankApi.GANK_HOME_PAGE_URL, GankApi.GANK_HOME_PAGE_NAME);
                return true;
            case R.id.menu_main_top_github:
//                EasyWebViewActivity.toUrl(this, Constant.GIHUB_TRENDING, Constant.GIHUB_TRENDING);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.dataDecoration = new EasyBorderDividerItemDecoration(
                this.getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
                this.getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans)
        );
        this.welfareDecoration = new EasyBorderDividerItemDecoration(
                this.getResources().getDimensionPixelOffset(R.dimen.welfare_border_divider_height),
                this.getResources().getDimensionPixelOffset(R.dimen.welfare_border_padding_infra_spans)
        );
        this.mainRv.addItemDecoration(this.dataDecoration);
        this.mLinearLayoutManager = (LinearLayoutManager) this.mainRv.getLayoutManager();
        this.mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        this.mActionBarHelper.setDrawerTitle(this.getResources().getString(R.string.app_menu));

//        UmengUpdateAgent.update(this);
    }

    /**
     * Initialize the View of the listener
     */
    @Override
    protected void initListeners() {
        this.mainRv.addOnScrollListener(this.getRecyclerViewOnScrollListener());
        this.mainAdapter.setOnItemClickListener((view, position) -> {
            Object o = HomeActivity.this.mainAdapter.getItem(position);
            if (o instanceof BaseGankData) {
                BaseGankData baseGankData = (BaseGankData) o;
                if (GankTypeDict.urlType2TypeDict.get(baseGankData.type) == GankType.welfare) {
//                    PictureActivity.startActivityByActivityOptionsCompat(MainActivity.this, baseGankData.url, baseGankData.desc, view);
                } else {
//                    EasyWebViewActivity.toUrl(
//                            HomeActivity.this,
//                            baseGankData.url,
//                            baseGankData.desc,
//                            baseGankData.type
//                    );
                }
            } else if (o instanceof GankDaily) {
                GankDaily daily = (GankDaily) o;
                HomeActivity.this.presenter.getDailyDetail(daily.results);
            }
        });
    }

    /**
     * LinearLayoutManager 时的滚动监听
     *
     * @return RecyclerView.OnScrollListener
     */
    public RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            private boolean toLast = false;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /*
                 * dy 表示y轴滑动方向
                 * dx 表示x轴滑动方向
                 */
                if (dy > 0) {
                    // 正在向下滑动
                    this.toLast = true;
                } else {
                    // 停止滑动或者向上滑动
                    this.toLast = false;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    // 不滚动
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // 最后完成显示的item的position 正好是 最后一条数据的index
                        if (toLast && manager.findLastCompletelyVisibleItemPosition() == (manager.getItemCount() - 1)) {
                            HomeActivity.this.loadMoreRequest();
                        }
                    }
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                    // 不滚动
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        /*
                         * 由于是StaggeredGridLayoutManager
                         * 取最底部数据可能有两个item，所以判断这之中有一个正好是 最后一条数据的index
                         * 就OK
                         */
                        int[] bottom = manager.findLastCompletelyVisibleItemPositions(new int[2]);
                        int lastItemCount = manager.getItemCount() - 1;
                        if (toLast && (bottom[0] == lastItemCount || bottom[1] == lastItemCount)) {
                            HomeActivity.this.loadMoreRequest();
                        }
                    }
                }

            }
        };
    }

    /**
     * 请求加载更多
     */
    private void loadMoreRequest() {
        // 没数据了
        if (this.emptyCount >= EMPTY_LIMIT) {
            this.showToast(HomeActivity.this.getString(R.string.main_empty_data), Toast.LENGTH_LONG);
            return;
        }

        // 如果没在刷新中
        if (!HomeActivity.this.isRefreshStatus()) {
            // 加载更多
            this.presenter.setPage(HomeActivity.this.presenter.getPage() + 1);
            this.setRefreshStatus(false);
            this.loadMore(HomeActivity.this.gankType);
            this.refresh(true);
        }
    }

    /**
     * 加载更多
     *
     * @param gankType gankType
     */
    private void loadMore(int gankType) {
        switch (gankType) {
            case GankType.daily:
                this.presenter.getDaily(false, GankTypeDict.DONT_SWITCH);
                break;
            case GankType.android:
            case GankType.ios:
            case GankType.js:
            case GankType.resources:
            case GankType.welfare:
            case GankType.video:
            case GankType.app:
                this.presenter.getData(this.gankType, false, GankTypeDict.DONT_SWITCH);
                break;
        }
    }

    /**
     * Initialize the Activity data
     */
    @Override
    protected void initData() {
        this.presenter = new MainPresenter();
        this.presenter.attachView(this);
        this.gankType = GankType.daily;
        // 默认是每日干货
        this.mainAdapter = new MainAdapter(this, this.gankType);
        this.mainAdapter.setListener(this);
        this.mainRv.setAdapter(this.mainAdapter);

        this.refreshData(this.gankType);

        this.mainRv.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新 or 下拉刷新
     */
    private void refreshData(int gankType) {
        this.presenter.setPage(1);
        new Handler().post(() -> HomeActivity.this.refresh(true));
        switch (gankType) {
            case GankType.daily:
                this.presenter.getDaily(true, GankTypeDict.DONT_SWITCH);
                break;
            case GankType.android:
            case GankType.ios:
            case GankType.js:
            case GankType.resources:
            case GankType.welfare:
            case GankType.video:
            case GankType.app:
                this.presenter.getData(this.gankType, true, GankTypeDict.DONT_SWITCH);
                break;
        }
    }

    /**
     * 查询每日干货成功
     *
     * @param dailyData dailyData
     * @param refresh   是否刷新
     */
    @Override
    public void onGetDailySuccess(List<GankDaily> dailyData, boolean refresh) {
        if (refresh) {
            this.emptyCount = 0;
            this.mainAdapter.clear();
            this.mainAdapter.setList(dailyData);
        } else {
            this.mainAdapter.addAll(dailyData);
        }
        this.mainAdapter.notifyDataSetChanged();
        this.refresh(false);
        if (dailyData.size() == 0) this.emptyCount++;
    }

    /**
     * 查询 ( Android、iOS、前端、拓展资源、福利、休息视频 ) 成功
     *
     * @param data    data
     * @param refresh 是否刷新
     */
    @Override
    public void onGetDataSuccess(List<BaseGankData> data, boolean refresh) {
        if (refresh) {
            this.emptyCount = 0;
            this.mainAdapter.clear();
            this.mainAdapter.setList(data);
        } else {
            this.mainAdapter.addAll(data);
        }
        this.mainAdapter.notifyDataSetChanged();
        this.refresh(false);
        if (data.size() == 0) this.emptyCount++;
    }

    /**
     * 切换数据源成功
     *
     * @param type type
     */
    @Override
    public void onSwitchSuccess(int type) {
        this.emptyCount = 0;
        this.mainAdapter.setType(type);
        this.mainAdapter.clear();
        this.gankType = type;

        // 重置LayoutManager 和 分割线
        switch (gankType) {
            case GankType.daily:
            case GankType.android:
            case GankType.ios:
            case GankType.js:
            case GankType.resources:
            case GankType.video:
            case GankType.app:
                // 防止重复添加一样的
                this.clearDecoration();
                this.mainRv.setLayoutManager(this.mLinearLayoutManager);
                this.mainRv.addItemDecoration(this.dataDecoration);
                break;
            case GankType.welfare:
                this.clearDecoration();
                this.mainRv.setLayoutManager(this.mStaggeredGridLayoutManager);
                this.mainRv.addItemDecoration(this.welfareDecoration);
                break;
        }
    }

    /**
     * 获取每日详情数据
     *
     * @param title  title
     * @param detail detail
     */
    @Override
    public void getDailyDetail(String title, ArrayList<ArrayList<BaseGankData>> detail) {
//        DailyDetailActivity.startActivity(this, title, detail);
    }


    private void clearDecoration() {
        this.mainRv.removeItemDecoration(this.dataDecoration);
        this.mainRv.removeItemDecoration(this.welfareDecoration);
    }

    /**
     * 发生错误
     *
     * @param e e
     */
    @Override
    public void onFailure(Throwable e) {
        this.refresh(false);
        this.setRefreshStatus(true);
        Snackbar.make(this.mainRv, R.string.main_load_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        this.presenter.detachView();
        super.onDestroy();
    }

    /**
     * Fill in NavigationView.OnNavigationItemSelectedListener
     *
     * @return NavigationView.OnNavigationItemSelectedListener
     */
    @Override
    protected NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return item -> HomeActivity.this.menuItemChecked(item.getItemId());
    }

    /**
     * Fill in NavigationView menu ids
     *
     * @return int[]
     */
    @Override
    protected int[] getMenuItemIds() {
        return GankTypeDict.menuIds;
    }

    /**
     * Fill in your menu operation on click
     * <p>
     * 走到这，就不会有两次点击都一样的情况
     * Come to this, there would be no two clicks are all the same
     *
     * @param now Now you choose the item
     */
    @Override
    protected void onMenuItemOnClick(MenuItem now) {
        if (now.getItemId() == R.id.navigation_function) {
            FunctionListActivity.startActivity(HomeActivity.this);
            return;
        }
        if (GankTypeDict.menuId2TypeDict.indexOfKey(now.getItemId()) >= 0)
            this.changeGankType(GankTypeDict.menuId2TypeDict.get(now.getItemId()));
    }

    /**
     * 走到这，就不会有两次点击都一样的情况
     * Come to this, there would be no two clicks are all the same
     *
     * @param gankType gankType
     */
    private void changeGankType(int gankType) {
        this.refresh(true);
        this.presenter.switchType(gankType);
    }

    @Override
    public void onClickPicture(String url, String title, View view) {
//        PictureActivity.startActivityByActivityOptionsCompat(this, url, title, view);
    }

    /*********
     * Umeng *
     *********/

    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    /*********
     * Umeng *
     *********/
}
