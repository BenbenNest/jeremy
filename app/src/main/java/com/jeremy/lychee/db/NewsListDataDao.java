package com.jeremy.lychee.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NEWS_LIST_DATA.
*/
public class NewsListDataDao extends AbstractDao<NewsListData, Long> {

    public static final String TABLENAME = "NEWS_LIST_DATA";

    /**
     * Properties of entity NewsListData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_ID");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property Nid = new Property(2, String.class, "nid", false, "NID");
        public final static Property Uid = new Property(3, String.class, "uid", false, "UID");
        public final static Property Channel = new Property(4, String.class, "channel", false, "CHANNEL");
        public final static Property Time = new Property(5, Long.class, "time", false, "TIME");
        public final static Property Md5 = new Property(6, String.class, "md5", false, "MD5");
        public final static Property Cid = new Property(7, String.class, "cid", false, "CID");
        public final static Property Album_pic = new Property(8, String.class, "album_pic", false, "ALBUM_PIC");
        public final static Property Title = new Property(9, String.class, "title", false, "TITLE");
        public final static Property Zm = new Property(10, String.class, "zm", false, "ZM");
        public final static Property Url = new Property(11, String.class, "url", false, "URL");
        public final static Property Comment = new Property(12, String.class, "comment", false, "COMMENT");
        public final static Property Pdate = new Property(13, String.class, "pdate", false, "PDATE");
        public final static Property Source = new Property(14, String.class, "source", false, "SOURCE");
        public final static Property Module = new Property(15, String.class, "module", false, "MODULE");
        public final static Property Share = new Property(16, String.class, "share", false, "SHARE");
        public final static Property News_from = new Property(17, String.class, "news_from", false, "NEWS_FROM");
        public final static Property Open_type = new Property(18, String.class, "open_type", false, "OPEN_TYPE");
        public final static Property News_type = new Property(19, String.class, "news_type", false, "NEWS_TYPE");
        public final static Property News_data = new Property(20, String.class, "news_data", false, "NEWS_DATA");
        public final static Property News_stick = new Property(21, String.class, "news_stick", false, "NEWS_STICK");
        public final static Property News_stick_time = new Property(22, String.class, "news_stick_time", false, "NEWS_STICK_TIME");
        public final static Property Transmit_num = new Property(23, String.class, "transmit_num", false, "TRANSMIT_NUM");
        public final static Property Is_focus = new Property(24, String.class, "is_focus", false, "IS_FOCUS");
        public final static Property Uuid_flag = new Property(25, String.class, "uuid_flag", false, "UUID_FLAG");
        public final static Property Video_length = new Property(26, String.class, "video_length", false, "VIDEO_LENGTH");
        public final static Property Live_channel_id = new Property(27, String.class, "live_channel_id", false, "LIVE_CHANNEL_ID");
        public final static Property Live_topic_id = new Property(28, String.class, "live_topic_id", false, "LIVE_TOPIC_ID");
        public final static Property Live_channel_tag = new Property(29, String.class, "live_channel_tag", false, "LIVE_CHANNEL_TAG");
        public final static Property Summary = new Property(30, String.class, "summary", false, "SUMMARY");
    };


    public NewsListDataDao(DaoConfig config) {
        super(config);
    }
    
    public NewsListDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NEWS_LIST_DATA' (" + //
                "'_ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "'ID' TEXT," + // 1: id
                "'NID' TEXT," + // 2: nid
                "'UID' TEXT," + // 3: uid
                "'CHANNEL' TEXT," + // 4: channel
                "'TIME' INTEGER," + // 5: time
                "'MD5' TEXT," + // 6: md5
                "'CID' TEXT," + // 7: cid
                "'ALBUM_PIC' TEXT," + // 8: album_pic
                "'TITLE' TEXT," + // 9: title
                "'ZM' TEXT," + // 10: zm
                "'URL' TEXT," + // 11: url
                "'COMMENT' TEXT," + // 12: comment
                "'PDATE' TEXT," + // 13: pdate
                "'SOURCE' TEXT," + // 14: source
                "'MODULE' TEXT," + // 15: module
                "'SHARE' TEXT," + // 16: share
                "'NEWS_FROM' TEXT," + // 17: news_from
                "'OPEN_TYPE' TEXT," + // 18: open_type
                "'NEWS_TYPE' TEXT," + // 19: news_type
                "'NEWS_DATA' TEXT," + // 20: news_data
                "'NEWS_STICK' TEXT," + // 21: news_stick
                "'NEWS_STICK_TIME' TEXT," + // 22: news_stick_time
                "'TRANSMIT_NUM' TEXT," + // 23: transmit_num
                "'IS_FOCUS' TEXT," + // 24: is_focus
                "'UUID_FLAG' TEXT," + // 25: uuid_flag
                "'VIDEO_LENGTH' TEXT," + // 26: video_length
                "'LIVE_CHANNEL_ID' TEXT," + // 27: live_channel_id
                "'LIVE_TOPIC_ID' TEXT," + // 28: live_topic_id
                "'LIVE_CHANNEL_TAG' TEXT," + // 29: live_channel_tag
                "'SUMMARY' TEXT);"); // 30: summary
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NEWS_LIST_DATA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, NewsListData entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String nid = entity.getNid();
        if (nid != null) {
            stmt.bindString(3, nid);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(4, uid);
        }
 
        String channel = entity.getChannel();
        if (channel != null) {
            stmt.bindString(5, channel);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(6, time);
        }
 
        String md5 = entity.getMd5();
        if (md5 != null) {
            stmt.bindString(7, md5);
        }
 
        String cid = entity.getCid();
        if (cid != null) {
            stmt.bindString(8, cid);
        }
 
        String album_pic = entity.getAlbum_pic();
        if (album_pic != null) {
            stmt.bindString(9, album_pic);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(10, title);
        }
 
        String zm = entity.getZm();
        if (zm != null) {
            stmt.bindString(11, zm);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(12, url);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(13, comment);
        }
 
        String pdate = entity.getPdate();
        if (pdate != null) {
            stmt.bindString(14, pdate);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(15, source);
        }
 
        String module = entity.getModule();
        if (module != null) {
            stmt.bindString(16, module);
        }
 
        String share = entity.getShare();
        if (share != null) {
            stmt.bindString(17, share);
        }
 
        String news_from = entity.getNews_from();
        if (news_from != null) {
            stmt.bindString(18, news_from);
        }
 
        String open_type = entity.getOpen_type();
        if (open_type != null) {
            stmt.bindString(19, open_type);
        }
 
        String news_type = entity.getNews_type();
        if (news_type != null) {
            stmt.bindString(20, news_type);
        }
 
        String news_data = entity.getNews_data();
        if (news_data != null) {
            stmt.bindString(21, news_data);
        }
 
        String news_stick = entity.getNews_stick();
        if (news_stick != null) {
            stmt.bindString(22, news_stick);
        }
 
        String news_stick_time = entity.getNews_stick_time();
        if (news_stick_time != null) {
            stmt.bindString(23, news_stick_time);
        }
 
        String transmit_num = entity.getTransmit_num();
        if (transmit_num != null) {
            stmt.bindString(24, transmit_num);
        }
 
        String is_focus = entity.getIs_focus();
        if (is_focus != null) {
            stmt.bindString(25, is_focus);
        }
 
        String uuid_flag = entity.getUuid_flag();
        if (uuid_flag != null) {
            stmt.bindString(26, uuid_flag);
        }
 
        String video_length = entity.getVideo_length();
        if (video_length != null) {
            stmt.bindString(27, video_length);
        }
 
        String live_channel_id = entity.getLive_channel_id();
        if (live_channel_id != null) {
            stmt.bindString(28, live_channel_id);
        }
 
        String live_topic_id = entity.getLive_topic_id();
        if (live_topic_id != null) {
            stmt.bindString(29, live_topic_id);
        }
 
        String live_channel_tag = entity.getLive_channel_tag();
        if (live_channel_tag != null) {
            stmt.bindString(30, live_channel_tag);
        }
 
        String summary = entity.getSummary();
        if (summary != null) {
            stmt.bindString(31, summary);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public NewsListData readEntity(Cursor cursor, int offset) {
        NewsListData entity = new NewsListData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // uid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // channel
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // time
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // md5
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // cid
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // album_pic
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // title
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // zm
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // url
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // comment
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // pdate
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // source
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // module
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // share
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // news_from
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // open_type
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // news_type
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // news_data
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // news_stick
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // news_stick_time
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // transmit_num
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // is_focus
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // uuid_flag
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // video_length
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // live_channel_id
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // live_topic_id
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // live_channel_tag
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30) // summary
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, NewsListData entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setChannel(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTime(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setMd5(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCid(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAlbum_pic(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTitle(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setZm(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setUrl(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setComment(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPdate(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSource(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setModule(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setShare(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setNews_from(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setOpen_type(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setNews_type(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setNews_data(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setNews_stick(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setNews_stick_time(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setTransmit_num(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setIs_focus(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setUuid_flag(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setVideo_length(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setLive_channel_id(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setLive_topic_id(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setLive_channel_tag(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setSummary(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(NewsListData entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(NewsListData entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
