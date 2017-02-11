package com.jeremy.lychee.presenter;

import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.db.NewsListDataDao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class ContentFragmentDbHelper {
    private static final int MAX_COUNT_CHANNEL_NEWS = 300;

    public synchronized static List<NewsListData> getStickNewsHistory(String channel) {
        try {
            NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
            return dao.queryBuilder()
                    .where(NewsListDataDao.Properties.Channel.eq(channel),
                            NewsListDataDao.Properties.News_stick.eq("1"))
                    .orderDesc(NewsListDataDao.Properties.Time).build().list();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized static List<NewsListData> getNewsHistory(String channel, int uuidLimit, int offset) {
        try {
            NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
            QueryBuilder<NewsListData> builder = dao.queryBuilder();
            builder.where(NewsListDataDao.Properties.Channel.eq(channel),
                    builder.or(NewsListDataDao.Properties.Is_focus.isNull(), NewsListDataDao.Properties.Is_focus.notEq("1")));
            List<NewsListData> list = builder.limit(uuidLimit * 13).offset(offset).orderDesc(NewsListDataDao.Properties.Time).build().list();
            if (list == null || list.size() == 0) {
                return null;
            }
            int i = 0;
            int j = 0;
            String uuidFlag = "";
            for (NewsListData data : list) {
                if (data.getUuid_flag().equals(uuidFlag)) {
                    i++;
                }else {
                    uuidFlag = data.getUuid_flag();
                    if (j < uuidLimit) {
                        j++;
                        i++;
                    }else {
                        break;
                    }
                }
            }
            return list.subList(0,i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public synchronized static List<NewsListData> getAllNewsHistory(String channel, int limit, int offset) {
////        List<NewsListData> stickNewsHistory = getStickNewsHistory(channel);
////        int size = 0;
////        if (stickNewsHistory != null) {
////            size = stickNewsHistory.size();
////        }
//        List<NewsListData> newsHistory = getNewsHistory(channel, limit, offset);
//        return ChannelFragmentPresenter.AppendToList(stickNewsHistory, newsHistory);
//    }

    public static void saveToDB(List<NewsListData> beans, String channel) {
        try {
            NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
            for (NewsListData data : beans) {
                dao.queryBuilder().where(NewsListDataDao.Properties._id.eq(data.get_id())).buildDelete()
                        .executeDeleteWithoutDetachingEntities();
            }
            dao.insertOrReplaceInTx(beans);
            long count = dao.queryBuilder().where(NewsListDataDao.Properties.Channel.eq(channel)).count();
            if (count > MAX_COUNT_CHANNEL_NEWS) {
                int limit = (int) (count - MAX_COUNT_CHANNEL_NEWS);
                List<NewsListData> list = dao.queryBuilder()
                        .where(NewsListDataDao.Properties.Channel.eq(channel)).limit(limit)
                        .offset(MAX_COUNT_CHANNEL_NEWS).orderDesc(NewsListDataDao.Properties.Time).build().list();
                dao.deleteInTx(list);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void saveImageToDB(NewsListData beans, String channel, String nid) {
        try {
            NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
            dao.insertOrReplaceInTx(beans);
            List<NewsListData> query = dao.queryBuilder().where(NewsListDataDao.Properties.Channel.eq(channel)).orderAsc(NewsListDataDao.Properties.Time).build().list();
            dao.deleteInTx(query);
            beans.setNid(nid);
            beans.setTime(System.currentTimeMillis());
            beans.setChannel(channel);
            dao.insert(beans);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static NewsListData getValidFocusNews(String channel) {
        NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
        List<NewsListData> list = dao.queryBuilder().where(NewsListDataDao.Properties.Channel.eq(channel),
                NewsListDataDao.Properties.Is_focus.eq("1")).limit(1).orderAsc(NewsListDataDao.Properties.Time).build().list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void setFocusNewsStatus(NewsListData data) {
        data.setIs_focus("0");
        NewsListDataDao dao = ContentApplication.getDaoSession().getNewsListDataDao();
        dao.insertOrReplace(data);
    }
}
