package com.jeremy.lychee.adapter.user;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class WeMediaSubjectListAdapter extends BaseAdapter {
    Context mContext;
    private int selectItem;
    private int lastSelectItem;

    private static final float SCALE = 1.1f;
    private static final int DURATION = 150;

    private List<WeMediaTopic> mData = new ArrayList<>();
    public void setData(List<WeMediaTopic> data) {
        this.mData = data;
    }

    public WeMediaSubjectListAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Integer.MAX_VALUE;          //这里的目的是可以让图片循环浏览
//        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setSelectItem(int selectItem) {
        if (this.selectItem != selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(com.jeremy.lychee.R.layout.wemedia_subject_list_item, parent, false);
        }
        if (mData.size() > 0) {
            WeMediaTopic data = mData.get(position % mData.size());//取余，让图片循环浏览
            ((GlideImageView)convertView.findViewById(com.jeremy.lychee.R.id.subject_img)).loadImage(data.getImage());
            ((TextView)convertView.findViewById(com.jeremy.lychee.R.id.subject_name)).setText(data.getTitle());
        }

        int height = convertView.getLayoutParams().height;
        int width = convertView.getLayoutParams().width;
        if (selectItem == position) {
            int duration = selectItem == lastSelectItem ? 0 : DURATION;
            AnimatorSet set = new AnimatorSet();
            set.play(ObjectAnimator.ofFloat(convertView, View.SCALE_X, SCALE))
                    .with(ObjectAnimator.ofFloat(convertView, View.SCALE_Y, SCALE));
            set.setDuration(duration);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    lastSelectItem = selectItem;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        } else {
            convertView.setLayoutParams(new Gallery.LayoutParams(width, height));//未选中
        }

        return convertView;
    }

}