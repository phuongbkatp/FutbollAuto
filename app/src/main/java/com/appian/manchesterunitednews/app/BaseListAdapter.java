package com.appian.manchesterunitednews.app;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mData;

    public BaseListAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        if(mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public T getItem(int i) {
        if(mData == null) {
            return null;
        }
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
