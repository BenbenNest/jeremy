package com.jeremy.lychee.adapter.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.jeremy.lychee.fragment.news.ContentFragment;
import com.jeremy.lychee.db.NewsChannel;

import java.util.HashMap;
import java.util.List;

public class ContentAdapter extends FragmentStatePagerAdapter {


    private List<NewsChannel> mChannels;
//    private HashMap<String, ChannelFragment> fragments = new HashMap<>();
    private HashMap<Integer, ContentFragment> mPageReferenceMap = new HashMap<>();
//    private SceneChannel sceneChannel;


    public ContentAdapter(FragmentManager fm, List<NewsChannel> channels) {
        super(fm);
        this.mChannels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        NewsChannel newsChannel = mChannels.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("cname", newsChannel.getCname());
        bundle.putString("cid", newsChannel.getCid());
        ContentFragment channelFragment = new ContentFragment();
        channelFragment.setArguments(bundle);
        mPageReferenceMap.put(position, channelFragment);
        return channelFragment;
    }

    @Override
    public int getCount() {
        if (mChannels != null) {
            return mChannels.size();
        } else {
            return 0;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void setChannels(List<NewsChannel> channelList) {
        this.mChannels = channelList;
        mPageReferenceMap.clear();
        notifyDataSetChanged();
    }

    public ContentFragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getCname();
    }
}
