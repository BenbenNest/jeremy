package com.jeremy.lychee.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeremy.lychee.widget.LiveHotUltimateRecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * An abstract adapter which can be extended for Recyclerview
 */
public abstract class LiveHotUltimateViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, ItemTouchHelperAdapter{


    protected View customLoadMoreView = null;

    /**
     * Set the header view of the adapter.
     */
    public void setCustomHeaderView(LiveHotUltimateRecyclerView.CustomRelativeWrapper customHeaderView) {
        this.customHeaderView = customHeaderView;
    }

    public LiveHotUltimateRecyclerView.CustomRelativeWrapper getCustomHeaderView() {
        return customHeaderView;
    }

    protected LiveHotUltimateRecyclerView.CustomRelativeWrapper customHeaderView = null;



//    public abstract VH getViewHolder(View view, int viewType);
//
//
//    public abstract VH onCreateViewHolder(ViewGroup parent);

    /**
     * Using a custom LoadMoreView
     *
     * @param customview the inflated view
     */
    public void setCustomLoadMoreView(View customview) {
        customLoadMoreView = customview;
    }

    /**
     * Changing the loadmore view
     *
     * @param customview the inflated view
     */
    public void swipeCustomLoadMoreView(View customview) {
        customLoadMoreView = customview;
        isLoadMoreChanged = true;
    }

    public View getCustomLoadMoreView() {
        return customLoadMoreView;
    }

    public boolean isLoadMoreChanged = false;



    protected boolean hasHeaderView() {
        return customHeaderView != null;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int headerOrFooter = 0;
        if (customHeaderView != null) headerOrFooter++;
        if (customLoadMoreView != null) headerOrFooter++;
        return getAdapterItemCount() + headerOrFooter;
    }

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     *
     * @return The number of items in the bound adapter
     */
    public abstract int getAdapterItemCount();


    public void toggleSelection(int pos) {
        notifyItemChanged(pos);
    }


    public void clearSelection(int pos) {
        notifyItemChanged(pos);
    }

    public void setSelected(int pos) {
        notifyItemChanged(pos);
    }

    /**
     * Swap the item of list
     *
     * @param list data list
     * @param from position from
     * @param to   position to
     */
    public void swapPositions(List<?> list, int from, int to) {
        if (customHeaderView != null) {
            from--;
            to--;
        }
        Collections.swap(list, from, to);
    }


    /**
     * Insert a item to the list of the adapter
     *
     * @param list     data list
     * @param object   object T
     * @param position position
     * @param <T>      in T
     */
    public <T> void insert(List<T> list, T object, int position) {
        list.add(position, object);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);
    }

    /**
     * Remove a item of  the list of the adapter
     *
     * @param list     data list
     * @param position position
     */
    public void remove(List<?> list, int position) {
        if (list.size() > 0) {
            list.remove(customHeaderView != null ? position - 1 : position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Clear the list of the adapter
     *
     * @param list data list
     */
    public void clear(List<?> list) {
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public long getHeaderId(int position) {
        if (customHeaderView != null && position == 0) return -1;
        if (customLoadMoreView != null && position >= getItemCount() - 1) return -1;
        if (getAdapterItemCount() > 0)
            return generateHeaderId(position);
        else return -1;
    }

    public abstract long generateHeaderId(int position);




    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
        public static final int FOCUS = 4;
    }




    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        notifyDataSetChanged();
    }


    protected OnStartDragListener mDragStartListener = null;

    /**
     * Listener for manual initiation of a drag.
     */
    public interface OnStartDragListener {

        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

}
