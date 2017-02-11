package com.jeremy.lychee.adapter.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.ItemTouchHelperViewHolder;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UnSelectChannelAdapter extends RecyclerView.Adapter<UnSelectChannelAdapter.ItemViewHolder>
    {
    private static final String TAG = "UnSelectChannelAdapter";
    private Context context;
    public static boolean isplayanimotion = true;
        public  boolean isDeling = false;
    private List<ItemViewHolder> list = new ArrayList<>();
        List<ColumnChannel> unselectedList;
    public UnSelectChannelAdapter(Context context, List<ColumnChannel> selectedList) {
        this.context = context;
        this.unselectedList = selectedList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_channel_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemViewHolder viewHolder = holder;
        ColumnChannel columnChannel = unselectedList.get(position);
        viewHolder.columnChannel = columnChannel;
        viewHolder.logo.loadImageOriginalSize(columnChannel.getChannel_icon(), (req, v) -> req
                .bitmapTransform(new GlideCircleTransform(context))
                .placeholder(context.getResources().getDrawable(R.drawable.livechannel_item_logo))
                .into(v));
        holder.channelname.setText(columnChannel.getChannel_name());
        holder.del_img.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> {
            //if (isDeling){
               // return;
            //}else {
                if (isplayanimotion){
                    LiveEvent.SubscriptChannel subscriptedChannel = new LiveEvent.SubscriptChannel();
                    subscriptedChannel.position = position;
                    subscriptedChannel.columnChannel = viewHolder.columnChannel;
                    QEventBus.getEventBus().post(subscriptedChannel);
                }else {
                    return;
                }
            //}
        });

       /* RxView.clickEvents(holder.itemView).debounce(300,TimeUnit.MILLISECONDS).subscribe(new Subscriber<ViewClickEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ViewClickEvent viewClickEvent) {
                LiveEvent.SubscriptChannel subscriptedChannel = new LiveEvent.SubscriptChannel();
            subscriptedChannel.position = position;
            subscriptedChannel.columnChannel = columnChannel;
            QEventBus.getEventBus().post(subscriptedChannel);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        if (unselectedList == null) {
            return 0;
        } else {
            return unselectedList.size();
        }
    }




    public List<ColumnChannel> getUnSelectedList(){
        return this.unselectedList;
    }

    public void setUnSelectedList(List<ColumnChannel> unselectedList){
        this.unselectedList = unselectedList;
    }
/*    @Override
    public void onViewAttachedToWindow(ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!list.contains(holder)) {
            list.add(holder);
            holder.del_img.setVisibility(isVisible(holder) ? View.VISIBLE : View.GONE);
        }

    }*/

    /*@Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        list.remove(holder);
        super.onViewDetachedFromWindow(holder);
    }*/
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @Bind(R.id.item_layout)
        RelativeLayout itemLayout;
        @Bind(R.id.channel_logo)
        GlideImageView logo;
        @Bind(R.id.del_img)
        ImageView del_img;
        @Bind(R.id.channelname)
        TextView channelname;

        ColumnChannel columnChannel;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
