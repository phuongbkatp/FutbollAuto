package com.appian.manchesterunitednews.util;

import android.widget.ImageView;

import com.appian.manchesterunitednews.MainApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public final class ImageLoader {

    public static void displayImage(String url, ImageView imageView) {
        Glide.with(MainApplication.getApplication()).load(url).into(imageView);
    }

    public static void displayImage(String url, ImageView imageView, int defaultDrawableId) {
        Glide.with(MainApplication.getApplication()).load(url).apply(new RequestOptions()
        .placeholder(defaultDrawableId)).into(imageView);
    }
}