package com.jeremy.lychee.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.user.TransArticleActivity;
import com.jeremy.lychee.activity.user.CreateArticleActivity;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class UserComposeFragment extends BaseFragment {

    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (null == rootView) {
            rootView = (ViewGroup)inflater.inflate(
                    R.layout.fragment_user_compose_view, container, false);
            ButterKnife.bind(this,rootView);
        }

        return rootView;

    }

    @OnClick(R.id.create_article)
    void onClickCreateArticle(){
        Intent intent = new Intent(getActivity(), CreateArticleActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.transmit_article)
    void onClickTransmitArticle(){
        //点击跳转到文章推荐界面
        ((SlidingActivity)getActivity()).openActivity(TransArticleActivity.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
