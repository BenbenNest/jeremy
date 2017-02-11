//package com.qihoo.lianxian.manager;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import ContentApplication;
//import Logger;
//
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import rx.functions.Action0;
//
//public class LocationManager implements AMapLocationListener {
//
//    private static final int INTERVAL_TIME = 5 * 60 * 1000;
//
//    private static volatile LocationManager instance;
//    private AMapLocationClient client;
//    private AMapLocation location;
//    private long lastUpdateTime = 0;
//    private boolean updating = false;
//    private CopyOnWriteArrayList<Action0> notificationList = new CopyOnWriteArrayList<>();
//
//
//    public static LocationManager getInstance() {
//        if (instance == null) {
//            synchronized (LocationManager.class) {
//                if (instance == null) {
//                    instance = new LocationManager();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private LocationManager() {
//        client = new AMapLocationClient(ContentApplication.getInstance());
//        client.setLocationListener(this);
//        AMapLocationClientOption option = new AMapLocationClientOption();
//        option.setOnceLocation(true);
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        client.setLocationOption(option);
//    }
//
//    public void updateLocation(Action0 notify) {
//        updateLocation(notify, false);
//    }
//
//    public void updateLocation(Action0 notify, boolean forceRefresh) {
//        if (!forceRefresh && location != null &&
//                ((System.currentTimeMillis() - lastUpdateTime) < INTERVAL_TIME)) {
//            notifyLocationChange(notify);
//            return;
//        }
//
//        notificationList.add(notify);
//        update();
//    }
//
//    private void update() {
//        if (updating) {
//            return;
//        }
//        updating = true;
//        client.startLocation();
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        updating = false;
//        if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
//            if (aMapLocation != null) {
//                Logger.t("amap").d(aMapLocation.getErrorInfo());
//            }
//        } else {
//            lastUpdateTime = System.currentTimeMillis();
//            location = aMapLocation;
//        }
//        notifyCB();
//    }
//
//    public AMapLocation getLocation() {
//        return location;
//    }
//
//    private void notifyLocationChange(Action0 notify) {
//        notify.call();
//    }
//
//    private void notifyCB() {
//        for (Action0 notify : notificationList) {
//            notifyLocationChange(notify);
//        }
//        notificationList.clear();
//    }
//}
