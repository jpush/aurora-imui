package cn.jiguang.imui.messages;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class ScrollMoreListener extends RecyclerView.OnScrollListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private MsgListAdapter mAdapter;
    private int mCurrentPage = 0;
    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = false;
    private boolean mScrolled = false;
    private boolean mDisable = false;

    public ScrollMoreListener(LinearLayoutManager layoutManager, MsgListAdapter adapter) {
        this.mLayoutManager = layoutManager;
        mAdapter = adapter;
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i=0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy != 0) {
            mScrolled = true;
        }
        if (mAdapter != null) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = mLayoutManager.getItemCount();
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager)
                        .findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

            if (mDisable) {
                return;
            }

            if (totalItemCount < mPreviousTotalItemCount) {
                mCurrentPage = 0;
                mPreviousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    mLoading = true;
                }
            }

            if (mLoading && totalItemCount > mPreviousTotalItemCount) {
                mLoading = false;
                mPreviousTotalItemCount = totalItemCount;
            }

            int visibleThreshold = 5;
            if (!mLoading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                mCurrentPage++;
                mAdapter.onLoadMore(mCurrentPage, totalItemCount);
                mLoading = true;
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case SCROLL_STATE_IDLE:
                if (mAdapter.getScrolling() && mScrolled) {
                    mAdapter.setScrolling(false);
                    mAdapter.notifyDataSetChanged();
                }
                mScrolled = false;
                break;
            case SCROLL_STATE_DRAGGING:
//                mAdapter.setScrolling(false);
                break;
            case SCROLL_STATE_SETTLING:
//                mAdapter.setScrolling(true);
                break;
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    public void forbidScrollToRefresh(boolean disable) {
        mDisable = disable;
    }

    @Deprecated
    interface OnLoadMoreListener {
        void onLoadMore(int page, int total);
    }
}
