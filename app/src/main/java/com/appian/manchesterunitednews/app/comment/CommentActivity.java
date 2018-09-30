package com.appian.manchesterunitednews.app.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.appian.manchesterunitednews.Constant;
import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseActivity;

public class CommentActivity extends BaseActivity {
    private static final String TAG_COMMENT = "comment";
    public static final String KEY_ID = "id";
    private int mNewsId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        View btnBackArrow = findViewById(R.id.img_back_arrow);
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        if(intent != null) {
            mNewsId = intent.getIntExtra(KEY_ID, 0);
        }
        FragmentManager fm = getSupportFragmentManager();
        CommentFragment commentFrg = (CommentFragment)fm.findFragmentByTag(TAG_COMMENT);
        if(commentFrg == null) {
            commentFrg = CommentFragment.newInstance(Constant.OBJECT_TYPE_NEWS, mNewsId);
        }
        fm.beginTransaction().replace(R.id.view_content, commentFrg, TAG_COMMENT).commit();
    }
}
