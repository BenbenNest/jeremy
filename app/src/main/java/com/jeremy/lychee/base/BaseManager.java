package com.jeremy.lychee.base;

import com.jeremy.lychee.eventbus.QEventBus;

public abstract class BaseManager {

    public BaseManager(){
        QEventBus.getEventBus().register(this);
    }

    public void finish(){
        QEventBus.getEventBus().unregister(this);
    }

}
