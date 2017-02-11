package com.jeremy.lychee.activity.live;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;


public class LiveRecordPrepareActivity extends SlidingActivity {


    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_live_record_prepare);
    }


    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        findViewById(com.jeremy.lychee.R.id.start_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LiveRecordingActivity.class);
                startActivity(intent);
            }
        });

        initToolbar();

        ImageView cover = (ImageView) findViewById(com.jeremy.lychee.R.id.cover);
        Glide.with(getApplicationContext())
                .load(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + com.jeremy.lychee.R.drawable.topic_head_backimage))
                .placeholder(com.jeremy.lychee.R.drawable.default_avatar)
                .bitmapTransform(new GlideCircleTransform(getApplicationContext()))
                .into(cover);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(com.jeremy.lychee.R.id.toolbar);
        toolbar.setTitle(com.jeremy.lychee.R.string.user_comments);
        toolbar.setNavigationIcon(com.jeremy.lychee.R.drawable.ic_actionbar_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
