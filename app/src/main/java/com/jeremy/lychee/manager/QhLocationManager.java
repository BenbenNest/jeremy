package com.jeremy.lychee.manager;

import com.jeremy.lychee.base.ContentApplication;
import com.qihu.mobile.lbs.location.IQHLocationListener;
import com.qihu.mobile.lbs.location.LocationManager;
import com.qihu.mobile.lbs.location.QHLocation;
import com.qihu.mobile.lbs.location.QHLocationClientOption;

import java.util.concurrent.CopyOnWriteArrayList;

import rx.functions.Action0;

public class QhLocationManager implements IQHLocationListener {

    private static final int INTERVAL_TIME = 5 * 60 * 1000;

    private static volatile QhLocationManager instance;
    private static LocationManager locationManager;
    private QHLocation location;
    private long lastUpdateTime = 0;
    private boolean updating = false;
    private CopyOnWriteArrayList<Action0> notificationList = new CopyOnWriteArrayList<>();
    private static QHLocationClientOption option;

    public static QhLocationManager getInstance() {
        if (instance == null) {
            synchronized (QhLocationManager.class) {
                if (instance == null) {

                    instance = new QhLocationManager();
                }
            }
        }
        return instance;
    }

    private QhLocationManager() {
        locationManager = LocationManager.makeInstance(ContentApplication.getInstance());
        option = new QHLocationClientOption();
        option.setLocationMode(QHLocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(-1);
    }

    public void updateLocation(Action0 notify) {
        updateLocation(notify, false);
    }

    public void updateLocation(Action0 notify, boolean forceRefresh) {
        if (!forceRefresh && location != null &&
                ((System.currentTimeMillis() - lastUpdateTime) < INTERVAL_TIME)) {
            notifyLocationChange(notify);
            return;
        }

        notificationList.add(notify);
        update();
    }


    private void update() {
        if (updating) {
            return;
        }
        updating = true;
        locationManager.requestLocationUpdates(option, this);
    }


    @Override
    public void onReceiveLocation(QHLocation qhLocation) {
        updating = false;
        lastUpdateTime = System.currentTimeMillis();
        location = qhLocation;
        notifyCB();
    }

    @Override
    public void onReceiveCompass(float v) {

    }

    @Override
    public void onProviderStatusChanged(String s, int i) {

    }

    @Override
    public void onProviderServiceChanged(String s, boolean b) {

    }

    @Override
    public void onGpsSatelliteStatusChanged(int i) {

    }

    @Override
    public void onLocationError(int i) {
        updating = false;
        notifyCB();
    }

    private void notifyLocationChange(Action0 notify) {
        notify.call();
    }

    private void notifyCB() {
        for (Action0 notify : notificationList) {
            notifyLocationChange(notify);
        }
        notificationList.clear();
    }

    public QHLocation getLocation() {
        return location;
    }
}
