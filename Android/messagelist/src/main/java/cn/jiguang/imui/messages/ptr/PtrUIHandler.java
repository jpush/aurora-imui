package cn.jiguang.imui.messages.ptr;


public interface PtrUIHandler {
    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    public void onUIReset(PullToRefreshLayout frame);

    /**
     * prepare for loading
     *
     * @param frame
     */
    public void onUIRefreshPrepare(PullToRefreshLayout frame);

    /**
     * perform refreshing UI
     */
    public void onUIRefreshBegin(PullToRefreshLayout frame);

    /**
     * perform UI after refresh
     */
    public void onUIRefreshComplete(PullToRefreshLayout frame);

    public void onUIPositionChange(PullToRefreshLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
}
