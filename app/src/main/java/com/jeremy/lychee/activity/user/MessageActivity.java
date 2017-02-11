package com.jeremy.lychee.activity.user;


import android.support.v7.widget.Toolbar;

import com.jeremy.lychee.R;
import com.jeremy.lychee.fragment.user.UserMessageFragment;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends SlidingActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.my_message));
        toolbar.setNavigationOnClickListener(v -> finish());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, new UserMessageFragment())
                .commit();
    }


}
