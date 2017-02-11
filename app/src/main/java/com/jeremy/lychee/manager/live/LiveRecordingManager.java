package com.jeremy.lychee.manager.live;

import android.support.annotation.IntDef;

import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.utils.PairKey;
import com.jeremy.lychee.model.live.ShorVideoPostResult;
import com.jeremy.lychee.net.OldRetroAdapter;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class LiveRecordingManager {
    static public LiveRecordingManager createInstance() {
        return new LiveRecordingManager();
    }

    private LiveRecordingManager() {
    }

    @IntDef({RECORDING_STATE.PREPARE, RECORDING_STATE.RECORDING, RECORDING_STATE.AFTER_RECORD, RECORDING_STATE.PUBLISH_PAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RECORDING_STATE {
        int PREPARE = 1;
        int RECORDING = 2;
        int AFTER_RECORD = 3;
        int PUBLISH_PAGE = 4;
    }

    private
    @LiveRecordingManager.RECORDING_STATE
    int currentState = RECORDING_STATE.PREPARE;

    private Map<PairKey<Integer, Integer>, Func2<Integer, Integer, Boolean>> actionMap = new HashMap<>(10);

    public boolean registerAction(@RECORDING_STATE int from, @RECORDING_STATE int to, Func2<Integer, Integer, Boolean> action) {
        PairKey<Integer, Integer> pairKey = new PairKey<>(from, to);
        if (actionMap.containsKey(pairKey)) {
            return false;
        }
        actionMap.put(pairKey, action);
        return true;
    }

    public
    @RECORDING_STATE
    int getCurrentState() {
        return currentState;
    }

    public void doNext(@RECORDING_STATE int to) {
        @RECORDING_STATE int from = getCurrentState();
        PairKey<Integer, Integer> pairKey = new PairKey<>(from, to);
        if (!actionMap.containsKey(pairKey)) {
            return;
        }

        currentState = to;
        if (!actionMap.get(pairKey).call(from, to)) {
            currentState = from;
        }
    }

    public static Observable<ModelBase<ShorVideoPostResult>> uploadVideo(String title, String desc, int length,
                                                                         String thumbnailImage, String videoFile,
                                                                         Float latitude, Float longitude,
                                                                         String poi_name) {
        return OldRetroAdapter.getService().postShortVideo(
                RequestBody.create(MediaType.parse("text/plain"), title),
                RequestBody.create(MediaType.parse("text/plain"), desc),
                length,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(thumbnailImage)),
                RequestBody.create(MediaType.parse("video/mp4"), new File(videoFile)),
                latitude, longitude,
                RequestBody.create(MediaType.parse("text/plain"), poi_name)).subscribeOn(Schedulers.io());
    }
}
