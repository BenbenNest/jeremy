/**
 * 
 */

package com.jeremy.lychee.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jeremy.lychee.model.user.HotNews;


/**
 * @author xiaozhongzhong
 */
public class HotKeyWordsAdapter extends BaseAdapter {
    private Context mContext;
    private List<HotNews> mHotNews;

    public HotKeyWordsAdapter(Context context, List<HotNews> hotNews) {
        super();
        mContext = context;
        mHotNews = hotNews;
    }

    @Override
    public int getCount() {
        return (mHotNews == null) ? 0 : mHotNews.size();
    }

    @Override
    public Object getItem(int position) {
        return (mHotNews == null || mHotNews.size() <= position) ? null : mHotNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(com.jeremy.lychee.R.layout.item_hotnews, null);
            holder.title = (TextView) convertView.findViewById(com.jeremy.lychee.R.id.hotnews_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(mHotNews.get(position).getTitle());
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
    }
}
