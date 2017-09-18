package cn.jiguang.imui.messages;


import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.jiguang.imui.commons.models.IMessage;


public class ViewHolderController {

    private static ViewHolderController mInstance = new ViewHolderController();
    private HashMap<Integer, ImageView> mData = new LinkedHashMap<>();
    private int mLastPlayPosition = -1;
    private boolean mIsSender;
    private int mSendDrawable;
    private int mReceiveDrawable;
    private ReplayVoiceListener mListener;
    private IMessage mMsg;

    private ViewHolderController() {

    }

    public static ViewHolderController getInstance() {
        return mInstance;
    }

    public void addView(int position, ImageView view) {
        mData.put(position, view);
    }

    public void setLastPlayPosition(int position, boolean isSender) {
        mLastPlayPosition = position;
        mIsSender = isSender;
    }

    public int getLastPlayPosition() {
        return mLastPlayPosition;
    }

    public void notifyAnimStop() {
        ImageView imageView = mData.get(mLastPlayPosition);
        try {
            if (imageView != null) {
                AnimationDrawable anim = (AnimationDrawable) imageView.getDrawable();
                anim.stop();
                if (mIsSender) {
                    imageView.setImageResource(mSendDrawable);
                } else {
                    imageView.setImageResource(mReceiveDrawable);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void remove(int position) {
        if (null != mData && mData.size() > 0) {
            mData.remove(position);
        }
    }

    public void setMessage(IMessage message) {
        this.mMsg = message;
    }

    public IMessage getMessage() {
        return mMsg;
    }

    public void setReplayVoiceListener(ReplayVoiceListener listener) {
        this.mListener = listener;
    }

    public void replayVoice() {
        mListener.replayVoice();
    }

    public void release() {
        mData.clear();
        mData = null;
    }

    public void setDrawable(int sendDrawable, int receiveDrawable) {
        mSendDrawable = sendDrawable;
        mReceiveDrawable = receiveDrawable;
    }

    interface ReplayVoiceListener{
        public void replayVoice();
    }
}
