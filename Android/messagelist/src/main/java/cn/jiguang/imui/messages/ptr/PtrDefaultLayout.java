package cn.jiguang.imui.messages.ptr;

import android.content.Context;
import android.util.AttributeSet;


public class PtrDefaultLayout extends PullToRefreshLayout {

    PtrDefaultHeader mPtrDefaultHeader;

    public PtrDefaultLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrDefaultLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrDefaultLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrDefaultHeader = new PtrDefaultHeader(getContext());
        setHeaderView(mPtrDefaultHeader);
        addPtrUIHandler(mPtrDefaultHeader);
    }

    public PtrDefaultHeader getHeader() {
        return mPtrDefaultHeader;
    }

}
