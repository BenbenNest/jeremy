package com.jeremy.lychee.adapter.user;

/**
 * Created by leikang on 2015/11/22.
 * footer view 不同状态间的更新
 * */
public interface LoadViewAction {
    /***
     * 加载中
     * */
    public void onLoading();

    /**
     *  静止状态中
     *  @return boolean true need remove footerView , false no
     * */
    public boolean onNormal(boolean hasMore);

}
