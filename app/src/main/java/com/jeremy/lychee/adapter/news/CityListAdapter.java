package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.jeremy.lychee.R;
import com.jeremy.lychee.model.news.City;

import java.util.Arrays;
import java.util.List;

public class CityListAdapter extends ArrayAdapter<City> implements SectionIndexer {
    public static final int SHARP_KEY_POSITION = -1; //#
    public static final int NOT_SET = -100;

    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private List<City> mList;

    private int[] mKeyPositions;
    private boolean isSearchMode;
    private int mHeaderCount;
    private int mPriviousPosition;

    public CityListAdapter(Context context, List<City> list, int headerCount) {
        super(context, R.layout.item_city, R.id.tv_city_name, list);
        this.mList = list;
        this.mHeaderCount = headerCount;


        mKeyPositions = new int[mSections.length()];
        Arrays.fill(mKeyPositions, NOT_SET);

        mKeyPositions[0] = SHARP_KEY_POSITION;
        mKeyPositions[1] = 0;
        char finded = 'a';
        for (int i = 0; i < list.size(); i++) {
            char cityFirstChar = list.get(i).getCitySpell().charAt(0);

            if (finded == cityFirstChar) {
                continue;
            }
            int sectionIndex = findSectionIndex(cityFirstChar);
            if (sectionIndex !=NOT_SET) {
                mKeyPositions[sectionIndex] = i;
                finded = cityFirstChar;
            } /*else {
                mKeyPositions[sectionIndex] = 0;
            }*/
        }


    }


    private int findSectionIndex(char c) {
        for (int i = 0; i < mSections.length(); i++) {
            if (c == Character.toLowerCase(mSections.charAt(i))) {
                return i;
            }
        }
        return NOT_SET;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_city, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_city_name);
            viewHolder.section = (TextView) convertView.findViewById(R.id.tv_section);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(item.getName());

        viewHolder.section.setVisibility(View.GONE);
        if (!isSearchMode) {
            for (int keyPosition : mKeyPositions) {
                if (position == keyPosition) {
                    viewHolder.section.setVisibility(View.VISIBLE);
                    viewHolder.section.setText(String.valueOf(Character.toUpperCase(item.getCitySpell().charAt(0))));
                    break;
                }
            }
        }


//        showSectionViewIfFirstItem(parentView, item, position);

//        populateDataForRow(parentView, item, position);

        return convertView;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
//        if (sectionIndex < 0 || sectionIndex >= mSections.length()) {
//            return -1;
//        }
        int keyPosition = mKeyPositions[sectionIndex];

        if (keyPosition == SHARP_KEY_POSITION) {
            return 0;
        }
        if (keyPosition == NOT_SET && mPriviousPosition > 0) {
            return mPriviousPosition;
        }
        int position = keyPosition + mHeaderCount;
        mPriviousPosition = position;
        return position;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public void setSearchMode(boolean isSearchMode) {
        this.isSearchMode = isSearchMode;
    }

    class ViewHolder {
        TextView section;
        TextView name;
    }
}
