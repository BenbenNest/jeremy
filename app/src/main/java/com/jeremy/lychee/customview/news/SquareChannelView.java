package com.jeremy.lychee.customview.news;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.news.SquareChannelAdapter;
import com.jeremy.lychee.manager.FontManager;
import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.widget.LayoutManager.WrapContentLinearLayoutManager;
import com.jeremy.lychee.widget.RecyclerViewDecoration.HorizontalSpaceItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SquareChannelView extends LinearLayout {

    @Bind(R.id.feature_recycler_view)
    RecyclerView featureRecyclerView;
    @Bind(R.id.title_txt)
    TextView titleTxt;

    private SquareChannelAdapter adapter;

    public SquareChannelView(Context context) {
        super(context);
        init(context);
    }

    public SquareChannelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(context).inflate(R.layout.square_channle_view, this, true);
        ButterKnife.bind(this, view);
        titleTxt.setTypeface(FontManager.getKtTypeface());
        initRecyclerView();
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initRecyclerView() {
        adapter = new SquareChannelAdapter(getContext());
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        featureRecyclerView.setAdapter(adapter);
        featureRecyclerView.setLayoutManager(layoutManager);
        featureRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(
                DensityUtils.dip2px(getContext(), 16)));
    }

    public void setTitle(String title) {
        titleTxt.setText(title);
    }

    public void setFeatureList(List<SquareModel.ElementModel> list) {
        adapter.setFeatureChannelList(list);
    }
}
