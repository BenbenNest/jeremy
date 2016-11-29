package com.jeremy.library.injector;

import android.content.Context;
import android.view.View;

/**
 * Created by JokAr on 16/8/6.
 */
public interface Provider {
    Context getContext(Object object);

    View findView(Object object, int id);
}
