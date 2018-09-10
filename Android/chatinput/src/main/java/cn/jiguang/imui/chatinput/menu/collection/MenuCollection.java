package cn.jiguang.imui.chatinput.menu.collection;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;

import cn.jiguang.imui.chatinput.utils.SimpleCommonUtils;


public class MenuCollection extends HashMap<String, View> {

    public static final String TAG = SimpleCommonUtils.formatTag(MenuCollection.class.getSimpleName());
    protected Context mContext;
    protected LayoutInflater mInflater;


    public MenuCollection(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    protected void addMenu(String menuTag, View menu) {
        if(TextUtils.isEmpty(menuTag)){
            Log.e(TAG, "Collection custom menu failed,tag is empty.");
            return;
        }

        if (containsKey(menuTag)) {
            Log.e(TAG, "Collection custom menu failed,Tag " + menuTag + " has been used already！");
            return;
        }
        menu.setTag(menuTag);
        if (mMenuCollectionChangedListener != null) {
            mMenuCollectionChangedListener.addMenu(menuTag, menu);
        }

        this.put(menuTag, menu);

    }

    private MenuCollectionChangedListener mMenuCollectionChangedListener;

    public void setMenuCollectionChangedListener(MenuCollectionChangedListener menuCollectionChangedListener) {
        this.mMenuCollectionChangedListener = menuCollectionChangedListener;
    }

    public interface MenuCollectionChangedListener {
        void addMenu(String menuTag, View menu);
    }


}
