package com.appian.manchesterunitednews.app.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.ads.widget.FbAdRecyclerAdapter;
import com.appnet.android.football.fbvn.data.News;

import java.util.ArrayList;
import java.util.List;

class NewsRecycleAdapter extends FbAdRecyclerAdapter<News> {
    private static final int SMALL_NEWS_VIEW_TYPE = 1;
    private static final int LARGE_NEWS_VIEW_TYPE = 2;
    private static final int TOPIC_NEWS_VIEW_TYPE = 3;
    private static final int MAX_FB_ADS = 6;

    NewsRecycleAdapter(Context context, String unitId) {
        super(context, unitId, MAX_FB_ADS);
    }

    @Override
    protected int getViewType(int position) {
        News news = getItem(position);
        if (news == null) {
            return 0;
        }
        return news.getLayoutType();
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
        News item = getItem(position);
        if (item == null) {
            return;
        }
        RecyclerViewHolder itemHolder = (RecyclerViewHolder) holder;
        itemHolder.TvTitle.setText(item.getTitle());
        itemHolder.TvDate.setText(Utils.calculateTimeAgo(mContext, item.getCreatedAt()));
        itemHolder.TvCommentCount.setText(String.valueOf(item.getCommentCount()));
        ImageLoader.displayImage(item.getThumbnail(), itemHolder.ImgThumbnail);
        if(item.isVideoType()) {
            itemHolder.IconVideo.setVisibility(View.VISIBLE);
        } else {
            itemHolder.IconVideo.setVisibility(View.GONE);
        }
    }

    @Override
    public List<News> getNextItems(int position, int count) {
        List<News> data = new ArrayList<>();
        int size = getItemCount();
        int index = position + 1;
        int k = 0;
        while (index < size && k < count) {
            News item = getItem(index);
            if (item != null && !item.isVideoType()) {
                data.add(item);
                k++;
            }
            index++;
        }
        return data;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView TvTitle;
        TextView TvDate;
        TextView TvCommentCount;
        ImageView ImgThumbnail;
        View IconVideo;

        RecyclerViewHolder(View view) {
            super(view);
            TvTitle = view.findViewById(R.id.tv_title);
            TvDate = view.findViewById(R.id.tv_date);
            TvCommentCount = view.findViewById(R.id.tv_news_comment_count);
            ImgThumbnail = view.findViewById(R.id.img_thumb_news);
            IconVideo = view.findViewById(R.id.img_ic_video_type);
        }
    }
}
