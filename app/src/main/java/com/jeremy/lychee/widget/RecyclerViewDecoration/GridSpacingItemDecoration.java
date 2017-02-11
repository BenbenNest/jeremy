package com.jeremy.lychee.widget.RecyclerViewDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int horizontalSpacing;
    private int verticalSpacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount,int verticalSpacing, int horizontalSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount; // horizontalSpacing - column * ((1f / spanCount) * horizontalSpacing)
            outRect.right = (column + 1) * horizontalSpacing / spanCount; // (column + 1) * ((1f / spanCount) * horizontalSpacing)

            if (position < spanCount) { // top edge
                outRect.top = verticalSpacing;
            }
            outRect.bottom = verticalSpacing; // item bottom
        } else {
            outRect.left = column * horizontalSpacing / spanCount; // column * ((1f / spanCount) * horizontalSpacing)
            outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / spanCount; // horizontalSpacing - (column + 1) * ((1f /    spanCount) * horizontalSpacing)
            if (position >= spanCount) {
                outRect.top = verticalSpacing; // item top
            }
        }
    }
}