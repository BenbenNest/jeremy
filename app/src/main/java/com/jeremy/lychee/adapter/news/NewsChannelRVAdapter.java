package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.adapter.ItemTouchHelperAdapter;
import com.jeremy.lychee.adapter.ItemTouchHelperViewHolder;
import com.jeremy.lychee.adapter.OnStartDragListener;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsChannelRVAdapter extends RecyclerView.Adapter<NewsChannelRVAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = "NewsChannelRVAdapter";
    private Context context;
    private List<NewsChannel> newsChannelList;
    private OnStartDragListener onStartDragListener;
    private boolean editable = false;
    private List<ItemViewHolder> viewHolderList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnItemDismissListener onItemDismissListener;
    private String currentCid ="";

    public NewsChannelRVAdapter(Context context, List<NewsChannel> newsChannelList,
                                OnStartDragListener onStartDragListener, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.newsChannelList = newsChannelList;
        this.onItemClickListener = onItemClickListener;
        if (onStartDragListener != null) {
            this.onStartDragListener = onStartDragListener;
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.ordered_channel_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        NewsChannel newsChannel = newsChannelList.get(position);
        holder.channelTitle.setText(newsChannel.getCname());
        if (newsChannel.getCid().equals(currentCid)) {
            holder.channelTitle.setTextColor(0xffff3644);
        }else {
            holder.channelTitle.setTextColor(0xff333333);
        }
        holder.delImg.setOnClickListener(view -> {
            Logger.d("holder.getAdapterPosition:" + holder.getAdapterPosition());
            int indexOf = holder.getAdapterPosition();
            Logger.d("holder.index:" + indexOf);
            if (indexOf == -1) {
                return;
            }
            onItemDismiss(indexOf);
        });
        holder.delImg.setVisibility(isVisible(holder) ? View.VISIBLE : View.GONE);

        if (onStartDragListener != null) {
            holder.itemView.setOnTouchListener((v, event) -> {
                switch (MotionEventCompat.getActionMasked(event)) {
                    case MotionEvent.ACTION_DOWN:
                        if (editable) {
                            onStartDragListener.onStartDrag(holder);
                        }
                        break;
                }
                return false;
            });
            holder.itemView.setOnLongClickListener(view -> {
                setEditable(true);
                QEventBus.getEventBus().post(new Events.ChangeNewsStatusEvent(true));
                return true;
            });
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int indexOf = holder.getAdapterPosition();
                if (indexOf != -1) {
                    onItemClickListener.onItemClick(v, indexOf);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (newsChannelList == null) {
            return 0;
        } else {
            return newsChannelList.size();
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        for (ItemViewHolder viewHolder : viewHolderList) {
            viewHolder.delImg.setVisibility(isVisible(viewHolder) ? View.VISIBLE : View.GONE);
        }
    }

    public void setCurrentCid(String currentCid) {
        this.currentCid = currentCid;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDismissListener(OnItemDismissListener onItemDismissListener) {
        this.onItemDismissListener = onItemDismissListener;
    }

    private boolean isVisible(ItemViewHolder viewHolder) {
        boolean flag = true;
        if (editable) {
            if (viewHolder.getAdapterPosition() == 0) {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    @Override
    public void onViewAttachedToWindow(ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!viewHolderList.contains(holder)) {
            viewHolderList.add(holder);
            holder.delImg.setVisibility(isVisible(holder) ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        viewHolderList.remove(holder);
        super.onViewDetachedFromWindow(holder);
    }

    public List<NewsChannel> getNewsChannelList() {
        return newsChannelList;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //热点频道不能移动
        if (fromPosition == 0 || toPosition == 0) {
            return;
        }
        if (fromPosition >= getItemCount() || toPosition >= getItemCount() || fromPosition < 0 || toPosition < 0) {
            return;
        }
        Logger.t(TAG).d("onItemMove ,fromPostion:%d,toPostion %d", fromPosition, toPosition);
        NewsChannel channel = newsChannelList.remove(fromPosition);
        newsChannelList.add(toPosition, channel);
        notifyItemMoved(fromPosition, toPosition);
    }

    public NewsChannel remove(int position) {
        if (newsChannelList == null || newsChannelList.size() <= position) {
            return null;
        }
        NewsChannel newsChannel = newsChannelList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        return newsChannel;
    }


    public void add(NewsChannel newsChannel) {
        int position = 0;
        if (newsChannelList != null) {
            position = newsChannelList.size();
        }
        add(position, newsChannel);
    }

    public void add(int position, NewsChannel newsChannel) {
        if (newsChannelList == null || newsChannelList.size() < position) {
            return;
        }
        newsChannelList.add(position, newsChannel);
        notifyItemInserted(position);
    }


    @Override
    public void onItemDismiss(int position) {
        NewsChannel newsChannel = remove(position);
        if (onItemDismissListener != null) {
            onItemDismissListener.onItemDismiss(position, newsChannel);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {
        @Bind(com.jeremy.lychee.R.id.channel_title)
        TextView channelTitle;
        @Bind(com.jeremy.lychee.R.id.del_img)
        ImageView delImg;

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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemDismissListener {
        void onItemDismiss(int position, NewsChannel newsChannel);
    }
}
