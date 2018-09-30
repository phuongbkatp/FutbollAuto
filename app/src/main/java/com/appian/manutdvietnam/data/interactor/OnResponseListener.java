package com.appian.manutdvietnam.data.interactor;

public interface OnResponseListener<T> {
    void onSuccess(T data);

    void onFailure(String error);
}
