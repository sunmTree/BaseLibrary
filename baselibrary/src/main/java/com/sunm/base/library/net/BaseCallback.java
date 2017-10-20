package com.sunm.base.library.net;

public interface BaseCallback<T, M> {
    void requestSuccess(T t);
    void requestFailed(M m, Exception e);
}
