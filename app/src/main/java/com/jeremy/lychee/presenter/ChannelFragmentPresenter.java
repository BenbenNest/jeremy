package com.jeremy.lychee.presenter;

import android.os.Bundle;

import com.jeremy.lychee.activity.news.ImageNewsActivity;
import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.activity.news.SceneDirectSeedingActivity;
import com.jeremy.lychee.activity.news.SubjectLlistActivity;
import com.jeremy.lychee.activity.news.WebViewActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.news.NewsListDataWrapper;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;

public class ChannelFragmentPresenter {
    public final static String EXTRA_CHANNEL_NEWS_OBJECT = "NEW_ENTITY";

    public static List<NewsListData> AppendToList(List<NewsListData> mainList, List<NewsListData> subList) {

        if (mainList == null) {
            mainList = new ArrayList<>();
        }else {
            mainList = new ArrayList<>(mainList);
        }
        if (subList == null) {
            return mainList;
        }
        for (NewsListData data : subList) {
            boolean flag = true;
            for (NewsListData data1 : mainList) {
                if (data1.getNid().equals(data.getNid())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                mainList.add(data);
            }
        }
        return mainList;
    }

    public static void OpenNewsDetailActivity(SlidingActivity activity, NewsListData data,
                                              String scene, String channel) {
        if (data == null) {
            return;
        }
        int newsType;
        try {
            newsType = Integer.parseInt(data.getNews_type());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        switch (newsType) {
            case 1:
                OpenTxtNews(activity, data, scene, channel);
                break;
            case 2:
                ImageNewsActivity.startActivity(activity, data);
                break;
            case 3:
                OpenTxtNews(activity, data, scene, channel);
//                String newsData = data.getNews_data();
//                try {
//                    JSONObject jsonObject = new JSONObject(newsData);
//                    String video_id = jsonObject.getString("video_id");
//                    String source_type = jsonObject.getString("source_type");
//                    String tag = jsonObject.getString("tag");
//                    LivePlayerLoadingActivity.loadVideo(activity,video_id,source_type,tag );
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
            case 4://图文直播
                Bundle bundle = new Bundle();
                bundle.putParcelable(SceneDirectSeedingActivity.NEW_ENTITY, new NewsListDataWrapper(data));
                activity.openActivity(SceneDirectSeedingActivity.class, bundle, 0);
                break;
            case 5:
                SubjectLlistActivity.startActivity(activity, data.getNid(), data.getUrl());
                break;
            case 6://外链
                Bundle bundle2 = new Bundle();
                bundle2.putString("url", data.getUrl());
                activity.openActivity(WebViewActivity.class, bundle2, 0);
                break;
            default:
                Logger.e("news type: " + newsType + " is not support!");
        }
    }

    private static void OpenTxtNews(SlidingActivity activity, NewsListData data,
                                    String scene, String channel) {
        int openType = 0;
        try {
            openType = Integer.parseInt(data.getOpen_type());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        switch (openType) {
            case 0:
                OldNewsDetailActivity.startActivity(
                        activity, data, 0,
                        null, null, channel, scene);
                break;
            case 1:
                bundle.putParcelable(EXTRA_CHANNEL_NEWS_OBJECT, new NewsListDataWrapper(data));
                activity.openActivity(WebViewActivity.class, bundle, 0);
                break;
        }
    }
}
