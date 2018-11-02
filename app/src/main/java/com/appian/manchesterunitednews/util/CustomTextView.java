package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;

/**
 * Created by phuongbkatp on 10/5/2018.
 */

public class CustomTextView extends LinearLayout {
    public CustomTextView(Context context, String content, boolean isHead) {
        super(context);
        initView(context, content, isHead);
    }

    private void initView(Context context,String content,boolean isHead) {
        View view = inflate(context, R.layout.custom_text__view, null);
        TextView textView = view.findViewById(R.id.content);
        textView.setText(content);
        if (isHead) {
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(view, params);
    }

}
