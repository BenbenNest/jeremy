package com.jeremy.design.observer;

/**
 * Created by changqing on 2018/2/24.
 */

public class Observer2 implements Observer {

    @Override
    public void update() {
        System.out.println("observer2 has received!");
    }

}