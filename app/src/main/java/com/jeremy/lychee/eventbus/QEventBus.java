package com.jeremy.lychee.eventbus;

import android.text.TextUtils;

import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.utils.SystemInfo;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

@SuppressWarnings("unused")
public class QEventBus {
    private EventBus eventBus;

    private static Map<String, QEventBus> mapEventBus = new HashMap<>();
    private static final Object lock = new Object();

    public static final int EVENTBUS_PRIORITY_HIGH = 100;
    public static final int EVENTBUS_PRIORITY_NORMAL = 50;
    public static final int EVENTBUS_PRIORITY_LOW = 10;

    public QEventBus() {
        eventBus = EventBus.builder().logNoSubscriberMessages(false).sendNoSubscriberEvent(false).throwSubscriberException(SystemInfo.isDebugMode()).build();
//        eventBus = EventBus.builder().logNoSubscriberMessages(false).sendNoSubscriberEvent(false).build();
    }

    public static QEventBus getEventBus(String TAG) {
        synchronized (lock) {
            if (TextUtils.isEmpty(TAG)) {
                TAG = "default";
            }

            if (!mapEventBus.containsKey(TAG)) {
                mapEventBus.put(TAG, new QEventBus());
            }

            return mapEventBus.get(TAG);
        }
    }

    public static QEventBus getEventBus() {
        return getEventBus(null);
    }

    public void register(Object subscriber) {
        register(subscriber, EVENTBUS_PRIORITY_NORMAL);
    }

    public void register(Object subscriber, int priority) {
        try {
            Logger.d("QEventBus", "register [" + subscriber.getClass().toString() + "]");
            eventBus.register(subscriber, priority);
        } catch (Exception e) {
            Logger.e("QEventBus", e.getMessage());
        }
    }

    public void registerSticky(Object subscriber) {
        registerSticky(subscriber, EVENTBUS_PRIORITY_NORMAL);
    }

    public void registerSticky(Object subscriber, int priority) {
        try {
            Logger.d("QEventBus", "registerSticky [" + subscriber.getClass().toString() + "]");
            eventBus.registerSticky(subscriber, priority);
        } catch (Exception e) {
            Logger.e("QEventBus", e.getMessage());
        }
    }

    public boolean isRegistered(Object subscriber)
    {
    	return eventBus.isRegistered(subscriber);
    }

    public void unregister(Object subscriber) {
        try {
            Logger.d("QEventBus", "unregister [" + subscriber.getClass().toString() + "]");
            eventBus.unregister(subscriber);
        } catch (Exception e) {
            Logger.e("QEventBus", e.getMessage());
        }
    }

    public void post(Object event) {
        Logger.d("QEventBus", "post [" + event.getClass().toString() + "]");
        eventBus.post(event);
    }

    public void cancelEventDelivery(Object event) {
        eventBus.cancelEventDelivery(eventBus);
    }

    public void postSticky(Object event) {
        Logger.d("QEventBus", "postSticky [" + event.getClass().toString() + "]");
        eventBus.postSticky(event);
    }

    public <T> T getStickyEvent(Class<T> eventType) {
        return eventBus.getStickyEvent(eventType);
    }

    public <T> T removeStickyEvent(Class<T> eventType) {
        return eventBus.removeStickyEvent(eventType);
    }

    public boolean removeStickyEvent(Object event) {
        return eventBus.removeStickyEvent(event);
    }

    public void removeAllStickyEvents() {
        eventBus.removeAllStickyEvents();
    }

    public boolean hasSubscriberForEvent(Class<?> eventClass) {
        return eventBus.hasSubscriberForEvent(eventClass);
    }
}
