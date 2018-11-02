package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.appian.manchesterunitednews.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by phuongbkatp on 10/5/2018.
 */
public class CustomVideoLayout extends LinearLayout {

    private MediaController mediacontroller;
    private ProgressBar progressBar;
    private boolean isPlaying = true;
    private ImageButton playPause;
    public CustomVideoLayout(Context context, String link) {
        super(context);
        initView(context, link);
    }

    private void initView(Context context, String link) {
        View view = inflate(context, R.layout.custom_video_layout, null);
        final VideoView videoView = view.findViewById(R.id.content_video);
        progressBar = view.findViewById(R.id.progrss);
        playPause = view.findViewById(R.id.play_or_pause);
        playPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isPlaying) {
                    playPause.setSelected(true);
                    videoView.pause();
                } else {
                    playPause.setSelected(false);
                    videoView.start();
                }
                isPlaying = !isPlaying;
            }
        });
        mediacontroller = new MediaController(getContext());
        mediacontroller.setAnchorView(videoView);
        Uri uri = Uri.parse(link);

        videoView.setVideoURI(uri);
        progressBar.setVisibility(View.VISIBLE);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });
        addView(view);
    }
}