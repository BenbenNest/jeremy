package com.jeremy.lychee.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.db.NewsListData;

import java.security.SecureRandom;
import java.util.List;


public class SimpleAdapter extends UltimateViewAdapter<SimpleAdapter.SimpleAdapterViewHolder> {
    private List<NewsListData> mNewsBeans;

    public SimpleAdapter(List<NewsListData> newsBeans) {
        this.mNewsBeans = newsBeans;
    }


    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mNewsBeans.size() : position < mNewsBeans.size()) && (customHeaderView != null ? position > 0 : true)) {

      //      ((SimpleAdapterViewHolder) holder).title.setText(mNewsBeans.get(customHeaderView != null ? position - 1 : position).);
            // ((ViewHolder) holder).itemView.setActivated(selectedItems.get(position, false));
            if (mDragStartListener != null) {
//                ((ViewHolder) holder).imageViewSample.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                            mDragStartListener.onStartDrag(holder);
//                        }
//                        return false;
//                    }
//                });

                ((SimpleAdapterViewHolder) holder).item_view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        }

    }

    @Override
    public int getAdapterItemCount() {
        return mNewsBeans.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view, int viewType) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.jeremy.lychee.R.layout.rv_normal_news_level2, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }


    public void insert(NewsListData string, int position) {
        insert(mNewsBeans, string, position);
    }

    public void remove(int position) {
        remove(mNewsBeans, position);
    }

    public void clear() {
        clear(mNewsBeans);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mNewsBeans, from, to);
    }


    @Override
    public long generateHeaderId(int position) {
        // URLogs.d("position--" + position + "   " + getItem(position));
        /*if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;*/
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(com.jeremy.lychee.R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        TextView textView = (TextView) viewHolder.itemView.findViewById(com.jeremy.lychee.R.id.stick_text);
        //textView.setText(String.valueOf(getItem(position).charAt(0)));
//        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AA70DB93"));
        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(com.jeremy.lychee.R.id.stick_img);

        SecureRandom imgGen = new SecureRandom();
        switch (imgGen.nextInt(3)) {
            case 0:
                imageView.setImageResource(com.jeremy.lychee.R.mipmap.ic_launcher);
                break;
            case 1:
                imageView.setImageResource(com.jeremy.lychee.R.mipmap.ic_launcher);
                break;
            case 2:
                imageView.setImageResource(com.jeremy.lychee.R.mipmap.ic_launcher);
                break;
        }

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);
        // notifyItemRemoved(position);
//        notifyDataSetChanged();
        super.onItemDismiss(position);
    }
//
//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }


    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        TextView time;
        View item_view;

        public  SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
//            itemView.setOnTouchListener(new SwipeDismissTouchListener(itemView, null, new SwipeDismissTouchListener.DismissCallbacks() {
//                @Override
//                public boolean canDismiss(Object token) {
//                    Logs.d("can dismiss");
//                    return true;
//                }
//
//                @Override
//                public void onDismiss(View view, Object token) {
//                   // Logs.d("dismiss");
//                    remove(getPosition());
//
//                }
//            }));
            if (isItem) {
                title = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.title);
                time = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.time);
                image = (ImageView) itemView.findViewById(com.jeremy.lychee.R.id.image);
                item_view = itemView.findViewById(com.jeremy.lychee.R.id.itemview);
            }

        }
    }

    public NewsListData getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mNewsBeans.size())
            return mNewsBeans.get(position);
        else return null;
    }

}