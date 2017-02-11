package com.jeremy.lychee.widget.RecyclerViewDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeremy.lychee.preference.AppConstant;

/**
 * Created by zhaozuotong on 2015/11/18.
 */
public class LiveDiscoveryColumnSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;
    private @AppConstant.LayoutManager
    int mMode;
    private Boolean hasHeader;

    public LiveDiscoveryColumnSpacesItemDecoration(int itemOffset, @AppConstant.LayoutManager int mode, Boolean hasHeader) {
        mItemOffset = itemOffset/2;
        mMode = mode;
        this.hasHeader = hasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildLayoutPosition(view) == 0) {
            if(hasHeader) {
                outRect.set(0, 0, 0, 0);
                return;
            }
        }
        int start = hasHeader ? 1 : 0 ;
        if(mMode == AppConstant.LayoutManager.LINEAR_LAOUT){
            if (parent.getChildLayoutPosition(view) == start) {
                outRect.set(2 * mItemOffset, 2 * mItemOffset, 2 * mItemOffset, mItemOffset);
            } else {
                outRect.set(2 * mItemOffset, mItemOffset, 2 * mItemOffset, mItemOffset);
            }
        }else if(mMode == AppConstant.LayoutManager.GRID_LAYOUT){
            if (parent.getChildLayoutPosition(view) % 2 == start) {
                if (parent.getChildLayoutPosition(view) == start) {//1
                    outRect.set(mItemOffset*2, 0, mItemOffset, 0);
                } else {//3
                    outRect.set(mItemOffset*2, 0, mItemOffset, 0);
                }
            } else {//2
                if (parent.getChildLayoutPosition(view) == start+1) {
                    outRect.set(mItemOffset, 0, mItemOffset*2, 0);
                } else {//4
                    outRect.set(mItemOffset, 0, mItemOffset*2, 0);
                }
            }
        }
    }
}
