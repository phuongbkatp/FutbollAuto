package com.appian.manchesterunitednews.app.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.appian.manchesterunitednews.R;
import com.appian.manchesterunitednews.app.BaseFragment;
import com.appian.manchesterunitednews.app.comment.presenter.CommentsPresenter;
import com.appian.manchesterunitednews.app.comment.presenter.PostCommentPresenter;
import com.appian.manchesterunitednews.app.comment.view.CommentsView;
import com.appian.manchesterunitednews.app.comment.view.PostCommentView;
import com.appian.manchesterunitednews.data.account.AccountManager;
import com.appian.manchesterunitednews.data.account.UserAccount;
import com.appian.manchesterunitednews.data.interactor.CommentsInteractor;
import com.appian.manchesterunitednews.util.EndlessRecyclerViewScrollListener;
import com.appian.manchesterunitednews.util.ViewHelper;
import com.appnet.android.football.fbvn.data.Comment;

import java.util.List;

public class CommentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        CommentsView, PostCommentView {
    private static final int LIMIT_COMMENTS = 10;
    private static final int STARTING_PAGE = 1;

    private LinearLayout mLlNoData;
    private CommentsAdapter mAdapter;
    private String mObjectType;
    private int mObjectId;

    private CommentsPresenter mCommentsPresenter;
    private PostCommentPresenter mPostCommentPresenter;
    private int mCurrentPage;
    private EndlessRecyclerViewScrollListener mOnLoadMoreListener;

    public static CommentFragment newInstance(String objectType, int objectId) {
        Bundle args = new Bundle();
        args.putString("object_type", objectType);
        args.putInt("object_id", objectId);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CommentsAdapter(getContext());
        Bundle args = getArguments();
        if (args != null) {
            mObjectType = args.getString("object_type");
            mObjectId = args.getInt("object_id", 0);
        }
        mCurrentPage = STARTING_PAGE;
        CommentsInteractor interactor = new CommentsInteractor();
        mCommentsPresenter = new CommentsPresenter(interactor);
        mCommentsPresenter.attachView(this);
        mPostCommentPresenter = new PostCommentPresenter(interactor);
        mPostCommentPresenter.attachView(this);
        loadComments();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLlNoData = view.findViewById(R.id.ll_comment_no_data);
        final EditText edtComment = view.findViewById(R.id.edt_comment);
        View btnSendComment = view.findViewById(R.id.btn_comment_send);
        RecyclerView lvComments = view.findViewById(R.id.lv_comment);
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        lvComments.setLayoutManager(llm);
        lvComments.setAdapter(mAdapter);
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment(edtComment.getText().toString());
                edtComment.setText("");
            }
        });
        mOnLoadMoreListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page) {
                mCurrentPage++;
                loadComments();
            }
        };
        lvComments.addOnScrollListener(mOnLoadMoreListener);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_comment;
    }

    private void postComment(final String content) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(mObjectType) || mObjectId == 0) {
            return;
        }
        UserAccount userAccount = AccountManager.getInstance().getAccount(getContext());
        if (userAccount == null || TextUtils.isEmpty(userAccount.getAccessToken())) {
            ViewHelper.requestLogin(getContext());
            return;
        }
        mPostCommentPresenter.postComment(userAccount.getAuthorization(), userAccount.getId(),
                content, mObjectType, mObjectId);
    }

    private void loadComments() {
        if (TextUtils.isEmpty(mObjectType) || mObjectId == 0) {
            return;
        }
        mCommentsPresenter.loadComments(mObjectType, mObjectId, mCurrentPage, LIMIT_COMMENTS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCommentsPresenter.detachView();
        mPostCommentPresenter.detachView();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = STARTING_PAGE;
        mOnLoadMoreListener.resetState();
        loadComments();
    }

    @Override
    public void showComments(List<Comment> data) {
        if(data == null) {
            return;
        }
        if (mCurrentPage == STARTING_PAGE) {
            mAdapter.updateData(data);
        } else {
            mAdapter.addData(data);
        }
        if (getView() != null) {
            int visible = mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE;
            mLlNoData.setVisibility(visible);
        }
    }

    @Override
    public void onPostCommentSuccess() {
        onRefresh();
    }

    @Override
    public void onLoadCommentsFail() {
    }
}
