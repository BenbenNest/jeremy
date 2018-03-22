package com.jeremy.demo.skin;

import android.util.AttributeSet;
import android.view.View;

import com.jeremy.demo.skin.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changqing on 2018/3/21.
 */

public class SkinAttribute {

    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    List<SkinView> mSkinViews = new ArrayList<>();

    public void load(View view, AttributeSet attrs) {
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获得属性名
            String attributeName = attrs.getAttributeName(i);
            //是否有符合需要换肤的属性
            if (attributeName.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                //写死颜色值，不参与换肤
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    resId = Integer.parseInt(attributeValue.substring(1));//去掉@，然后取int
                }
                if (resId != 0) {
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }
            }
        }
        if (!skinPairs.isEmpty()) {
            SkinView skinView = new SkinView(view, skinPairs);
            mSkinViews.add(skinView);
        }
    }

    static class SkinView {
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }
    }

    static class SkinPair {
        String attributeName;
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }

}
