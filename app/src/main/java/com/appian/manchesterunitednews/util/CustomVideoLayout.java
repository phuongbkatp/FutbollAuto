package com.appian.manchesterunitednews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
import com.marcinmoskala.videoplayview.VideoPlayView;

import java.util.HashMap;

/**
 * Created by phuongbkatp on 10/5/2018.
 */
public class CustomVideoLayout extends LinearLayout {

    Bitmap bitmap;

    public CustomVideoLayout(Context context, String link) {
        super(context);
        initView(context, link);
    }

    private void initView(Context context, String link) {
        View view = inflate(context, R.layout.custom_video_layout, null);
        VideoPlayView videoView = view.findViewById(R.id.content_video);
        try {
            bitmap = retriveVideoFrameFromVideo(link);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 240, false);
        }
        videoView.setVideoUrl(link);
        Glide.with(this).load(bitmap).into(videoView.getImageView());

        addView(view);
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}