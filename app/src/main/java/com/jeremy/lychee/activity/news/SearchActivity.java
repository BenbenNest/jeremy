/**
 *
 */

package com.jeremy.lychee.activity.news;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremy.lychee.model.news.Suggest;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.HotKeyWordsAdapter;
import com.jeremy.lychee.adapter.news.SearchSuggestAdapter;
import com.jeremy.lychee.model.user.HotNews;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends SlidingActivity {
    public static final String TAG_SEARCH = "tag_search";
    public static final String TAG_URL = "url";
    public static final String SEARCH_HISTORY = "search_history";

    private GridView mHots;
    private View mHotArea;
    private ListView mSuggest;
    private WebView mWebView;
    private static final int HOT_NEWS_SIZE = 6;
    private static final String SEARCH_API = "http://m.haosou.com/s?src=discovery&mso_app=1&offset=0&q=%s";
    private ProgressBar mWebLoadingProgressBar;
    private View mWebNetError;
    private boolean mLoadError;
    private EditText mSearchView;
    private LinkedHashMap<String, String> history = new LinkedHashMap<String, String>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 5;
        }
    };
    private View clearHistory;
    private ListView mSearchHistory;
    private ArrayList<Map<String, Object>> historyListData;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        showHistory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveHistory();
        AppUtil.hideSoft(this);
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.clearCache(true);
            mWebView.destroyDrawingCache();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    private void switchContent(@Mode int mode) {
        mWebLoadingProgressBar.setVisibility(View.GONE);
        if (mWebNetError.getVisibility() == View.VISIBLE) {
            mWebNetError.setVisibility(View.GONE);
        }
        switch (mode) {
            case Mode.HOTNEWS:
                mHotArea.setVisibility(View.VISIBLE);
                mSuggest.setVisibility(View.GONE);
                setWebViewVisibility(View.GONE);
                break;
            case Mode.SUGGEST:
                mHotArea.setVisibility(View.GONE);
                mSuggest.setVisibility(View.VISIBLE);
                setWebViewVisibility(View.GONE);
                break;
            case Mode.RESULT:
                mHotArea.setVisibility(View.GONE);
                mSuggest.setVisibility(View.GONE);
                setWebViewVisibility(View.VISIBLE);
                break;

            default:
                break;
        }

    }

    private void setWebViewVisibility(int visibility) {
        if (mWebView != null) {
            mWebView.setVisibility(visibility);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        clearHistory = findViewById(R.id.clear);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("搜索");
        toolbar.setNavigationOnClickListener(v -> {
            AppUtil.hideSoft(SearchActivity.this);
            finish();
        });
        final MenuItem searchItem = toolbar.getMenu().findItem(R.id.menu_item_action_search);
        mSearchView = (EditText) findViewById(R.id.searchView);
        mSearchView.clearFocus();

        // Get the search close button image view
        final ImageView closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String wordToSearch = s.toString().trim();

                if (closeButton != null) {
                    if (TextUtils.isEmpty(wordToSearch)) {
                        closeButton.setVisibility(View.INVISIBLE);
                    } else {
                        closeButton.setVisibility(View.VISIBLE);
                    }
                }
                if (mSearchView.getTag() != null) {
                    mSearchView.setTag(null);
                    if (!TextUtils.isEmpty(wordToSearch)) {
                        return;
                    }
                }
                if (TextUtils.isEmpty(wordToSearch)) {
                    switchContent(Mode.HOTNEWS);
                } else {
                    Observable.just(wordToSearch)
                            .flatMap(word -> Observable.just(getSuggest(wordToSearch)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(suggest -> {
                                String text = mSearchView.getText().toString();
                                if (TextUtils.isEmpty(text)) {
                                    return;
                                }
                                if (suggest != null) {
                                    String[] keys = suggest.getS();
                                    String q = suggest.getQ();
                                    if (keys != null && keys.length > 0 && q != null && q.equals(text)) {
                                        ((SearchSuggestAdapter) mSuggest.getAdapter()).setSuggest(suggest);
                                        switchContent(Mode.SUGGEST);
                                    }
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                final String text = mSearchView.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, R.string.search_empty, Toast.LENGTH_SHORT).show();
                    return false;
                }
                AppUtil.hideSoft(SearchActivity.this);
                showWebViewResult(text, null);
                //热搜打点
                HitLog.hitLogSearch(text, HitLog.ACTION_SEARCH_INPUT_HISTORY);
                return true;
            }
            return false;
        });

        mWebView = (WebView) findViewById(R.id.search_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());


        //http://code.google.com/p/android/issues/detail?id=20446
        if (android.os.Build.VERSION.SDK_INT < 16) {
            mWebView.setBackgroundColor(0x00000000);
        } else {
            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }

        mHots = (GridView) findViewById(R.id.search_hot);
        mHotArea = findViewById(R.id.layout_search_hot);
        mSuggest = (ListView) findViewById(R.id.search_suggest);

        mHots.setOnItemClickListener(mOnItemClickListener);
        mSuggest.setOnItemClickListener(mOnItemClickListener);
        mWebLoadingProgressBar = (ProgressBar) findViewById(R.id.web_load_progress);
        mWebNetError = findViewById(R.id.web_net_error);
        SearchSuggestAdapter suggestAdapter = new SearchSuggestAdapter(null);
        suggestAdapter.setGotoClickedListener(word -> {
            if (TextUtils.isEmpty(word)) {
                return;
            }
            mSearchView.setText(word);
        });
        mSuggest.setAdapter(suggestAdapter);
        mSuggest.setDivider(new InsetDrawable(new ColorDrawable(Color.parseColor("#d9d9d9")), dp2px(20), 0, dp2px(20), 0));
        mSuggest.setDividerHeight(1);

        mSearchView.setOnClickListener(v -> mLoadError = false);
        refreshHotNews();
        switchContent(Mode.HOTNEWS);
        // 获取传递来的数据 搜索
        Intent intent = getIntent();
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra(TAG_SEARCH))) {
            String data = intent.getStringExtra(TAG_SEARCH);
            showWebViewResult(data, intent.getStringExtra(TAG_URL));
        } else {
//            new Handler().postDelayed(() -> showSoftInputMethod(mSearchView), 300);
            if (closeButton != null) {
                closeButton.setVisibility(View.INVISIBLE);
            }
        }

        clearHistory.setOnClickListener(v -> {
            clearHistory.setVisibility(View.GONE);
            history.clear();
            historyListData.clear();
            SimpleAdapter adapter = (SimpleAdapter) mSearchHistory.getAdapter();
            adapter.notifyDataSetChanged();
            adjustListViewHeight(mSearchHistory);
        });
    }

    private void showHistory() {
        if (history == null || history.size() == 0) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> history = new Gson().fromJson(new NewsChannelPreference().getStringValue(SEARCH_HISTORY), type);
            if (history == null || history.size() == 0) {
                clearHistory.setVisibility(View.GONE);
                return;
            }
            this.history.putAll(history);
        }
        clearHistory.setVisibility(View.VISIBLE);
        mSearchHistory = (ListView) findViewById(R.id.history_list_view);
        mSearchHistory.setDivider(new InsetDrawable(new ColorDrawable(Color.parseColor("#d9d9d9")), dp2px(20), 0, dp2px(20), 0));
        mSearchHistory.setDividerHeight(1);
        historyListData = new ArrayList<>();
        Set<Map.Entry<String, String>> set = history.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if (entry.getValue().trim().length()==0)
                continue;
            Map<String, Object> map = new HashMap<>();
            map.put("content", entry.getValue());
            map.put("ic_close", R.drawable.ic_search_delete);
            historyListData.add(map);
        }
        Collections.reverse(historyListData);
        SimpleAdapter adapter = new SimpleAdapter(this,
                historyListData,
                R.layout.row_search_history,
                new String[]{"content", "ic_close"},
                new int[]{R.id.tv_content, R.id.iv_clear}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.findViewById(R.id.iv_clear).setOnClickListener(v -> {
                    history.remove(((Map<String, Object>) getItem(position)).get("content").toString());
                    historyListData.remove(position);
                    notifyDataSetChanged();
                    adjustListViewHeight(mSearchHistory);
                    if (historyListData.size() == 0) {
                        clearHistory.setVisibility(View.GONE);
                    }
                });
                return view;
            }
        };
        mSearchHistory.setAdapter(adapter);
        adjustListViewHeight(mSearchHistory);
        mSearchHistory.setOnItemClickListener((parent, view, position, id) -> {
                    String content = (String) historyListData.get(position).get("content");
                    showWebViewResult(content, null);
                    //热搜打点
                    HitLog.hitLogSearch(content, HitLog.ACTION_SEARCH_INPUT_HISTORY);
                }
        );
    }

    private void saveHistory() {
        new NewsChannelPreference().saveStringValue(SEARCH_HISTORY, new Gson().toJson(history, Map.class));
    }

    public static void adjustListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void adjustGridViewHeight(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, c = listAdapter.getCount(), numColumns = listView.getNumColumns(); i < c; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            if (i % numColumns == 0) {
                totalHeight += listItem.getMeasuredHeight() + 1/*divider*/;
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight - 1;
        listView.setLayoutParams(params);
    }

    private void refreshHotNews() {
        String sign = "BC076E39784D7B27";
        OldRetroAdapter.getService().getHotWords("http://sh.qihoo.com/api/hot_new.json", "day0", sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HotNews>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<HotNews> hotNewses) {
                        if (hotNewses != null && hotNewses.size() > 0) {
                            if (hotNewses.size() > 6) {
                                hotNewses = hotNewses.subList(0, 6);
                            }
                            mHots.setAdapter(new HotKeyWordsAdapter(getApplicationContext(), hotNewses));
                            adjustGridViewHeight(mHots);
                        }
                    }
                });
    }

