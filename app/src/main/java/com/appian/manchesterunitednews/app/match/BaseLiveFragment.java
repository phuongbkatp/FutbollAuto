package com.appian.manchesterunitednews.app.match;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.appian.manchesterunitednews.app.BaseStateFragment;

import java.lang.ref.WeakReference;

public abstract class BaseLiveFragment extends BaseStateFragment {
    private static final int TIME_LIVE = 30000;
    private static final int MSG_LIVE = 1;

    // Live
    private boolean mIsLive;
    private Handler mLiveHandler;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLive();
    }

    private void initLive() {
        mIsLive = false;
        mLiveHandler = new LiveHandler(this);
    }

    protected void onLive() {

    }

    protected void onLiveStopped() {

    }

    protected int getTimeLive() {
        return TIME_LIVE;
    }

    protected void stopLive() {
        mLiveHandler.removeMessages(MSG_LIVE);
        mIsLive = false;
        onLiveStopped();
    }

    protected void startLive() {
        if(mIsLive) {
            return;
        }
        mLiveHandler.sendEmptyMessageDelayed(MSG_LIVE, getTimeLive());
    }

    protected boolean isLive() {
        return mIsLive;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLiveHandler.removeMessages(MSG_LIVE);
        mIsLive = false;
        mLiveHandler = null;
    }

    private static class LiveHandler extends Handler {
        private final WeakReference<BaseLiveFragment> mPref;

        LiveHandler(BaseLiveFragment fragment) {
            super(Looper.getMainLooper());
            mPref = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseLiveFragment fragment = mPref.get();
            if(mPref.get() == null) {
                return;
            }
            switch (msg.what) {
                case MSG_LIVE:
                    fragment.mIsLive = true;
                    fragment.onLive();
                    sendEmptyMessageDelayed(MSG_LIVE, fragment.getTimeLive());
                    break;
            }
        }
    }
}
