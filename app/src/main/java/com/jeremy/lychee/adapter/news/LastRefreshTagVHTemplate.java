package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LastRefreshTagVHTemplate extends ContentVHTemplate<Void> {

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new FocusNewsVH(view);
    }


    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, Void data) {
    }

    public static class FocusNewsVH extends RecyclerView.ViewHolder {
        @Bind(R.id.tag_txt)
        TextView tagTxt;

        public FocusNewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private int getLayoutId() {
        switch (itemType) {
            case 1:
                return R.layout.rv_content_last_refresh_tag2;
            default:
                return R.layout.rv_content_last_refresh_tag;
        }
    }
}
