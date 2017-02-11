package com.jeremy.lychee.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.widget.LoadingRecyclerViewFooter;

public abstract class BaseRVAdapter extends HeaderFooterRecyclerViewAdapter {

    protected LoadingRecyclerViewFooter footerView = null;
    protected RecyclerViewWrapper.CustomRelativeWrapper customHeaderView = null;

    @Override
    protected final int getHeaderItemCount() {
        return customHeaderView == null ? 0 : 1;
    }

    @Override
    protected final int getFooterItemCount() {
        return footerView == null ? 0 : 1;
    }

    @Override
    protected final RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        if (customHeaderView != null) {
            return new HeaderViewVH(customHeaderView);
        } else {
            return null;
        }
    }

    @Override
    protected final RecyclerView.ViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        if (footerView == null) {
            return null;
        } else {
            return new FooterViewVH(footerView);
        }
    }

    public boolean canTopBound(){
        return false;
    }
    public int getTopBounceRange(){
        return 0;
    }

    @Override
    protected final void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {

    }

    @Override
    protected final void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {

    }

    public LoadingRecyclerViewFooter getFooterView() {
        return footerView;
    }

    public void setFooterView(LoadingRecyclerViewFooter footerView) {
        this.footerView = footerView;
    }

    public RecyclerViewWrapper.CustomRelativeWrapper getCustomHeaderView() {
        return customHeaderView;
    }

    public void setCustomHeaderView(RecyclerViewWrapper.CustomRelativeWrapper customHeaderView) {
        this.customHeaderView = customHeaderView;
    }

    public void setFooterStatus(LoadingRecyclerViewFooter.FooterStatus status) {
        if (footerView != null) {
            if (status == LoadingRecyclerViewFooter.FooterStatus.loading && getItemCount() == 0) {
                status = LoadingRecyclerViewFooter.FooterStatus.idle;
            }
            footerView.setStatus(status);
        }
    }

    public void setFooterClickListener(LoadingRecyclerViewFooter.FooterClickListener listener) {
        if (footerView != null) {
            footerView.setFooterListener(listener);
        }
    }

    public static class HeaderViewVH extends RecyclerView.ViewHolder {

        public HeaderViewVH(View itemView) {
            super(itemView);
        }
    }

    public static class FooterViewVH extends RecyclerView.ViewHolder {

        public FooterViewVH(View itemView) {
            super(itemView);
        }
    }
}
