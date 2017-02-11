package com.jeremy.lychee.activity.live;

import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jeremy.lychee.adapter.live.UnSelectChannelAdapter;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.adapter.OnStartDragListener;
import com.jeremy.lychee.adapter.OrderChannelItemTouchHelperCallback;
import com.jeremy.lychee.adapter.live.SelectChannelAdapter;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.RecyclerViewDecoration.DividerGridItemDecoration;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by daibangwen-xy on 16/2/29.
 */
public class SelectChannelActivity extends SlidingActivity implements OnStartDragListener {

    @Bind(com.jeremy.lychee.R.id.selected_channel_recyclerView)
    RecyclerView selected_channel_recyclerView;
    @Bind(com.jeremy.lychee.R.id.unselected_channel_recyclerView)
    RecyclerView unselected_channel_recyclerView;

    @Bind(com.jeremy.lychee.R.id.mainview)
    RelativeLayout mainview;

    @Bind(com.jeremy.lychee.R.id.toolbarview)
    View toolbarview;

    private SelectChannelAdapter selectChannelAdapter;
    private UnSelectChannelAdapter unselectChannelAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private List<ColumnChannel> selectedList;
    private List<ColumnChannel> allList;
    private List<ColumnChannel> unselectedList;
    private List<String> forbidDelCids;//不允许删除的channelid
    private List<String> forbidMoveCids;//不允许移动的channelid
    private DividerGridItemDecoration selectDividerGridItemDecoration;

