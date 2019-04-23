package com.appian.manchesterunitednews.util;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.appian.manchesterunitednews.R;
import com.appnet.android.ads.admob.AbstractAdMob;
import com.appnet.android.ads.admob.AdmobHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Utils {
    private static final String[] ADMOB_TEST_ID = {"779DF624C32BC4E7F95BD786CC47F69D", "04ACC18D19FD79C8D2C145D8F4AA969E", "6687c922-a9f1-407d-9333-be26b64fbd2b"};

    private static final String DATE_TIME_FORMAT_TZ = "yyyy-MM-dd'T'HH:mm:ssX";
    private static final String D_D_M_FORMAT = "EEE, dd-MM";
    private static final String HH_MM_FORMAT = "HH:mm";
    private static final String YY_MM_DD_HH_MM_FORMAT = "dd-MMM-yyyy HH:mm";
    private static final String D_M_Y_FORMAT = "dd-MM-yyyy";
    private static final String M_Y_FORMAT = "dd-MM-yyyy";

    static final int SECOND = 1000;        // no. of ms in a second
    static final int MINUTE = SECOND * 60; // no. of ms in a minute
    static final int HOUR = MINUTE * 60;   // no. of ms in an hour
    static final int DAY = HOUR * 24;      // no. of ms in a day
    static final int WEEK = DAY * 7;

    private static final Locale LOCALE_VN = new Locale("vi", "VN");

    public static String formatDate(String str) {
        Date date = dateOf(str);
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(D_D_M_FORMAT, Locale.getDefault()).format(date);
    }

    public static String formatDateTime(Date date) {
        return new SimpleDateFormat(YY_MM_DD_HH_MM_FORMAT, Locale.getDefault()).format(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(D_D_M_FORMAT, Locale.getDefault()).format(date);
    }

    public static String formatDateMonthYear(Date date) {
        return new SimpleDateFormat(D_M_Y_FORMAT, Locale.getDefault()).format(date);
    }

    public static String formatMonthYear(long time) {
        return new SimpleDateFormat(M_Y_FORMAT, Locale.getDefault()).format(new Date(time));
    }

    public static String formatTime(Date date) {
        return new SimpleDateFormat(HH_MM_FORMAT, Locale.getDefault()).format(date);
    }

    public static Date dateOf(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_TZ, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatTransferValue(final long value, final String currency) {
        String data = "";
        if (value > 1000000) {
            data = (value / 1000000) + "M " + currency;
        } else if (value > 1000) {
            data = (value / 1000) + "M " + currency;
        }
        return data;
    }

    public static String calculateRemainTime(Context context, long scheduled_date) {
        String remaining_time = "";
        // date format
        SimpleDateFormat format = new SimpleDateFormat();
        // two dates
        Date scheduledDate;
        Calendar current = Calendar.getInstance();
        Date currentDate;
        String current_date = format.format(current.getTime());
        try {
            //scheduledDate = format.parse(scheduled_date);
            currentDate = format.parse(current_date);
            long diffInMillies = scheduled_date - currentDate.getTime();
            int days = (int) (diffInMillies / DAY);
            int hours = (int) ((diffInMillies % DAY) / HOUR);
            int minutes = (int) (diffInMillies / MINUTE);
            if (days > 1) {
                remaining_time = context.getResources().getString(R.string.remaining_day, days);
            } else if (days == 1) {
                remaining_time = context.getResources().getString(R.string.remaining_time_des, days, hours);
            } else if (hours > 0) {
                remaining_time = context.getResources().getString(R.string.remaining_hours, hours);
            } else if (minutes > 1) {
                remaining_time = context.getResources().getString(R.string.remaining_minutes, minutes);
            } else {
                remaining_time = context.getResources().getString(R.string.now);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return remaining_time;
    }

    public static String calculateTimeAgo(Context context, long past_time) {
        String remaining_time = "";
        // date format
        SimpleDateFormat format = new SimpleDateFormat();
        // two dates
        Date scheduledDate;
        Calendar current = Calendar.getInstance();
        Date currentDate;
        String current_date = format.format(current.getTime());
        try {
            //scheduledDate = format.parse(past_time);
            currentDate = format.parse(current_date);
            long diffInMillies = currentDate.getTime() - past_time;
            int days = (int) (diffInMillies / DAY);
            int hours = (int) ((diffInMillies % DAY) / HOUR);
            int minutes = (int) (diffInMillies / MINUTE);
            if (days > 1) {
                remaining_time = context.getResources().getString(R.string.past_day, days);
            } else if (days == 1) {
                remaining_time = context.getResources().getString(R.string.yesterday);
            } else if (hours > 1) {
                remaining_time = context.getResources().getString(R.string.past_hours, hours);
            }else if (hours == 1) {
                remaining_time = context.getResources().getString(R.string.past_hours, hours);
            } else if (minutes > 1){
                remaining_time = context.getResources().getString(R.string.past_min, minutes);
            } else {
                remaining_time = context.getResources().getString(R.string.now);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return remaining_time;
    }

    public static String formatTime(Context context, long time)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat  inFormat = new SimpleDateFormat( "HH:mm, -, dd/MM/yyyy");
        Date date=calendar.getTime();
        String day=getNameOfDay(context, calendar.get(Calendar.DAY_OF_WEEK));
        String str=inFormat.format(date);
        str=str.replace("-",day);
        return str;

    }

    public static String formatWeekTime(Context context, long time)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat  inFormat = new SimpleDateFormat( "HH:mm, -");
        Date date=calendar.getTime();
        String day=getNameOfDay(context, calendar.get(Calendar.DAY_OF_WEEK));
        String str=inFormat.format(date);
        str=str.replace("-",day);
        return str;

    }
    public static String getNameOfDay(Context context, int index) {
        switch (index) {
        case 1:
            return context.getResources().getString(R.string.sunday);

        case 2:
            return context.getResources().getString(R.string.monday);

        case 3:
            return context.getResources().getString(R.string.tuesday);

        case 4:
            return context.getResources().getString(R.string.wednesday);

        case 5:
            return context.getResources().getString(R.string.thursday);

        case 6:
            return context.getResources().getString(R.string.friday);

        case 7:
            return context.getResources().getString(R.string.saturday);

        }
        return "";
    }

    public static String getPath(Context context, Uri uri) {
        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    public static void initAdmob(Context context) {
        AdmobHelper.initialize(context, context.getString(R.string.admob_app_id));
    }

    public static void addAdmobTestDevice(AbstractAdMob adMob) {
        for(String id : ADMOB_TEST_ID) {
            adMob.addTestDevice(id);
        }
    }
}
