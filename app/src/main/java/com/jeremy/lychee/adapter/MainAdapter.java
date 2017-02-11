/*
 * {EasyGank}  Copyright (C) {2015}  {CaMnter}
 *
 * This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; type `show c' for details.
 *
 * The hypothetical commands `show w' and `show c' should show the appropriate
 * parts of the General Public License.  Of course, your program's commands
 * might be different; for a GUI interface, you would use an "about box".
 *
 * You should also get your employer (if you work as a programmer) or school,
 * if any, to sign a "copyright disclaimer" for the program, if necessary.
 * For more information on this, and how to apply and follow the GNU GPL, see
 * <http://www.gnu.org/licenses/>.
 *
 * The GNU General Public License does not permit incorporating your program
 * into proprietary programs.  If your program is a subroutine library, you
 * may consider it more useful to permit linking proprietary applications with
 * the library.  If this is what you want to do, use the GNU Lesser General
 * Public License instead of this License.  But first, please read
 * <http://www.gnu.org/philosophy/why-not-lgpl.html>.
 */

package com.jeremy.lychee.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.camnter.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.camnter.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.jeremy.lychee.R;
import com.jeremy.lychee.bean.BaseGankData;
import com.jeremy.lychee.bean.DateUtils;
import com.jeremy.lychee.bean.GankDaily;
import com.jeremy.lychee.bean.GlideUtils;
import com.jeremy.lychee.constant.Constant;
import com.jeremy.lychee.constant.UrlMatch;
import com.jeremy.lychee.gank.GankApi;
import com.jeremy.lychee.gank.GankType;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageLoader;
import com.jeremy.lychee.widget.RatioImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends EasyRecyclerViewAdapter {


    public static final int LAYOUT_TYPE_DAILY = 0;
    public static final int LAYOUT_TYPE_TECHNOLOGY = 1;
    public static final int LAYOUT_TYPE_WELFARE = 2;

    private Context context;

    private int type;
    private OnClickListener listener;

    public MainAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_daily,
                R.layout.item_data,
                R.layout.item_welfate,
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder easyRecyclerViewHolder, int position) {
        int layoutType = this.getRecycleViewItemType(position);
        switch (layoutType) {
            case LAYOUT_TYPE_DAILY:
                this.loadingDaily(easyRecyclerViewHolder, position);
                break;
            case LAYOUT_TYPE_TECHNOLOGY:
                this.loadingTechnology(easyRecyclerViewHolder, position);
                break;
            case LAYOUT_TYPE_WELFARE:
                this.loadingWelfare(easyRecyclerViewHolder, position);
                break;
        }
    }

    public int getType() {
        return type;
    }

    /**
     * 用于切换数据类型，从而切换数据源(url)
     *
     * @param type type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 不需要一个RecyclerView里item不一样的情况
     *
     * @param position position
     * @return int
     */
    @Override
    public int getRecycleViewItemType(int position) {
        /*
         * android、ios、js、resource、app是一套布局
         */
        switch (this.type) {
            case GankType.daily:
                return LAYOUT_TYPE_DAILY;
            case GankType.android:
            case GankType.ios:
            case GankType.js:
            case GankType.resources:
            case GankType.video:
            case GankType.app:
                return LAYOUT_TYPE_TECHNOLOGY;
            case GankType.welfare:
                return LAYOUT_TYPE_WELFARE;
            default:
                return LAYOUT_TYPE_DAILY;
        }
    }

    List<String> urlList = new ArrayList<String>();
    String urls = "";

    /**
     * 加载每日干货类型数据
     *
     * @param easyRecyclerViewHolder easyRecyclerViewHolder
     * @param position               position
     */
    private void loadingDaily(EasyRecyclerViewHolder easyRecyclerViewHolder, int position) {
        GankDaily dailyData = this.getItem(position);
        if (dailyData == null) return;
        ImageView dailyIV = easyRecyclerViewHolder.findViewById(R.id.daily_iv);
        TextView dailyDateTV = easyRecyclerViewHolder.findViewById(R.id.daily_date_tv);
        TextView dailyTitleTV = easyRecyclerViewHolder.findViewById(R.id.daily_title_tv);

        TextView androidTagTV = easyRecyclerViewHolder.findViewById(R.id.daily_android_tag_tv);
        TextView iOSTagTV = easyRecyclerViewHolder.findViewById(R.id.daily_ios_tag_tv);
        TextView jsTagTV = easyRecyclerViewHolder.findViewById(R.id.daily_js_tag_tv);

        /*
         * 标题 和 日期
         *
         * 如果没有视频的title和date就找福利的title和date，实在没有就，完！
         */
        if (dailyData.results.videoData != null && dailyData.results.videoData.size() > 0) {
            BaseGankData video = dailyData.results.videoData.get(0);
            dailyTitleTV.setText(video.desc.trim());
            dailyDateTV.setText(DateUtils.date2String(video.publishedAt.getTime(), Constant.DAILY_DATE_FORMAT));
        } else if (dailyData.results.welfareData != null && dailyData.results.welfareData.size() > 0) {
            BaseGankData welfare = dailyData.results.welfareData.get(0);
            dailyTitleTV.setText(welfare.desc.trim());
            dailyDateTV.setText(DateUtils.date2String(welfare.publishedAt.getTime(), Constant.DAILY_DATE_FORMAT));
        } else {
            dailyTitleTV.setText("这期没福利了，安心学习吧！");
            dailyDateTV.setText("");
        }

        // 图片
        if (dailyData.results.welfareData != null && dailyData.results.welfareData.size() > 0) {
            final BaseGankData welfare = dailyData.results.welfareData.get(0);
            GlideUtils.display(dailyIV, welfare.url);
            if (!urlList.contains(welfare.url)) {
                urlList.add(welfare.url);
                GlideImageLoader imageLoader = new GlideImageLoader(dailyIV);
                imageLoader.loadImage(welfare.url, new GlideImageLoader.OnGlideLoadListener() {
                    @Override
                    public void onLoad(DrawableTypeRequest request, ImageView view) {

                    }
                });
            }
            urls += welfare.url;
            dailyIV.setOnClickListener(v -> {
                if (MainAdapter.this.listener != null)
                    MainAdapter.this.listener.onClickPicture(welfare.url, welfare.desc, v);
            });
        } else {
            GlideUtils.displayNative(dailyIV, R.mipmap.img_default_gray);
        }

        /*
         * 标签 ListView 和 RecyclerView 都要写else 因为复用问题
         * 忧伤
         */
        if (dailyData.category == null) {
            androidTagTV.setVisibility(View.GONE);
            iOSTagTV.setVisibility(View.GONE);
            jsTagTV.setVisibility(View.GONE);
        } else {
            if (dailyData.category.contains(GankApi.DATA_TYPE_ANDROID)) {
                androidTagTV.setVisibility(View.VISIBLE);
            } else {
                androidTagTV.setVisibility(View.GONE);
            }
            if (dailyData.category.contains(GankApi.DATA_TYPE_IOS)) {
                iOSTagTV.setVisibility(View.VISIBLE);
            } else {
                iOSTagTV.setVisibility(View.GONE);
            }
            if (dailyData.category.contains(GankApi.DATA_TYPE_JS)) {
                jsTagTV.setVisibility(View.VISIBLE);
            } else {
                jsTagTV.setVisibility(View.GONE);
            }
        }
        if (position == getList().size() - 1) {
            Log.v("url", urls);
            saveUrl(urlList);
        }
    }

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

