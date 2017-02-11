package com.jeremy.lychee.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jeremy.lychee.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadingRecyclerViewFooter extends LinearLayout {

    @Bind(R.id.load_label)
    TextView loadLabel;
    @Bind(R.id.load_progressBar)
    ProgressBar loadProgressBar;

    private FooterClickListener footerListener;
    private FooterStatus currentStatus = FooterStatus.idle;

    private String fullText;
    public LoadingRecyclerViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadingRecyclerViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_footer, this, true);
        ButterKnife.bind(this, view);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setStatus(currentStatus);
    }

    @OnClick(R.id.load_label)
    public void loading() {
        if (footerListener == null) {
            return;
        }
        if (currentStatus == FooterStatus.full) {
            footerListener.onFullRefresh();
            setStatus(FooterStatus.loading);
        }else if (currentStatus == FooterStatus.error) {
            footerListener.onErrorClick();
            setStatus(FooterStatus.loading);
        }

    }

    public void setFooterListener(FooterClickListener footerListener) {
        this.footerListener = footerListener;
    }

    public void setStatus(FooterStatus status) {
        if (status == currentStatus) {
            return;
        }
        currentStatus = status;
        switch (status) {
            case idle:
                loadProgressBar.setVisibility(GONE);
                loadLabel.setVisibility(INVISIBLE);
                break;
            case loading:
                loadProgressBar.setVisibility(VISIBLE);
                loadLabel.setVisibility(VISIBLE);
                loadLabel.setText(R.string.doing_update);
                break;
            case full:
                loadProgressBar.setVisibility(GONE);
                loadLabel.setVisibility(VISIBLE);
                loadLabel.setText(TextUtils.isEmpty(fullText)?getContext().getString(R.string.full_data):fullText);
                break;
            case error:
                loadProgressBar.setVisibility(GONE);
                loadLabel.setVisibility(VISIBLE);
                loadLabel.setText("加载失败,点击重试!");
                break;

        }
    }


    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public interface FooterClickListener {
        void onFullRefresh();

        void onErrorClick();
    }


    public enum FooterStatus {
        idle,
        loading,
        full,
        error,
    }
}
