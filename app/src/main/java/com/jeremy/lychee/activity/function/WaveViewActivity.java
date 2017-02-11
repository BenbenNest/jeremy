package com.jeremy.lychee.activity.function;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jeremy.lychee.R;
import com.jeremy.lychee.widget.WaveView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by benbennest on 16/7/28.
 */
public class WaveViewActivity extends Activity {

    @Bind(R.id.wave_view)
    WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waveview);
        ButterKnife.bind(this);
        waveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                waveView.invalidate();
//                waveView.setLeft(100);
//                waveView.setRight(waveView.getRight() + 100);
//                ToastHelper.getInstance(WaveViewActivity.this).toast("test");
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) waveView.getLayoutParams();
//                params.width -= 50;
//                params.height -= 50;
//                waveView.setLayoutParams(params);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
