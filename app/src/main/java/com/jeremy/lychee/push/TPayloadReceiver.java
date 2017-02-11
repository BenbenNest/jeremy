package com.jeremy.lychee.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.qihoo.kd.push.Constants;
import com.qihoo.kd.push.utils.QLogUtil;

public class TPayloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        Gson gson = new Gson();
        try {

            String payload = bundle.getString(Constants.KEY_MESSAGE_DATA);

            QLogUtil.debugLog("TPayloadReceiver payload:" + payload);
            // json="{\"msgID\":1404118956,\"expire\":\"2014-06-30 20:06:36\",\"title\":\"\u4e16\u754c\u676f\u665a\u62a5:\u5fb7\u56fd\u76fc\u8fde\u7eed16\u5c4a\u8fdb8\u5f3a|\u6cd5\u5c3c\u6216\u6253\u5bf9\u653b\u6218\",\"content\":\"\u4e16\u754c\u676f\u665a\u62a5:\u5fb7\u56fd\u76fc\u8fde\u7eed16\u5c4a\u8fdb8\u5f3a|\u6cd5\u5c3c\u6216\u6253\u5bf9\u653b\u6218\",\"channelId\":\"head\",\"url\":\"http://mtjapi.news.haosou.com/index.php?flag=2&ro=topic&ra=view&tid=18264\",\"m\":\"2d4bf49452223cb63d38f0b3d88f6ef01637254e\",\"kws\":\"\u4e16\u754c\u676f \u5fb7\u56fd\",\"important\":true,\"interests\":\"\",\"province\":null,\"city\":null,\"src\":\"360\u65b0\u95fb\",\"type\":0}";
//            PushMeta pushMeta = gson.fromJson(payload, PushMeta.class);
            Intent service = new Intent(context, RetrievePushService.class);
            service.setAction(RetrievePushService.RETRIEVE_PUSH_SERVICE);
            service.putExtra(RetrievePushService.PUSH_META, payload);
            context.startService(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
