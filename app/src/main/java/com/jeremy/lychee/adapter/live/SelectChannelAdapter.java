package com.jeremy.lychee.adapter.live;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.lychee.adapter.ItemTouchHelperAdapter;
import com.jeremy.lychee.adapter.ItemTouchHelperViewHolder;
import com.jeremy.lychee.adapter.OnStartDragListener;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectChannelAdapter extends RecyclerView.Adapter<SelectChannelAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = "SelectChannelAdapter";
    private Context context;
    private List<ColumnChannel> selectedList;
    private OnStartDragListener onStartDragListener;
    private boolean editable = false;
    private List<String> forbidDelCids; //静止删除的
    private List<String> forbidMoveCids; //静止移动的
    private List<ItemViewHolder> list = new ArrayList<>();

    private int newIndx = 0;
    private int oldIndex = -1;

    public SelectChannelAdapter(Context context, List<ColumnChannel> selectedList, OnStartDragListener onStartDragListener) {
        this.context = context;
        this.selectedList = selectedList;
        this.onStartDragListener = onStartDragListener;

    }

    public void setForbidDelCids(List<String> forbidDelCids) {
        this.forbidDelCids = forbidDelCids;
    }

    public void setForbidMoveCids(List<String> forbidMoveCids) {
        this.forbidMoveCids = forbidMoveCids;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.edit_channel_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemViewHolder viewHolder = holder;
        ColumnChannel columnChannel = selectedList.get(position);
        viewHolder.columnChannel = columnChannel;
        viewHolder.logo.loadImageOriginalSize(columnChannel.getChannel_icon(), (req, v) -> req
                .bitmapTransform(new GlideCircleTransform(context))
                .placeholder(context.getResources().getDrawable(com.jeremy.lychee.R.drawable.livechannel_item_logo))
                .into(v));
        holder.del_img.setOnClickListener(view -> {
            Logger.d("holder.getAdapterPosition:" + holder.getAdapterPosition());
            if (holder.columnChannel == null) {
                return;
            }
            int indexOf = selectedList.indexOf(holder.columnChannel);
            Logger.d("holder.index:" + indexOf);
            if (indexOf == -1) {
                return;
            }
            onItemDismiss(indexOf);
        });
        holder.itemLayout.setOnLongClickListener(view -> {
            setEditable(true);
            QEventBus.getEventBus().post(new LiveEvent.LongClickChannel());
            return false;
        });
        holder.del_img.setVisibility(isVisible(holder) ? View.VISIBLE : View.GONE);
        holder.itemLayout.setOnTouchListener((v, event) -> {
            if (editable && MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                onStartDragListener.onStartDrag(holder);
            }
            return false;
        });
        holder.itemLayout.setOnClickListener(v -> {
            if (!editable) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).columnChannel != null) {
                        if (list.get(i).columnChannel.isClicked()) {
                            int index = selectedList.indexOf(list.get(i).columnChannel);
                            selectedList.get(index).setIsClicked(false);
                            notifyItemChanged(index);
                            int newindex = selectedList.indexOf(holder.columnChannel);
                            selectedList.get(newindex).setIsClicked(true);
                            holder.columnChannel.setIsClicked(true);
                            notifyItemChanged(newindex);
                            QEventBus.getEventBus().post(new LiveEvent.ClickSelectChannel());
                            break;
                        }
                    }
                }

            }

        });
        holder.channelname.setText(columnChannel.getChannel_name());
        if (columnChannel.isClicked()) {
            holder.channelname.setTextColor(context.getResources().getColor(com.jeremy.lychee.R.color.livechannel_name_color));
        } else {
            holder.channelname.setTextColor(context.getResources().getColor(com.jeremy.lychee.R.color.channels_item_text_color));
        }
    }

    @Override
    public int getItemCount() {
        if (selectedList == null) {
            return 0;
        } else {
            return selectedList.size();
        }
    }

    public boolean isEditable() {
        return editable;
    }

    private boolean isVisible(ItemViewHolder viewHolder) {
        boolean flag = true;
        if (editable) {
            for (String cid : forbidDelCids) {
                if (viewHolder.columnChannel != null &&
                        viewHolder.columnChannel.getChannel_id().equals(cid)) {
                    flag = false;
                    break;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        for (ItemViewHolder viewHolder : list) {
            viewHolder.del_img.setVisibility(isVisible(viewHolder) ? View.VISIBLE : View.GONE);
        }
    }

    public List<ColumnChannel> getSelectedList() {
        return this.selectedList;
    }

    public void setSelectedList(List<ColumnChannel> selectedList) {
        this.selectedList = selectedList;
    }

    @Override
    public void onViewAttachedToWindow(ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!list.contains(holder)) {
            list.add(holder);
            holder.del_img.setVisibility(isVisible(holder) ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        list.remove(holder);
        super.onViewDetachedFromWindow(holder);
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //热点频道不能移动
        if (fromPosition <= forbidMoveCids.size() - 1 || toPosition <= forbidMoveCids.size() - 1) {
            return;
        }
        if (fromPosition >= getItemCount() || toPosition >= getItemCount() || fromPosition < 0 || toPosition < 0) {
            return;
        }
        Logger.t(TAG).d("onItemMove ,fromPostion:%d,toPostion %d", fromPosition, toPosition);
        ColumnChannel columnChannel = selectedList.remove(fromPosition);
        selectedList.add(toPosition, columnChannel);
        notifyItemMoved(fromPosition, toPosition);
        QEventBus.getEventBus().post(new LiveEvent.MoveChannel());
       /* notifyDataSetChanged();*/
    }

    @Override
    public void onItemDismiss(int position) {
        if (UnSelectChannelAdapter.isplayanimotion) {
            LiveEvent.UnSubscriptChannel unSubscriptChannel = new LiveEvent.UnSubscriptChannel();
            Logger.t(TAG).d("onItemDismiss, position:%d", position);
            unSubscriptChannel.position = position;
            unSubscriptChannel.columnChannel = selectedList.get(position);
            selectedList.remove(position);
            notifyItemRemoved(position);
            QEventBus.getEventBus().post(unSubscriptChannel);
        } else {
            return;
        }
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @Bind(com.jeremy.lychee.R.id.item_layout)
        RelativeLayout itemLayout;
        @Bind(com.jeremy.lychee.R.id.channel_logo)
        GlideImageView logo;
        @Bind(com.jeremy.lychee.R.id.del_img)
        ImageView del_img;
        @Bind(com.jeremy.lychee.R.id.channelname)
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
