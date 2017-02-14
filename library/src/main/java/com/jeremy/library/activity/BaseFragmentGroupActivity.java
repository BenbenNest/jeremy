package com.jeremy.library.activity;

import android.app.ProgressDialog;


/**
 * Created by zouzhihui on 15/7/2.
 */
public abstract class BaseFragmentGroupActivity extends AppCompatFragmentGroupActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        mProgressDialog = null;
        super.onDestroy();
    }

    public void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
