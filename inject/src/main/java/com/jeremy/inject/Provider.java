package com.jeremy.inject;

import android.content.Context;
import android.view.View;

public interface Provider {
    Context getContext(Object object);

    View findView(Object object, int id);
}
