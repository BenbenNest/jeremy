/**
 * 
 */

package com.jeremy.lychee.adapter.news;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jeremy.lychee.model.news.Suggest;


/**
 * @author xiaozhongzhong
 */
public class SearchSuggestAdapter extends BaseAdapter {

    private Suggest mSuggest;
    private OnGotoClickListener mGotoClickListener;

    public SearchSuggestAdapter(Suggest suggest) {
        mSuggest = suggest;
    }

    @Override
    public int getCount() {
        return mSuggest == null ? 0 : mSuggest.getS().length;
    }

    @Override
    public Object getItem(int position) {
        return mSuggest == null ? null : mSuggest.getS()[position];
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
            convertView = View.inflate(parent.getContext(), com.jeremy.lychee.R.layout.row_search_suggest, null);
            holder.text = (TextView) convertView.findViewById(com.jeremy.lychee.R.id.suggest_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(mSuggest.getS()[position]);
        return convertView;
    }

    private class ViewHolder {
        TextView text;
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mGotoClickListener != null) {
                mGotoClickListener.onGoto((String) v.getTag());
            }
        }

    };

    /**
     * @author xiaozhongzhong
     */
    public interface OnGotoClickListener {

        void onGoto(String word);

    }

    public OnGotoClickListener getGotoClickListener() {
        return mGotoClickListener;
    }

    public void setGotoClickedListener(OnGotoClickListener clickListener) {
        mGotoClickListener = clickListener;
    }

    public Suggest getSuggest() {
        return mSuggest;
    }

    public void setSuggest(Suggest suggest) {
        this.mSuggest = suggest;
        notifyDataSetChanged();
    }
}
