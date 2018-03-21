package com.jeremy.demo.skin;

import android.util.AttributeSet;
import android.view.View;

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

    public void load(View view, AttributeSet attrs) {
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
                if (attributeValue.startsWith("?")) {

                } else {
                    int resId = Integer.parseInt(attributeValue.substring(1));//去掉@，然后取int
                }
            }
        }
    }

}