//    http://ww2.sinaimg.cn/large/610dc034gw1f35cxyferej20dw0i2789.jpghttp://ww2.sinaimg.cn/large/7a8aed7bjw1f340c8jrk4j20j60srgpf.jpghttp://ww4.sinaimg.cn/large/7a8aed7bjw1f32d0cumhkj20ey0mitbx.jpghttp://ww2.sinaimg.cn/large/7a8aed7bjw1f30sgi3jf0j20iz0sg40a.jpghttp://ww1.sinaimg.cn/large/7a8aed7bjw1f2zwrqkmwoj20f00lg0v7.jpghttp://ww3.sinaimg.cn/large/7a8aed7bjw1f2x7vxkj0uj20d10mi42r.jpghttp://ww2.sinaimg.cn/large/7a8aed7bjw1f2w0qujoecj20f00kzjtt.jpghttp://ww4.sinaimg.cn/large/610dc034jw1f2uyg3nvq7j20gy0p6myx.jpghttp://ww4.sinaimg.cn/large/7a8aed7bjw1f2tpr3im0mj20f00l6q4o.jpghttp://ww1.sinaimg.cn/large/7a8aed7bjw1f2sm0ns82hj20f00l8tb9.jpghttp://ww3.sinaimg.cn/large/7a8aed7bjw1f2p0v9vwr5j20b70gswfi.jpghttp://ww1.sinaimg.cn/large/7a8aed7bjw1f2zwrqkmwoj20f00lg0v7.jpghttp://ww2.sinaimg.cn/large/7a8aed7bjw1f30sgi3jf0j20iz0sg40a.jpghttp://ww4.sinaimg.cn/large/7a8aed7bjw1f32d0cumhkj20ey0mitbx.jpghttp://ww2.sinaimg.cn/large/7a8aed7bjw1f340c8jrk4j20j60srgpf.jpghttp://ww2.sinaimg.cn/large/610dc034gw1f35cxyferej20dw0i2789.jpghttp://ww3.sinaimg.cn/large/7a8aed7bjw1f2x7vxkj0uj20d10mi42r.jpghttp://ww4.sinaimg.cn/large/610dc034jw1f2uyg3nvq7j20gy0p6myx.jpghttp://ww4.sinaimg.cn/large/7a8aed7bjw1f2tpr3im0mj20f00l6q4o.jpghttp://ww1.sinaimg.cn/large/7a8aed7bjw1f2sm0ns82hj20f00l8tb9.jpghttp://ww3.sinaimg.cn/large/7a8aed7bjw1f2p0v9vwr5j20b70gswfi.jpg

    /**
     * 加载技术类型数据 ( Android、iOS、前端、拓展资源、App )
     *
     * @param easyRecyclerViewHolder easyRecyclerViewHolder
     * @param position               position
     */
    private void loadingTechnology(EasyRecyclerViewHolder easyRecyclerViewHolder, int position) {
        BaseGankData baseGankData = this.getItem(position);
        if (baseGankData == null) return;
        TextView dataDateTV = easyRecyclerViewHolder.findViewById(R.id.data_date_tv);
        TextView dataTitleTV = easyRecyclerViewHolder.findViewById(R.id.data_title_tv);
        TextView dataViaTV = easyRecyclerViewHolder.findViewById(R.id.data_via_tv);
        TextView dataTagTV = easyRecyclerViewHolder.findViewById(R.id.data_tag_tv);

        // 标题
        if (TextUtils.isEmpty(baseGankData.desc)) {
            dataTitleTV.setText("");
        } else {
            dataTitleTV.setText(baseGankData.desc.trim());
        }

        // 时间
        if (baseGankData.publishedAt == null) {
            dataDateTV.setText("");
        } else {
            dataDateTV.setText(DateUtils.getTimestampString(baseGankData.publishedAt));
        }

        // 小编
        if (TextUtils.isEmpty(baseGankData.who)) {
            dataViaTV.setText("");
        } else {
            dataViaTV.setText(this.context.getString(R.string.common_via, baseGankData.who));
        }


        if (TextUtils.isEmpty(baseGankData.url)) {
            dataTagTV.setVisibility(View.GONE);
        } else {
            this.setTag(dataTagTV, baseGankData.url);
        }

    }

    /**
     * @param dataTagTV dataTagTV
     * @param url       url
     */
    private void setTag(TextView dataTagTV, String url) {
        String key = UrlMatch.processUrl(url);
        GradientDrawable drawable = (GradientDrawable) dataTagTV.getBackground();
        if (UrlMatch.url2Content.containsKey(key)) {
            drawable.setColor(UrlMatch.url2Color.get(key));
            dataTagTV.setText(UrlMatch.url2Content.get(key));
        } else {
            if (this.type == GankType.video) {
                drawable.setColor(UrlMatch.OTHER_VIDEO_COLOR);
                dataTagTV.setText(UrlMatch.OTHER_VIDEO_CONTENT);
            } else {
                // github 的要特殊处理
                if (url.contains(UrlMatch.GITHUB_PREFIX)) {
                    drawable.setColor(UrlMatch.url2Color.get(UrlMatch.GITHUB_PREFIX));
                    dataTagTV.setText(UrlMatch.url2Content.get(UrlMatch.GITHUB_PREFIX));
                } else {
                    drawable.setColor(UrlMatch.OTHER_BLOG_COLOR);
                    dataTagTV.setText(UrlMatch.OTHER_BLOG_CONTENT);
                }
            }
        }
    }

    /**
     * 加载 福利
     *
     * @param easyRecyclerViewHolder easyRecyclerViewHolder
     * @param position               position
     */
    private void loadingWelfare(EasyRecyclerViewHolder easyRecyclerViewHolder, int position) {
        BaseGankData baseGankData = this.getItem(position);
        if (baseGankData == null) return;
        RatioImageView welfareIV = easyRecyclerViewHolder.findViewById(R.id.welfare_iv);

        if (position % 2 == 0) {
            welfareIV.setImageRatio(0.7f);
        } else {
            welfareIV.setImageRatio(0.6f);
        }

        // 图片
        if (TextUtils.isEmpty(baseGankData.url)) {
            GlideUtils.displayNative(welfareIV, R.mipmap.img_default_gray);
        } else {
            GlideUtils.display(welfareIV, baseGankData.url);
        }

    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClickPicture(String url, String title, View view);
    }

}
