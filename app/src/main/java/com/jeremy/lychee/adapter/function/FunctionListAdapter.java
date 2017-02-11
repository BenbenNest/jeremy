package com.jeremy.lychee.adapter.function;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.BaseAdapter;
import com.jeremy.lychee.bean.FunctionListData;
import com.jeremy.lychee.bean.GlideUtils;
import com.jeremy.lychee.model.function.IFunctionListModel;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by benbennest on 16/4/26.
 */
public class FunctionListAdapter extends BaseAdapter {
    private Context mContext;
    private IFunctionListModel model;
    //    private SparseArray<FunctionListData> list;
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

//    public String getFromAssets(String fileName){
//        String result = "";
//        try {
//            InputStream in = mContext.getResources().getAssets().open(fileName);
////获取文件的字节数
//            int lenght = in.available();
////创建byte数组
//            byte[]  buffer = new byte[lenght];
////将文件中的数据读到byte数组中
//            in.read(buffer);
//            result = EncodingUtils.getString(buffer, ENCODING);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}

    private void saveUrl(List<String> list) {
        if (AppUtil.isMounted()) {
            String dir = AppUtil.getAppSdRootPath();
            File Dir = new File(dir);
            if (!Dir.exists()) {
                Dir.mkdirs();
            }
            String path = "NetImageUrl.txt";
            File file = new File(Dir, path);
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(list);
                objectOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FunctionListAdapter(Context context, IFunctionListModel model) {
        mContext = context;
        this.model = model;
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
        FunctionListData data = model.getFunction(i);
        holder.tvTitle.setText(data.getTitle());
        holder.tvDes.setText(data.getDes());
        holder.tvDate.setText(DateUtils.date2String(new Date().getTime()));
        String url = "";
        if (images != null && images.size() > 0) {
            Random random = new Random();
            int pos = random.nextInt(images.size() - 1);
            url = images.get(pos);
        }
        if (!TextUtils.isEmpty(url)) {
            GlideUtils.display(holder.ivIcon, url);
        } else {
            GlideUtils.displayNative(holder.ivIcon, R.mipmap.img_default_gray);
        }
        setOnclickListener(holder, data);
    }

    @Override
    public int getItemCount() {
        if (model.getFunctionList() == null) {
            return 0;
        }
        return model.getFunctionList().size();
    }

    private void setOnclickListener(FunctionViewHolder holder, FunctionListData data) {
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

        @Bind(R.id.item_root)
        CardView itemRoot;
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_des)
        TextView tvDes;

        public FunctionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
