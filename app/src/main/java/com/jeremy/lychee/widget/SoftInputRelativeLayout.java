package com.jeremy.lychee.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SoftInputRelativeLayout extends RelativeLayout {
	private OnSoftInputWindowChangeListener mOnSoftInputWindowChangeListener;
	/**
	 * view最大高度
	 */
	private int vH;

	public SoftInputRelativeLayout(Context context) {
		super(context);
	}
	public SoftInputRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SoftInputRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(h>vH){
			vH = h;
		}
		if(oldh>0&&h==vH){
			mOnSoftInputWindowChangeListener.onChange(true);
		}
	}
	
	public void setOnSoftInputWindowChangeListener(OnSoftInputWindowChangeListener mOnSoftInputWindowChangeListener) {
		this.mOnSoftInputWindowChangeListener = mOnSoftInputWindowChangeListener;
	}

	public interface OnSoftInputWindowChangeListener{
		public void onChange(boolean isClosed);
	}
}
