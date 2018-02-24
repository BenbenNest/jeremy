package com.jeremy.design.observer;

/**
 * Created by changqing on 2018/2/24.
 */

public class MySubject extends AbstractSubject {

    @Override
    public void operation() {
        System.out.println("update self!");
        notifyObservers();
    }

}
