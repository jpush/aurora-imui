package imui.jiguang.cn.imuisample.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PtrDefaultHeader;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.utils.DisplayUtil;
import imui.jiguang.cn.imuisample.R;

import static cn.jiguang.imui.chatinput.ChatInputView.KEYBOARD_STATE_HIDE;
import static cn.jiguang.imui.chatinput.ChatInputView.KEYBOARD_STATE_INIT;
import static cn.jiguang.imui.chatinput.ChatInputView.KEYBOARD_STATE_SHOW;


public class ChatView extends RelativeLayout {

    private TextView mTitle;
    private MessageList mMsgList;
    private ChatInputView mChatInput;
    private LinearLayout mMenuLl;
    private RecordVoiceButton mRecordVoiceBtn;
    private PullToRefreshLayout mPtrLayout;

    private boolean mHasInit;
    private boolean mHasKeyboard;
    private int mHeight;

    private OnKeyboardChangedListener mKeyboardListener;
    private OnSizeChangedListener mSizeChangedListener;

    public ChatView(Context context) {
        super(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initModule() {
        mTitle = (TextView) findViewById(R.id.title_tv);
        mMsgList = (MessageList) findViewById(R.id.msg_list);
        mMenuLl = (LinearLayout) findViewById(R.id.aurora_ll_menuitem_container);
        mChatInput = (ChatInputView) findViewById(R.id.chat_input);
        mPtrLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);

        mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
        PtrDefaultHeader header = new PtrDefaultHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.dp2px(getContext(),15), 0,
                DisplayUtil.dp2px(getContext(),10));
        header.setPtrFrameLayout(mPtrLayout);
        mMsgList = (MessageList) findViewById(R.id.msg_list);
        mMsgList.setHasFixedSize(true);
        mPtrLayout.setLoadingMinTime(1000);
        mPtrLayout.setDurationToCloseHeader(1500);
        mPtrLayout.setHeaderView(header);
        mPtrLayout.addPtrUIHandler(header);
        // 下拉刷新时，内容固定，只有 Header 变化
        mPtrLayout.setPinContent(true);
//        mMsgList.setShowReceiverDisplayName(0);
//        mMsgList.setShowSenderDisplayName(1);
    }

    public PullToRefreshLayout getPtrLayout() {
        return mPtrLayout;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mChatInput.setMenuClickListener(listener);
    }

    public void setAdapter(MsgListAdapter adapter) {
        mMsgList.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mMsgList.setLayoutManager(layoutManager);
    }

    public void setRecordVoiceFile(String path, String fileName) {
        mRecordVoiceBtn.setVoiceFilePath(path, fileName);
    }

    public void setCameraCaptureFile(String path, String fileName) {
        mChatInput.setCameraCaptureFile(path, fileName);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mRecordVoiceBtn.setRecordVoiceListener(listener);
    }

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener) {
        mChatInput.setOnCameraCallbackListener(listener);
    }

    public void setKeyboardChangedListener(OnKeyboardChangedListener listener) {
        mKeyboardListener = listener;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mSizeChangedListener = listener;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mMsgList.setOnTouchListener(listener);
    }

    public void setOnTouchEditTextListener(OnClickEditTextListener listener) {
        mChatInput.setOnClickEditTextListener(listener);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSizeChangedListener != null) {
            mSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
            if (null != mKeyboardListener) {
                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_INIT);
            }
        } else {
            if (null != mKeyboardListener) {
                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_INIT);
            }
            mHeight = mHeight < b ? b : mHeight;
        }
        if (mHasInit && mHeight > b) {
            mHasKeyboard = true;
            if (null != mKeyboardListener) {
                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_SHOW);
            }
        }
        if (mHasInit && mHasKeyboard && mHeight == b) {
            mHasKeyboard = false;
            if (null != mKeyboardListener) {
                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_HIDE);
            }
        }
    }

    public ChatInputView getChatInputView() {
        return mChatInput;
    }

    public MessageList getMessageListView() {
        return mMsgList;
    }

    public void setMenuHeight(int height) {
        mChatInput.setMenuContainerHeight(height);
    }

    /**
     * Keyboard status changed will invoke onKeyBoardStateChanged
     */
    public interface OnKeyboardChangedListener {

        /**
         * Soft keyboard status changed will invoke this callback, use this callback to do you logic.
         *
         * @param state Three states: init, show, hide.
         */
        public void onKeyBoardStateChanged(int state);
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
