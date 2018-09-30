package com.appian.manchesterunitednews.app.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseRecyclerViewAdapter;
import com.appian.manchesterunitednews.util.ImageLoader;
import com.appian.manchesterunitednews.util.Utils;
import com.appnet.android.football.fbvn.data.Comment;
import com.appnet.android.football.fbvn.data.User;
import com.appnet.android.football.fbvn.data.UserProfile;

import java.util.Date;
import java.util.List;

public class CommentsAdapter extends BaseRecyclerViewAdapter<Comment, CommentsAdapter.CommentViewHolder> {

    public CommentsAdapter(Context context) {
        super(context);
    }

    @Override
    @NonNull
    public CommentsAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentViewHolder viewHolder, int position) {
        Comment item = mData.get(position);
        User user = item.getUser();
        if(user != null) {
            String displayName = user.getFullName();
            if(TextUtils.isEmpty(displayName) || TextUtils.isEmpty(displayName.trim())) {
                if(!TextUtils.isEmpty(user.getFirstName()) || !TextUtils.isEmpty(user.getLastName())) {
                    displayName = user.getFirstName() + " " + user.getLastName();
                } else if(!TextUtils.isEmpty(user.getEmail())) {
                    String email = user.getEmail();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < email.length(); ++i) {
                        if(email.charAt(i) == '@') {
                            break;
                        }
                        sb.append(email.charAt(i));
                    }
                    displayName = sb.toString();
                } else {
                    displayName = "user_"+user.getId();
                }
            }
            viewHolder.TvUserName.setText(displayName);
        }
        UserProfile profile = item.getUserProfile();
        if(profile != null && profile.getAvatar() != null) {
            ImageLoader.displayImage(profile.getAvatar().getSmallThumb(), viewHolder.ImgUser, R.drawable.profile_pic);
        }
        viewHolder.TvCommentContent.setText(item.getContent());
        viewHolder.TvCommentTime.setText(Utils.formatDateTime(new Date(item.getCreatedAt())));
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public void updateData(List<Comment> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void addData(List<Comment> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView ImgUser;
        TextView TvUserName;
        TextView TvCommentContent;
        TextView TvCommentTime;

        CommentViewHolder(View view) {
            super(view);
            ImgUser = view.findViewById(R.id.img_profile_pic);
            TvUserName = view.findViewById(R.id.tv_comment_user_name);
            TvCommentContent = view.findViewById(R.id.tv_comment_content);
            TvCommentTime = view.findViewById(R.id.tv_comment_created_time);
        }
    }
}
