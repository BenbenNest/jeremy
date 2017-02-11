package com.jeremy.lychee.fragment.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.activity.news.SearchActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.widget.LayoutManager.ContentRVLayoutManager;
import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.news.ContentRVAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.db.NewsListDataDao;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.preference.NewsListPreference;
import com.jeremy.lychee.presenter.ChannelFragmentPresenter;
import com.jeremy.lychee.presenter.ContentFragmentDbHelper;
import com.jeremy.lychee.utils.StringUtil;
import com.jeremy.lychee.utils.hitLog.HitLogHelper;
import com.jeremy.lychee.utils.transformer.MapWithIndex;
import com.jeremy.lychee.widget.LoadingRecyclerViewFooter;
import com.jeremy.lychee.widget.RecyclerViewDecoration.ContentRVDecoration;
import com.jeremy.lychee.widget.recyclerview.RecyclerViewWrapper;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;
import com.qihoo.sdk.report.QHStatAgent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentFragment extends BaseFragment {
    public final static String EXTRA_CHANNEL_ALIAS = "cid";
    public final static String EXTRA_CHANNEL_NAME = "cname";
    public final static String EXTRA_CHANNEL_SCENEID = "sceneId";
    public final static String EXTRA_CHANNEL_IS_SCENE = "isScene";
    public final static String EXTRA_IS_NEED_SEARCH_VIEW = "searchView";
    public final static String EXTRA_CHANNEL_TOP_IMAGE = "_top_image";
    public final static String EXTRA_CHANNEL_LAST_SCENE_NAME = "last_scene_name";
    public final static String EXTRA_CHANNEL_FIRST_REFRESH = "first_refresh";
    public final static String EXTRA_CHANNEL_NEWS_OBJECT = "NEW_ENTITY";
    public final long EXTRA_CHANNEL_LAST_REFRESH_TIME = 15 * 60 * 1000;

    @Bind(R.id.article_recycler_view)
    RecyclerViewWrapper recyclerViewWrapper;

    private int mCurrentMode = -1;
    private String cid;
    private String channelName = "";
    private Boolean isScene = false;
    private boolean isNeedSearchView;

    public String getSceneId() {
        return sceneId;
    }

    private String sceneId;
    private long mLastRefreshTime;
    private Boolean isInit = false;
    private Boolean canGoRefresh = false;
    private ContentRVAdapter adapter;
    private int freshCount = 1;
    private boolean isRefreshing = false;

    // 用于db的频道的queryfmHitLoggedNewsNormalCache
    private String channelQuery = "";
    //    private ConcurrentHashMap<String, NewsListData>
//            mHitLoggedNewsNormalCache = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<String, NewsListData>
//            mHitLoggedNewsSkickCache = new ConcurrentHashMap<>();
    private ContentRVLayoutManager layoutManager;
    private ContentRVDecoration contentRVDecoration;

    //打点
    private HashMap<String, NewsListData> mHitLogedNews = new HashMap<>();

    @Override
    public void onResume() {
        super.onResume();
        //hitlog cache reset
        if (mHitLogedNews != null && mHitLogedNews.size() != 0) {
            mHitLogedNews.clear();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewWrapper.setRefreshing(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QEventBus.getEventBus().register(this);
        cid = getArguments().getString(EXTRA_CHANNEL_ALIAS);
        channelName = getArguments().getString(EXTRA_CHANNEL_NAME);
        isScene = getArguments().getBoolean(EXTRA_CHANNEL_IS_SCENE);
        isNeedSearchView = getArguments().getBoolean(EXTRA_IS_NEED_SEARCH_VIEW, true);
        channelQuery = cid;
        if (isScene) {
            sceneId = getArguments().getString(EXTRA_CHANNEL_SCENEID);
            channelQuery += sceneId;
        }
        mLastRefreshTime = NewsListPreference.getInstance().getLongValue(channelQuery);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentMode = CommonPreferenceUtil.GetNewsType();
        View view = inflater.inflate(R.layout.content_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        initView();
        initHistory();
        return view;
    }

    @Override
    public void onDestroy() {
//        mHitLoggedNewsNormalCache.clear();
//        mHitLoggedNewsSkickCache.clear();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
        super.onDestroy();
    }


    private void initView() {
        recyclerViewWrapper.setBackgroundColor(Color.parseColor("#ffffff"));
        ContentRVAdapter.ContentRVOnItemClickListener listener = (view, position) -> {
            int itemViewType = adapter.getContentItemViewType(position);
            NewsListData data = null;
            switch (itemViewType) {
                case ContentRVAdapter.SEARCH_TYPE:
                    goToSearchActivity();
                    return;
                case ContentRVAdapter.LAST_REFRESH_TAG:
                    recyclerViewWrapper.setRefreshing(true);
                    layoutManager.smoothScrollToPosition(recyclerViewWrapper.mRecyclerView, null, 0);
                    goRefresh(true);
                    return;
                case ContentRVAdapter.FOCUS_NEWS_TYPE:
                    data = adapter.getFocusNewsData();
                    break;
                case ContentRVAdapter.IMAGE_NEWS_TYPE1:
                case ContentRVAdapter.IMAGE_NEWS_TYPE2:
                case ContentRVAdapter.NORMAL_NEWS_TYPE:
                case ContentRVAdapter.NO_PIC_NEWS_TYPE:
                case ContentRVAdapter.EX_FOCUS_NEWS_TAG:
                    List<NewsListData> list = adapter.getList();
                    int listPosition = adapter.getListPosition(position);
                    if (listPosition >= 0 && listPosition < list.size()) {
                        data = list.get(listPosition);
                    }
                    break;

            }
            if (data == null) {
                return;
            }
            if (getActivity() instanceof SlidingActivity) {
                ChannelFragmentPresenter.OpenNewsDetailActivity(
                        (SlidingActivity) getActivity(), data, sceneId, cid);
            }

            //点击新闻打点
            if ("1".equals(data.getNews_stick())
                    && Long.parseLong(data.getNews_stick_time()) > System.currentTimeMillis()) {
                HitLog.hitLogNewsListClick(sceneId, cid,
                        HitLog.POSITION_STICK_NEWS, data);
            } else {
                HitLog.hitLogNewsListClick(sceneId, cid,
                        HitLog.POSITION_NORMAL_NEWS, data);
            }
        };
        adapter = new ContentRVAdapter(getContext(), mCurrentMode, listener);
        adapter.setItemType(mCurrentMode);
        if (isScene) {
            adapter.setHasSearchView(true);
        } else if (!isNeedSearchView) {
            adapter.setHasSearchView(false);
        }
        layoutManager = new ContentRVLayoutManager(getContext(), getSpanCount(), adapter);
        recyclerViewWrapper.setLayoutManager(layoutManager);
        recyclerViewWrapper.setDefaultOnRefreshListener(() -> {
            refreshData();
            //上传现阶段打点信息
            HitLogHelper.getHitLogHelper().postLogToNet();
        });
        recyclerViewWrapper.setAdapter(adapter);
        recyclerViewWrapper.enableLoadMore();
        contentRVDecoration = new ContentRVDecoration(getSpanCount(), getDecorationOffset(), adapter);
        recyclerViewWrapper.addItemDecoration(contentRVDecoration);

        recyclerViewWrapper.setOnLoadMoreListener((itemsCount, maxLastVisiblePosition) -> loadHistoryData());
        //新闻列表显示打点
        recyclerViewWrapper.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Observable.just(newState)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .filter(s -> s == RecyclerView.SCROLL_STATE_IDLE)
                        .map(s -> adapter.getList())
                        .flatMap(Observable::from)
                        .compose(MapWithIndex.<NewsListData>instance())
                        .filter(s ->
                                s.index() >= recyclerViewWrapper.getVisibleDataPosition().first &&
                                        s.index() <= recyclerViewWrapper.getVisibleDataPosition().second)
                        .filter(s -> mHitLogedNews.get((s.value().getNid())) == null)
                        .doOnNext(s -> mHitLogedNews.put(s.value().getNid(), s.value()))
                        .map(MapWithIndex.Indexed::value)
                        .toList()
                        .subscribe(
                                s -> HitLog.hitLogNewsListShow(
                                        sceneId, StringUtil.isEmpty(sceneId) ? cid : null, HitLog.POSITION_NORMAL_NEWS, s),
                                Throwable::printStackTrace);
            }
        });
        adapter.setFooterClickListener(new LoadingRecyclerViewFooter.FooterClickListener() {
            @Override
            public void onFullRefresh() {
                layoutManager.smoothScrollToPosition(recyclerViewWrapper.mRecyclerView, null, 0);
                refreshData();
            }

            @Override
            public void onErrorClick() {
                loadHistoryData();
            }
        });
        recyclerViewWrapper.setErrorViewClickListener(v -> {
            refreshData();
            recyclerViewWrapper.showLoadingView();
        });
    }

    private void goToSearchActivity() {
        if (getContext() instanceof SlidingActivity) {
            SlidingActivity activity = (SlidingActivity) getContext();
            activity.openActivity(SearchActivity.class);
        }
    }

    private void initHistory() {
        Observable.just(getNewsHistory())
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (list != null && list.size() > 0) {
                        adapter.setList(list);
                        justCheckRefresh();
                    } else {
                        recyclerViewWrapper.showLoadingView();
                        canGoRefreshData(true);
                    }
                    hideSearchView();
                    isInit = true;
                }, throwable -> {
                    throwable.printStackTrace();
                    justCheckRefresh();
                    isInit = true;
                });
    }

    private List<NewsListData> getNewsHistory() {
        NewsListData validFocusNews = ContentFragmentDbHelper.getValidFocusNews(channelQuery);
        List<NewsListData> dataList = ContentFragmentDbHelper.getNewsHistory(channelQuery, 3, 0);
        if (validFocusNews != null && dataList != null) {
            dataList.add(0, validFocusNews);
        }
        return dataList;
    }


    private void refreshData() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        QHStatAgent.onEvent(getContext(), QHState.NEWS_REFRESH);
        recyclerViewWrapper.setRefreshing(true);
        getRightObservable()
                .compose(this.bindToLifecycle())
                .map(this::setItemProperty)
                .doOnNext(this::changeFocusNews)
                .doOnNext(list -> {
                    String uuid = UUID.randomUUID().toString();
                    for (NewsListData data : list) {
                        data.setUuid_flag(uuid);
                    }
                })
                .doOnNext(list -> ContentFragmentDbHelper.saveToDB(list, channelQuery))
                .subscribe(it -> {
                    recyclerViewWrapper.hideLoadingView();
                    recyclerViewWrapper.setRefreshing(false);
                    if (it.size() > 0 && adapter.getList() != null
                            && adapter.getList().size() > 0) {
                        adapter.setTagPosition(adapter.hasStickyFocusNews() ? it.size() + 1 : it.size());
                    }
                    if (it.size() > 0) {
                        mLastRefreshTime = System.currentTimeMillis();
                        String temp = cid;
                        if (isScene) {
                            temp += sceneId;
                        }
                        NewsListPreference.getInstance().saveLongValue(temp, mLastRefreshTime);
                        NewsListPreference.getInstance().saveBooleanValue(getRightKey(), false);
                        freshCount++;
                        TopToastUtil.showTopToast(
                                recyclerViewWrapper, String.format("为你推荐了%d条新鲜资讯", it.size()));
                    } else {
                        if (adapter.getList() == null || adapter.getList().size() == 0) {
                            adapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.full);
                        }
                        TopToastUtil.showTopToast(recyclerViewWrapper, "小编正在玩命赶稿中…");
                    }
                    adapter.insertList(0, it);
                    hideSearchView();
                    isRefreshing = false;
                    //Hitlog 2001
                    Observable.just(adapter.getList())
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .flatMap(Observable::from)
                            .compose(MapWithIndex.<NewsListData>instance())
                            .filter(s ->
                                    s.index() >= recyclerViewWrapper.getVisibleDataPosition().first &&
                                            s.index() <= recyclerViewWrapper.getVisibleDataPosition().second)
                            .filter(s -> mHitLogedNews.get((s.value().getNid())) == null)
                            .doOnNext(s -> mHitLogedNews.put(s.value().getNid(), s.value()))
                            .map(MapWithIndex.Indexed::value)
                            .toList()
                            .subscribe(
                                    s -> HitLog.hitLogNewsListShow(
                                            sceneId, StringUtil.isEmpty(sceneId) ? cid : null, HitLog.POSITION_NORMAL_NEWS, s),
                                    Throwable::printStackTrace);

                }, throwable -> {
                    TopToastUtil.showTopToast(recyclerViewWrapper, getResources().getString(R.string.top_toast_net_error));
                    throwable.printStackTrace();
                    recyclerViewWrapper.setRefreshing(false);
                    if (adapter.getList() == null || adapter.getList().size() == 0) {
                        recyclerViewWrapper.showErrorView();
                    }
                    isRefreshing = false;
                });
    }

    private void changeFocusNews(List<NewsListData> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        NewsListData data = adapter.getFocusNewsData();
        if (data == null) {
            return;
        }
        if ("1".equals(data.getNews_stick()) && !"1".equals(list.get(0).getIs_focus())) {
            return;
        }
        data.setIs_focus("0");
        ContentFragmentDbHelper.setFocusNewsStatus(data);
    }

    private void hideSearchView() {
        if (adapter.isHasSearchView()) {
            layoutManager.scrollToPositionWithOffset(1, 0);
        }
    }

    private Observable<List<NewsListData>> getRightObservable() {
        if (isScene) {
            Boolean temp = NewsListPreference.getInstance().getBooleanValue(getRightKey());
            return getNewsSceneList(cid, sceneId, temp ? "1" : "0");
        } else {
            Boolean temp = NewsListPreference.getInstance().getBooleanValue(getRightKey());
            return getNewsDataList(cid, temp ? "1" : "0");
        }
    }

    private Observable<List<NewsListData>> getNewsDataList(String channel, String init) {
        return OldRetroAdapter.getService().getNewsDataList(channel, init, getFreshCount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(it -> Observable.just(it.getData()));
    }

    private Observable<List<NewsListData>> getNewsSceneList(String channel, String sceneid, String init) {
        return OldRetroAdapter.getService().getNewsSceneList(channel, sceneid, init)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(it -> Observable.just(it.getData()));
    }

    private List<NewsListData> setItemProperty(List<NewsListData> list) {
        if (list != null) {
            String channel = cid;
            if (isScene) {
                channel += sceneId;
            }
            long time = System.currentTimeMillis();

            for (NewsListData item : list) {
                item.setNid(item.getNid());
                item.setPdate(String.valueOf(time));
                item.setTime(time);
                item.setChannel(channel);
                time--;
            }
        }
        return list;
    }

    private void justCheckRefresh() {
        if (canGoRefresh) {
            canGoRefreshData(false);
        }
    }

    private String getRightKey() {
        return isScene ? cid + sceneId + EXTRA_CHANNEL_FIRST_REFRESH : cid + EXTRA_CHANNEL_FIRST_REFRESH;
    }

    private void canGoRefreshData(Boolean forceRefresh) {
        if (forceRefresh) {
            refreshData();
        } else {
            long now = System.currentTimeMillis();
            if (now - mLastRefreshTime > EXTRA_CHANNEL_LAST_REFRESH_TIME) {
                refreshData();
            }
        }
    }

    public void goRefresh() {
        goRefresh(false);
    }

    public void goRefresh(Boolean forceRefresh) {
        if (isInit) {
            canGoRefreshData(forceRefresh);
        } else {
            canGoRefresh = true;
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCid() {
        return cid;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setCidAndSceneID(String channelAlias, String sceneId) {
        this.cid = channelAlias;
        this.sceneId = sceneId;
        String nowName = channelAlias + this.sceneId;
        channelQuery = channelAlias + this.sceneId;
        String lastSceneName = NewsListPreference.getInstance().getStringValue(EXTRA_CHANNEL_LAST_SCENE_NAME);
        if (!TextUtils.isEmpty(lastSceneName)) {
            if (!nowName.equals(lastSceneName)) {
                resetListData(lastSceneName);
                clearSceneData(lastSceneName);
            }
        }
        NewsListPreference.getInstance().saveStringValue(EXTRA_CHANNEL_LAST_SCENE_NAME, nowName);

        //hitlog cache reset
        if (mHitLogedNews != null && mHitLogedNews.size() != 0) {
            mHitLogedNews.clear();
        }
    }

    public void resetListData(String sceneName) {
        if (isScene) {
            recyclerViewWrapper.getLayoutManager().removeAllViews();
            adapter.clear();
            mLastRefreshTime = 0;
            NewsListPreference.getInstance().saveLongValue(sceneName, mLastRefreshTime);
        }
    }

    private void clearSceneData(String cidAndSceneid) {
        Schedulers.io().createWorker().schedule(() -> {
            try {
                NewsListPreference.getInstance().saveLongValue(cidAndSceneid, 0L);
                NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
                List<NewsListData> list = dao.queryBuilder().where(NewsListDataDao.Properties.Channel.eq(cidAndSceneid)).build().list();
                dao.deleteInTx(list);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void loadHistoryData() {
        int size = 0;
        if (adapter.getList() != null) {
            size = adapter.getFocusNewsData() == null ? adapter.getList().size() :
                    adapter.getList().size() - 1;
        }
        Observable.just(ContentFragmentDbHelper.getNewsHistory(channelQuery, 3, size))
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsHistory -> {
                    if (newsHistory == null || newsHistory.size() == 0) {
                        recyclerViewWrapper.setIsFullData(true);
                    } else {
                        adapter.insertList(newsHistory);
                    }
                }, Throwable::printStackTrace);
    }

    public Observable<List<NewsListData>> getImageHistoryObservable() {
        return Observable.create(new Observable.OnSubscribe<List<NewsListData>>() {
            @Override
            public void call(Subscriber<? super List<NewsListData>> subscriber) {
                if (isScene) {
                    subscriber.onCompleted();
                }
                NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
                List<NewsListData> list = dao.queryBuilder().where(NewsListDataDao.Properties.Channel.eq(cid + EXTRA_CHANNEL_TOP_IMAGE)).build().list();
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    private int getSpanCount() {
        switch (mCurrentMode) {
            case 1:
                return 2;
            case 0:
            default:
                return 1;
//            case 2:
//                return 2;
//            case 3:
//            case 4:
//            default:
//                return 2;

        }
    }

    private int getDecorationOffset()

    {
        switch (mCurrentMode) {
            case 0:
                return DensityUtils.dip2px(getContext(), 8);
            case 1:
            default:
                return DensityUtils.dip2px(getContext(), 2f);

        }
    }

    public void onEventMainThread(Events.NewsLayoutChangeEvent event) {
        if (event == null || mCurrentMode == event.level) {
            return;
        }
        mCurrentMode = event.level;
        setListMode(event.level);
    }

    public void setListMode(int mode) {
        int position = layoutManager.findFirstVisibleItemPosition();
        layoutManager.removeAllViews();
        layoutManager.setSpanCount(getSpanCount());
        recyclerViewWrapper.removeItemDecoration(contentRVDecoration);
        contentRVDecoration = new ContentRVDecoration(getSpanCount(), getDecorationOffset(), adapter);
        recyclerViewWrapper.addItemDecoration(contentRVDecoration);
        adapter.setItemType(mode);
        adapter.notifyDataSetChanged();
        recyclerViewWrapper.setAdapter(adapter);
        layoutManager.scrollToPositionWithOffset(position, 0);
    }

    private int getFreshCount() {
        if (System.currentTimeMillis() - mLastRefreshTime > 3 * 60 * 60 * 1000) {
            freshCount = 1;
        }
        return freshCount;
    }
}
