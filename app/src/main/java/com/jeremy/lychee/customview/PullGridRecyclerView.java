package com.jeremy.lychee.customview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.qihoo.livecloud.tools.NetUtil;

public class PullGridRecyclerView extends PullListRecyclerView {
    public PullGridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && scrollEnable && !isLoading) {
                    GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && recylerViewAdapter != null) {

                        if (!NetUtil.isConnected(context)) {
                            recylerViewAdapter.setDataState(BaseRecyclerViewAdapter.DataState.NET_ERROR);
                            return;
                        }

                        if (recylerViewAdapter.isBackwardLoadEnable()) {
                            footLoadAction.onLoading();
                            recylerViewAdapter.load(true);

                            Logger.d("base adapter:", "滑到底部开始加载");

                        } else {
                            footLoadAction.onNormal(false);
                        }
                    }
                }

            }
        });
    }
}
