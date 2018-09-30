package com.appian.manutdvietnam.app.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manutdvietnam.R;
import com.appian.manutdvietnam.app.BaseListAdapter;
import com.appian.manutdvietnam.util.ImageLoader;
import com.appnet.android.football.fbvn.data.LeagueSeason;

import java.util.List;

public class NavigationListAdapter extends BaseListAdapter<LeagueSeason> {

    public NavigationListAdapter(Context context, List<LeagueSeason> data) {
        super(context, data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_navigation_drawer, null);
            holder = new Holder();
            holder.icon = view.findViewById(R.id.img_navigation_item);
            holder.title = view.findViewById(R.id.tv_navigation_item);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        LeagueSeason item = mData.get(i);
        ImageLoader.displayImage(item.getLeagueLogo(), holder.icon);
        holder.icon.setColorFilter(ContextCompat.getColor(mContext, R.color.navigation_icon_color), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.title.setText(item.getLeagueName());

        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private static class Holder {
        ImageView icon;
        TextView title;
    }
}
