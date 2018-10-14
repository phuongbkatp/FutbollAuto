package com.appian.manchesterunitednews.util;

import android.content.Context;
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
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height =
                LayoutParams.WRAP_CONTENT;
        lp.width =
                LayoutParams.MATCH_PARENT;

        Glide.with(context).load(link).into(imageView);
        //imageView.setLayoutParams(lp);

        TextView textView = view.findViewById(R.id.txt_caption);
        textView.setText(caption);
        addView(view);
    }
}