    private int selectChannalPosition = 0;
    private String selectChannel_ChannelId = "";

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_select_channel);
        ButterKnife.bind(this);
        QEventBus.getEventBus().register(this);
        selectedList = new ArrayList<>();
        unselectedList = new ArrayList<>();
        forbidDelCids = new ArrayList<>();
        forbidMoveCids = new ArrayList<>();
        initToolbar("");
        initView();
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        if (getIntent() != null) {
            selectedList = (List<ColumnChannel>) getIntent().getSerializableExtra("selectedChannels");
            allList = (List<ColumnChannel>) getIntent().getSerializableExtra("allchannels");
            if (selectedList.size() > 0) {
                for (ColumnChannel channel : selectedList) {
                    if (!channel.getChannel_type().equals(ColumnChannel.ChannelType.NOMAL)) {
                        forbidDelCids.add(channel.getChannel_id());
                        if (channel.getChannel_type().equals(ColumnChannel.ChannelType.UNORDERDELANDMOVE)) {
                            forbidMoveCids.add(channel.getChannel_id());
                        }
                    }
                }
                selectChannelAdapter.setSelectedList(selectedList);
                selectChannelAdapter.setForbidDelCids(forbidDelCids);
                selectChannelAdapter.setForbidMoveCids(forbidMoveCids);
                selectChannelAdapter.notifyDataSetChanged();
                setSelectedListParm();
            }
            if (selectedList.size() > 0 && allList.size() > 0) {
                for (int i = 0; i < allList.size(); i++) {
                    int index = 0;
                    for (int j = 0; j < selectedList.size(); j++) {
                        if (!allList.get(i).getChannel_id().equals(selectedList.get(j).getChannel_id())) {
                            index++;
                        }
                    }
                    if ((index == selectedList.size())) {
                        unselectedList.add(allList.get(i));
                    }
                }
                unselectChannelAdapter.setUnSelectedList(unselectedList);
                unselectChannelAdapter.notifyDataSetChanged();
                setUnselectedListParm();
            }
        }
    }

    private void initView() {
        selectChannelAdapter = new SelectChannelAdapter(this, selectedList, this);
        unselectChannelAdapter = new UnSelectChannelAdapter(this, unselectedList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        selected_channel_recyclerView.setLayoutManager(layoutManager);
        selectDividerGridItemDecoration = new DividerGridItemDecoration(this);
        selected_channel_recyclerView.addItemDecoration(selectDividerGridItemDecoration);
        selected_channel_recyclerView.setAdapter(selectChannelAdapter);
        ItemTouchHelper.Callback callback = new OrderChannelItemTouchHelperCallback(selectChannelAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(selected_channel_recyclerView);

        GridLayoutManager unSelectChannellayoutManager = new GridLayoutManager(this, 3);
        unselected_channel_recyclerView.setLayoutManager(unSelectChannellayoutManager);
        unselected_channel_recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        unselected_channel_recyclerView.setAdapter(unselectChannelAdapter);

    }

    private void initToolbar(String title) {
        configToolbar(com.jeremy.lychee.R.menu.menu_selectchannel, title);
    }

    public Toolbar configToolbar(int menuId, String columnName) {
        Toolbar toolbar = (Toolbar) toolbarview.findViewById(com.jeremy.lychee.R.id.toolbar);
        if (menuId != 0) {
            toolbar.inflateMenu(menuId);
        }
        if (!TextUtils.isEmpty(columnName)) {
            toolbar.setTitle(columnName);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        findViewById(com.jeremy.lychee.R.id.menu_editover).setVisibility(View.GONE);
        findViewById(com.jeremy.lychee.R.id.menu_edit_channel).setOnClickListener(v -> {
            selectChannelAdapter.setEditable(true);
            findViewById(com.jeremy.lychee.R.id.menu_edit_channel).setVisibility(View.GONE);
            findViewById(com.jeremy.lychee.R.id.menu_editover).setVisibility(View.VISIBLE);
        });
        findViewById(com.jeremy.lychee.R.id.menu_editover).setOnClickListener(v -> {
            selectChannelAdapter.setEditable(false);
            findViewById(com.jeremy.lychee.R.id.menu_edit_channel).setVisibility(View.VISIBLE);
            findViewById(com.jeremy.lychee.R.id.menu_editover).setVisibility(View.GONE);

            if (selectChannelAdapter == null) {
                return;
            }
            if (selectChannelAdapter.isEditable()) {
                selectChannelAdapter.setEditable(false);
                return;
            } else {
                /*List<ColumnChannel> selectedChannels = selectChannelAdapter.getSelectedList();*/
               /* if (selectedChannels != null) {
                    LiveEvent.SubscriptedChannel event = new LiveEvent.SubscriptedChannel();
                    event.selectChannels = selectedChannels;
                    QEventBus.getEventBus().post(event);
                }*/
            }
        });
        return toolbar;
    }

    public void setSelectedListParm() {
        int rowNum = selectedList.size() % 3 == 0 ? (selectedList.size() / 3) : (selectedList.size() / 3) + 1;
        ViewGroup.LayoutParams params = selected_channel_recyclerView.getLayoutParams();
        params.height = AppUtil.dp2px(this, rowNum * (float) 65.5);
        selected_channel_recyclerView.setLayoutParams(params);
        // selected_channel_recyclerView.invalidate();
        //selected_channel_recyclerView.requestLayout();
    }

    public void setUnselectedListParm() {
        /*unselectedList = unselectChannelAdapter.getUnSelectedList();*/
        int rowNum = unselectedList.size() % 3 == 0 ? (unselectedList.size() / 3) : (unselectedList.size() / 3) + 1;
        ViewGroup.LayoutParams params = unselected_channel_recyclerView.getLayoutParams();
        params.height = AppUtil.dp2px(this, rowNum * (float) 65.5) + AppUtil.dp2px(this, 20);
        unselected_channel_recyclerView.setLayoutParams(params);
    }

    public void onEventMainThread(LiveEvent.SubscriptChannel event) {
        if (event == null) {
            return;
        }
        setSelectedListParm();
        int[] fromxy = new int[2];
        if (unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position) == null) {
            return;
        }
        unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position).getLocationInWindow(fromxy);
        int fromx = fromxy[0];
        int fromy = fromxy[1];
        int[] endxy = new int[2];

        if (selected_channel_recyclerView.getLayoutManager().getChildAt(selectedList.size() - 1) == null) {
            return;
        }
        selected_channel_recyclerView.getLayoutManager().getChildAt(selectedList.size() - 1).getLocationInWindow(endxy);

        if (unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position) == null) {
            return;
        }
        unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position).setDrawingCacheEnabled(true);

        ImageView imageView = new ImageView(this);
        Bitmap bm = Bitmap.createBitmap(unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position).getDrawingCache());
        if (bm == null || bm.isRecycled()) {
            // 如果没有图片缓存则不移动item
            return;
        }
        if (bm != null && !bm.isRecycled()) {
            imageView.setImageBitmap(bm);
        } else {
            imageView.setImageBitmap(null);
            return;
        }


        int endx = endxy[0];
        int endy = endxy[1];
        if (selectedList.size() % 3 == 1) {
            endx += selected_channel_recyclerView.getLayoutManager().getChildAt(selectedList.size() - 1).getMeasuredWidth();
        }
        if (selectedList.size() % 3 == 2) {
            endx += selected_channel_recyclerView.getLayoutManager().getChildAt(selectedList.size() - 1).getMeasuredWidth();
        }
        if (selectedList.size() % 3 == 0) {
           /* endy +=selected_channel_recyclerView.getChildAt(selectedList.size()-1).getMeasuredHeight();*/
            endy += AppUtil.dp2px(this, (float) 65.5);
            endx = 0;
        }
        TranslateAnimation animation = new TranslateAnimation(0, endx - fromx, 0, endy - fromy - AppUtil.dp2px(this, 25));
        /*TranslateAnimation animation=new TranslateAnimation(fromx, endx, fromy, endy);*/
        animation.setDuration(250);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position).getMeasuredWidth(), AppUtil.dip2px(this, 65));
        p.leftMargin = fromx;
        p.topMargin = fromy;

        //imageView.setImageDrawable(new BitmapDrawable(bm));
        mainview.addView(imageView, p);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                unselectChannelAdapter.isplayanimotion = false;
                unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position).setVisibility(View.GONE);
                if (selectedList.size() % 3 == 0) {
                    ViewGroup.LayoutParams params = selected_channel_recyclerView.getLayoutParams();
                    params.height += AppUtil.dp2px(getBaseContext(), (float) 65.5);
                    selected_channel_recyclerView.setLayoutParams(params);
                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
                unselected_channel_recyclerView.getLayoutManager().getChildAt(event.position).setVisibility(View.VISIBLE);
                unselectedList.remove(event.position);
                unselectChannelAdapter.notifyDataSetChanged();
                setUnselectedListParm();
                mainview.removeView(imageView);
                selectedList.add(event.columnChannel);
                /*setSelectedListParm();*/
                selectChannelAdapter.notifyDataSetChanged();
                unselectChannelAdapter.isplayanimotion = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //unselected_channel_recyclerView.getChildAt(event.position).startAnimation(animation);
        imageView.startAnimation(animation);
    }

    public void onEventMainThread(LiveEvent.UnSubscriptChannel event) {
        if (event == null) {
            return;
        }
        // unselectChannelAdapter.isDeling = true;
        selectChannelAdapter.notifyDataSetChanged();
        setSelectedListParm();

        unselectedList.add(0, event.columnChannel);
        unselectChannelAdapter.notifyDataSetChanged();
        setUnselectedListParm();
        // unselectChannelAdapter.isDeling = false;

    }

    public void onEvent(LiveEvent.ClickSelectChannel event) {
        this.finish();
    }

    public void onEventMainThread(LiveEvent.LongClickChannel event) {
        if (findViewById(com.jeremy.lychee.R.id.menu_editover).getVisibility() == View.GONE) {
            findViewById(com.jeremy.lychee.R.id.menu_edit_channel).setVisibility(View.GONE);
            findViewById(com.jeremy.lychee.R.id.menu_editover).setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        QEventBus.getEventBus().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void finish() {
        if (selectChannelAdapter == null) {
            super.finish();
            return;
        }
        if (selectChannelAdapter.isEditable()) {
            selectChannelAdapter.setEditable(false);
            if (findViewById(com.jeremy.lychee.R.id.menu_editover).getVisibility() == View.VISIBLE) {
                findViewById(com.jeremy.lychee.R.id.menu_editover).setVisibility(View.GONE);
                findViewById(com.jeremy.lychee.R.id.menu_edit_channel).setVisibility(View.VISIBLE);
            }
            return;
        } else {
            selectChannelAdapter.notifyDataSetChanged();
            List<ColumnChannel> selectedChannels = selectChannelAdapter.getSelectedList();
            if (selectedChannels != null) {
                /*LiveSubscriptedChannelPreference subscriptedChannelPreference = new LiveSubscriptedChannelPreference();
                String lastcids = subscriptedChannelPreference.getStringValue(LiveChannelFragment3.subscriptionkey);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < selectedChannels.size(); i++) {
                    stringBuilder.append(selectedChannels.get(i).getChannel_id());
                    if (i != selectedChannels.size() - 1) {
                        stringBuilder.append("|");
                    }
                }
                if (!lastcids.equals(stringBuilder.toString())){*/
                    LiveEvent.SubscriptedChannel event = new LiveEvent.SubscriptedChannel();
                    event.selectChannels = selectedChannels;
                    QEventBus.getEventBus().post(event);
                //}
            }
        }
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (findViewById(com.jeremy.lychee.R.id.menu_editover).getVisibility() == View.VISIBLE) {
                findViewById(com.jeremy.lychee.R.id.menu_editover).setVisibility(View.GONE);
                findViewById(com.jeremy.lychee.R.id.menu_edit_channel).setVisibility(View.VISIBLE);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
