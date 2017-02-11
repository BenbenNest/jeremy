package com.jeremy.lychee.db2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.jeremy.lychee.db2.Program;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PROGRAM.
*/
public class ProgramDao extends AbstractDao<Program, String> {

    public static final String TABLENAME = "PROGRAM";

    /**
     * Properties of entity Program.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Column_name = new Property(1, String.class, "column_name", false, "COLUMN_NAME");
        public final static Property Channel_name = new Property(2, String.class, "channel_name", false, "CHANNEL_NAME");
        public final static Property Start_time = new Property(3, String.class, "start_time", false, "START_TIME");
        public final static Property End_time = new Property(4, String.class, "end_time", false, "END_TIME");
        public final static Property Is_live = new Property(5, String.class, "is_live", false, "IS_LIVE");
        public final static Property Is_sub = new Property(6, Boolean.class, "is_sub", false, "IS_SUB");
    };


    public ProgramDao(DaoConfig config) {
        super(config);
    }
    
    public ProgramDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PROGRAM' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'COLUMN_NAME' TEXT," + // 1: column_name
                "'CHANNEL_NAME' TEXT," + // 2: channel_name
                "'START_TIME' TEXT," + // 3: start_time
                "'END_TIME' TEXT," + // 4: end_time
                "'IS_LIVE' TEXT," + // 5: is_live
                "'IS_SUB' INTEGER);"); // 6: is_sub
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PROGRAM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Program entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
 
        String column_name = entity.getColumn_name();
        if (column_name != null) {
            stmt.bindString(2, column_name);
        }
 
        String channel_name = entity.getChannel_name();
        if (channel_name != null) {
            stmt.bindString(3, channel_name);
        }
 
        String start_time = entity.getStart_time();
        if (start_time != null) {
            stmt.bindString(4, start_time);
        }
 
        String end_time = entity.getEnd_time();
        if (end_time != null) {
            stmt.bindString(5, end_time);
        }
 
        String is_live = entity.getIs_live();
        if (is_live != null) {
            stmt.bindString(6, is_live);
        }
 
        Boolean is_sub = entity.getIs_sub();
        if (is_sub != null) {
            stmt.bindLong(7, is_sub ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Program readEntity(Cursor cursor, int offset) {
        Program entity = new Program( //
            cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // column_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // channel_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // start_time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // end_time
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // is_live
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0 // is_sub
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Program entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setColumn_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setChannel_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStart_time(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEnd_time(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIs_live(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIs_sub(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Program entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Program entity) {
        if(entity != null) {
            return entity.getId();
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
