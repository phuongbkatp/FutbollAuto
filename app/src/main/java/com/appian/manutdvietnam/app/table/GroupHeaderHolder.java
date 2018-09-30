package com.appian.manutdvietnam.app.table;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appian.manutdvietnam.R;

class GroupHeaderHolder extends RecyclerView.ViewHolder {
    TextView TvGroup;

    GroupHeaderHolder(View view) {
        super(view);
        this.TvGroup = view.findViewById(R.id.tvGroupName);
    }
}
