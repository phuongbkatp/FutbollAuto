package com.appian.manchesterunitednews.app.comment.view;

import com.appnet.android.football.fbvn.data.Comment;

import java.util.List;

public interface CommentsView {
    void showComments(List<Comment> data);
    void onLoadCommentsFail();
}
