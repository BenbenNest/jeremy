package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jeremy.lychee.model.user.FeedNewsEntity;
import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.user.WeMediaImportArticleFeedListAdapter;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WeMediaImportArticleActivity extends SlidingActivity{

    @Bind(R.id.article_recycler_view)
    PullListRecyclerView mRecyclerView;

    @Bind(R.id.left_top)
    public TextView mAllSelect;

    @Bind(R.id.bottom_bar)
    View mBottomBar;

    private static final String ALBUM_ID = "ALBUM_ID";
    private String mDestAlbumId;

    private WeMediaImportArticleFeedListAdapter mAdapter;

    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, WeMediaImportArticleActivity.class);
        intent.putExtra(ALBUM_ID, id);
        context.startActivity(intent);
    }

    private void getExtraParameters() {
        if (getIntent() != null) {
            mDestAlbumId = getIntent().getStringExtra(ALBUM_ID);
        } else {
            finish();
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_wemedia_import_article);
        ButterKnife.bind(this);
        getExtraParameters();
        QEventBus.getEventBus().register(this);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();

        if (mRecyclerView != null) {
            mAdapter = new WeMediaImportArticleFeedListAdapter(this);
            mRecyclerView.setAdapter(mAdapter, true);
        }

    }

    @OnClick(R.id.right_top)
    void OnCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.confirm)
    void OnConfirmClicked() {
        if (mAdapter.isIsAllSelected()) {
            List<String> unSelecteds = new ArrayList<>();
            for(FeedNewsEntity i : (List<FeedNewsEntity>)mAdapter.getItems()){
                if (i.isIs_select() == FeedNewsEntity.UNSELECT) {
                    unSelecteds.add(i.getTransmit().getTransmitid());
                }
            }
            //全选true时，输入的数组表示反选的item
            importArticle(true, unSelecteds.toArray(new String[unSelecteds.size()]));
            return;
        }
        List<String> selected = new ArrayList<>();
        for(FeedNewsEntity i : (List<FeedNewsEntity>)mAdapter.getItems()){
            if (i.isIs_select() == FeedNewsEntity.SELECT) {
                selected.add(i.getTransmit().getTransmitid());
            }
        }
        if (selected.size() > 0) {
            importArticle(false, selected.toArray(new String[selected.size()]));
        } else {
            ToastHelper.getInstance(this).toast("请选择需要添加的文章");
        }
    }

    private void importArticle(boolean allSelect, String[] ids){
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(",");
            sb.append(s);
        }
        if (sb.length() > 0) {
            sb.replace(0, 1, "");
        }

        OldRetroAdapter.getService().moveFeed(
                null, mDestAlbumId,
                allSelect ? "all" : sb.toString(), allSelect ? sb.toString() : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getErrno() == 0) {
                        ToastHelper.getInstance(this).toast("添加成功");
                        QEventBus.getEventBus().post(new Events.OnAlbumDetailUpdated(null));
                        finish();
                    } else {
                        ToastHelper.getInstance(this).toast("添加失败");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastHelper.getInstance(this).toast("添加失败");
                });
    }

    @OnClick(R.id.left_top)
    void OnAllSelClicked(){
        if (mAllSelect.getText().equals(getString(R.string.menu_all_unselect_text))){
            //当前全选中状态
            mAdapter.resetAllSelect();
            mAllSelect.setText(getString(R.string.menu_all_select_text));
        }else{
            //当前是未全选状态
            mAdapter.setAllSelect();
            mAllSelect.setText(getString(R.string.menu_all_unselect_text));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }

    final public void onEventMainThread(Events.OnArticleEmpty event) {
        mBottomBar.setVisibility(View.GONE);
        mAllSelect.setVisibility(View.GONE);
    }

}
