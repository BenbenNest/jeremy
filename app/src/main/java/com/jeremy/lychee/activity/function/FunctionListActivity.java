package com.jeremy.lychee.activity.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.function.FunctionListAdapter;
import com.jeremy.lychee.bean.FunctionListData;
import com.jeremy.lychee.core.BaseToolbarActivity;
import com.jeremy.lychee.db.CityDBHelper;
import com.jeremy.lychee.model.function.impl.FunctionListModel;
import com.jeremy.lychee.model.news.City;
import com.jeremy.lychee.presenter.function.FunctionListPresenter;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.view.function.FunctionListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by benbennest on 16/4/22.
 */
public class FunctionListActivity extends BaseToolbarActivity implements FunctionListView {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    private FunctionListPresenter presenter;
    private FunctionListAdapter mAdpater;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FunctionListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_function_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        presenter = new FunctionListPresenter();
        presenter.attachView(this);
        presenter.getData();
//        handleCity();
    }

    @Override
    protected void onDestroy() {
        this.presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onFailure(Throwable e) {

    }

    @Override
    public void onGetDataSuccess(SparseArray<FunctionListData> list) {
//        EasyBorderDividerItemDecoration dataDecoration = new EasyBorderDividerItemDecoration(
//                this.getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
//                this.getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans)
//        );
//
//        this.recyclerview.addItemDecoration(dataDecoration);
//        mAdpater = new FunctionListAdapter(list);
//        Observable.just(mAdpater).subscribe(l -> recyclerview.setAdapter(mAdpater));

//        recyclerview.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        FunctionListAdapter adapter = new FunctionListAdapter(this, new FunctionListModel());
//        Observable.just(mFunctionPresenter.getFunctionList())
//                .map(list -> new FunctionAdapter(list))
//                .subscribe(list -> recylerview.setAdapter(list));
        recyclerview.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTitle("程序案例");
    }

    class model {

    }

    HashMap<String, JSONObject> maps = new HashMap<String, JSONObject>();

    public void handleCity() {
        CityDBHelper mCityDBHelper = new CityDBHelper(this);

        List<City> list = mCityDBHelper.getAllCities();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals("101090102")) {
                ToastHelper.getInstance(this).toast(list.get(i).getCity());
            }
        }

//        String str = FileUtil.loadAssertFile(this, "city.json");
//        JSONArray citys = new JSONArray();
//        try {
//            String city = "";
//            String code = "";
//            JSONArray array = new JSONArray(str);
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject obj = new JSONObject();
//                city = array.getJSONArray(i).get(0).toString();
//                code = array.getJSONArray(i).get(1).toString();
//                obj.put(city, code);
//                maps.put(city, obj);
////                citys.put(obj);
//                for (int j = 0; j < array.getJSONArray(i).getJSONArray(2).length(); j++) {
//                    city = array.getJSONArray(i).getJSONArray(2).getJSONArray(j).get(0).toString();
//                    code = array.getJSONArray(i).getJSONArray(2).getJSONArray(j).get(1).toString();
//                    obj = new JSONObject();
//                    obj.put(city, code);
//                    maps.put(city, obj);
////                    citys.put(obj);
//                    for (int m = 0; m < array.getJSONArray(i).getJSONArray(2).getJSONArray(j).getJSONArray(2).length(); m++) {
//                        city = array.getJSONArray(i).getJSONArray(2).getJSONArray(j).getJSONArray(2).getJSONArray(m).get(0).toString();
//                        code = array.getJSONArray(i).getJSONArray(2).getJSONArray(j).getJSONArray(2).getJSONArray(m).get(1).toString();
//                        obj = new JSONObject();
//                        obj.put(city, code);
//                        maps.put(city, obj);
////                        citys.put(obj);
//                    }
//                }
//            }
//            Iterator<Map.Entry<String, JSONObject>> iterator = maps.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) iterator.next();
//                JSONObject object = entry.getValue();
//                citys.put(object);
//            }
//            if (citys != null && citys.length() > 0) {
//                FileUtil.saveObject2File(citys.toString(), "city.json");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

//    　Map map = new HashMap();
//    　　Iterator iter = map.entrySet().iterator();
//    　　while (iter.hasNext()) {
//        　　Map.Entry entry = (Map.Entry) iter.next();
//        　　Object key = entry.getKey();
//        　　Object val = entry.getValue();
//        　　}


}
