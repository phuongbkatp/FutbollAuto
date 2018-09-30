package com.appian.manchesterunitednews.data.interactor;

import com.appian.manchesterunitednews.data.RestfulService;
import com.appnet.android.football.fbvn.data.Comment;
import com.appnet.android.football.fbvn.data.CommentsData;
import com.appnet.android.football.fbvn.data.DefaultData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsInteractor {
    public void loadComments(String objectType, int objectId, int page, final int limit,
                             final OnResponseListener<List<Comment>> listener) {
        if (listener == null) {
            return;
        }
        Call<CommentsData> call = RestfulService.getInstance().loadComments(objectType, objectId, page, limit);
        call.enqueue(new Callback<CommentsData>() {
            @Override
            public void onResponse(Call<CommentsData> call, Response<CommentsData> response) {
                if (response.body() == null) {
                    listener.onSuccess(null);
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<CommentsData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void postComment(String authorization, int userId, String content, String objectType, int objectId,
                            final OnResponseListener<DefaultData> listener) {
        if (listener == null) {
            return;
        }
        Call<DefaultData> call = RestfulService.getInstance().postComment(authorization, userId, content, objectType, objectId);
        call.enqueue(new Callback<DefaultData>() {
            @Override
            public void onResponse(Call<DefaultData> call, Response<DefaultData> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<DefaultData> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
