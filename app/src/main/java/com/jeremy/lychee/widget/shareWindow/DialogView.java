package com.jeremy.lychee.widget.shareWindow;

import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.jeremy.lychee.R;


/**
 * 
 * @author daibangwen-xy
 *
 */
public class DialogView {

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}

	protected Context mcontext;
	protected View view;
	private android.app.Dialog dialog;
	private OnDismissListener mOnDialogDismissListener;

	protected DialogView(Context context) {
	}

	protected DialogView(Context context, int layoutId) {
		this.mcontext = context;
		this.view = loadLayout(layoutId);
		initDialog();
	}

	private View loadLayout(int layoutId) {
		View v = LayoutInflater.from(mcontext).inflate(layoutId, null);
		onLoadLayout(v);
		return v;
	}

	/**
	 * 
	 * @param context
	 * @param view
	 *            dialog的布局view
	 */
	public DialogView(Context context, View view) {
		this.mcontext = context;
		this.view = view;
		initDialog();
	}

	protected void onLoadLayout(View view) {
		// Should be override if init with #DialogView(Context context, int
		// layoutId)
	}

	protected void initDialog() {
		dialog = new android.app.Dialog(mcontext, R.style.dialog_view_theme);
		// 设置dialog显示的位置
		dialog.getWindow().setGravity(Gravity.CENTER);
		// 设置dialog进出动画
		dialog.getWindow().setWindowAnimations(R.style.dialog_view_animation);
		// 设置dialog 点击返回键的时候可以消失
		dialog.setCancelable(true);
		// 设置在点击dialog外面的时候 消失dialog
		dialog.setCanceledOnTouchOutside(true);
		// 设置dialog的内容 这里已经通过theme 把dialog的title去掉了
		dialog.setContentView(view);
	}

	/**
	 * 留下一个设置显示位置的方法 用于扩展<br/>
	 * 默认是GRAVITY.CENTER 在屏幕居中显示
	 * 
	 * @param gravity
	 *            参考android.view.Gravity 的值
	 */
	public void setGravity(int gravity) {
		dialog.getWindow().setGravity(gravity);
	}

	/**
	 * 如果没有显示就显示
	 */
	public void showDialog() {
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
			// 在stackoverflow上面查到的 解决办法 但是不明白为什么show之后再设置 才能控制dialog的宽度
			// show之前设置没用
			// LayoutParams lp = dialog.getWindow().getAttributes();
			// lp.width = LayoutParams.MATCH_PARENT;
			// lp.height = LayoutParams.WRAP_CONTENT;
			// dialog.getWindow().setAttributes(lp);
		}
	}
	
	public boolean isShowing() {
		if (dialog != null && dialog.isShowing()) {
			return true;
		}
		return false;
	}

	/**
	 * 如果正在显示就消失Dialog
	 */
	public void disMissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void setFullWidth(boolean isFull) {
		if (isFull && dialog != null) {
			LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.WRAP_CONTENT;
		}
	}

	public void setFullScreen(boolean isFull) {
		if (isFull && dialog != null) {
			LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.MATCH_PARENT;
		}
	}

	public void setCancelable(boolean cancel) {
		if (dialog != null) {
			dialog.setCancelable(cancel);
		}
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		if (dialog != null) {
			dialog.setCanceledOnTouchOutside(cancel);
		}
	}

	/**
	 * @return the mOnDialogDismissListener
	 */
	public OnDismissListener getOnDialogDismissListener() {
		return mOnDialogDismissListener;
	}

	/**
	 * @param onDialogDismissListener
	 *            the mOnDialogDismissListener to set
	 */
	public void setOnDialogDismissListener(OnDismissListener onDialogDismissListener) {
		this.mOnDialogDismissListener = onDialogDismissListener;
		if (dialog != null) {
			dialog.setOnDismissListener(mOnDialogDismissListener);
		}
	}
}
