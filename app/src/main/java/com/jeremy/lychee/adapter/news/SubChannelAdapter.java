package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.model.news.ChannelCategory;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.R;
import com.jeremy.lychee.manager.FontManager;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubChannelAdapter extends BaseAdapter {

    private Context context;
    private List<ChannelCategory.ChannelInfo.SubChannel> subChannelList = new ArrayList<>();
//    private static Typeface typeface = Typeface.createFromAsset(ContentApplication.getInstance().getApplicationContext().getAssets(),
//            "fonts/fzktjt.ttf");;
    private static String[] cidArray;

    public SubChannelAdapter(Context context, List<ChannelCategory.ChannelInfo.SubChannel> subChannelList) {
        this.context = context;
        this.subChannelList = subChannelList;
        getCidArray();
    }

    public void getCidArray() {
        if (cidArray == null || cidArray.length == 0) {
            String cids = new NewsChannelPreference().getNewsChannelCids();
            cidArray = cids.split("\\|");
        }
    }

    @Override
    public int getCount() {
        if (subChannelList == null) {
            return 0;
        } else {
            return subChannelList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.sub_channel_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ChannelCategory.ChannelInfo.SubChannel subChannel = subChannelList.get(i);
        boolean flag = false;
        for (String cid : cidArray) {
            if (cid.equals(subChannel.getCid())) {
                flag = true;
                break;
            }
        }
        viewHolder.orderedTxt.setVisibility(flag ? View.VISIBLE : View.GONE);
        Glide.with(context).load(ImageOptiUrl.get(subChannel.getIcon(), viewHolder.imageView)).into(viewHolder.imageView);
        viewHolder.textView.setTypeface(FontManager.getKtTypeface());
        viewHolder.textView.setText(subChannel.getCname());
        return view;
    }

    public static void setCidArray(String[] cidArray) {
        SubChannelAdapter.cidArray = cidArray;
    }

    public List<ChannelCategory.ChannelInfo.SubChannel> getSubChannelList() {
        return subChannelList;
    }

    public void setSubChannelList(List<ChannelCategory.ChannelInfo.SubChannel> subChannelList) {
        this.subChannelList = subChannelList;
    }

    public static class ViewHolder {
        @Bind(R.id.channel_icon)
        public GlideImageView imageView;
        @Bind(R.id.channel_name_txt)
        public TextView textView;
        @Bind(R.id.ordered_txt)
        TextView orderedTxt;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