//    private void showSoftInputMethod(View view) {
//        InputMethodManager imm =
//                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_IMPLICIT_ONLY);
//    }

    private void showWebViewResult(String text, String jumpUrl) {
        history.remove(text);
        history.put(text, text);
        showHistory();
        switchContent(Mode.RESULT);
        setSearchContentSilence(text);
        String url = jumpUrl;
        if (TextUtils.isEmpty(url)) {
            url = String.format(SEARCH_API, text);
        }
        mWebView.loadUrl(url);
    }


    private List<HotNews> getShowingHotNews(final List<HotNews> hotNews) {
        if (hotNews == null || hotNews.size() <= 0) {
            return null;
        }

        ArrayList<HotNews> showingHotNews = new ArrayList<>(HOT_NEWS_SIZE);
        for (int i = 0; i < HOT_NEWS_SIZE; i++) {
            HotNews hotNew = null;
            int c = hotNews.size();
            if (c > 0) {
                int r = (int) (Math.random() * c);
                hotNew = hotNews.remove(r);
                showingHotNews.add(hotNew);
            }
        }
        return showingHotNews;

    }

    private void setSearchContentSilence(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mSearchView.setTag(true);
        mSearchView.setText(text);
        mSearchView.setSelection(text.length());
    }


    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String word = "";
            if (parent instanceof GridView) {
                word = ((HotNews) mHots.getAdapter().getItem(position)).getKeyword();
            } else {
                word = (String) mSuggest.getAdapter().getItem(position);
            }
            showWebViewResult(word, null);
            //热搜打点
            HitLog.hitLogSearch(word, HitLog.ACTION_SEARCH_HOTWORD);
        }
    };

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mWebNetError.getVisibility() == View.VISIBLE && !mLoadError) {
                mWebNetError.setVisibility(View.GONE);
            }
            AppUtil.hideSoft(SearchActivity.this);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            AppUtil.hideSoft(SearchActivity.this);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            openActivity(WebViewActivity.class, bundle, 0);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mLoadError = true;
            view.stopLoading();
            view.clearView();
            view.loadUrl("about:blank");
            if (mWebNetError.getVisibility() == View.GONE) {
                mWebNetError.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mWebLoadingProgressBar.setProgress(newProgress);
            if (newProgress >= 100) {
                mWebLoadingProgressBar.setVisibility(View.GONE);
            } else {
                mWebLoadingProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @IntDef({Mode.HOTNEWS, Mode.SUGGEST, Mode.RESULT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int HOTNEWS  = 0;
        int SUGGEST  = 1;
        int RESULT   = 2;
    }

    private int dp2px(float dp) {
        return (int) (dp * getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private Suggest getSuggest(String word) {
        if (word == null || "".equals(word)) {
            return null;
        }
        try {
            String url = "http://sug.so.360.cn/suggest/word?encodein=utf-8&encodeout=utf-8&word=" + URLEncoder.encode(word, "utf-8");
            Request request = new Request.Builder().url(url).build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                int start = string.indexOf("(");
                int end = string.indexOf(")");
                string = string.substring(start + 1, end);
                Gson gson = new Gson();
                Type type = new TypeToken<Suggest>() {
                }.getType();
                Suggest bean = gson.fromJson(string, type);
                if (bean != null) {
                    return bean;
                }
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
