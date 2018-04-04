package com.jeremy.demo.activity;

import android.support.v7.app.AppCompatActivity;

import com.jeremy.demo.model.GankList;
import com.jeremy.demo.mvp.view.GankAllView;

import io.reactivex.Observable;

public class GankAllActivity extends AppCompatActivity implements GankAllView {
    @Override
    public void onFailure(Throwable e) {

    }

    @Override
    public void onDataSuccess(Observable<GankList> observable) {

    }

//    @BindView(R.id.list)
//    RecyclerView mRecyclerView;
//    private GankAdapter mAdapter;
//    private RequestManager glide;
//    private GankAllPresenter mPresenter;
//
//    public static void startActivity(Context context) {
//        Intent intent = new Intent(context, GankAllActivity.class);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gank_all);
//        ButterKnife.bind(this);
//        initToolBar();
//        init();
//    }
//
//    private void init() {
//        glide = Glide.with(this);
//        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        CustomDecoration customDecoration = new CustomDecoration(this, CustomDecoration.VERTICAL_LIST);
//        mRecyclerView.addItemDecoration(customDecoration);
//        mAdapter = new GankAdapter();
//        mRecyclerView.setAdapter(mAdapter);
//        mPresenter = new GankAllPresenter();
//        mPresenter.attachView(this);
//        mPresenter.getData();
//    }
//
//    private void initToolBar() {
//        getSupportActionBar().setTitle("干货集中营");
//    }
//
//    @Override
//    public void onFailure(Throwable e) {
//
//    }
//
//    @Override
//    public void onDataSuccess(Observable<GankList> observable) {
//        observable.observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<GankList>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        //Disposable是1.x的Subscription改名的，因为Reactive-Streams规范用这个名称，为了避免重复
//                        //这个回调方法是在2.0之后新添加的
//                        //可以使用d.dispose()方法来取消订阅
//                        //由于1.x中Observable不能合理的背压，导致了无法意料的 MissingBackpressureException，所以在2.x中，添加了Flowable来支持背压，而把Observable设计成非背压的。
//                        //还有一点需要注意的就是，在上边注释中也有，onSubscribe(Disposable d)这个回调方法是在2.x中添加的，Dispose参数是由1.x中的Subscription改名的，为了避免名称冲突！
//                    }
//
//                    @Override
//                    public void onNext(GankList gankList) {
//                        if (mAdapter != null) {
//                            mAdapter.addData(gankList.getResults());
//                            mRecyclerView.setAdapter(mAdapter);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//    class GankAdapter extends RecyclerView.Adapter {
//        private LayoutInflater mLayoutInflater;
//        private List<GankList.ResultsBean> mDataList = new ArrayList<>();
//
//        public void setData(List<GankList.ResultsBean> list) {
//            mDataList.clear();
//            mDataList.addAll(list);
//            notifyDataSetChanged();
//        }
//
//        public void addData(List<GankList.ResultsBean> list) {
//            mDataList.addAll(list);
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_function, null);
//            return new FunctionViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            FunctionViewHolder viewHolder = (FunctionViewHolder) holder;
//            GankList.ResultsBean data = mDataList.get(position);
//
//            viewHolder.tvTitle.setText(data.getWho());
//            viewHolder.tvDes.setText(data.getDesc());
//            viewHolder.tvDate.setText(DateUtil.getSimpleDate(new Date().getTime()));
//
////            if (!TextUtils.isEmpty(data.getUrl())) {
////                glide.load(data.getUrl()).into(viewHolder.ivIcon);
//////                GlideUtils.display(holder.ivIcon, url);
////            }
//        }
//
//        @Override
//        public int getItemCount() {
//            if (mDataList == null) {
//                return 0;
//            }
//            return mDataList.size();
//        }
//
//        class FunctionViewHolder extends RecyclerView.ViewHolder {
//            @BindView(R.id.iv_icon)
//            ImageView ivIcon;
//            @BindView(R.id.tv_title)
//            TextView tvTitle;
//            @BindView(R.id.tv_date)
//            TextView tvDate;
//            @BindView(R.id.tv_des)
//            TextView tvDes;
//            @BindView(R.id.item_root)
//            CardView itemRoot;
//
//            public FunctionViewHolder(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//
//            }
//        }
//    }


}
