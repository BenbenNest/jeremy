package com.jeremy.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.demo.R;
import com.jeremy.demo.algorithm.LinkListActivity;
import com.jeremy.demo.mvp.bean.FunctionData;
import com.jeremy.demo.mvp.presenter.FunctionListPresenter;
import com.jeremy.demo.mvp.view.FunctionListView;
import com.jeremy.library.recycler_view.CommonRecyclerView;
import com.jeremy.library.util.DateUtil;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements FunctionListView {
    @BindView(R.id.common_recyclerview)
    CommonRecyclerView commonRecyclerView;

    private FunctionListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        test();
        init();
    }

    private void test() {
        LinkListActivity.test();

//        int[] arr = new int[]{12, 8, 98, 23, 34, 9, 4, 56, 37};
//        NumAlgorithmActivity.quickSort(arr, 0, arr.length - 1);

//        String result = stringZip("aaabbbbccdddefff");
//        String result = stringFilter("dbadbasdfsadf");
//        result = StringAlgorithmActivity.stringReverseRecurse("asdf");
//        String result = StringAlgorithmActivity.getLongestPalinDrome("awaabcbafhd");
//        List<String> list = StringAlgorithmActivity.getLongestNoRepeatSubString("dsafotioiuadfjklweljk");
//        List<String> list = StringAlgorithmActivity.getLongestNoRepeatSubString("abcad");
    }

    public static String stringFilter(String s) {
        if (TextUtils.isEmpty(s)) return "";

        StringBuffer sb = new StringBuffer();
        int[] arr = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int num = ch - 'a';
            if (arr[num] == 0) {
                sb.append(ch);
                arr[num] = 1;
            }
        }
        //由于Set默认会自动排序，因此下面的代码不是按先后顺序，而是按字母排序的，所以不能选择用Set
//        TreeSet<Character> set = new TreeSet<>();
//        for (int i = 0; i < s.length(); i++) {
//            set.add(s.charAt(i));
//        }
//        Iterator<Character> it = set.iterator();
//        while (it.hasNext()) {
//            char ch = it.next();
//            sb.append(ch);
//        }
//        for (Character ch : set) {
//            sb.append(ch);
//        }
        return sb.toString();
    }


    private void init() {
        commonRecyclerView.disableRefresh();
        presenter = new FunctionListPresenter();
        presenter.attachView(this);
        presenter.getData();
    }

    @Override
    public void onGetDataSuccess(ArrayList<FunctionData> list) {
        FunctionListAdapter adapter = new FunctionListAdapter(HomeActivity.this, list);
        commonRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Throwable e) {

    }


    class FunctionListAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private ArrayList<FunctionData> list;
        private List<String> images;

        private void getImages() {
            try {
                InputStream inputStream = mContext.getResources().getAssets().open("cache/NetImageUrl.txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                images = (List<String>) objectInputStream.readObject();
                images.size();
                objectInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public FunctionListAdapter(Context context, ArrayList<FunctionData> list) {
            mContext = context;
            this.list = list;
            getImages();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_function, null);
            return new FunctionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            FunctionViewHolder holder = (FunctionViewHolder) viewHolder;
            FunctionData data = list.get(i);

            holder.tvTitle.setText(data.getTitle());
            holder.tvDes.setText(data.getDes());
            holder.tvDate.setText(DateUtil.getSimpleDate(new Date().getTime()));
            String url = "";
            if (images != null && images.size() > 0) {
                Random random = new Random();
                int pos = random.nextInt(images.size() - 1);
                url = images.get(pos);
            }
//            if (!TextUtils.isEmpty(url)) {
//                GlideUtils.display(holder.ivIcon, url);
//            } else {
//                GlideUtils.displayNative(holder.ivIcon, R.mipmap.img_default_gray);
//            }
            setOnclickListener(holder, data);
        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        private void setOnclickListener(FunctionViewHolder holder, final FunctionData data) {
            holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mContext.startActivity(new Intent(mContext, Class.forName(data.getUrl())));
                    } catch (Exception e) {

                    }
                }
            });
        }

        class FunctionViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_icon)
            ImageView ivIcon;
            @BindView(R.id.tv_title)
            TextView tvTitle;
            @BindView(R.id.tv_date)
            TextView tvDate;
            @BindView(R.id.tv_des)
            TextView tvDes;
            @BindView(R.id.item_root)
            CardView itemRoot;

            public FunctionViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }


    }
}
