package com.jeremy.inject;

public interface Inject<T> {

    void inject(T host, Object object, Provider provider);
}
