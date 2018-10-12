package com.appian.manchesterunitednews.data.interactor;

public interface OnDetailNewsResponseListener<T> {
    void onSuccess(T data);

    void onFailure(String error);
}
