package cn.jiguang.imui.messages;


import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class ViewHolderController {

    private static ViewHolderController mInstance = new ViewHolderController();
    private HashMap<Integer, ImageView> mData = new LinkedHashMap<>();
    private int mLastPlayPosition = -1;

    private ViewHolderController() {

    }

    public static ViewHolderController getInstance() {
        return mInstance;
    }

    public void addView(int position, ImageView view) {
        mData.put(position, view);
    }

    public void setLastPlayPosition(int position) {
        mLastPlayPosition = position;
    }

    public int getLastPlayPosition() {
        return mLastPlayPosition;
    }

    public void notifyAnimStop(int resId) {
        ImageView imageView = mData.get(mLastPlayPosition);
        try {
            if (imageView != null) {
                AnimationDrawable anim = (AnimationDrawable) imageView.getDrawable();
                anim.stop();
                imageView.setImageResource(resId);
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

    public void release() {
        mData.clear();
        mData = null;
    }

}
