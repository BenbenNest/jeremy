package com.jeremy.library.http;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ModelCallback<T> implements ICallback{

    @Override
    public void onSuccess(String result) {
        Class<? extends T> geneticClass = getGeneticClass(this);//this的类型应该是ICallback
        T t = new Gson().fromJson(result, geneticClass);
        //重定向到新的success函数
        onSuccess(t);
    }

    protected  Class<? extends T> getGeneticClass(Object object){
        //获得带有泛型的直接父类的type   ModelCallback
        Type genericSuperclass = object.getClass().getGenericSuperclass();
        // ParameterizedType 带参数的类型 泛型
        //getActualTypeArguments 参数的类型 泛型类型
        return (Class<? extends T>) ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];
    }

    public abstract void onSuccess(T t);
}
