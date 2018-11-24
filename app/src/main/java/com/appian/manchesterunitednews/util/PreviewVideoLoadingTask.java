package com.appian.manchesterunitednews.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;

import java.util.HashMap;

public class PreviewVideoLoadingTask extends AsyncTask<Void, Void, Bitmap> {

    public interface OnBitmapLoaded{
        void loadBitmap(Bitmap bitmap);
    }

    private OnBitmapLoaded bitmapLoaded;
    private String url;

    public PreviewVideoLoadingTask(String url){
        this.url = url;
    }

    public PreviewVideoLoadingTask setBitMapLoaded(OnBitmapLoaded bitMapLoaded){
        this.bitmapLoaded = bitMapLoaded;
        return this;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try {
            bitmap = retriveVideoFrameFromVideo(url);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        if(bitmapLoaded != null) bitmapLoaded.loadBitmap(result);
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