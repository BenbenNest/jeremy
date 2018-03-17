package com.jeremy.library.recycler_view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by changqing on 2018/3/17.
 */

public class RecyclerViewUtils {

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position 要把第position条Item显示到第一个
     */
    private static void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        LinearLayoutManager mLinearLayoutManager=(LinearLayoutManager)mRecyclerView.getLayoutManager();
        position = position + mLinearLayoutManager.findLastVisibleItemPosition() - mLinearLayoutManager.findFirstVisibleItemPosition() - 1;
        if (position > mLinearLayoutManager.getItemCount()) {
            position = mLinearLayoutManager.getItemCount();
        }
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
        }
    }


}
