package com.jeremy.lychee.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.user.WeMediaChannelAlbumArticleFeedListAdapter;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.model.user.FeedNewsEntity;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WeMediaAlbumDetailActivity extends SlidingActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Bind(R.id.article_recycler_view)
    PullListRecyclerView mRecyclerView;

    @Bind(R.id.bottom_bar)
    View mBottomBar;

    @Bind(R.id.left_top)
    public TextView mAllSelect;

    @Bind(R.id.restore)
    View mRestoreBtn;

    private boolean mIsMy;
    private String mAlbumId;
    private String mAlbumName;
    private static final String ALBUM_ID = "ALBUM_ID";
    private static final String ALBUM_NAME = "ALBUM_POSTER";
    private static final String ALBUM_IS_MY = "IS_MY";
    private boolean mIsSelectMode = false;

    private WeMediaChannelAlbumArticleFeedListAdapter mAdapter;
    private MenuItem mEditArticleMenu;

    public static void startActivity(Context context, String id, String name, boolean isMy) {
        Intent intent = new Intent(context, WeMediaAlbumDetailActivity.class);
        intent.putExtra(ALBUM_ID, id);
        intent.putExtra(ALBUM_NAME, name);
        intent.putExtra(ALBUM_IS_MY, isMy);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    private void getExtraParameters() {
        if (getIntent() != null) {
            mAlbumId = getIntent().getStringExtra(ALBUM_ID);
            mAlbumName = getIntent().getStringExtra(ALBUM_NAME);
            mIsMy = getIntent().getBooleanExtra(ALBUM_IS_MY, false);
        } else {
            finish();
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_wemedia_album_detail);
        ButterKnife.bind(this);
        QEventBus.getEventBus().register(this);
        getExtraParameters();
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        //以下toolbar的设置顺序不能改变
        mToolBar.setTitle(mAlbumName);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        if (mRecyclerView != null) {
            mAdapter = new WeMediaChannelAlbumArticleFeedListAdapter(this, mAlbumId, mIsMy);
            mRecyclerView.setAdapter(mAdapter, true);
            mRecyclerView.enablePullToRefresh(!mIsMy);//自己的专辑列表不需要刷新
        }
    }

    public void setArticlesEditMenuVisibility(boolean val) {
        if (!mIsMy) return;
        if (mAlbumName.equals("默认专辑")) {
            findViewById(R.id.menu).setVisibility(val ? View.VISIBLE : View.GONE);
        }
        mEditArticleMenu.setVisible(val);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_name:
                doEdit();
                return true;
            case R.id.menu_delete:
                if (mAdapter.getItems().size() > 0) {
                    DialogUtil.showAlertDialog(this, "请清空专辑下的所有文章后再删除", null);
                } else {
                    doDelete();
                }
                return true;
            case R.id.menu_edit_article_list:
                setSelectMode(true);
                return true;
            case R.id.menu_add_article:
                WeMediaImportArticleActivity.startActivity(this, mAlbumId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsMy) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_wemedia_album_detail, menu);
            mToolBar.post(() -> {
                final View v = findViewById(R.id.menu);
                if (v != null) {
                    //禁止一级menu长按toast
                    v.setOnLongClickListener(v1 -> false);
                }
            });
            mEditArticleMenu = menu.findItem(R.id.menu_edit_article_list);
            if (mAlbumName.equals("默认专辑")) {
                menu.findItem(R.id.menu_add_article).setVisible(false);
                menu.findItem(R.id.menu_edit_name).setVisible(false);
                menu.findItem(R.id.menu_delete).setVisible(false);
                mRestoreBtn.setVisibility(View.GONE);
            }
        }
        return true;
    }

    private void doEdit() {
        DialogUtil.showEditTextDialog(this, "修改专辑名", mToolBar.getTitle().toString(),
                (DialogInterface dialog, CharSequence str) -> updateAlbum(dialog, str.toString()),
                (DialogInterface dialog, CharSequence str) -> dialog.dismiss());
    }


    private void doDelete() {
        DialogUtil.showConfirmDialog(this, "确定要删除此专辑吗？",
                getString(R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                    deleteAlbum();
                    dialog.dismiss();
                },
                getString(R.string.dialog_button_cancel), DialogInterface::dismiss);
    }

    static volatile boolean sflg_ = true;

    private void updateAlbum(DialogInterface dialog, String name) {
        //判空
        if (TextUtils.isEmpty(name)) {
            ToastHelper.getInstance(this).toast("专辑名不能为空");
            return;
        }

        synchronized (WeMediaAlbumDetailActivity.this) {
            sflg_ = true;
        }
        WeakReference<ProgressDialog> dialog_ =
                new WeakReference<>(new ProgressDialog(WeMediaAlbumDetailActivity.this));
        dialog_.get().setIndeterminate(true);
        dialog_.get().setMessage("专辑名更新中...");
        dialog_.get().setCancelable(false);
        if (sflg_ && dialog_.get() != null) {
            dialog_.get().show();
        }
        //上传数据
        OldRetroAdapter.getService().editAlbum(mAlbumId, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .flatMap(s -> {
                    if (s.getErrno() != 0) {
                        return Observable.just(null);
                    } else {
                        return Observable.just(s.getData());
                    }
                })
                .subscribe(
                        s -> {
                            if (s != null) {
                                QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                                dialog.dismiss();
                                mToolBar.setTitle(name);
                            } else {
                                ToastHelper.getInstance(this).toast("专辑名更新失败");
                            }
                            dismissProgressDialog(dialog_);
                        }, e -> {
                            ToastHelper.getInstance(this).toast("专辑名更新失败");
                            dismissProgressDialog(dialog_);
                        }, () -> dismissProgressDialog(dialog_)
                );

    }

    private void dismissProgressDialog(WeakReference<ProgressDialog> dialog) {
        synchronized (WeMediaAlbumDetailActivity.this) {
            sflg_ = false;
            if (dialog.get() != null && dialog.get().isShowing())
                dialog.get().dismiss();
        }
    }

    private void deleteAlbum() {
        OldRetroAdapter.getService().deleteAlbum(mAlbumId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            if (s.getErrno() == 0) {
                                QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                                ToastHelper.getInstance(this).toast("专辑删除成功");
                                finish();
                            } else {
                                ToastHelper.getInstance(this).toast("专辑删除失败");
                            }
                        },
                        e -> {
                            e.printStackTrace();
                            ToastHelper.getInstance(this).toast("专辑删除失败");
                        });
    }

    private void resetAllSelect() {
        mAdapter.resetAllSelect();
        mAllSelect.setText(getString(R.string.menu_all_select_text));
    }

    @Override
    public void onBackPressed() {
        if (mIsSelectMode) {
            resetAllSelect();
            setSelectMode(false);
        } else {
            finish();
        }
    }

    @OnClick(R.id.restore)
    void OnRestoreClicked() {
        DialogUtil.showConfirmDialog(this, "将所选文章移除到默认专辑吗？",
                this.getString(R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                    if (mAdapter.isIsAllSelected()) {
                        List<String> unSelecteds = new ArrayList<>();
                        for (FeedNewsEntity i : (List<FeedNewsEntity>) mAdapter.getItems()) {
                            if (i.isIs_select() == FeedNewsEntity.UNSELECT) {
                                unSelecteds.add(i.getTransmit().getTransmitid());
                            }
                        }
                        //全选true时，输入的数组表示反选的item
                        restoreArticle(true, unSelecteds.toArray(new String[unSelecteds.size()]));
                        QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                    } else {
                        List<String> selected = new ArrayList<>();
                        for (FeedNewsEntity i : (List<FeedNewsEntity>) mAdapter.getItems()) {
                            if (i.isIs_select() == FeedNewsEntity.SELECT) {
                                selected.add(i.getTransmit().getTransmitid());
                            }
                        }
                        if (selected.size() > 0) {
                            restoreArticle(false, selected.toArray(new String[selected.size()]));
                            QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                        } else {
                            ToastHelper.getInstance(this).toast("请选择需要移除的文章");
                        }
                    }
                    dialog.dismiss();
                }, this.getString(R.string.dialog_button_cancel), DialogInterface::dismiss
        );
    }

    private void restoreArticle(boolean allSelect, String[] ids) {
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(",");
            sb.append(s);
        }
        if (sb.length() > 0) {
            sb.replace(0, 1, "");
        }

        OldRetroAdapter.getService().moveFeed(
                mAlbumId, null,
                allSelect ? "all" : sb.toString(), allSelect ? sb.toString() : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getErrno() == 0) {
                        ToastHelper.getInstance(this).toast("移除成功");
                        onBackPressed();
                        mRecyclerView.loadData();
                    } else {
                        ToastHelper.getInstance(this).toast("移除失败");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastHelper.getInstance(this).toast("移除失败");
                });
    }

    @OnClick(R.id.move_to)
    void OnMoveToClicked() {
        if (mAdapter.isIsAllSelected()) {
            List<String> unSelecteds = new ArrayList<>();
            for (FeedNewsEntity i : (List<FeedNewsEntity>) mAdapter.getItems()) {
                if (i.isIs_select() == FeedNewsEntity.UNSELECT) {
                    unSelecteds.add(i.getTransmit().getTransmitid());
                }
            }
            //全选true时，输入的数组表示反选的item
            WeMediaChangeAlbumActivity.startActivity(this, true,
                    unSelecteds.toArray(new String[unSelecteds.size()]), mAlbumId);
            QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
            return;
        }
        List<String> selected = new ArrayList<>();
        for (FeedNewsEntity i : (List<FeedNewsEntity>) mAdapter.getItems()) {
            if (i.isIs_select() == FeedNewsEntity.SELECT) {
                selected.add(i.getTransmit().getTransmitid());
            }
        }
        if (selected.size() > 0) {
            WeMediaChangeAlbumActivity.startActivity(this, false,
                    selected.toArray(new String[selected.size()]), mAlbumId);
            QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
        } else {
            ToastHelper.getInstance(this).toast("请选择需要移动的文章");
        }
    }

    @OnClick(R.id.right_top)
    void OnCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.left_top)
    void OnAllSelClicked() {
        if (mAllSelect.getText().equals(getString(R.string.menu_all_unselect_text))) {
            //当前全选中状态
            mAdapter.resetAllSelect();
            mAllSelect.setText(getString(R.string.menu_all_select_text));
        } else {
            //当前是未全选状态
            mAdapter.setAllSelect();
            mAllSelect.setText(getString(R.string.menu_all_unselect_text));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setSelectMode(boolean val) {
        mIsSelectMode = val;
        mAdapter.setSelectMode(mIsSelectMode);
        setSlideEnabled(!mIsSelectMode);
        mToolBar.setVisibility(mIsSelectMode ? View.GONE : View.VISIBLE);
        mBottomBar.setVisibility(mIsSelectMode ? View.VISIBLE : View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }

    final public void onEventMainThread(Events.OnAlbumDetailUpdated event) {
        setSelectMode(false);
        if (event.transmitId == null) {
            mRecyclerView.loadData();
            QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
            return;
        }
        List<FeedNewsEntity> list = (List<FeedNewsEntity>) mAdapter.getItems();
        for (FeedNewsEntity i : list) {
            if (i.getTransmit().getTransmitid().equals(event.transmitId)) {
                int position = list.indexOf(i);
                list.remove(position);
                mAdapter.notifyItemRemoved(position);
                break;
            }
        }
        setArticlesEditMenuVisibility(list.size() != 0);
        QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(mAlbumId));
    }


}
