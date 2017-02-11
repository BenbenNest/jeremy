package com.jeremy.lychee.videoplayer;

import android.support.v4.util.Pair;

import com.jeremy.lychee.utils.AppUtil;
import com.qihoo.livecloud.OnLiveCloudCallback;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action2;
import rx.schedulers.Schedulers;

@SuppressWarnings("unused")
public class LiveCloudCallbackWrapper implements OnLiveCloudCallback {
    private List<Pair<Action0, Scheduler>> onPreparedActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action2<Integer, Integer>, Scheduler>> onBufferingUpdateActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action2<Integer, Integer>, Scheduler>> onProgressChangeActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action0, Scheduler>> onSeekCompleteActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action0, Scheduler>> onCompletionActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action2<Integer, Integer>, Scheduler>> onSizeChangedActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action2<Integer, Long>, Scheduler>> onInfoActionList = new CopyOnWriteArrayList<>();
    private List<Pair<Action2<Integer, Long>, Scheduler>> onErrorActionList = new CopyOnWriteArrayList<>();

    @Override
    public void onPrepared() {
        for (Pair<Action0, Scheduler> action : onPreparedActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(action.first);
            }
        }
    }

    @Override
    public void onBufferingUpdate(int buffering, int progress) {
        for (Pair<Action2<Integer, Integer>, Scheduler> action : onBufferingUpdateActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(() -> action.first.call(buffering, progress));
            }
        }
    }

    @Override
    public void onProgressChange(int total, int progress) {
        for (Pair<Action2<Integer, Integer>, Scheduler> action : onProgressChangeActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(() -> action.first.call(total, progress));
            }
        }
    }

    @Override
    public void onSeekComplete() {
        for (Pair<Action0, Scheduler> action : onSeekCompleteActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(action.first);
            }
        }
    }

    @Override
    public void onCompletion() {
        for (Pair<Action0, Scheduler> action : onCompletionActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(action.first);
            }
        }
    }

    @Override
    public void onSizeChanged(int width, int height) {
        for (Pair<Action2<Integer, Integer>, Scheduler> action : onSizeChangedActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(() -> action.first.call(width, height));
            }
        }
    }

    @Override
    public void onInfo(int what, long extra) {
        for (Pair<Action2<Integer, Long>, Scheduler> action : onInfoActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(() -> action.first.call(what, extra));
            }
        }
    }

    @Override
    public void onError(int what, long extra) {
        for (Pair<Action2<Integer, Long>, Scheduler> action : onErrorActionList) {
            if (action.first != null && action.second != null) {
                action.second.createWorker().schedule(() -> action.first.call(what, extra));
            }
        }
    }

    private LiveCloudCallbackWrapper() {
    }

    public static LiveCloudCallbackWrapper createInstance() {
        return new LiveCloudCallbackWrapper();
    }

    public LiveCloudCallbackWrapper addOnPreparedAction(Action0 action) {
        return addOnPreparedAction(action, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnPreparedAction(Action0 action, Scheduler scheduler) {
        for (int i = onPreparedActionList.size() - 1; i >= 0; i--) {
            Pair<Action0, Scheduler> item = onPreparedActionList.get(i);
            if (item.first != action && !AppUtil.actionEquals(item.first, action)) {
                continue;
            }

            return this;
        }

        onPreparedActionList.add(new Pair<>(action, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnPreparedAction(Action0 action) {
        for (int i = onPreparedActionList.size() - 1; i >= 0; i--) {
            Pair<Action0, Scheduler> item = onPreparedActionList.get(i);
            if (item.first != action && !AppUtil.actionEquals(item.first, action)) {
                continue;
            }

            onPreparedActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnBufferingUpdateAction(Action2<Integer, Integer> onBufferingUpdateAction) {
        return addOnBufferingUpdateAction(onBufferingUpdateAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnBufferingUpdateAction(Action2<Integer, Integer> onBufferingUpdateAction, Scheduler scheduler) {
        for (int i = onBufferingUpdateActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Integer>, Scheduler> item = onBufferingUpdateActionList.get(i);
            if (item.first != onBufferingUpdateAction && !AppUtil.actionEquals(item.first, onBufferingUpdateAction)) {
                continue;
            }

            return this;
        }

        onBufferingUpdateActionList.add(new Pair<>(onBufferingUpdateAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnBufferingUpdateAction(Action2<Integer, Integer> onBufferingUpdateAction) {
        for (int i = onBufferingUpdateActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Integer>, Scheduler> item = onBufferingUpdateActionList.get(i);
            if (item.first != onBufferingUpdateAction && !AppUtil.actionEquals(item.first, onBufferingUpdateAction)) {
                continue;
            }

            onBufferingUpdateActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnProgressChangeAction(Action2<Integer, Integer> onProgressChangeAction) {
        return addOnProgressChangeAction(onProgressChangeAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnProgressChangeAction(Action2<Integer, Integer> onProgressChangeAction, Scheduler scheduler) {
        for (int i = onProgressChangeActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Integer>, Scheduler> item = onProgressChangeActionList.get(i);
            if (item.first != onProgressChangeAction && !AppUtil.actionEquals(item.first, onProgressChangeAction)) {
                continue;
            }

            return this;
        }

        onProgressChangeActionList.add(new Pair<>(onProgressChangeAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnProgressChangeAction(Action2<Integer, Integer> onProgressChangeAction) {
        for (int i = onProgressChangeActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Integer>, Scheduler> item = onProgressChangeActionList.get(i);
            if (item.first != onProgressChangeAction && !AppUtil.actionEquals(item.first, onProgressChangeAction)) {
                continue;
            }

            onProgressChangeActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnSeekCompleteAction(Action0 onSeekCompleteAction) {
        return addOnSeekCompleteAction(onSeekCompleteAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnSeekCompleteAction(Action0 onSeekCompleteAction, Scheduler scheduler) {
        for (int i = onSeekCompleteActionList.size() - 1; i >= 0; i--) {
            Pair<Action0, Scheduler> item = onSeekCompleteActionList.get(i);
            if (item.first != onSeekCompleteAction && !AppUtil.actionEquals(item.first, onSeekCompleteAction)) {
                continue;
            }

            return this;
        }

        onSeekCompleteActionList.add(new Pair<>(onSeekCompleteAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnSeekCompleteAction(Action0 onSeekCompleteAction) {
        for (int i = onSeekCompleteActionList.size() - 1; i >= 0; i--) {
            Pair<Action0, Scheduler> item = onSeekCompleteActionList.get(i);
            if (item.first != onSeekCompleteAction && !AppUtil.actionEquals(item.first, onSeekCompleteAction)) {
                continue;
            }

            onSeekCompleteActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnCompletionAction(Action0 onCompletionAction) {
        return addOnCompletionAction(onCompletionAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnCompletionAction(Action0 onCompletionAction, Scheduler scheduler) {
        for (int i = onCompletionActionList.size() - 1; i >= 0; i--) {
            Pair<Action0, Scheduler> item = onCompletionActionList.get(i);
            if (item.first != onCompletionAction && !AppUtil.actionEquals(item.first, onCompletionAction)) {
                continue;
            }

            return this;
        }

        onCompletionActionList.add(new Pair<>(onCompletionAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnCompletionAction(Action0 onCompletionAction) {
        for (int i = onCompletionActionList.size() - 1; i >= 0; i--) {
            Pair<Action0, Scheduler> item = onCompletionActionList.get(i);
            if (item.first != onCompletionAction && !AppUtil.actionEquals(item.first, onCompletionAction)) {
                continue;
            }

            onCompletionActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnSizeChangedAction(Action2<Integer, Integer> onSizeChangedAction) {
        return addOnSizeChangedAction(onSizeChangedAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnSizeChangedAction(Action2<Integer, Integer> onSizeChangedAction, Scheduler scheduler) {
        for (int i = onSizeChangedActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Integer>, Scheduler> item = onSizeChangedActionList.get(i);
            if (item.first != onSizeChangedAction && !AppUtil.actionEquals(item.first, onSizeChangedAction)) {
                continue;
            }

            return this;
        }

        onSizeChangedActionList.add(new Pair<>(onSizeChangedAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnSizeChangedAction(Action2<Integer, Integer> onSizeChangedAction) {
        for (int i = onSizeChangedActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Integer>, Scheduler> item = onSizeChangedActionList.get(i);
            if (item.first != onSizeChangedAction && !AppUtil.actionEquals(item.first, onSizeChangedAction)) {
                continue;
            }

            onSizeChangedActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnInfoAction(Action2<Integer, Long> onInfoAction) {
        return addOnInfoAction(onInfoAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnInfoAction(Action2<Integer, Long> onInfoAction, Scheduler scheduler) {
        for (int i = onInfoActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Long>, Scheduler> item = onInfoActionList.get(i);
            if (item.first != onInfoAction && !AppUtil.actionEquals(item.first, onInfoAction)) {
                continue;
            }

            return this;
        }

        onInfoActionList.add(new Pair<>(onInfoAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnInfoAction(Action2<Integer, Long> onInfoAction) {
        for (int i = onInfoActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Long>, Scheduler> item = onInfoActionList.get(i);
            if (item.first != onInfoAction && !AppUtil.actionEquals(item.first, onInfoAction)) {
                continue;
            }

            onInfoActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper addOnErrorAction(Action2<Integer, Long> onErrorAction) {
        return addOnErrorAction(onErrorAction, AndroidSchedulers.mainThread());
    }

    public LiveCloudCallbackWrapper addOnErrorAction(Action2<Integer, Long> onErrorAction, Scheduler scheduler) {
        for (int i = onErrorActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Long>, Scheduler> item = onErrorActionList.get(i);
            if (item.first != onErrorAction && !AppUtil.actionEquals(item.first, onErrorAction)) {
                continue;
            }

            return this;
        }

        onErrorActionList.add(new Pair<>(onErrorAction, scheduler));
        return this;
    }

    public LiveCloudCallbackWrapper removeOnErrorAction(Action2<Integer, Long> onErrorAction) {
        for (int i = onErrorActionList.size() - 1; i >= 0; i--) {
            Pair<Action2<Integer, Long>, Scheduler> item = onErrorActionList.get(i);
            if (item.first != onErrorAction && !AppUtil.actionEquals(item.first, onErrorAction)) {
                continue;
            }

            onErrorActionList.remove(i);
        }
        return this;
    }

    public LiveCloudCallbackWrapper chainLiveCloudCallback(OnLiveCloudCallback callback) {
        if (callback == null) {
            return this;
        }

        return addOnBufferingUpdateAction(callback::onBufferingUpdate, Schedulers.immediate())
                .addOnCompletionAction(callback::onCompletion, Schedulers.immediate())
                .addOnErrorAction(callback::onError, Schedulers.immediate())
                .addOnInfoAction(callback::onInfo, Schedulers.immediate())
                .addOnPreparedAction(callback::onPrepared, Schedulers.immediate())
                .addOnProgressChangeAction(callback::onProgressChange, Schedulers.immediate())
                .addOnSeekCompleteAction(callback::onSeekComplete, Schedulers.immediate())
                .addOnSizeChangedAction(callback::onSizeChanged, Schedulers.immediate());
    }
    
    public LiveCloudCallbackWrapper unchainLiveCloudCallback(OnLiveCloudCallback callback) {
        if (callback == null) {
            return this;
        }

        return removeOnBufferingUpdateAction(callback::onBufferingUpdate)
                .removeOnCompletionAction(callback::onCompletion)
                .removeOnErrorAction(callback::onError)
                .removeOnInfoAction(callback::onInfo)
                .removeOnPreparedAction(callback::onPrepared)
                .removeOnProgressChangeAction(callback::onProgressChange)
                .removeOnSeekCompleteAction(callback::onSeekComplete)
                .removeOnSizeChangedAction(callback::onSizeChanged);        
    }

    public LiveCloudCallbackWrapper reset() {
        onPreparedActionList.clear();
        onBufferingUpdateActionList.clear();
        onProgressChangeActionList.clear();
        onSeekCompleteActionList.clear();
        onCompletionActionList.clear();
        onSizeChangedActionList.clear();
        onInfoActionList.clear();
        onErrorActionList.clear();
        return this;
    }
}
