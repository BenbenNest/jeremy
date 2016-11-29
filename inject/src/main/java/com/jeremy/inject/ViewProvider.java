package com.jeremy.inject;

import android.content.Context;
import android.view.View;

public class ViewProvider implements Provider {
    @Override
    public Context getContext(Object object) {
        return ((View) object).getContext();
    }

    @Override
    public View findView(Object object, int id) {
        return ((View) object).findViewById(id);
    }
}
