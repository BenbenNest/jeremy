package com.jeremy.lychee.customview.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.news.SquareArticleItemAdapter;
import com.jeremy.lychee.manager.FontManager;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.widget.LayoutManager.WrappableGridLayoutManager;
import com.jeremy.lychee.widget.RecyclerViewDecoration.GridSpacingItemDecoration;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SquareArticleView extends RelativeLayout {

    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    String id;
    private SquareArticleItemAdapter adapter;

    public SquareArticleView(Context context) {
        super(context);
        init(context);
    }

    public SquareArticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.square_article, this, true);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this, view);
        titleTxt.setTypeface(FontManager.getKtTypeface());
        adapter = new SquareArticleItemAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 0,
                DensityUtils.dip2px(context, 7), false));
        recyclerView.setLayoutManager(new WrappableGridLayoutManager(getContext(), 2));
    }

    public void setModularNewsList(SquareModel.ModularModel list) {
        id = list.getId();
        titleTxt.setText(list.getName());
        setElementModelList(list.getData());
    }

    private void setElementModelList(List<SquareModel.ElementModel> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.refresh_img)
    public void refresh(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.rotation_by_center));
        if (TextUtils.isEmpty(id)) {
            return;
        }
        OldRetroAdapter.getService().refreshSquareArticle(id)
                .subscribeOn(Schedulers.io())
                .map(ModelBase::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setElementModelList, throwable -> {
                    TopToastUtil.showTopToast(recyclerView, "网络不给力，请检查网络刷新重试");
                    throwable.printStackTrace();
                });
    }
}
