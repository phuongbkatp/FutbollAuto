package com.appian.manchesterunitednews.app.newstopic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.detailnews.DetailArticleActivity;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;

public class HeadNewsFragment extends BaseFragment {

    private int mId;
    private String mTitle;
    private String mThumbnail;
    private long mTime;
    private int mComments;

    public static HeadNewsFragment newInstance(int id, String title, String thumbnail, long time, int comments) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("title", title);
        args.putString("thumbnail", thumbnail);
        args.putLong("time", time);
        args.putInt("comments", comments);
        HeadNewsFragment fragment = new HeadNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mId = args.getInt("id");
            mTitle = args.getString("title");
            mThumbnail = args.getString("thumbnail");
            mTime = args.getLong("time");
            mComments = args.getInt("comments");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imvThumbnail = view.findViewById(R.id.img_thumb_news);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvTime = view.findViewById(R.id.tv_date);
        TextView tvComments = view.findViewById(R.id.tv_comments);
        ImageLoader.displayImage(mThumbnail, imvThumbnail, R.drawable.jersey);
        tvTitle.setText(mTitle);
        tvTime.setText(Utils.calculateTimeAgo(getContext(), mTime));
        tvComments.setText(String.valueOf(mComments));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailArticleActivity();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.item_news_header;
    }

    private void startDetailArticleActivity() {
        Intent intent = new Intent(getContext(), DetailArticleActivity.class);
        int[] ids = {mId};
        //intent.putExtra(DetailArticleActivity.EXTRA_NEWS_LIST_ID, ids);
        startActivity(intent);

    }
}
