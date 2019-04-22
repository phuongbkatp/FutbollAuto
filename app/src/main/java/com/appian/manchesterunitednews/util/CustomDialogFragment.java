package com.appian.manchesterunitednews.util;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CustomDialogFragment extends DialogFragment {
    private String mLink;
    private static final int AUTO_DISMISS_MILLIS = 6000;
    private CountDownTimer mTimer;

    public static CustomDialogFragment newInstance(String link) {
        CustomDialogFragment frg = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("link", link);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        Resources res = getResources();
        TextView tv1 = rootView.findViewById(R.id.text1);
        tv1.setText(res.getString(R.string.disclaimer_title, res.getString(R.string.app_name).toUpperCase()));
        TextView tv2 = rootView.findViewById(R.id.text2);
        tv2.setText(res.getString(R.string.disclaimer_text_1, res.getString(R.string.app_name).toUpperCase()));
        TextView tv4 = rootView.findViewById(R.id.text4);
        tv4.setText(res.getString(R.string.disclaimer_text_2, res.getString(R.string.app_name).toUpperCase()));

        final TextView mCountDownText = rootView.findViewById(R.id.countdownTxt);
        Bundle args = getArguments();
        if(args != null) {
            mLink = args.getString("link");
        }
        rootView.findViewById(R.id.btn_watch_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mLink));
                startActivity(intent);
            }
        });
        mTimer = new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCountDownText.setText(String.format(
                        Locale.getDefault(), "(%d)",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                ));
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mLink));
                startActivity(intent);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mTimer != null) {
            mTimer.cancel();
        }
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
