package cn.jiguang.imui.chatinput.menu.collection;

import android.content.Context;
import android.util.Log;
import android.view.View;

import cn.jiguang.imui.chatinput.menu.view.MenuFeature;


public class MenuFeatureCollection extends MenuCollection {


    public MenuFeatureCollection(Context context) {
        super(context);
    }

    public void addMenuFeature(String tag, int resource) {
        View view = mInflater.inflate(resource, null);
        addMenuFeature(tag, view);
    }

    public void addMenuFeature(String tag, View menuFeature) {

        if (menuFeature instanceof MenuFeature) {
            menuFeature.setVisibility(View.GONE);
            addMenu(tag, menuFeature);
        } else {
            Log.e(TAG, "Collection menu feature failed exception!");
        }
    }

}
