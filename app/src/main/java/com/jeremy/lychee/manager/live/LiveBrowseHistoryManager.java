package com.jeremy.lychee.manager.live;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.preference.NewsListPreference;
import com.jeremy.lychee.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhangying-pd on 2015/12/24.
 * LiveBrowseHistory add\del or get data manager
 */
public class LiveBrowseHistoryManager {
    private final static String tag = "tag";
    private static LiveBrowseHistoryManager ourInstance = new LiveBrowseHistoryManager();

    public static LiveBrowseHistoryManager getInstance() {
        return ourInstance;
    }

    private LiveBrowseHistoryManager() {

    }

    /**
     * 添加浏览历史数据
     * @param item LiveVideoInfo
     */
    public void addBrowseItem(LiveVideoInfo item){
        synchronized (tag){
            if(item==null){
                return;
            }
            if(verify(item.getVideo_islive())){
                long currentTimeMillis = System.currentTimeMillis()/1000;
                item.setPdate(currentTimeMillis);
                LinkedHashMap<String, LiveVideoInfo> live_map = getMapDateFromLocal();
                if(live_map==null){
                    live_map = new LinkedHashMap<>();
                }
                String key = item.getId();
                if(live_map.containsKey(key)){
                    live_map.remove(key);
                }
                live_map.put(key, item);
                String value = GsonUtils.toJson(live_map);
                saveToLocal(value);
            }

        }
    }

    /**
     * ；验证是否符合保存到播放记录的要求
     * @return false 不保存   true  保存
     */
    private boolean verify(String video_islive){
        return !"1".equals(video_islive);
    }


    /**
     * 删除some 浏览历史item
     * @param item LiveChannel
     */
    public void removeBrowseItem(LiveVideoInfo item){
        synchronized (tag){
            String key = item.getId();
            LinkedHashMap<String, LiveVideoInfo> live_map = getMapDateFromLocal();
            if(live_map!=null&&live_map.containsKey(key)){
                live_map.remove(key);
                String value = GsonUtils.toJson(live_map);
                saveToLocal(value);
            }

        }
    }

    public List<LiveVideoInfo> getBrowseVideoList(){
        synchronized (tag){
            return getListDateFromLocal();
        }
    }

    /**
     * 保存数据到本地
     * @param value 被保存的string 数据
     */
    private static void saveToLocal(String value){
        String save_key = "LIVEBROWSEHISTORY";
        User mUsrInfo = Session.getSession();
        if(mUsrInfo!=null){
            save_key+=mUsrInfo.getUid();
        }

        NewsListPreference.getInstance().saveStringValue(save_key, value);
    }

    /**
     *  获取本地被保存的数据
     */
    private static List<LiveVideoInfo> getListDateFromLocal(){
        try {
            LinkedHashMap<String, LiveVideoInfo> live_map = getMapDateFromLocal();
            if(live_map==null){
                return null;
            }
            List<LiveVideoInfo> list = new ArrayList<>(live_map.values());
            Collections.reverse(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LinkedHashMap<String, LiveVideoInfo> getMapDateFromLocal(){
        try {
            String save_key = "LIVEBROWSEHISTORY";
            User mUsrInfo = Session.getSession();
            if(mUsrInfo!=null){
                save_key+=mUsrInfo.getUid();
            }


            String value = NewsListPreference.getInstance().getStringValue(save_key);
            if(TextUtils.isEmpty(value)){
                return null;
            }
            new LinkedHashMap<>();
            Type type = new TypeToken<LinkedHashMap<String, LiveVideoInfo>>() {}.getType();
            return GsonUtils.fromJson(value, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
