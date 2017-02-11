package com.jeremy.lychee.customview.user;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.db.WeMediaChannel;

import java.util.List;

/**
 * Publish Channel tags layout
 */
public class PublishChannelsLayout extends FrameLayout {
    private int hgap = getResources().getDimensionPixelSize(R.dimen.margin_medium); // horizontal gap
    private int vgap = getResources().getDimensionPixelSize(R.dimen.margin_medium); // vertical gap
    private WeMediaChannel checked_channel;

    public PublishChannelsLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public PublishChannelsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PublishChannelsLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PublishChannelsLayout, defStyle, 0);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        hgap = (int) a.getDimension( R.styleable.PublishChannelsLayout_hgap, hgap);
        vgap = (int) a.getDimension( R.styleable.PublishChannelsLayout_hgap, vgap);
        a.recycle();
    }

    /**
     * 更新数据，选中状态不变
     */
    public void notifyChannelsChange(List<WeMediaChannel> channels){
        removeAllViews();
        if(channels==null) {
            requestLayout();
            return;
        }

        for(WeMediaChannel channel:channels){
            PublishTagViewHolder holder = onCreateView();
            onBindView(holder, channel);

            holder.channel_tag.setOnClickListener(v -> {
                v.setSelected(true);
                checked_channel = channel;
                int count = getChildCount();
                for(int i=0; i<count; i++){
                    View child = getChildAt(i);
                    if(child!=v&&child.isSelected()){
                        child.setSelected(false);
                    }
                }
            });
            addView(holder.channel_tag, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        requestLayout();
    }

    public void setChannels(List<WeMediaChannel> channels){
        checked_channel = null;
        removeAllViews();
        if(channels==null) {
            requestLayout();
            return;
        }

        for(WeMediaChannel channel:channels){
            if(checked_channel==null){
                checked_channel = channel;
            }
            PublishTagViewHolder holder = onCreateView();
            onBindView(holder, channel);
            holder.channel_tag.setOnClickListener(v -> {
                v.setSelected(true);
                checked_channel = channel;
                int count = getChildCount();
                for(int i=0; i<count; i++){
                    View child = getChildAt(i);
                    if(child!=v&&child.isSelected()){
                        child.setSelected(false);
                    }
                }
            });
            addView(holder.channel_tag, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int vW = MeasureSpec.getSize(widthMeasureSpec);

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int maxHeight = 0;
        int count = getChildCount();
        int childState = 0;

        int cLeft = mPaddingLeft;
        int cTop = mPaddingTop;
        int rowMaxHeight = 0;

        for(int i=0; i<count; i++){
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cW = child.getMeasuredWidth();
                int cH = child.getMeasuredHeight();

                int l = cLeft + lp.leftMargin;
                int r = l + cW;
                cLeft = r + lp.rightMargin + hgap;

                if(cLeft>vW - mPaddingRight){
                    cLeft = mPaddingLeft;
                    cTop = cTop + rowMaxHeight + vgap;

                    l = cLeft + lp.leftMargin;
                    r = l + cW;

                    cLeft = r + lp.rightMargin + hgap;
                } else {
                    rowMaxHeight = Math.max(cH + lp.topMargin + lp.bottomMargin, rowMaxHeight);
                }

                int t = cTop + lp.topMargin;
                int b = t + cH;
                maxHeight = Math.max(b, maxHeight);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        maxHeight += mPaddingBottom;
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());

        // Check against our foreground's minimum height and width
        final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
        }

        setMeasuredDimension(widthMeasureSpec, resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren();
    }

    private void layoutChildren(){
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int w = getMeasuredWidth();

        int mChildrenCount = getChildCount();
        int cLeft = mPaddingLeft;
        int cTop = mPaddingTop;
        int rowMaxHeight = 0;
        for(int i=0; i<mChildrenCount; i++){
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cW = child.getMeasuredWidth();
                int cH = child.getMeasuredHeight();

                int l = cLeft + lp.leftMargin;
                int r = l + cW;
                cLeft = r + lp.rightMargin + hgap;

                if(cLeft>w-mPaddingRight){
                    cLeft = mPaddingLeft;
                    cTop = cTop + rowMaxHeight + vgap;

                    l = cLeft + lp.leftMargin;
                    r = l + cW;

                    cLeft = r + lp.rightMargin + hgap;
                } else {
                    rowMaxHeight = Math.max(cH + lp.topMargin + lp.bottomMargin, rowMaxHeight);
                }

                int t = cTop + lp.topMargin;
                int b = t + cH;

                child.layout(l, t, r, b);
            }
        }
    }

    private PublishTagViewHolder onCreateView(){
        View publish_channel_item = View.inflate(getContext(), R.layout.publish_channel_item, null);
        return new PublishTagViewHolder(publish_channel_item);
    }

    private void onBindView(@NonNull PublishTagViewHolder holder, @NonNull  WeMediaChannel channel){
        holder.channel_tag.setText(channel.getName());

        if(checked_channel!=null && !TextUtils.isEmpty(checked_channel.getId()) &&checked_channel.getId().equals(channel.getId())){
            holder.channel_tag.setSelected(true);
        }
    }

    /**
     * @return get checked MediaChannels
     */
    public WeMediaChannel getCheckedChannel() {
        return checked_channel;
    }

    static class PublishTagViewHolder {
        TextView channel_tag;

        public PublishTagViewHolder(@NonNull View itemView) {
            this.channel_tag = (TextView) itemView;
        }
    }

}
