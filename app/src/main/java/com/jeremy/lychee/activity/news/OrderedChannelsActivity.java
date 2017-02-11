package com.jeremy.lychee.activity.news;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.activity.user.SettingsActivity;
import com.jeremy.lychee.adapter.OnStartDragListener;
import com.jeremy.lychee.adapter.OrderChannelItemTouchHelperCallback;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.db.NewsChannelDao;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.widget.LayoutManager.WrappableGridLayoutManager;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.adapter.news.NewsChannelRVAdapter;
import com.jeremy.lychee.utils.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderedChannelsActivity extends SlidingActivity implements OnStartDragListener {

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar toolbar;
    @Bind(com.jeremy.lychee.R.id.ordered_channel_recyclerView)
    RecyclerView orderChannelRv;
    @Bind(com.jeremy.lychee.R.id.unordered_channel_recyclerView)
    RecyclerView unorderedChannelRv;
    @Bind(com.jeremy.lychee.R.id.one_item_news_layout)
    LinearLayout oneItemNewsLayout;
    @Bind(com.jeremy.lychee.R.id.two_item_news_layout)
    LinearLayout twoItemNewsLayout;
    @Bind(com.jeremy.lychee.R.id.status_txt)
    TextView statusTxt;
    private String lastCids = "";
    private ItemTouchHelper mItemTouchHelper;
    private NewsChannelRVAdapter orderedChannelAdapter;
    private NewsChannelRVAdapter unorderedChannelAdapter;
    private String orderCids;
    private String[] orderedCidArrays;
    private String currentCid;



    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_ordered_channels);
        QEventBus.getEventBus().register(this);
        currentCid = getIntent().getStringExtra("currentCid");
        ButterKnife.bind(this);
        initToolbar();

        getOrderedChannelCids()
                .compose(bindToLifecycle())
                .flatMap(this::getOrderChannelItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    orderedChannelAdapter = new NewsChannelRVAdapter(this, pair.first, this, orderedChannelOnItemClickListener);
                    orderedChannelAdapter.setOnItemDismissListener((position, newsChannel) -> {
                        unorderedChannelAdapter.add(0, newsChannel);
                    });
                    orderedChannelAdapter.setCurrentCid(currentCid);
                    WrappableGridLayoutManager layoutManager = new WrappableGridLayoutManager(this, 4);
                    orderChannelRv.setLayoutManager(layoutManager);
                    orderChannelRv.setAdapter(orderedChannelAdapter);
                    ItemTouchHelper.Callback callback = new OrderChannelItemTouchHelperCallback(orderedChannelAdapter);
                    mItemTouchHelper = new ItemTouchHelper(callback);
                    mItemTouchHelper.attachToRecyclerView(orderChannelRv);

                    unorderedChannelAdapter = new NewsChannelRVAdapter(this, pair.second, null, unorderedChannelOnItemClickListener);
                    layoutManager = new WrappableGridLayoutManager(this, 4);
                    unorderedChannelRv.setLayoutManager(layoutManager);
                    unorderedChannelRv.setAdapter(unorderedChannelAdapter);
                }, Throwable::printStackTrace);
        getOrderedChannelCids()
                .subscribe(strings -> orderCids = getOrderCids(strings), Throwable::printStackTrace);
        int type = CommonPreferenceUtil.GetNewsType();
        if (type == 0) {
            oneItemNewsLayout.setSelected(true);
            twoItemNewsLayout.setSelected(false);
        } else {
            oneItemNewsLayout.setSelected(false);
            twoItemNewsLayout.setSelected(true);
        }
    }

    @Override
    protected void onDestroy() {
        QEventBus.getEventBus().unregister(this);
        super.onDestroy();
    }

    private NewsChannelRVAdapter.OnItemClickListener orderedChannelOnItemClickListener = new NewsChannelRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            boolean editable = orderedChannelAdapter.isEditable();
            if (!editable) {
                finish();
                QEventBus.getEventBus().post(new Events.UpdateNewsChannelListEvent(position));
            } else if (position != 0) {
                NewsChannel newsChannel = orderedChannelAdapter.remove(position);
                unorderedChannelAdapter.add(0, newsChannel);
            }
        }
    };
    private NewsChannelRVAdapter.OnItemClickListener unorderedChannelOnItemClickListener = new NewsChannelRVAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            NewsChannel newsChannel = unorderedChannelAdapter.remove(position);
            if (newsChannel != null) {
                orderedChannelAdapter.add(newsChannel);
            }
        }
    };

    private String getOrderCids(String[] cids) {
        String orderCids = "";
        Arrays.sort(cids);
        for (String cid : cids) {
            orderCids += cid + "|";
        }
        if (!orderCids.isEmpty()) {
            orderCids = orderCids.substring(0, orderCids.length() - 1);
        }
        return orderCids;
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setTitle("频道管理");
        toolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_channel_manager);
        findViewById(com.jeremy.lychee.R.id.menu_item_setting).setOnClickListener(v -> {
            openActivity(SettingsActivity.class);
        });
    }

    private Observable<Pair<ArrayList<NewsChannel>, ArrayList<NewsChannel>>> getOrderChannelItem(String[] newsChannelCids) {
        NewsChannelDao dao = ContentApplication.getDaoSession().getNewsChannelDao();
        List<NewsChannel> newsChannelList = dao.queryBuilder().build().list();
        if (newsChannelCids == null || newsChannelList.size() == 0) {
            return Observable.just(new Pair<>(new ArrayList<>(), new ArrayList<>()));
        }
        ArrayList<NewsChannel> orderedChannelList = new ArrayList<>();
        ArrayList<NewsChannel> unorderedChannelList = new ArrayList<>();
        for (NewsChannel newsChannel : newsChannelList) {
            boolean flag = false;
            for (String cid : newsChannelCids) {
                if (newsChannel.getCid().equals(cid)) {

                    flag = true;
                    break;
                }
            }
            if (flag) {
                orderedChannelList.add(newsChannel);
            } else {
                unorderedChannelList.add(newsChannel);
            }

        }
        return Observable.just(new Pair<>(orderedChannelList, unorderedChannelList));
    }

    private Observable<String[]> getOrderedChannelCids() {
        return Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                if (orderedCidArrays == null || orderedCidArrays.length == 0) {
                    NewsChannelPreference newsChannelPreference = new NewsChannelPreference();
                    lastCids = newsChannelPreference.getNewsChannelCids();
                    if (TextUtils.isEmpty(lastCids)) {
                        subscriber.onCompleted();
                    }
                    orderedCidArrays = lastCids.split("\\|");
                }
                subscriber.onNext(orderedCidArrays);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    @OnClick({com.jeremy.lychee.R.id.one_item_news_layout, com.jeremy.lychee.R.id.two_item_news_layout})
    public void oneItemStyleLayout(View view) {
        String tag = (String) view.getTag();
        int type = Integer.parseInt(tag);
        if (CommonPreferenceUtil.GetNewsType() == type) {
            return;
        }
        CommonPreferenceUtil.SetNewsType(type);
        oneItemNewsLayout.setSelected(type == 0);
        twoItemNewsLayout.setSelected(type == 1);
        QEventBus.getEventBus().post(new Events.NewsLayoutChangeEvent(type));
    }

    @OnClick(com.jeremy.lychee.R.id.status_txt)
    public void onStatusClick(TextView statusTxt) {
        if (orderedChannelAdapter == null) {
            return;
        }
        boolean editable = orderedChannelAdapter.isEditable();
        statusTxt.setText(editable ? "编辑" : "完成");
        orderedChannelAdapter.setEditable(!editable);
    }

    public void onEventMainThread(Events.ChangeNewsStatusEvent event) {
        if (event == null) {
            return;
        }
        statusTxt.setText(event.isEdited?"完成": "编辑");
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onBackPressed() {
        if (orderedChannelAdapter.isEditable()) {
            orderedChannelAdapter.setEditable(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        if (orderedChannelAdapter == null) {
            super.finish();
            return;
        }
        List<NewsChannel> newsChannelList = orderedChannelAdapter.getNewsChannelList();
        if (newsChannelList != null) {
            String cids = "";
            for (NewsChannel newsChannel : newsChannelList) {
                cids += newsChannel.getCid() + "|";
            }
            NewsChannelPreference newsChannelPreference = new NewsChannelPreference();
            newsChannelPreference.saveNewsChannelCids(cids);
            SyncCids(cids);
            if (!cids.equals(lastCids)) {
                CommonPreferenceUtil.SetModifyNewsChannel(true);
                QEventBus.getEventBus().post(new Events
                        .UpdateNewsChannelListEvent(-1));
            }
        }
        super.finish();
    }

    private void SyncCids(String cids) {
        Observable.just(cids)
                .map(s -> s.split("\\|"))
                .map(this::getOrderCids)
                .filter(s -> !s.equals(orderCids))
                .subscribe(s -> {
                    OldRetroAdapter.getService().newsChannelAsyn(s, AppUtil.getSyChannelSign(s))
                            .subscribeOn(Schedulers.io())
                            .subscribe(data -> {
                            }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }
}
