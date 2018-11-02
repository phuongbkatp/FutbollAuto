package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by phuongbkatp on 10/5/2018.
 */
public class CustomImageLayout extends LinearLayout {

    public CustomImageLayout(Context context, String link, String caption) {
        super(context);
        initView(context, link, caption);
    }

    private void initView(Context context, String link, String caption) {
        View view = inflate(context, R.layout.custom_image_layout, null);
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        Glide.with(context).load(link).apply(options).into(imageView);

        TextView textView = view.findViewById(R.id.txt_caption);
        if(TextUtils.isEmpty(caption)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(caption);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(view, params);
    }
}