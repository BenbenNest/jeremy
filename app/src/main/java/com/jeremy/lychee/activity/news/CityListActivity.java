package com.jeremy.lychee.activity.news;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremy.lychee.adapter.news.CityListAdapter;
import com.jeremy.lychee.db.CityDBHelper;
import com.jeremy.lychee.manager.QhLocationManager;
import com.jeremy.lychee.model.news.City;
import com.jeremy.lychee.widget.IndexableListView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.qihu.mobile.lbs.location.QHLocation;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CityListActivity extends SlidingActivity {

    private IndexableListView mListView;
    private CityDBHelper mCityDBHelper;
    private List<City> mList;
    private CityListAdapter mCityListAdapter;
    private View mHeaderView;
    private boolean isSearchMode;
    private Toolbar toolbar;

    @Override
    protected void setContentView() {
        mCityDBHelper = new CityDBHelper(this);

        setContentView(com.jeremy.lychee.R.layout.activity_citylist);
        toolbar = (Toolbar) findViewById(com.jeremy.lychee.R.id.toolbar);
        mListView = (IndexableListView) findViewById(com.jeremy.lychee.R.id.lv_citylist);


        mHeaderView = View.inflate(this, com.jeremy.lychee.R.layout.item_city, null);
        TextView tvSection = (TextView) mHeaderView.findViewById(com.jeremy.lychee.R.id.tv_section);
        TextView tvCityname = (TextView) mHeaderView.findViewById(com.jeremy.lychee.R.id.tv_city_name);
        tvSection.setText("定位到的城市");
        mListView.addHeaderView(mHeaderView, null, false);
        mListView.setFastScrollEnabled(true);

        mList = mCityDBHelper.getAllCities();
        mCityListAdapter = new CityListAdapter(this, mList, mListView.getHeaderViewsCount());
        mListView.setAdapter(mCityListAdapter);

        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    City city = (City) v.getTag();
                    city.setProvinceSpell(mCityDBHelper.getCitySpell(city.getProvince()));
//                    SharePreferenceUtil.saveCity(mHeaderView.getContext(), city);
                }
//                EventBus.getDefault().post(new CitySelectedEvent());
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = mList.get(position - mListView.getHeaderViewsCount());
                city.setProvinceSpell(mCityDBHelper.getCitySpell(city.getProvince()));
//                SharePreferenceUtil.saveCity(view.getContext(), city);
//                EventBus.getDefault().post(new CitySelectedEvent());
                finish();
            }
        });


        SearchView searchView = (SearchView) findViewById(com.jeremy.lychee.R.id.sv_search);
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconified(false);
        searchView.setQueryHint("搜索城市");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });

        EditText editText = (EditText) searchView.findViewById(com.jeremy.lychee.R.id.search_src_text);
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(14);
        editText.setHintTextColor(Color.parseColor("#8f8f8f"));
//        showSoftInputMethod();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                isSearchMode = s.length() != 0;
                new QuaryTask().execute(s.toString());
                return true;
            }
        });

        initToolBar();
        QhLocationManager.getInstance().updateLocation(() -> {
            QHLocation qhLocation = QhLocationManager.getInstance().getLocation();
            final CityDBHelper cityDBHelper = new CityDBHelper(getApplicationContext());
            if (qhLocation != null) {
                Observable.just(qhLocation)
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .map(qhLocation1 -> {
                            City city = new City();
                            city.setCity(qhLocation1.getCity());
                            city.setCode(qhLocation1.getAdcode());
                            city.setProvince(qhLocation1.getProvince());
                            city.setAuto(true);
                            city.setDistrict(qhLocation1.getDistrict());
                            // 这里把定位获取到的city 替换掉citycode为获取天气的code和获取城市的拼写用来获取本地新闻的
                            city = cityDBHelper.completeCity(city);
                            if (city == null || TextUtils.isEmpty(city.getCitySpell()) || TextUtils.isEmpty(city.getCode())) {
//                        city = SharePreferenceUtil.getDefaultCity();
                            }
                            return city;
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(city -> {
                            mHeaderView.setTag(city);
                            tvCityname.setText(city.getCity());
                        });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInputMethod();
    }


    private void hideSoftInputMethod() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mListView.getWindowToken(), 0);
    }

    public void initToolBar() {
        toolbar.setTitle("选择城市");
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }


    class QuaryTask extends AsyncTask<String, Void, List<City>> {


        @Override
        protected List<City> doInBackground(String... params) {
            String param = params[0];
            List<City> cities;
            if (param.length() > 0) {
                cities = mCityDBHelper.queryCities(param);
                mList = cities;
            } else {
                cities = mCityDBHelper.getAllCities();
                mList = cities;
            }
            return cities;
        }

        @Override
        protected void onPostExecute(List<City> cities) {
            super.onPostExecute(cities);
            mListView.setAdapter(null);
            if (isSearchMode) {
                mListView.removeHeaderView(mHeaderView);
                mListView.hideIndexScroller();
            } else {
                mListView.addHeaderView(mHeaderView);
                mListView.showIndexScroller();
            }
            mCityListAdapter = new CityListAdapter(getApplicationContext(), mList, mListView.getHeaderViewsCount());
            mCityListAdapter.setSearchMode(isSearchMode);
            mListView.setAdapter(mCityListAdapter);
        }
    }

}
