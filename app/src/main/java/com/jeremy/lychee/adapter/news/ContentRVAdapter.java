package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.widget.recyclerview.BaseRVAdapter;
import com.jeremy.lychee.R;
import com.jeremy.lychee.channel.QHState;
import com.qihoo.sdk.report.QHStatAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentRVAdapter extends BaseRVAdapter {

    public static final int SEARCH_TYPE = 1;
    public static final int FOCUS_NEWS_TYPE = 2;
    public static final int NORMAL_NEWS_TYPE = 3;
    public static final int NO_PIC_NEWS_TYPE = 4;
    public static final int IMAGE_NEWS_TYPE1 = 5;
    public static final int IMAGE_NEWS_TYPE2 = 6;
    public static final int LAST_REFRESH_TAG = 7;
    public static final int EX_FOCUS_NEWS_TAG = 8;


    private List<NewsListData> list = new ArrayList<>();
    private Context context;
    private int itemType = 2;
    public boolean hasSearchView = true;
    private ContentRVOnItemClickListener listener;
    private int tagPosition = -1;
    private  Map<Integer, ContentVHTemplate> map = new HashMap<>();

    private View mHeaderView;

    public View getHeaderView() {
        return mHeaderView;
    }

    public ContentRVAdapter(Context context, int itemType, ContentRVOnItemClickListener listener) {
        this.context = context;
        this.itemType = itemType;
        this.listener = listener;
        map.put(SEARCH_TYPE, new SearchVHTemplate());
        map.put(FOCUS_NEWS_TYPE, new FocusVHTemplate());
        map.put(NORMAL_NEWS_TYPE, new NormalNews1VHTemplate());
        map.put(NO_PIC_NEWS_TYPE, new NoPicNews1VHTemplate());
        map.put(IMAGE_NEWS_TYPE1, new ImageNews1VHTemplate());
        map.put(LAST_REFRESH_TAG, new LastRefreshTagVHTemplate());
        map.put(EX_FOCUS_NEWS_TAG, new ExFocusNewsVHTemplate());
        notifyAllTemplate(itemType);
    }

    @Override
    public int getTopBounceRange() {
        return context.getResources().getDimensionPixelSize(R.dimen.news_search_bar_height);
    }

    @Override
    public int getContentItemCount() {
        int count = 0;
        if (hasSearchView) {
            count++;
        }
        if (tagPosition >= 0) {
            count++;
        }
        if (list != null) {
            count += list.size();
        }
        return count;
    }

    @Override
    public int getContentItemViewType(int position) {
        if (position == 0 && hasSearchView) {
            return SEARCH_TYPE;
        }
        if ((position == 0 && getFocusNewsData() != null)
                || position == 1 && hasSearchView && getFocusNewsData() != null) {
            return FOCUS_NEWS_TYPE;
        }
        if (getTagInsideList(position) == tagPosition) {
            return LAST_REFRESH_TAG;
        }
        int listPosition = getListPosition(position);
        if (list == null || listPosition >= list.size()) {
            return NORMAL_NEWS_TYPE;
        }
        NewsListData data = list.get(listPosition);
        String module = data.getModule();
        if ("4".equals(module)|| "5".equals(module)) {
            return IMAGE_NEWS_TYPE1;
//        } else if () {
//            return IMAGE_NEWS_TYPE1;
        } else if ("2".equals(module)) {
            return NO_PIC_NEWS_TYPE;
        } else if ("3".equals(module)) {
            return EX_FOCUS_NEWS_TAG;
        }
        return NORMAL_NEWS_TYPE;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        ContentVHTemplate contentVHTemplate = map.get(contentViewType);
        if (contentVHTemplate != null) {
            RecyclerView.ViewHolder holder = contentVHTemplate.getViewHolder(parent);
            if (contentViewType == SEARCH_TYPE) {
                mHeaderView = holder.itemView;
            }
            return holder;
        } else {
            return null;
        }
    }

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        int type = getContentItemViewType(position);
        int listPosition = getListPosition(position);
        NewsListData data = null;
        if (list != null && listPosition < list.size() && listPosition >= 0) {
            data = list.get(listPosition);
        }
        ContentVHTemplate template;
        template = map.get(type);
        switch (type) {
            case FOCUS_NEWS_TYPE:
            case IMAGE_NEWS_TYPE1:
            case IMAGE_NEWS_TYPE2:
            case NORMAL_NEWS_TYPE:
            case NO_PIC_NEWS_TYPE:
            case EX_FOCUS_NEWS_TAG:
                if (template != null && data != null) {
                    template.onBindContentItemViewHolder(contentViewHolder, data);
                }
                break;
            case SEARCH_TYPE:
                template.onBindContentItemViewHolder(contentViewHolder, null);
                break;
        }
        contentViewHolder.itemView.setOnClickListener(v -> {
            QHStatAgent.onEvent(context, QHState.NEWSCLICK,getContentItemViewType(position)+"",1);
            listener.onItemClick(v, position);
        });
    }

    public void setList(List<NewsListData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void insertList(List<NewsListData> subList) {
        if (list == null) {
            list = new ArrayList<>();
        }
        insertList(list.size(), subList);
    }

    public void insertList(int position, List<NewsListData> subList) {
        if (subList == null || subList.size() == 0) {
            return;
        }
        if (list == null) {
            list = subList;
            notifyDataSetChanged();
            return;
        }
        if (position == 0) {
            NewsListData data = subList.get(0);
            if (!"1".equals(data.getIs_focus()) &&hasStickyFocusNews()) {
                list.addAll(position +1 , subList);
            }else {
                list.addAll(position, subList);
            }
            notifyDataSetChanged();
            return;
        }
        list.addAll(subList);
        if (hasSearchView) {
            position++;
        }
        if (tagPosition >= 0) {
            position++;
        }
        notifyContentItemRangeInserted(position, subList.size());
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
        notifyAllTemplate(itemType);
//        notifyDataSetChanged();
    }

    public void setHasSearchView(boolean hasSearchView) {
        this.hasSearchView = hasSearchView;
    }

    public boolean isHasSearchView() {
        return hasSearchView;
    }

//    public void setFocusNewsData(NewsListData focusNewsData) {
//        this.focusNewsData = focusNewsData;
//        notifyItemChanged(hasSearchView ? 1 : 0);
//    }

    public NewsListData getFocusNewsData() {
        if (list == null || list.size() == 0) {
            return null;
        }
        NewsListData data = list.get(0);
        if ("1".equals(data.getIs_focus())) {
            return data;
        } else {
            return null;
        }
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public List<NewsListData> getList() {
        return list;
    }

    private void notifyAllTemplate(int itemType) {
        for (Map.Entry<Integer, ContentVHTemplate> entry : map.entrySet()) {
            ContentVHTemplate template = entry.getValue();
            template.setItemType(itemType);
        }
    }

    public void setTagPosition(int tagPosition) {
        this.tagPosition = tagPosition;
    }

    public int getTagPosition() {
        return tagPosition;
    }

    public int getListPosition(int position) {
        int listPosition = position;
        if (hasSearchView) {
            listPosition--;
        }
        if (tagPosition >= 0 && listPosition >= tagPosition) {
            listPosition--;
        }
        return listPosition;
    }

    public int getTagInsideList(int position) {
        if (hasSearchView) {
            position--;
        }
        return position;
    }

    @Override
    public boolean canTopBound() {
//        return hasSearchView;
        return super.canTopBound();//取消二段下拉
    }

    public interface ContentRVOnItemClickListener {
        void onItemClick(View view, int position);
    }

    public boolean hasStickyFocusNews() {
        if (list != null && list.size() > 0) {
            NewsListData data = list.get(0);
            if ("1".equals(data.getIs_focus()) && "1".equals(data.getNews_stick())
                    && Long.parseLong(data.getNews_stick_time()) > System.currentTimeMillis() / 1000) {
                return true;
            }
        }
        return false;
    }
}
