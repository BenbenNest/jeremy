package com.jeremy.demo.fragmentwithback;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by changqing.zhao on 2017/4/24.
 */

public class FragmentWithBackTest extends BackHandledFragment {

    private boolean hadIntercept;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText("this is a BackHandledFragment");
        textView.setTextColor(Color.BLUE);
        return textView;
//        return getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_a, null);
    }

    @Override
    protected boolean onBackPressed() {
        if (hadIntercept) {
            return false;
        } else {
            Toast.makeText(getActivity(), "Click From MyFragment", Toast.LENGTH_SHORT).show();
            hadIntercept = true;
            return true;
        }
    }
}
