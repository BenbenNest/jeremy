package com.jeremy.lychee.widget.RecyclerViewDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * An empty header (or footer) decoration for RecyclerView, since RecyclerView can't clipToPadding
 */
public class SimpleHeaderDecoration extends RecyclerView.ItemDecoration {
    private final int headerHeight;
    private final int footerHeight;

    public SimpleHeaderDecoration(int headerHeight, int footerHeight) {
        this.headerHeight = headerHeight;
        this.footerHeight = footerHeight;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                         RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == 0) {
            outRect.top = headerHeight;
        } else if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = footerHeight;
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }
}

