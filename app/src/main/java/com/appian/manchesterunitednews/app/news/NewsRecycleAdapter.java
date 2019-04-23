package com.appian.manchesterunitednews.app.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.widget.FbAdRecyclerAdapter;
import com.appnet.android.football.fbvn.data.NewsAuto;

class NewsRecycleAdapter extends FbAdRecyclerAdapter<NewsAuto> {
    private static final int SMALL_NEWS_VIEW_TYPE = 1;
    private static final int LARGE_NEWS_VIEW_TYPE = 2;
    private static final int TOPIC_NEWS_VIEW_TYPE = 3;

    NewsRecycleAdapter(Context context, String unitId) {
        super(context, unitId, 0);
    }

    NewsRecycleAdapter(Context context, String unitId, int maxFbAds) {
        super(context, unitId, maxFbAds);
    }

    @Override
    protected int getViewType(int position) {
/*        NewsAuto news = getItem(position);
        if (news == null) {
            return 0;
        }
        return news.getLayoutType();*/
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SMALL_NEWS_VIEW_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_news_small, parent, false);

                return new RecyclerViewHolder(view);
            case LARGE_NEWS_VIEW_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_news_large, parent, false);

                return new RecyclerViewHolder(view);
            case TOPIC_NEWS_VIEW_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_news_topic, parent, false);

                return new RecyclerViewHolder(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_news_small, parent, false);

                return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsAuto item = getItem(position);
        if (item == null) {
            return;
        }
        RecyclerViewHolder itemHolder = (RecyclerViewHolder) holder;
        itemHolder.TvTitle.setText(item.getTitle());
        itemHolder.TvSource.setText(item.getSiteName());
        if (item.getCreatedTime() != 0) {
            itemHolder.TvDate.setText(Utils.calculateTimeAgo(mContext, item.getCreatedTime()));
        } else {
            itemHolder.TvDate.setVisibility(View.GONE);
        }
        ImageLoader.displayImage(item.getImage(), itemHolder.ImgThumbnail);
/*        if(item.isVideoType()) {
            itemHolder.IconVideo.setVisibility(View.VISIBLE);
        } else {
            itemHolder.IconVideo.setVisibility(View.GONE);
        }*/
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView TvTitle;
        TextView TvDate;
        TextView TvSource;
        ImageView ImgThumbnail;
        View IconVideo;

        RecyclerViewHolder(View view) {
            super(view);
            TvTitle = view.findViewById(R.id.tv_title);
            TvDate = view.findViewById(R.id.tv_date);
            TvSource = view.findViewById(R.id.tv_source);
            ImgThumbnail = view.findViewById(R.id.img_thumb_news);
            IconVideo = view.findViewById(R.id.img_ic_video_type);
        }
    }
}
