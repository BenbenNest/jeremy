package com.jeremy.lychee.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremy.lychee.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LunboFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private OnLunboFragmentClickListener mListener;

    @Bind(com.jeremy.lychee.R.id.viewpager)
    ViewPager viewpager;

    @Bind(com.jeremy.lychee.R.id.lunbo_image_icon)
    ImageView lunbo_image_icon;

    public LunboFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLunboFragmentClickListener) {
            mListener = (OnLunboFragmentClickListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.fragment_lunbo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        viewpager.setAdapter(new LunboPagerAdapter());
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int icon_res;
        switch (position){
            case 0:
                icon_res = com.jeremy.lychee.R.mipmap.lunbo0_0;
                break;
            case 1:
                icon_res = com.jeremy.lychee.R.mipmap.lunbo1_0;
                break;
            case 2:
            default:
                icon_res = com.jeremy.lychee.R.drawable.selector_lunbo_go_feel;
                break;
        }
        lunbo_image_icon.setImageResource(icon_res);
        if(position==2){
            lunbo_image_icon.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onLunboFragmentClick();
                }
            });
        } else {
            lunbo_image_icon.setOnClickListener(null);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class LunboPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            int bg_res;
            switch (position){
                case 0:
                    bg_res = com.jeremy.lychee.R.mipmap.lunbo0;
                    break;
                case 1:
                    bg_res = com.jeremy.lychee.R.mipmap.lunbo1;
                    break;
                case 2:
                default:
                    bg_res = com.jeremy.lychee.R.mipmap.lunbo2;
                    break;
            }
            View view = View.inflate(getActivity(), com.jeremy.lychee.R.layout.row_lunbo_item, null);
            view.setBackgroundResource(bg_res);

            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public interface OnLunboFragmentClickListener {
        void onLunboFragmentClick();
    }
}
