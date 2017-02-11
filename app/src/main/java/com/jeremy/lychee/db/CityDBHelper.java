package com.jeremy.lychee.db;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;

import com.jeremy.lychee.R;
import com.jeremy.lychee.model.news.City;
import com.jeremy.lychee.utils.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CityDBHelper extends SQLiteOpenHelper {
    private final static String dbName = "cities.db";
    private static final int version = 1;
    private Context mContext;
    private String dbPath;
    private SQLiteDatabase db;

    public CityDBHelper(Context ctx) {
        super(ctx, dbName, null, version);
        this.mContext = ctx;

        dbPath = Environment.getDataDirectory().getPath() + File.separator
                + "data" + File.separator + mContext.getPackageName()
                + File.separator + "databases" + File.separator;
        try {
            SQLiteDatabase localSQLiteDatabase = initCityDB();
            if (localSQLiteDatabase != null) {
                localSQLiteDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 初始化city数据库
     */
    private SQLiteDatabase initCityDB() {

        String dstFileName = dbPath + dbName;
        File dstFile = new File(dstFileName);
        if (!dstFile.getParentFile().exists()) {
            dstFile.getParentFile().mkdirs();
        }

        SQLiteDatabase res = null;
        if (!dstFile.exists()) {
            try {
                FileHelper.copyFile(
                        mContext.getResources().openRawResource(R.raw.cities),
                        new FileOutputStream(dstFile));
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            res = SQLiteDatabase.openOrCreateDatabase(dstFileName, null);
            res.setVersion(version);
        }

        return res;
    }


    /**
     * 打开city数据库
     */
    public void openDataBase() {

        // Open the database
        String path = dbPath + dbName;
        try {
            db = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭city数据库
     */
    @Override
    public synchronized void close() {
        try {
            if (db != null && db.isOpen()) {
                db.close();
            }
            super.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热门城市列表
     */
    public ArrayList<City> getHotCities() {
        ArrayList<City> res = null;
        String sql = "select code,name from hotcity";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return res;
        }
        try {
            Cursor cursor = db.rawQuery(sql, null);
            res = new ArrayList<City>();
            while (cursor.moveToNext()) {
                City city = new City();
                city.setCode(cursor.getString(0));
                city.setCity(cursor.getString(1));
                // 每次将新纪录插在第一个element里
                res.add(0, city);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将城市插入到城市列表前面
     *
     * @param c
     */
    public void resortCities(City c) {
        if (c == null || TextUtils.isEmpty(c.getCode()))
            return;
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return;
        }
        String cityCode = c.getCode();
        String sql = "select code,name from hotcity where code = ?";
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{cityCode});
            int cnt = cursor.getCount();
            if (cnt != 1) {
                //所查城市没有在hotcity里
                if (cnt < 1) {
                    sql = "insert into hotcity values(?, ?)";
                    db.execSQL(sql, new String[]{c.getCode(), c.getCity()});
                }
                return;
            }

            City city = null;
            while (cursor.moveToNext()) {
                city = new City();
                city.setCode(cursor.getString(0));
                city.setCity(cursor.getString(1));
            }
            if (city == null)
                return;
            sql = "delete from hotcity where code = ?";
            db.beginTransaction();
            try {
                db.execSQL(sql, new String[]{cityCode});
                sql = "insert into hotcity values(?, ?)";
                db.execSQL(sql, new String[]{city.getCode(), city.getCity()});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据query检索匹配的城市列表
     *
     * @param query
     */
    public List<City> queryCities(String query) {
        List<City> res = null;
        //String sql = "select code,name,province from city where shortpinyin like ? or fullpinyin like ? or name like ? or province like ?";
        String sql = "select code,name,province,pinyin from city where name like ? or province like ? or shortpinyin like ? or fullpinyin like ? order by case " +
                "when name like ? then 1 " +
                "when name like ? then 2 " +
                "when province like ?  then 3 " +
                "when province like ?  then 4 " +
                "when shortpinyin like ? then 5 " +
                "when shortpinyin like ? then 6 " +
                "when fullpinyin like ? then 7 " +
                "when fullpinyin like ? then 8 " +
                "else 9 end";

        String likeQuery = "%" + query + "%";
        String firstQuery = query + "%";
        //TODO 为什么与likeQuery一样
        String secondQuery = "%" + query + "%";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return res;
        }
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{likeQuery, likeQuery, likeQuery, likeQuery, firstQuery, secondQuery, firstQuery, secondQuery, firstQuery, secondQuery, firstQuery, secondQuery});
            res = new ArrayList<City>();
            while (cursor.moveToNext()) {
                City city = new City();
                city.setCode(cursor.getString(0));
                city.setCity(cursor.getString(1));
                city.setProvince(cursor.getString(2));
                city.setCitySpell(cursor.getString(3));
                res.add(city);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<City> getAllCities() {
        List<City> list = new ArrayList<>();
        String sql = "select code,name,province,pinyin from city order by pinyin";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return null;
        }
        try {

            Cursor cursor = db.rawQuery(sql, null);
            while (cursor != null && cursor.moveToNext()) {
                City city = new City();
                city.setCode(cursor.getString(0));
                city.setCity(cursor.getString(1));
                city.setProvince(cursor.getString(2));
                city.setCitySpell(cursor.getString(3));
                list.add(city);
            }
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取对应省市区的citycode
     *
     * @param province
     * @param city
     * @param district
     */
    public String getCityCode(String province, String city, String district) {
        // province 去掉省
        // city去掉 市
        // district 去掉区，县
        String code = null;
        //默认定位到的province和city和district不能同时为空
        if (TextUtils.isEmpty(province) && (TextUtils.isEmpty(city) && TextUtils.isEmpty(district)))
            return code;
        String sql = "select code from city where name like ? and province like ?";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return code;
        }
        String[] params = null;
        Cursor cursor = null;

        try {
            if (TextUtils.isEmpty(province)) {
                params = new String[]{district + "%", city + "%"};
                cursor = db.rawQuery(sql, params);
                while (cursor.moveToNext()) {
                    code = cursor.getString(0);
                    break;
                }
                return code;
            }

            cursor = db.rawQuery(sql, new String[]{district + "%", province + "%"});
            if (cursor.getCount() >= 1) {
                while (cursor.moveToNext()) {
                    code = cursor.getString(0);
                    break;
                }
            } else {
                cursor = db.rawQuery(sql, new String[]{city + "%", province + "%"});
                if (cursor.getCount() >= 1) {
                    while (cursor.moveToNext()) {
                        code = cursor.getString(0);
                        break;
                    }
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public City getCityByCode(String code) {
        String sql = "select code,name,province from city where code=?";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return null;
        }
        try {
            Cursor cursor = db.rawQuery(sql, new String[]{code});
            City city = new City();
            while (cursor.moveToNext()) {
                city.setCode(cursor.getString(0));
                city.setCity(cursor.getString(1));
                city.setProvince(cursor.getString(2));
                break;
            }
            cursor.close();
            db.close();
            return city;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public City getCityByProvinceAndName(String province, String name) {
//        String sql = "select code,name,province from city where province=?";
//        if (db == null || !db.isOpen()) {
//            openDataBase();
//        }
//        if (db == null || !db.isOpen()) {
//            return null;
//        }
//        try {
//            Cursor cursor = db.rawQuery(sql, new String[]{code});
//            City city = new City();
//            while (cursor.moveToNext()) {
//                city.setCode(cursor.getString(0));
//                city.setCity(cursor.getString(1));
//                city.setProvince(cursor.getString(2));
//                break;
//            }
//            cursor.close();
//            db.close();
//            return city;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 获取对应省市区的citySpell
     *
     * @param cityName
     */
    public String getCitySpell(String cityName) {
        // province 去掉省
        // city去掉 市
        // district 去掉区，县
        String code = null;
        //默认定位到的province和city和district不能同时为空
        if (TextUtils.isEmpty(cityName)) {
            return code;
        }
        String sql = "select code from cityspell where name like ?";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return code;
        }

        try {

            Cursor cursor = db.rawQuery(sql, new String[]{cityName + "%"});
            while (cursor != null && cursor.moveToNext()) {
                int index = cursor.getColumnIndex("code");
                code = cursor.getString(index);
                break;
            }
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }


    /**
     * 获取对应省市区的cityCode
     *
     * @param cityName
     */
    public String getCityCode(String cityName) {
        // province 去掉省
        // city去掉 市
        // district 去掉区，县
        String code = null;
        //默认定位到的province和city和district不能同时为空
        if (TextUtils.isEmpty(cityName))
            return code;
        String sql = "select code from city where name like ?";
        if (db == null || !db.isOpen()) {
            openDataBase();
        }
        if (db == null || !db.isOpen()) {
            return code;
        }

        try {

            Cursor cursor = db.rawQuery(sql, new String[]{cityName + "%"});
            while (cursor != null && cursor.moveToNext()) {
                int index = cursor.getColumnIndex("code");
                code = cursor.getString(index);
                break;
            }
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
    
    
    /**
     * 完善city 补充城市拼写和省份拼写
     * @param city
     * @return
     */
	public City completeCity(City city) {
		String cityCode = getCityCode(city.getCity());
		city.setCode(cityCode);
		city.setCitySpell(getCitySpell(city.getCity()));
		city.setProvinceSpell(getCitySpell(city.getProvince()));
		return city;
	}
}
