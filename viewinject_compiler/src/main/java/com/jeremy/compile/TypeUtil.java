package com.jeremy.compile;

import com.squareup.javapoet.ClassName;

/**
 * Created by JokAr on 16/8/8.
 */
public class TypeUtil {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName INJET = ClassName.get("com.jeremy.inject", "Inject");
    public static final ClassName PROVIDER = ClassName.get("com.jeremy.inject","Provider");
}
