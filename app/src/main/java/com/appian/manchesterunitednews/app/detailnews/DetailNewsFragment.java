package com.appian.manchesterunitednews.app.detailnews;

import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseStateFragment;
import com.appian.manchesterunitednews.app.comment.CommentsAdapter;
import com.appian.manchesterunitednews.app.detailnews.presenter.DetailNewsPresenter;
import com.appian.manchesterunitednews.app.detailnews.view.DetailNewsView;
import com.appian.manchesterunitednews.data.interactor.NewsInteractor;
import com.appian.manchesterunitednews.util.CustomImageLayout;
import com.appian.manchesterunitednews.util.CustomTableLayout;
import com.appian.manchesterunitednews.util.CustomTextView;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.fbvn.data.Cell;
import com.appnet.android.football.fbvn.data.ColumnHeader;
import com.appian.manchesterunitednews.util.MyTableViewAdapter;
import com.appnet.android.football.fbvn.data.RowHeader;
import com.appnet.android.football.fbvn.data.ContentDetailNewsAuto;
import com.appnet.android.football.fbvn.data.DetailNewsAuto;
import com.evrencoskun.tableview.TableView;

import java.util.List;

public class DetailNewsFragment extends BaseStateFragment implements DetailNewsView {
    private TextView mTvTitle;
    private TextView mTvDescription;
    private TextView mTvSource;
    private ImageView mImgThumbnail;
    private VideoView mContentVideo;
    private RelativeLayout mRlVideo;
    private ProgressBar progressBar;
    private TextView mTvTimeNews;
    private LinearLayout ll_content;

    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;

    private String link;
    private DetailNewsAuto mNews;

    private DetailNewsPresenter mPresenter;

    private CommentsAdapter mAdapter;
    private MediaController mediacontroller;
    private boolean isPlaying = true;
    private ImageButton playPause;

    @Override
    public void showNews(DetailNewsAuto news) {
        mNews = news;
        fillData();
    }

    public static DetailNewsFragment newInstance(int newsId) {
        Bundle args = new Bundle();
        args.putInt("news_id", newsId);
        DetailNewsFragment fragment = new DetailNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            link = args.getString("link");
        }
        mAdapter = new CommentsAdapter(getContext());
        mPresenter = new DetailNewsPresenter(new NewsInteractor());
        mPresenter.attachView(this);
        mPresenter.loadNewsDetail(link);

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail_article;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fillData();
    }

    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_news_detail_title);
        mTvDescription = view.findViewById(R.id.tv_news_detail_description);
        mTvSource = view.findViewById(R.id.tv_news_detail_source);
        mImgThumbnail = view.findViewById(R.id.img_news_detail_thumbnail);
        mContentVideo = view.findViewById(R.id.content_video);
        playPause = view.findViewById(R.id.play_or_pause);


        playPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isPlaying) {
                    playPause.setSelected(true);
                    mContentVideo.pause();
                }else{
                    playPause.setSelected(false);
                    mContentVideo.start();
                }
                isPlaying = !isPlaying;
            }
        });
        mRlVideo = view.findViewById(R.id.rl_video);
        progressBar = view.findViewById(R.id.progrss);
        mTvTimeNews = view.findViewById(R.id.tv_time_news);
        ll_content = view.findViewById(R.id.ll_content);
    }

    private void fillData() {
        if (mNews == null || getView() == null) {
            return;
        }
        mTvTimeNews.setText((Utils.formatTime(getContext(), mNews.getMetaDetailNewsAuto().getCreatedTime())));
        if (TextUtils.isEmpty(mNews.getMetaDetailNewsAuto().getDescription())) {
            mTvDescription.setVisibility(View.GONE);
        } else {
            mTvDescription.setVisibility(View.VISIBLE);
            mTvDescription.setText(mNews.getMetaDetailNewsAuto().getDescription());
        }
        mTvTitle.setText(mNews.getMetaDetailNewsAuto().getTitle());
        if (!TextUtils.isEmpty(mNews.getSource())) {
            mTvSource.setText(mNews.getSource());
        }
        String url = mNews.getMetaDetailNewsAuto().getVideo();
        if (!url.equals("")) {
            mRlVideo.setVisibility(View.VISIBLE);
            playVideo(url);
        } else {
            mRlVideo.setVisibility(View.GONE);
        }
        ImageLoader.displayImage(mNews.getMetaDetailNewsAuto().getImage(), mImgThumbnail);
        List<ContentDetailNewsAuto> listContent = mNews.getContentDetailNewsAuto();
        if (listContent.size() == 0) {
            return;
        }
        for (ContentDetailNewsAuto item : listContent) {
            if (item.getType() != null && item.getType().equals("text")) {
                LinearLayout textView = new CustomTextView(getContext(), item.getText(), item.isHead());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_content.addView(textView, params);
            } else if (item.getType() != null && item.getType().equals("image")) {
                if (item.getLinkImg() == null) {
                    continue;
                }
                CustomImageLayout imgLayout = new CustomImageLayout(getContext(), item.getLinkImg(), item.getText());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_content.addView(imgLayout, params);
                //ll_content.addView(new RecyclerView(getContext()));
            } else if (item.getType() != null && item.getType().equals("table")) {
                mColumnHeaderList = item.getRowHeaderList();
                mCellList = item.getCellList();
                CustomTableLayout tableLayout = new CustomTableLayout(getContext(), item.getText(), mColumnHeaderList, mCellList);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_content.addView(tableLayout, params);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void playVideo(String uriPath) {
        mediacontroller = new MediaController(getContext());
        mediacontroller.setAnchorView(mContentVideo);
        Uri uri = Uri.parse(uriPath);

        mContentVideo.setVideoURI(uri);
        progressBar.setVisibility(View.VISIBLE);
        mContentVideo.start();
        
        mContentVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
