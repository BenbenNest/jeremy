//package com.qihoo.lianxian.fragment.user;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.RecyclerView;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import R;
//import WeMediaHotChannelsActivity;
//import com.qihoo.lianxian.adapter.user.UserDiscoveryRecyclerViewAdapter;
//import BaseFragment;
//import QEventBus;
//import Events;
//import SnackBarUtil;
//import PullListRecyclerView;
//import SlidingActivity;
//import TopToast;
//import TopToastUtil;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by leikang on 15-11-23.
// * 首页发现
// */
//public class UserDiscoveryFragment extends BaseFragment {
//
//    static private String TAG = UserDiscoveryFragment.class.toString();
//    @Bind(R.id.user_discovery_recycler_view)
//    PullListRecyclerView userDiscoveryRecyclerView;
//
//    private ViewGroup rootView;
//    private UserDiscoveryRecyclerViewAdapter mDiscoveryRecyclerViewAdapter;
//    private boolean hasMoreHotChannel = false;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        if (null == rootView) {
//            rootView = (ViewGroup)inflater.inflate(
//                    R.layout.fragment_user_discovery_view, container, false);
//            ButterKnife.bind(this, rootView);
//            initUI();
//        }
//
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    private void initUI() {
//        if (mDiscoveryRecyclerViewAdapter == null) {
//            Context context = getContext();
//            mDiscoveryRecyclerViewAdapter = new UserDiscoveryRecyclerViewAdapter(getContext(), this);
//            mDiscoveryRecyclerViewAdapter.setHasFooterView(true);
//            userDiscoveryRecyclerView.setAdapter(mDiscoveryRecyclerViewAdapter, false);
//        }
//    }
//
//    static public class HotChannelRecyclerView extends RecyclerView {
//        static boolean s_isDragEnabled = false;
//        static boolean s_isDragging = false;
//
//        public HotChannelRecyclerView(Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//        }
//
////        @Override
////        public boolean onTouchEvent(MotionEvent e) {
////            if (!custom) return false;
////            switch (e.getAction()) {
////                case MotionEvent.ACTION_DOWN:
////                case MotionEvent.ACTION_MOVE:
////                    QEventBus.getEventBus().post(new Events.OnDragEnabled(true));
////                    break;
////                case MotionEvent.ACTION_CANCEL:
////                case MotionEvent.ACTION_UP:
////                    QEventBus.getEventBus().post(new Events.OnDragEnabled(false));
////                    break;
////            }
////            return super.onTouchEvent(e);
////        }
////
////        private float xDistance, yDistance, lastX, lastY;
////        boolean custom = true;
////
////        @Override
////        public boolean onInterceptTouchEvent(MotionEvent ev) {
////            switch (ev.getAction()) {
////                case MotionEvent.ACTION_DOWN:
////                    xDistance = yDistance = 0f;
////                    lastX = ev.getX();
////                    lastY = ev.getY();
////                    return false;
////                case MotionEvent.ACTION_MOVE:
////                    final float curX = ev.getX();
////                    final float curY = ev.getY();
////                    xDistance += Math.abs(curX - lastX);
////                    yDistance += Math.abs(curY - lastY);
////                    lastX = curX;
////                    lastY = curY;
////                    if (yDistance > xDistance) {
////                        custom = false;
////                    } else {
////                        custom = true;
////                    }
////                    return true;
////            }
////
////            return super.onInterceptTouchEvent(ev);
////        }
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        new Handler().post(() -> {
//            if (getUserVisibleHint() && !mIgnore.get()) {
////                updateList();
//            }
//        });
//    }
//
//    @Override
//    protected void lazyLoad() {
//        super.lazyLoad();
//        updateList();
//    }
//
//    private void updateList() {
//        if (null != rootView) {
//            if (userDiscoveryRecyclerView == null) {
//                //java.lang.NullPointerException: Attempt to read from field
//                //'boolean PullListRecyclerView.isLoading' on a null object reference
//                userDiscoveryRecyclerView =
//                        (PullListRecyclerView) rootView.findViewById(R.id.user_discovery_recycler_view);
//            }
//            if (userDiscoveryRecyclerView != null
//                    && !userDiscoveryRecyclerView.isLoading) {
//                mDiscoveryRecyclerViewAdapter.load(false);
//            }
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
//
//    /**
//     * TODO(wangp)
//     */
//    private Events.IgnoreRefreshNextTime.IsIgnore mIgnore =
//            new Events.IgnoreRefreshNextTime.IsIgnore(true);//初始化true:避免onResume执行两次造成的多余load
//    final public void onEventMainThread(Events.IgnoreRefreshNextTime event) {
//        mIgnore = event.mData;
//    }
//
//    final public void onEventMainThread(Events.OnDragEnabled event) {
//        if (!hasMoreHotChannel) {
//            HotChannelRecyclerView.s_isDragEnabled = false;
//            return;
//        }
//        if (event.enabled) {
//            HotChannelRecyclerView.s_isDragEnabled = true;
////            viewPager.getAdapter().notifyDataSetChanged();
//        } else {
//            rootView.postDelayed(() -> {
//                if (!HotChannelRecyclerView.s_isDragging) {
//                    HotChannelRecyclerView.s_isDragEnabled = false;
////                    viewPager.getAdapter().notifyDataSetChanged();
//                }
//            }, 150);
//            //添加延迟，以保证RecyclerView的事件抛给父ViewPager，
//            //否則MotionEvent.ACTION_CANCEL触发s_isDragEnabled设置，curCount立即返回1
//            //造成第二屏ViewPager无法正常显示
//        }
//    }
//
//    final public void onEventMainThread(Events.HasMoreHotChannels event) {
//        hasMoreHotChannel = event.val;
//    }
//
//    final public void onEventMainThread(Events.ShowHotChannels event) {
//        HotChannelRecyclerView.s_isDragEnabled = true;
//        ((SlidingActivity)getActivity()).openActivity(WeMediaHotChannelsActivity.class);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        QEventBus.getEventBus().register(this);
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden && !mIgnore.get()) {
//            //TODO(wangp)
////            updateList();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        QEventBus.getEventBus().unregister(this);
//    }
//
//    public void showSnackBar() {
////        SnackBarUtil.showSnackBar(rootView, () -> mDiscoveryRecyclerViewAdapter.load(true));
//        TopToastUtil.showTopToast(rootView, getResources().getString(R.string.top_toast_net_error));
//    }
//}
