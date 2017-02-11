package com.jeremy.lychee.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.news.OldNewsDetailActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.PushInfo;
import com.jeremy.lychee.model.PushMeta;
import com.jeremy.lychee.model.news.NewsListDataWrapper;
import com.jeremy.lychee.preference.PreferenceKey;
import com.jeremy.lychee.preference.ProgramPreference;
import com.jeremy.lychee.preference.UserPreference;
import com.qihoo.kd.push.utils.QLogUtil;
import com.qihoo.sdk.report.QHStatAgent;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author houwenchang
 *         <p>
 *         2015/7/7.
 */
public class RetrievePushService extends Service {

    public final static String RETRIEVE_PUSH_SERVICE = "com.qihoo.miop.Retrieve_Push_Service";
    public static final String PUSH_META = "com.qihoo.miop.PUSH_META";


    //1为收到PUSH，2为单击PUSH，3为已展示，4为触发下载。
    public final static int QDUS_PUSH_EVNET_TYPE_RECEIVE = 1;
    public final static int QDUS_PUSH_EVNET_TYPE_CLICK = 2;
    public final static int QDUS_PUSH_EVNET_TYPE_SHOW = 3;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        QLogUtil.debugLog("RetrievePushService onStartCommand");
        if (intent != null && RETRIEVE_PUSH_SERVICE.equals(intent.getAction())) {
            QLogUtil.debugLog("RetrievePushService onStartCommand action:" + intent.getAction());
            String meta = intent.getStringExtra(PUSH_META);
//            if (meta != null) {
//                String id = meta.getMsgID();
//            }


            Observable.create(subscriber -> handleIntent(intent))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();


        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void handleIntent(Intent intent) {
        QLogUtil.debugLog("RetrievePushService handleIntent");
        if (intent != null && RETRIEVE_PUSH_SERVICE.equals(intent.getAction())) {
            PushMeta pushMeta = new Gson().fromJson(intent.getStringExtra(PUSH_META), PushMeta.class);
            try {
                String url = pushMeta.pushUrl;
                if (TextUtils.isEmpty(url)) {
                    QLogUtil.debugLog("RetrievePushService handleIntent url is empty");
                    return;
                }

//                pushId = Uri.parse(url).getQueryParameter("pushId");

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    JsonElement rootElement = new JsonParser().parse(new JsonReader(new StringReader(json)));
                    JsonObject obj = rootElement.getAsJsonObject();
                    int code = obj.get("status").getAsInt();
                    if (code == 0) {
                        String messge = obj.get("data").toString();
                        QLogUtil.debugLog("RetrievePushService handleIntent messge:" + messge);
//                json="{\"msgID\":1404118956,\"expire\":\"2014-06-30 20:06:36\",\"title\":\"\u4e16\u754c\u676f\u665a\u62a5:\u5fb7\u56fd\u76fc\u8fde\u7eed16\u5c4a\u8fdb8\u5f3a|\u6cd5\u5c3c\u6216\u6253\u5bf9\u653b\u6218\",\"content\":\"\u4e16\u754c\u676f\u665a\u62a5:\u5fb7\u56fd\u76fc\u8fde\u7eed16\u5c4a\u8fdb8\u5f3a|\u6cd5\u5c3c\u6216\u6253\u5bf9\u653b\u6218\",\"channelId\":\"head\",\"url\":\"http://mtjapi.news.haosou.com/index.php?flag=2&ro=topic&ra=view&tid=18264\",\"m\":\"2d4bf49452223cb63d38f0b3d88f6ef01637254e\",\"kws\":\"\u4e16\u754c\u676f \u5fb7\u56fd\",\"important\":true,\"interests\":\"\",\"province\":null,\"city\":null,\"src\":\"360\u65b0\u95fb\",\"type\":0}";

                        PushInfo pushInfo = new Gson().fromJson(messge, PushInfo.class);
                        QHStatAgent.onPushEvent(this, String.valueOf(pushInfo.id), QDUS_PUSH_EVNET_TYPE_RECEIVE);
                        if (!UserPreference.getInstance().getUseMobilePushEnabled()) {
                            QLogUtil.debugLog("RetrievePushService handleIntent isUseMobilePushEnabled false");
                            return;
                        }
                        if(isRepeat(pushInfo)){
                            return;
                        }

                        Notification mNotification = buildNotification(this, pushInfo);
                        NotificationManager mNotifyManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotifyManager.notify(pushInfo.id, mNotification);
                        QHStatAgent.onPushEvent(this, String.valueOf(pushInfo.id), QDUS_PUSH_EVNET_TYPE_SHOW);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                QLogUtil.debugLog("RetrievePushService handleIntent error");
            }
        }
    }


    //创建通知

    public static Notification buildNotification(Context context, PushInfo pushInfo) {


        Intent intent = getIntentByVt(context, pushInfo);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, pushInfo.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(pushInfo.summary).setBigContentTitle(pushInfo.title);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(pushInfo.title)
                .setContentText(pushInfo.summary)
                .setTicker(pushInfo.title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(bigTextStyle);


        int flag = Notification.DEFAULT_ALL;
        // 默认声音
        flag = flag & ~Notification.DEFAULT_SOUND;
        // 默认振动
        flag = flag & ~Notification.DEFAULT_VIBRATE;
        mBuilder.setDefaults(flag);
        return mBuilder.build();
    }

    /**
     * 消息类型跳转界面不一样的逻辑
     */

    private static Intent getIntentByVt(Context context, PushInfo pushInfo) {
        //跳转到详情界面
        Intent intent = new Intent(context, OldNewsDetailActivity.class);

        //需要拼接一个newsentity

        NewsListData news = new NewsListData();
        news.setMd5(pushInfo.url_md5);
        news.setTitle(pushInfo.title);
        news.setZm(pushInfo.zm);
        news.setUrl(pushInfo.url);
        news.setNid(news.getNid());
        intent.putExtra(OldNewsDetailActivity.NEW_ENTITY, new NewsListDataWrapper(news));
        intent.putExtra(OldNewsDetailActivity.PUSH_ID, pushInfo.id);
        return intent;
    }


    private boolean isRepeat(PushInfo pushInfo) {
        String key = PreferenceKey.PUSH_ID_BUFFER;
        List<String> pushIdPool = new ArrayList<>();
        Collections.addAll(pushIdPool, ProgramPreference.getInstance().getStringValue(key).split(","));
        if (pushIdPool.contains(String.valueOf(pushInfo.id))) {
            return true;
        }
        pushIdPool.add(String.valueOf(pushInfo.id));
        if (pushIdPool.size() > 10) {
            pushIdPool.remove(0);
        }
        String value = "";
        for (String id : pushIdPool) {
            value += id;
            value += ",";
        }
        ProgramPreference.getInstance().saveStringValue(key, value);
        return false;
    }
}
