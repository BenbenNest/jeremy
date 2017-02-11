package com.jeremy.lychee.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by zhaozuotong on 2015/11/20.
 */
public class ListLoadHolder extends RecyclerView.ViewHolder {

    //TextView title;
    //TextView time;
    //ImageView image;
    public RelativeLayout moreLayout,errorLayout,nodataLayout;

    public ListLoadHolder(View itemView) {
        super(itemView);
        moreLayout =  (RelativeLayout) itemView.findViewById(com.jeremy.lychee.R.id.load_more);
        errorLayout =  (RelativeLayout) itemView.findViewById(com.jeremy.lychee.R.id.load_error);
        nodataLayout =  (RelativeLayout) itemView.findViewById(com.jeremy.lychee.R.id.load_no_data);
        /*title = (TextView) itemView.findViewById(R.id.title);
        time = (TextView) itemView.findViewById(R.id.time);
        image = (ImageView) itemView.findViewById(R.id.image);
        item_view = itemView.findViewById(R.id.itemview);*/
    }

}