package com.jeremy.library.recycler_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jeremy.library.R;

/**
 * Created by changqing.zhao on 2017/5/5.
 */

public class RecyclerViewFooter extends LinearLayout {

    public RecyclerViewFooter(Context context) {
        super(context);
        init();
    }

    public RecyclerViewFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerViewFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_view_footer, this);

    }

}
