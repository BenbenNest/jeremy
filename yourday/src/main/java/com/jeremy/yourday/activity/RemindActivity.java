package com.jeremy.yourday.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jeremy.yourday.R;
import com.jeremy.yourday.recycler_view.CommonRecyclerView;
import com.jeremy.yourday.util.DateUtils;
import com.jeremy.yourday.util.PrefUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemindActivity extends AppCompatActivity {

    HashMap<String, String> maps = new HashMap<>();
    ArrayList<String> listKeys = new ArrayList<>();
    ArrayList<String> listValues = new ArrayList<>();
    RemindAdapter mAdapter;
    @BindView(R.id.common_recyclerview)
    CommonRecyclerView commonRecyclerview;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RemindActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        ButterKnife.bind(this);
        initData();
        init();
    }

    private void init() {
        mAdapter = new RemindAdapter();
        commonRecyclerview.setAdapter(mAdapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    private void initData() {
        String[] arr = getResources().getStringArray(R.array.remind_items_key);
        for (String s : arr) {
            String date = DateUtils.getDate(new Date().getDate());
            s = date + "_" + s;
            listKeys.add(s);
        }
        arr = getResources().getStringArray(R.array.remind_items_value);
        for (String s : arr) {
            listValues.add(s);
        }
    }

    class RemindAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RemindActivity.this).inflate(R.layout.list_item_remind, null, false);
            return new RemindViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            RemindViewHolder viewHolder = (RemindViewHolder) holder;
            viewHolder.tvTitle.setText(listValues.get(position));
            if (PrefUtils.getBoolData(listKeys.get(position), false)) {
                viewHolder.ckState.setChecked(true);
            } else {
                viewHolder.ckState.setChecked(false);
            }

            viewHolder.ckState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PrefUtils.saveBoolData(listKeys.get(position), isChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listValues.size();
        }
    }

    public static class RemindViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ck_state)
        CheckBox ckState;

        public RemindViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
