package com.appian.manchesterunitednews.service.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.appian.manchesterunitednews.R;

public class NotificationProvider {
    private NotificationCompat.Builder mBuilder;
    private Intent mIntent;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private int mId = 0;

    public NotificationProvider(Context context) {
        mContext = context;
        init();
    }

    private void init() {

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if(mNotificationManager == null) {
            return;
        }
        String name = "daily_news_channel";
        String id = "daily_news_channel_id"; // The user-visible name of the channel.
        String description = "daily_news_channel_first_channel"; // The user-visible description of the channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = mNotificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(mChannel);
            }
            mBuilder = new NotificationCompat.Builder(mContext, id);
        } else {
            mBuilder = new NotificationCompat.Builder(mContext, id);
        }
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(true);
        mIntent = new Intent();
    }

    public void setTitle(String title) {
        mBuilder.setContentTitle(title);
    }

    public void setText(String text) {
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        mBuilder.setContentText(text);
    }

    public void putExtra(String key, int value) {
        mIntent.putExtra(key, value);
    }

    public void putExtra(String key, int[] value) {
        mIntent.putExtra(key, value);
    }

    public void putExtra(String key, String value) {
        mIntent.putExtra(key, value);
    }

    public void putExtra(String key, long value) {
        mIntent.putExtra(key, value);
    }

    public void putExtra(String key, float value) {
        mIntent.putExtra(key, value);
    }

    public void putExtra(String key, boolean value) {
        mIntent.putExtra(key, value);
    }

    public void setClass(Class<?> cls) {
        mIntent.setClass(mContext, cls);
    }

    public void setAction(String action) {
        mIntent.setAction(action);
    }

    public void setData(Uri uri) {
        mIntent.setData(uri);
    }

    public void setId(int id) {
        mId = id;
    }

    public Context getContext() {
        return mContext;
    }

    public void pushNotify() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mContext);
        taskStackBuilder.addNextIntentWithParentStack(mIntent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
